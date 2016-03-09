/*
 * Copyright (c) 2015 Yu AOKI
 *
 * This software may be modified and distributed under the terms
 * of the MIT license. See the LICENSE file for details.
 */

package com.aokyu.pocket.http;

/**
 * The mandatory headers for {@link HttpClient}.
 */
public final class HttpHeader {

    public static final String HOST = "Host";
    public static final String CONTENT_TYPE = "Content-Type";
    public static final String COOKIE = "Cookie";
    public static final String LOCATION = "Location";

    public static final String X_ACCEPT = "X-Accept";
    public static final String X_ERROR_CODE = "X-Error-Code";
    public static final String X_ERROR = "X-Error";

    private HttpHeader() {}
}
