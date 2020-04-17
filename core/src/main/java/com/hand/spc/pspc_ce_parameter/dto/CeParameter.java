package com.hand.spc.pspc_ce_parameter.dto;

/**Auto Generated By Hap Code Generator**/
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import com.hand.hap.mybatis.annotation.ExtensionAttribute;
import org.hibernate.validator.constraints.Length;
import javax.persistence.Table;
import com.hand.hap.system.dto.BaseDTO;

import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.NotEmpty;
@ExtensionAttribute(disable=true)
@Table(name = "pspc_ce_parameter")
public class CeParameter extends BaseDTO {

     public static final String FIELD_CE_PARAMETER_ID = "ceParameterId";
     public static final String FIELD_TENANT_ID = "tenantId";
     public static final String FIELD_SITE_ID = "siteId";
     public static final String FIELD_CE_PARAMETER = "ceParameter";
     public static final String FIELD_CE_PARAMETER_NAME = "ceParameterName";
     public static final String FIELD_UOM = "uom";
     public static final String FIELD_REMARK = "remark";


     @Id
     @GeneratedValue
     private Long ceParameterId; //控制要素ID

     private Long tenantId; //租户ID

     private Long siteId; //站点ID

     @NotEmpty
     @Length(max = 60)
     private String ceParameter; //控制要素

     @Length(max = 120)
     private String ceParameterName; //名称

     @Length(max = 10)
     private String uom; //单位

     @Length(max = 240)
     private String remark; //描述

    @Transient
    private Long classifyGroupId;
    @Transient
    private String operateStatus;//操作状态，判断前台传过来的操作是新增还是编辑

    /**
     * 分类组和控制要素关系id
     */
    @Transient
    private Long relationId;

    @Transient
    private Long ceGroupId;//控制要素组ID

    @Transient
    private Long ceRelationshipId;//控制要素组及控制要素关系ID

    @Transient
    private String oldCeParameterCode; //原控制要素编码

    @Transient
    private String __status;
    private String type; 

    public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
     public void setCeParameterId(Long ceParameterId){
         this.ceParameterId = ceParameterId;
     }

     public Long getCeParameterId(){
         return ceParameterId;
     }

     public void setTenantId(Long tenantId){
         this.tenantId = tenantId;
     }

     public Long getTenantId(){
         return tenantId;
     }

     public void setSiteId(Long siteId){
         this.siteId = siteId;
     }

     public Long getSiteId(){
         return siteId;
     }

     public void setCeParameter(String ceParameter){
         this.ceParameter = ceParameter;
     }

     public String getCeParameter(){
         return ceParameter;
     }

     public void setCeParameterName(String ceParameterName){
         this.ceParameterName = ceParameterName;
     }

     public String getCeParameterName(){
         return ceParameterName;
     }

     public void setUom(String uom){
         this.uom = uom;
     }

     public String getUom(){
         return uom;
     }

     public void setRemark(String remark){
         this.remark = remark;
     }

     public String getRemark(){
         return remark;
     }

    public Long getClassifyGroupId() {
        return classifyGroupId;
    }

    public void setClassifyGroupId(Long classifyGroupId) {
        this.classifyGroupId = classifyGroupId;
    }

    public String getOperateStatus() {
        return operateStatus;
    }

    public void setOperateStatus(String operateStatus) {
        this.operateStatus = operateStatus;
    }

    public Long getRelationId() {
        return relationId;
    }

    public void setRelationId(Long relationId) {
        this.relationId = relationId;
    }

    public Long getCeGroupId() {
        return ceGroupId;
    }

    public void setCeGroupId(Long ceGroupId) {
        this.ceGroupId = ceGroupId;
    }

    public Long getCeRelationshipId() {
        return ceRelationshipId;
    }

    public void setCeRelationshipId(Long ceRelationshipId) {
        this.ceRelationshipId = ceRelationshipId;
    }

    public String getOldCeParameterCode() {
        return oldCeParameterCode;
    }

    public void setOldCeParameterCode(String oldCeParameterCode) {
        this.oldCeParameterCode = oldCeParameterCode;
    }

    @Override
    public String get__status() {
        return __status;
    }

    @Override
    public void set__status(String __status) {
        this.__status = __status;
    }
}
