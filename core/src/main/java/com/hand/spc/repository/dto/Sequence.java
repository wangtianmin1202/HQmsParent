package com.hand.spc.repository.dto;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.hand.hap.mybatis.annotation.ExtensionAttribute;
import com.hand.hap.system.dto.BaseDTO;


@ExtensionAttribute(disable=true)
@Table(name = "pspc_sequence")
public class Sequence extends BaseDTO {

	@Id
    @GeneratedValue
    private Long sequenceId;//序号ID
    private Long tenantId;//租户ID
    private Long siteId;//站点ID
    private String sequenceCode;//序号编码
    private Long maxNum;//序号最大值

    public Sequence() {
    }

    public Sequence(Long tenantId, Long siteId, String sequenceCode, Long maxNum) {
        this.tenantId = tenantId;
        this.siteId = siteId;
        this.sequenceCode = sequenceCode;
        this.maxNum = maxNum;
    }

    public Long getSequenceId() {
        return sequenceId;
    }

    public void setSequenceId(Long sequenceId) {
        this.sequenceId = sequenceId;
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

    public String getSequenceCode() {
        return sequenceCode;
    }

    public void setSequenceCode(String sequenceCode) {
        this.sequenceCode = sequenceCode;
    }

    public Long getMaxNum() {
        return maxNum;
    }

    public void setMaxNum(Long maxNum) {
        this.maxNum = maxNum;
    }
}
