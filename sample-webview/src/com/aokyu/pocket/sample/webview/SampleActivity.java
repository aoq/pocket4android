/*
 * Copyright (c) 2015 Yu AOKI
 *
 * This software may be modified and distributed under the terms
 * of the MIT license.  See the LICENSE file for details.
 */

package com.aokyu.pocket.sample.webview;

import com.aokyu.pocket.AccessToken;
import com.aokyu.pocket.AuthorizationCallback;
import com.aokyu.pocket.ConsumerKey;
import com.aokyu.pocket.PocketClient;
import com.aokyu.pocket.PocketServer;
import com.aokyu.pocket.RequestToken;
import com.aokyu.pocket.RetrieveRequest;
import com.aokyu.pocket.RetrieveRequestParameter;
import com.aokyu.pocket.RetrieveResponse;
import com.aokyu.pocket.content.Page;
import com.aokyu.pocket.error.InvalidRequestException;
import com.aokyu.pocket.error.PocketException;
import com.aokyu.pocket.http.ContentType;
import com.aokyu.pocket.http.HttpClient;
import com.aokyu.pocket.http.HttpHeader;
import com.aokyu.pocket.http.HttpHeaders;
import com.aokyu.pocket.http.HttpMethod;
import com.aokyu.pocket.http.HttpRequest;
import com.aokyu.pocket.http.HttpResponse;
import com.aokyu.pocket.http.content.ParametersBody;
import com.aokyu.pocket.sample.webview.AuthFragment.AuthListener;
import com.aokyu.pocket.util.PocketUtils;

import org.json.JSONException;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * Insert your Pocket consumer key at:
 * <p>
 * <code>private static final String CONSUMER_KEY = "YOUR-CONSUMER-KEY";</code>
 * </p>
 * and modify AndroidManifest.xml at:
 * <p>
 * <data android:scheme="pocketappXXXXX" />
 * </p>
 * "XXXXX" should be replaced with the 5 digit number at the start of your consumer key.
 */
public class SampleActivity extends AppCompatActivity
        implements AuthorizationCallback, AuthListener {

    private Context mContext;
    private FragmentManager mFragmentManager;
    private SharedPreferences mPreferences;

    private final class PreferenceKey {

        public static final String ACCESS_TOKEN = "access_token";
        public static final String USERNAME = "username";

        private PreferenceKey() {
        }
    }

    private LinearLayout mButtonLayout;
    private Button mLoginButton;
    private Button mRetrieveButton;
    private RelativeLayout mContainerView;

    private static final String CONSUMER_KEY = "11153-d287068d2f761d0342c329c1";

    private PocketClient mClient;
    private ConsumerKey mConsumerKey = new ConsumerKey(CONSUMER_KEY);

    private RequestToken mRequestToken;
    private AccessToken mAccessToken;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_sample);
        mContext = getApplicationContext();
        mFragmentManager = getSupportFragmentManager();
        mPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        mClient = new PocketClient(mConsumerKey);
        setupViews();
    }

    private void setupViews() {
        mButtonLayout = (LinearLayout) findViewById(R.id.button_layout);
        mLoginButton = (Button) findViewById(R.id.login_button);
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProgressDialog();
                new AsyncTask<Void, Void, Void>() {

                    @Override
                    protected Void doInBackground(Void... params) {
                        try {
                            mClient.authorize(SampleActivity.this);
                        } catch (IOException e) {
                            Log.d("PocketSample", "Network I/O error : " + e.getMessage());
                        } catch (InvalidRequestException e) {
                            Log.d("PocketSample", "Invalid request : " + e.getMessage());
                        } catch (PocketException e) {
                            Log.d("PocketSample", "Error : " + e.getMessage());
                        }
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void result) {
                        hideProgressDialog();
                    }

                }.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
            }
        });

        mRetrieveButton = (Button) findViewById(R.id.retrieve_button);
        mRetrieveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProgressDialog();
                new AsyncTask<Void, Void, RetrieveResponse>() {

                    @Override
                    protected RetrieveResponse doInBackground(Void... params) {
                        RetrieveResponse response = null;
                        try {
                            RetrieveRequest request = new RetrieveRequest.Builder()
                                    .setContentType(RetrieveRequestParameter.ContentType.ARTICLE)
                                    .setDetailType(RetrieveRequestParameter.DetailType.SIMPLE)
                                    .setCount(10)
                                    .setOffset(0)
                                    .build();
                            response = mClient.retrieve(mAccessToken, request);
                        } catch (IOException e) {
                        } catch (InvalidRequestException e) {
                        } catch (PocketException e) {
                        }
                        return response;
                    }

                    @Override
                    protected void onPostExecute(RetrieveResponse result) {
                        List<Page> pages = null;
                        try {
                            if (result != null) {
                                pages = result.getItems();
                            }
                        } catch (JSONException e) {
                            Log.d("PocketSample", "Invalid JSON : " + e.getMessage());
                        }

                        if (pages != null) {
                            for (Page page : pages) {
                                Log.d("PocketSample", "TITLE=" + page.getResolvedTitle()
                                        + ", URL=" + page.getResolvedUrl()
                                        + ", EXCERPT=" + page.getExcerpt());
                            }
                        }

                        hideProgressDialog();
                    }

                }.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
            }
        });
        mContainerView = (RelativeLayout) findViewById(R.id.container_view);

        String token = mPreferences.getString(PreferenceKey.ACCESS_TOKEN, null);
        String username = mPreferences.getString(PreferenceKey.USERNAME, null);
        if (!TextUtils.isEmpty(token) || !TextUtils.isEmpty(username)) {
            mAccessToken = new AccessToken(token, username);
            mRetrieveButton.setEnabled(true);
        } else {
            mRetrieveButton.setEnabled(false);
        }
    }

    @Override
    public void onRequested(ConsumerKey consumerKey, RequestToken requestToken) {
        mRequestToken = requestToken;
    }

    @Override
    public Activity onRequestContinued() {
        if (mRequestToken == null) {
            return null;
        }

        final String url = PocketServer.getRedirectUrl(mConsumerKey, mRequestToken);
        CookieSyncManager cookieSyncManager = CookieSyncManager.createInstance(mContext);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        String cookies = cookieManager.getCookie(url);
        try {
            HttpClient client = new HttpClient();
            URL requestUrl = new URL(url);
            HttpHeaders headers = new HttpHeaders();
            headers.put(HttpHeader.HOST, requestUrl.getHost());
            headers.put(HttpHeader.CONTENT_TYPE, ContentType.X_WWW_FORM_URLENCODED_UTF8.get());
            headers.put(HttpHeader.X_ACCEPT, ContentType.X_WWW_FORM_URLENCODED.get());
            headers.put(HttpHeader.COOKIE, cookies);
            ParametersBody body = new ParametersBody();
            HttpRequest request = new HttpRequest(HttpMethod.POST, requestUrl, headers, body);
            HttpResponse response = client.execute(request);
            response.setFollowRedirects(false);
            int statusCode = response.getStatusCode();
            if (statusCode == HttpURLConnection.HTTP_MOVED_TEMP) {
                String location = response.getHeaderField(HttpHeader.LOCATION);
                if (location != null && location.startsWith(PocketUtils.getAppId(mConsumerKey))) {
                    onAuthorizationFinished(null);
                    return null;
                }
            }
        } catch (MalformedURLException e) {
            Log.d("PocketSample", "Invalid URL : " + e.getMessage());
        } catch (IOException e) {
            Log.d("PocketSample", "Network I/O error : " + e.getMessage());
        }

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                showAuthFragment(url);
            }
        });

        return null;
    }

    private void showAuthFragment(String url) {
        AuthFragment fragment = (AuthFragment) mFragmentManager.findFragmentByTag(
                AuthFragment.TAG);
        if (fragment == null) {
            fragment = AuthFragment.newInstance();
            Bundle args = new Bundle();
            args.putString(AuthFragment.Argument.URL, url);
            fragment.setArguments(args);
        }

        mContainerView.setVisibility(View.VISIBLE);
        mButtonLayout.setVisibility(View.GONE);
        showFragment(fragment, AuthFragment.TAG);
    }

    private void showFragment(Fragment fragment, String tag) {
        if (fragment.isAdded()) {
            return;
        }

        if (fragment.isVisible()) {
            return;
        }

        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        transaction.replace(R.id.container_view, fragment, tag);
        transaction.commit();
    }


    @Override
    public void onAuthorizationFinished() {
        hideAuthFragment();
        onAuthorizationFinished(mRequestToken);
    }

    private void hideAuthFragment() {
        String tag = AuthFragment.TAG;
        AuthFragment fragment = (AuthFragment) mFragmentManager.findFragmentByTag(tag);
        if (fragment == null) {
            return;
        }

        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        transaction.remove(fragment);
        transaction.commit();
    }

    @Override
    public void onAuthorizationFinished(RequestToken requestToken) {
        mButtonLayout.setVisibility(View.VISIBLE);
        mContainerView.setVisibility(View.GONE);
        showProgressDialog();
        new AsyncTask<Void, Void, AccessToken>() {

            @Override
            protected AccessToken doInBackground(Void... params) {
                AccessToken token = null;
                try {
                    token = mClient.authenticate(mRequestToken);
                } catch (IOException e) {
                    Log.d("PocketSample", "Network I/O error : " + e.getMessage());
                } catch (InvalidRequestException e) {
                    Log.d("PocketSample", "Invalid request : " + e.getMessage());
                } catch (PocketException e) {
                    Log.d("PocketSample", "Error : " + e.getMessage());
                }
                return token;
            }

            @Override
            protected void onPostExecute(AccessToken result) {
                if (result != null) {
                    String token = result.get();
                    String username = result.getUsername();
                    mPreferences.edit()
                            .putString(PreferenceKey.ACCESS_TOKEN, token)
                            .putString(PreferenceKey.USERNAME, username)
                            .apply();
                    if (!TextUtils.isEmpty(token)) {
                        mAccessToken = result;
                        mRetrieveButton.setEnabled(true);
                    }
                }
                hideProgressDialog();
            }

        }.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
    }

    private void showProgressDialog() {
        FragmentManager manager = getSupportFragmentManager();

        ProgressDialogFragment fragment =
                (ProgressDialogFragment) manager.findFragmentByTag(ProgressDialogFragment.TAG);
        if (fragment == null) {
            fragment = ProgressDialogFragment.newInstance();
        }

        if (!fragment.isAdded()) {
            fragment.show(manager, ProgressDialogFragment.TAG);
        }
    }

    private void hideProgressDialog() {
        FragmentManager manager = getSupportFragmentManager();

        ProgressDialogFragment fragment =
                (ProgressDialogFragment) manager.findFragmentByTag(ProgressDialogFragment.TAG);
        if (fragment != null) {
            fragment.dismissAllowingStateLoss();
        }
    }
}
