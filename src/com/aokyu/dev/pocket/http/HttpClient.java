/*
 * Copyright 2012 Yu AOKI
 */

package com.aokyu.dev.pocket.http;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.net.URL;
import java.util.Iterator;
import java.util.Set;

import org.json.JSONObject;

public class HttpClient {

    public static class Configuration {

        private String mUserAgent;
        private Proxy mProxy;
        private String mProxyUser;
        private String mProxyPassword;
        private int mConnectTimeout = -1;
        private int mReadTimeout = -1;

       Configuration() {}

       void setUserAgent(String userAgent) {
           mUserAgent = userAgent;
       }

       public String getUserAgent() {
           return mUserAgent;
       }

       public boolean hasUserAgent() {
           return (mUserAgent != null);
       }

        void setProxy(Proxy proxy) {
            mProxy = proxy;
        }

        public Proxy getProxy() {
            return mProxy;
        }

        public boolean hasProxy() {
            return (mProxy != null);
        }

        void setProxyUser(String username) {
            mProxyUser = username;
        }

        public String getProxyUser() {
            return mProxyUser;
        }

        public boolean hasProxyUser() {
            return (mProxyUser != null);
        }

        void setProxyPassword(String password) {
            mProxyPassword = password;
        }

        public String getProxyPassword() {
            return mProxyPassword;
        }

        public boolean hasProxyPassword() {
            return (mProxyPassword != null);
        }

        void setConnectTimeout(int timeoutMillis) {
            mConnectTimeout = timeoutMillis;
        }

        public int getConnectTimeout() {
            return mConnectTimeout;
        }

        public boolean hasConnectTimeout() {
            return (mConnectTimeout != -1);
        }

        void setReadTimeout(int timeoutMillis) {
            mReadTimeout = timeoutMillis;
        }

        public int getReadTimeout() {
            return mReadTimeout;
        }

        public boolean hasReadTimeout() {
            return (mReadTimeout != -1);
        }
    }

    public static class ConfigurationBuilder {

        private Configuration mConfiguration = new Configuration();

        public ConfigurationBuilder setUserAgent(String userAgent) {
            mConfiguration.setUserAgent(userAgent);
            return this;
        }

        public ConfigurationBuilder setProxy(Proxy proxy) {
            mConfiguration.setProxy(proxy);
            return this;
        }

        public ConfigurationBuilder setProxyUser(String username) {
            mConfiguration.setProxyUser(username);
            return this;
        }

        public ConfigurationBuilder setProxyPassword(String password) {
            mConfiguration.setProxyPassword(password);
            return this;
        }

        public ConfigurationBuilder setConnectTimeout(int timeoutMillis) {
            if (timeoutMillis <= 0) {
                throw new IllegalArgumentException();
            }

            mConfiguration.setConnectTimeout(timeoutMillis);
            return this;
        }

        public ConfigurationBuilder setReadTimeout(int timeoutMillis) {
            if (timeoutMillis <= 0) {
                throw new IllegalArgumentException();
            }

            mConfiguration.setReadTimeout(timeoutMillis);
            return this;
        }

        public Configuration build() {
            return mConfiguration;
        }
    }

    private static final String HEADER_USER_AGENT = "User-Agent";

    private Configuration mConfiguration;

    public HttpClient() {}

    public void setConfiguration(Configuration config) {
        mConfiguration = config;
    }

    public Configuration getConfiguration() {
        return mConfiguration;
    }

    public HttpResponse execute(HttpRequest request) throws IOException {
        URL url = request.getUrl();
        HttpURLConnection connection = null;
        HttpResponse response = null;
        connection = prepareConnection(url);

        String method = request.getMethodAsString();
        connection.setRequestMethod(method);

        connection.setDoOutput(true);
        connection.setDoInput(true);

        HttpHeaders headers = request.getHeaders();
        setHeaders(connection, headers);

        HttpParameters parameters = request.getParameters();
        JSONObject jsonObj = parameters.toJSONObject();

        OutputStreamWriter writer =
                new OutputStreamWriter(connection.getOutputStream());
        writer.write(jsonObj.toString());
        writer.close();

        response = new HttpResponse(connection);
        return response;
    }

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

    private void setProxyAuthenticator(final String username, final String password) {
        Authenticator.setDefault(new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password.toCharArray());
            }
        });
    }

    private void setHeaders(HttpURLConnection connection, HttpHeaders headers) {
        Set<String> keys = headers.keySet();
        Iterator<String> it = keys.iterator();
        while (it.hasNext()) {
            String header = it.next();
            String value = headers.get(header);
            connection.setRequestProperty(header, value);
        }
    }
}
