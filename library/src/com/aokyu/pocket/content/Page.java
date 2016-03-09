/*
 * Copyright (c) 2012-2014 Yu AOKI
 *
 * This software may be modified and distributed under the terms
 * of the MIT license.  See the LICENSE file for details.
 */

package com.aokyu.pocket.content;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;

import com.aokyu.pocket.RetrieveResponse;
import com.aokyu.pocket.util.JSONUtils;

public class Page extends PocketItem implements Parcelable {

    public enum PageState {
        UNREAD(0),
        ARCHIVED(1),
        DELETED(2);

        private int mState;

        private PageState(int state) {
            mState = state;
        }

        public int intValue() {
            return mState;
        }

        public static PageState valueOf(int state) {
            PageState[] states = values();
            for (PageState pageState : states) {
                if (pageState.intValue() == state) {
                    return pageState;
                }
            }
            return null;
        }
    }

    public enum ImageState {
        HAS_NO_IMAGES(0),
        HAS_IMAGES(1),
        IS_IMAGE(2);

        private int mState;

        private ImageState(int state) {
            mState = state;
        }

        public int intValue() {
            return mState;
        }

        public static ImageState valueOf(int state) {
            ImageState[] states = values();
            for (ImageState imageState : states) {
                if (imageState.intValue() == state) {
                    return imageState;
                }
            }
            return null;
        }
    }

    public enum VideoState {
        HAS_NO_VIDEOS(0),
        HAS_VIDEOS(1),
        IS_VIDEO(2);

        private int mState;

        private VideoState(int state) {
            mState = state;
        }

        public int intValue() {
            return mState;
        }

        public static VideoState valueOf(int state) {
            VideoState[] states = values();
            for (VideoState videoState : states) {
                if (videoState.intValue() == state) {
                    return videoState;
                }
            }
            return null;
        }
    }

    private String mResolvedId;
    private String mGivenUrl;
    private String mResolvedUrl;
    private String mGivenTitle;
    private String mResolvedTitle;
    private boolean mIsFavorited;
    private PageState mState;
    private String mExcerpt;
    private boolean mIsArticle;
    private ImageState mImageState;
    private VideoState mVideoState;
    private int mWordCount;
    private String[] mTags;
    private String[] mAuthors;
    private Image[] mImages;
    private Video[] mVideos;

    private Page(String uid) {
        super(uid);
    }

    /* package */ void setResolvedId(String id) {
        mResolvedId = id;
    }

    public String getResolvedId() {
        return mResolvedId;
    }

    /* package */ void setGivenUrl(String url) {
        mGivenUrl = url;
    }

    public String getGivenUrl() {
        return mGivenUrl;
    }

    /* package */ void setResolvedUrl(String url) {
        mResolvedUrl = url;
    }

    public String getResolvedUrl() {
        return mResolvedUrl;
    }

    /* package */ void setGivenTitle(String title) {
        mGivenTitle = title;
    }

    public String getGivenTitle() {
        return mGivenTitle;
    }

    /* package */ void setResolvedTitle(String title) {
        mResolvedTitle = title;
    }

    public String getResolvedTitle() {
        return mResolvedTitle;
    }

    /* package */ void setFavorited(boolean favorited) {
        mIsFavorited = favorited;
    }

    public boolean isFavorited() {
        return mIsFavorited;
    }

    /* package */ void setState(PageState state) {
        mState = state;
    }

    public PageState getState() {
        return mState;
    }

    /* package */ void setExcerpt(String excerpt) {
        mExcerpt = excerpt;
    }

    public String getExcerpt() {
        return mExcerpt;
    }

    /* package */ void setIsArticle(boolean isArticle) {
        mIsArticle = isArticle;
    }

    public boolean isArticle() {
        return mIsArticle;
    }

    /* package */ void setImageState(ImageState state) {
        mImageState = state;
    }

    public ImageState getImageState() {
        return mImageState;
    }

    /* package */ void setVideoState(VideoState state) {
        mVideoState = state;
    }

    public VideoState getVideoState() {
        return mVideoState;
    }

    /* package */ void setWordCount(int count) {
        mWordCount = count;
    }

    public int getWordCount() {
        return mWordCount;
    }

    /* package */ void setTags(String[] tags) {
        mTags = tags;
    }

    public String[] getTags() {
        return mTags;
    }

    /* package */ void setAuthors(String[] authors) {
        mAuthors = authors;
    }

    public String[] getAuthors() {
        return mAuthors;
    }

    /* package */ void setImages(Image[] images) {
        mImages = images;
    }

    public Image[] getImages() {
        return mImages;
    }

    /* package */ void setVideos(Video[] videos) {
        mVideos = videos;
    }

    public Video[] getVideos() {
        return mVideos;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(getUID());
        dest.writeString(mResolvedId);
        dest.writeString(mGivenUrl);
        dest.writeString(mResolvedUrl);
        dest.writeString(mGivenTitle);
        dest.writeString(mResolvedTitle);
        dest.writeInt(mIsFavorited ? 1 : 0);
        dest.writeInt(mState.intValue());
        dest.writeString(mExcerpt);
        dest.writeInt(mIsArticle ? 1 : 0);
        dest.writeInt(mImageState.intValue());
        dest.writeInt(mVideoState.intValue());
        dest.writeInt(mWordCount);
        dest.writeStringArray(mTags);
        dest.writeStringArray(mAuthors);
        dest.writeParcelableArray(mImages, 0);
        dest.writeParcelableArray(mVideos, 0);
    }

    private Page(Parcel in) {
        super(in.readString());
        mResolvedId = in.readString();
        mGivenUrl = in.readString();
        mResolvedUrl = in.readString();
        mGivenTitle = in.readString();
        mResolvedTitle = in.readString();
        mIsFavorited = (in.readInt() == 1);
        mState = PageState.valueOf(in.readInt());
        mExcerpt = in.readString();
        mIsArticle = (in.readInt() == 1);
        mImageState = ImageState.valueOf(in.readInt());
        mVideoState = VideoState.valueOf(in.readInt());
        mWordCount = in.readInt();
        in.readStringArray(mTags);
        in.readStringArray(mAuthors);
        mImages = (Image[]) in.readParcelableArray(Image.class.getClassLoader());
        mVideos = (Video[]) in.readParcelableArray(Video.class.getClassLoader());
    }

    public static final Parcelable.Creator<Page> CREATOR =
            new Parcelable.Creator<Page>() {
                @Override
                public Page createFromParcel(Parcel source) {
                    Page page = new Page(source);
                    return page;
                }

                @Override
                public Page[] newArray(int size) {
                    return new Page[size];
                }
    };

    public static class Builder {

        private Page mPage;

        public Builder(JSONObject jsonObj) throws JSONException {
            String uid = jsonObj.getString(RetrieveResponse.Parameter.ITEM_ID);
            mPage = new Page(uid);

            if (!jsonObj.isNull(RetrieveResponse.Parameter.RESOLVED_ID)) {
                String resolvedId = jsonObj.getString(RetrieveResponse.Parameter.RESOLVED_ID);
                mPage.setResolvedId(resolvedId);
            }

            if (!jsonObj.isNull(RetrieveResponse.Parameter.GIVEN_URL)) {
                String givenUrl = jsonObj.getString(RetrieveResponse.Parameter.GIVEN_URL);
                mPage.setGivenUrl(givenUrl);
            }

            if (!jsonObj.isNull(RetrieveResponse.Parameter.RESOLVED_URL)) {
                String resolvedUrl = jsonObj.getString(RetrieveResponse.Parameter.RESOLVED_URL);
                mPage.setResolvedUrl(resolvedUrl);
            }

            if (!jsonObj.isNull(RetrieveResponse.Parameter.GIVEN_TITLE)) {
                String givenTitle = jsonObj.getString(RetrieveResponse.Parameter.GIVEN_TITLE);
                mPage.setGivenTitle(givenTitle);
            }

            if (!jsonObj.isNull(RetrieveResponse.Parameter.RESOLVED_TITLE)) {
                String resolvedTitle =
                        jsonObj.getString(RetrieveResponse.Parameter.RESOLVED_TITLE);
                mPage.setResolvedTitle(resolvedTitle);
            }

            if (!jsonObj.isNull(RetrieveResponse.Parameter.FAVORITE)) {
                int state = jsonObj.getInt(RetrieveResponse.Parameter.FAVORITE);
                mPage.setFavorited(state == 1);
            }

            if (!jsonObj.isNull(RetrieveResponse.Parameter.STATUS)) {
                int state = jsonObj.getInt(RetrieveResponse.Parameter.STATUS);
                mPage.setState(PageState.valueOf(state));
            }

            if (!jsonObj.isNull(RetrieveResponse.Parameter.EXCERPT)) {
                String excerpt = jsonObj.getString(RetrieveResponse.Parameter.EXCERPT);
                mPage.setExcerpt(excerpt);
            }

            if (!jsonObj.isNull(RetrieveResponse.Parameter.IS_ARTICLE)) {
                int state = jsonObj.getInt(RetrieveResponse.Parameter.FAVORITE);
                mPage.setIsArticle(state == 1);
            }

            if (!jsonObj.isNull(RetrieveResponse.Parameter.HAS_IMAGE)) {
                int state = jsonObj.getInt(RetrieveResponse.Parameter.HAS_IMAGE);
                mPage.setImageState(ImageState.valueOf(state));
            }

            if (!jsonObj.isNull(RetrieveResponse.Parameter.HAS_VIDEO)) {
                int state = jsonObj.getInt(RetrieveResponse.Parameter.HAS_VIDEO);
                mPage.setVideoState(VideoState.valueOf(state));
            }

            if (!jsonObj.isNull(RetrieveResponse.Parameter.WORD_COUNT)) {
                int count = jsonObj.getInt(RetrieveResponse.Parameter.WORD_COUNT);
                mPage.setWordCount(count);
            }

            if (!jsonObj.isNull(RetrieveResponse.Parameter.TAGS)) {
                JSONObject jsonTags =
                        jsonObj.getJSONObject(RetrieveResponse.Parameter.TAGS);
                String[] tags = JSONUtils.getKeys(jsonTags);
                mPage.setTags(tags);
            }

            if (!jsonObj.isNull(RetrieveResponse.Parameter.AUTHORS)) {
                JSONObject jsonAuthors =
                        jsonObj.getJSONObject(RetrieveResponse.Parameter.AUTHORS);
                String[] authors = JSONUtils.getKeys(jsonAuthors);
                mPage.setAuthors(authors);
            }

            if (!jsonObj.isNull(RetrieveResponse.Parameter.IMAGES)) {
                JSONObject jsonImages =
                        jsonObj.getJSONObject(RetrieveResponse.Parameter.IMAGES);
                String[] keys = JSONUtils.getKeys(jsonImages);
                Image[] images = null;
                int size = keys.length;
                if (size > 0) {
                    images = new Image[size];
                    for(int i = 0; i < size; i++) {
                        String key = keys[i];
                        try {
                            JSONObject image = jsonImages.getJSONObject(key);
                            images[i] = new Image.Builder(image).build();
                        } catch (JSONException e) {
                        }
                    }
                }
                if (images != null) {
                    mPage.setImages(images);
                }
            }

            if (!jsonObj.isNull(RetrieveResponse.Parameter.VIDEOS)) {
                JSONObject jsonVideos =
                        jsonObj.getJSONObject(RetrieveResponse.Parameter.VIDEOS);
                String[] keys = JSONUtils.getKeys(jsonVideos);
                Video[] videos = null;
                int size = keys.length;
                if (size > 0) {
                    videos = new Video[size];
                    for(int i = 0; i < size; i++) {
                        String key = keys[i];
                        try {
                            JSONObject video = jsonVideos.getJSONObject(key);
                            videos[i] = new Video.Builder(video).build();
                        } catch (JSONException e) {
                        }
                    }
                }
                if (videos != null) {
                    mPage.setVideos(videos);
                }
            }
        }

        public Builder(String uid) {
            mPage = new Page(uid);
        }

        public Builder setResolvedId(String resolvedId) {
            mPage.setResolvedId(resolvedId);
            return this;
        }

        public Builder setGivenUrl(String givenUrl) {
            mPage.setGivenUrl(givenUrl);
            return this;
        }

        public Builder setResolvedUrl(String resolvedUrl) {
            mPage.setResolvedUrl(resolvedUrl);
            return this;
        }

        public Builder setGivenTitle(String givenTitle) {
            mPage.setGivenTitle(givenTitle);
            return this;
        }

        public Builder setResolvedTitle(String resolvedTitle) {
            mPage.setResolvedTitle(resolvedTitle);
            return this;
        }

        public Builder setFavorited(boolean favorited) {
            mPage.setFavorited(favorited);
            return this;
        }

        public Builder setState(PageState state) {
            mPage.setState(state);
            return this;
        }

        public Builder setExcerpt(String excerpt) {
            mPage.setExcerpt(excerpt);
            return this;
        }

        public Builder setIsArticle(boolean isArticle) {
            mPage.setIsArticle(isArticle);
            return this;
        }

        public Builder setImageState(ImageState imageState) {
            mPage.setImageState(imageState);
            return this;
        }

        public Builder setVideoState(VideoState videoState) {
            mPage.setVideoState(videoState);
            return this;
        }

        public Builder setWordCount(int count) {
            mPage.setWordCount(count);
            return this;
        }

        public Builder setTags(String[] tags) {
            mPage.setTags(tags);
            return this;
        }

        public Builder setAuthors(String[] authors) {
            mPage.setAuthors(authors);
            return this;
        }

        public Builder setImages(Image[] images) {
            mPage.setImages(images);
            return this;
        }

        public Builder setVideos(Video[] videos) {
            mPage.setVideos(videos);
            return this;
        }

        public Page build() {
            return mPage;
        }
    }
}
