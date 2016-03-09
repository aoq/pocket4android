/*
 * Copyright (c) 2012-2014 Yu AOKI
 *
 * This software may be modified and distributed under the terms
 * of the MIT license.  See the LICENSE file for details.
 */

package com.aokyu.pocket;

import android.app.Activity;

public interface AuthorizationCallback {
    void onRequested(ConsumerKey consumerKey, RequestToken requestToken);
    Activity onRequestContinued();
    void onAuthorizationFinished(RequestToken requestToken);
}
