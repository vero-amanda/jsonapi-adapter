package com.nextmatch.vero.jsonapiadapter.internal;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.nextmatch.vero.jsonapiadapter.JsonApiConstants;
import com.nextmatch.vero.jsonapiadapter.model.Error;
import com.nextmatch.vero.jsonapiadapter.model.JsonApiParseException;
import com.nextmatch.vero.jsonapiadapter.model.Resource;
import com.nextmatch.vero.jsonapiadapter.model.ResourceIdentifier;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author vero
 * @since 2016. 11. 27.
 */
public class JsonApiResponseAdapter<T extends Resource> implements JsonApiAdapter {

    private JsonObject _jsonApiObject;
    private TypeToken<T> _typeToken;
    private JsonApiReader _reader;

    private List<T> _data;
    private Map<ResourceIdentifier, Map<String, JsonElement>> _relationships;
    private List<Error> _errors;

    JsonApiResponseAdapter(Gson context, TypeToken<T> typeToken, JsonObject jsonApiObject) {
        this._jsonApiObject = jsonApiObject;
        this._typeToken = typeToken;
        this._reader = new JsonApiReader(context);

        readTopLevel();
    }

    /**
     * Errors가 없는 것으로 Interface 성공 여부를 확인.
     * @return Interface 성공 여부.
     */
    public boolean isSuccess() {
        return _errors == null;
    }

    /**
     * 기본 데이터를 목록으로 반환.
     * @return 기본 데이터 목록.
     */
    public List<T> getDataList() {
        return this._data;
    }

    /**
     * 기본 데이터를 반환.
     * @return 기본 데이터 객체.
     */
    public T getData() {
        if (_data != null && !_data.isEmpty())
            return _data.get(0);

        return null;
    }

    /**
     * Relationships 정보가 존재하는지 확인.
     * 첫번째 Data를 검사한다
     * @param name    Relationship name
     * @return 매개변수로 받은 Name을 Key값으로 갖고 있는 Relationships 존재여부.
     */
    public boolean hasRelationships(String name) {
        if (getData() == null) throw new JsonApiParseException("Data도 없는데 무슨짓잉가");
        return hasRelationships(getData(), name);
    }

    /**
     * Relationships 정보가 존재하는지 확인.
     * @param data    ResourceIdentifier 를 갖고있는 Data Resource.
     * @param name    Relationship name
     * @return 매개변수로 받은 Name을 Key값으로 갖고 있는 Relationships 존재여부.
     */
    public boolean hasRelationships(T data, String name) {
        return hasRelationships(data, name, false);
    }

    @Override
    public JsonObject getJsonApiObject() {
        return _jsonApiObject;
    }

    /**
     * Resource 에 Relationship 정보가 있는지
     * @param resource
     * @param name
     * @param <R>
     * @return
     */
    private <R extends Resource> boolean hasRelationships(R resource, String name, boolean isIncluded) {
        if (isIncluded) {
            // TODO: 2016. 11. 28. included 에서 relationships 검사..
            return false;
        } else {
            return hasRelationshipsFromData(resource, name);
        }
    }

    private <R extends Resource> boolean hasRelationshipsFromData(R resource, String name) {
        if (_relationships != null && !_relationships.isEmpty()) {
            if (_relationships.containsKey(resource.getIdentifier())) {
                return _relationships.get(resource.getIdentifier()).containsKey(name);
            }
        }

        return false;
    }

    /**
     * Error 목록 반환.
     * @return Error 목록.
     */
    public List<Error> getErrors() {
        return this._errors;
    }

    /**
     * Top Level Tag parsing.
     */
    private void readTopLevel() {
        if (_jsonApiObject.has(JsonApiConstants.NAME_DATA)) {
            _data = new ArrayList<>();
            _relationships = new LinkedHashMap<>();

            JsonElement dataJsonElement = _jsonApiObject.get(JsonApiConstants.NAME_DATA);
            if (dataJsonElement.isJsonArray()) {
                for (int i = 0; i < dataJsonElement.getAsJsonArray().size(); i++) {
                    JsonObject dataJsonObject = dataJsonElement.getAsJsonArray().get(i).getAsJsonObject();
                    readData(dataJsonObject);
                }
            } else {
                readData(dataJsonElement.getAsJsonObject());
            }
        } else if (_jsonApiObject.has(JsonApiConstants.NAME_ERRORS)) {
            JsonArray errorJsonArray = _jsonApiObject.get(JsonApiConstants.NAME_ERRORS).getAsJsonArray();
            readErrors(errorJsonArray);
        }
    }

    /**
     * Top Level Data tag parsing.
     * @param jsonObject    Data object
     */
    private void readData(JsonObject jsonObject) {
        T instance = readResource(jsonObject, _typeToken);
        Map<String, JsonElement> relationships = _reader.readRelationships(jsonObject);

        _data.add(instance);
        if (relationships != null)
            _relationships.put(instance.getIdentifier(), relationships);
    }

    /**
     * Resource parsing
     * @param jsonObject    Resource tag jsonObject
     * @param typeToken     Resource TypeToken
     * @param <R>           Resource class
     * @return Resource instance
     */
    private <R extends Resource> R readResource(JsonObject jsonObject, TypeToken<R> typeToken) {
        return _reader.readResource(jsonObject, typeToken);
    }

    private void readErrors(JsonArray jsonArray) {
        _errors = _reader.readErrors(jsonArray);
    }

}
