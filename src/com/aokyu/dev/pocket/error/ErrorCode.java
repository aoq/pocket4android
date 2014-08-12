/*
 * Copyright (c) 2012-2014 Yu AOKI
 *
 * This software may be modified and distributed under the terms
 * of the MIT license.  See the LICENSE file for details.
 */

package com.aokyu.dev.pocket.error;

public final class ErrorCode {

    public static final int UNKNOWN = -1;

    public static final int MISSING_CONSUMER_KEY = 138;
    public static final int MISSING_REDIRECT_URL = 140;
    public static final int INVALID_CONSUMER_KEY = 152;
    public static final int USER_REJECTED_CODE = 158;
    public static final int ALREADY_USED_CODE = 159;
    public static final int INVALID_REDIRECT_URI = 181;
    public static final int MISSING_CODE = 182;
    public static final int CODE_NOT_FOUND = 185;
    public static final int POCKET_SERVER_ISSUE = 199;

    private ErrorCode() {}

}
