/*
 * Copyright (c) 2012-2014 Yu AOKI
 *
 * This software may be modified and distributed under the terms
 * of the MIT license.  See the LICENSE file for details.
 */

package com.aokyu.pocket.util;

import com.aokyu.pocket.ConsumerKey;

public final class PocketUtils {

    private static final String APP_ID_PREFIX = "pocketapp";
    private static final String REDIRECT_URI_SUFFIX = ":authorizationFinished";

    private PocketUtils() {}

    public static String getAppId(ConsumerKey consumerKey) {
        String key = consumerKey.getKey();
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
