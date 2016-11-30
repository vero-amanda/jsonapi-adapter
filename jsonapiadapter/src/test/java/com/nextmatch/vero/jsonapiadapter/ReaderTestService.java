package com.nextmatch.vero.jsonapiadapter;

import com.nextmatch.vero.jsonapiadapter.model.Article;

import retrofit2.Call;
import retrofit2.http.POST;

/**
 * @author vero
 * @since 2016. 11. 30.
 */
public interface ReaderTestService {

    @POST("single/resource")
    Call<Article> singleResource();

}
