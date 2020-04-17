package com.hand.spc.repository.dto;

import java.util.Date;

import org.hibernate.validator.constraints.NotBlank;

import com.hand.hap.system.dto.BaseDTO;

import io.swagger.annotations.ApiModelProperty;

/**
 * SE查询对象
 */
public class SeRequestDTO extends BaseDTO{

    @ApiModelProperty(value = "租户ID")
    private Long tenantId;
    @ApiModelProperty(value = "站点ID")
    private Long siteId;
    @ApiModelProperty(value = "实体控制图编码")
    @NotBlank(message = "pspc.error.entitycode.null")
    private String entityCode;
    @ApiModelProperty(value = "实体控制图版本")
    @NotBlank(message = "pspc.error.entityversion.null")
    private String entityVersion;
    @ApiModelProperty(value = "录入开始时间")
    private Date startDate;
    @ApiModelProperty(value = "录入结束时间")
    private Date endDate;
    @ApiModelProperty(value = "OOC ID")
    private String oocId;//OOC ID(包括计量&计数)
    @ApiModelProperty(value = "最小组号", hidden = true)
    private Long minSubgroupNum;
    @ApiModelProperty(value = "最大组号", hidden = true)
    private Long maxSubgroupNum;

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public Long getSiteId() {
        return siteId;
    }

    public void setSiteId(Long siteId) {
        this.siteId = siteId;
    }

    public String getEntityCode() {
        return entityCode;
    }

    public void setEntityCode(String entityCode) {
        this.entityCode = entityCode;
    }

    public String getEntityVersion() {
        return entityVersion;
    }

    public void setEntityVersion(String entityVersion) {
        this.entityVersion = entityVersion;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getOocId() {
        return oocId;
    }

    public void setOocId(String oocId) {
        this.oocId = oocId;
    }

    public Long getMinSubgroupNum() {
        return minSubgroupNum;
    }

    public void setMinSubgroupNum(Long minSubgroupNum) {
        this.minSubgroupNum = minSubgroupNum;
    }

    public Long getMaxSubgroupNum() {
        return maxSubgroupNum;
    }

    public void setMaxSubgroupNum(Long maxSubgroupNum) {
        this.maxSubgroupNum = maxSubgroupNum;
    }

}
