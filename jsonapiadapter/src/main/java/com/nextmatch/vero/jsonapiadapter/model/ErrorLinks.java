package com.nextmatch.vero.jsonapiadapter.model;

import com.google.gson.annotations.SerializedName;

/**
 * @author vero
 * @since 2016. 11. 27.
 */
public class ErrorLinks implements Links {

    @SerializedName("about")
    private String _about;

    public String getAbout() {
        return this._about;
    }

}
