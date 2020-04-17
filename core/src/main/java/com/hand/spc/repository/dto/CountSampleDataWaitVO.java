package com.hand.spc.repository.dto;

import java.util.List;

public class CountSampleDataWaitVO {
    private Long tenantId;//租户ID
    private Long siteId;//站点ID
    private Long attachmentGroupId;//附着对象组ID
    private Long ceGroupId;//控制要素组ID
    private Long ceParameterId;//控制要素ID
    private List<Long> countSampleDataWaitIdList;//预处理样本数据ID集合
    private List<DataAccessConfigurationR> dataAccessConfigurationList;//数据接入配置集合
    private List<Long> countSampleDataWaitIdDelList;//预处理样本数据ID集合(删除集合;抽样计算获取)

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

    public Long getAttachmentGroupId() {
        return attachmentGroupId;
    }

    public void setAttachmentGroupId(Long attachmentGroupId) {
        this.attachmentGroupId = attachmentGroupId;
    }

    public Long getCeGroupId() {
        return ceGroupId;
    }

    public void setCeGroupId(Long ceGroupId) {
        this.ceGroupId = ceGroupId;
    }

    public Long getCeParameterId() {
        return ceParameterId;
    }

    public void setCeParameterId(Long ceParameterId) {
        this.ceParameterId = ceParameterId;
    }

    public List<Long> getCountSampleDataWaitIdList() {
        return countSampleDataWaitIdList;
    }

    public void setCountSampleDataWaitIdList(List<Long> countSampleDataWaitIdList) {
        this.countSampleDataWaitIdList = countSampleDataWaitIdList;
    }

    public List<DataAccessConfigurationR> getDataAccessConfigurationList() {
        return dataAccessConfigurationList;
    }

    public void setDataAccessConfigurationList(List<DataAccessConfigurationR> dataAccessConfigurationList) {
        this.dataAccessConfigurationList = dataAccessConfigurationList;
    }

    public List<Long> getCountSampleDataWaitIdDelList() {
        return countSampleDataWaitIdDelList;
    }

    public void setCountSampleDataWaitIdDelList(List<Long> countSampleDataWaitIdDelList) {
        this.countSampleDataWaitIdDelList = countSampleDataWaitIdDelList;
    }
}
