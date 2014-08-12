/*
 * Copyright (c) 2012-2014 Yu AOKI
 *
 * This software may be modified and distributed under the terms
 * of the MIT license.  See the LICENSE file for details.
 */

package com.aokyu.dev.pocket.http;

import java.net.URL;

public class HttpRequest {

    private final HttpMethod mMethod;
    private final URL mUrl;
    private final HttpHeaders mHeaders;
    private final MessageBody mMessageBody;

    public HttpRequest(HttpMethod method, URL url,
            HttpHeaders requestHeaders, MessageBody messageBody) {
        mMethod = method;
        mUrl = url;
        mHeaders = requestHeaders;
        mMessageBody = messageBody;
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

    public MessageBody getBody() {
        return mMessageBody;
    }
}
