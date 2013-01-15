/*
 * Copyright 2012 Yu AOKI
 */

package com.aokyu.dev.pocket.content;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;


public class Video extends MediaItem implements Parcelable {

    public final class Parameter {

        public static final String ITEM_ID = "item_id";
        public static final String VIDEO_ID = "video_id";
        public static final String SOURCE = "src";
        public static final String WIDTH = "width";
        public static final String HEIGHT = "height";
        public static final String TYPE = "type";
        public static final String VID = "vid";

        private Parameter() {}

    }

    private String mType;
    private String mVid;

    private Video(String uid) {
        super(uid);
    }

    /* package */ void setType(String type) {
        mType = type;
    }

    public String getType() {
        return mType;
    }

    /* package */ void setVid(String vid) {
        mVid = vid;
    }

    public String getVid() {
        return mVid;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(getUID());
        dest.writeLong(getId());
        dest.writeString(getSource());
        dest.writeInt(getWidth());
        dest.writeInt(getHeight());
        dest.writeString(mType);
        dest.writeString(mVid);
    }

    private Video(Parcel in) {
        super(in.readString());
        setId(in.readLong());
        setSource(in.readString());
        setWidth(in.readInt());
        setHeight(in.readInt());
        mType = in.readString();
        mVid = in.readString();
    }

    public static final Parcelable.Creator<Video> CREATOR =
            new Parcelable.Creator<Video>() {
        public Video createFromParcel(Parcel source) {
            Video video = new Video(source);
            return video;
        }

        public Video[] newArray(int size) {
            return new Video[size];
        }
    };

    public static class Builder {

        private Video mVideo;

        public Builder(JSONObject jsonObj) throws JSONException {
            String uid = jsonObj.getString(Parameter.ITEM_ID);
            mVideo = new Video(uid);

            if (!jsonObj.isNull(Parameter.VIDEO_ID)) {
                long imageId = jsonObj.getLong(Parameter.VIDEO_ID);
                mVideo.setId(imageId);
            }

            if (!jsonObj.isNull(Parameter.WIDTH)) {
                int width = jsonObj.getInt(Parameter.WIDTH);
                mVideo.setWidth(width);
            }

            if (!jsonObj.isNull(Parameter.HEIGHT)) {
                int height = jsonObj.getInt(Parameter.HEIGHT);
                mVideo.setHeight(height);
            }

            if (!jsonObj.isNull(Parameter.SOURCE)) {
                String src = jsonObj.getString(Parameter.SOURCE);
                mVideo.setSource(src);
            }

            if (!jsonObj.isNull(Parameter.TYPE)) {
                String type = jsonObj.getString(Parameter.TYPE);
                mVideo.setType(type);
            }

            if (!jsonObj.isNull(Parameter.VID)) {
                String vid = jsonObj.getString(Parameter.VID);
                mVideo.setVid(vid);
            }

        }

        public Builder(String uid) {
            mVideo = new Video(uid);
        }

        public Builder setSource(String source) {
            mVideo.setSource(source);
            return this;
        }

        public Builder setSize(int width, int height) {
            mVideo.setSize(width, height);
            return this;
        }

        public Builder setType(String type) {
            mVideo.setType(type);
            return this;
        }

        public Builder setVid(String vid) {
            mVideo.setVid(vid);
            return this;
        }

        public Video build() {
            return mVideo;
        }
    }
}
