/*
 * Copyright (c) 2012-2014 Yu AOKI
 *
 * This software may be modified and distributed under the terms
 * of the MIT license.  See the LICENSE file for details.
 */

package com.aokyu.pocket;

import com.aokyu.pocket.PocketServer.RequestType;
import com.aokyu.pocket.error.ErrorHandler;
import com.aokyu.pocket.error.ErrorResponse;
import com.aokyu.pocket.error.InvalidRequestException;
import com.aokyu.pocket.error.PocketException;
import com.aokyu.pocket.http.ContentType;
import com.aokyu.pocket.http.HttpClient;
import com.aokyu.pocket.http.HttpHeader;
import com.aokyu.pocket.http.HttpHeaders;
import com.aokyu.pocket.http.HttpMethod;
import com.aokyu.pocket.http.HttpRequest;
import com.aokyu.pocket.http.HttpResponse;
import com.aokyu.pocket.http.content.MessageBody;
import com.aokyu.pocket.http.content.ParametersBody;
import com.aokyu.pocket.util.PocketUtils;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Pair;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The client to access to the Pocket features.
 * @see <a href="https://getpocket.com/developer/">Pocket: Developer API</a>
 */
public class PocketClient {

    private static final String KEY_CHARSET = "charset";

    private static final String ENCODING = "UTF-8";

    private HttpClient mHttpClient;

    private ConsumerKey mConsumerKey;

    private ErrorHandler mErrorHandler;

    /**
     * Creates the Pocket client for the consumer key.
     *
     * @param consumerKey The consumer key.
     */
    public PocketClient(ConsumerKey consumerKey) {
        mHttpClient = new HttpClient();
        mConsumerKey = consumerKey;
        mErrorHandler = new ErrorHandler();
    }

    /**
     * Authorizes a user on this client.
     *
     * @param context The context.
     * @param callback The callback to receive a request token.
     * @throws IOException If a network I/O error occurs.
     * @throws InvalidRequestException If the authorization fails by invalid parameters.
     * @throws PocketException If the authorization fails by invalid response.
     */
    public void authorize(Context context, AuthorizationCallback callback)
            throws IOException, InvalidRequestException, PocketException {
        RequestToken requestToken = retrieveRequestToken();
        boolean handled = callback.onRequestTokenRetrieved(context, mConsumerKey, requestToken);
        if (handled) {
            // The authorization has been handled in the callback.
            return;
        }
        continueAuthorization(context, requestToken);
    }

    /**
     * Retrieves the request token for authentication.
     *
     * @return The request token.
     * @throws IOException If a network I/O error occurs.
     * @throws InvalidRequestException If the authorization fails by invalid parameters.
     * @throws PocketException If the authorization fails by invalid response.
     */
    private RequestToken retrieveRequestToken()
            throws IOException, InvalidRequestException, PocketException {
        String endpoint = PocketServer.getEndpoint(RequestType.OAUTH_REQUEST);
        URL requestUrl = new URL(endpoint);
        HttpHeaders headers = new HttpHeaders();
        Pair<String, String> charsetParam = new Pair<>(KEY_CHARSET, ENCODING);
        headers.put(HttpHeader.CONTENT_TYPE, ContentType.JSON.toHeader(charsetParam));
        headers.put(HttpHeader.X_ACCEPT, ContentType.JSON.toHeader());
        headers.put(HttpHeader.HOST, requestUrl.getHost());

        Map<String, String> parameters = new HashMap<>();
        parameters.put(AuthRequestParameter.CONSUMER_KEY, mConsumerKey.getKey());
        parameters.put(AuthRequestParameter.REDIRECT_URI, PocketUtils.getRedirectUri(mConsumerKey));
        ParametersBody body = new ParametersBody(parameters);

        HttpRequest request = new HttpRequest(HttpMethod.POST, requestUrl, headers, body);

        HttpResponse response = null;
        RequestToken requestToken = null;
        try {
            response = mHttpClient.execute(request);
            if (response.getStatusCode() == HttpURLConnection.HTTP_OK) {
                JSONObject jsonObj = response.getResponseAsJSONObject(ENCODING);
                requestToken = new RequestToken(jsonObj);
            } else {
                ErrorResponse error = new ErrorResponse(response);
                mErrorHandler.handleResponse(error);
            }
        } catch (JSONException e) {
            throw new PocketException(e.getMessage());
        } finally {
            if (response != null) {
                response.disconnect();
            }
        }

        return requestToken;
    }

    /**
     * Continues the authorization by starting a browser application.
     *
     * @param context The context to start a browser application.
     * @param requestToken The request token.
     */
    private void continueAuthorization(Context context, RequestToken requestToken) {
        String url = PocketServer.getRedirectUrl(mConsumerKey, requestToken);
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP);
        if (!(context instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
    }

    /**
     * Authenticates a user on this client.
     *
     * @param requestToken The request token.
     * @return The access token.
     * @throws IOException If a network I/O error occurs.
     * @throws InvalidRequestException If the authentication fails by invalid parameters.
     * @throws PocketException If the authentication fails by invalid response.
     */
    public AccessToken authenticate(RequestToken requestToken)
            throws IOException, InvalidRequestException, PocketException {
        String endpoint = PocketServer.getEndpoint(RequestType.OAUTH_AUTHORIZE);
        URL requestUrl = new URL(endpoint);
        HttpHeaders headers = new HttpHeaders();
        Pair<String, String> charsetParam = new Pair<>(KEY_CHARSET, ENCODING);
        headers.put(HttpHeader.CONTENT_TYPE, ContentType.JSON.toHeader(charsetParam));
        headers.put(HttpHeader.X_ACCEPT, ContentType.JSON.toHeader());
        headers.put(HttpHeader.HOST, requestUrl.getHost());

        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put(AuthRequestParameter.CONSUMER_KEY, mConsumerKey.getKey());
        parameters.put(AuthRequestParameter.CODE, requestToken.getToken());
        ParametersBody body = new ParametersBody(parameters);

        HttpRequest request = new HttpRequest(HttpMethod.POST, requestUrl, headers, body);

        HttpResponse response = null;
        AccessToken accessToken = null;
        try {
            response = mHttpClient.execute(request);
            if (response.getStatusCode() == HttpURLConnection.HTTP_OK) {
                JSONObject jsonObj = response.getResponseAsJSONObject(ENCODING);
                accessToken = new AccessToken(jsonObj);
            } else {
                ErrorResponse error = new ErrorResponse(response);
                mErrorHandler.handleResponse(error);
            }
        } catch (JSONException e) {
            throw new PocketException(e.getMessage());
        } finally {
            if (response != null) {
                response.disconnect();
            }
        }

        return accessToken;
    }

    /**
     * Adds the item of the URL.
     *
     * @param accessToken The access token.
     * @param url The URL.
     * @return The response.
     * @throws IOException If a network I/O error occurs.
     * @throws InvalidRequestException If the authentication fails by invalid parameters.
     * @throws PocketException If the authentication fails by invalid response.
     */
    public AddResponse add(AccessToken accessToken, String url)
            throws IOException, InvalidRequestException, PocketException {
        return add(accessToken, url, null, null, -1);
    }

    /**
     * Adds the item with the parameters.
     *
     * @param accessToken The access token.
     * @param url The URL.
     * @param title The title of the item.
     * @param tags The tags.
     * @param tweetId The tweet ID.
     * @return The response.
     * @throws IOException If a network I/O error occurs.
     * @throws InvalidRequestException If the authentication fails by invalid parameters.
     * @throws PocketException If the authentication fails by invalid response.
     */
    public AddResponse add(AccessToken accessToken, String url, String title,
            String[] tags, long tweetId)
                    throws IOException, InvalidRequestException, PocketException {
        AddRequest.Builder builder = new AddRequest.Builder(url);

        if (title != null) {
            builder.setTitle(title);
        }

        if (tags != null) {
            builder.setTags(tags);
        }

        if (tweetId != -1) {
            builder.setTweetId(tweetId);
        }

        AddRequest addRequest = builder.build();
        return add(accessToken, addRequest);
    }

    /**
     * Adds the item with the request.
     *
     * @param accessToken The access token.
     * @param addRequest The request.
     * @return The response.
     * @throws IOException If a network I/O error occurs.
     * @throws InvalidRequestException If the authentication fails by invalid parameters.
     * @throws PocketException If the authentication fails by invalid response.
     */
    public AddResponse add(AccessToken accessToken, AddRequest addRequest)
            throws IOException, InvalidRequestException, PocketException {
        String endpoint = PocketServer.getEndpoint(RequestType.ADD);
        URL requestUrl = new URL(endpoint);
        HttpHeaders headers = new HttpHeaders();
        Pair<String, String> charsetParam = new Pair<>(KEY_CHARSET, ENCODING);
        headers.put(HttpHeader.CONTENT_TYPE, ContentType.JSON.toHeader(charsetParam));
        headers.put(HttpHeader.X_ACCEPT, ContentType.JSON.toHeader());
        headers.put(HttpHeader.HOST, requestUrl.getHost());

        addRequest.put(AddRequest.Parameter.ACCESS_TOKEN, accessToken.getToken());
        addRequest.put(AddRequest.Parameter.CONSUMER_KEY, mConsumerKey.getKey());

        MessageBody body = addRequest.getBody();

        HttpRequest request = new HttpRequest(HttpMethod.POST, requestUrl, headers, body);

        HttpResponse response = null;
        JSONObject jsonObj = null;
        Map<String, List<String>> responseHeaders = null;
        try {
            response = mHttpClient.execute(request);
            if (response.getStatusCode() == HttpURLConnection.HTTP_OK) {
                jsonObj = response.getResponseAsJSONObject(ENCODING);
                responseHeaders = response.getHeaderFields();
            } else {
                ErrorResponse error = new ErrorResponse(response);
                mErrorHandler.handleResponse(error);
            }
        } catch (JSONException e) {
            throw new PocketException(e.getMessage());
        } finally {
            if (response != null) {
                response.disconnect();
            }
        }

        AddResponse addResponse = null;
        if (jsonObj != null) {
            try {
                addResponse = new AddResponse(jsonObj, responseHeaders);
            } catch (JSONException e) {
                throw new PocketException(e.getMessage());
            }
        }

        return addResponse;
    }

    /**
     * Retrieves items for the request.
     *
     * @param accessToken The access token.
     * @param retrieveRequest The request.
     * @return The response.
     * @throws IOException If a network I/O error occurs.
     * @throws InvalidRequestException If the authentication fails by invalid parameters.
     * @throws PocketException If the authentication fails by invalid response.
     */
    public RetrieveResponse retrieve(AccessToken accessToken, RetrieveRequest retrieveRequest)
            throws IOException, InvalidRequestException, PocketException {
        String endpoint = PocketServer.getEndpoint(RequestType.RETRIEVE);
        URL requestUrl = new URL(endpoint);
        HttpHeaders headers = new HttpHeaders();
        Pair<String, String> charsetParam = new Pair<>(KEY_CHARSET, ENCODING);
        headers.put(HttpHeader.CONTENT_TYPE, ContentType.JSON.toHeader(charsetParam));
        headers.put(HttpHeader.X_ACCEPT, ContentType.JSON.toHeader());
        headers.put(HttpHeader.HOST, requestUrl.getHost());

        retrieveRequest.put(AddRequest.Parameter.ACCESS_TOKEN, accessToken.getToken());
        retrieveRequest.put(AddRequest.Parameter.CONSUMER_KEY, mConsumerKey.getKey());

        MessageBody body = retrieveRequest.getBody();

        HttpRequest request = new HttpRequest(HttpMethod.POST, requestUrl, headers, body);

        HttpResponse response = null;
        JSONObject jsonObj = null;
        Map<String, List<String>> responseHeaders = null;
        try {
            response = mHttpClient.execute(request);
            if (response.getStatusCode() == HttpURLConnection.HTTP_OK) {
                jsonObj = response.getResponseAsJSONObject(ENCODING);
                responseHeaders = response.getHeaderFields();
            } else {
                ErrorResponse error = new ErrorResponse(response);
                mErrorHandler.handleResponse(error);
            }
        } catch (JSONException e) {
            throw new PocketException(e.getMessage());
        } finally {
            if (response != null) {
                response.disconnect();
            }
        }

        RetrieveResponse retrieveResponse = null;
        if (jsonObj != null) {
            try {
                retrieveResponse = new RetrieveResponse(jsonObj, responseHeaders);
            } catch (JSONException e) {
                throw new PocketException(e.getMessage());
            }
        }

        return retrieveResponse;
    }

    /**
     * Modifies items for the request.
     *
     * @param accessToken The access token.
     * @param modifyRequest The request.
     * @return The response.
     * @throws IOException If a network I/O error occurs.
     * @throws InvalidRequestException If the authentication fails by invalid parameters.
     * @throws PocketException If the authentication fails by invalid response.
     */
    public ModifyResponse modify(AccessToken accessToken, ModifyRequest modifyRequest)
            throws IOException, InvalidRequestException, PocketException {
        String endpoint = PocketServer.getEndpoint(RequestType.MODIFY);
        URL requestUrl = new URL(endpoint);
        HttpHeaders headers = new HttpHeaders();
        Pair<String, String> charsetParam = new Pair<>(KEY_CHARSET, ENCODING);
        headers.put(HttpHeader.CONTENT_TYPE, ContentType.JSON.toHeader(charsetParam));
        headers.put(HttpHeader.X_ACCEPT, ContentType.JSON.toHeader());
        headers.put(HttpHeader.HOST, requestUrl.getHost());

        modifyRequest.put(AddRequest.Parameter.ACCESS_TOKEN, accessToken.getToken());
        modifyRequest.put(AddRequest.Parameter.CONSUMER_KEY, mConsumerKey.getKey());

        MessageBody body = modifyRequest.getBody();

        HttpRequest request = new HttpRequest(HttpMethod.POST, requestUrl, headers, body);

        HttpResponse response = null;
        JSONObject jsonObj = null;
        Map<String, List<String>> responseHeaders = null;
        try {
            response = mHttpClient.execute(request);
            if (response.getStatusCode() == HttpURLConnection.HTTP_OK) {
                jsonObj = response.getResponseAsJSONObject(ENCODING);
                responseHeaders = response.getHeaderFields();
            } else {
                ErrorResponse error = new ErrorResponse(response);
                mErrorHandler.handleResponse(error);
            }
        } catch (JSONException e) {
            throw new PocketException(e.getMessage());
        } finally {
            if (response != null) {
                response.disconnect();
            }
        }

        ModifyResponse modifyResponse = null;
        if (jsonObj != null) {
            try {
                modifyResponse = new ModifyResponse(jsonObj, responseHeaders);
            } catch (JSONException e) {
                throw new PocketException(e.getMessage());
            }
        }

        return modifyResponse;
    }
}
