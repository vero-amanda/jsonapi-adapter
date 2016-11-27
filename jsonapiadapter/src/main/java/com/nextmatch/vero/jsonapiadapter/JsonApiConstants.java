package com.nextmatch.vero.jsonapiadapter;

import java.nio.charset.Charset;

import okhttp3.MediaType;

/**
 * @author vero
 * @since 2016. 11. 22.
 */
public interface JsonApiConstants {

    MediaType MEDIA_TYPE_DEFAULT = MediaType.parse("application/json; charset=UTF-8");
    MediaType MEDIA_TYPE_JSONAPI = MediaType.parse("application/vnd.api+json");

    Charset UTF_8 = Charset.forName("UTF-8");

    String NAME_DATA = "data";
    String NAME_ERRORS = "errors";
    String NAME_META = "meta";
    String NAME_JSON_API = "jsonapi";
    String NAME_LINKS = "links";
    String NAME_RELATIONSHIPS = "relationships";
    String NAME_INCLUDED = "included";
    String NAME_RELATED = "related";
    String NAME_ID = "id";
    String NAME_TYPE = "type";
    String NAME_ATTRIBUTES = "attributes";

}
