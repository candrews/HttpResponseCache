/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.integralblue.httpresponsecache.compat.libcore.net.http;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * Best-effort parser for HTTP dates.
 */
public final class HttpDate {
    private static final int RFC1123_PARSER_POOL_SIZE = 10;

    /**
     * Most websites serve cookies in the blessed format. Eagerly create the parser to ensure such
     * cookies are on the fast path.
     */
    static final DateFormatPoolRfc1123 sParserPoolRfc1123 = new DateFormatPoolRfc1123(RFC1123_PARSER_POOL_SIZE);

    /**
     * If we fail to parse a date in a non-standard format, try each of these formats in sequence.
     */
    private static final String[] BROWSER_COMPATIBLE_DATE_FORMATS = new String[] {
            /* This list comes from  {@code org.apache.http.impl.cookie.BrowserCompatSpec}. */
            "EEEE, dd-MMM-yy HH:mm:ss zzz", // RFC 1036
            "EEE MMM d HH:mm:ss yyyy", // ANSI C asctime()
            "EEE, dd-MMM-yyyy HH:mm:ss z",
            "EEE, dd-MMM-yyyy HH-mm-ss z",
            "EEE, dd MMM yy HH:mm:ss z",
            "EEE dd-MMM-yyyy HH:mm:ss z",
            "EEE dd MMM yyyy HH:mm:ss z",
            "EEE dd-MMM-yyyy HH-mm-ss z",
            "EEE dd-MMM-yy HH:mm:ss z",
            "EEE dd MMM yy HH:mm:ss z",
            "EEE,dd-MMM-yy HH:mm:ss z",
            "EEE,dd-MMM-yyyy HH:mm:ss z",
            "EEE, dd-MM-yyyy HH:mm:ss z",

            /* RI bug 6641315 claims a cookie of this format was once served by www.yahoo.com */
            "EEE MMM d yyyy HH:mm:ss z",
    };

    /**
     * Returns the date for {@code value}. Returns null if the value couldn't be
     * parsed.
     */
    public static Date parse(String value) {
        DateFormat parser = null;

        try {
            parser = sParserPoolRfc1123.take();
            return parser.parse(value);
        } catch (ParseException ignore) {
        } finally {
            sParserPoolRfc1123.put(parser);
        }
        for (String formatString : BROWSER_COMPATIBLE_DATE_FORMATS) {
            try {
                return new SimpleDateFormat(formatString, Locale.US).parse(value);
            } catch (ParseException ignore) {
            }
        }
        return null;
    }

    /**
     * Returns the string for {@code value}.
     */
    public static String format(Date value) {
        DateFormat formatter = sParserPoolRfc1123.take();
        String formattedString = formatter.format(value);
        sParserPoolRfc1123.put(formatter);
        return formattedString;
    }

    /** Class that creates a pool of RFC1123 DataFormat objects and allows taking, and returning those objects. */
    public static final class DateFormatPoolRfc1123 {

        private final BlockingQueue<DateFormat> mObjects;

        public DateFormatPoolRfc1123(int capacity) {
            mObjects = new ArrayBlockingQueue<DateFormat>(capacity, false);

            // Create a thread to populate the pool of date parsers.
            new Thread(new Runnable() {
                public void run() {
                    TimeZone timeZone = TimeZone.getTimeZone("UTC");

                    for (int i = 0; i < RFC1123_PARSER_POOL_SIZE; i++) {
                        SimpleDateFormat parserRfc1123 = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz", Locale.US);
                        parserRfc1123.setTimeZone(timeZone);
                        sParserPoolRfc1123.put(parserRfc1123);
                    }
                }
            }).start();
        }

        /**
         * Take a DateFormat object from the queue.
         * This will block until a DateFormat object is available.
         * When done using it be sure to call {@code #put(DateFormat)}}.
         */
        public DateFormat take() {
            try {
                return mObjects.take();
            } catch (InterruptedException e) {
                // This will never be interrupted
                return null;
            }
        }

        /**
         * Adds an object back onto the queue.
         * This will block if the queue is already full.
         */
        public void put(DateFormat dateFormat) {
            try {
                if (dateFormat != null) {
                    mObjects.put(dateFormat);
                }
            } catch (InterruptedException e) {
                // This will never be interrupted
            }
        }
    }
}
