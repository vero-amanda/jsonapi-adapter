package com.nextmatch.vero.jsonapiadapter.model;

import com.google.gson.annotations.SerializedName;
import com.nextmatch.vero.jsonapiadapter.internal.ConverterUtils;

/**
 * @author vero
 * @since 2016. 11. 27.
 */
public class ResourceIdentifier {

    public static ResourceIdentifier create(Resource resource, Integer id) {
        ResourceIdentifier identifier = new ResourceIdentifier();
        identifier.setType(ConverterUtils.getJsonApiType(resource));
        identifier.setId(id);

        return identifier;
    }

    @SerializedName("type")
    private String _type;

    @SerializedName("id")
    private Integer _id;

    public String getType() {
        return this._type;
    }

    public void setType(String type) {
        this._type = type;
    }

    public Integer getId() {
        return this._id;
    }

    public void setId(Integer id) {
        this._id = id;
    }

}
