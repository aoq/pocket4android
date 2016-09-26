/*
 * Copyright (c) 2012-2014 Yu AOKI
 *
 * This software may be modified and distributed under the terms
 * of the MIT license.  See the LICENSE file for details.
 */

package com.aokyu.pocket;

import com.aokyu.pocket.error.PocketException;

import java.util.List;
import java.util.Map;

/**
 * This class holds the rate limit for a user. Each user is limited to 320 API calls per hour.
 * @see <a href="https://getpocket.com/developer/docs/rate-limits">Developer API - Rate Limits</a>
 */
public class UserLimit {

    /**
     * The header key for the current rate limit enforced enforced per user.
     */
    private static final String USER_LIMIT = "X-Limit-User-Limit";

    /**
     * The header key for the number of calls remaining before hitting user's rate limit.
     */
    private static final String USER_REMAINING = "X-Limit-User-Remaining";

    /**
     * The header key for seconds until user's rate limit resets.
     */
    private static final String USER_RESET = "X-Limit-User-Reset";

    private RateLimit mLimit;

    public UserLimit(Map<String, List<String>> headerFields) throws PocketException {
        List<String> limitField = headerFields.get(USER_LIMIT);
        int limit = getValue(limitField);
        List<String> remainingField = headerFields.get(USER_REMAINING);
        int remaining = getValue(remainingField);
        List<String> resetField = headerFields.get(USER_RESET);
        int reset = getValue(resetField);
        mLimit = new RateLimit(limit, remaining, reset);
    }

    private int getValue(List<String> values) throws PocketException {
        int size = values.size();
        if (size > 0) {
            String limit = values.get(0);
            try {
                return Integer.parseInt(limit);
            } catch (NumberFormatException e) {
                throw new PocketException("cannot parse rate limit");
            }
        } else {
            throw new PocketException("cannot get rate limit");
        }
    }

    /**
     * Returns the current rate limit enforced per user.
     *
     * @return The current rate limit.
     */
    public int getLimit() {
        return mLimit.getLimit();
    }

    /**
     * Returns the number of calls remaining before hitting user's rate limit.
     *
     * @return The number of calls remaining.
     */
    public int getRemainingCalls() {
        return mLimit.getRemainingCalls();
    }

    /**
     * Returns the time until user's rate limit resets, in seconds.
     *
     * @return The time until user's rate limit resets.
     */
    public long getResetTime() {
        return mLimit.getResetTime();
    }

    @Override
    public String toString() {
        return "[User] " + mLimit.toString();
    }
}
