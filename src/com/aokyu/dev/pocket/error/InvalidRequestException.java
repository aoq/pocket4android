/*
 * Copyright 2012 Yu AOKI
 */

package com.aokyu.dev.pocket.error;

public class InvalidRequestException extends Exception {

    private static final long serialVersionUID = -5815252058868021275L;

    public InvalidRequestException() {
        super();
    }

    public InvalidRequestException(String message) {
        super(message);
    }
}
