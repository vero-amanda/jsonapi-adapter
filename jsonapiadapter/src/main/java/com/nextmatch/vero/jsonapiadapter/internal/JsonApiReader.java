package com.nextmatch.vero.jsonapiadapter.internal;

import com.google.gson.Gson;
import com.google.gson.InstanceCreator;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.reflect.TypeToken;
import com.nextmatch.vero.jsonapiadapter.model.Error;
import com.nextmatch.vero.jsonapiadapter.model.Resource;
import com.nextmatch.vero.jsonapiadapter.model.ResourceIdentifier;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author vero
 * @since 2016. 11. 28.
 */
class JsonApiReader {

    private Gson _context;
    private ConstructorConstructor _constructor;

    JsonApiReader(Gson context) {
        this._context = context;
        this._constructor = new ConstructorConstructor(Collections.<Type, InstanceCreator<?>>emptyMap());
    }

    <T extends Resource> T readData(JsonObject dataJsonObject, TypeToken<T> typeToken) {
        T instance = _constructor.get(typeToken).construct();
        instance.setIdentifier(readResourceIdentifier(dataJsonObject));

        return instance;
    }

    private ResourceIdentifier readResourceIdentifier(JsonObject dataJsonObject) {
        return _context.fromJson(dataJsonObject, ResourceIdentifier.class);
    }

    List<Error> readErrors(JsonArray errorJsonArray) {
        List<Error> errors = new ArrayList<>();
        for (int i = 0; i < errorJsonArray.size(); i++) {
            JsonObject errorJsonObject = errorJsonArray.get(i).getAsJsonObject();
            errors.add(_context.fromJson(errorJsonObject, Error.class));
        }

        return errors;
    }

}
