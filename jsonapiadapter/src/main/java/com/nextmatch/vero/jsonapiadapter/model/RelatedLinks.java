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

    @Override
    public String getSelf() {
        return _self;
    }

    @Override
    public String getRelated() {
        if (_related != null)
            return _related._href;

        return null;
    }

    public Meta getRelatedMeta() {
        if (_related != null)
            return _related._meta;

        return null;
    }

    private static class Related {

        Meta _meta;

        String _href;

    }

}
