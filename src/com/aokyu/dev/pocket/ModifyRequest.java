/*
 * Copyright 2012 Yu AOKI
 */

package com.aokyu.dev.pocket;

public class ModifyRequest extends Request {

    public interface Parameter extends RequestParameter {

        public static final String ACCESS_TOKEN = "access_token";
        public static final String ACTIONS = "actions";
        public static final String CONSUMER_KEY = "consumer_key";

    }

    public interface Action {

        public static final String ADD = "add";
        public static final String ARCHIVE = "archive";
        public static final String FAVORITE = "favorite";
        public static final String UNFAVORITE = "unfavorite";
        public static final String DELETE = "delete";

    }

    public interface ActionParameter extends RequestParameter {

        public static final String ITEM_ID = "item_id";
        public static final String REF_ID = "ref_id";
        public static final String TAGS = "tags";
        public static final String TIME = "time";
        public static final String TITLE = "title";
        public static final String URL = "url";

    }

    public interface TagAction {

        public static final String TAGS_ADD = "tags_add";
        public static final String TAGS_REMOVE = "tags_remove";
        public static final String TAGS_REPLACE = "tags_replace";
        public static final String TAGS_CLEAR = "tags_clear";
        public static final String TAG_RENAME = "tag_rename";

    }

    public interface TagActionParameter extends RequestParameter {

        public static final String ITEM_ID = "item_id";
        public static final String TAGS = "tags";
        public static final String TIME = "time";
        public static final String OLD_TAG = "old_tag";
        public static final String NEW_TAG = "new_tag";

    }
}
