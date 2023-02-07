package com.qyh.demo.base.util.bucket;

import com.qyh.demo.entity.FacilityInfo;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 因KIT工具箱内容过多,为保证KIT简洁单一,本人常用的工具放置于此类
 * @author qiuyuehao
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

    /**
     * INSERT INTO public.facility_info
     * (id, devc_id, devc_name, rid, lng, lat, road_name, pile_no, sub_facility_type_no, gmt_create, facility_type_no, adcode, facility_angle, bearing, alt, pair, model_file_name, "bearing（偏转角）")
     * VALUES(2, '01_151954333', '高速立杆', '', 113.303635, 23.486862, '', '', 101, 20200000847872, 1, 440114, '上行', NULL, 204.60173, '', 'zhihuidenggan.gltf', -249.46036);
     * @param clazz
     * @return
     * @throws Exception
     */
    public static String getInsertHeader(Class<?> clazz) throws Exception {
        Field[] declaredFields = clazz.getDeclaredFields();
        StringBuilder sb = new StringBuilder();
        String simpleName = clazz.getSimpleName();
        String tableName = className2TableName(clazz);
        sb.append("insert into ").append(tableName).append("(");
        for (int i = 0; i < declaredFields.length; i++) {
            Field field = declaredFields[i];
            String name = field.getName();
            sb.append(name);
        }
        sb.append(")");
        return sb.toString();
    }

    public static void main(String[] args) {
        try {
            String insertHeader = getInsertHeader(FacilityInfo.class);
            System.err.println(insertHeader);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
