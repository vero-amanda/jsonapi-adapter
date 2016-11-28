package com.nextmatch.vero.jsonapiadapter.internal;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.nextmatch.vero.jsonapiadapter.JsonApiConstants;
import com.nextmatch.vero.jsonapiadapter.model.Error;
import com.nextmatch.vero.jsonapiadapter.model.Resource;

import java.util.ArrayList;
import java.util.List;

/**
 * @author vero
 * @since 2016. 11. 27.
 */
public class JsonApiResponseAdapter<T extends Resource> implements JsonApiAdapter {

    private JsonObject _jsonApiObject;
    private TypeToken<T> _typeToken;
    private JsonApiReader _reader;

    private List<T> _data;
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
     * Error 목록 반환.
     * @return Error 목록.
     */
    public List<Error> getErrors() {
        return this._errors;
    }

    private void readTopLevel() {
        if (_jsonApiObject.has(JsonApiConstants.NAME_DATA)) {
            _data = new ArrayList<>();
            JsonElement dataJsonElement = _jsonApiObject.get(JsonApiConstants.NAME_DATA);
            if (dataJsonElement.isJsonArray()) {
                for (int i = 0; i < dataJsonElement.getAsJsonArray().size(); i++) {
                    JsonObject dataJsonObject = dataJsonElement.getAsJsonArray().get(i).getAsJsonObject();
                    _data.add(readData(dataJsonObject, _typeToken));
                }
            } else {
                _data.add(readData(dataJsonElement.getAsJsonObject(), _typeToken));
            }
        } else if (_jsonApiObject.has(JsonApiConstants.NAME_ERRORS)) {
            JsonArray errorJsonArray = _jsonApiObject.get(JsonApiConstants.NAME_ERRORS).getAsJsonArray();
            readErrors(errorJsonArray);
        }
    }

    private <R extends Resource> R readData(JsonObject dataJsonObject, TypeToken<R> typeToken) {
        return _reader.readData(dataJsonObject, typeToken);
    }

    private void readErrors(JsonArray errorJsonArray) {
        _errors = _reader.readErrors(errorJsonArray);
    }

    @Override
    public JsonObject getJsonApiObject() {
        return _jsonApiObject;
    }

}
