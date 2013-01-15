/*
 * Copyright 2012 Yu AOKI
 */

package com.aokyu.dev.pocket;

import java.util.Set;

import org.json.JSONException;
import org.json.JSONObject;

import com.aokyu.dev.pocket.http.HttpParameters;
import com.aokyu.dev.pocket.util.JSONUtils;

public class Response {

    public final class Parameter {

        public static final String STATUS = "status";

        private Parameter() {}

    }

    public enum Status {
        FAILED(0),
        SUCCEEDED(1);

        private int mStatus;

        private Status(int status) {
            mStatus = status;
        }

        public int intValue() {
            return mStatus;
        }

        public static Status valueOf(int value) {
            Status[] statuses = values();
            for (Status status : statuses) {
                if (status.intValue() == value) {
                    return status;
                }
            }
            return null;
        }
    }

    private HttpParameters mParameters = new HttpParameters();

    protected Response(JSONObject jsonObj) throws JSONException {
        if (jsonObj != null) {
            String[] keys = JSONUtils.getKeys(jsonObj);
            int size = keys.length;
            for (int i = 0; i < size; i++) {
                String key = keys[i];
                Object value = jsonObj.get(key);
                put(key, value);
            }
        }
    }

    protected Response(JSONObject jsonObj, String rootKey) throws JSONException {
        JSONObject obj = jsonObj.getJSONObject(rootKey);
        if (obj != null) {
            String[] keys = JSONUtils.getKeys(obj);
            int size = keys.length;
            for (int i = 0; i < size; i++) {
                String key = keys[i];
                Object value = obj.get(key);
                put(key, value);
            }
        }
    }

    /* package */ void put(String key, Object value) {
        mParameters.put(key, value);
    }

    public Set<String> keySet() {
        return mParameters.keySet();
    }

    public int size() {
        return mParameters.size();
    }

    public Object get(String key) {
        return mParameters.get(key);
    }

    public boolean containsKey(String key) {
        return mParameters.containsKey(key);
    }

    public Status getStatus() {
        if (containsKey(Parameter.STATUS)) {
            int status = (Integer) get(Parameter.STATUS);
            return Status.valueOf(status);
        } else {
            return Status.FAILED;
        }
    }
}
