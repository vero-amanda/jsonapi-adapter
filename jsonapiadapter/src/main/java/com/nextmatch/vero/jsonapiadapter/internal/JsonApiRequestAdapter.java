package com.nextmatch.vero.jsonapiadapter.internal;

import com.google.gson.JsonObject;
import com.nextmatch.vero.jsonapiadapter.model.Resource;

/**
 * @author vero
 * @since 2016. 11. 27.
 */
public class JsonApiRequestAdapter<T extends Resource> implements JsonApiAdapter {

    @Override
    public JsonObject getJsonApiObject() {
        return null;
    }

}
