package com.slgunz.root.sialia.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.slgunz.root.sialia.data.model.subtype.Banner;


public class Banners {
    @SerializedName("ipad")
    @Expose
    private Banner ipad;
    @SerializedName("ipad_retina")
    @Expose
    private Banner ipadRetina;
    @SerializedName("web")
    @Expose
    private Banner web;
    @SerializedName("web_retina")
    @Expose
    private Banner webRetina;
    @SerializedName("mobile")
    @Expose
    private Banner mobile;
    @SerializedName("mobile_retina")
    @Expose
    private Banner mobileRetina;
    @SerializedName("300x100")
    @Expose
    private Banner _300x100;
    @SerializedName("600x200")
    @Expose
    private Banner _600x200;
    @SerializedName("1500x500")
    @Expose
    private Banner _1500x500;

    public Banner getIpad() {
        return ipad;
    }

    public void setIpad(Banner ipad) {
        this.ipad = ipad;
    }

    public Banner getIpadRetina() {
        return ipadRetina;
    }

    public void setIpadRetina(Banner ipadRetina) {
        this.ipadRetina = ipadRetina;
    }

    public Banner getWeb() {
        return web;
    }

    public void setWeb(Banner web) {
        this.web = web;
    }

    public Banner getWebRetina() {
        return webRetina;
    }

    public void setWebRetina(Banner webRetina) {
        this.webRetina = webRetina;
    }

    public Banner getMobile() {
        return mobile;
    }

    public void setMobile(Banner mobile) {
        this.mobile = mobile;
    }

    public Banner getMobileRetina() {
        return mobileRetina;
    }

    public void setMobileRetina(Banner mobileRetina) {
        this.mobileRetina = mobileRetina;
    }

    public Banner get_300x100() {
        return _300x100;
    }

    public void set_300x100(Banner _300x100) {
        this._300x100 = _300x100;
    }

    public Banner get_600x200() {
        return _600x200;
    }

    public void set_600x200(Banner _600x200) {
        this._600x200 = _600x200;
    }

    public Banner get_1500x500() {
        return _1500x500;
    }

    public void set_1500x500(Banner _1500x500) {
        this._1500x500 = _1500x500;
    }
}
