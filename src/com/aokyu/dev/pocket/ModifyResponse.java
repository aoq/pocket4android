/*
 * Copyright 2012 Yu AOKI
 */

package com.aokyu.dev.pocket;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ModifyResponse extends Response {

    public final class Parameter {

        public static final String ACTION_RESULTS = "action_results";
        public static final String STATUS = "status";

    }

    /* package */ ModifyResponse(JSONObject jsonObj) throws JSONException {
        super(jsonObj);
    }

    public boolean[] getActionResults() throws JSONException {
        JSONArray jsonArray = (JSONArray) get(Parameter.ACTION_RESULTS);
        int size = jsonArray.length();
        boolean[] results = new boolean[size];
        for (int i = 0; i < size; i++) {
            results[i] = jsonArray.getBoolean(i);
        }
        return results;
    }

}
