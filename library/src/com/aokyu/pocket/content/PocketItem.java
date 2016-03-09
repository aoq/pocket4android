/*
 * Copyright (c) 2012-2014 Yu AOKI
 *
 * This software may be modified and distributed under the terms
 * of the MIT license.  See the LICENSE file for details.
 */

package com.aokyu.pocket.content;

/* package */ class PocketItem {

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
