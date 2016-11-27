package com.nextmatch.vero.jsonapiadapter.internal;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.nextmatch.vero.jsonapiadapter.adapter.JsonApiAdapter;
import com.nextmatch.vero.jsonapiadapter.adapter.JsonApiResponseAdapter;
import com.nextmatch.vero.jsonapiadapter.model.Resource;

import java.io.IOException;

/**
 * @author vero
 * @since 2016. 11. 21.
 */
public class JsonApiTypeAdapterFactory implements TypeAdapterFactory {

    @SuppressWarnings("unchecked")
    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
        if (!ConverterUtils.isResourceAssignable(type)) return null;

        return new Adapter(gson, type);
    }

    private static final class Adapter<T extends Resource> extends TypeAdapter<JsonApiAdapter<T>> {

        private final Gson _context;
        private final TypeToken<T> _dataTypeToken;

        Adapter(Gson context, TypeToken<T> dataTypeToken) {
            this._context = context;
            this._dataTypeToken = dataTypeToken;
        }

        @Override
        public void write(JsonWriter out, JsonApiAdapter value) throws IOException {
            _context.toJson(value.getJsonApiElement(), out);
        }

        @Override
        public JsonApiAdapter<T> read(JsonReader in) throws IOException {
            JsonObject jsonApi = _context.fromJson(in, JsonObject.class);
            return new JsonApiResponseAdapter<>(_context, _dataTypeToken, jsonApi);
        }

    }

}
