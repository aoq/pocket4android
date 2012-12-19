/*
 * Copyright 2012 Yu AOKI
 */

package com.aokyu.dev.pocket;

import org.json.JSONException;
import org.json.JSONObject;

import com.aokyu.dev.pocket.content.Image;
import com.aokyu.dev.pocket.content.Page.ImageState;
import com.aokyu.dev.pocket.content.Page.VideoState;
import com.aokyu.dev.pocket.content.Video;
import com.aokyu.dev.pocket.util.JSONUtils;

public class AddResponse extends Response {

    public interface Parameter extends ResponseParameter {

        public static final String ITEM = "item";
        public static final String STATUS = "status";
        public static final String ITEM_ID = "item_id";
        public static final String NORMAL_URL = "normal_url";
        public static final String RESOLVED_ID = "resolved_id";
        public static final String RESOLVED_URL = "resolved_url";
        public static final String DOMAIN_ID = "domain_id";
        public static final String ORIGIN_DOMAIN_ID = "origin_domain_id";
        public static final String RESPONSE_CODE = "response_code";
        public static final String MIME_TYPE = "mime_type";
        public static final String CONTENT_LENGTH = "content_length";
        public static final String ENCODING = "encoding";
        public static final String DATE_RESOLVED = "date_resolved";
        public static final String DATE_PUBLISHED = "date_published";
        public static final String TITLE = "title";
        public static final String EXCERPT = "excerpt";
        public static final String WORD_COUNT = "word_count";
        public static final String HAS_IMAGE = "has_image";
        public static final String HAS_VIDEO = "has_video";
        public static final String IS_INDEX = "is_index";
        public static final String IS_ARTICLE = "is_article";
        public static final String AUTHORS = "authors";
        public static final String IMAGES = "images";
        public static final String VIDEOS = "videos";

    }

    /* package */ AddResponse(JSONObject jsonObj) throws JSONException {
        super(jsonObj, Parameter.ITEM);
    }

    public String getUID() {
        return (String) get(Parameter.ITEM_ID);
    }

    public String getNormalUrl() {
        return (String) get(Parameter.NORMAL_URL);
    }

    public String getResolvedId() {
        return (String) get(Parameter.RESOLVED_URL);
    }

    public String getDomainId() {
        return (String) get(Parameter.DOMAIN_ID);
    }

    public String getOriginDomainId() {
        return (String) get(Parameter.ORIGIN_DOMAIN_ID);
    }

    public int getResponseCode() {
        try {
            return Integer.parseInt((String) get(Parameter.RESPONSE_CODE));
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    public String getMimeType() {
        return (String) get(Parameter.MIME_TYPE);
    }

    public int getContentLength() {
        try {
            return Integer.parseInt((String) get(Parameter.CONTENT_LENGTH));
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    public String getEncoding() {
        return (String) get(Parameter.ENCODING);
    }

    public String getDateResolved() {
        return (String) get(Parameter.DATE_RESOLVED);
    }

    public String getDatePublished() {
        return (String) get(Parameter.DATE_PUBLISHED);
    }

    public String getTitle() {
        return (String) get(Parameter.TITLE);
    }

    public String getExcerpt() {
        return (String) get(Parameter.EXCERPT);
    }

    public int getWordCount() {
        try {
            return Integer.parseInt((String) get(Parameter.WORD_COUNT));
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    public ImageState getImageState() {
        int state = -1;
        try {
            state = Integer.parseInt((String) get(Parameter.HAS_IMAGE));
            ImageState imageState = ImageState.valueOf(state);
            return imageState;
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public VideoState getVideoState() {
        int state = -1;
        try {
            state = Integer.parseInt((String) get(Parameter.HAS_VIDEO));
            VideoState videoState = VideoState.valueOf(state);
            return videoState;
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public boolean isIndex() {
        try {
            int isIndex = Integer.parseInt((String) get(Parameter.IS_INDEX));
            switch (isIndex) {
            case 0:
                return false;
            case 1:
                return true;
            default:
                return false;
            }
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public boolean isArticle() {
        try {
            int isArticle =
                    Integer.parseInt((String) get(Parameter.IS_ARTICLE));
            switch (isArticle) {
            case 0:
                return false;
            case 1:
                return true;
            default:
                return false;
            }
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public String[] getAuthors() {
        String authors = (String) get(Parameter.AUTHORS);
        if (authors != null) {
            return authors.split(",");
        } else {
            return null;
        }
    }

    public Image[] getImages() {
        Image[] images = null;
        JSONObject jsonImages = (JSONObject) get(Parameter.IMAGES);
        String[] keys = JSONUtils.getKeys(jsonImages);
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
        return images;
    }

    public Video[] getVideos() {
        Video[] videos = null;
        JSONObject jsonVideo = (JSONObject) get(Parameter.VIDEOS);
        String[] keys = JSONUtils.getKeys(jsonVideo);
        int size = keys.length;
        if (size > 0) {
            videos = new Video[size];
            for(int i = 0; i < size; i++) {
                String key = keys[i];
                try {
                    JSONObject video = jsonVideo.getJSONObject(key);
                    videos[i] = new Video.Builder(video).build();
                } catch (JSONException e) {
                }
            }
        }
        return videos;
    }
}
