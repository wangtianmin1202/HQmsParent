package com.hand.spc.repository.dto;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.hand.hap.system.dto.BaseDTO;

//import io.choerodon.mybatis.domain.AuditDomain;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author peng.hu04@hand-china.com 2019-07-07 11:32:28
 */
@ApiModel("")
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Table(name = "pspc_message_upload_rel")
public class MessageUploadRelR extends BaseDTO {

    public static final String FIELD_REL_ID = "relId";
    public static final String FIELD_UPLOAD_CONFIG_ID = "uploadConfigId";
    public static final String FIELD_MESSAGE_ID = "messageId";
    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_SITE_ID = "siteId";
    public static final String FIELD_STATUS = "status";

    //
    // 业务方法(按public protected private顺序排列)
    // ------------------------------------------------------------------------------

    //
    // 数据库字段
    // ------------------------------------------------------------------------------


    @ApiModelProperty("主键ID")
    @Id
    @GeneratedValue
    private Long relId;
    @ApiModelProperty(value = "上传配置表ID", required = true)
    @NotNull
    private Long uploadConfigId;
    @ApiModelProperty(value = "", required = true)
    private String messageId;
    @ApiModelProperty(value = "租户ID", required = true)
    @NotNull
    private Long tenantId;
    @ApiModelProperty(value = "站点ID", required = true)
    @NotNull
    private Long siteId;
    @ApiModelProperty(value = "状态")
    private String status;

    //
    // 非数据库字段
    // ------------------------------------------------------------------------------

    //
    // getter/setter
    // ------------------------------------------------------------------------------

    /**
     * @return
     */
    public Long getRelId() {
        return relId;
    }

    public void setRelId(Long relId) {
        this.relId = relId;
    }

    /**
     * @return
     */
    public Long getUploadConfigId() {
        return uploadConfigId;
    }

    public void setUploadConfigId(Long uploadConfigId) {
        this.uploadConfigId = uploadConfigId;
    }

    /**
     * @return
     */
    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
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
     * @return
     */
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
