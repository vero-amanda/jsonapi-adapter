package com.nextmatch.vero.jsonapiadapter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nextmatch.vero.jsonapiadapter.internal.GsonAdapter;
import com.nextmatch.vero.jsonapiadapter.internal.JsonApiResponseAdapter;
import com.nextmatch.vero.jsonapiadapter.internal.JsonApiTypeAdapterFactory;
import com.nextmatch.vero.jsonapiadapter.model.Article;
import com.nextmatch.vero.jsonapiadapter.model.Error;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * @author vero
 * @since 2016. 11. 27.
 */
public class JsonApiReaderTest {

    private GsonAdapter _gsonAdapter;

    @Before
    public void setUp() throws Exception {
        Gson gson = new GsonBuilder()
                .registerTypeAdapterFactory(new JsonApiTypeAdapterFactory()).create();

        _gsonAdapter = new GsonAdapter(gson);
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
        }
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

}
