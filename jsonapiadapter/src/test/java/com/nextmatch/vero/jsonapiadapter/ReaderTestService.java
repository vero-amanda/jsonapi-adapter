package com.nextmatch.vero.jsonapiadapter;

import com.nextmatch.vero.jsonapiadapter.model.Article;
import com.nextmatch.vero.jsonapiadapter.model.People;
import com.nextmatch.vero.jsonapiadapter.model.Person;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.POST;
import rx.Observable;

/**
 * @author vero
 * @since 2016. 11. 30.
 */
public interface ReaderTestService {

    @POST("jsonapi/single/resource")
    Call<Article> singleResource();

    @POST("single/resource")
    Observable<Article>  singleResourceRx();

    @POST("json/array/peoples")
    Call<List<Person>> people();

}
