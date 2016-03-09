/*
 * Copyright (c) 2012-2014 Yu AOKI
 *
 * This software may be modified and distributed under the terms
 * of the MIT license.  See the LICENSE file for details.
 */

package com.aokyu.pocket.error;

import java.io.IOException;

import com.aokyu.pocket.http.HttpHeader;
import com.aokyu.pocket.http.HttpResponse;

public class ErrorResponse {

    private int mStatus;
    private int mErrorCode;
    private String mErrorMessage;

    public ErrorResponse(HttpResponse response) throws IOException {
        mStatus = response.getStatusCode();
        String code = response.getHeaderField(HttpHeader.X_ERROR_CODE);
        try {
            mErrorCode = Integer.parseInt(code);
        } catch (NumberFormatException e) {
            mErrorCode = ErrorCode.UNKNOWN;
        }

        mErrorMessage = response.getHeaderField(HttpHeader.X_ERROR);
    }

    public int getStatus() {
        return mStatus;
    }

    public int getErrorCode() {
        return mErrorCode;
    }

    public String getErrorMessage() {
        return mErrorMessage;
    }
}
