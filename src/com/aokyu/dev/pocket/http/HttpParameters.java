/*
 * Copyright 2012 Yu AOKI
 */

package com.aokyu.dev.pocket.http;

import java.util.HashMap;

import org.json.JSONObject;

public class HttpParameters extends HashMap<String, Object> {

    private static final long serialVersionUID = -7410515868503883376L;

    public HttpParameters() {
        super();
    }

    public JSONObject toJSONObject() {
        return new JSONObject(this);
    }
}
