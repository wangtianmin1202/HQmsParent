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

@ApiModel("分类项")
@Table(name = "pspc_classify")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ClassifyR extends BaseDTO {

    @Id
    @GeneratedValue
    private Long classifyId;

    @ApiModelProperty("租户ID")
    @NotNull(message = "pspc.error.tenant.null")
    private Long tenantId;

    @ApiModelProperty("站点ID")
    @NotNull(message = "pspc.error.site.null")
    private Long siteId;

    @ApiModelProperty("分类项")
    @NotBlank(message = "pspc.error.classify_code.null")
    private String classifyCode;

    @ApiModelProperty("描述")
    private String description;

    @ApiModelProperty("分类项与分类组关系序号(基础界面)")
    @Transient
    private Long sequence;

    public Long getClassifyId() {
        return classifyId;
    }

    public void setClassifyId(Long classifyId) {
        this.classifyId = classifyId;
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

    public String getClassifyCode() {
        return classifyCode;
    }

    public void setClassifyCode(String classifyCode) {
        this.classifyCode = classifyCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getSequence() {
        return sequence;
    }

    public void setSequence(Long sequence) {
        this.sequence = sequence;
    }
}
