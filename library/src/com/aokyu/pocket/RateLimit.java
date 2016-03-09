/*
 * Copyright (c) 2012-2014 Yu AOKI
 *
 * This software may be modified and distributed under the terms
 * of the MIT license.  See the LICENSE file for details.
 */

package com.aokyu.pocket;

import java.util.concurrent.TimeUnit;

public class RateLimit {

    public static final int INVALID_LIMIT = -1;

    private int mLimit;
    private int mRemaining;
    private int mResetSeconds;

    private long mResetTime;

    public RateLimit(int limit, int remaining, int resetSeconds) {
        mLimit = limit;
        mRemaining = remaining;
        mResetSeconds = resetSeconds;

        if (resetSeconds != INVALID_LIMIT) {
            long currentMillis = System.currentTimeMillis();
            long resetMillis = TimeUnit.SECONDS.toMillis(mResetSeconds);
            mResetTime = currentMillis + resetMillis;
        } else {
            mResetTime = INVALID_LIMIT;
        }
    }

    public int getLimit() {
        return mLimit;
    }

    public int getRemainingCalls() {
        return mRemaining;
    }

    public long getResetTime() {
        return mResetTime;
    }
}
