/*
 * Copyright (c) 2012-2015 Yu AOKI
 *
 * This software may be modified and distributed under the terms
 * of the MIT license.  See the LICENSE file for details.
 */

package com.aokyu.pocket;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * TODO: This class should be replaced with a class using a JSON-POJO mapper.
 */
public class ModifyRequest extends Request {

    public final class Parameter {

        public static final String ACCESS_TOKEN = "access_token";
        public static final String ACTIONS = "actions";
        public static final String CONSUMER_KEY = "consumer_key";

        private Parameter() {}

    }

    private ModifyRequest() {}

    /* package */ String getAccessToken() {
        return (String) get(Parameter.ACCESS_TOKEN);
    }

    /* package */ String getConsumerKey() {
        return (String) get(Parameter.CONSUMER_KEY);
    }

    public boolean hasActions() {
        return containsKey(Parameter.ACTIONS);
    }

    public JSONArray getActions() {
        return (JSONArray) get(Parameter.ACTIONS);
    }

    public static class Builder {

        private ModifyRequest mRequest = new ModifyRequest();

        public Builder() {}

        public Builder(JSONArray jsonArray) {
            mRequest.put(Parameter.ACTIONS, jsonArray);
        }

        /* package */ Builder(String accessToken, String consumerKey, JSONArray jsonArray) {
            this(jsonArray);
            mRequest.put(Parameter.ACCESS_TOKEN, accessToken);
            mRequest.put(Parameter.CONSUMER_KEY, consumerKey);
        }

        /* package */ Builder setAccessToken(String accessToken) {
            mRequest.put(Parameter.ACCESS_TOKEN, accessToken);
            return this;
        }

        /* package */ Builder setConsumerKey(String consumerKey) {
            mRequest.put(Parameter.CONSUMER_KEY, consumerKey);
            return this;
        }

        private JSONArray getActions() {
            JSONArray jsonArray = null;
            if (mRequest.containsKey(Parameter.ACTIONS)) {
                jsonArray = (JSONArray) mRequest.get(Parameter.ACTIONS);
            } else {
                jsonArray = new JSONArray();
            }
            return jsonArray;
        }

        public Builder addAction(Action action) {
            JSONArray jsonArray = getActions();
            jsonArray.put(action.toJSONObject());
            mRequest.put(Parameter.ACTIONS, jsonArray);
            return this;
        }

        public Builder addActions(Action[] actions) {
            JSONArray jsonArray = getActions();

            int size = actions.length;
            for (int i = 0; i < size; i++) {
                Action action = actions[i];
                jsonArray.put(action.toJSONObject());
            }
            mRequest.put(Parameter.ACTIONS, jsonArray);
            return this;
        }

        public Builder addActions(List<Action> actions) {
            JSONArray jsonArray = getActions();

            int size = actions.size();
            for (int i = 0; i < size; i++) {
                Action action = actions.get(i);
                jsonArray.put(action.toJSONObject());
            }
            mRequest.put(Parameter.ACTIONS, jsonArray);
            return this;
        }

        public Builder setActions(Action[] actions) {
            JSONArray jsonArray = new JSONArray();

            int size = actions.length;
            for (int i = 0; i < size; i++) {
                Action action = actions[i];
                jsonArray.put(action.toJSONObject());
            }
            mRequest.put(Parameter.ACTIONS, jsonArray);
            return this;
        }

        public Builder addTagAction(TagAction tagAction) {
            JSONArray jsonArray = getActions();
            jsonArray.put(tagAction.toJSONObject());
            mRequest.put(Parameter.ACTIONS, jsonArray);
            return this;
        }

        public Builder addTagActions(TagAction[] tagActions) {
            JSONArray jsonArray = getActions();

            int size = tagActions.length;
            for (int i = 0; i < size; i++) {
                TagAction tagAction = tagActions[i];
                jsonArray.put(tagAction.toJSONObject());
            }
            mRequest.put(Parameter.ACTIONS, jsonArray);
            return this;
        }

        public Builder addTagActions(List<TagAction> tagActions) {
            JSONArray jsonArray = getActions();

            int size = tagActions.size();
            for (int i = 0; i < size; i++) {
                TagAction tagAction = tagActions.get(i);
                jsonArray.put(tagAction.toJSONObject());
            }
            mRequest.put(Parameter.ACTIONS, jsonArray);
            return this;
        }

        public Builder setTagActions(TagAction[] tagActions) {
            JSONArray jsonArray = new JSONArray();

            int size = tagActions.length;
            for (int i = 0; i < size; i++) {
                TagAction tagAction = tagActions[i];
                jsonArray.put(tagAction.toJSONObject());
            }
            mRequest.put(Parameter.ACTIONS, jsonArray);
            return this;
        }

        public ModifyRequest build() {
            return mRequest;
        }
    }

    /**
     * TODO: This class should be replaced with a class using a JSON-POJO mapper.
     */
    public static class Action extends Request {

        public static final String ADD = "add";
        public static final String ARCHIVE = "archive";
        public static final String FAVORITE = "favorite";
        public static final String UNFAVORITE = "unfavorite";
        public static final String DELETE = "delete";

        public class Parameter {

            public static final String ACTION = "action";
            public static final String ITEM_ID = "item_id";
            public static final String REF_ID = "ref_id";
            public static final String TAGS = "tags";
            public static final String TIME = "time";
            public static final String TITLE = "title";
            public static final String URL = "url";

            private Parameter() {}

        }

        private Action() {}

        public String getAction() {
            return (String) get(Parameter.ACTION);
        }

        public String getUID() {
            return (String) get(Parameter.ITEM_ID);
        }

        public long getTime() {
            return (Long) get(Parameter.TIME);
        }

        public boolean hasTweetId() {
            return containsKey(Parameter.REF_ID);
        }

        public long getTweetId() {
            return (Long) get(Parameter.REF_ID);
        }

        public boolean hasTags() {
            return containsKey(Parameter.TAGS);
        }

        public String[] getTags() {
            JSONArray tags = (JSONArray) get(Parameter.TAGS);
            int size = tags.length();
            String[] tagsArray = new String[size];
            for (int i = 0; i < size; i++) {
                try {
                    tagsArray[i] = tags.getString(i);
                } catch (JSONException e) {
                    return null;
                }
            }
            return tagsArray;
        }

        public boolean hasTitle() {
            return containsKey(Parameter.TITLE);
        }

        public String getTitle() {
            return (String) get(Parameter.TITLE);
        }

        public boolean hasUrl() {
            return containsKey(Parameter.URL);
        }

        public String getUrl() {
            return (String) get(Parameter.URL);
        }

        public static class Builder {

            private Action mAction = new Action();

            public Builder(String action, String uid, long time) {
                mAction.put(Parameter.ACTION, action);
                mAction.put(Parameter.ITEM_ID, uid);
                mAction.put(Parameter.TIME, time);
            }

            public Builder setTweetId(long tweetId) {
                mAction.put(Parameter.REF_ID, tweetId);
                return this;
            }

            public Builder setTags(String[] tags) {
                int size = tags.length;
                JSONArray array = new JSONArray();
                for (int i = 0; i < size; i++) {
                    String tag = tags[i];
                    if (tag != null) {
                        array.put(tag);
                    }
                }
                mAction.put(Parameter.TAGS, array);
                return this;
            }

            public Builder setTitle(String title) {
                mAction.put(Parameter.TITLE, title);
                return this;
            }

            public Builder setUrl(String url) {
                mAction.put(Parameter.URL, url);
                return this;
            }

            public Action build() {
                return mAction;
            }
        }
    }

    /**
     * TODO: This class should be replaced with a class using a JSON-POJO mapper.
     */
    public static class TagAction extends Request {

        public static final String TAGS_ADD = "tags_add";
        public static final String TAGS_REMOVE = "tags_remove";
        public static final String TAGS_REPLACE = "tags_replace";
        public static final String TAGS_CLEAR = "tags_clear";
        public static final String TAG_RENAME = "tag_rename";

        public class Parameter {

            public static final String ACTION = "action";
            public static final String ITEM_ID = "item_id";
            public static final String TAGS = "tags";
            public static final String TIME = "time";
            public static final String OLD_TAG = "old_tag";
            public static final String NEW_TAG = "new_tag";

            private Parameter() {}

        }

        private TagAction() {}

        public String getAction() {
            return (String) get(Parameter.ACTION);
        }

        public String getUID() {
            return (String) get(Parameter.ITEM_ID);
        }

        public long getTime() {
            return (Long) get(Parameter.TIME);
        }

        public boolean hasTags() {
            return containsKey(Parameter.TAGS);
        }

        public String[] getTags() {
            String tags = (String) get(Parameter.TAGS);
            return tags.split(",");
        }

        public boolean hasOldTag() {
            return containsKey(Parameter.OLD_TAG);
        }

        public String getOldTag() {
            return (String) get(Parameter.OLD_TAG);
        }

        public boolean hasNewTag() {
            return containsKey(Parameter.NEW_TAG);
        }

        public String getNewTag() {
            return (String) get(Parameter.NEW_TAG);
        }

        public static class Builder {

            private TagAction mAction = new TagAction();

            public Builder(String action, String uid, long time) {
                mAction.put(Parameter.ACTION, action);
                mAction.put(Parameter.ITEM_ID, uid);
                mAction.put(Parameter.TIME, time);
            }

            public Builder setTags(String[] tags) {
                int size = tags.length;
                JSONArray array = new JSONArray();
                for (int i = 0; i < size; i++) {
                    String tag = tags[i];
                    if (tag != null) {
                        array.put(tag);
                    }
                }
                mAction.put(Parameter.TAGS, array);
                return this;
            }

            public Builder setOldTag(String oldTag) {
                mAction.put(Parameter.OLD_TAG, oldTag);
                return this;
            }

            public Builder setNewTag(String newTag) {
                mAction.put(Parameter.NEW_TAG, newTag);
                return this;
            }

            public TagAction build() {
                return mAction;
            }
        }
    }
}
