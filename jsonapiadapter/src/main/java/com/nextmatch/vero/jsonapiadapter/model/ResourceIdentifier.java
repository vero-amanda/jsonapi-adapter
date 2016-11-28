package com.nextmatch.vero.jsonapiadapter.model;

import com.google.gson.annotations.SerializedName;
import com.nextmatch.vero.jsonapiadapter.internal.ConverterUtils;

/**
 * @author vero
 * @since 2016. 11. 27.
 */
public class ResourceIdentifier {

    public static ResourceIdentifier create(Resource resource, String id) {
        ResourceIdentifier identifier = new ResourceIdentifier();
        identifier.setType(ConverterUtils.getJsonApiType(resource));
        identifier.setId(id);

        return identifier;
    }

    @SerializedName("type")
    private String _type;

    @SerializedName("id")
    private String _id;

    public String getType() {
        return this._type;
    }

    public void setType(String type) {
        this._type = type;
    }

    public String getId() {
        return this._id;
    }

    public void setId(String id) {
        this._id = id;
    }

}
