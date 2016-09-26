/*
 * Copyright (c) 2016 Yu AOKI
 *
 * This software may be modified and distributed under the terms
 * of the MIT license. See the LICENSE file for details.
 */

package com.aokyu.pocket.sample.browser;

import com.aokyu.pocket.AccessToken;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import android.text.TextUtils;

public class AppSettings {

    private final class PreferenceKey {

        public static final String ACCESS_TOKEN = "access_token";
        public static final String USERNAME = "username";

        private PreferenceKey() {
        }
    }

    private static volatile AppSettings sSettings = null;

    private SharedPreferences mPreferences;

    public static AppSettings getInstance(Context context) {
        if (sSettings == null) {
            synchronized (AppSettings.class) {
                sSettings = new AppSettings(context);
            }
        }
        return sSettings;
    }

    private AppSettings(Context context) {
        mPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public void setAccessToken(AccessToken accessToken) {
        String username = accessToken.getUsername();
        String token = accessToken.getToken();
        Editor editor = mPreferences.edit();

        if (!TextUtils.isEmpty(username)) {
            editor.putString(PreferenceKey.USERNAME, username);
        }

        if (!TextUtils.isEmpty(token)) {
            editor.putString(PreferenceKey.ACCESS_TOKEN, token);
        }

        editor.apply();
    }

    public AccessToken getAccessToken() {
        String token = mPreferences.getString(PreferenceKey.ACCESS_TOKEN, null);
        String username = mPreferences.getString(PreferenceKey.USERNAME, null);
        if (!TextUtils.isEmpty(token) && !TextUtils.isEmpty(username)) {
            return new AccessToken(token, username);
        }

        return null;
    }
}
