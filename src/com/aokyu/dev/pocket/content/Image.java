/*
 * Copyright 2012 Yu AOKI
 */

package com.aokyu.dev.pocket.content;

import org.json.JSONException;
import org.json.JSONObject;

public class Image extends MediaItem {

    private interface Parameter {

        public static final String ITEM_ID = "item_id";
        public static final String IMAGE_ID = "image_id";
        public static final String SOURCE = "src";
        public static final String WIDTH = "width";
        public static final String HEIGHT = "height";
        public static final String CREDIT = "credit";
        public static final String CAPTION = "caption";

    }

    private String mCredit;
    private String mCaption;

    private Image(String uid) {
        super(uid);
    }

    /* package */ void setCredit(String credit) {
        mCredit = credit;
    }

    public String getCredit() {
        return mCredit;
    }

    /* package */ void setCaption(String caption) {
        mCaption = caption;
    }

    public String getCaption() {
        return mCaption;
    }

    public static class Builder {

        private Image mImage;

        public Builder(JSONObject jsonObj) throws JSONException {
            String uid = jsonObj.getString(Parameter.ITEM_ID);
            mImage = new Image(uid);

            if (!jsonObj.isNull(Parameter.IMAGE_ID)) {
                long imageId = jsonObj.getLong(Parameter.IMAGE_ID);
                mImage.setId(imageId);
            }

            if (!jsonObj.isNull(Parameter.WIDTH)) {
                int width = jsonObj.getInt(Parameter.WIDTH);
                mImage.setWidth(width);
            }

            if (!jsonObj.isNull(Parameter.HEIGHT)) {
                int height = jsonObj.getInt(Parameter.HEIGHT);
                mImage.setHeight(height);
            }

            if (!jsonObj.isNull(Parameter.SOURCE)) {
                String src = jsonObj.getString(Parameter.SOURCE);
                mImage.setSource(src);
            }

            if (!jsonObj.isNull(Parameter.CREDIT)) {
                String credit = jsonObj.getString(Parameter.CREDIT);
                mImage.setCredit(credit);
            }

            if (!jsonObj.isNull(Parameter.CAPTION)) {
                String caption = jsonObj.getString(Parameter.CAPTION);
                mImage.setCaption(caption);
            }

        }

        public Builder(String uid) {
            mImage = new Image(uid);
        }

        public Builder setSource(String source) {
            mImage.setSource(source);
            return this;
        }

        public Builder setSize(int width, int height) {
            mImage.setSize(width, height);
            return this;
        }

        public Builder setCredit(String credit) {
            mImage.setCredit(credit);
            return this;
        }

        public Builder setCaption(String caption) {
            mImage.setCaption(caption);
            return this;
        }

        public Image build() {
            return mImage;
        }
    }
}
