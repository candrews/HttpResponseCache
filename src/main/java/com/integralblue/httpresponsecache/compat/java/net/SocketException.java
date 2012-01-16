package com.integralblue.httpresponsecache.compat.java.net;

public class SocketException extends java.net.SocketException {

    public SocketException() {
        super();
    }

    public SocketException(String detailMessage) {
        super(detailMessage);
    }

    public SocketException(String detailMessage, Throwable cause) {
        super(detailMessage + "\n" + cause.toString());
    }
}
