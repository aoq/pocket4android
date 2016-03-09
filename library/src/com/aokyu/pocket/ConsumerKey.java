/*
 * Copyright (c) 2012-2014 Yu AOKI
 *
 * This software may be modified and distributed under the terms
 * of the MIT license.  See the LICENSE file for details.
 */

package com.aokyu.pocket;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public final class ConsumerKey {

    public static final String KEY = "consumer_key";
    private static final String ENCODE = "UTF-8";

    private String mConsumerKey;

    public ConsumerKey(String consumerKey) {
        mConsumerKey = consumerKey;
    }

    public String get() {
        return mConsumerKey;
    }

    public String getUrlEncoded() {
        String encoded = null;
        try {
            encoded = URLEncoder.encode(mConsumerKey, ENCODE);
        } catch (UnsupportedEncodingException e) {
            encoded = mConsumerKey;
        }
        return encoded;
    }

}
