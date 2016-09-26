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
 * This class holds the rate limit for an application.
 * Each application is limited to 10,000 calls per hour.
 * @see <a href="https://getpocket.com/developer/docs/rate-limits">Developer API - Rate Limits</a>
 */
public class ClientLimit {

    /**
     * The header key for the current rate limit enforced per consumer key.
     */
    private static final String CLIENT_LIMIT = "X-Limit-Key-Limit";

    /**
     * The header key for the number of calls remaining before hitting consumer key's rate limit.
     */
    private static final String CLIENT_REMAINING = "X-Limit-Key-Remaining";

    /**
     * The header key for seconds until consumer key's rate limit resets.
     */
    private static final String CLIENT_RESET = "X-Limit-Key-Reset";

    private RateLimit mLimit;

    public ClientLimit(Map<String, List<String>> headerFields) throws PocketException {
        List<String> limitField = headerFields.get(CLIENT_LIMIT);
        int limit = getValue(limitField);
        List<String> remainingField = headerFields.get(CLIENT_REMAINING);
        int remaining = getValue(remainingField);
        List<String> resetField = headerFields.get(CLIENT_RESET);
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
     * Returns the current rate limit enforced per consumer key.
     *
     * @return The current rate limit.
     */
    public int getLimit() {
        return mLimit.getLimit();
    }

    /**
     * Returns the number of calls remaining before hitting consumer key's rate limit.
     *
     * @return The number of calls remaining.
     */
    public int getRemainingCalls() {
        return mLimit.getRemainingCalls();
    }

    /**
     * Returns the time until consumer key's rate limit resets, in seconds.
     *
     * @return The time until consumer key's rate limit resets.
     */
    public long getResetTime() {
        return mLimit.getResetTime();
    }

    @Override
    public String toString() {
        return "[Client] " + mLimit.toString();
    }
}
