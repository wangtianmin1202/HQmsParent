package com.hand.spc.repository.dto;

import java.util.Date;
import java.util.List;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.hand.hap.system.dto.BaseDTO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("实体控制图")

@JsonInclude(JsonInclude.Include.NON_NULL)
@Table(name = "pspc_entity")
public class EntityR extends BaseDTO {
	@Id
    @GeneratedValue
    @ApiModelProperty("实体控制图ID")
    private Long entityId;//实体控制图ID
    @ApiModelProperty("租户ID")
    private Long tenantId;//租户ID
    @ApiModelProperty("站点ID")
    private Long siteId;//站点ID
    @ApiModelProperty("实体控制图版本")
    private String entityVersion;//实体控制图版本
    @ApiModelProperty("实体控制图")
    private String entityCode;//实体控制图
    @ApiModelProperty("控制时间")
    private Date controlDate;//控制时间
    @ApiModelProperty("实体控制图描述")
    private String description;//实体控制图描述
    @ApiModelProperty("附着对象组ID")
    private Long attachmentGroupId;//附着对象组ID
    @Transient
    @ApiModelProperty("附着对象组描述")
    private String attachmentGroupDescription;//附着对象组描述
    @ApiModelProperty("控制要素组ID")
    private Long ceGroupId;//控制要素组ID
    @Transient
    @ApiModelProperty("控制要素组")
    private String ceGroup;//控制要素组
    @Transient
    @ApiModelProperty("控制要素组描述")
    private String ceGroupDescription;
    @ApiModelProperty("控制要素ID")
    private Long ceParameterId;//控制要素ID
    @Transient
    @ApiModelProperty("控制要素")
    private String ceParameter;//控制要素
    @Transient
    @ApiModelProperty("控制要素描述")
    private String ceParameterName;
    @ApiModelProperty("控制图ID")
    private Long chartId;//控制图ID
    @Transient
    @ApiModelProperty("控制图")
    private String chartCode;//控制图
    @ApiModelProperty("状态")
    private String entityStatus;//状态
    @Transient
    @ApiModelProperty("状态描述")
    private String entityStatusDesc;//状态描述
    @ApiModelProperty("是否新增")
    private String entityNew;//是否新增
    @Transient
    @ApiModelProperty("子组大小")
    private Long subgroupSize;
    @Transient
    @ApiModelProperty("实体控制图ID集合")
    private List<Long> entityIdList;//实体控制图ID集合
    @Transient
    @ApiModelProperty("角色集合,逗号拼接")
    private String roleNames;
    @Transient
    private List<SampleSubgroupWaitR> sampleSubgroupWaitList;//样本待分组数据集合
    @Transient
    private List<CountSampleDataWaitR> countSampleDataWaitList;//样本数据预处理(计数)
    @Transient
    private List<EntityRoleRelationR> entityRoleRelationList;//实体控制图与角色关系
    @Transient
    private String attachmentCodes;//附着对象编码,以逗号拼接
    @Transient
    private String attchmentDescriptions;//附着对象描述，以逗号拼接
    @Transient
    private Long size;
    @Transient
    private Date startDate;
    @Transient
    private Date endDate;
    @Transient
    private String subgroupRedisKey;
    @Transient
    private String oocRedisKey;

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

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
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

    public String getEntityVersion() {
        return entityVersion;
    }

    public void setEntityVersion(String entityVersion) {
        this.entityVersion = entityVersion;
    }

    public String getEntityCode() {
        return entityCode;
    }

    public void setEntityCode(String entityCode) {
        this.entityCode = entityCode;
    }

    public Date getControlDate() {
        return controlDate;
    }

    public void setControlDate(Date controlDate) {
        this.controlDate = controlDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getAttachmentGroupId() {
        return attachmentGroupId;
    }

    public void setAttachmentGroupId(Long attachmentGroupId) {
        this.attachmentGroupId = attachmentGroupId;
    }

    public String getAttachmentGroupDescription() {
        return attachmentGroupDescription;
    }

    public void setAttachmentGroupDescription(String attachmentGroupDescription) {
        this.attachmentGroupDescription = attachmentGroupDescription;
    }

    public Long getCeGroupId() {
        return ceGroupId;
    }

    public void setCeGroupId(Long ceGroupId) {
        this.ceGroupId = ceGroupId;
    }

    public String getCeGroup() {
        return ceGroup;
    }

    public void setCeGroup(String ceGroup) {
        this.ceGroup = ceGroup;
    }

    public String getCeGroupDescription() {
        return ceGroupDescription;
    }

    public void setCeGroupDescription(String ceGroupDescription) {
        this.ceGroupDescription = ceGroupDescription;
    }

    public Long getCeParameterId() {
        return ceParameterId;
    }

    public void setCeParameterId(Long ceParameterId) {
        this.ceParameterId = ceParameterId;
    }

    public String getCeParameter() {
        return ceParameter;
    }

    public void setCeParameter(String ceParameter) {
        this.ceParameter = ceParameter;
    }

    public String getCeParameterName() {
        return ceParameterName;
    }

    public void setCeParameterName(String ceParameterName) {
        this.ceParameterName = ceParameterName;
    }

    public Long getChartId() {
        return chartId;
    }

    public void setChartId(Long chartId) {
        this.chartId = chartId;
    }

    public String getChartCode() {
        return chartCode;
    }

    public void setChartCode(String chartCode) {
        this.chartCode = chartCode;
    }

    public String getEntityStatus() {
        return entityStatus;
    }

    public void setEntityStatus(String entityStatus) {
        this.entityStatus = entityStatus;
    }

    public String getEntityStatusDesc() {
        return entityStatusDesc;
    }

    public void setEntityStatusDesc(String entityStatusDesc) {
        this.entityStatusDesc = entityStatusDesc;
    }

    public String getEntityNew() {
        return entityNew;
    }

    public void setEntityNew(String entityNew) {
        this.entityNew = entityNew;
    }

    public Long getSubgroupSize() {
        return subgroupSize;
    }

    public void setSubgroupSize(Long subgroupSize) {
        this.subgroupSize = subgroupSize;
    }

    public List<Long> getEntityIdList() {
        return entityIdList;
    }

    public void setEntityIdList(List<Long> entityIdList) {
        this.entityIdList = entityIdList;
    }

    public String getRoleNames() {
        return roleNames;
    }

    public void setRoleNames(String roleNames) {
        this.roleNames = roleNames;
    }

    public List<SampleSubgroupWaitR> getSampleSubgroupWaitList() {
        return sampleSubgroupWaitList;
    }

    public void setSampleSubgroupWaitList(List<SampleSubgroupWaitR> sampleSubgroupWaitList) {
        this.sampleSubgroupWaitList = sampleSubgroupWaitList;
    }

    public List<EntityRoleRelationR> getEntityRoleRelationList() {
        return entityRoleRelationList;
    }

    public void setEntityRoleRelationList(List<EntityRoleRelationR> entityRoleRelationList) {
        this.entityRoleRelationList = entityRoleRelationList;
    }

    public String getAttachmentCodes() {
        return attachmentCodes;
    }

    public void setAttachmentCodes(String attachmentCodes) {
        this.attachmentCodes = attachmentCodes;
    }

    public String getAttchmentDescriptions() {
        return attchmentDescriptions;
    }

    public void setAttchmentDescriptions(String attchmentDescriptions) {
        this.attchmentDescriptions = attchmentDescriptions;
    }

    public List<CountSampleDataWaitR> getCountSampleDataWaitList() {
        return countSampleDataWaitList;
    }

    public void setCountSampleDataWaitList(List<CountSampleDataWaitR> countSampleDataWaitList) {
        this.countSampleDataWaitList = countSampleDataWaitList;
    }

    public String getSubgroupRedisKey() {
        return subgroupRedisKey;
    }

    public void setSubgroupRedisKey(String subgroupRedisKey) {
        this.subgroupRedisKey = subgroupRedisKey;
    }

    public String getOocRedisKey() {
        return oocRedisKey;
    }

    public void setOocRedisKey(String oocRedisKey) {
        this.oocRedisKey = oocRedisKey;
    }
}
