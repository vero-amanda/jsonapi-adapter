package com.nextmatch.vero.jsonapiadapter.internal;

import com.google.gson.JsonObject;

/**
 * @author vero
 * @since 2016. 11. 27.
 */
interface JsonApiAdapter extends ResourceProvider {

    JsonObject getJsonApiObject();

}
