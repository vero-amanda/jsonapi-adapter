package com.nextmatch.vero.jsonapiadapter.model;

import com.google.gson.annotations.SerializedName;

/**
 * Related Tag 가 단순 String 일 경우 사용할 Links Model.
 * @author vero
 * @since 2016. 11. 16.
 */
public class SimpleLinks implements Links {

    @SerializedName("self")
    private String _self;
    @SerializedName("related")
    private String _related;
    @SerializedName("next")
    private String _next;
    @SerializedName("last")
    private String _last;

    @Override
    public String getSelf() {
        return _self;
    }

    @Override
    public String getRelated() {
        return _related;
    }

    public String getNext() {
        return this._next;
    }

    public String getLast() {
        return this._last;
    }

}
