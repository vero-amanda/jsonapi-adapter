package com.nextmatch.vero.jsonapiadapter.model;

/**
 * Type, Id 를 갖는 JsonObject Data Resource
 * ResourceIdentifier 정보만을 갖는 Data 도 존재할 수 있기 때문에 ResourceIdentifier 객체를 분리, Resource 에 Field 로 추가.
 * Links 정보도 내부와 외부에 추가될 수 있으므로, 내부에서 사용할 Links 객체도 Field 로 추가.
 * 외부에서 사용할 Links 정보는 JsonApiResponseAdapter 에서 Dat Resource Object 를 매개변수로 동적으로 가져오게 구현.
 * @author vero
 * @since 2016. 11. 16.
 */
public class Resource implements ResourceProvider {

    private transient ResourceIdentifier _identifier;

    private transient Links _links;

    public void setIdentifier(String id) {
        getIdentifier().setId(id);
    }

    public void setIdentifier(ResourceIdentifier identifier) {
        this._identifier = identifier;
    }

    public ResourceIdentifier getIdentifier() {
        if (_identifier == null)
            _identifier = ResourceIdentifier.create(this, null);

        return _identifier;
    }

    public void setLinks(Links links) {
        this._links = links;
    }

    public Links getLinks() {
        return this._links;
    }

}
