package com.nextmatch.vero.jsonapiadapter.model;

import com.nextmatch.vero.jsonapiadapter.internal.ResourceProvider;

/**
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
