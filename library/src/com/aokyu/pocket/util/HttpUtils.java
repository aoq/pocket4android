/*
 * Copyright (c) 2012-2014 Yu AOKI
 *
 * This software may be modified and distributed under the terms
 * of the MIT license.  See the LICENSE file for details.
 */

package com.aokyu.pocket.util;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.SocketAddress;

import android.text.TextUtils;

public final class HttpUtils {

    private static final String USER_AGENT = "http.agent";
    private static final String PROXY_HOST = "http.proxyHost";
    private static final String PROXY_PORT = "http.proxyPort";

    private HttpUtils() {}

    public static String getDefaultUserAgent() {
        String userAgent = System.getProperty(USER_AGENT);
        if (TextUtils.isEmpty(userAgent)) {
            userAgent = null;
        }
        return userAgent;
    }

    public static Proxy getDefaultProxy() {
        String host = getDefaultHost();
        if (host == null) {
            return null;
        } else {
            int port = getDefaultPort();
            if (port != -1) {
                SocketAddress address = new InetSocketAddress(host, port);
                return new Proxy(Proxy.Type.HTTP, address);
            } else {
                return null;
            }
        }
    }

    public static String getDefaultHost() {
        String host = System.getProperty(PROXY_HOST);
        if (TextUtils.isEmpty(host)) {
            host = null;
        }
        return host;
    }

    public static int getDefaultPort() {
        int port = -1;
        try {
            port = Integer.parseInt(System.getProperty(PROXY_PORT));
        } catch (NumberFormatException e) {
            return -1;
        }
        return port;
    }
}
