package com.nextmatch.vero.jsonapiadapter.model;

import com.google.gson.annotations.SerializedName;

/**
 * Related Tag 가 Meta Tag 를 가진 Object 일 경우 사용할 Links Model.
 * @author vero
 * @since 2016. 11. 21.
 */
public class RelatedLinks implements Links {

    @SerializedName("self")
    private String _self;
    @SerializedName("related")
    private Related _related;

    public String getSelf() {
        return _self;
    }

    public String getRelated() {
        if (_related != null)
            return _related._href;

        return null;
    }

    private static class Related {

        String _href;

    }

}
