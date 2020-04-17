package com.hand.spc.repository.dto;

import java.util.Date;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.hand.hap.system.dto.BaseDTO;

import io.swagger.annotations.ApiModel;

@ApiModel("样本数据分组")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Table(name = "pspc_sample_subgroup")
public class SampleSubgroupR extends BaseDTO {

    @Id
    private Long sampleSubgroupId;//样本数据分组ID
    private Long tenantId;//租户ID
    private Long siteId;//站点ID
    private Long subgroupNum;//组号
    private String entityCode;//实体控制图编码
    private String entityVersion;//实体控制图版本
    private Long subgroupSize;//分组大小
    private Long existNum;//已分组数
    private Date sampleSubgroupTime;//样本子组时间
    private String sampleCalculateStatus;//计算状态
    private String sampleModified;//是否修改

    @Transient
    private Double mainStatisticValue;//主图统计量值
    @Transient
    private Double secondStatisticValue;//次图统计量值
    @Transient
    private Double subgroupBar;//平均值  （子组计算时移动极差需要用到）
    @Transient
    private String sampleDatas;//样本数据，样本时间+"/"+样本值 以逗号拼接

    public Long getSampleSubgroupId() {
        return sampleSubgroupId;
    }

    public void setSampleSubgroupId(Long sampleSubgroupId) {
        this.sampleSubgroupId = sampleSubgroupId;
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

    public Long getSubgroupNum() {
        return subgroupNum;
    }

    public void setSubgroupNum(Long subgroupNum) {
        this.subgroupNum = subgroupNum;
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

    public Long getSubgroupSize() {
        return subgroupSize;
    }

    public void setSubgroupSize(Long subgroupSize) {
        this.subgroupSize = subgroupSize;
    }

    public Long getExistNum() {
        return existNum;
    }

    public void setExistNum(Long existNum) {
        this.existNum = existNum;
    }

    public Date getSampleSubgroupTime() {
        return sampleSubgroupTime;
    }

    public void setSampleSubgroupTime(Date sampleSubgroupTime) {
        this.sampleSubgroupTime = sampleSubgroupTime;
    }

    public String getSampleCalculateStatus() {
        return sampleCalculateStatus;
    }

    public void setSampleCalculateStatus(String sampleCalculateStatus) {
        this.sampleCalculateStatus = sampleCalculateStatus;
    }

    public String getSampleModified() {
        return sampleModified;
    }

    public void setSampleModified(String sampleModified) {
        this.sampleModified = sampleModified;
    }

    public Double getMainStatisticValue() {
        return mainStatisticValue;
    }

    public void setMainStatisticValue(Double mainStatisticValue) {
        this.mainStatisticValue = mainStatisticValue;
    }

    public Double getSecondStatisticValue() {
        return secondStatisticValue;
    }

    public void setSecondStatisticValue(Double secondStatisticValue) {
        this.secondStatisticValue = secondStatisticValue;
    }

    public Double getSubgroupBar() {
        return subgroupBar;
    }

    public void setSubgroupBar(Double subgroupBar) {
        this.subgroupBar = subgroupBar;
    }

    public String getSampleDatas() {
        return sampleDatas;
    }

    public void setSampleDatas(String sampleDatas) {
        this.sampleDatas = sampleDatas;
    }
}
