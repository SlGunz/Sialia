package com.slgunz.root.sialia.data.model.subtype;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by root on 09.01.2018.
 */

public class Banner {
    @SerializedName("h")
    @Expose
    private Integer height;
    @SerializedName("w")
    @Expose
    private Integer width;
    @SerializedName("url")
    @Expose
    private String url;

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
