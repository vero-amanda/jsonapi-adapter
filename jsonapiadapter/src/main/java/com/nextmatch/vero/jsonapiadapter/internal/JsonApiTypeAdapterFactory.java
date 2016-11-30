package com.nextmatch.vero.jsonapiadapter.internal;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
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
        if (!ConverterUtils.isResourceProviderAssignable(type)) return null;

        return new Adapter(gson, type);
    }

    private static class Adapter<T extends Resource> extends TypeAdapter<ResourceProvider> {

        private final Gson _context;
        private final TypeToken<T> _typeToken;

        Adapter(Gson context, TypeToken<T> dataTypeToken) {
            this._context = context;
            this._typeToken = dataTypeToken;
        }

        @Override
        public void write(JsonWriter out, ResourceProvider value) throws IOException {
            if (JsonApiRequestAdapter.class.isInstance(value)) {
                JsonApiRequestAdapter adapter = JsonApiRequestAdapter.class.cast(value);
                _context.toJson(adapter.getJsonApiObject(), out);
                System.out.println("write >> JsonApiAdapter");
            } else {
                Resource resource = Resource.class.cast(value);
                _context.toJson(resource, resource.getClass(), out);
                System.out.println("write >> Resource");
            }
        }

        @SuppressWarnings("unchecked")
        @Override
        public ResourceProvider read(JsonReader in) throws IOException {
            JsonObject jsonApi = _context.fromJson(in, JsonObject.class);
            return new JsonApiResponseAdapter<>(_context, _typeToken, jsonApi);
        }

    }

}
