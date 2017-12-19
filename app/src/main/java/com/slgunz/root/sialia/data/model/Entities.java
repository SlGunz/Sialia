
package com.slgunz.root.sialia.data.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.slgunz.root.sialia.data.model.subtype.Hashtag;
import com.slgunz.root.sialia.data.model.subtype.Media;
import com.slgunz.root.sialia.data.model.subtype.Url;
import com.slgunz.root.sialia.data.model.subtype.UserMention;

public class Entities {

    @SerializedName("hashtags")
    @Expose
    private List<Hashtag> hashtags = null;
    @SerializedName("media")
    @Expose
    private List<Media> media = null;
    @SerializedName("urls")
    @Expose
    private List<Url> urls = null;
    @SerializedName("user_mentions")
    @Expose
    private List<UserMention> userMentions = null;

    public List<Hashtag> getHashtags() {
        return hashtags;
    }

    public void setHashtags(List<Hashtag> hashtags) {
        this.hashtags = hashtags;
    }

    public List<Media> getMedia() {
        return media;
    }

    public void setMedia(List<Media> media) {
        this.media = media;
    }

    public List<Url> getUrls() {
        return urls;
    }

    public void setUrls(List<Url> urls) {
        this.urls = urls;
    }

    public List<UserMention> getUserMentions() {
        return userMentions;
    }

    public void setUserMentions(List<UserMention> userMentions) {
        this.userMentions = userMentions;
    }

}
