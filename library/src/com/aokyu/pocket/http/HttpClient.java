/*
 * Copyright (c) 2015 Yu AOKI
 *
 * This software may be modified and distributed under the terms
 * of the MIT license. See the LICENSE file for details.
 */

package com.aokyu.pocket.http;

import com.aokyu.pocket.http.content.MessageBody;
import com.aokyu.pocket.http.content.MessageStream;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.net.URL;
import java.util.Map.Entry;
import java.util.Set;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;

/**
 * The wrapper class of {@link HttpURLConnection} to send HTTP requests.
 * A request can be sent through {@link #execute(HttpRequest)}.
 * @see HttpRequest
 * @see HttpResponse
 */
public class HttpClient {

    private static final String DEFAULT_ENCODING = "UTF-8";

    private static final String HEADER_USER_AGENT = "User-Agent";

    private Configuration mConfiguration;

    private HostnameVerifier mVerifier;
    private SSLContext mSslContext;

    public HttpClient() {}

    /**
     * Sets the hostname verifier for this client.
     *
     * @param verifier The hostname verifier to set.
     * @see HttpsURLConnection#setHostnameVerifier(HostnameVerifier)
     */
    public void setHostnameVerifier(HostnameVerifier verifier) {
        mVerifier = verifier;
    }

    /**
     * Returns the hostname verifier for this client.
     *
     * @return the hostname verifier for this client.
     */
    public HostnameVerifier getHostnameVerifier() {
        return mVerifier;
    }

    /**
     * Sets the SSL context for retrieving a socket factory.
     *
     * @param sslContext The SSL context to set.
     * @see SSLContext
     * @see HttpsURLConnection#setSSLSocketFactory(SSLSocketFactory)
     */
    public void setSslContext(SSLContext sslContext) {
        mSslContext = sslContext;
    }

    /**
     * Returns the SSL context for this client.
     *
     * @return the SSL context for this client.
     */
    public SSLContext getSslContext() {
        return mSslContext;
    }

    /**
     * Sets the configuration for this client.
     *
     * @param config The configuration to set.
     * @see Configuration
     * @see ConfigurationBuilder
     */
    public void setConfiguration(Configuration config) {
        mConfiguration = config;
    }

    /**
     * Returns the configuration for this client.
     *
     * @return the configuration for this client.
     */
    public Configuration getConfiguration() {
        return mConfiguration;
    }

    /**
     * Sends the request with {@link MessageBody} to the {@link URL} by {@link HttpMethod}.
     *
     * @param request The {@link HttpRequest} to send.
     * @return The {@link HttpResponse} retrieved from the server.
     * @throws IOException if an I/O-related error occurs.
     * @see HttpRequest
     * @see HttpResponse
     */
    public HttpResponse execute(HttpRequest request) throws IOException {
        URL url = request.getUrl();
        HttpURLConnection connection = null;
        HttpResponse response = null;
        connection = prepareConnection(url);

        if (connection instanceof HttpsURLConnection) {
            if (mVerifier != null) {
                ((HttpsURLConnection)connection).setHostnameVerifier(mVerifier);
            }

            // Set the secure socket protocol implementation if the HTTPS scheme is used.
            if (mSslContext != null) {
                SSLSocketFactory socketFactory = mSslContext.getSocketFactory();
                ((HttpsURLConnection)connection).setSSLSocketFactory(socketFactory);
            }
        }

        HttpMethod method = request.getMethod();
        connection.setRequestMethod(method.name());

        HttpHeaders headers = request.getHeaders();
        if (headers != null) {
            Set<Entry<String, String>> entries = headers.entrySet();
            for (Entry<String, String> entry : entries) {
                String header = entry.getKey();
                String value = entry.getValue();
                connection.setRequestProperty(header, value);
            }
        }

        switch (method) {
        case GET:
        case DELETE:
            connection.setDoInput(true);
            connection.connect();
            onConnected();
            break;
        case PUT:
        case POST:
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.connect();
            onConnected();

            String contentType = headers.get(HttpHeader.CONTENT_TYPE);
            if (contentType == null || contentType.length() == 0) {
                throw new IllegalArgumentException("Content type should be specified.");
            }

            MessageBody body = request.getBody();
            if (ContentType.JSON.equals(contentType)) {
                onJson(connection, body);
            } else if (ContentType.X_WWW_FORM_URLENCODED.equals(contentType)) {
                onFormUrlEncoded(connection, body);
            } else if (ContentType.MULTIPART_DATA.equals(contentType)){
                onMultipartData(connection, body);
            } else if (ContentType.OCTET_STREAM.equals(contentType)) {
                onOctetStream(connection, body);
            } else {
                onBinaryData(connection, body);
            }
        }

        response = new HttpResponse(connection);
        return response;
    }

    /**
     * Called when the type of the message body for the request is a JSON data.
     * @param connection The connection for the request.
     * @param body The message body to send.
     * @throws IOException if a network I/O error occurs.
     */
    private void onJson(HttpURLConnection connection, MessageBody body)
            throws IOException {
        String messageBody = body.toJson();
        byte[] bytes = messageBody.getBytes(DEFAULT_ENCODING);
        OutputStreamWriter writer =
                new OutputStreamWriter(connection.getOutputStream(), DEFAULT_ENCODING);
        writer.write(messageBody);
        writer.flush();
        writer.close();
        onRequestCompleted(bytes.length);
    }

    /**
     * Called when the type of the message body for the request is an encoded string.
     *
     * @param connection The connection for the request.
     * @param body The message body to send.
     * @throws IOException if a network I/O error occurs.
     */
    private void onFormUrlEncoded(HttpURLConnection connection, MessageBody body)
            throws IOException {
        String messageBody = body.toEncodedParameter();
        byte[] bytes = messageBody.getBytes(DEFAULT_ENCODING);
        OutputStreamWriter writer =
                new OutputStreamWriter(connection.getOutputStream(), DEFAULT_ENCODING);
        writer.write(messageBody);
        writer.flush();
        writer.close();
        onRequestCompleted(bytes.length);
    }

    /**
     * Called when the type of the message body for the request is a multipart data.
     *
     * @param connection The connection for the request.
     * @param body The message body to send.
     * @throws IOException if a network I/O error occurs.
     */
    private void onMultipartData(HttpURLConnection connection, MessageBody body)
            throws IOException {
        MessageStream stream = body.toMessageStream(connection.getOutputStream());
        int numberOfBytes = stream.write();
        stream.flush();
        stream.close();
        onRequestCompleted(numberOfBytes);
    }

    /**
     * Called when the type of the message body for the request is a binary data.
     *
     * @param connection The connection for the request.
     * @param body The message body to send.
     * @throws IOException if a network I/O error occurs.
     */
    private void onOctetStream(HttpURLConnection connection, MessageBody body) throws IOException {
        onBinaryData(connection, body);
    }

    /**
     * Called when the type of the message body for the request is unknown.
     *
     * @param connection The connection for the request.
     * @param body The message body to send.
     * @throws IOException if a network I/O error occurs.
     */
    private void onBinaryData(HttpURLConnection connection, MessageBody body)
            throws IOException {
        MessageStream stream = body.toMessageStream(connection.getOutputStream());
        int numberOfBytes = stream.write();
        stream.flush();
        stream.close();
        onRequestCompleted(numberOfBytes);
    }

    /**
     * Called when the connection was opened.
     */
    protected void onConnected() {}

    /**
     * Called when the request completed.
     *
     * @param numberOfBytes The number of bytes that has been sent.
     */
    protected void onRequestCompleted(int numberOfBytes) {}

    /**
     * Prepares the connection for the {@link URL} with the {@link Configuration}.
     *
     * @param url The {@link URL} to connect.
     * @return The {HttpURLConnection}.
     * @throws IOException if an I/O error occurs while opening the connection.
     * @see URL#openConnection()
     * @see Configuration
     */
    private HttpURLConnection prepareConnection(URL url) throws IOException {
        HttpURLConnection connection = null;
        if (mConfiguration == null) {
            connection = (HttpURLConnection) url.openConnection();
        } else {

            if (mConfiguration.hasProxy()) {
                if (mConfiguration.hasProxyUser() && mConfiguration.hasProxyPassword()) {
                    String username = mConfiguration.getProxyUser();
                    String password = mConfiguration.getProxyPassword();
                    setProxyAuthenticator(username, password);
                }
                Proxy proxy = mConfiguration.getProxy();
                connection = (HttpURLConnection) url.openConnection(proxy);
            } else {
                connection = (HttpURLConnection) url.openConnection();
            }

            if (mConfiguration.hasUserAgent()) {
                String agent = mConfiguration.getUserAgent();
                connection.setRequestProperty(HEADER_USER_AGENT, agent);
            }

            if (mConfiguration.hasConnectTimeout()) {
                int timeoutMillis = mConfiguration.getConnectTimeout();
                connection.setConnectTimeout(timeoutMillis);
            }

            if (mConfiguration.hasReadTimeout()) {
                int timeoutMillis = mConfiguration.getReadTimeout();
                connection.setReadTimeout(timeoutMillis);
            }
        }
        return connection;
    }

    /**
     * Sets the password authenticator.
     *
     * @param username The username for the password authentication.
     * @param password The password associated with the username for the password authentication.
     * @see PasswordAuthentication
     */
    private void setProxyAuthenticator(final String username, final String password) {
        Authenticator.setDefault(new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password.toCharArray());
            }
        });
    }

    /**
     * The configuration for a HTTP client.
     *
     * @see HttpClient#prepareConnection(URL)
     */
    public static class Configuration {

        private static final int NO_TIMEOUT = -1;

        private String mUserAgent;
        private Proxy mProxy;
        private String mProxyUser;
        private String mProxyPassword;
        private int mConnectTimeout = NO_TIMEOUT;
        private int mReadTimeout = NO_TIMEOUT;

        /* package */ Configuration() {}

        /* package */ void setUserAgent(String userAgent) {
            mUserAgent = userAgent;
        }

        public String getUserAgent() {
            return mUserAgent;
        }

        public boolean hasUserAgent() {
            return (mUserAgent != null);
        }

        /* package */ void setProxy(Proxy proxy) {
            mProxy = proxy;
        }

        public Proxy getProxy() {
            return mProxy;
        }

        public boolean hasProxy() {
            return (mProxy != null);
        }

        /* package */ void setProxyUser(String username) {
            mProxyUser = username;
        }

        public String getProxyUser() {
            return mProxyUser;
        }

        public boolean hasProxyUser() {
            return (mProxyUser != null);
        }

        /* package */ void setProxyPassword(String password) {
            mProxyPassword = password;
        }

        public String getProxyPassword() {
            return mProxyPassword;
        }

        public boolean hasProxyPassword() {
            return (mProxyPassword != null);
        }

        /* package */ void setConnectTimeout(int timeoutMillis) {
            mConnectTimeout = timeoutMillis;
        }

        public int getConnectTimeout() {
            return mConnectTimeout;
        }

        public boolean hasConnectTimeout() {
            return (mConnectTimeout != NO_TIMEOUT);
        }

        /* package */ void setReadTimeout(int timeoutMillis) {
            mReadTimeout = timeoutMillis;
        }

        public int getReadTimeout() {
            return mReadTimeout;
        }

        public boolean hasReadTimeout() {
            return (mReadTimeout != NO_TIMEOUT);
        }
    }

    /**
     * The builder class for {@link Configuration}.
     */
    public static class ConfigurationBuilder {

        private Configuration mConfiguration = new Configuration();

        /**
         * Sets the custom user agent.
         *
         * @param userAgent The user agent to set.
         * @see HttpURLConnection#setRequestProperty(String, String)
         */
        public ConfigurationBuilder setUserAgent(String userAgent) {
            mConfiguration.setUserAgent(userAgent);
            return this;
        }

        /**
         * Sets the proxy server settings.
         *
         * @param proxy The proxy server settings to set.
         * @see #setProxyUser(String)
         * @see #setProxyPassword(String)
         * @see Proxy
         * @see URL#openConnection(Proxy)
         */
        public ConfigurationBuilder setProxy(Proxy proxy) {
            mConfiguration.setProxy(proxy);
            return this;
        }

        /**
         * Sets the username for the password authentication.
         *
         * @param username The username to set.
         * @see #setProxy(Proxy)
         * @see #setProxyPassword(String)
         */
        public ConfigurationBuilder setProxyUser(String username) {
            mConfiguration.setProxyUser(username);
            return this;
        }

        /**
         * Sets the password associated with the username for the password authentication.
         *
         * @param password The password to set.
         */
        public ConfigurationBuilder setProxyPassword(String password) {
            mConfiguration.setProxyPassword(password);
            return this;
        }

        /**
         * Sets the maximum time in milliseconds to wait while connecting.
         *
         * @param timeoutMillis The time in milliseconds
         * @see HttpURLConnection#setConnectTimeout(int)
         */
        public ConfigurationBuilder setConnectTimeout(int timeoutMillis) {
            if (timeoutMillis <= 0) {
                throw new IllegalArgumentException("time should not be lower than 0");
            }

            mConfiguration.setConnectTimeout(timeoutMillis);
            return this;
        }

        /**
         * Sets the maximum time to wait for an input stream read to complete.
         *
         * @param timeoutMillis The time in milliseconds.
         * @see HttpURLConnection#setReadTimeout(int)
         */
        public ConfigurationBuilder setReadTimeout(int timeoutMillis) {
            if (timeoutMillis <= 0) {
                throw new IllegalArgumentException("time should not be lower than 0");
            }

            mConfiguration.setReadTimeout(timeoutMillis);
            return this;
        }

        /**
         * Creates a configuration for HTTP client.
         *
         * @return The configuration for HTTP client.
         */
        public Configuration build() {
            return mConfiguration;
        }
    }

}
