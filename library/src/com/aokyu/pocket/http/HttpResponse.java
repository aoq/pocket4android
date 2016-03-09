/*
 * Copyright (c) 2015 Yu AOKI
 *
 * This software may be modified and distributed under the terms
 * of the MIT license. See the LICENSE file for details.
 */

package com.aokyu.pocket.http;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.List;
import java.util.Map;

/**
 * The wrapper of {@link HttpURLConnection} to retrieve a response.
 */
public class HttpResponse {

    private static final String DEFAULT_ENCODING = "UTF-8";

    private HttpURLConnection mConnection;

    public HttpResponse(HttpURLConnection connection) throws IOException {
        if (connection == null) {
            throw new IllegalArgumentException();
        }
        mConnection = connection;
    }

    public void setFollowRedirects(boolean followRedirects) {
        mConnection.setInstanceFollowRedirects(followRedirects);
    }

    public int getStatusCode() throws IOException {
        return mConnection.getResponseCode();
    }

    public Map<String, List<String>> getHeaderFields() {
        return mConnection.getHeaderFields();
    }

    public String getHeaderField(String key) {
        return mConnection.getHeaderField(key);
    }

    public InputStream getErrorAsStream() throws IOException {
        return mConnection.getErrorStream();
    }

    public String getErrorAsString() throws IOException {
        InputStream error = mConnection.getErrorStream();
        InputStreamReader reader = null;
        BufferedReader buffer = null;
        String response = null;
        try {
            reader = new InputStreamReader(error, DEFAULT_ENCODING);
            buffer = new BufferedReader(reader);

            String line = null;
            StringBuilder builder = new StringBuilder();
            while ((line = buffer.readLine()) != null) {
                builder.append(line);
            }

            response = builder.toString();
        } finally {
            if (buffer != null) {
                buffer.close();
            }
            if (reader != null) {
                reader.close();
            }
        }
        return response;
    }

    public InputStream getResponseAsStream() throws IOException {
        return mConnection.getInputStream();
    }

    public String getResponseAsString() throws IOException {
        InputStream input = mConnection.getInputStream();
        InputStreamReader reader = null;
        BufferedReader buffer = null;
        String response = null;
        try {
            reader = new InputStreamReader(input, DEFAULT_ENCODING);
            buffer = new BufferedReader(reader);

            String line = null;
            StringBuilder builder = new StringBuilder();
            while ((line = buffer.readLine()) != null) {
                builder.append(line);
            }

            response = builder.toString();
        } finally {
            if (buffer != null) {
                buffer.close();
            }
            if (reader != null) {
                reader.close();
            }
        }
        return response;
    }

    public JSONObject getResponseAsJSONObject() throws JSONException, IOException {
        String responseString = getResponseAsString();
        return new JSONObject(responseString);
    }

    public void disconnect() {
        mConnection.disconnect();
    }
}
