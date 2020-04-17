package com.hand.spc.repository.dto;

/**
 * Created by slj on 2019-07-01.
 */
public class UploadMessageDto {


    private String  context;
    private String  uploadType;
    private String  uploadParamete;

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

    private Long tenantId;
    private Long siteId;


    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public String getUploadType() {
        return uploadType;
    }

    public void setUploadType(String uploadType) {
        this.uploadType = uploadType;
    }

    public String getUploadParamete() {
        return uploadParamete;
    }

    public void setUploadParamete(String uploadParamete) {
        this.uploadParamete = uploadParamete;
    }
}
