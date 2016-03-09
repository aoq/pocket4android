/*
 * Copyright (c) 2015 Yu AOKI
 *
 * This software may be modified and distributed under the terms
 * of the MIT license. See the LICENSE file for details.
 */

package com.aokyu.pocket.http.content;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * This class holds JSON data and converts data to the specified format if needed.
 */
public class JsonBody extends AbstractMessageBody {

    private final JSONObject mJson;

    /**
     * Creates the message body for the JSON object.
     *
     * @param json The JSON object.
     */
    public JsonBody(JSONObject json) {
        mJson = json;
    }

    /**
     * Creates the message body for the JSON array.
     *
     * @param json The JSON string.
     * @throws JSONException if the JSON string includes an error.
     */
    public JsonBody(String json) throws JSONException {
        mJson = new JSONObject(json);
    }

    @Override
    public String toJson() {
        return mJson.toString();
    }
}
