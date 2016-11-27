package com.nextmatch.vero.jsonapiadapter.retrofit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.nextmatch.vero.jsonapiadapter.internal.JsonApiTypeAdapterFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * @author vero
 * @since 2016. 11. 22.
 */
public class JsonApiConverterFactory extends Converter.Factory {

    public static JsonApiConverterFactory create() {
        Gson gson = new GsonBuilder()
                .registerTypeAdapterFactory(new JsonApiTypeAdapterFactory())
                .create();
        return create(gson);
    }

    public static JsonApiConverterFactory create(Gson context) {
        return new JsonApiConverterFactory(context);
    }

    private Gson _context;

    private JsonApiConverterFactory(Gson context) {
        this._context = context;
    }

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        TypeAdapter<?> adapter = _context.getAdapter(TypeToken.get(type));
        return new JsonApiResponseBodyConverter<>(_context, adapter);
    }

    @Override
    public Converter<?, RequestBody> requestBodyConverter(Type type, Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {
        TypeAdapter<?> adapter = _context.getAdapter(TypeToken.get(type));
        return new JsonApiRequestBodyConverter<>(_context, adapter);
    }

}
