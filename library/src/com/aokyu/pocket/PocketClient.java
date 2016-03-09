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
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PocketClient {

    private HttpClient mClient;
    private ConsumerKey mConsumerKey;

    private ErrorHandler mErrorHandler;

    public PocketClient(ConsumerKey consumerKey) {
        mClient = new HttpClient();
        mConsumerKey = consumerKey;
        mErrorHandler = new ErrorHandler();
    }

    public void authorize(AuthorizationCallback callback)
            throws IOException, InvalidRequestException, PocketException {
        RequestToken requestToken = retrieveRequestToken();
        callback.onRequested(mConsumerKey, requestToken);
        Activity activity = callback.onRequestContinued();
        if (activity != null) {
            continueAuthorization(activity, requestToken);
        } else {
            throw new InvalidRequestException("no activity for callback");
        }
    }

    public AccessToken authenticate(RequestToken requestToken)
            throws IOException, InvalidRequestException, PocketException {
        String endpoint = PocketServer.getEndpoint(RequestType.OAUTH_AUTHORIZE);
        URL requestUrl = new URL(endpoint);
        HttpHeaders headers = new HttpHeaders();
        headers.put(HttpHeader.CONTENT_TYPE, ContentType.JSON_UTF8.get());
        headers.put(HttpHeader.X_ACCEPT, ContentType.JSON.get());
        headers.put(HttpHeader.HOST, requestUrl.getHost());

        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put(AuthRequestParameter.CONSUMER_KEY, mConsumerKey.get());
        parameters.put(AuthRequestParameter.CODE, requestToken.get());
        ParametersBody body = new ParametersBody(parameters);

        HttpRequest request = new HttpRequest(HttpMethod.POST, requestUrl, headers, body);

        HttpResponse response = null;
        AccessToken accessToken = null;
        try {
            response = mClient.execute(request);
            if (response.getStatusCode() == HttpURLConnection.HTTP_OK) {
                JSONObject jsonObj = response.getResponseAsJSONObject();
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

    private RequestToken retrieveRequestToken()
            throws IOException, InvalidRequestException, PocketException {
        String endpoint = PocketServer.getEndpoint(RequestType.OAUTH_REQUEST);
        URL requestUrl = new URL(endpoint);
        HttpHeaders headers = new HttpHeaders();
        headers.put(HttpHeader.CONTENT_TYPE, ContentType.JSON_UTF8.get());
        headers.put(HttpHeader.X_ACCEPT, ContentType.JSON.get());
        headers.put(HttpHeader.HOST, requestUrl.getHost());

        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put(AuthRequestParameter.CONSUMER_KEY, mConsumerKey.get());
        parameters.put(AuthRequestParameter.REDIRECT_URI, PocketUtils.getRedirectUri(mConsumerKey));
        ParametersBody body = new ParametersBody(parameters);

        HttpRequest request = new HttpRequest(HttpMethod.POST, requestUrl, headers, body);

        HttpResponse response = null;
        RequestToken requestToken = null;
        try {
            response = mClient.execute(request);
            if (response.getStatusCode() == HttpURLConnection.HTTP_OK) {
                JSONObject jsonObj = response.getResponseAsJSONObject();
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

    private void continueAuthorization(Activity callback, RequestToken requestToken) {
        String url = PocketServer.getRedirectUrl(mConsumerKey, requestToken);
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY|Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP);
        callback.startActivity(intent);
    }

    public AddResponse add(AccessToken accessToken, String url)
            throws IOException, InvalidRequestException, PocketException {
        return add(accessToken, url, null, null, -1);
    }

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

    public AddResponse add(AccessToken accessToken, AddRequest addRequest)
            throws IOException, InvalidRequestException, PocketException {
        String endpoint = PocketServer.getEndpoint(RequestType.ADD);
        URL requestUrl = new URL(endpoint);
        HttpHeaders headers = new HttpHeaders();
        headers.put(HttpHeader.CONTENT_TYPE, ContentType.JSON_UTF8.get());
        headers.put(HttpHeader.X_ACCEPT, ContentType.JSON.get());
        headers.put(HttpHeader.HOST, requestUrl.getHost());

        addRequest.put(AddRequest.Parameter.ACCESS_TOKEN, accessToken.get());
        addRequest.put(AddRequest.Parameter.CONSUMER_KEY, mConsumerKey.get());

        MessageBody body = addRequest.getBody();

        HttpRequest request = new HttpRequest(HttpMethod.POST, requestUrl, headers, body);

        HttpResponse response = null;
        JSONObject jsonObj = null;
        Map<String, List<String>> responseHeaders = null;
        try {
            response = mClient.execute(request);
            if (response.getStatusCode() == HttpURLConnection.HTTP_OK) {
                jsonObj = response.getResponseAsJSONObject();
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

    public RetrieveResponse retrieve(AccessToken accessToken, RetrieveRequest retrieveRequest)
            throws IOException, InvalidRequestException, PocketException {
        String endpoint = PocketServer.getEndpoint(RequestType.RETRIEVE);
        URL requestUrl = new URL(endpoint);
        HttpHeaders headers = new HttpHeaders();
        headers.put(HttpHeader.CONTENT_TYPE, ContentType.JSON_UTF8.get());
        headers.put(HttpHeader.X_ACCEPT, ContentType.JSON.get());
        headers.put(HttpHeader.HOST, requestUrl.getHost());

        retrieveRequest.put(AddRequest.Parameter.ACCESS_TOKEN, accessToken.get());
        retrieveRequest.put(AddRequest.Parameter.CONSUMER_KEY, mConsumerKey.get());

        MessageBody body = retrieveRequest.getBody();

        HttpRequest request = new HttpRequest(HttpMethod.POST, requestUrl, headers, body);

        HttpResponse response = null;
        JSONObject jsonObj = null;
        Map<String, List<String>> responseHeaders = null;
        try {
            response = mClient.execute(request);
            if (response.getStatusCode() == HttpURLConnection.HTTP_OK) {
                jsonObj = response.getResponseAsJSONObject();
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

    public ModifyResponse modify(AccessToken accessToken, ModifyRequest modifyRequest)
            throws IOException, InvalidRequestException, PocketException {
        String endpoint = PocketServer.getEndpoint(RequestType.MODIFY);
        URL requestUrl = new URL(endpoint);
        HttpHeaders headers = new HttpHeaders();
        headers.put(HttpHeader.CONTENT_TYPE, ContentType.JSON_UTF8.get());
        headers.put(HttpHeader.X_ACCEPT, ContentType.JSON.get());
        headers.put(HttpHeader.HOST, requestUrl.getHost());

        modifyRequest.put(AddRequest.Parameter.ACCESS_TOKEN, accessToken.get());
        modifyRequest.put(AddRequest.Parameter.CONSUMER_KEY, mConsumerKey.get());

        MessageBody body = modifyRequest.getBody();

        HttpRequest request = new HttpRequest(HttpMethod.POST, requestUrl, headers, body);

        HttpResponse response = null;
        JSONObject jsonObj = null;
        Map<String, List<String>> responseHeaders = null;
        try {
            response = mClient.execute(request);
            if (response.getStatusCode() == HttpURLConnection.HTTP_OK) {
                jsonObj = response.getResponseAsJSONObject();
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
