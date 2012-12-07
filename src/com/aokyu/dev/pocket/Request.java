/*
 * Copyright 2012 Yu AOKI
 */

package com.aokyu.dev.pocket;

import java.util.Set;

import org.json.JSONObject;

import com.aokyu.dev.pocket.http.HttpParameters;

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

    /* package */ JSONObject toJSONObject() {
        return mParameters.toJSONObject();
    }
}
