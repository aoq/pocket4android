/*
 * Copyright 2012 Yu AOKI
 */

package com.aokyu.dev.pocket;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.json.JSONException;
import org.json.JSONObject;

import com.aokyu.dev.pocket.util.JSONUtils;

public class RequestToken implements Serializable {

    private static final long serialVersionUID = -9155567621462690980L;

    public static final String KEY_CODE = "code";
    private static final String ENCODE = "UTF-8";

    private String mToken;

    public RequestToken(String token) {
        mToken = token;
    }

    RequestToken(JSONObject jsonObj) throws JSONException {
        String[] keys = JSONUtils.getKeys(jsonObj);
        int size = keys.length;
        for (int i = 0; i < size; i++) {
            String key = keys[i];
            if (key.equals(KEY_CODE)) {
                mToken = jsonObj.getString(KEY_CODE);
                continue;
            }
        }
    }

    public String get() {
        return mToken;
    }

    public String getUrlEncoded() {
        String encoded = null;
        try {
            encoded = URLEncoder.encode(mToken, ENCODE);
        } catch (UnsupportedEncodingException e) {
            encoded = mToken;
        }
        return encoded;
    }
}
