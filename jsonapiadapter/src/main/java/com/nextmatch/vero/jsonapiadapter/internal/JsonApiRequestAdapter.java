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
 * 일단 간단하게 Resource 와 Resource 에 포함된 Links, Included 만 처리
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

    /**
     * 기본 데이터 Null 처리용 생성자.
     * @param context    Gson context
     */
    public JsonApiRequestAdapter(Gson context) {
        this._writer = new JsonApiWriter(context);
    }

    /**
     * 기본 데이터 처리용 생성자.
     * @param context    Gson context
     * @param data       Resource instance
     */
    public JsonApiRequestAdapter(Gson context, T data) {
        this(context);
        this._data = data;
    }

    /**
     * 배열 데이터 처리용 생성자.
     * @param context     Gson context
     * @param dataList    Resource Collection
     */
    public JsonApiRequestAdapter(Gson context, List<T> dataList) {
        this(context);
        this._dataList = dataList;
    }

    /**
     * 기본 데이터에 Resource instance Relationship 정보 추가.
     * @param data        기본 데이터
     * @param name        Relationships name
     * @param resource    Relationships resource
     * @param <R>         Relationships resource type
     */
    public <R extends Resource> void addRelationship(T data, String name, R resource) {
        if (data.getIdentifier().getId() == null) throw new JsonApiParseException("Relationships must have to should ID");
        createNeedRelationshipsStorage();
        _includedList.add(resource);
        Map<String, JsonObject> relationships = findDataRelationshipsMap(data);
        relationships.put(name, _writer.writeRelationshipsIdentifier(resource));
    }

    /**
     * 기본 데이터에 Resource collection Relationship 정보 추가.
     * @param data            기본 데이터
     * @param name            Relationships name
     * @param resourceList    Relationships resource collection
     * @param <R>             Relationships resource type
     */
    public <R extends Resource> void addListRelationship(T data, String name, List<R> resourceList) {
        if (data.getIdentifier().getId() == null) throw new JsonApiParseException("Relationships must have to should ID");
        createNeedRelationshipsStorage();
        _includedList.addAll(resourceList);
        Map<String, JsonObject> relationships = findDataRelationshipsMap(data);
        relationships.put(name, _writer.writeListRelationshipsIdentifier(resourceList));
    }

    /**
     * 주요 변수들을 저장소에 넣어 저장해 두었다가
     * Background task 에서 parsing 하기위해 JsonObject 를 요청하면 그 때 저장소의 Class 들을 JsonObject 로 변환하게 작성.
     */
    @Override
    public JsonObject getJsonApiObject() {
        writeNeedJsonApiObject();
        return this._jsonApiObject;
    }

    /**
     * 저장소에 저장해 놓은 JsonApi 정보들을 JsonObject 로 변환.
     * 생성해 놓은 JsonApiObject 가 존재할 경우 write 하지 않고 종료.
     */
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

    /**
     * Relationships, Included 저장소가 Null 일 경우 최초 생성.
     */
    private void createNeedRelationshipsStorage() {
        if (_includedList == null)
            _includedList = new ArrayList<>();

        if (_relationships == null)
            _relationships = new LinkedHashMap<>();
    }

    /**
     * Relationships 저장소에 기본 데이터 Relationships 정보가 있는지 확인하고,
     * 없을 경우 빈 저장소를 생성 후 반환.
     * @param data    기본 데이터
     * @return 기본 데이터의 Relationships 저장소
     */
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
