/*
 * Copyright (c) 2015 Yu AOKI
 *
 * This software may be modified and distributed under the terms
 * of the MIT license. See the LICENSE file for details.
 */

package com.aokyu.pocket.http.content;

import java.io.File;

/**
 * This class holds the element of multipart data.
 */
/* package */ class MultipartElement {

    private static final String CONTENT_DISPOSITION = "Content-Disposition: ";
    private static final String FORM_DATA = "form-data";
    private static final String FORM_NAME = "name=";
    private static final String FILE_NAME = "filename=";
    private static final String CONTENT_TYPE = "Content-Type: ";

    private static final String QUOT_MARK = "\"";
    private static final String CRLF = "\r\n";

    private final String mFilePath;
    private final String mHeader;

    public MultipartElement(String filePath, String contentType) {
        mFilePath = filePath;
        File file = new File(mFilePath);
        String fileName = file.getName();
        mHeader = new StringBuilder()
            .append(CONTENT_DISPOSITION)
            .append(FORM_DATA + "; ")
            .append(FORM_NAME + QUOT_MARK + "upload" + QUOT_MARK + "; ")
            .append(FILE_NAME + QUOT_MARK + fileName + QUOT_MARK + CRLF)
            .append(CONTENT_TYPE)
            .append(contentType)
            .append(CRLF)
            .append(CRLF)
            .toString();
    }

    public String getFilePath() {
        return mFilePath;
    }

    public String getHeader() {
        return mHeader;
    }
}
