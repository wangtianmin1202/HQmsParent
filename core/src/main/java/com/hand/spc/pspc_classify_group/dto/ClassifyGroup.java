package com.hand.spc.pspc_classify_group.dto;

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

import java.util.List;

@ExtensionAttribute(disable=true)
@Table(name = "pspc_classify_group")
public class ClassifyGroup extends BaseDTO {

     public static final String FIELD_CLASSIFY_GROUP_ID = "classifyGroupId";
     public static final String FIELD_TENANT_ID = "tenantId";
     public static final String FIELD_SITE_ID = "siteId";
     public static final String FIELD_CLASSIFY_GROUP = "classifyGroup";
     public static final String FIELD_CLASSIFY_STATUS = "classifyStatus";
     public static final String FIELD_DESCRIPTION = "description";
     public static final String FIELD_CLASSIFY_TYPE = "classifyType";


     @Id
     @GeneratedValue
     private Long classifyGroupId; //表ID，主键

     @NotNull
     private Long tenantId; //租户ID

     @NotNull
     private Long siteId; //站点ID

     @NotEmpty
     @Length(max = 100)
     private String classifyGroup; //分类组

     @NotEmpty
     @Length(max = 30)
     private String classifyStatus; //状态

     @Length(max = 150)
     private String description; //描述

     @Length(max = 100)
     private String classifyType; //类型（区分OOC备注原因分类与计数型数据分析）

    /**
     * 分类组下的分类项ID
     */
    @Transient
     private List<Long> classifyIdList;

    /**
     * 分类组下的控制要素id
     */
     @Transient
     private List<Long> ceParameterIdList;

    /**
     * 保存类型 1、新建 2、修改 3、副本保存
     */
    @Transient
     private String saveType;

    /**
     * 需要copy的分类组id
     */
    @Transient
    private Long copyGroupId;

    /**
     * 控制要素ID
     */
    @Transient
    private Long ceParameterId;

    public void setClassifyGroupId(Long classifyGroupId){
         this.classifyGroupId = classifyGroupId;
     }

     public Long getClassifyGroupId(){
         return classifyGroupId;
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

     public void setClassifyGroup(String classifyGroup){
         this.classifyGroup = classifyGroup;
     }

     public String getClassifyGroup(){
         return classifyGroup;
     }

     public void setClassifyStatus(String classifyStatus){
         this.classifyStatus = classifyStatus;
     }

     public String getClassifyStatus(){
         return classifyStatus;
     }

     public void setDescription(String description){
         this.description = description;
     }

     public String getDescription(){
         return description;
     }

     public void setClassifyType(String classifyType){
         this.classifyType = classifyType;
     }

     public String getClassifyType(){
         return classifyType;
     }

    public List<Long> getClassifyIdList() {
        return classifyIdList;
    }

    public void setClassifyIdList(List<Long> classifyIdList) {
        this.classifyIdList = classifyIdList;
    }

    public List<Long> getCeParameterIdList() {
        return ceParameterIdList;
    }

    public void setCeParameterIdList(List<Long> ceParameterIdList) {
        this.ceParameterIdList = ceParameterIdList;
    }

    public String getSaveType() {
        return saveType;
    }

    public void setSaveType(String saveType) {
        this.saveType = saveType;
    }

    public Long getCopyGroupId() {
        return copyGroupId;
    }

    public void setCopyGroupId(Long copyGroupId) {
        this.copyGroupId = copyGroupId;
    }

    public Long getCeParameterId() {
        return ceParameterId;
    }

    public void setCeParameterId(Long ceParameterId) {
        this.ceParameterId = ceParameterId;
    }
}
