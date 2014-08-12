/*
 * Copyright (c) 2012-2014 Yu AOKI
 *
 * This software may be modified and distributed under the terms
 * of the MIT license.  See the LICENSE file for details.
 */

package com.aokyu.dev.pocket.util;

import java.util.HashMap;
import java.util.Iterator;

import org.json.JSONException;
import org.json.JSONObject;

public final class JSONUtils {

    private JSONUtils() {}

    @SuppressWarnings("unchecked")
    public static String[] getKeys(JSONObject obj) {
        int length = obj.length();
        if (length == 0) {
            return null;
        }
        Iterator<String> it = obj.keys();
        String[] keys = new String[length];
        int j = 0;
        while (it.hasNext()) {
            keys[j] = it.next();
            j += 1;
        }
        return keys;
    }

    public static HashMap<String, String> toHashMap(String response) throws JSONException {
        JSONObject jsonObj = new JSONObject(response);
        return toHashMap(jsonObj);
    }

    public static HashMap<String, String> toHashMap(JSONObject jsonObj) throws JSONException {
        HashMap<String, String> map = new HashMap<String, String>();
        String[] names = getKeys(jsonObj);
        int size = names.length;
        for (int i = 0; i < size; i++) {
            String key = names[i];
            String value = jsonObj.getString(key);
            map.put(key, value);
        }
        return map;
    }
}
