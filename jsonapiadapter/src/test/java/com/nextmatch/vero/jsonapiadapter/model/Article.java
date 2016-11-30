package com.nextmatch.vero.jsonapiadapter.model;

import com.nextmatch.vero.jsonapiadapter.annotation.Type;

import java.util.Date;

/**
 * @author vero
 * @since 2016. 11. 17.
 */
@Type("articles")
public class Article extends Resource {

    public String title;
    public String body;
    public Date created;
    public Date updated;

}
