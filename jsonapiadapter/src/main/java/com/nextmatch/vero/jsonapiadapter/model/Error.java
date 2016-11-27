package com.nextmatch.vero.jsonapiadapter.model;

import com.google.gson.annotations.SerializedName;

/**
 * @author vero
 * @since 2016. 11. 18.
 */
public class Error {

    @SerializedName("links")
    public SimpleLinks _links;
    @SerializedName("source")
    public Source _source;
    @SerializedName("meta")
    public Meta _meta;

    @SerializedName("id")
    public String _id;
    @SerializedName("status")
    public String _status;
    @SerializedName("code")
    public String _code;
    @SerializedName("title")
    public String _title;
    @SerializedName("detail")
    public String _detail;


}
