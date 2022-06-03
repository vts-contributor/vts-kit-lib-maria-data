package com.viettel.vtskit.maria.utils;

import com.google.gson.Gson;

public class StringUtils {
    private StringUtils(){}
    
    private static final Gson GSON = new Gson();

    public static boolean isNullOrEmpty(String str){
        return str == null || str.isEmpty();
    }

    public static String cvtObjToJsonString(Object object){
        try {
            if(object == null){
                return null;
            }
            return GSON.toJson(object);
        }catch (Exception ex){
            return null;
        }
    }

    public static <T> T cvtJsonToObjectString(String json, Class<T> clazz){
        try {
            return GSON.fromJson(json, clazz);
        }catch (Exception ex){
            return null;
        }
    }

}
