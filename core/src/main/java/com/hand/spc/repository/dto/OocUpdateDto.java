package com.hand.spc.repository.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.hand.hap.system.dto.BaseDTO;

import io.swagger.annotations.ApiModelProperty;
import java.util.Date;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class OocUpdateDto extends BaseDTO{
    @ApiModelProperty("OOC ID")
    private String oocId;
    @ApiModelProperty("计数型CountOocID")
    private String countOocId;
    @ApiModelProperty("原因分类组ID")
    private Long classifyGroupId;
    @ApiModelProperty("原因分类ID")
    private Long classifyId;
    @ApiModelProperty("备注")
    private String remark;
    @ApiModelProperty("行版本号")
    private Long objectVersionNumber;

    public String getOocId() {
        return oocId;
    }

    public void setOocId(String oocId) {
        this.oocId = oocId;
    }

    public String getCountOocId() {
        return countOocId;
    }

    public void setCountOocId(String countOocId) {
        this.countOocId = countOocId;
    }

    public Long getClassifyGroupId() {
        return classifyGroupId;
    }

    public void setClassifyGroupId(Long classifyGroupId) {
        this.classifyGroupId = classifyGroupId;
    }

    public Long getClassifyId() {
        return classifyId;
    }

    public void setClassifyId(Long classifyId) {
        this.classifyId = classifyId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Long getObjectVersionNumber() {
        return objectVersionNumber;
    }

    public void setObjectVersionNumber(Long objectVersionNumber) {
        this.objectVersionNumber = objectVersionNumber;
    }
}
