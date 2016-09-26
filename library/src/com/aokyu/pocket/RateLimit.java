/*
 * Copyright (c) 2012-2014 Yu AOKI
 *
 * This software may be modified and distributed under the terms
 * of the MIT license.  See the LICENSE file for details.
 */

package com.aokyu.pocket;

import java.util.concurrent.TimeUnit;

/**
 * This class holds information of a rate limit.
 */
public class RateLimit {

    public static final int INVALID_TIME = -1;

    private int mLimit;
    private int mRemaining;
    private int mResetSeconds;

    private long mResetTime;

    public RateLimit(int limit, int remaining, int resetSeconds) {
        mLimit = limit;
        mRemaining = remaining;
        mResetSeconds = resetSeconds;

        if (resetSeconds != INVALID_TIME) {
            long currentMillis = System.currentTimeMillis();
            long resetMillis = TimeUnit.SECONDS.toMillis(mResetSeconds);
            mResetTime = currentMillis + resetMillis;
        } else {
            mResetTime = INVALID_TIME;
        }
    }

    /**
     * Returns the current rate limit.
     *
     * @return The current rate limit.
     */
    public int getLimit() {
        return mLimit;
    }

    /**
     * Returns the number of calls remaining before hitting the rate limit.
     *
     * @return The number of calls remaining before hitting the rate limit.
     */
    public int getRemainingCalls() {
        return mRemaining;
    }

    /**
     * Returns the time until rate limit resets, in seconds.
     *
     * @return The time until rate limit resets, in seconds.
     */
    public long getResetTime() {
        return mResetTime;
    }

    @Override
    public String toString() {
        return new StringBuilder().append("Limit: ")
                .append(mLimit)
                .append(", ")
                .append("Remaining API calls: ")
                .append(mRemaining)
                .append(", ")
                .append("Reset time: " + mResetSeconds)
                .toString();
    }
}
