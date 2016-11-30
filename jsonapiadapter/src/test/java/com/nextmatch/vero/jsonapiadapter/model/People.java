package com.nextmatch.vero.jsonapiadapter.model;

import com.google.gson.annotations.SerializedName;
import com.nextmatch.vero.jsonapiadapter.annotation.Type;

/**
 * @author vero
 * @since 2016. 11. 17.
 */
@Type("people")
public class People extends Resource {

    @SerializedName("first-name")
    public String firstName;
    @SerializedName("last-name")
    public String lastName;
    public String twitter;
    public Integer age;
    public String gender;

}
