
package com.slgunz.root.sialia.data.model.subtype;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Sizes {

    @SerializedName("thumb")
    @Expose
    private MediaSize thumb;
    @SerializedName("large")
    @Expose
    private MediaSize large;
    @SerializedName("medium")
    @Expose
    private MediaSize medium;
    @SerializedName("small")
    @Expose
    private MediaSize small;

    public MediaSize getThumb() {
        return thumb;
    }

    public void setThumb(MediaSize thumb) {
        this.thumb = thumb;
    }

    public MediaSize getLarge() {
        return large;
    }

    public void setLarge(MediaSize large) {
        this.large = large;
    }

    public MediaSize getMedium() {
        return medium;
    }

    public void setMedium(MediaSize medium) {
        this.medium = medium;
    }

    public MediaSize getSmall() {
        return small;
    }

    public void setSmall(MediaSize small) {
        this.small = small;
    }

}
