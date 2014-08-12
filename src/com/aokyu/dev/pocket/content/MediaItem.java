/*
 * Copyright (c) 2012-2014 Yu AOKI
 *
 * This software may be modified and distributed under the terms
 * of the MIT license.  See the LICENSE file for details.
 */

package com.aokyu.dev.pocket.content;

/* package */ class MediaItem extends PocketItem {

    private long mId;
    private String mSource;
    private int mWidth;
    private int mHeight;

    public MediaItem(String uid) {
        super(uid);
    }

    /* package */ void setId(long mediaId) {
        mId = mediaId;
    }

    public long getId() {
        return mId;
    }

    /* package */ void setSource(String source) {
        mSource = source;
    }

    public String getSource() {
        return mSource;
    }

    /* package */ void setWidth(int width) {
        mWidth = width;
    }

    public int getWidth() {
        return mWidth;
    }

    /* package */ void setHeight(int height) {
        mHeight = height;
    }

    public int getHeight() {
        return mHeight;
    }

    /* package */ void setSize(int width, int height) {
        mWidth = width;
        mHeight = height;
    }
}
