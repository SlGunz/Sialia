
package com.slgunz.root.sialia.data.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.slgunz.root.sialia.data.model.subtype.Media;

public class ExtendedEntities {

    @SerializedName("media")
    @Expose
    private List<Media> media = null;

    public List<Media> getMedia() {
        return media;
    }

    public void setMedia(List<Media> media) {
        this.media = media;
    }

}
