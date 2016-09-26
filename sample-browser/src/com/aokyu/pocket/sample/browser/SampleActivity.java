/*
 * Copyright (c) 2015 Yu AOKI
 *
 * This software may be modified and distributed under the terms
 * of the MIT license.  See the LICENSE file for details.
 */

package com.aokyu.pocket.sample.browser;

import com.aokyu.pocket.AccessToken;
import com.aokyu.pocket.ConsumerKey;
import com.aokyu.pocket.PocketClient;
import com.aokyu.pocket.RequestToken;
import com.aokyu.pocket.RetrieveRequest;
import com.aokyu.pocket.RetrieveRequestParameter;
import com.aokyu.pocket.RetrieveResponse;
import com.aokyu.pocket.content.Page;
import com.aokyu.pocket.error.InvalidRequestException;
import com.aokyu.pocket.error.PocketException;
import com.aokyu.pocket.util.AuthCallbackCompatActivity;

import org.json.JSONException;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.IOException;
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
public class SampleActivity extends AuthCallbackCompatActivity {

    private Context mContext;
    private AppSettings mSettings;

    private final class PreferenceKey {

        public static final String ACCESS_TOKEN = "access_token";
        public static final String USERNAME = "username";

        private PreferenceKey() {
        }
    }

    private Button mLoginButton;
    private Button mRetrieveButton;

    private static final String CONSUMER_KEY = "YOUR-CONSUMER-KEY";

    private PocketClient mClient;
    private ConsumerKey mConsumerKey = new ConsumerKey(CONSUMER_KEY);

    private AccessToken mAccessToken;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_sample);
        mContext = getApplicationContext();
        mSettings = AppSettings.getInstance(mContext);
        mClient = new PocketClient(mConsumerKey);
        setupViews();
    }

    private void setupViews() {
        mLoginButton = (Button) findViewById(R.id.login_button);
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AsyncTask<Void, Void, Void>() {

                    @Override
                    public void onPreExecute() {
                        showProgressDialog();
                    }

                    @Override
                    protected Void doInBackground(Void... params) {
                        try {
                            mClient.authorize(SampleActivity.this, SampleActivity.this);
                        } catch (IOException e) {
                        } catch (InvalidRequestException e) {
                        } catch (PocketException e) {
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
                new AsyncTask<Void, Void, RetrieveResponse>() {

                    @Override
                    public void onPreExecute() {
                        showProgressDialog();
                    }

                    @Override
                    protected RetrieveResponse doInBackground(Void... params) {
                        AccessToken token = null;
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


        mAccessToken = mSettings.getAccessToken();
        mRetrieveButton.setEnabled(mAccessToken != null);
    }

    public void onAuthorizationFinished(ConsumerKey consumerKey, final RequestToken requestToken) {
        new AsyncTask<Void, Void, AccessToken>() {

            @Override
            protected AccessToken doInBackground(Void... params) {
                AccessToken token = null;
                try {
                    token = mClient.authenticate(requestToken);
                } catch (IOException e) {
                } catch (InvalidRequestException e) {
                } catch (PocketException e) {
                }
                return token;
            }

            @Override
            protected void onPostExecute(AccessToken result) {
                if (result != null) {
                    mSettings.setAccessToken(result);
                    mAccessToken = result;
                    mRetrieveButton.setEnabled(true);
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
