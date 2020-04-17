package com.hand.spc.repository.dto;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.hand.hap.mybatis.annotation.ExtensionAttribute;
import com.hand.hap.system.dto.BaseDTO;

import io.swagger.annotations.ApiModelProperty;

/**
 * 附着对象类型表
 *
 * @author a 2019-10-09 10:32:22
 */
@ExtensionAttribute(disable=true)
@Table(name = "pspc_attachment_type")
public class AttachmentType extends BaseDTO {

    public static final String FIELD_ATTACHMENT_TYPE_ID = "attachmentTypeId";
    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_SITE_ID = "siteId";
    public static final String FIELD_ATTACHMENT_TYPE_CODE = "attachmentTypeCode";
    public static final String FIELD_ATTACHMENT_TYPE_DESC = "attachmentTypeDesc";

    //
    // 业务方法(按public protected private顺序排列)
    // ------------------------------------------------------------------------------

    //
    // 数据库字段
    // ------------------------------------------------------------------------------


    @ApiModelProperty("表ID，主键")
    @Id
    @GeneratedValue
    private Long attachmentTypeId;
    @ApiModelProperty(value = "租户ID",required = true)
    @NotNull
    private Long tenantId;
    @ApiModelProperty(value = "站点ID",required = true)
    @NotNull
    private Long siteId;
    @ApiModelProperty(value = "附着对象编码")
    private String attachmentTypeCode;
    @ApiModelProperty(value = "附着对象编码描述")
    private String attachmentTypeDesc;

    //
    // 非数据库字段
    // ------------------------------------------------------------------------------

    //
    // getter/setter
    // ------------------------------------------------------------------------------

    /**
     * @return 表ID，主键
     */
    public Long getAttachmentTypeId() {
        return attachmentTypeId;
    }

    public void setAttachmentTypeId(Long attachmentTypeId) {
        this.attachmentTypeId = attachmentTypeId;
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
     * @return 附着对象编码
     */
    public String getAttachmentTypeCode() {
        return attachmentTypeCode;
    }

    public void setAttachmentTypeCode(String attachmentTypeCode) {
        this.attachmentTypeCode = attachmentTypeCode;
    }
    /**
     * @return 附着对象编码描述
     */
    public String getAttachmentTypeDesc() {
        return attachmentTypeDesc;
    }

    public void setAttachmentTypeDesc(String attachmentTypeDesc) {
        this.attachmentTypeDesc = attachmentTypeDesc;
    }

}
