package com.hand.spc.pspc_cpk_analysis.dto;

import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

import java.util.Date;

public class CPKAnalyseReqDTO {


    @ApiModelProperty("租户ID")
    private Long tenantId;//租户ID
    @ApiModelProperty("站点ID")
    private Long siteId;//站点ID
    @ApiModelProperty("实体控制图编码")
    @NotBlank(message = "pspc.error.entitycode.null")
    private String entityCode;//实体控制图编码
    @ApiModelProperty("实体控制图版本")
    @NotBlank(message = "pspc.error.entityversion.null")
    private String entityVersion;//实体控制图版本
    @ApiModelProperty("录入开始时间")
    private Date startDate;//录入开始时间
    @ApiModelProperty("录入结束时间")
    private Date endDate;//录入结束时间

    private Long size;

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

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
}
