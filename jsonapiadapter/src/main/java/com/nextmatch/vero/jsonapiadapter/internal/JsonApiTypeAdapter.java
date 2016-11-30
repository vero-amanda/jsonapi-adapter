package com.nextmatch.vero.jsonapiadapter.internal;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.nextmatch.vero.jsonapiadapter.model.Resource;

import java.io.IOException;

/**
 * @author vero
 * @since 2016. 11. 30.
 */
public final class JsonApiTypeAdapter<T extends Resource> extends TypeAdapter<JsonApiAdapter> {

    private final Gson _context;
    private final TypeToken<T> _typeToken;

    JsonApiTypeAdapter(Gson context, TypeToken<T> dataTypeToken) {
        this._context = context;
        this._typeToken = dataTypeToken;
    }

    @Override
    public void write(JsonWriter out, JsonApiAdapter value) throws IOException {
        _context.toJson(value.getJsonApiObject(), out);
    }

    @Override
    public JsonApiAdapter read(JsonReader in) throws IOException {
        JsonObject jsonApi = _context.fromJson(in, JsonObject.class);
        return new JsonApiResponseAdapter<>(_context, _typeToken, jsonApi);
    }

}
