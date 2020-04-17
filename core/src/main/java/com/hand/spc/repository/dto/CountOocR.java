package com.hand.spc.repository.dto;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.hand.hap.mybatis.annotation.ExtensionAttribute;
import com.hand.hap.system.dto.BaseDTO;

//import io.choerodon.mybatis.domain.AuditDomain;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * OOC(计数)
 *
 * @author peng.hu04@hand-china.com 2019-06-20 11:01:40
 */
@ExtensionAttribute(disable = true)
@Table(name = "pspc_count_ooc")
public class CountOocR extends BaseDTO {

	//
	// 业务方法(按public protected private顺序排列)
	// ------------------------------------------------------------------------------

	//
	// 数据库字段
	// ------------------------------------------------------------------------------

	@Id
	@GeneratedValue
	private String countOocId;
	@ApiModelProperty(value = "租户ID", required = true)
	@NotNull
	private Long tenantId;
	@ApiModelProperty(value = "站点ID", required = true)
	@NotNull
	private Long siteId;
	@ApiModelProperty(value = "状态", required = true)
	@NotNull
	private String oocStatus;
	@ApiModelProperty(value = "SPC实体控制图", required = true)
	@NotNull
	private String entityCode;
	@ApiModelProperty(value = "实体控制图版本")
	private String entityVersion;
	@ApiModelProperty(value = "绘点数")
	private Long maxPlotPoints;
	@ApiModelProperty(value = "X轴刻度")
	private String tickLabelX;
	@ApiModelProperty(value = "X轴标签")
	private String axisLabelX;
	@ApiModelProperty(value = "Y轴标签")
	private String axisLabelY;
	@ApiModelProperty(value = "主图/次图")
	private String chartDetailType;
	@ApiModelProperty(value = "控制上限")
	private BigDecimal upperControlLimit;
	@ApiModelProperty(value = "中心线")
	private BigDecimal centerLine;
	@ApiModelProperty(value = "控制下限")
	private BigDecimal lowerControlLimit;
	@ApiModelProperty(value = "规格上线")
	private BigDecimal upperSpecLimit;
	@ApiModelProperty(value = "目标值")
	private BigDecimal specTarget;
	@ApiModelProperty(value = "规格下线")
	private BigDecimal lowerSpecLimit;
	@ApiModelProperty(value = "规则行ID")
	private Long judgementId;
	@ApiModelProperty(value = "开始组号")
	private Long firstSubgroupNum;
	@ApiModelProperty(value = "结束组号")
	private Long lastSubgroupNum;
	@ApiModelProperty(value = "分类组ID")
	private Long classifyGroupId;
	@ApiModelProperty(value = "分类项ID")
	private Long classifyId;
	@ApiModelProperty(value = "备注")
	private String remark;
	@ApiModelProperty(value = "样本数据ID")
	private Long countSampleDataId;

	//
	// 非数据库字段
	// ------------------------------------------------------------------------------
	@Transient
	private MessageR message;// 消息
	@Transient
	private List<MessageDetailR> messageDetailList;// 消息行集合
	@Transient
	private List<MessageUploadRelR> messageUploadRelList;// 消息命令集合

//
	// getter/setter
	// ------------------------------------------------------------------------------

	/**
	 * @return 表ID，主键
	 */
	public String getCountOocId() {
		return countOocId;
	}

	public void setCountOocId(String countOocId) {
		this.countOocId = countOocId;
	}

	/**
	 * @return 租户ID
	 */
	public Long getTenantId() {
		return tenantId;
	}

	public void setTenantId(Long tenantId) {
		this.tenantId = tenantId;
	}

	/**
	 * @return 站点ID
	 */
	public Long getSiteId() {
		return siteId;
	}

	public void setSiteId(Long siteId) {
		this.siteId = siteId;
	}

	/**
	 * @return 状态
	 */
	public String getOocStatus() {
		return oocStatus;
	}

	public void setOocStatus(String oocStatus) {
		this.oocStatus = oocStatus;
	}

	/**
	 * @return SPC实体控制图
	 */
	public String getEntityCode() {
		return entityCode;
	}

	public void setEntityCode(String entityCode) {
		this.entityCode = entityCode;
	}

	/**
	 * @return 实体控制图版本
	 */
	public String getEntityVersion() {
		return entityVersion;
	}

	public void setEntityVersion(String entityVersion) {
		this.entityVersion = entityVersion;
	}

	/**
	 * @return 绘点数
	 */
	public Long getMaxPlotPoints() {
		return maxPlotPoints;
	}

	public void setMaxPlotPoints(Long maxPlotPoints) {
		this.maxPlotPoints = maxPlotPoints;
	}

	/**
	 * @return 主图/次图
	 */
	public String getChartDetailType() {
		return chartDetailType;
	}

	/**
	 * @return X轴刻度
	 */
	public String getTickLabelX() {
		return tickLabelX;
	}

	public void setTickLabelX(String tickLabelx) {
		this.tickLabelX = tickLabelx;
	}

	/**
	 * @return X轴标签
	 */

	public String getAxisLabelX() {
		return axisLabelX;
	}

	public void setAxisLabelX(String axisLabelx) {
		this.axisLabelX = axisLabelx;
	}

	/**
	 * @return Y轴标签
	 */

	public String getAxisLabelY() {
		return axisLabelY;
	}

	public void setAxisLabelY(String axisLabely) {
		this.axisLabelY = axisLabely;
	}

	public void setChartDetailType(String chartDetailType) {
		this.chartDetailType = chartDetailType;
	}

	/**
	 * @return 控制上限
	 */
	public BigDecimal getUpperControlLimit() {
		return upperControlLimit;
	}

	public void setUpperControlLimit(BigDecimal upperControlLimit) {
		this.upperControlLimit = upperControlLimit;
	}

	/**
	 * @return 中心线
	 */
	public BigDecimal getCenterLine() {
		return centerLine;
	}

	public void setCenterLine(BigDecimal centerLine) {
		this.centerLine = centerLine;
	}

	/**
	 * @return 控制下限
	 */
	public BigDecimal getLowerControlLimit() {
		return lowerControlLimit;
	}

	public void setLowerControlLimit(BigDecimal lowerControlLimit) {
		this.lowerControlLimit = lowerControlLimit;
	}

	/**
	 * @return 规格上线
	 */
	public BigDecimal getUpperSpecLimit() {
		return upperSpecLimit;
	}

	public void setUpperSpecLimit(BigDecimal upperSpecLimit) {
		this.upperSpecLimit = upperSpecLimit;
	}

	/**
	 * @return 目标值
	 */
	public BigDecimal getSpecTarget() {
		return specTarget;
	}

	public void setSpecTarget(BigDecimal specTarget) {
		this.specTarget = specTarget;
	}

	/**
	 * @return 规格下线
	 */
	public BigDecimal getLowerSpecLimit() {
		return lowerSpecLimit;
	}

	public void setLowerSpecLimit(BigDecimal lowerSpecLimit) {
		this.lowerSpecLimit = lowerSpecLimit;
	}

	/**
	 * @return 规则行ID
	 */
	public Long getJudgementId() {
		return judgementId;
	}

	public void setJudgementId(Long judgementId) {
		this.judgementId = judgementId;
	}

	/**
	 * @return 开始组号
	 */
	public Long getFirstSubgroupNum() {
		return firstSubgroupNum;
	}

	public void setFirstSubgroupNum(Long firstSubgroupNum) {
		this.firstSubgroupNum = firstSubgroupNum;
	}

	/**
	 * @return 结束组号
	 */
	public Long getLastSubgroupNum() {
		return lastSubgroupNum;
	}

	public void setLastSubgroupNum(Long lastSubgroupNum) {
		this.lastSubgroupNum = lastSubgroupNum;
	}

	/**
	 * @return 分类组ID
	 */
	public Long getClassifyGroupId() {
		return classifyGroupId;
	}

	public void setClassifyGroupId(Long classifyGroupId) {
		this.classifyGroupId = classifyGroupId;
	}

	/**
	 * @return 分类项ID
	 */
	public Long getClassifyId() {
		return classifyId;
	}

	public void setClassifyId(Long classifyId) {
		this.classifyId = classifyId;
	}

	/**
	 * @return 备注
	 */
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	/**
	 * @return 样本数据ID
	 */
	public Long getCountSampleDataId() {
		return countSampleDataId;
	}

	public void setCountSampleDataId(Long countSampleDataId) {
		this.countSampleDataId = countSampleDataId;
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
}
