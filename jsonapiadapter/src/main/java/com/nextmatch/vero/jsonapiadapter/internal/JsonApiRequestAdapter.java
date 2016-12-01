package com.nextmatch.vero.jsonapiadapter.internal;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.nextmatch.vero.jsonapiadapter.model.JsonApiParseException;
import com.nextmatch.vero.jsonapiadapter.model.Resource;
import com.nextmatch.vero.jsonapiadapter.model.ResourceIdentifier;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 일단 간단하게 Resource 와 Resource 에 포함된 Links 만 처리
 * 이후 추가 기능이 생기면 추가할것
 * @author vero
 * @since 2016. 11. 27.
 */
public class JsonApiRequestAdapter<T extends Resource> implements JsonApiAdapter {

    private JsonObject _jsonApiObject;
    private JsonApiWriter _writer;

    private T _data;
    private List<T> _dataList;
    private List<Resource> _includedList;
    private Map<ResourceIdentifier, Map<String, JsonObject>> _relationships;

    public JsonApiRequestAdapter(Gson context) {
        this._writer = new JsonApiWriter(context);
    }

    public JsonApiRequestAdapter(Gson context, T data) {
        this(context);
        this._data = data;
    }

    public JsonApiRequestAdapter(Gson context, List<T> dataList) {
        this(context);
        this._dataList = dataList;
    }

    public <R extends Resource> void addRelationship(T data, String name, R resource) {
        if (data.getIdentifier().getId() == null) throw new JsonApiParseException("Relationships must have to should ID");
        createNeedRelationshipsStorage();
        _includedList.add(resource);
        Map<String, JsonObject> relationships = findDataRelationshipsMap(data);
        relationships.put(name, _writer.writeRelationshipsIdentifier(resource));
    }

    public <R extends Resource> void addListRelationship(T data, String name, List<R> resourceList) {
        if (data.getIdentifier().getId() == null) throw new JsonApiParseException("Relationships must have to should ID");
        createNeedRelationshipsStorage();
        _includedList.addAll(resourceList);
        Map<String, JsonObject> relationships = findDataRelationshipsMap(data);
        relationships.put(name, _writer.writeListRelationshipsIdentifier(resourceList));
    }

    @Override
    public JsonObject getJsonApiObject() {
        writeNeedJsonApiObject();
        return this._jsonApiObject;
    }

    private void writeNeedJsonApiObject() {
        if (_jsonApiObject != null && _jsonApiObject.entrySet().size() > 0)
            return;

        _jsonApiObject = new JsonObject();

        if (_dataList != null) {
            _writer.writeDataArray(_dataList, _relationships, _jsonApiObject);
        } else {
            _writer.writeData(_data, _relationships, _jsonApiObject);
        }

        _writer.writeIncluded(_includedList, _jsonApiObject);
    }

    private void createNeedRelationshipsStorage() {
        if (_includedList == null)
            _includedList = new ArrayList<>();

        if (_relationships == null)
            _relationships = new LinkedHashMap<>();
    }

    private Map<String, JsonObject> findDataRelationshipsMap(T data) {
        Map<String, JsonObject> relationships;
        if (_relationships.containsKey(data.getIdentifier())) {
            relationships = _relationships.get(data.getIdentifier());
        } else {
            relationships = new LinkedHashMap<>();
            _relationships.put(data.getIdentifier(), relationships);
        }

        return relationships;
    }
}
