package com.nextmatch.vero.jsonapiadapter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nextmatch.vero.jsonapiadapter.internal.JsonApiRequestAdapter;
import com.nextmatch.vero.jsonapiadapter.internal.JsonApiTypeAdapterFactory;
import com.nextmatch.vero.jsonapiadapter.model.Article;
import com.nextmatch.vero.jsonapiadapter.model.Comment;
import com.nextmatch.vero.jsonapiadapter.model.JsonApiParseException;
import com.nextmatch.vero.jsonapiadapter.model.People;
import com.nextmatch.vero.jsonapiadapter.model.SimpleLinks;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

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
