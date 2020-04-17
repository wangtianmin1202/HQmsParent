package com.hand.spc.repository.dto;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.hand.hap.system.dto.BaseDTO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 控制要素
 */
@ApiModel("控制要素")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Table(name = "pspc_ce_parameter")
public class CeParameterSPC extends BaseDTO {

    @Id
    @GeneratedValue
    private Long ceParameterId;//控制要素ID

    @ApiModelProperty("租户")
    @NotNull(message = "pspc.error.tenant.null")
    private Long tenantId;//租户

    @ApiModelProperty("站点")
    @NotNull(message = "pspc.error.site.null")
    private Long siteId;//站点

    @ApiModelProperty("控制要素")
    @NotBlank(message = "pspc.error.ce_parameter.null")
    private String ceParameter;//控制要素

    @ApiModelProperty("控制要素名称")
    private String ceParameterName;//控制要素名称

    @ApiModelProperty("单位")
    private String uom;//单位

    @ApiModelProperty("备注")
    private String remark;//备注

    @ApiModelProperty("控制要素关系ID")
    @Transient
    private Long ceRelationshipId;

    @ApiModelProperty("控制要素关系 顺序(基础界面)")
    @Transient
    private Long sequence;

    @ApiModelProperty("控制要素组ID(逻辑待确定)")
    @Transient
    private Long ceGroupId;

    public Long getCeParameterId() {
        return ceParameterId;
    }

    public void setCeParameterId(Long ceParameterId) {
        this.ceParameterId = ceParameterId;
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

    public String getUom() {
        return uom;
    }

    public void setUom(String uom) {
        this.uom = uom;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Long getCeRelationshipId() {

        return ceRelationshipId;
    }

    public void setCeRelationshipId(Long ceRelationshipId) {
        this.ceRelationshipId = ceRelationshipId;
    }

    public Long getSequence() {
        return sequence;
    }

    public void setSequence(Long sequence) {
        this.sequence = sequence;
    }

    public Long getCeGroupId() {
        return ceGroupId;
    }

    public void setCeGroupId(Long ceGroupId) {
        this.ceGroupId = ceGroupId;
    }
}
