package com.nextmatch.vero.jsonapiadapter.model;

import com.google.gson.annotations.SerializedName;
import com.nextmatch.vero.jsonapiadapter.internal.ConverterUtils;

/**
 * Resource Type, Id 정보를 저장.
 * Type 은 Annotation 에 정의된 값을 가져와서 설정하도록 생성자와 setType() 은 internal 로 처리.
 * @author vero
 * @since 2016. 11. 27.
 */
public class ResourceIdentifier {

    static ResourceIdentifier create(Resource resource, String id) {
        ResourceIdentifier identifier = new ResourceIdentifier();
        identifier.setType(ConverterUtils.getJsonApiType(resource));
        identifier.setId(id);

        return identifier;
    }

    private ResourceIdentifier() {}

    @SerializedName("type")
    private String _type;

    @SerializedName("id")
    private String _id;

    public String getType() {
        return this._type;
    }

    private void setType(String type) {
        this._type = type;
    }

    public String getId() {
        return this._id;
    }

    public void setId(String id) {
        this._id = id;
    }

    @Override
    public boolean equals(Object obj) {
        if (ResourceIdentifier.class.isInstance(obj)) {
            ResourceIdentifier identifier = ResourceIdentifier.class.cast(obj);

            if ((_id != null && identifier.getId() != null && _id.equals(identifier.getId())) &&
                    _type.equals(identifier.getType()))
                return true;
        }

        return false;
    }
}
