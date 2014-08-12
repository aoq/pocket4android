/*
 * Copyright (c) 2012-2014 Yu AOKI
 *
 * This software may be modified and distributed under the terms
 * of the MIT license.  See the LICENSE file for details.
 */

package com.aokyu.dev.pocket.http;

public enum ContentType {

    X_WWW_FORM_URLENCODED("application/x-www-form-urlencoded"),
    JSON("application/json"),
    JSON_WITH_UTF8("application/json; charset=UTF-8");

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
            return value.equals(mValue);
        }
    }

}
