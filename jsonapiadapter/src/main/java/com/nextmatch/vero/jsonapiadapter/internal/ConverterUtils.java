package com.nextmatch.vero.jsonapiadapter.internal;

import com.google.gson.internal.$Gson$Types;
import com.google.gson.reflect.TypeToken;
import com.nextmatch.vero.jsonapiadapter.model.Resource;

import java.lang.reflect.Type;
import java.util.Collection;

/**
 * @author vero
 * @since 2016. 11. 27.
 */
class ConverterUtils {

    /**
     * ResourceTypeAdapter를 사용할 TypeToken인지 확인.
     * Collection인 경우 ElementType를 확인.
     * @param typeToken    TypeToken from Gson
     * @return Resource를 상속받았는지 확인
     */
    static boolean isResourceAssignable(TypeToken typeToken) {
        if (isCollectionAssignableFrom(typeToken)) {
            return isResourceAssignableFrom(getCollectionElementTypeToken(typeToken));
        } else {
            return isResourceAssignableFrom(typeToken);
        }
    }

    /**
     * Collection 여부를 확인
     * @param typeToken    TypeToken from Gson
     * @return Collection 상속 여부
     */
    private static boolean isCollectionAssignableFrom(TypeToken typeToken) {
        return Collection.class.isAssignableFrom(typeToken.getRawType());
    }

    /**
     * Resource 여부를 확인
     * @param typeToken    TypeToken from Gson
     * @return Resource 상속 여부
     */
    private static boolean isResourceAssignableFrom(TypeToken typeToken) {
        return Resource.class.isAssignableFrom(typeToken.getRawType());
    }

    /**
     * Collection ElementType을 반환
     * @param typeToken    Collection TypeToken from Gson
     * @return Element TypeToken
     */
    private static TypeToken getCollectionElementTypeToken(TypeToken typeToken) {
        return TypeToken.get(getCollectionElementType(typeToken));
    }

    /**
     * Collection ElementType을 반환
     * @param typeToken    Collection TypeToken from Gson
     * @return Element Type
     */
    private static Type getCollectionElementType(TypeToken typeToken) {
        return $Gson$Types.getCollectionElementType(typeToken.getType(), typeToken.getRawType());
    }

}
