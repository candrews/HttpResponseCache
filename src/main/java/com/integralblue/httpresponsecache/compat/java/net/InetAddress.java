package com.integralblue.httpresponsecache.compat.java.net;

import java.util.regex.Pattern;

public class InetAddress {
	private static final Pattern IPV4_PATTERN = Pattern.compile("\\A(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)(\\.(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)){3}\\z");
	private static final Pattern IPV6_HEX4DECCOMPRESSED_PATTERN = Pattern.compile("\\A((?:[0-9A-Fa-f]{1,4}(?::[0-9A-Fa-f]{1,4})*)?) ::((?:[0-9A-Fa-f]{1,4}:)*)(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)(\\.(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)){3}\\z");
	private static final Pattern IPV6_6HEX4DEC_PATTERN = Pattern.compile("\\A((?:[0-9A-Fa-f]{1,4}:){6,6})(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)(\\.(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)){3}\\z");
	private static final Pattern IPV6_HEXCOMPRESSED_PATTERN = Pattern.compile("\\A((?:[0-9A-Fa-f]{1,4}(?::[0-9A-Fa-f]{1,4})*)?)::((?:[0-9A-Fa-f]{1,4}(?::[0-9A-Fa-f]{1,4})*)?)\\z");
	private static final Pattern IPV6_PATTERN = Pattern.compile("\\A(?:[0-9a-fA-F]{1,4}:){7}[0-9a-fA-F]{1,4}\\z");

	
    /**
     * Returns true if the string is a valid numeric IPv4 or IPv6 address (such as "192.168.0.1").
     * This copes with all forms of address that Java supports, detailed in the {@link InetAddress}
     * class documentation.
     *
     * @hide used by frameworks/base to ensure that a getAllByName won't cause a DNS lookup.
     */
    public static boolean isNumeric(String address) {
    	return IPV4_PATTERN.matcher(address).matches()
    			|| IPV6_HEX4DECCOMPRESSED_PATTERN.matcher(address).matches()
    			|| IPV6_6HEX4DEC_PATTERN.matcher(address).matches()
    			|| IPV6_HEXCOMPRESSED_PATTERN.matcher(address).matches()
    			|| IPV6_PATTERN.matcher(address).matches();
    }
}
