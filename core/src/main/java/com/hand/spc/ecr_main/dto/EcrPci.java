package com.hand.spc.ecr_main.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hand.hap.mybatis.annotation.ExtensionAttribute;
import com.hand.hap.system.dto.BaseDTO;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * description
 *
 * @author KOCDZX0 2020/03/11 6:09 PM
 */
@ExtensionAttribute(disable=true)
@Table(name = "hpm_ecr_pci")
public class EcrPci extends BaseDTO {

    @Id
    @GeneratedValue
    private Long pciId;

    private String pciCode;

    private String ecrno;

    private String pciAttachment;

    private String pciText;

    private String status;

    @Transient
    private String createName;
    
    // 负责人
    private String dutyby;
    
    // 是否要做
    private String isNeed;
    
    // PCI 序号
    private Long pciSeq;

    @DateTimeFormat(pattern =DATE_FORMAT)
    @JsonFormat(pattern = DATE_FORMAT)
    private Date askFinishedDate;

    @DateTimeFormat(pattern =DATE_FORMAT)
    @JsonFormat(pattern = DATE_FORMAT)
    private Date planFinishedDate;

    @DateTimeFormat(pattern =DATE_FORMAT)
    @JsonFormat(pattern = DATE_FORMAT)
    private Date actFinishedDate;

    public Long getPciId() {
        return pciId;
    }

    public void setPciId(Long pciId) {
        this.pciId = pciId;
    }

    public String getPciCode() {
        return pciCode;
    }

    public void setPciCode(String pciCode) {
        this.pciCode = pciCode;
    }

    public String getEcrno() {
        return ecrno;
    }

    public void setEcrno(String ecrno) {
        this.ecrno = ecrno;
    }

    public String getPciAttachment() {
        return pciAttachment;
    }

    public void setPciAttachment(String pciAttachment) {
        this.pciAttachment = pciAttachment;
    }

    public String getPciText() {
        return pciText;
    }

    public void setPciText(String pciText) {
        this.pciText = pciText;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreateName() {
        return createName;
    }

    public void setCreateName(String createName) {
        this.createName = createName;
    }

    public Date getAskFinishedDate() {
        return askFinishedDate;
    }

    public void setAskFinishedDate(Date askFinishedDate) {
        this.askFinishedDate = askFinishedDate;
    }

    public Date getPlanFinishedDate() {
        return planFinishedDate;
    }

    public void setPlanFinishedDate(Date planFinishedDate) {
        this.planFinishedDate = planFinishedDate;
    }

    public Date getActFinishedDate() {
        return actFinishedDate;
    }

    public void setActFinishedDate(Date actFinishedDate) {
        this.actFinishedDate = actFinishedDate;
    }

	public String getDutyby() {
		return dutyby;
	}

	public void setDutyby(String dutyby) {
		this.dutyby = dutyby;
	}

	public String getIsNeed() {
		return isNeed;
	}

	public void setIsNeed(String isNeed) {
		this.isNeed = isNeed;
	}

	public Long getPciSeq() {
		return pciSeq;
	}

	public void setPciSeq(Long pciSeq) {
		this.pciSeq = pciSeq;
	}
	
	
    
}
