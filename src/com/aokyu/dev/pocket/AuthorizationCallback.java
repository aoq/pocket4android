/*
 * Copyright 2012 Yu AOKI
 */

package com.aokyu.dev.pocket;

import android.app.Activity;

public interface AuthorizationCallback {
    void onRequested(ConsumerKey consumerKey, RequestToken requestToken);
    Activity onRequestContinued();
    void onAuthorizationFinished(RequestToken requestToken);
}
