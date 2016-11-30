package com.nextmatch.vero.jsonapiadapter.internal;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.nextmatch.vero.jsonapiadapter.model.Resource;

import java.util.List;

/**
 * @author vero
 * @since 2016. 11. 27.
 */
public class JsonApiRequestAdapter<T extends Resource> implements JsonApiAdapter {

    private JsonObject _jsonApiObject;
    private JsonApiWriter _writer;

    private T _data;
    private List<T> _dataList;
    private List<Resource> _includedList;

    public JsonApiRequestAdapter(Gson context) {
        this._jsonApiObject = new JsonObject();
        this._writer = new JsonApiWriter(context);
    }

    public JsonApiRequestAdapter(Gson context, T data) {
        this(context);
        writeData(data);
    }

    public JsonApiRequestAdapter(Gson context, List<T> dataList) {
        this(context);
        writeDataArray(dataList);
    }

    private void writeData(T data) {
        _writer.writeData(data, _jsonApiObject);
    }

    private void writeDataArray(List<T> data) {
        _writer.writeDataArray(data, _jsonApiObject);
    }

    @Override
    public JsonObject getJsonApiObject() {
        return this._jsonApiObject;
    }

}
