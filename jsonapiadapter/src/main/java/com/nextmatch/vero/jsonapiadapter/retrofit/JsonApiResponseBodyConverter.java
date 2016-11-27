package com.nextmatch.vero.jsonapiadapter.retrofit;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Converter;

/**
 * @author vero
 * @since 2016. 11. 22.
 */
class JsonApiResponseBodyConverter<T> implements Converter<ResponseBody, T> {

    private Gson _context;
    private TypeAdapter<T> _adapter;

    JsonApiResponseBodyConverter(Gson context, TypeAdapter<T> adapter) {
        this._context = context;
        this._adapter = adapter;
    }

    @Override
    public T convert(ResponseBody value) throws IOException {
        JsonReader jsonReader = _context.newJsonReader(value.charStream());
        try {
            return _adapter.read(jsonReader);
        } finally {
            value.close();
        }
    }
}
