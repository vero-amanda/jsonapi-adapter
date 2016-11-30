package com.nextmatch.vero.jsonapiadapter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nextmatch.vero.jsonapiadapter.gson.GsonAdapter;
import com.nextmatch.vero.jsonapiadapter.internal.JsonApiResponseAdapter;
import com.nextmatch.vero.jsonapiadapter.internal.JsonApiTypeAdapterFactory;
import com.nextmatch.vero.jsonapiadapter.model.Article;
import com.nextmatch.vero.jsonapiadapter.model.Comment;
import com.nextmatch.vero.jsonapiadapter.model.Error;
import com.nextmatch.vero.jsonapiadapter.model.JsonApiParseException;
import com.nextmatch.vero.jsonapiadapter.model.People;
import com.nextmatch.vero.jsonapiadapter.model.Person;
import com.nextmatch.vero.jsonapiadapter.model.SimpleLinks;
import com.nextmatch.vero.jsonapiadapter.retrofit.JsonApiConverterFactory;
import com.nextmatch.vero.jsonapiadapter.retrofit.RetrofitJsonApiHelper;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import rx.Observable;
import rx.schedulers.Schedulers;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * @author vero
 * @since 2016. 11. 27.
 */
public class JsonApiReaderTest {

    private GsonAdapter _gsonAdapter;
    private ReaderTestService _service;
    private MockWebServer _server;

    @Before
    public void setUp() throws Exception {
        _server = new MockWebServer();
        _server.start();

        Gson gson = new GsonBuilder()
                .registerTypeAdapterFactory(new JsonApiTypeAdapterFactory()).create();

        _gsonAdapter = new GsonAdapter(gson);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(_server.url("/").toString())
                .addConverterFactory(JsonApiConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        _service = retrofit.create(ReaderTestService.class);
    }

    @After
    public void destroy() throws Exception {
        _server.shutdown();
    }

    @Test
    public void castResourceToJsonResponseAdapter() throws Exception {
        JsonApiResponseAdapter<Article> responseAdapter = _gsonAdapter.fromJsonApi(JsonApiStrings.simpleSingleResource, Article.class);
        assertNotNull(responseAdapter);
    }

    @Test
    public void resourceIdentifier() throws Exception {
        JsonApiResponseAdapter<Article> responseAdapter = _gsonAdapter.fromJsonApi(JsonApiStrings.simpleSingleResource, Article.class);
        if (responseAdapter.isSuccess()) {
            Article article = responseAdapter.getData();
            assertNotNull(article);
            assertNotNull(article.getIdentifier());
            assertTrue(article.getIdentifier().getId().equals("1"));
            assertTrue(article.getIdentifier().getType().equals("articles"));
        }
    }

    @Test
    public void resourceArrayIdentifier() throws Exception {
        JsonApiResponseAdapter<Article> responseAdapter = _gsonAdapter.fromJsonApi(JsonApiStrings.simpleArrayResource, Article.class);
        if (responseAdapter.isSuccess()) {
            List<Article> articles = responseAdapter.getDataList();
            assertNotNull(articles);
            assertTrue(articles.size() == 1);
        }
    }

    @Test
    public void relationships() throws Exception {
        JsonApiResponseAdapter<Article> responseAdapter = _gsonAdapter.fromJsonApi(JsonApiStrings.simpleArrayRelationshipsResource, Article.class);
        if (responseAdapter.isSuccess()) {
            Article article = responseAdapter.getData();
            assertNotNull(article);
            assertTrue(responseAdapter.hasRelationships("author"));
            assertTrue(responseAdapter.hasRelationships("comments"));
            assertTrue(responseAdapter.hasRelationships(article, "author"));
            assertTrue(responseAdapter.hasRelationships(article, "comments"));
        }
    }

    @Test(expected = JsonApiParseException.class)
    public void relationshipsException() {
        JsonApiResponseAdapter<Article> responseAdapter = _gsonAdapter.fromJsonApi(JsonApiStrings.error, Article.class);
        assertTrue(responseAdapter.hasRelationships("author"));
    }

    @Test
    public void included() throws Exception {
        JsonApiResponseAdapter<Article> responseAdapter = _gsonAdapter.fromJsonApi(JsonApiStrings.included, Article.class);
        Article article = responseAdapter.getData();
        assertTrue(responseAdapter.hasRelationships("comments"));
        List<Comment> comments = responseAdapter.getIncludedCollection(article, "comments", Comment.class);
        assertTrue(comments.size() == 2);
        People author = responseAdapter.getIncluded(article, "author", People.class);
        assertNotNull(author);
        assertFalse(responseAdapter.hasRelationships(author, "author"));
        assertTrue(responseAdapter.hasRelationships(comments.get(0), "author"));
        assertTrue(responseAdapter.hasRelationships(comments.get(1), "author"));
    }

    @Test
    public void links() throws Exception {
        JsonApiResponseAdapter<Article> responseAdapter = _gsonAdapter.fromJsonApi(JsonApiStrings.included, Article.class);
        SimpleLinks rootLinks = responseAdapter.getLinks(SimpleLinks.class);
        Article article = responseAdapter.getData();
        SimpleLinks articleLinks = responseAdapter.getDataLinks(article, SimpleLinks.class);
        SimpleLinks relationshipsLinks = responseAdapter.getRelationshipsLinks(article, "author", SimpleLinks.class);

        assertNotNull(rootLinks);
        assertNotNull(articleLinks);
        assertNotNull(relationshipsLinks);

        People author = responseAdapter.getIncluded(article, "author", People.class);
        SimpleLinks includedLinks = responseAdapter.getIncludedLinks(author, SimpleLinks.class);

        assertNotNull(includedLinks);
    }

    @Test
    public void error() throws Exception {
        JsonApiResponseAdapter<Article> responseAdapter = _gsonAdapter.fromJsonApi(JsonApiStrings.error, Article.class);
        if (!responseAdapter.isSuccess()) {
            List<Error> errors = responseAdapter.getErrors();
            assertNotNull(errors);
            assertTrue(errors.size() == 1);
        }
    }

    @Test
    public void retrofitResponse() throws Exception {
        _server.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(JsonApiStrings.simpleSingleResource));

        Response<Article> response = _service.singleResource().execute();
        if (response.isSuccessful()) {
            JsonApiResponseAdapter<Article> responseAdapter = RetrofitJsonApiHelper.getJsonApiAdapterFromResponse(response);
            if (responseAdapter.isSuccess()) {
                Article article = responseAdapter.getData();
                assertNotNull(article);
                assertNotNull(article.getIdentifier());
                assertTrue(article.getIdentifier().getId().equals("1"));
                assertTrue(article.getIdentifier().getType().equals("articles"));
            }
        }
    }

    @Test
    public void retrofitObservable() throws Exception {
        _server.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(JsonApiStrings.simpleSingleResource));

        Observable<Article> response = _service.singleResourceRx();
        response.subscribeOn(Schedulers.io())
                .subscribe(
                        resource -> {
                            JsonApiResponseAdapter<Article> responseAdapter = RetrofitJsonApiHelper.getJsonApiAdapterFromResource(resource);

                            Article article = responseAdapter.getData();
                            assertNotNull(article);
                            assertNotNull(article.getIdentifier());
                            assertTrue(article.getIdentifier().getId().equals("1"));
                            assertTrue(article.getIdentifier().getType().equals("articles"));
                        },
                        throwable -> {

                        }
                );
    }

    @Test
    public void retrofitJson() throws Exception {
        _server.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(JsonStrings.arrayTest));

        Response<List<Person>> response = _service.people().execute();
        if (response.isSuccessful()) {
            List<Person> people = response.body();
            assertTrue(people.size() == 3);
        }
    }

}
