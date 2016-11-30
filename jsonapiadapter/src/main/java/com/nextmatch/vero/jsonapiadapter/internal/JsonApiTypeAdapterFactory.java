package com.nextmatch.vero.jsonapiadapter.internal;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;

/**
 * @author vero
 * @since 2016. 11. 21.
 */
public class JsonApiTypeAdapterFactory implements TypeAdapterFactory {

    @SuppressWarnings("unchecked")
    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
        if (!ConverterUtils.isResourceAssignable(type)) return null;
        System.out.println("type : " + type.getRawType().getName());

        return new JsonApiTypeAdapter(gson, type);
    }

}
