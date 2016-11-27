package com.nextmatch.vero.jsonapiadapter.adapter;

import com.google.gson.JsonElement;
import com.nextmatch.vero.jsonapiadapter.model.Resource;

/**
 * @author vero
 * @since 2016. 11. 27.
 */
public class JsonApiRequestAdapter<T extends Resource> implements JsonApiAdapter<T> {

    @Override
    public JsonElement getJsonApiElement() {
        return null;
    }

}
