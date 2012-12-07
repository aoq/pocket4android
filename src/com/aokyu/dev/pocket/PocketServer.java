/*
 * Copyright 2012 Yu AOKI
 */

package com.aokyu.dev.pocket;

import com.aokyu.dev.pocket.util.PocketUtils;


public class PocketServer {

    private static final String SERVER_URL = "https://getpocket.com";
    private static final String REDIRECT_FORMAT = "/auth/authorize?request_token=%s&redirect_uri=%s";

    public enum RequestType {

        OAUTH_REQUEST("/v3/oauth/request"),
        OAUTH_AUTHORIZE("/v3/oauth/authorize"),
        ADD("/v3/add"),
        MODIFY("/v3/send"),
        RETRIEVE("/v3/get");

        private String mEndpoint;

        private RequestType(String endpoint) {
            mEndpoint = endpoint;
        }

        private String getEndpoint() {
            return mEndpoint;
        }
    }

    public static String getEndpoint(RequestType type) {
        return SERVER_URL + type.getEndpoint();
    }

    public static String getRedirectUrl(ConsumerKey consumerKey, RequestToken requestToken) {
        String format = SERVER_URL + REDIRECT_FORMAT;
        String redirectUrl =
                String.format(format, requestToken.get(), PocketUtils.getRedirectUri(consumerKey));
        return redirectUrl;
    }
}
