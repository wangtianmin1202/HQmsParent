package com.hand.utils.ObjectUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

public  class CopyObject {
    public static void copyByName(Object src, Object target) {
        if (src == null || target == null) {
            return;
        }
        try {
            Map<String, Field> srcFieldMap= getAssignableFieldsMap(src);
            Map<String, Field> targetFieldMap = getAssignableFieldsMap(target);
            for (String srcFieldName : srcFieldMap.keySet()) {
                Field srcField = srcFieldMap.get(srcFieldName);
                if (srcField == null) {
                    continue;
                }
                // 变量名需要相同
                if (!targetFieldMap.keySet().contains(srcFieldName)) {
                    continue;
                }
                Field targetField = targetFieldMap.get(srcFieldName);
                if (targetField == null) {
                    continue;
                }
                // 类型需要相同
                if (!srcField.getType().equals(targetField.getType())) {
                    continue;
                }

                targetField.set(target,srcField.get(src));
            }
        }catch (Exception e) {
            // 异常
        }
        return ;
    }
    private static Map<String, Field> getAssignableFieldsMap(Object obj) {
        if (obj == null) {
            return new HashMap<String, Field>();
        }
        Map<String, Field> fieldMap = new HashMap<String, Field>();
        for (Field field : obj.getClass().getDeclaredFields()) {
            // 过滤不需要拷贝的属性
            if (Modifier.isStatic(field.getModifiers())
                    || Modifier.isFinal(field.getModifiers())) {
                continue;
            }
            field.setAccessible(true);
            fieldMap.put(field.getName(), field);
        }
        return fieldMap;
    }
}
