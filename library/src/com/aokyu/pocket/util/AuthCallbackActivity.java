/*
 * Copyright (c) 2012-2014 Yu AOKI
 *
 * This software may be modified and distributed under the terms
 * of the MIT license.  See the LICENSE file for details.
 */

package com.aokyu.pocket.util;

import com.aokyu.pocket.AuthorizationCallback;
import com.aokyu.pocket.ConsumerKey;
import com.aokyu.pocket.RequestToken;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

/**
 * The utility class to handle authorization callbacks in an {@link Activity}.
 * @see AuthCallbackCompatActivity
 */
public abstract class AuthCallbackActivity extends Activity implements AuthorizationCallback {

    private ConsumerKey mConsumerKey;

    private RequestToken mRequestToken;

    /**
     * Called when the authorization was finished with the request token.
     *
     * @param consumerKey The consumer key.
     * @param requestToken The request token.
     */
    public abstract void onAuthorizationFinished(ConsumerKey consumerKey,
            RequestToken requestToken);

    @Override
    public boolean onRequestTokenRetrieved(Context context, ConsumerKey consumerKey,
            RequestToken requestToken) {
        mConsumerKey = consumerKey;
        mRequestToken = requestToken;
        return false;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        Uri uri = intent.getData();
        if (uri != null) {
            String callbackUri = uri.toString();
            if (callbackUri.startsWith(PocketUtils.getAppId(mConsumerKey))) {
                onAuthorizationFinished(mConsumerKey, mRequestToken);
            }
        }
    }
}
