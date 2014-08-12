/*
 * Copyright (c) 2012-2014 Yu AOKI
 *
 * This software may be modified and distributed under the terms
 * of the MIT license.  See the LICENSE file for details.
 */

package com.aokyu.dev.pocket;

import com.aokyu.dev.pocket.error.PocketException;
import com.aokyu.dev.pocket.http.HttpParameters;
import com.aokyu.dev.pocket.util.JSONUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;
import java.util.Set;

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

    private UserLimit mUserLimit;
    private ClientLimit mClientLimit;

    protected Response(JSONObject jsonObj, Map<String, List<String>> headerFields)
            throws JSONException, PocketException {
        if (jsonObj != null) {
            String[] keys = JSONUtils.getKeys(jsonObj);
            int size = keys.length;
            for (int i = 0; i < size; i++) {
                String key = keys[i];
                Object value = jsonObj.get(key);
                put(key, value);
            }
        }

        mUserLimit = new UserLimit(headerFields);
        mClientLimit = new ClientLimit(headerFields);
    }

    protected Response(JSONObject jsonObj, String rootKey, Map<String, List<String>> headerFields)
            throws JSONException, PocketException {
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

        mUserLimit = new UserLimit(headerFields);
        mClientLimit = new ClientLimit(headerFields);
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

    public UserLimit getUserRateLimit() {
        return mUserLimit;
    }

    public ClientLimit getClientRateLimit() {
        return mClientLimit;
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
