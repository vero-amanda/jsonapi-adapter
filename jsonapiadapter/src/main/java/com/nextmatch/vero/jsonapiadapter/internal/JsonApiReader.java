package com.nextmatch.vero.jsonapiadapter.internal;

import com.google.gson.Gson;
import com.google.gson.InstanceCreator;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.reflect.TypeToken;
import com.nextmatch.vero.jsonapiadapter.JsonApiConstants;
import com.nextmatch.vero.jsonapiadapter.model.Error;
import com.nextmatch.vero.jsonapiadapter.model.Resource;
import com.nextmatch.vero.jsonapiadapter.model.ResourceIdentifier;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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

    <T extends Resource> T readResource(JsonObject dataJsonObject, TypeToken<T> typeToken) {
        T instance = _constructor.get(typeToken).construct();
        instance.setIdentifier(readResourceIdentifier(dataJsonObject));

        if (dataJsonObject.has(JsonApiConstants.NAME_ATTRIBUTES)) {
            readResourceAttributes(dataJsonObject.get(JsonApiConstants.NAME_ATTRIBUTES).getAsJsonObject(), typeToken, instance);
        }

        return instance;
    }

    Map<String, JsonElement> readRelationships(JsonObject jsonObject) {
        if (jsonObject.has(JsonApiConstants.NAME_RELATIONSHIPS)) {
            Map<String, JsonElement> relationships = new LinkedHashMap<>();
            JsonObject relationship = jsonObject.get(JsonApiConstants.NAME_RELATIONSHIPS).getAsJsonObject();
            for (Map.Entry<String, JsonElement> entry : relationship.entrySet()) {
                relationships.put(entry.getKey(), entry.getValue());
            }

            return relationships;
        }

        return null;
    }

    private ResourceIdentifier readResourceIdentifier(JsonObject jsonObject) {
        return _context.fromJson(jsonObject, ResourceIdentifier.class);
    }

    private <T extends Resource> void readResourceAttributes(JsonObject jsonObject, TypeToken<T> typeToken, T instance) {
        for (BoundField boundField : ConverterUtils.getBoundFields(_context, typeToken).values()) {
            if (jsonObject.has(boundField.getName())) {
                try {
                    boundField.getField().set(instance, _context.fromJson(jsonObject.get(boundField.getName()),
                            boundField.getTypeToken().getRawType()));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    List<Error> readErrors(JsonArray jsonArray) {
        List<Error> errors = new ArrayList<>();
        for (int i = 0; i < jsonArray.size(); i++) {
            JsonObject errorJsonObject = jsonArray.get(i).getAsJsonObject();
            errors.add(_context.fromJson(errorJsonObject, Error.class));
        }

        return errors;
    }

}
