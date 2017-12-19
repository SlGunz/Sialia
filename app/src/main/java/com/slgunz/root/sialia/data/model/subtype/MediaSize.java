
package com.slgunz.root.sialia.data.model.subtype;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MediaSize {

    @SerializedName("h")
    @Expose
    private Integer height;
    @SerializedName("resize")
    @Expose
    private String resize;
    @SerializedName("w")
    @Expose
    private Integer width;

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public String getResize() {
        return resize;
    }

    public void setResize(String resize) {
        this.resize = resize;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

}
