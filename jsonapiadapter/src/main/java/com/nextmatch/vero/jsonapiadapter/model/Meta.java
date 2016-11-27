package com.nextmatch.vero.jsonapiadapter.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author vero
 * @since 2016. 11. 18.
 */
public class Meta {

    @SerializedName("authors")
    public List<String> _authors;

    @SerializedName("copyright")
    public String _copyright;

}
