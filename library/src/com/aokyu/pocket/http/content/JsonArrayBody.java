/*
 * Copyright (c) 2015 Yu AOKI
 *
 * This software may be modified and distributed under the terms
 * of the MIT license. See the LICENSE file for details.
 */

package com.aokyu.pocket.http.content;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * This class holds JSON array data and converts data to the specified format if needed.
 */
public class JsonArrayBody extends AbstractMessageBody {

    private final JSONArray mJsonArray;

    /**
     * Creates the message body for the JSON array.
     *
     * @param jsonArray The JSON array.
     * @throws JSONException if the JSON array includes an error.
     */
    public JsonArrayBody(JSONArray jsonArray) {
        mJsonArray = jsonArray;
    }

    /**
     * Creates the message body for the JSON array.
     *
     * @param json The JSON array string.
     * @throws JSONException if the JSON array includes an error.
     */
    public JsonArrayBody(String json) throws JSONException {
        mJsonArray = new JSONArray(json);
    }

    @Override
    public String toJson() {
        return mJsonArray.toString();
    }
}
