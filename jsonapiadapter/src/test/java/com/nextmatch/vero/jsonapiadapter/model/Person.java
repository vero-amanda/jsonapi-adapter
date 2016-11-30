package com.nextmatch.vero.jsonapiadapter.model;

import com.google.gson.annotations.SerializedName;

/**
 * @author vero
 * @since 2016. 11. 30.
 */
public class Person {

    @SerializedName("first-name")
    public String firstName;
    @SerializedName("last-name")
    public String lastName;

}
