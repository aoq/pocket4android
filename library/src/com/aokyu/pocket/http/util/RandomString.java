/*
 * Copyright (c) 2015 Yu AOKI
 *
 * This software may be modified and distributed under the terms
 * of the MIT license. See the LICENSE file for details.
 */

package com.aokyu.pocket.http.util;

import java.util.Random;

/**
 * This class is used to generate a random string.
 */
public class RandomString {

    private final int mMinimumLength;
    private final int mMaximumLength;

    private final static char[] AVAILABLE_CHARS =
            "-_1234567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"
            .toCharArray();

    private final Random mRandom;

    public RandomString(int minLength, int maxLength) {
        mRandom = new Random();
        mMinimumLength = minLength;
        mMaximumLength = maxLength;
    }

    public String get() {
        StringBuilder builder = new StringBuilder();
        int count = mMinimumLength;
        int additionalLength = mMaximumLength - mMinimumLength + 1;
        count += mRandom.nextInt(additionalLength);
        int length = AVAILABLE_CHARS.length;
        for (int i = 0; i < count; i++) {
            int index = mRandom.nextInt(length);
            char character = AVAILABLE_CHARS[index];
            builder.append(character);
        }
        return builder.toString();
    }
}
