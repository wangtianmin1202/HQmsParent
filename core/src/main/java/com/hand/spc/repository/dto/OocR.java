package com.hand.spc.repository.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.hand.hap.system.dto.BaseDTO;

import io.swagger.annotations.ApiModel;

@ApiModel("OocR")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Table(name = "pspc_ooc")
public class OocR extends BaseDTO {
	@Id
    private String oocId;//ooc id
    private Long tenantId;//租户ID
    private Long siteId;//站点id
    private String oocStatus;//OOC状态
    private Long sampleSubgroupId;//样本数据分组ID
    private String entityCode;//实体控制图编码
    private String entityVersion;//实体控制图版本
    private Long maxPlotPoints;//最大绘点数
    private String tickLabelX;//X轴刻度
    private String axisLabelX;//X轴标签
    private String axisLabelY;//Y轴标签
    private String chartDetailType;//控制图明细类型
    private BigDecimal upperControlLimit;//控制上限
    private BigDecimal centerLine;//中心线
    private BigDecimal lowerControlLimit;//控制下限
    private BigDecimal upperSpecLimit;//规格上限
    private BigDecimal specTarget;//目标值
    private BigDecimal lowerSpecLimit;//规格下限
    private Long judgementId;//判异规则ID
    private Long firstSubgroupNum;//开始样本数据分组组号
    private Long lastSubgroupNum;//结束样本数据分组组号
    private Long classifyGroupId;//分类组ID
    private Long classifyId;//分类项ID
    private String remark;//备注
    @Transient
    private MessageR message;//消息
    @Transient
    private List<MessageDetailR> messageDetailList;//消息行集合
    @Transient
    private List<MessageUploadRelR> messageUploadRelList;//消息命令集合

    public OocR() {
    }

    public OocR(Long tenantId, Long siteId, Long sampleSubgroupId, String entityCode, String entityVersion, String chartDetailType, Long judgementId) {
        this.tenantId = tenantId;
        this.siteId = siteId;
        this.sampleSubgroupId = sampleSubgroupId;
        this.entityCode = entityCode;
        this.entityVersion = entityVersion;
        this.chartDetailType = chartDetailType;
        this.judgementId = judgementId;
    }

    public String getOocId() {
        return oocId;
    }

    public void setOocId(String oocId) {
        this.oocId = oocId;
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

    public String getOocStatus() {
        return oocStatus;
    }

    public void setOocStatus(String oocStatus) {
        this.oocStatus = oocStatus;
    }

    public Long getSampleSubgroupId() {
        return sampleSubgroupId;
    }

    public void setSampleSubgroupId(Long sampleSubgroupId) {
        this.sampleSubgroupId = sampleSubgroupId;
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

    public Long getMaxPlotPoints() {
        return maxPlotPoints;
    }

    public void setMaxPlotPoints(Long maxPlotPoints) {
        this.maxPlotPoints = maxPlotPoints;
    }
    
    public String getTickLabelX() {
		return tickLabelX;
	}

	public void setTickLabelX(String tickLabelX) {
		this.tickLabelX = tickLabelX;
	}

	public String getAxisLabelX() {
		return axisLabelX;
	}

	public void setAxisLabelX(String axisLabelX) {
		this.axisLabelX = axisLabelX;
	}

	public String getAxisLabelY() {
		return axisLabelY;
	}

	public void setAxisLabelY(String axisLabelY) {
		this.axisLabelY = axisLabelY;
	}

	public String getChartDetailType() {
        return chartDetailType;
    }

    public void setChartDetailType(String chartDetailType) {
        this.chartDetailType = chartDetailType;
    }

    public BigDecimal getUpperControlLimit() {
        return upperControlLimit;
    }

    public void setUpperControlLimit(BigDecimal upperControlLimit) {
        this.upperControlLimit = upperControlLimit;
    }

    public BigDecimal getCenterLine() {
        return centerLine;
    }

    public void setCenterLine(BigDecimal centerLine) {
        this.centerLine = centerLine;
    }

    public BigDecimal getLowerControlLimit() {
        return lowerControlLimit;
    }

    public void setLowerControlLimit(BigDecimal lowerControlLimit) {
        this.lowerControlLimit = lowerControlLimit;
    }

    public BigDecimal getUpperSpecLimit() {
        return upperSpecLimit;
    }

    public void setUpperSpecLimit(BigDecimal upperSpecLimit) {
        this.upperSpecLimit = upperSpecLimit;
    }

    public BigDecimal getSpecTarget() {
        return specTarget;
    }

    public void setSpecTarget(BigDecimal specTarget) {
        this.specTarget = specTarget;
    }

    public BigDecimal getLowerSpecLimit() {
        return lowerSpecLimit;
    }

    public void setLowerSpecLimit(BigDecimal lowerSpecLimit) {
        this.lowerSpecLimit = lowerSpecLimit;
    }

    public Long getJudgementId() {
        return judgementId;
    }

    public void setJudgementId(Long judgementId) {
        this.judgementId = judgementId;
    }

    public Long getFirstSubgroupNum() {
        return firstSubgroupNum;
    }

    public void setFirstSubgroupNum(Long firstSubgroupNum) {
        this.firstSubgroupNum = firstSubgroupNum;
    }

    public Long getLastSubgroupNum() {
        return lastSubgroupNum;
    }

    public void setLastSubgroupNum(Long lastSubgroupNum) {
        this.lastSubgroupNum = lastSubgroupNum;
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

    public MessageR getMessage() {
        return message;
    }

    public void setMessage(MessageR message) {
        this.message = message;
    }

    public List<MessageDetailR> getMessageDetailList() {
        return messageDetailList;
    }

    public void setMessageDetailList(List<MessageDetailR> messageDetailList) {
        this.messageDetailList = messageDetailList;
    }

    public List<MessageUploadRelR> getMessageUploadRelList() {
        return messageUploadRelList;
    }

    public void setMessageUploadRelList(List<MessageUploadRelR> messageUploadRelList) {
        this.messageUploadRelList = messageUploadRelList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OocR ooc = (OocR) o;
        return tenantId.equals(ooc.tenantId) &&
                siteId.equals(ooc.siteId) &&
                sampleSubgroupId.equals(ooc.sampleSubgroupId) &&
                entityCode.equals(ooc.entityCode) &&
                entityVersion.equals(ooc.entityVersion) &&
                chartDetailType.equals(ooc.chartDetailType) &&
                judgementId.equals(ooc.judgementId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tenantId, siteId, sampleSubgroupId, entityCode, entityVersion, chartDetailType, judgementId);
    }
}
