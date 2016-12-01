package com.nextmatch.vero.jsonapiadapter.internal;

import com.google.gson.JsonObject;
import com.nextmatch.vero.jsonapiadapter.model.ResourceProvider;

/**
 * @author vero
 * @since 2016. 11. 27.
 */
interface JsonApiAdapter extends ResourceProvider {

    JsonObject getJsonApiObject();

}
