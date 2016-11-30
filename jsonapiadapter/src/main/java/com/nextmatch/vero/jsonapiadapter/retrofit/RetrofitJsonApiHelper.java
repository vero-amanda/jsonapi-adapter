package com.nextmatch.vero.jsonapiadapter.retrofit;

import com.google.gson.internal.Primitives;
import com.nextmatch.vero.jsonapiadapter.internal.JsonApiResponseAdapter;
import com.nextmatch.vero.jsonapiadapter.model.Resource;

import retrofit2.Response;

/**
 * @author vero
 * @since 2016. 11. 30.
 */
public final class RetrofitJsonApiHelper {

    @SuppressWarnings("unchecked")
    public static <T extends Resource> JsonApiResponseAdapter<T> getJsonApiAdapterFromResponse(Response<T> response) {
        return Primitives.wrap(JsonApiResponseAdapter.class).cast(response.body());
    }

    @SuppressWarnings("unchecked")
    public static <T extends Resource> JsonApiResponseAdapter<T> getJsonApiAdapterFromResource(Resource resource) {
        return Primitives.wrap(JsonApiResponseAdapter.class).cast(resource);
    }

}
