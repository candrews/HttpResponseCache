package com.integralblue.httpresponsecache.compat;

import java.util.logging.Logger;

public final class System {
    private final static Logger LOGGER = Logger.getLogger(System.class.getName());

    public static final void logW(String msg){
        LOGGER.warning(msg);
    }
    
    public static void logI(String message, Throwable th) {
    	LOGGER.info(message + ": " + th);
    }
}
