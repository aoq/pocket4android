/*
 * Copyright 2012 Yu AOKI
 */

package com.aokyu.dev.pocket;


public class AddRequest extends Request {

    public interface Parameter extends RequestParameter {

        public static final String ACCESS_TOKEN = "access_token";
        public static final String CONSUMER_KEY = "consumer_key";
        public static final String TITLE = "title";
        public static final String TAGS = "tags";
        public static final String TWEET_ID = "tweet_id";
        public static final String URL = "url";

    }

    private AddRequest() {}

    /* package */ String getAccessToken() {
        return (String) get(Parameter.ACCESS_TOKEN);
    }

    /* package */ String getConsumerKey() {
        return (String) get(Parameter.CONSUMER_KEY);
    }

    /* package */ void setUrl(String url) {
        put(Parameter.URL, url);
    }

    public String getUrl() {
        return (String) get(Parameter.URL);
    }

    public boolean hasTitle() {
        return containsKey(Parameter.TITLE);
    }

    public String getTitle() {
        return (String) get(Parameter.TITLE);
    }

    public boolean hasTags() {
        return containsKey(Parameter.TAGS);
    }

    public String[] getTags() {
        String tags = (String) get(Parameter.TAGS);
        return tags.split(",");
    }

    public boolean hasTweetId() {
        return containsKey(Parameter.TWEET_ID);
    }

    public long getTweetId() {
        return (Long) get(Parameter.TWEET_ID);
    }

    public static class Builder {

        private AddRequest mRequest = new AddRequest();

        public Builder(String url) {
            mRequest.put(Parameter.URL, url);
        }

        /* package */ Builder(String accessToken, String consumerKey, String url) {
            this(url);
            mRequest.put(Parameter.ACCESS_TOKEN, accessToken);
            mRequest.put(Parameter.CONSUMER_KEY, consumerKey);
        }

        /* package */ void setAccessToken(String accessToken) {
            mRequest.put(Parameter.ACCESS_TOKEN, accessToken);
        }

        /* package */ void setConsumerKey(String consumerKey) {
            mRequest.put(Parameter.CONSUMER_KEY, consumerKey);
        }

        public Builder setTitle(String title) {
            mRequest.put(Parameter.TITLE, title);
            return this;
        }

        public Builder setTags(String[] tags) {
            int size = tags.length;
            StringBuilder tagBuilder = new StringBuilder();
            for (int i = 0; i < size; i++) {
                String tag = tags[i];
                if (tag != null) {
                    tagBuilder.append(tag);
                    if (i < size-1) {
                        tagBuilder.append(",");
                    }
                }
            }
            mRequest.put(Parameter.TAGS, tagBuilder.toString());
            return this;
        }

        public Builder setTweetId(long tweetId) {
            mRequest.put(Parameter.TWEET_ID, tweetId);
            return this;
        }

        public AddRequest build() {
            return mRequest;
        }
    }
}
