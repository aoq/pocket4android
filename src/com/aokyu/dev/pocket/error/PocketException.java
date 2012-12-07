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
