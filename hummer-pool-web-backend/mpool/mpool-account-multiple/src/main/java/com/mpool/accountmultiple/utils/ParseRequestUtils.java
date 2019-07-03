package com.mpool.accountmultiple.utils;

import com.mpool.accountmultiple.service.impl.AccountException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;

/**
 * 数据转换
 */

public class ParseRequestUtils {
    private final static Logger log= LoggerFactory.getLogger(ParseRequestUtils.class);

    /**
     * 基本类型和基本类型包装类转换
     * @param object
     * @param field
     * @param arg
     * @return
     */
    private static boolean basicAssign(Object object, Field field, String arg){
        field.setAccessible(true);
        Class className = field.getType();
        String name = field.getType().getTypeName();
        try {
            if(className.equals(java.lang.String.class)) {
                field.set(object, arg);
            }else if (className.equals(java.lang.Integer.class) || name.equals("int")) {
                if (className.equals(java.lang.Integer.class)){
                    field.set(object, Integer.parseInt(arg));
                }else{
                    field.setInt(object, Integer.parseInt(arg));
                }
                return true;
            } else if (className.equals(java.lang.Byte.class) || name.equals("byte")) {
                if (className.equals(java.lang.Byte.class)){
                    field.set(object, Byte.parseByte(arg));
                }else {
                    field.setByte(object, Byte.parseByte(arg));
                }
                return true;
            } else if (className.equals(java.lang.Long.class) || name.equals("long")) {
                if (className.equals(java.lang.Long.class)){
                    field.set(object, Long.parseLong(arg));
                }else{
                    field.setLong(object, Long.parseLong(arg));
                }
                return true;
            } else if (className.equals(java.lang.Double.class) || name.equals("double")) {
                if (className.equals(java.lang.Double.class)){
                    field.set(object, Double.parseDouble(arg));
                }else{
                    field.setDouble(object, Double.parseDouble(arg));
                }
                return true;
            } else if (className.equals(java.lang.Float.class) || name.equals("float")) {
                if (className.equals(java.lang.Float.class)){
                    field.set(object, Float.parseFloat(arg));
                }else{
                    field.setFloat(object, Float.parseFloat(arg));
                }
                return true;
            }else if(className.equals(java.lang.Character.class)){
                return true;
            }
            else if (className.equals(java.lang.Short.class) || name.equals("short")) {
                if (className.equals(java.lang.Short.class)){
                    field.set(object, Short.parseShort(arg));
                }else{
                    field.setShort(object, Short.parseShort(arg));
                }
                return true;
            } else if (className.equals(java.lang.Boolean.class) || name.equals("boolean")) {
                if (className.equals(java.lang.Boolean.class)){
                    field.set(object, Boolean.parseBoolean(arg));
                }else{
                    field.setBoolean(object, Boolean.parseBoolean(arg));
                }
                return true;
            }
        }catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 对象转换
     * @param t
     * @param <T>
     * @return
     */
    public static <T extends Object> T getObjectValue(Class<T> t){
        T tt = null;
        try {
            tt = t.newInstance();
            HttpServletRequest request = ((ServletRequestAttributes)(RequestContextHolder.currentRequestAttributes())).getRequest();

            Field[] fields = tt.getClass().getDeclaredFields();
            for(Field field : fields){
                String value = (String) request.getAttribute(field.getName());
                if(value != null){
                    basicAssign(tt, field, value);
                }
            }
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return tt;
    }

    /**
     * 获得指定key对应的值
     * @param key
     * @return
     */
    public static String getStringByKey(String key){
        HttpServletRequest request = ((ServletRequestAttributes)(RequestContextHolder.currentRequestAttributes())).getRequest();
        return (String)request.getAttribute(key);
    }

    public static Long getLongValueByKey(String key){
        String value = getStringByKey(key);
        if(value == null){
            return null;
        }

        return Long.parseLong(value);
    }

    public static Integer getIntegerValueByKey(String key){
        String value = getStringByKey(key);
        if(value == null){
            return null;
        }

        return Integer.parseInt(value);
    }

    /**
     * 解析字符串数组
     * @param key
     * @return
     */
    public static String[] getGenericConverterByKey(String key){
        String value = getStringByKey(key);
        if(value == null){
            log.error("multiple url generic converter error, key is null! key = {}", key);
            throw new AccountException("dataparse.error", "数据解析错误");
        }

        String[] subValue = value.split(",");

        return subValue;
    }

    public static Long[] getGenericConverterLong(String key){
        String[] subValue = getGenericConverterByKey(key);

        Long[] tmpValue = new Long[subValue.length];
        for(int i = 0; i < subValue.length; i++) {
            tmpValue[i] = Long.parseLong(subValue[i]);
        }
        return tmpValue;
    }
}
