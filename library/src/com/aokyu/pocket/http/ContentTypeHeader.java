/*
 * Copyright (c) 2016 Yu AOKI
 *
 * This software may be modified and distributed under the terms
 * of the MIT license. See the LICENSE file for details.
 */

package com.aokyu.pocket.http;

import android.util.Pair;

import java.util.ArrayList;
import java.util.List;

/**
 * This class holds information of a content type header.
 */
/* package */ class ContentTypeHeader {

    /**
     * The separator of parameters.
     */
    private static final String PARAMS_SEPARATOR = ";";

    /**
     * The separator of a parameter.
     */
    private static final String PARAM_SEPARATOR = "=";

    private static final String KEY_CHARSET = "charset";

    private static final String KEY_BOUNDARY = "boundary";

    private String mType;

    private List<Pair<String, String>> mParams = new ArrayList<>();

    public ContentTypeHeader(String contentTypeHeader) {
        parseHeader(contentTypeHeader);
    }

    /**
     * Parses the content type header.
     *
     * @param header The content type header.
     */
    private void parseHeader(String header) {
        String[] elements = header.split(PARAMS_SEPARATOR);
        int size = elements.length;
        for (int i = 0; i < size; i++) {
            if (i == 0) {
                mType = elements[i].trim();
            } else {
                String param = elements[i].trim();
                String[] paramElements = param.split(PARAM_SEPARATOR);
                String key = paramElements[0];
                String value = paramElements[1];
                mParams.add(new Pair<>(key, value));
            }
        }
    }

    public String getParam(String key, String defaultValue) {
        for (Pair<String, String> param : mParams) {
            if (key.equals(param.first)) {
                return param.second;
            }
        }
        return defaultValue;
    }

    /**
     * Returns the charset in the header.
     *
     * @param defaultValue The value to be returned if no charset is specified.
     * @return The charset.
     */
    public String getCharset(String defaultValue) {
        return getParam(KEY_CHARSET, defaultValue);
    }

    /**
     * Returns the boundary string in the header.
     *
     * @return The boundary string
     */
    public String getBoundary() {
        return getParam(KEY_BOUNDARY, null);
    }

    public List<Pair<String, String>> getParameters() {
        return mParams;
    }

    /**
     * Returns the {@link ContentType} for this header.
     * @return The {@link ContentType} for this header.
     */
    public ContentType toContentType() {
        for (ContentType contentType : ContentType.values()) {
            if (contentType.equals(mType)) {
                return contentType;
            }
        }
        return ContentType.UNKNOWN;
    }
}
