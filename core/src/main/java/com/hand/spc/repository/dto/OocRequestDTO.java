package com.hand.spc.repository.dto;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;
import java.util.List;

import com.hand.hap.system.dto.BaseDTO;

public class OocRequestDTO extends BaseDTO{

    @ApiModelProperty("租户ID")
    private Long tenantId;
    @ApiModelProperty("站点ID")
    private Long siteId;
    @ApiModelProperty("控制要素组ID")
    private Long ceGroupId;
    @ApiModelProperty("控制要素ID")
    private Long ceParameterId;
    @ApiModelProperty("实体控制图ID")
    private Long entityId;
    @ApiModelProperty("OOC状态编码")
    private String oocStatus;
    @ApiModelProperty("开始时间")
    private Date creationDateFrom;
    @ApiModelProperty("结束时间")
    private Date creationDateTo;
    @ApiModelProperty("控制图明细类型")
    private String chartDetailType;
    @ApiModelProperty(hidden = true)
    private List<Long> subgroupIdList;//样本分组ID集合


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

    public Long getCeGroupId() {
        return ceGroupId;
    }

    public void setCeGroupId(Long ceGroupId) {
        this.ceGroupId = ceGroupId;
    }

    public Long getCeParameterId() {
        return ceParameterId;
    }

    public void setCeParameterId(Long ceParameterId) {
        this.ceParameterId = ceParameterId;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public String getOocStatus() {
        return oocStatus;
    }

    public void setOocStatus(String oocStatus) {
        this.oocStatus = oocStatus;
    }

    public Date getCreationDateFrom() {
        return creationDateFrom;
    }

    public void setCreationDateFrom(Date creationDateFrom) {
        this.creationDateFrom = creationDateFrom;
    }

    public Date getCreationDateTo() {
        return creationDateTo;
    }

    public void setCreationDateTo(Date creationDateTo) {
        this.creationDateTo = creationDateTo;
    }

    public List<Long> getSubgroupIdList() {
        return subgroupIdList;
    }

    public void setSubgroupIdList(List<Long> subgroupIdList) {
        this.subgroupIdList = subgroupIdList;
    }

    public String getChartDetailType() {
        return chartDetailType;
    }

    public void setChartDetailType(String chartDetailType) {
        this.chartDetailType = chartDetailType;
    }


}
