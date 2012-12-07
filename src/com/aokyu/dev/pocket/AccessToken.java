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

public class AccessToken implements Serializable {

    private static final long serialVersionUID = 5522663727862182215L;

    public static final String KEY_TOKEN = "access_token";
    public static final String KEY_USERNAME = "username";
    private static final String ENCODE = "UTF-8";

    private String mToken;
    private String mUsername;

    public AccessToken(String token, String username) {
        mToken = token;
        mUsername = username;
    }

    /* package */ AccessToken(String response) {
        // String#split() is available in JDK1.4 or later.
        String[] elements = response.split("&");
        mToken = obtainParameter(elements, KEY_TOKEN);
        mUsername = obtainParameter(elements, KEY_USERNAME);
    }

    /* package */ AccessToken(JSONObject jsonObj) throws JSONException {
        String[] keys = JSONUtils.getKeys(jsonObj);
        int size = keys.length;
        for (int i = 0; i < size; i++) {
            String key = keys[i];
            if (key.equals(KEY_TOKEN)) {
                mToken = jsonObj.getString(KEY_TOKEN);
                continue;
            } else if (key.equals(KEY_USERNAME)) {
                mUsername = jsonObj.getString(KEY_USERNAME);
                continue;
            }
        }
    }

    private String obtainParameter(String[] elements, String key) {
        String value = null;
        for (String element : elements) {
            if (element.startsWith(key + '=')) {
                value = element.split("=")[1].trim();
                break;
            }
        }
        return value;
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

    public String getUsername() {
        return mUsername;
    }
}
