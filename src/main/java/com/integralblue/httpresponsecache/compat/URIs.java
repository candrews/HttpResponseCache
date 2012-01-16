package com.integralblue.httpresponsecache.compat;

import java.net.URI;

public class URIs {
    public static final int getEffectivePort(URI uri) {
        return getEffectivePort(uri.getScheme(), uri.getPort());
    }

    /**
     * Returns the port to use for {@code scheme} connections will use when
     * {@link #getPort} returns {@code specifiedPort}.
     *
     * @hide
     */
    public static int getEffectivePort(String scheme, int specifiedPort) {
        if (specifiedPort != -1) {
            return specifiedPort;
        }

        if ("http".equalsIgnoreCase(scheme)) {
            return 80;
        } else if ("https".equalsIgnoreCase(scheme)) {
            return 443;
        } else {
            return -1;
        }
    }

}
