package com.integralblue.httpresponsecache.compat;

import java.net.URLStreamHandler;
import java.net.URLStreamHandlerFactory;

import libcore.net.http.HttpHandler;
import libcore.net.http.HttpsHandler;


public class URLStreamHandlerFactoryImpl implements URLStreamHandlerFactory {

    public URLStreamHandler createURLStreamHandler(String protocol) {
        if (protocol.equals("http")) {
            return new HttpHandler();
        } else if (protocol.equals("https")) {
            return new HttpsHandler();
        }
        return null;
    }

}
