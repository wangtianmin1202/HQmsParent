package com.hand.spc.repository.dto;

import java.math.BigDecimal;

import io.swagger.annotations.ApiModelProperty;

/**
 * 柏拉图 分类项行表对象(计数)
 */
public class PlatoCountSampleDataClassifyVO {
    @ApiModelProperty(value = "分类项描述")
    private String classifyDesc;
    @ApiModelProperty(value = "分类项值")
    private BigDecimal classifyCountValue;

    public String getClassifyDesc() {
        return classifyDesc;
    }

    public void setClassifyDesc(String classifyDesc) {
        this.classifyDesc = classifyDesc;
    }

    public BigDecimal getClassifyCountValue() {
        return classifyCountValue;
    }

    public void setClassifyCountValue(BigDecimal classifyCountValue) {
        this.classifyCountValue = classifyCountValue;
    }
}
