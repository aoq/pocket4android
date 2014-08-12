/*
 * Copyright (c) 2012-2014 Yu AOKI
 *
 * This software may be modified and distributed under the terms
 * of the MIT license.  See the LICENSE file for details.
 */

package com.aokyu.dev.pocket.error;

public class PocketException extends Exception {

    private static final long serialVersionUID = -2027734071522552936L;

    public PocketException() {
        super();
    }

    public PocketException(String message) {
        super(message);
    }
}
