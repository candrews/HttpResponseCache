package com.integralblue.httpresponsecache.compat;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

public class URLs {
    public static final int getEffectivePort(URL url) {
        return URIs.getEffectivePort(url.getProtocol(), url.getPort());
    }

    /**
     * Encodes this URL to the equivalent URI after escaping characters that are
     * not permitted by URI.
     *
     * @param url
     * @return
     * @throws URISyntaxException
     */
    public static final URI toURILenient(URL url) throws URISyntaxException {
        // URL.toURILenient() is not part of the Java API (through 6)
        // there doesn't seem to be a way to recreate this function using
        // the public API, so I'm hoping that simply using
        // toURI() works well enough.
        return url.toURI();
    }

}
