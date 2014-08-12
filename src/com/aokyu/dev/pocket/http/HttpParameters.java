/*
 * Copyright (c) 2012-2014 Yu AOKI
 *
 * This software may be modified and distributed under the terms
 * of the MIT license.  See the LICENSE file for details.
 */

package com.aokyu.dev.pocket.http;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Set;

public class HttpParameters extends HashMap<String, Object> implements MessageBody {

    private static final long serialVersionUID = -7410515868503883376L;

    public HttpParameters() {
        super();
    }

    public String toJson() {
        JSONObject jsonObj = new JSONObject(this);
        return jsonObj.toString();
    }

    public String toParameter() {
        StringBuilder builder = new StringBuilder();
        Set<Entry<String, Object>> entries = entrySet();
        boolean first = true;
        for (Entry<String, Object> entry : entries) {
            if (first) {
                first = false;
            } else {
                builder.append("&");
            }
            String key = entry.getKey();
            Object value = entry.getValue();
            builder.append(key);
            builder.append("=");
            builder.append(value.toString());
        }
        return builder.toString();
    }

    public String toEncodedParameter() throws UnsupportedEncodingException {
        StringBuilder builder = new StringBuilder();
        Set<Entry<String, Object>> entries = entrySet();
        boolean first = true;
        for (Entry<String, Object> entry : entries) {
            if (first) {
                first = false;
            } else {
                builder.append("&");
            }
            String key = entry.getKey();
            Object value = entry.getValue();
            builder.append(key);
            builder.append("=");
            String valueAsString = value.toString();
            String encoded = URLEncoder.encode(valueAsString, "UTF-8");
            builder.append(encoded);
        }
        return builder.toString();
    }

}
