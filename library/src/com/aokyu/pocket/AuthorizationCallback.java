/*
 * Copyright (c) 2012-2014 Yu AOKI
 *
 * This software may be modified and distributed under the terms
 * of the MIT license.  See the LICENSE file for details.
 */

package com.aokyu.pocket;

import android.content.Context;

/**
 * The callback interface to receive notification of authorization status.
 */
public interface AuthorizationCallback {

    /**
     * Called when the request token was retrieved.
     * If you return false for this method, a browser application will start for authorization.
     * Note that this callback will be called synchronously with
     * {@link PocketClient#authorize(Context, AuthorizationCallback)}.
     *
     * @param context The context to start a browser application.
     * @param consumerKey The consumer key.
     * @param requestToken The request token.
     * @return true if you have consumed the request, false if you haven't.
     * @see PocketClient#authorize(Context, AuthorizationCallback)
     */
    boolean onRequestTokenRetrieved(Context context, ConsumerKey consumerKey,
            RequestToken requestToken);
}
