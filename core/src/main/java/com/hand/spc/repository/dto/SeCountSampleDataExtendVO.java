package com.hand.spc.repository.dto;

import io.swagger.annotations.ApiModelProperty;

/**
 * SE 样本数据拓展表(计数)对象
 */
public class SeCountSampleDataExtendVO {
    @ApiModelProperty(value = "拓展属性")
    private String extendAttribute;
    @ApiModelProperty(value = "拓展值")
    private String extendValue;

    public String getExtendAttribute() {
        return extendAttribute;
    }

    public void setExtendAttribute(String extendAttribute) {
        this.extendAttribute = extendAttribute;
    }

    public String getExtendValue() {
        return extendValue;
    }

    public void setExtendValue(String extendValue) {
        this.extendValue = extendValue;
    }
}
