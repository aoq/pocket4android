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

public class ClientLimit {

    private static final String CLIENT_LIMIT = "X-Limit-Key-Limit";
    private static final String CLIENT_REMAINING = "X-Limit-Key-Remaining";
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
