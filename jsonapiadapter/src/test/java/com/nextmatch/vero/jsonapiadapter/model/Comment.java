package com.nextmatch.vero.jsonapiadapter.model;

import com.nextmatch.vero.jsonapiadapter.annotation.Type;

/**
 * @author vero
 * @since 2016. 11. 17.
 */
@Type("comment")
public class Comment extends Resource {

    public String body;

}
