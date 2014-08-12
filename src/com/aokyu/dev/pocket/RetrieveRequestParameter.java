/*
 * Copyright (c) 2012-2014 Yu AOKI
 *
 * This software may be modified and distributed under the terms
 * of the MIT license.  See the LICENSE file for details.
 */

package com.aokyu.dev.pocket;

public final class RetrieveRequestParameter {

    public static final String ACCESS_TOKEN = "access_token";
    public static final String CONSUMER_KEY = "consumer_key";
    public static final String STATE = "state";
    public static final String FAVORITE = "favorite";
    public static final String TAG = "tag";
    public static final String CONTENT_TYPE = "contentType";
    public static final String SORT = "sort";
    public static final String DETAIL_TYPE = "detailType";
    public static final String SEARCH = "search";
    public static final String DOMAIN = "domain";
    public static final String SINCE = "since";
    public static final String COUNT = "count";
    public static final String OFFSET = "offset";

    public interface State {
        public static final String UNREAD = "unread";
        public static final String ARCHIVE = "archive";
        public static final String ALL = "all";
    }

    public interface Favorite {
        public static final int UNFAVORITED = 0;
        public static final int FAVORITED = 1;
    }

    public interface Tag {
        public static final String UNTAGGED = "_untagged_";
    }

    public interface ContentType {
        public static final String ARTICLE = "article";
        public static final String VIDEO = "video";
        public static final String IMAGE = "image";
    }

    public interface Sort {
        public static final String NEWEST = "newest";
        public static final String OLDEST = "oldest";
        public static final String TITLE = "title";
        public static final String SITE = "site";
    }

    public interface DetailType {
        public static final String SIMPLE = "simple";
        public static final String COMPLETE = "complete";
    }

    private RetrieveRequestParameter() {}

}
