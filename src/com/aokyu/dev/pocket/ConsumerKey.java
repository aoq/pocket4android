/*
 * Copyright 2012 Yu AOKI
 */

package com.aokyu.dev.pocket;

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
