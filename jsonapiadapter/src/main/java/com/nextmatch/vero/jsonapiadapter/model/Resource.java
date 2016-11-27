package com.nextmatch.vero.jsonapiadapter.model;

import com.nextmatch.vero.jsonapiadapter.annotation.Type;

/**
 * @author vero
 * @since 2016. 11. 16.
 */
public class Resource {

    private transient String _id;

    public void setId(String id) {
        this._id = id;
    }

    public String getId() {
        return this._id;
    }

    public String getType() {
        Type gsonApi = getClass().getAnnotation(Type.class);
        if (gsonApi != null) return gsonApi.value();

        return null;
    }

}
