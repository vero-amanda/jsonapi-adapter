package com.nextmatch.vero.jsonapiadapter.model;

import com.google.gson.annotations.SerializedName;

/**
 * @author vero
 * @since 2016. 11. 18.
 */
public class Error {

    @SerializedName("links")
    private ErrorLinks _links;
    @SerializedName("source")
    private Source _source;

    @SerializedName("id")
    private String _id;
    @SerializedName("status")
    private String _status;
    @SerializedName("code")
    private String _code;
    @SerializedName("title")
    private String _title;
    @SerializedName("detail")
    private String _detail;

    public ErrorLinks getLinks() {
        return _links;
    }

    public Source getSource() {
        return _source;
    }

    public String getId() {
        return _id;
    }

    public String getStatus() {
        return _status;
    }

    public String getCode() {
        return _code;
    }

    public String getTitle() {
        return _title;
    }

    public String getDetail() {
        return _detail;
    }

}
