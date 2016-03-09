/*
 * Copyright (c) 2012-2014 Yu AOKI
 *
 * This software may be modified and distributed under the terms
 * of the MIT license.  See the LICENSE file for details.
 */

package com.aokyu.pocket;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.json.JSONException;
import org.json.JSONObject;

import com.aokyu.pocket.error.PocketException;

public class RequestToken implements Serializable {

    private static final long serialVersionUID = -9155567621462690980L;

    private static final String KEY_CODE = "code";
    private static final String ENCODE = "UTF-8";

    private String mToken;

    public RequestToken(String token) {
        mToken = token;
    }

    /* package */ RequestToken(JSONObject jsonObj) throws PocketException {
        try {
            if (!jsonObj.isNull(KEY_CODE)) {
                mToken = jsonObj.getString(KEY_CODE);
            }
        } catch (JSONException e) {
            throw new PocketException();
        }

        if (mToken == null) {
            throw new PocketException();
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
