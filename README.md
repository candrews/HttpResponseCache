HttpResponseCache
=================

The HttpResponseCache library provides transparent and automatic caching of HTTP and HTTPS requests that use the java.net.HttpUrlConnection classes.

For information on how to use HttpUrlConnection, refer to the (Android documentation)[https://developer.android.com/reference/java/net/HttpURLConnection.html] - don't worry, the information also applies to non-Android Java.

Requirements
------------
This library will work on Java 5 and later, and (theoretically) any version of Android (it has only been tested back to 1.5).

Usage
-----
When using Maven (or any of the dependency management systems that use Maven's library, such as Ivy), simply add the dependency reference. The library, and its dependencies, are in Maven Central: http://mvnrepository.com/artifact/com.integralblue/httpresponsecache
For example, for a Maven pom.xml:

```xml
<dependency>
  <groupId>com.integralblue</groupId>
  <artifactId>httpresponsecache</artifactId>
  <version>1.3</version>
</dependency>
```

If Maven is not in use, such as when using a non-Maven Android project, include the httpresponsecache jar and its dependency, [DiskLruCache](https://github.com/JakeWharton/DiskLruCache).

In the application, before making any HTTP(s) requests, simply call:

```java
com.integralblue.httpresponsecache.HttpResponseCache.install(File directory, long maxSize);
```

Where directory is the directory to hold cache data, and maxSize is the maximum size of the cache in bytes.

On Android, it may be desirable to use Android 4.0 and higher's built in [HttpResponseCache](https://developer.android.com/reference/android/net/http/HttpResponseCache.html) and fall back to this library on older versions of Android. This code would do that:

```java
final long httpCacheSize = 10 * 1024 * 1024; // 10 MiB
final File httpCacheDir = new File(getCacheDir(), "http");
try {
    Class.forName("android.net.http.HttpResponseCache")
        .getMethod("install", File.class, long.class)
        .invoke(null, httpCacheDir, httpCacheSize);
} catch (Exception httpResponseCacheNotAvailable) {
    Ln.d(httpResponseCacheNotAvailable, "android.net.http.HttpResponseCache not available, probably because we're running on a pre-ICS version of Android. Using com.integralblue.httpresponsecache.HttpHttpResponseCache.");
    try{
        com.integralblue.httpresponsecache.HttpResponseCache.install(httpCacheDir, httpCacheSize);
    }catch(Exception e){
        Ln.e(e, "Failed to set up com.integralblue.httpresponsecache.HttpResponseCache");
    }
}
```

Note that this library may be more up to date and have more bug fixes / features / performance improvements than the version included with the Android platform being targeted. For example, this library includes some performance improvements that are in Jelly Bean (4.1) but not in ICS (4.0). So if you want users on ICS to get these performance improvements, always use this library, and do not conditionally use the one from Android. In other words, you may want to always simply use:

```java
com.integralblue.httpresponsecache.HttpResponseCache.install(httpCacheDir, httpCacheSize);
```

and not use the conditional code above.

License
-------
This library is licensed under the terms of the Apache License, Version 2.0. You may obtain a copy of the license at http://www.apache.org/licenses/LICENSE-2.0

Origin/Contributors
-------------------
This library consists of code copied from the Android Open Source Project (AOSP). android.net.http.HttpResponseCache was copied, along with all of its non-standard-Java dependencies, to a new namespace and then modified to work under standard Java. Special thanks for Jesse Wilson for writing android.net.http.HttpResponseCache and assisting in the creation of this library. Also special thanks to Jake Wharton for porting [DiskLruCache](https://github.com/JakeWharton/DiskLruCache) from AOSP, greatly reducing the amount of work necessary to create this library.
