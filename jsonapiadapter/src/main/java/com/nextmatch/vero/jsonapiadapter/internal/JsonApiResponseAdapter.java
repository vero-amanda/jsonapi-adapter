package com.nextmatch.vero.jsonapiadapter.internal;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.nextmatch.vero.jsonapiadapter.JsonApiConstants;
import com.nextmatch.vero.jsonapiadapter.model.Error;
import com.nextmatch.vero.jsonapiadapter.model.JsonApiParseException;
import com.nextmatch.vero.jsonapiadapter.model.Links;
import com.nextmatch.vero.jsonapiadapter.model.Resource;
import com.nextmatch.vero.jsonapiadapter.model.ResourceIdentifier;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * GsonAdapter#fromJsonApi 에서 반환되는 ResponseAdapter.
 * 객체가 생성될때 Top Level tags 를 Parsing 하고,
 * Optional 로 추가될 수 있는 tags(included, links, meta, jsonapi...) 는 Type 을 매개변수로 받아 동적으로 구성할 수 있도록 구현.
 * @author vero
 * @since 2016. 11. 27.
 */
public class JsonApiResponseAdapter<T extends Resource> implements JsonApiAdapter {

    private JsonObject _jsonApiObject;
    private TypeToken<T> _typeToken;
    private JsonApiReader _reader;

    private List<T> _data;
    private Map<ResourceIdentifier, Map<String, JsonObject>> _relationships;
    private List<JsonObject> _included;
    private List<Error> _errors;

    JsonApiResponseAdapter(Gson context, TypeToken<T> typeToken, JsonObject jsonApiObject) {
        this._jsonApiObject = jsonApiObject;
        this._typeToken = typeToken;
        this._reader = new JsonApiReader(context);
        this._data = new ArrayList<>();
        this._relationships = new LinkedHashMap<>();

        readTopLevel();
    }

    /**
     * Top Level tag 정보를 읽는다
     * data, error, included
     */
    private void readTopLevel() {
        if (_reader.hasData(_jsonApiObject)) {
            _reader.readData(_data, _relationships, _jsonApiObject, _typeToken);
        } else if (_reader.hasErrors(_jsonApiObject)) {
            _errors = _reader.readErrors(_jsonApiObject);
        }

        if (_reader.hasIncluded(_jsonApiObject)) {
            _included = _reader.readIncluded(_jsonApiObject);
        }
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
     * @param resource      ResourceIdentifier를 가지는 Resource Instance
     * @param name          Relationship name
     * @param <R>           Resource Type
     * @return 매개변수로 받은 Name을 Key값으로 갖고 있는 Relationships 존재여부.
     */
    public <R extends Resource> boolean hasRelationships(R resource, String name) {
        if (_typeToken.getRawType().isInstance(resource)) {
            return hasRelationshipsFromData(resource.getIdentifier(), name);
        } else {
            return hasRelationshipsFromIncluded(resource.getIdentifier(), name);
        }
    }

    /**
     * Included 의 Array Data 를 Collection 으로 반환.
     * @param parentResource     기본 Data Resource 객체
     * @param name               Relationships 에 정의된 Name
     * @param classOfResource    Array Data 의 Type
     * @param <R>                Array Data 의 Type
     * @return Included Data Collection
     */
    public <R extends Resource> List<R> getIncludedCollection(T parentResource, String name, Class<R> classOfResource) {
        if (hasRelationships(parentResource, name)) {
            Map<String, JsonObject> relationshipsMap = _relationships.get(parentResource.getIdentifier());
            JsonElement dataJsonElement = relationshipsMap.get(name).get(JsonApiConstants.NAME_DATA);
            if (!dataJsonElement.isJsonArray()) throw new JsonApiParseException(name + " relationship은 array가 아님");
            if (_included == null || _included.isEmpty()) throw new JsonApiParseException("included tag가 존재하지 않음");

            return _reader.readIncludedCollection(_included, dataJsonElement.getAsJsonArray(), classOfResource);
        }

        return null;
    }

    /**
     * Included 의 Object Data 를 반환.
     * @param parentResource     기본 Data Resource 객체
     * @param name               Relationships 에 정의된 Name
     * @param classOfResource    Data 의 Type
     * @param <R>                Data 의 Type
     * @return Included Data
     */
    public <R extends Resource> R getIncluded(T parentResource, String name, Class<R> classOfResource) {
        if (hasRelationships(parentResource, name)) {
            Map<String, JsonObject> relationshipsMap = _relationships.get(parentResource.getIdentifier());
            JsonElement dataJsonElement = relationshipsMap.get(name).get(JsonApiConstants.NAME_DATA);
            if (!dataJsonElement.isJsonObject()) throw new JsonApiParseException(name + " relationship은 object가 아님");
            if (_included == null || _included.isEmpty()) throw new JsonApiParseException("included tag가 존재하지 않음");

            return _reader.readIncluded(_included, dataJsonElement.getAsJsonObject(), classOfResource);
        }

        return null;
    }

    public <L extends Links> Links getLinks(Class<L> classOfLinks) {
        return getLinks(null, classOfLinks);
    }

    public <L extends Links> Links getLinks(Resource resource, Class<L> classOfLinks) {
        if (resource == null) {
            return _reader.readLinks(_jsonApiObject, classOfLinks);
        } else if (_typeToken.getRawType().isInstance(resource)) {

        } else {

        }

        return null;
    }

    /**
     * Error 목록 반환.
     * @return Error 목록.
     */
    public List<Error> getErrors() {
        return this._errors;
    }

    @Override
    public JsonObject getJsonApiObject() {
        return _jsonApiObject;
    }

    /**
     * 기본 Data에 Relationships 정보가 존재하는지 확인.
     * @param identifier    Data ResourceIdentifier
     * @param name          Relationship name
     * @return 매개변수로 받은 Name을 Key값으로 갖고 있는 Relationships 존재여부.
     */
    private boolean hasRelationshipsFromData(ResourceIdentifier identifier, String name) {
        if (_relationships != null && !_relationships.isEmpty()) {
            if (_relationships.containsKey(identifier)) {
                return _relationships.get(identifier).containsKey(name);
            }
        }

        return false;
    }

    /**
     * Included Data Relationships 정보가 존재하는지 확인.
     * @param identifier    Included ResourceIdentifier
     * @param name          Relationship name
     * @return 매개변수로 받은 Name 을 Key 값으로 갖고 있는 Relationships 존재여부.
     */
    private boolean hasRelationshipsFromIncluded(ResourceIdentifier identifier, String name) {
        if (_included == null || _included.isEmpty()) throw new JsonApiParseException("included tag가 존재하지 않음");

        JsonObject includedObject = _reader.findIncludedJsonObjectFromResourceIdentifier(_included, identifier);
        if (includedObject != null) {
            if (_reader.hasRelationships(includedObject)) {
                Map<String, JsonObject> relationships = _reader.readRelationships(includedObject);
                return relationships != null && relationships.containsKey(name);
            }
        }
        return false;
    }

}
