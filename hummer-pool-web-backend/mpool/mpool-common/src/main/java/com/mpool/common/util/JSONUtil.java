package com.mpool.common.util;

import java.io.IOException;
import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JSONUtil
{
    private static final ObjectMapper mapper = new ObjectMapper();

    private static final Logger log = LoggerFactory.getLogger(JSONUtil.class);

    public static String toJson(Object obj)
    {
        if (obj == null) {
            return null;
        }
        try {
            return mapper.writeValueAsString(obj);
        } catch (IOException e) {
            log.error(String.format("obj=[%s]", obj.toString()), e);
        }
        return null;
    }
    public static <T> T fromJson(String json, Class<T> clazz)
    {
        if (json == null) {
            return null;
        }
        try {
            return mapper.readValue(json, clazz);
        } catch (IOException e) {
            log.error(String.format("json=[%s]", json), e);
        }
        return null;
    }
    public static <T> Collection<T> fromJson(String json,
            Class<? extends Collection<T>> collectionClazz, Class<T> clazz)
    {
        if (json == null) {
            return null;
        }
        try {
            Collection<T> collection = mapper.readValue(json,
                    getCollectionType(collectionClazz, clazz));
            return collection;
        } catch (IOException e) {
            log.error(String.format("json=[%s]", json), e);
        }
        return null;
    }

    private static JavaType getCollectionType(Class<?> collectionClass,
            Class<?>... elementClasses)
    {
        return mapper.getTypeFactory().constructParametricType(collectionClass,
                elementClasses);
    }
}
