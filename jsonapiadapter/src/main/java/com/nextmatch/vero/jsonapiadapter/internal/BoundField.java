package com.nextmatch.vero.jsonapiadapter.internal;

import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Field;

/**
 * Reflection 을 사용해 Resource instance 를 생성할 경우 instance 의 Field 정보를 저장
 * @author vero
 * @since 2016. 11. 23.
 */
class BoundField {

    private final Field _field;
    private final TypeToken _typeToken;

    private final String _name;

    BoundField(Field field, TypeToken typeToken, String name) {
        this._field = field;
        this._typeToken = typeToken;
        this._name = name;
    }

    Field getField() {
        return this._field;
    }

    TypeToken getTypeToken() {
        return this._typeToken;
    }

    String getName() {
        return this._name;
    }

}
