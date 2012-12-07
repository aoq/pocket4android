/*
 * Copyright 2012 Yu AOKI
 */

package com.aokyu.dev.pocket.util;

import com.aokyu.dev.pocket.ConsumerKey;

public class PocketUtils {

    private static final String APP_ID_PREFIX = "pocketapp";
    private static final String REDIRECT_URI_SUFFIX = ":authorizationFinished";

    public static String getAppId(ConsumerKey consumerKey) {
        String key = consumerKey.get();
        String appId = null;
        if (key != null) {
            String[] elements = key.split("-");
            appId = APP_ID_PREFIX + elements[0];
        }
        return appId;
    }

    public static String getRedirectUri(ConsumerKey consumerKey) {
        String appId = getAppId(consumerKey);
        String redirectUri = null;
        if (appId != null) {
            redirectUri = appId + REDIRECT_URI_SUFFIX;
        }
        return redirectUri;
    }
}
