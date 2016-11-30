package com.nextmatch.vero.jsonapiadapter.gson;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.internal.Primitives;
import com.nextmatch.vero.jsonapiadapter.internal.JsonApiResponseAdapter;
import com.nextmatch.vero.jsonapiadapter.model.Resource;

import java.lang.reflect.Type;

/**
 * @author vero
 * @since 2016. 11. 28.
 */
public class GsonAdapter {

    private Gson _context;

    public GsonAdapter(Gson context) {
        this._context = context;
    }

    @SuppressWarnings("unchecked")
    public <T extends Resource> JsonApiResponseAdapter<T> fromJsonApi(String jsonApi, Class<T> classOfResource) throws JsonSyntaxException {
        return Primitives.wrap(JsonApiResponseAdapter.class).cast(_context.fromJson(jsonApi, (Type) classOfResource));
    }

}
