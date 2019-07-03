package com.mpool.common.util;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.mpool.common.util.gson.MapDeserializerDoubleAsIntFix;

import java.util.Map;

/**
 * @Author: stephen
 * @Date: 2019/5/6 19:50
 */
public class GsonUtil {
    private static volatile Gson gson = null;

    public static Gson getGson(){
        if(gson == null){
            synchronized (GsonUtil.class){
                if(gson == null){
                    GsonBuilder gsonBuilder = new GsonBuilder();
                    gsonBuilder.registerTypeAdapter(new TypeToken<Map<String, Object>>(){}.getType(),  new MapDeserializerDoubleAsIntFix());
                    gson = gsonBuilder.create();
                }
            }
        }

        return gson;
    }
}
