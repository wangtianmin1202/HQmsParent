package com.hand.spc.repository.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.hand.hap.system.dto.BaseDTO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@ApiModel("分类项与分类组关系")
@Table(name = "pspc_classify_relation")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ClassifyRelationR extends BaseDTO {

    @Id
    @GeneratedValue
    @ApiModelProperty(hidden = true)
    private Long classifyRelationId;

    @ApiModelProperty("租户ID")
    @NotNull(message = "pspc.error.tenant.null")
    private Long tenantId;

    @ApiModelProperty("站点ID")
    @NotNull(message = "pspc.error.site.null")
    private Long siteId;

    @ApiModelProperty("分类项ID")
    private Long classifyId;
    @ApiModelProperty("分类组ID")
    private Long classifyGroupId;
    @ApiModelProperty("顺序")
    private Long sequence;

    public Long getClassifyRelationId() {
        return classifyRelationId;
    }

    public void setClassifyRelationId(Long classifyRelationId) {
        this.classifyRelationId = classifyRelationId;
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

    public Long getClassifyId() {
        return classifyId;
    }

    public void setClassifyId(Long classifyId) {
        this.classifyId = classifyId;
    }

    public Long getClassifyGroupId() {
        return classifyGroupId;
    }

    public void setClassifyGroupId(Long classifyGroupId) {
        this.classifyGroupId = classifyGroupId;
    }

    public Long getSequence() {
        return sequence;
    }

    public void setSequence(Long sequence) {
        this.sequence = sequence;
    }
}
