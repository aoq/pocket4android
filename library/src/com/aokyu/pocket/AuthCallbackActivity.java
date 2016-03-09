/*
 * Copyright (c) 2012-2014 Yu AOKI
 *
 * This software may be modified and distributed under the terms
 * of the MIT license.  See the LICENSE file for details.
 */

package com.aokyu.pocket;

import com.aokyu.pocket.util.PocketUtils;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

public abstract class AuthCallbackActivity extends Activity implements AuthorizationCallback {

    private ConsumerKey mConsumerKey;
    private RequestToken mRequestToken;

    public abstract void onAuthorizationFinished(RequestToken requestToken);

    @Override
    public void onRequested(ConsumerKey consumerKey, RequestToken requestToken) {
        mConsumerKey = consumerKey;
        mRequestToken = requestToken;
    }

    @Override
    public Activity onRequestContinued() {
        return this;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        Uri uri = intent.getData();
        if (uri != null) {
            String callbackUri = uri.toString();
            if (callbackUri.startsWith(PocketUtils.getAppId(mConsumerKey))) {
                onAuthorizationFinished(mRequestToken);
            }
        }
    }
}
