package com.qyh.demo.base.util.bucket;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 因KIT工具箱内容过多,为保证KIT简洁单一,本人常用的工具放置于此类
 * @author q_y_h
 * @date 2020/3/19 15:38
 * create by 2012692013@qq.com
 */
public class QTool {

    /**
     * 编号补0
     * @param num
     * @param length
     * @return
     */
    public static String addZero(Integer num,Integer length){
        String numStr = String.valueOf(num);
        StringBuffer numBuffer = new StringBuffer();
        int numSub = length - numStr.length();
        for (int j = 0; j < numSub; j++) {
            numBuffer.append("0");
        }
        numBuffer.append(num);
        return numBuffer.toString();
    }

    /**
     * 数字类型初始化
     * @param data
     * @param <T>
     * @return
     */
    public static <T> T initMathData(T data) {
        if (data == null)
            return null;
        Class<?> aClass = data.getClass();
        Field[] classDeclaredFields = aClass.getDeclaredFields();

        for (int i = 0; i < classDeclaredFields.length; i++) {
            Field field = classDeclaredFields[i];
            field.setAccessible(true);
            try {
                if (field.get(data) == null) {
                    if (field.getType().getName().equals("java.math.BigDecimal"))
                        field.set(data, BigDecimal.ZERO);
                    if (field.getType().getName().equals("java.lang.Integer"))
                        field.set(data,0);
                    if (field.getType().getName().equals("java.lang.Double"))
                        field.set(data,0d);
                    if (field.getType().getName().equals("java.lang.Float"))
                        field.set(data,0f);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            field.setAccessible(false);
        }
        return data;
    }

    /**
     * 排除性强制初始化
     * @param data
     * @param excludes 属性名
     * @param <T>
     * @return
     */
    public static <T> T initMathData(T data, List<String> excludes) {
        if (data == null)
            return null;
        Class<?> aClass = data.getClass();
        Field[] classDeclaredFields = aClass.getDeclaredFields();

        for (int i = 0; i < classDeclaredFields.length; i++) {
            Field field = classDeclaredFields[i];
            if (excludes.contains(field.getName()))
                continue;
            field.setAccessible(true);
            try {
                if (field.getType().getName().equals("java.math.BigDecimal"))
                    field.set(data,BigDecimal.ZERO);
                if (field.getType().getName().equals("java.lang.Integer"))
                    field.set(data,0);
                if (field.getType().getName().equals("java.lang.Double"))
                    field.set(data,0d);
                if (field.getType().getName().equals("java.lang.Float"))
                    field.set(data,0f);
            } catch (Exception e) {
                e.printStackTrace();
            }
            field.setAccessible(false);
        }
        return data;
    }

    /**
     * 类名转表名
     * @return
     */
    public static String className2TableName(Class<?> clazz){
        String name = clazz.getSimpleName();
        StringBuffer nameBuffer = new StringBuffer();
        for (int i = 0; i < name.length(); i++) {
            char c = name.charAt(i);
            if (Character.isUpperCase(c)&&i!=0) {
                nameBuffer.append("_");
                nameBuffer.append(Character.toLowerCase(c));
            }else
                nameBuffer.append(Character.toLowerCase(c));
        }
        return nameBuffer.toString();
    }

    /**
     * 属性名转表名
     * @param propertyName
     * @return
     */
    public static String propertyName2ColumnName(String propertyName) {
        String name = propertyName;
        StringBuffer nameBuffer = new StringBuffer();
        for (int i = 0; i < name.length(); i++) {
            char c = name.charAt(i);
            if (Character.isUpperCase(c)&&i!=0) {
                nameBuffer.append("_");
                nameBuffer.append(Character.toLowerCase(c));
            }else
                nameBuffer.append(Character.toLowerCase(c));
        }
        return nameBuffer.toString();
    }

    /**
     * 表名转类名
     * @param tableName
     * @return
     */
    public static String columnName2PropertyName(String tableName){
        String[] split = tableName.split("_");
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < split.length; i++) {
            String splitName = split[i];
            if (i != 0) {
                char c = splitName.charAt(0);
                String substring = splitName.substring(1, splitName.length());
                buffer.append(String.valueOf(c).toUpperCase()+substring);
            }else buffer.append(splitName);
        }
        return buffer.toString();
    }

    /**
     * mapper默认查询MAP转义
     * @param map
     * @return
     */
    public static Map<String,Object> tableMap2ClassMap(Map<String,Object> map){
        HashMap<String, Object> hashMap = new HashMap<>();
        for (Map.Entry<String, ? extends Object> entry:
                map.entrySet()) {
            String key = entry.getKey();
            Object o = map.get(key);
            hashMap.put(columnName2PropertyName(key), o);
        }
        return hashMap;
    }
}
