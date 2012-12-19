/*
 * Copyright 2012 Yu AOKI
 */

package com.aokyu.dev.pocket;

public class RetrieveRequest extends Request {

    private RetrieveRequest() {}

    /* package */ String getAccessToken() {
        return (String) get(RetrieveRequestParameter.ACCESS_TOKEN);
    }

    /* package */ String getConsumerKey() {
        return (String) get(RetrieveRequestParameter.CONSUMER_KEY);
    }

    public boolean hasPageState() {
        return containsKey(RetrieveRequestParameter.STATE);
    }

    public String getPageState() {
        return (String) get(RetrieveRequestParameter.STATE);
    }

    public boolean hasFavorited() {
        return containsKey(RetrieveRequestParameter.FAVORITE);
    }

    public boolean getFavorited() {
        int favorited = -1;
        try {
            favorited = Integer.parseInt((String) get(RetrieveRequestParameter.FAVORITE));
            if (favorited == RetrieveRequestParameter.Favorite.FAVORITED) {
                return true;
            }
        } catch (NumberFormatException e) {}
        return false;
    }

    public boolean hasTag() {
        return containsKey(RetrieveRequestParameter.TAG);
    }

    public String getTag() {
        return (String) get(RetrieveRequestParameter.TAG);
    }

    public boolean hasContentType() {
        return containsKey(RetrieveRequestParameter.CONTENT_TYPE);
    }

    public String getContentType() {
        return (String) get(RetrieveRequestParameter.CONTENT_TYPE);
    }

    public boolean hasSort() {
        return containsKey(RetrieveRequestParameter.SORT);
    }

    public String getSort() {
        return (String) get(RetrieveRequestParameter.SORT);
    }

    public boolean hasDetailType() {
        return containsKey(RetrieveRequestParameter.DETAIL_TYPE);
    }

    public String getDetailType() {
        return (String) get(RetrieveRequestParameter.DETAIL_TYPE);
    }

    public boolean hasSearch() {
        return containsKey(RetrieveRequestParameter.SEARCH);
    }

    public String getSearch() {
        return (String) get(RetrieveRequestParameter.SEARCH);
    }

    public boolean hasDomain() {
        return containsKey(RetrieveRequestParameter.DOMAIN);
    }

    public String getDomain() {
        return (String) get(RetrieveRequestParameter.DOMAIN);
    }

    public boolean hasSince() {
        return containsKey(RetrieveRequestParameter.SINCE);
    }

    public long getSince() {
        long since = -1;
        try {
            since = Long.parseLong((String) get(RetrieveRequestParameter.SINCE));
        } catch (NumberFormatException e) {}

        return since;
    }

    public boolean hasCount() {
        return containsKey(RetrieveRequestParameter.COUNT);
    }

    public int getCount() {
        int count = -1;
        try {
            count = Integer.parseInt((String) get(RetrieveRequestParameter.COUNT));
        } catch (NumberFormatException e) {}

        return count;
    }

    public boolean hasOffset() {
        return containsKey(RetrieveRequestParameter.OFFSET);
    }

    public int getOffset() {
        int offset = -1;
        try {
            offset = Integer.parseInt((String) get(RetrieveRequestParameter.OFFSET));
        } catch (NumberFormatException e) {}

        return offset;
    }

    public static class Builder {

        private RetrieveRequest mRequest = new RetrieveRequest();

        public Builder() {}

        /* package */ Builder(String accessToken, String consumerKey, String url) {
            this();
            mRequest.put(RetrieveRequestParameter.ACCESS_TOKEN, accessToken);
            mRequest.put(RetrieveRequestParameter.CONSUMER_KEY, consumerKey);
        }

        /* package */ Builder setAccessToken(String accessToken) {
            mRequest.put(RetrieveRequestParameter.ACCESS_TOKEN, accessToken);
            return this;
        }

        /* package */ Builder setConsumerKey(String consumerKey) {
            mRequest.put(RetrieveRequestParameter.CONSUMER_KEY, consumerKey);
            return this;
        }

        public Builder setState(String state) {
            if (state.equals(RetrieveRequestParameter.State.ARCHIVE) ||
                    state.equals(RetrieveRequestParameter.State.UNREAD)) {
                mRequest.put(RetrieveRequestParameter.STATE, state);
            } else {
                throw new IllegalArgumentException();
            }
            return this;
        }

        public Builder setFavorited(boolean favorited) {
            int state = 0;
            if(favorited) {
                state = 1;
            }
            mRequest.put(RetrieveRequestParameter.FAVORITE, state);
            return this;
        }

        public Builder setTag(String tag) {
            mRequest.put(RetrieveRequestParameter.TAG, tag);
            return this;
        }

        public Builder setContentType(String contentType) {
            if (contentType.equals(RetrieveRequestParameter.ContentType.ARTICLE) ||
                    contentType.equals(RetrieveRequestParameter.ContentType.IMAGE) ||
                    contentType.equals(RetrieveRequestParameter.ContentType.VIDEO)) {
                mRequest.put(RetrieveRequestParameter.CONTENT_TYPE, contentType);
            } else {
                throw new IllegalArgumentException();
            }

            return this;
        }

        public Builder setSort(String sort) {
            if (sort.equals(RetrieveRequestParameter.Sort.NEWEST) ||
                    sort.equals(RetrieveRequestParameter.Sort.OLDEST) ||
                    sort.equals(RetrieveRequestParameter.Sort.SITE) ||
                    sort.equals(RetrieveRequestParameter.Sort.TITLE)) {
                mRequest.put(RetrieveRequestParameter.SORT, sort);
            } else {
                throw new IllegalArgumentException();
            }
            return this;
        }

        public Builder setDetailType(String type) {
            if (type.equals(RetrieveRequestParameter.DetailType.SIMPLE) ||
                    type.equals(RetrieveRequestParameter.DetailType.COMPLETE)) {
                mRequest.put(RetrieveRequestParameter.DETAIL_TYPE, type);
            } else {
                throw new IllegalArgumentException();
            }
            return this;
        }

        public Builder setSearch(String term) {
            mRequest.put(RetrieveRequestParameter.SEARCH, term);
            return this;
        }

        public Builder setDomain(String domain) {
            mRequest.put(RetrieveRequestParameter.DOMAIN, domain);
            return this;
        }

        public Builder setSince(long since) {
            mRequest.put(RetrieveRequestParameter.SINCE, since);
            return this;
        }

        public Builder setCount(int count) {
            mRequest.put(RetrieveRequestParameter.COUNT, count);
            return this;
        }

        public Builder setOffset(int offset) {
            mRequest.put(RetrieveRequestParameter.OFFSET, offset);
            return this;
        }

        public RetrieveRequest build() {
            return mRequest;
        }
    }
}
