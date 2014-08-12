/*
 * Copyright (c) 2012-2014 Yu AOKI
 *
 * This software may be modified and distributed under the terms
 * of the MIT license.  See the LICENSE file for details.
 */

package com.aokyu.dev.pocket;

import com.aokyu.dev.pocket.error.PocketException;

import java.util.List;
import java.util.Map;

public class UserLimit {

    private static final String USER_LIMIT = "X-Limit-User-Limit";
    private static final String USER_REMAINING = "X-Limit-User-Remaining";
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

    public int getLimit() {
        return mLimit.getLimit();
    }

    public int getRemainingCalls() {
        return mLimit.getRemainingCalls();
    }

    public long getResetTime() {
        return mLimit.getResetTime();
    }
}
