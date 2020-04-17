package com.hand.spc.ecr_main.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hand.hap.mybatis.annotation.ExtensionAttribute;
import com.hand.hap.system.dto.BaseDTO;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * description
 *
 * @author KOCDZX0 2020/03/12 1:59 PM
 */
@ExtensionAttribute(disable=true)
@Table(name = "hpm_ecr_qtp")
public class EcrQtp extends BaseDTO {

    @Id
    @GeneratedValue
    private Long qtpId;

    private String ecrno;

    private String labReqno;

    private String pciTitle;

    private String pciTestReport;

    private String conclusion;

    private Long qtpSeq;

    private String status;

    private String dutyby;

    @DateTimeFormat(pattern =DATE_FORMAT)
    @JsonFormat(pattern = DATE_FORMAT)
    private Date askFinishedDate;

    @DateTimeFormat(pattern =DATE_FORMAT)
    @JsonFormat(pattern = DATE_FORMAT)
    private Date planFinishedDate;

    @DateTimeFormat(pattern =DATE_FORMAT)
    @JsonFormat(pattern = DATE_FORMAT)
    private Date actFinishedDate;
    
    // 物料 id 或 skuId
    private Long itemId;

    // 是否要做
    private String isNeed;

    public Long getQtpId() {
        return qtpId;
    }

    public void setQtpId(Long qtpId) {
        this.qtpId = qtpId;
    }

    public String getEcrno() {
        return ecrno;
    }

    public void setEcrno(String ecrno) {
        this.ecrno = ecrno;
    }

    public String getLabReqno() {
        return labReqno;
    }

    public void setLabReqno(String labReqno) {
        this.labReqno = labReqno;
    }

    public String getPciTitle() {
        return pciTitle;
    }

    public void setPciTitle(String pciTitle) {
        this.pciTitle = pciTitle;
    }

    public String getPciTestReport() {
        return pciTestReport;
    }

    public void setPciTestReport(String pciTestReport) {
        this.pciTestReport = pciTestReport;
    }

    public String getConclusion() {
        return conclusion;
    }

    public void setConclusion(String conclusion) {
        this.conclusion = conclusion;
    }

    public Long getQtpSeq() {
        return qtpSeq;
    }

    public void setQtpSeq(Long qtpSeq) {
        this.qtpSeq = qtpSeq;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDutyby() {
        return dutyby;
    }

    public void setDutyby(String dutyby) {
        this.dutyby = dutyby;
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

	public Long getItemId() {
		return itemId;
	}

	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}

	public String getIsNeed() {
		return isNeed;
	}

	public void setIsNeed(String isNeed) {
		this.isNeed = isNeed;
	}
}
