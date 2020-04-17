package com.hand.spc.repository.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.hand.hap.system.dto.BaseDTO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import java.util.List;

@ApiModel("分类组")
@Table(name = "pspc_classify_group")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ClassifyGroupR extends BaseDTO {

    @Id
    @GeneratedValue
    private Long classifyGroupId;

    @ApiModelProperty("租户ID")
    @NotNull(message = "pspc.error.tenant.null")
    private Long tenantId;

    @ApiModelProperty("站点ID")
    @NotNull(message = "pspc.error.site.null")
    private Long siteId;

    @NotBlank(message = "pspc.error.classify_group.null")
    @ApiModelProperty("分类组")
    private String classifyGroup;
    @ApiModelProperty("状态")
    private String classifyStatus;
    @ApiModelProperty("描述")
    private String description;
    @ApiModelProperty("类型")
    private String classifyType;

    @ApiModelProperty("分类项集合")
    @Transient
    private List<ClassifyR> classifyList;

    @ApiModelProperty("控制要素集合")
    @Transient
    private List<CeParameterR> ceParameterList;

    public Long getClassifyGroupId() {
        return classifyGroupId;
    }

    public void setClassifyGroupId(Long classifyGroupId) {
        this.classifyGroupId = classifyGroupId;
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

    public String getClassifyGroup() {
        return classifyGroup;
    }

    public void setClassifyGroup(String classifyGroup) {
        this.classifyGroup = classifyGroup;
    }

    public String getClassifyStatus() {
        return classifyStatus;
    }

    public void setClassifyStatus(String classifyStatus) {
        this.classifyStatus = classifyStatus;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getClassifyType() {
        return classifyType;
    }

    public void setClassifyType(String classifyType) {
        this.classifyType = classifyType;
    }

    public List<ClassifyR> getClassifyList() {
        return classifyList;
    }

    public void setClassifyList(List<ClassifyR> classifyList) {
        this.classifyList = classifyList;
    }

    public List<CeParameterR> getCeParameterList() {
        return ceParameterList;
    }

    public void setCeParameterList(List<CeParameterR> ceParameterList) {
        this.ceParameterList = ceParameterList;
    }
}
