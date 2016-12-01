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

    public String getSelf() {
        return _self;
    }

    public void setSelf(String self) {
        _self = self;
    }

    public String getRelated() {
        return _related;
    }

    public void setRelated(String related) {
        _related = related;
    }

    public String getNext() {
        return _next;
    }

    public void setNext(String next) {
        _next = next;
    }

    public String getLast() {
        return _last;
    }

    public void setLast(String last) {
        _last = last;
    }

}
