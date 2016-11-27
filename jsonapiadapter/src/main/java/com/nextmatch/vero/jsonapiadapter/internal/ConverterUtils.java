package com.nextmatch.vero.jsonapiadapter.internal;

import com.google.gson.internal.$Gson$Types;
import com.google.gson.reflect.TypeToken;
import com.nextmatch.vero.jsonapiadapter.annotation.Type;
import com.nextmatch.vero.jsonapiadapter.model.Resource;

import java.util.Collection;

/**
 * @author vero
 * @since 2016. 11. 27.
 */
public class ConverterUtils {

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
        return TypeToken.get($Gson$Types.getCollectionElementType(typeToken.getType(), typeToken.getRawType()));
    }

    /**
     * Resource 객체의 Type 정보를 반환
     * @param resource    Resource 객체
     * @return Type value
     */
    public static String getJsonApiType(Resource resource) {
        return getJsonApiType(resource.getClass());
    }

    /**
     * Resource 객체의 Type 정보를 반환
     * @param classOfResource    Resource class
     * @return Type value
     */
    static String getJsonApiType(Class<? extends Resource> classOfResource) {
        return classOfResource.getAnnotation(Type.class).value();
    }

}
