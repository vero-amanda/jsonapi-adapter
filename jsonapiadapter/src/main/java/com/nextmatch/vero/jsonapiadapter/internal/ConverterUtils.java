package com.nextmatch.vero.jsonapiadapter.internal;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.google.gson.internal.$Gson$Types;
import com.google.gson.internal.Excluder;
import com.google.gson.reflect.TypeToken;
import com.nextmatch.vero.jsonapiadapter.annotation.Type;
import com.nextmatch.vero.jsonapiadapter.model.Resource;
import com.nextmatch.vero.jsonapiadapter.model.ResourceProvider;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
    static boolean isResourceProviderAssignable(TypeToken typeToken) {
        if (isCollectionAssignableFrom(typeToken)) {
            return isResourceProviderAssignableFrom(getCollectionElementTypeToken(typeToken));
        } else {
            return isResourceProviderAssignableFrom(typeToken);
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
    private static boolean isResourceProviderAssignableFrom(TypeToken typeToken) {
        return ResourceProvider.class.isAssignableFrom(typeToken.getRawType());
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
    private static String getJsonApiType(Class<? extends Resource> classOfResource) {
        return classOfResource.getAnnotation(Type.class).value();
    }


    /**
     * Object의 Field 정보를 가져온다.
     * @param context      Gson
     * @param typeToken    Object TypeToken
     * @return Field 정보를 가지고있는 BoundField List
     */
    static Map<String, BoundField> getBoundFields(Gson context, TypeToken<?> typeToken) {
        Class<?> clazz = typeToken.getRawType();
        Map<String, BoundField> result = new LinkedHashMap<>();
        if (clazz.isInterface()) {
            return result;
        }

        while (clazz != Object.class) {
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                boolean serialize = excludeField(context, field, true);
                boolean deserialize = excludeField(context, field, false);
                if (!serialize && !deserialize) {
                    continue;
                }
                field.setAccessible(true);
                java.lang.reflect.Type fieldType = $Gson$Types.resolve(typeToken.getType(), clazz, field.getGenericType());
                if (ConverterUtils.isResourceProviderAssignable(TypeToken.get(fieldType))) continue;
                List<String> fieldNames = getFieldNames(context, field);
                BoundField previous = null;
                for (int i = 0; i < fieldNames.size(); ++i) {
                    String name = fieldNames.get(i);
                    BoundField boundField = new BoundField(field, TypeToken.get(fieldType), name);
                    BoundField replaced = result.put(name, boundField);
                    if (previous == null) previous = replaced;
                }
                if (previous != null) {
                    throw new IllegalArgumentException(typeToken.getType()
                            + " declares multiple JSON fields named " + previous.getName());
                }
            }
            typeToken = TypeToken.get($Gson$Types.resolve(typeToken.getType(), clazz, clazz.getGenericSuperclass()));
            clazz = typeToken.getRawType();
        }

        return result;
    }

    /**
     * Parsing 예외 Field 여부를 확인
     * @param context      Gson
     * @param f            Field
     * @param serialize    Serialize : true, Deserialize : false
     * @return Parsing 예외 여부
     */
    private static boolean excludeField(Gson context, Field f, boolean serialize) {
        return excludeField(f, serialize, context.excluder());
    }

    /**
     * Parsing 예외 Field 여부를 확인
     * @param f            Field
     * @param serialize    Serialize : true, Deserialize : false
     * @param excluder     Gson Excluder
     * @return Parsing 예외 여부
     */
    private static boolean excludeField(Field f, boolean serialize, Excluder excluder) {
        return !excluder.excludeClass(f.getType(), serialize) && !excluder.excludeField(f, serialize);
    }

    /**
     * FieldName을 가져온다.
     * SerializedName이 정의되어 있으면 우선
     * @param context    Gson
     * @param f          Field
     * @return field name
     */
    private static List<String> getFieldNames(Gson context, Field f) {
        SerializedName annotation = f.getAnnotation(SerializedName.class);
        if (annotation == null) {
            String name = context.fieldNamingStrategy().translateName(f);
            return Collections.singletonList(name);
        }

        String serializedName = annotation.value();
        String[] alternates = annotation.alternate();
        if (alternates.length == 0) {
            return Collections.singletonList(serializedName);
        }

        List<String> fieldNames = new ArrayList<>(alternates.length + 1);
        fieldNames.add(serializedName);
        Collections.addAll(fieldNames, alternates);
        return fieldNames;
    }

}
