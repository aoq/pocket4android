/*
 * Copyright (c) 2012-2014 Yu AOKI
 *
 * This software may be modified and distributed under the terms
 * of the MIT license.  See the LICENSE file for details.
 */

package com.aokyu.dev.pocket;

import com.aokyu.dev.pocket.http.HttpParameters;
import com.aokyu.dev.pocket.http.MessageBody;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Set;

public class Request {

    private HttpParameters mParameters = new HttpParameters();

    /* package */ void put(String key, Object value) {
        mParameters.put(key, value);
    }

    /* package */ Object get(String key) {
        return mParameters.get(key);
    }

    /* package */ boolean containsKey(String key) {
        return mParameters.containsKey(key);
    }

    /* package */ Set<String> keySet() {
        return mParameters.keySet();
    }

    /* package */ int size() {
        return mParameters.size();
    }

    /* package */ HttpParameters getHttpParameters() {
        return mParameters;
    }

    public MessageBody getBody() {
        return mParameters;
    }

    public String toJson() {
        return mParameters.toJson();
    }

    public JSONObject toJSONObject() {
        String json = toJson();
        try {
            return new JSONObject(json);
        } catch (JSONException e) {
            return null;
        }
    }

    public String toParameter() {
        return mParameters.toParameter();
    }

    public String toEncodedParameter() throws UnsupportedEncodingException {
        return mParameters.toEncodedParameter();
    }
}
