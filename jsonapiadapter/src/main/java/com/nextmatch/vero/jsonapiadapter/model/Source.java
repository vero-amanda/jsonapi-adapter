package com.nextmatch.vero.jsonapiadapter.model;

import com.google.gson.annotations.SerializedName;

/**
 * @author vero
 * @since 2016. 11. 18.
 */
public class Source {

    @SerializedName("pointer")
    private String _pointer;
    @SerializedName("parameter")
    private String _parameter;

    public String getPointer() {
        return _pointer;
    }

    public String getParameter() {
        return _parameter;
    }

}
