/*
 * Copyright (c) 2015 Yu AOKI
 *
 * This software may be modified and distributed under the terms
 * of the MIT license. See the LICENSE file for details.
 */

package com.aokyu.pocket.http.content;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * This class is used as a proxy for the specified stream.
 */
public abstract class MessageStream {

    protected final DataOutputStream mStream;

    /**
     * Writes data to the specified output stream.
     *
     * @return The number of bytes.
     * @throws IOException if an error occurs while writing to the specified output stream.
     */
    public abstract int write() throws IOException;

    /**
     * Creates a message stream.
     *
     * @param output The output stream to which data will be written.
     */
    public MessageStream(OutputStream output) {
        mStream = new DataOutputStream(output);
    }

    public OutputStream getOutputStream() {
        return mStream;
    }

    /**
     * Flushes this stream.
     *
     * @throws IOException if an error occurs while flushing this stream.
     */
    public void flush() throws IOException {
        mStream.flush();
    }

    /**
     * Closes this stream.
     *
     * @throws IOException if an error occurs while closing this stream.
     */
    public void close() throws IOException {
        mStream.close();
    }
}
