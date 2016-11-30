package com.nextmatch.vero.jsonapiadapter.internal;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.nextmatch.vero.jsonapiadapter.JsonApiConstants;
import com.nextmatch.vero.jsonapiadapter.model.Resource;

import java.util.List;
import java.util.Map;

/**
 * @author vero
 * @since 2016. 11. 30.
 */
class JsonApiWriter implements JsonApiConstants {

    private Gson _context;

    JsonApiWriter(Gson context) {
        this._context = context;
    }

    <T extends Resource> void writeData(T data, JsonObject jsonObject) {
        JsonObject dataJsonObject = null;
        if (data != null)
            dataJsonObject = writeData(data);

        jsonObject.add(NAME_DATA, dataJsonObject);
    }

    <T extends Resource> void writeDataArray(List<T> data, JsonObject jsonObject) {
        if (data == null) return;

        JsonArray dataJsonArray = new JsonArray();
        for (T resource : data)
            dataJsonArray.add(writeData(resource));

        jsonObject.add(NAME_DATA, dataJsonArray);
    }

    private <T extends Resource> JsonObject writeData(T data) {
        JsonObject jsonObject = new JsonObject();
        if (data.getIdentifier().getId() != null)
            jsonObject.addProperty(NAME_ID, data.getIdentifier().getId());
        jsonObject.addProperty(NAME_TYPE, data.getIdentifier().getType());

        JsonObject attributesJsonObject = new JsonObject();
        try {
            Map<String, BoundField> boundFields = ConverterUtils.getBoundFields(_context, TypeToken.get(data.getClass()));
            for (String name : boundFields.keySet()) {
                BoundField boundField = boundFields.get(name);
                Object value = boundField.getField().get(data);
                if (value != null)
                    attributesJsonObject.add(name, _context.toJsonTree(value));
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        jsonObject.add(NAME_ATTRIBUTES, attributesJsonObject);

        return jsonObject;
    }

}
