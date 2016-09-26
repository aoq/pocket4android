/*
 * Copyright (c) 2015 Yu AOKI
 *
 * This software may be modified and distributed under the terms
 * of the MIT license. See the LICENSE file for details.
 */

package com.aokyu.pocket.http;

import android.util.Pair;

/**
 * The available content types for {@link HttpClient}.
 */
public enum ContentType {

    UNKNOWN(""),
    X_WWW_FORM_URLENCODED("application/x-www-form-urlencoded"),
    JSON("application/json"),
    MULTIPART_DATA("multipart/form-data"),
    OCTET_STREAM("application/octet-stream");

    private static final String SEPARATOR = "; ";

    private final String mType;

    private ContentType(String type) {
        mType = type;
    }

    public String toHeader(Pair<String, String>... params) {
        StringBuilder builder = new StringBuilder();
        builder.append(mType);
        for (Pair<String, String> param : params) {
            builder.append(SEPARATOR).append(param.first).append("=").append(param.second);
        }
        return builder.toString();
    }

    public boolean equals(String value) {
        if (value == null) {
            return false;
        } else {
            return mType.equals(value);
        }
    }
}
