package com.nextmatch.vero.jsonapiadapter.adapter;

import com.google.gson.JsonElement;
import com.nextmatch.vero.jsonapiadapter.model.Resource;

/**
 * @author vero
 * @since 2016. 11. 27.
 */
public interface JsonApiAdapter<T extends Resource> {

    JsonElement getJsonApiElement();

}
