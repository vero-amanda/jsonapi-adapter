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
import com.nextmatch.vero.jsonapiadapter.model.Links;
import com.nextmatch.vero.jsonapiadapter.model.Resource;
import com.nextmatch.vero.jsonapiadapter.model.ResourceIdentifier;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * JsonApiResponseAdapter 에서 사용되는 실제로 Parsing 로직을 담당하는 internal Reader 객체.
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

    boolean hasData(JsonObject jsonObject) {
        return jsonObject.has(JsonApiConstants.NAME_DATA);
    }

    boolean hasErrors(JsonObject jsonObject) {
        return jsonObject.has(JsonApiConstants.NAME_ERRORS);
    }

    boolean hasRelationships(JsonObject jsonObject) {
        return jsonObject.has(JsonApiConstants.NAME_RELATIONSHIPS);
    }

    boolean hasIncluded(JsonObject jsonObject) {
        return jsonObject.has(JsonApiConstants.NAME_INCLUDED);
    }

    JsonElement getData(JsonObject jsonObject) {
        return jsonObject.get(JsonApiConstants.NAME_DATA);
    }

    JsonArray getIncludedJsonArray(JsonObject jsonObject) {
        return jsonObject.get(JsonApiConstants.NAME_INCLUDED).getAsJsonArray();
    }

    /**
     * 최상위 Resource Data 를 읽어 매개변수로 전달받은 저장소에 넣어준다.
     * @param data             Data 가 저장될 Collection
     * @param relationships    Data Relationships 가 저장될 Map
     * @param jsonObject       JsonApi JsonObject
     * @param typeToken        Data TypeToken
     * @param <T>              Data Type
     */
    <T extends Resource> void readData(List<T> data, Map<ResourceIdentifier, Map<String, JsonObject>> relationships, JsonObject jsonObject, TypeToken<T> typeToken) {
        JsonElement jsonElement = getData(jsonObject);
        JsonArray jsonArray;
        if (jsonElement.isJsonArray()) {
            jsonArray = jsonElement.getAsJsonArray();
        } else {
            jsonArray = new JsonArray();
            jsonArray.add(jsonElement.getAsJsonObject());
        }

        for (int i = 0; i < jsonArray.size(); i++) {
            JsonObject dataJsonObject = jsonArray.get(i).getAsJsonObject();
            T instance = readResource(dataJsonObject, typeToken);
            data.add(instance);

            if (hasRelationships(dataJsonObject)) {
                Map<String, JsonObject> relationshipMap = readRelationships(dataJsonObject);
                if (relationshipMap != null) relationships.put(instance.getIdentifier(), relationshipMap);
            }
        }
    }

    /**
     * 최상위 Error 를 읽어 Collection 에 저장하고 반환.
     * @param jsonObject   JsonApi JsonObject
     * @return Error Collection
     */
    List<Error> readErrors(JsonObject jsonObject) {
        JsonArray jsonArray = jsonObject.get(JsonApiConstants.NAME_ERRORS).getAsJsonArray();
        List<Error> errors = new ArrayList<>();
        for (int i = 0; i < jsonArray.size(); i++) {
            JsonObject errorJsonObject = jsonArray.get(i).getAsJsonObject();
            errors.add(_context.fromJson(errorJsonObject, Error.class));
        }

        return errors;
    }

    /**
     * Data JsonObject 로부터 Resource 객체를 생성 후 반환.
     * @param dataJsonObject    Data JsonObject
     * @param typeToken         Resource 객체의 TypeToken
     * @param <T>               Data Type
     * @return Resource 객체
     */
    private <T extends Resource> T readResource(JsonObject dataJsonObject, TypeToken<T> typeToken) {
        T instance = _constructor.get(typeToken).construct();
        instance.setIdentifier(readResourceIdentifier(dataJsonObject));

        if (dataJsonObject.has(JsonApiConstants.NAME_ATTRIBUTES)) {
            readResourceAttributes(dataJsonObject.get(JsonApiConstants.NAME_ATTRIBUTES).getAsJsonObject(), instance);
        }

        return instance;
    }

    /**
     * Data JsonObject 에서 Relationships 정보를 읽어 Map 으로 반환.
     * @param jsonObject    Data Object (Top Level or Included)
     * @return Name, JsonObject Map.
     */
    Map<String, JsonObject> readRelationships(JsonObject jsonObject) {
        Map<String, JsonObject> relationships = new LinkedHashMap<>();
        JsonObject relationship = jsonObject.get(JsonApiConstants.NAME_RELATIONSHIPS).getAsJsonObject();
        for (Map.Entry<String, JsonElement> entry : relationship.entrySet()) {
            relationships.put(entry.getKey(), entry.getValue().getAsJsonObject());
        }

        return relationships;
    }

    List<JsonObject> readIncluded(JsonObject jsonObject) {
        JsonArray jsonArray = getIncludedJsonArray(jsonObject);
        List<JsonObject> jsonObjects = new ArrayList<>(jsonArray.size());
        for (int i = 0; i < jsonArray.size(); i++) {
            jsonObjects.add(jsonArray.get(i).getAsJsonObject());
        }

        return jsonObjects;
    }

    /**
     * Included 의 Array Data 를 Collection 으로 반환.
     * @param jsonApiObject     Root JsonObject
     * @param dataJsonArray     Relationships#Data JsonArray
     * @param classOfT          Array Data 의 Type
     * @param <T>               Array Data 의 Type
     * @return Collection Included Data
     */
    <T extends Resource> List<T> readCollectionIncluded(JsonObject jsonApiObject, JsonArray dataJsonArray, Class<T> classOfT) {
        List<ResourceIdentifier> relationshipIdentifiers = relationshipsToIdentifierList(dataJsonArray);
        List<T> includedList = new ArrayList<>();

        JsonArray jsonArray = jsonApiObject.get(JsonApiConstants.NAME_INCLUDED).getAsJsonArray();
        for (int i = 0; i < jsonArray.size(); i++) {
            JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();
            for (ResourceIdentifier identifier : relationshipIdentifiers) {
                if (equalsResourceIdentifier(identifier, jsonObject)) {
                    includedList.add(readResource(jsonObject, TypeToken.get(classOfT)));
                    break;
                }
            }
        }

        return includedList;
    }

    /**
     * Included 의 Object Data 를 반환.
     * @param jsonApiObject     Root JsonObject
     * @param dataJsonObject    Relationships#Data JsonObject
     * @param classOfT          Data 의 Type
     * @param <T>               Data 의 Type
     * @return Collection Included Data
     */
    <T extends Resource> T readIncluded(JsonObject jsonApiObject, JsonObject dataJsonObject, Class<T> classOfT) {
        ResourceIdentifier identifier = readResourceIdentifier(dataJsonObject);
        JsonArray jsonArray = jsonApiObject.get(JsonApiConstants.NAME_INCLUDED).getAsJsonArray();
        for (int i = 0; i < jsonArray.size(); i++) {
            JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();
            if (equalsResourceIdentifier(identifier, jsonObject)) {
                return readResource(jsonObject, TypeToken.get(classOfT));
            }
        }

        return readResource(dataJsonObject, TypeToken.get(classOfT));
    }

    JsonObject findIncludedJsonObjectFromResourceIdentifier(JsonArray includedJsonArray, ResourceIdentifier resourceIdentifier) {
        for (int i = 0; i < includedJsonArray.size(); i++) {
            JsonObject jsonObject = includedJsonArray.get(i).getAsJsonObject();
            if (equalsResourceIdentifier(resourceIdentifier, jsonObject)) {
                return jsonObject;
            }
        }

        return null;
    }

    <L extends Links> Links readLinks(JsonObject jsonObject, Class<L> classOfLinks) {
        if (jsonObject.has(JsonApiConstants.NAME_LINKS)) {
            return _context.fromJson(jsonObject.get(JsonApiConstants.NAME_LINKS), classOfLinks);
        }

        return null;
    }

    /**
     * Data JsonOjbect 에서 Type, Id 로 ResourceIdentifier 를 생성 후 반환.
     * @param jsonObject    Data JsonObject
     * @return ResourceIdentifier from JsonObject
     */
    private ResourceIdentifier readResourceIdentifier(JsonObject jsonObject) {
        return _context.fromJson(jsonObject, ResourceIdentifier.class);
    }

    /**
     * JsonObject 로부터 Field 정보를 가져와 값을 채워 반환.
     * @param jsonObject    Resource JsonObject
     * @param instance      값이 채워질 Resource Instance
     * @param <T>           Resource Type
     */
    private <T extends Resource> void readResourceAttributes(JsonObject jsonObject, T instance) {
        for (BoundField boundField : ConverterUtils.getBoundFields(_context, TypeToken.get(instance.getClass())).values()) {
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

    /**
     * ResourceIdentifier 가 JsonObject 의 type, id와 일치하는지 여부를 반환
     * id 와 type JsonObject 에 모두 존재하고 ResourceIdentifier 의 id 와 type 이 Null 이 아니여야 한다.
     * @param identifier        Relationships 정보로 생성한 ResourceIdentifier
     * @param dataJsonObject    Included Data JsonObject
     * @return type, id 가 일치하는지 여부.
     */
    private boolean equalsResourceIdentifier(ResourceIdentifier identifier, JsonObject dataJsonObject) {
        if (dataJsonObject.has(JsonApiConstants.NAME_ID) && dataJsonObject.has(JsonApiConstants.NAME_TYPE)) {
            String id = dataJsonObject.get(JsonApiConstants.NAME_ID).getAsString();
            String type = dataJsonObject.get(JsonApiConstants.NAME_TYPE).getAsString();
            if (identifier.getId().equals(id) && identifier.getType().equals(type)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Array Relationships 의 Data Array 를 ResourceIdentifier 목록으로 반환.
     * @param jsonArray    Array Relationships 의 JsonArray
     * @return ResourceIdentifier Collection
     */
    private List<ResourceIdentifier> relationshipsToIdentifierList(JsonArray jsonArray) {
        List<ResourceIdentifier> identifiers = new ArrayList<>();
        for (int i = 0; i < jsonArray.size(); i++) {
            JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();
            identifiers.add(readResourceIdentifier(jsonObject));
        }

        return identifiers;
    }

}
