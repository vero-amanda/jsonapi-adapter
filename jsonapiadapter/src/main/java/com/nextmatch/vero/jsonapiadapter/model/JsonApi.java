package com.nextmatch.vero.jsonapiadapter.model;

import com.google.gson.annotations.SerializedName;

/**
 * JsonApi Version Model.
 * JsonApi Tag 가 없을 경우 Server 는 Version 1.0 으로 구성되었다고 가정.
 * @author vero
 * @since 2016. 11. 21.
 */
public class JsonApi {

    @SerializedName("version")
    private String _version;

    public String getVersion() {
        return this._version;
    }

}
