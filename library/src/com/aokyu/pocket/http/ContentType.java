/*
 * Copyright (c) 2015 Yu AOKI
 *
 * This software may be modified and distributed under the terms
 * of the MIT license. See the LICENSE file for details.
 */

package com.aokyu.pocket.http;

/**
 * The available content types for {@link HttpClient}.
 */
public enum ContentType {

    X_WWW_FORM_URLENCODED("application/x-www-form-urlencoded"),
    X_WWW_FORM_URLENCODED_UTF8("application/x-www-form-urlencoded; charset=UTF-8"),
    JSON("application/json"),
    JSON_UTF8("application/json; charset=UTF-8"),
    MULTIPART_DATA("multipart/form-data; boundary="),
    OCTET_STREAM("application/octet-stream");

    private String mValue;

    private ContentType(String value) {
        mValue = value;
    }

    public String get() {
        return mValue;
    }

    public boolean equals(String value) {
        if (value == null) {
            return false;
        } else {
            return value.equals(mValue) || value.startsWith(mValue);
        }
    }
}
