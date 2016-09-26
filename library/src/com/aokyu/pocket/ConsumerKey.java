/*
 * Copyright (c) 2012-2014 Yu AOKI
 *
 * This software may be modified and distributed under the terms
 * of the MIT license.  See the LICENSE file for details.
 */

package com.aokyu.pocket;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * This class holds the consumer key.
 */
public final class ConsumerKey {

    private static final String ENCODE = "UTF-8";

    private final String mKey;

    public ConsumerKey(String key) {
        mKey = key;
    }

    public String getKey() {
        return mKey;
    }

    public String getUrlEncodedKey() {
        String encoded = null;
        try {
            encoded = URLEncoder.encode(mKey, ENCODE);
        } catch (UnsupportedEncodingException e) {
            encoded = mKey;
        }
        return encoded;
    }

}
