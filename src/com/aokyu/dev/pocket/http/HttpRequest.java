/*
 * Copyright 2012 Yu AOKI
 */

package com.aokyu.dev.pocket.http;

import java.net.URL;

public class HttpRequest {

    private final HttpMethod mMethod;
    private final URL mUrl;
    private final HttpHeaders mHeaders;
    private final HttpParameters mParameters;

    public HttpRequest(HttpMethod method, URL url,
            HttpHeaders requestHeaders, HttpParameters parameters) {
        mMethod = method;
        mUrl = url;
        mHeaders = requestHeaders;
        mParameters = parameters;
    }

    public HttpMethod getMethod() {
        return mMethod;
    }

    public String getMethodAsString() {
        return mMethod.name();
    }

    public URL getUrl() {
        return mUrl;
    }

    public String getHost() {
        return mUrl.getHost();
    }

    public HttpHeaders getHeaders() {
        return mHeaders;
    }

    public HttpParameters getParameters() {
        return mParameters;
    }
}
