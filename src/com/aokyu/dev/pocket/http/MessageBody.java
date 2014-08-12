/*
 * Copyright (c) 2012-2014 Yu AOKI
 *
 * This software may be modified and distributed under the terms
 * of the MIT license.  See the LICENSE file for details.
 */

package com.aokyu.dev.pocket.http;

import java.io.UnsupportedEncodingException;

public interface MessageBody {

    public String toJson();
    public String toParameter();
    public String toEncodedParameter() throws UnsupportedEncodingException;

}
