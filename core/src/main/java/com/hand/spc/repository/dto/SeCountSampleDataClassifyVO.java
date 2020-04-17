package com.hand.spc.repository.dto;

import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;

/**
 * SE 分类项行表(计数)对象
 */
public class SeCountSampleDataClassifyVO {
    @ApiModelProperty("分类项描述")
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
