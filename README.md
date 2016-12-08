# JsonApiAdapter
[![](https://jitpack.io/v/vero-amanda/jsonapi-adapter.svg)](https://jitpack.io/#vero-amanda/jsonapi-adapter)

Android [JsonApi][1] Spec v1.0 for [Gson][2]
[1]: http://jsonapi.org
[2]: https://github.com/google/gson

##Download

Root build.grald:

    allprojects {
        repositories {
            ...
            maven { url 'https://jitpack.io' }
        }
    }

Application build.gradle:

    dependencies {
        compile 'com.github.vero-amanda:jsonapi-adapter:1.1.0'
    }


##지원되는 기능
| **태그** |  **기능**  |  **읽기**  |  **쓰기**  |  **설명**  |
| :-----: | :-------- | :-------: | :-------: | :-------- |
| 최상위 | Null 기본 데이터 | 예 | 예 | |
| 최상위 | 단일 기본 데이터 | 예 | 예 | |
| 최상위 | 목록 기본 데이터 | 예 | 예 | |
| 최상위 | errors | 예 | 아니오 | |
| 최상위 | links | 예 | 아니오 | |
| 최상위 | jsonapi | 예 | 아니오 | |
| 최상위 | included | 예 | 예 | |
| 기본 데이터 | id tag | 예 | 예 | |
| 기본 데이터 | type tag | 예 | 예 | ```@Type``` Annotation 사용 |
| 기본 데이터 | relationships tag | 예 | 예 | |
| 기본 데이터 | links tag | 예 | 예 | |
| included | id tag | 예 | 예 | |
| included | type tag | 예 | 예 | |
| included | relationships tag | 예 | 아니오 | |
| included | links tag | 예 | 예 | |
| 기본 데이터 <br> relationships | links tag | 예 | 예 | |

##사용법
####JsonApi Resource Object
```java
@Type("articles")
public class Article extends Resource {

    @SerializedName("title")
    public String title;
    @SerializedName("body")
    public String body;
    public Date created;
    public Date updated;

}
```

####fromJsonApi
```java
Gson gson = new GsonBuilder()
        .registerTypeAdapterFactory(new JsonApiTypeAdapterFactory()).create();

GsonAdapter gsonAdapter = new GsonAdapter(gson);
JsonApiResponseAdapter<Article> responseAdapter = gsonAdapter.fromJsonApi(jsonApiString, Article.class);
if (responseAdapter.isSuccess()) {
    Article article = responseAdapter.getData();
    SimpleLinks links = (SimpleLinks) article.getLinks();
    People people = responseAdapter.getIncluded(article, "people", People.class);
// => List<Article> articles = responseAdapter.getDataList(); for Data array
} else {
    List<Error> errors = responseAdapter.getErrors();
}
```

####toJsonApi
```java
JsonApiRequestAdapter<Article> requestAdapter = new JsonApiRequestAdapter<>(_context, article);
System.out.println(new GsonAdapter(_context).toJsonApi(requestAdapter));
// => {"data":{"type":"articles","attributes":{"title":"title single resource","body":"body single resource"},"links":{"self":"http://google.com"}}}

```

####Retrofit
###### 1. Response
```java
Gson gson = new GsonBuilder()
        .registerTypeAdapterFactory(new JsonApiTypeAdapterFactory()).create();

Retrofit retrofit = new Retrofit.Builder()
        .baseUrl(server.url("/").toString())
        .addConverterFactory(JsonApiConverterFactory.create(gson))
        .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
        .build();

TestService service = retrofit.create(TestService.class);

Response<Article> response = service.singleResource().execute();
if (response.isSuccessful()) {
    JsonApiResponseAdapter<Article> responseAdapter = RetrofitJsonApiHelper.getJsonApiAdapterFromResponse(response);
    if (responseAdapter.isSuccess()) {
        Article article = responseAdapter.getData();
    }
}

```
###### 2. rx.Observable
```java
Gson gson = new GsonBuilder()
        .registerTypeAdapterFactory(new JsonApiTypeAdapterFactory()).create();

Retrofit retrofit = new Retrofit.Builder()
        .baseUrl(server.url("/").toString())
        .addConverterFactory(JsonApiConverterFactory.create(gson))
        .build();

TestService service = retrofit.create(TestService.class);

Observable<Article> response = service.singleResourceRx();
response.subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(
                new Action1<Article>() {
                    
                    @Override
                    public void call(Article response) {
                        JsonApiResponseAdapter<Article> responseAdapter = RetrofitJsonApiHelper.getJsonApiAdapterFromResource(resource);
                        Article article = responseAdapter.getData();
                    }
                    
                },
                new Action1<Throwable>() {

                    @Override
                    public void call(Throwable throwable) {
                        
                    }

                }
        );
```

##License

    Copyright 2016 Vero
    
    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at
    
       http://www.apache.org/licenses/LICENSE-2.0
    
    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

