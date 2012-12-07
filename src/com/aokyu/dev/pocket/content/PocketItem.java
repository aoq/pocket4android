/*
 * Copyright 2012 Yu AOKI
 */

package com.aokyu.dev.pocket.content;

class PocketItem {

    private String mUid;

    public PocketItem(String uid) {
        mUid = uid;
    }

    public String getUID() {
        return mUid;
    }

    public boolean equals(String uid) {
        return mUid.equals(uid);
    }
}
