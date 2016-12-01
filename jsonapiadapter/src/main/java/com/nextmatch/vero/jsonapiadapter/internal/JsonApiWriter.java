package com.nextmatch.vero.jsonapiadapter.internal;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.nextmatch.vero.jsonapiadapter.JsonApiConstants;
import com.nextmatch.vero.jsonapiadapter.model.JsonApiParseException;
import com.nextmatch.vero.jsonapiadapter.model.Resource;
import com.nextmatch.vero.jsonapiadapter.model.ResourceIdentifier;

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

    <T extends Resource> void writeData(T data, Map<ResourceIdentifier, Map<String, JsonObject>> relationships, JsonObject jsonObject) {
        JsonObject dataJsonObject = null;

        if (data != null) {
            dataJsonObject = writeData(data);
            writeRelationships(data, relationships, dataJsonObject);
        }

        jsonObject.add(NAME_DATA, dataJsonObject);
    }

    <T extends Resource> void writeDataArray(List<T> data, Map<ResourceIdentifier, Map<String, JsonObject>> relationships, JsonObject jsonObject) {
        if (data == null) return;

        JsonArray dataJsonArray = new JsonArray();
        for (T resource : data) {
            JsonObject dataJsonObject = writeData(resource);
            writeRelationships(resource, relationships, dataJsonObject);
            dataJsonArray.add(dataJsonObject);
        }

        jsonObject.add(NAME_DATA, dataJsonArray);
    }

    void writeIncluded(List<Resource> includedList, JsonObject jsonObject) {
        if (includedList == null || includedList.isEmpty()) return;

        JsonArray jsonArray = new JsonArray();
        for (Resource resource : includedList) {
            jsonArray.add(writeData(resource));
        }

        jsonObject.add(NAME_INCLUDED, jsonArray);
    }

    <T extends Resource> JsonObject writeRelationshipsIdentifier(T resource) {
        if (resource.getIdentifier().getId() == null) throw new JsonApiParseException("Relationships must have to should ID");

        JsonObject jsonObject = new JsonObject();
        jsonObject.add(NAME_DATA, makeDefaultResourceJsonObject(resource));

        return jsonObject;
    }

    <T extends Resource> JsonObject writeListRelationshipsIdentifier(List<T> resourceList) {
        JsonObject jsonObject = new JsonObject();
        JsonArray dataArray = new JsonArray();
        for (T resource : resourceList) {
            if (resource.getIdentifier().getId() == null)
                throw new JsonApiParseException("Relationships must have to should ID");

            dataArray.add(makeDefaultResourceJsonObject(resource));
        }

        jsonObject.add(NAME_DATA, dataArray);

        return jsonObject;
    }

    private <T extends Resource> JsonObject writeData(T data) {
        JsonObject jsonObject = makeDefaultResourceJsonObject(data);
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
        if (data.getLinks() != null) {
            jsonObject.add(NAME_LINKS, _context.toJsonTree(data.getLinks()));
        }

        return jsonObject;
    }

    private <T extends Resource> void writeRelationships(T resource, Map<ResourceIdentifier, Map<String, JsonObject>> relationships, JsonObject jsonObject) {
        if (relationships != null && relationships.containsKey(resource.getIdentifier())) {
            JsonObject relationshipJsonObject = new JsonObject();
            Map<String, JsonObject> relationship = relationships.get(resource.getIdentifier());
            for (String name : relationship.keySet()) {
                relationshipJsonObject.add(name, relationship.get(name));
            }

            jsonObject.add(NAME_RELATIONSHIPS, relationshipJsonObject);
        }
    }

    private <T extends Resource> JsonObject makeDefaultResourceJsonObject(T resource) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(NAME_TYPE, resource.getIdentifier().getType());
        if (resource.getIdentifier().getId() != null)
            jsonObject.addProperty(NAME_ID, resource.getIdentifier().getId());

        return jsonObject;
    }

}
