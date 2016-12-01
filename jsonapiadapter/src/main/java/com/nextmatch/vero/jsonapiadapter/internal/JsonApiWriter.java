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
 * JsonApiRequestAdatper 에서 사용되는 실제로 Resource 를 JsonObject 로 parsing 해주는 internal Writer 객체.
 * @author vero
 * @since 2016. 11. 30.
 */
class JsonApiWriter implements JsonApiConstants {

    private Gson _context;

    JsonApiWriter(Gson context) {
        this._context = context;
    }

    /**
     * 기본 데이터를 JsonObject 로 parsing 해서 상위 JsonObject 추가한다.
     * @param data             기본 데이터
     * @param relationships    기본 데이터 Relationships 저장소
     * @param jsonObject       parsing 한 JsonObject 를 추가할 상위 JsonObject
     * @param <T>              기본 데이터 Type
     */
    <T extends Resource> void writeData(T data, Map<ResourceIdentifier, Map<String, JsonObject>> relationships, JsonObject jsonObject) {
        JsonObject dataJsonObject = null;

        if (data != null) {
            dataJsonObject = writeData(data);
            writeRelationships(data, relationships, dataJsonObject);
        }

        jsonObject.add(NAME_DATA, dataJsonObject);
    }

    /**
     * 기본 데이터 목록을 JsonArray 로 parsing 해서 상위 JsonObject에 추가한다.
     * @param data             기본 데이터 목록
     * @param relationships    기본 데이터 Relationships 저장소
     * @param jsonObject       parsing 한 JsonObject 를 추가할 상위 JsonObject
     * @param <T>              기본 데이터 Type
     */
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

    /**
     * 저장소에 저장된 Included 목록을 JsonArray 로 parsing 후 상위 JsonObject 에 추가한다.
     * @param includedList    Included 목록
     * @param jsonObject      상위 JsonObject
     */
    void writeIncluded(List<Resource> includedList, JsonObject jsonObject) {
        if (includedList == null || includedList.isEmpty()) return;

        JsonArray jsonArray = new JsonArray();
        for (Resource resource : includedList) {
            jsonArray.add(writeData(resource));
        }

        jsonObject.add(NAME_INCLUDED, jsonArray);
    }

    /**
     * Relationship Resource 에서 Identifier 정보만 JsonObject 로 parsing 후 상위 data JsonObject 에 넣어 반환.
     * @param resource    Relationship Resource
     * @param <T>         Resource Type
     * @return Resource Identifier 가 data tag 로 포함된 JsonObject
     */
    <T extends Resource> JsonObject writeRelationshipsIdentifier(T resource) {
        if (resource.getIdentifier().getId() == null) throw new JsonApiParseException("Relationships must have to should ID");

        JsonObject jsonObject = new JsonObject();
        jsonObject.add(NAME_DATA, makeDefaultResourceJsonObject(resource));

        return jsonObject;
    }

    /**
     * Relationship Resource 목록에서 Identifier 정보만 JsonArray 로 parsing 후 상위 data JsonObject 에 넣어 반환.
     * @param resourceList    Relationship Resource 목록
     * @param <T>             Resource Type
     * @return Resource Identifier array 가 data tag 로 포함된 JsonObject
     */
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

    /**
     * 기본 데이터나 Included Resource 를 JsonObject 로 parsing
     * @param data    기본 데이나 or Included Resource
     * @param <T>     Resource Type
     * @return Resource JsonObject
     */
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

    /**
     * Relationships 정보를 저장소에서 찾아 상위 JsonObject 에 추가한다.
     * @param resource         relationship 대상 resource instance
     * @param relationships    Relationships 저장소
     * @param jsonObject       상위 JsonObject
     * @param <T>              Resource Type
     */
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

    /**
     * Resource 를 상속받아 구현된 Resource instance 에서 ResourceIdentifier 정보만 JsonObject 로 parsing 후 반환
     * @param resource    Resource instance
     * @param <T>         Resource Type
     * @return ResourceIdentifier 가 포함된 JsonObject
     */
    private <T extends Resource> JsonObject makeDefaultResourceJsonObject(T resource) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(NAME_TYPE, resource.getIdentifier().getType());
        if (resource.getIdentifier().getId() != null)
            jsonObject.addProperty(NAME_ID, resource.getIdentifier().getId());

        return jsonObject;
    }

}
