package com.nextmatch.vero.jsonapiadapter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nextmatch.vero.jsonapiadapter.model.Article;
import com.nextmatch.vero.jsonapiadapter.model.Comment;
import com.nextmatch.vero.jsonapiadapter.model.People;
import com.nextmatch.vero.jsonapiadapter.model.SimpleLinks;

import org.junit.Before;
import org.junit.Test;

/**
 * @author vero
 * @since 2016. 12. 1.
 */
public class GsonTest {

    private Gson _context;

    @Before
    public void setUp() throws Exception {
        _context = new GsonBuilder().create();
    }

    @Test
    public void gsonTest() throws Exception {
        System.out.println(_context.toJson(createArticle("test")));
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
