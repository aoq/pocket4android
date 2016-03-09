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
 * The interface is used to get data as a HTTP message body.
 */
public interface MessageBody {

    /**
     * Returns a string representation of JSON for this data.
     *
     * @return a string representation of JSON for this data.
     */
    public String toJson();

    /**
     * Returns a string representation of ampersand-separated parameters for this data.
     *
     * @return a string representation of ampersand-separated parameters for this data.
     */
    public String toParameter();

    /**
     * Returns a string representation of ampersand-separated encoded parameters for this data.
     *
     * @return a string representation of ampersand-separated encoded parameters for this data.
     */
    public String toEncodedParameter() throws UnsupportedEncodingException;

    /**
     * Returns a proxy for the specified output stream.
     *
     * @param output The output stream to which data will be written.
     * @return A proxy for the specified output stream.
     */
    public MessageStream toMessageStream(OutputStream output);

}
