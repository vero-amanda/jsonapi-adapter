package com.nextmatch.vero.jsonapiadapter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nextmatch.vero.jsonapiadapter.internal.JsonApiRequestAdapter;
import com.nextmatch.vero.jsonapiadapter.internal.JsonApiTypeAdapterFactory;
import com.nextmatch.vero.jsonapiadapter.model.Article;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

/**
 * @author vero
 * @since 2016. 11. 27.
 */
public class JsonApiWriterTest {

    private Gson _context;

    @Before
    public void setUp() throws Exception {
        _context = new GsonBuilder()
                .registerTypeAdapterFactory(new JsonApiTypeAdapterFactory()).create();
    }

    @Test
    public void singleResource() throws Exception {
        Article article = createArticle("single resource");
        JsonApiRequestAdapter<Article> requestAdapter = new JsonApiRequestAdapter<>(_context, article);
        assertNotNull(requestAdapter);
    }

    private static Article createArticle(String text) {
        Article article = new Article();
        article.title = "title " + text;
        article.body = "body " + text;

        return article;
    }

}
