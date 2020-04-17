package com.hand.wfl.util;

import org.activiti.rest.service.api.engine.variable.RestVariable;
import org.apache.commons.lang3.StringUtils;

/**
 * description 工作流用到的相关工具
 *
 * @author KOCDZX0 2020/03/25 10:47 AM
 */
public class ActivitiUtil {

    /**
     * 获取RestVariable
     *
     * @param name
     * @param value
     * @return
     */
    public static RestVariable getRestVariable(String name, Object value) {
        return getRestVariable(name, null, value);
    }

    /**
     * type 默认是 string
     *
     * @param name
     * @param type 默认是 string
     * @param value
     * @return
     */
    public static RestVariable getRestVariable(String name, String type, Object value) {
        RestVariable restVariableType = new RestVariable();
        restVariableType.setName(name);
        if (StringUtils.isBlank(type)) {
            restVariableType.setType("string");
        } else {
            restVariableType.setType(type);
        }
        restVariableType.setValue(value);
        return restVariableType;
    }
}
