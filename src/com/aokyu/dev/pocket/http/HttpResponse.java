/*
 * Copyright (c) 2012-2014 Yu AOKI
 *
 * This software may be modified and distributed under the terms
 * of the MIT license.  See the LICENSE file for details.
 */

package com.aokyu.dev.pocket.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

public class HttpResponse {

    private HttpURLConnection mConnection;

    public HttpResponse(HttpURLConnection connection) throws IOException {
        if (connection == null) {
            throw new IllegalArgumentException();
        }
        mConnection = connection;
    }

    public HttpResponse followRedirects(boolean follow) {
        mConnection.setInstanceFollowRedirects(follow);
        return this;
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

    public InputStream getResponseAsStream() throws IOException {
        return mConnection.getInputStream();
    }

    public String getResponseAsString() throws IOException {
        InputStream input = mConnection.getInputStream();
        InputStreamReader reader = null;
        BufferedReader buffer = null;
        String response = null;
        try {
            reader = new InputStreamReader(input);
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

    public JSONObject getResponseAsJSONObject() throws IOException, JSONException {
        String response = getResponseAsString();
        JSONObject jsonObj = new JSONObject(response);
        return jsonObj;
    }

    public void disconnect() {
        mConnection.disconnect();
    }
}
