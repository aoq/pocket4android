/*
 * Copyright 2012 Yu AOKI
 */

package com.aokyu.dev.pocket;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.json.JSONException;
import org.json.JSONObject;

import com.aokyu.dev.pocket.content.Image;
import com.aokyu.dev.pocket.content.Page;
import com.aokyu.dev.pocket.content.Page.ImageState;
import com.aokyu.dev.pocket.content.Page.VideoState;
import com.aokyu.dev.pocket.content.Video;
import com.aokyu.dev.pocket.util.JSONUtils;

public class RetrieveResponse extends Response {

    public interface Parameter extends ResponseParameter {

        public static final String STATUS = "status";
        public static final String LIST = "list";
        public static final String ITEM_ID = "item_id";
        public static final String RESOLVED_ID = "resolved_id";
        public static final String GIVEN_URL = "given_url";
        public static final String RESOLVED_URL = "resolved_url";
        public static final String GIVEN_TITLE = "given_title";
        public static final String RESOLVED_TITLE = "resolved_title";
        public static final String FAVORITE = "favorite";
        public static final String EXCERPT = "excerpt";
        public static final String IS_ARTICLE = "is_article";
        public static final String HAS_IMAGE = "has_image";
        public static final String HAS_VIDEO = "has_video";
        public static final String WORD_COUNT = "word_count";
        public static final String TAGS = "tags";
        public static final String AUTHORS = "authors";
        public static final String IMAGES = "images";
        public static final String VIDEOS = "videos";

    }

    /* package */ RetrieveResponse(JSONObject jsonObj) throws JSONException {
        super(jsonObj, Parameter.LIST);
    }

    public String[] getItemIds() {
        Set<String> itemIds = keySet();
        return itemIds.toArray(new String[0]);
    }

    public Page getItem(String itemId) throws JSONException {
        JSONObject obj = (JSONObject) get(itemId);
        Page page = new Page.Builder(obj).build();
        return page;
    }

    public List<Page> getItems() throws JSONException {
        List<Page> pages = new ArrayList<Page>();
        String[] itemIds = getItemIds();
        for (String itemId : itemIds) {
            JSONObject obj = (JSONObject) get(itemId);
            Page page = new Page.Builder(obj).build();
            pages.add(page);
        }
        return pages;
    }

    public JSONObject getItemAsJSONObject(String itemId) {
        return (JSONObject) get(itemId);
    }

    public String getResolvedId(String itemId) throws JSONException {
        JSONObject obj = (JSONObject) get(itemId);
        return obj.getString(Parameter.RESOLVED_ID);
    }

    public String getGivenUrl(String itemId) throws JSONException {
        JSONObject obj = (JSONObject) get(itemId);
        return obj.getString(Parameter.GIVEN_URL);
    }

    public String getResolvedUrl(String itemId) throws JSONException {
        JSONObject obj = (JSONObject) get(itemId);
        return obj.getString(Parameter.RESOLVED_URL);
    }

    public String getGivenTitle(String itemId) throws JSONException {
        JSONObject obj = (JSONObject) get(itemId);
        return obj.getString(Parameter.GIVEN_TITLE);
    }

    public String getResolvedTitle(String itemId) throws JSONException {
        JSONObject obj = (JSONObject) get(itemId);
        return obj.getString(Parameter.RESOLVED_TITLE);
    }

    public boolean isFavorited(String itemId) throws JSONException {
        JSONObject obj = (JSONObject) get(itemId);
        int state = -1;
        try {
            state = obj.getInt(Parameter.FAVORITE);
            return (state == 1);
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public String getExcerpt(String itemId) throws JSONException {
        JSONObject obj = (JSONObject) get(itemId);
        return obj.getString(Parameter.EXCERPT);
    }

    public boolean isArticle(String itemId) throws JSONException {
        JSONObject obj = (JSONObject) get(itemId);
        int state = -1;
        try {
            state = obj.getInt(Parameter.IS_ARTICLE);
            return (state == 1);
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public ImageState getImageState(String itemId) throws JSONException {
        JSONObject obj = (JSONObject) get(itemId);
        int state = -1;
        try {
            state = obj.getInt(Parameter.HAS_IMAGE);
            return ImageState.valueOf(state);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public VideoState getVideoState(String itemId) throws JSONException {
        JSONObject obj = (JSONObject) get(itemId);
        int state = -1;
        try {
            state = obj.getInt(Parameter.HAS_VIDEO);
            return VideoState.valueOf(state);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public int getWordCount(String itemId) throws JSONException {
        JSONObject obj = (JSONObject) get(itemId);
        return obj.getInt(Parameter.WORD_COUNT);
    }

    public String[] getTags(String itemId) throws JSONException {
        JSONObject obj = (JSONObject) get(itemId);
        JSONObject tags = obj.getJSONObject(Parameter.TAGS);
        return JSONUtils.getKeys(tags);
    }

    public String[] getAuthors(String itemId) throws JSONException {
        JSONObject obj = (JSONObject) get(itemId);
        JSONObject tags = obj.getJSONObject(Parameter.AUTHORS);
        return JSONUtils.getKeys(tags);
    }

    public Image[] getImages(String itemId) throws JSONException {
        JSONObject obj = (JSONObject) get(itemId);
        Image[] images = null;
        JSONObject jsonImages = obj.getJSONObject(Parameter.IMAGES);
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

    public Video[] getVideos(String itemId) throws JSONException {
        JSONObject obj = (JSONObject) get(itemId);
        Video[] videos = null;
        JSONObject jsonVideo = obj.getJSONObject(Parameter.VIDEOS);
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
