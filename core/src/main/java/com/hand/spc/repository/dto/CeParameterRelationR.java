package com.hand.spc.repository.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.hand.hap.system.dto.BaseDTO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@ApiModel("控制要素与分类组关系")
@Table(name = "pspc_ce_parameter_relation")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CeParameterRelationR extends BaseDTO {

    @Id
    @GeneratedValue
    @ApiModelProperty(hidden = true)
    private Long relationId;

    @ApiModelProperty("租户ID")
    @NotNull(message = "pspc.error.tenant.null")
    private Long tenantId;

    @ApiModelProperty("站点ID")
    @NotNull(message = "pspc.error.site.null")
    private Long siteId;

    @ApiModelProperty("控制要素ID")
    private Long ceParameterId;
    @ApiModelProperty("分类组ID")
    private Long classifyGroupId;

    public Long getRelationId() {
        return relationId;
    }

    public void setRelationId(Long relationId) {
        this.relationId = relationId;
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

    public Long getCeParameterId() {
        return ceParameterId;
    }

    public void setCeParameterId(Long ceParameterId) {
        this.ceParameterId = ceParameterId;
    }

    public Long getClassifyGroupId() {
        return classifyGroupId;
    }

    public void setClassifyGroupId(Long classifyGroupId) {
        this.classifyGroupId = classifyGroupId;
    }
}
