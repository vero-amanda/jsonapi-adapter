package com.nextmatch.vero.jsonapiadapter.retrofit;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonWriter;
import com.nextmatch.vero.jsonapiadapter.JsonApiConstants;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

import okhttp3.RequestBody;
import okio.Buffer;
import retrofit2.Converter;

/**
 * @author vero
 * @since 2016. 11. 22.
 */
class JsonApiRequestBodyConverter<T> implements Converter<T, RequestBody> {

    private Gson _context;
    private TypeAdapter<T> _adapter;

    JsonApiRequestBodyConverter(Gson context, TypeAdapter<T> adapter) {
        this._context = context;
        this._adapter = adapter;
    }

    @Override
    public RequestBody convert(T value) throws IOException {
        Buffer buffer = new Buffer();
        Writer writer = new OutputStreamWriter(buffer.outputStream(), JsonApiConstants.UTF_8);
        JsonWriter jsonWriter = _context.newJsonWriter(writer);
        _adapter.write(jsonWriter, value);
        jsonWriter.close();

        return RequestBody.create(JsonApiConstants.MEDIA_TYPE_DEFAULT, buffer.readByteString());
    }
}
