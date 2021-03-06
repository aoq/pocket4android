/*
 * Copyright (c) 2012-2014 Yu AOKI
 *
 * This software may be modified and distributed under the terms
 * of the MIT license.  See the LICENSE file for details.
 */

package com.aokyu.pocket;

import com.aokyu.pocket.error.PocketException;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * TODO: This class should be replaced with a class using a JSON-POJO mapper.
 */
public class AccessToken {

    private static final String KEY_TOKEN = "access_token";
    private static final String KEY_USERNAME = "username";
    private static final String ENCODE = "UTF-8";

    private String mToken;
    private String mUsername;

    public AccessToken(String token, String username) {
        mToken = token;
        mUsername = username;
    }

    /* package */ AccessToken(String response) {
        String[] elements = response.split("&");
        mToken = obtainParameter(elements, KEY_TOKEN);
        mUsername = obtainParameter(elements, KEY_USERNAME);
    }

    /* package */ AccessToken(JSONObject jsonObj) throws PocketException {
        try {
            if (!jsonObj.isNull(KEY_TOKEN)) {
                mToken = jsonObj.getString(KEY_TOKEN);
            }

            if (!jsonObj.isNull(KEY_USERNAME)) {
                mUsername = jsonObj.getString(KEY_USERNAME);
            }
        } catch (JSONException e) {
            throw new PocketException();
        }

        if (mToken == null || mUsername == null) {
            throw new PocketException();
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

    public String getToken() {
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
