/*
 * Copyright (c) 2015 Yu AOKI
 *
 * This software may be modified and distributed under the terms
 * of the MIT license. See the LICENSE file for details.
 */

package com.aokyu.pocket.http.content;

import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

/**
 * This is an abstract message body class that handles unsupported operations.
 * Subclasses should override methods.
 */
public class AbstractMessageBody implements MessageBody {

    protected AbstractMessageBody() {}

    @Override
    public String toJson() {
        throw new UnsupportedOperationException("JSON format is not supported");
    }

    @Override
    public String toParameter() {
        throw new UnsupportedOperationException("Parameter format is not supported");
    }

    @Override
    public String toEncodedParameter() throws UnsupportedEncodingException {
        throw new UnsupportedOperationException("Encoded parameter format is not supported");
    }

    @Override
    public MessageStream toMessageStream(OutputStream output) {
        throw new UnsupportedOperationException("Message stream is not supported");
    }
}
