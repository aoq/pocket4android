/*
 * Copyright 2012 Yu AOKI
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
