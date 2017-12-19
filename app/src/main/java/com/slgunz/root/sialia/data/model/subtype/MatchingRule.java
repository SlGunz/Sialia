
package com.slgunz.root.sialia.data.model.subtype;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MatchingRule {

    @SerializedName("tag")
    @Expose
    private String tag;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("id_str")
    @Expose
    private String idStr;

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getIdStr() {
        return idStr;
    }

    public void setIdStr(String idStr) {
        this.idStr = idStr;
    }

}
