package com.nextmatch.vero.jsonapiadapter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nextmatch.vero.jsonapiadapter.internal.JsonApiRequestAdapter;
import com.nextmatch.vero.jsonapiadapter.internal.JsonApiResponseAdapter;
import com.nextmatch.vero.jsonapiadapter.internal.JsonApiTypeAdapterFactory;
import com.nextmatch.vero.jsonapiadapter.model.Article;
import com.nextmatch.vero.jsonapiadapter.model.Comment;
import com.nextmatch.vero.jsonapiadapter.model.JsonApiParseException;
import com.nextmatch.vero.jsonapiadapter.model.People;
import com.nextmatch.vero.jsonapiadapter.model.SimpleLinks;
import com.nextmatch.vero.jsonapiadapter.retrofit.JsonApiConverterFactory;
import com.nextmatch.vero.jsonapiadapter.retrofit.RetrofitJsonApiHelper;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * @author vero
 * @since 2016. 11. 27.
 */
public class JsonApiWriterTest {

    private Gson _context;
    private TestService _service;
    private MockWebServer _server;

    @Before
    public void setUp() throws Exception {
        _server = new MockWebServer();
        _server.start();

        _context = new GsonBuilder()
                .registerTypeAdapterFactory(new JsonApiTypeAdapterFactory()).create();

        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
            clientBuilder.addInterceptor(createLoggingIntercepter());

        OkHttpClient httpClient = clientBuilder.build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(_server.url("/").toString())
                .addConverterFactory(JsonApiConverterFactory.create(_context))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(httpClient)
                .build();

        _service = retrofit.create(TestService.class);
    }

    private HttpLoggingInterceptor createLoggingIntercepter() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        return interceptor;
    }

    @Test
    public void nullResource() throws Exception {
        JsonApiRequestAdapter<Article> requestAdapter = new JsonApiRequestAdapter<>(_context);
        assertNotNull(requestAdapter.getJsonApiObject());
    }

    @Test
    public void singleResource() throws Exception {
        Article article = createArticle("single resource");
        article.setLinks(createSimpleLinks("http://google.com"));

        JsonApiRequestAdapter<Article> requestAdapter = new JsonApiRequestAdapter<>(_context, article);
        assertNotNull(requestAdapter.getJsonApiObject());
    }

    @Test
    public void arrayResource() throws Exception {
        List<Article> articles = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            Article article = createArticle("resource " + (i + 1));
            article.setLinks(createSimpleLinks("http://google.com/" + (i + 1)));
            articles.add(article);
        }

        JsonApiRequestAdapter<Article> requestAdapter = new JsonApiRequestAdapter<>(_context, articles);
        assertNotNull(requestAdapter.getJsonApiObject());
    }

    @Test(expected = JsonApiParseException.class)
    public void arrayResourceIncludedException() throws Exception {
        List<Article> articles = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            Article article = createArticle("resource " + (i + 1));
            article.setLinks(createSimpleLinks("http://google.com/" + (i + 1)));
            articles.add(article);
        }

        JsonApiRequestAdapter<Article> requestAdapter = new JsonApiRequestAdapter<>(_context, articles);
        requestAdapter.addRelationship(articles.get(1), "comment", createComments("1"));

        assertNotNull(requestAdapter.getJsonApiObject());
    }

    @Test
    public void arrayResourceIncluded() throws Exception {
        List<Article> articles = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            Article article = createArticle("resource " + (i + 1));
            article.setIdentifier(String.valueOf(i));
            article.setLinks(createSimpleLinks("http://google.com/" + (i + 1)));
            articles.add(article);
        }

        JsonApiRequestAdapter<Article> requestAdapter = new JsonApiRequestAdapter<>(_context, articles);
        requestAdapter.addRelationship(articles.get(1), "comment", createComments("1"));

        List<People> people = new ArrayList<>();
        people.add(createPeople("1"));
        people.add(createPeople("2"));
        people.add(createPeople("3"));

        requestAdapter.addListRelationship(articles.get(2), "guys", people);

        assertNotNull(requestAdapter.getJsonApiObject());
        System.out.println(requestAdapter.getJsonApiObject().toString());
    }

    @Test
    public void retrofitSingleResourceRequest() throws Exception {
        Article article = createArticle("single resource");
        article.setLinks(createSimpleLinks("http://google.com"));

        JsonApiRequestAdapter<Article> requestAdapter = new JsonApiRequestAdapter<>(_context, article);
        assertNotNull(requestAdapter.getJsonApiObject());

        _server.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(JsonApiStrings.simpleSingleResource));

        Response<Article> response = _service.singleResourceRequest(requestAdapter).execute();
        if (response.isSuccessful()) {
            JsonApiResponseAdapter<Article> responseAdapter = RetrofitJsonApiHelper.getJsonApiAdapterFromResponse(response);
            if (responseAdapter.isSuccess()) {
                Article responseArticle = responseAdapter.getData();
                assertNotNull(responseArticle);
                assertNotNull(responseArticle.getIdentifier());
                assertTrue(responseArticle.getIdentifier().getId().equals("1"));
                assertTrue(responseArticle.getIdentifier().getType().equals("articles"));
            }
        }
    }

    private static Article createArticle(String text) {
        Article article = new Article();
        article.title = "title " + text;
        article.body = "body " + text;

        return article;
    }

    private static Comment createComments(String text) {
        Comment comment = new Comment();
        comment.body = "comment " + text;
        comment.setIdentifier(text);

        return comment;
    }

    private static People createPeople(String text) {
        People people = new People();
        people.twitter = "twitter " + text;
        people.setIdentifier(text);

        return people;
    }

    private static SimpleLinks createSimpleLinks(String href) {
        SimpleLinks links = new SimpleLinks();
        links.setSelf(href);

        return links;
    }

}
