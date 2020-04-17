package com.hand.spc.ecr_main.view;

import jdk.nashorn.internal.objects.annotations.Getter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * description
 *
 * @author KOCDZX0 2020/03/09 11:19 AM
 */
public class EcrVtpVO implements Serializable {


    private String ecrno;

    private String skuCode;

    private String vtpNum;

    private BigDecimal vtpSeq;

    private String conclution;

    private String status;

    private String dutyby;

    private Date finishedDate;

    private Date planFinishedDate;

    private Date actuallyFinishedDate;

    private String skuDesc;
     
    
     
	public String getSkuDesc() {
		return skuDesc;
	}

	public void setSkuDesc(String skuDesc) {
		this.skuDesc = skuDesc;
	}

	public String getEcrno() {
        return ecrno;
    }

    public void setEcrno(String ecrno) {
        this.ecrno = ecrno;
    }

    public String getConclution() {
        return conclution;
    }

    public void setConclution(String conclution) {
        this.conclution = conclution;
    }

    public String getSkuCode() {
        return skuCode;
    }

    public void setSkuCode(String skuCode) {
        this.skuCode = skuCode;
    }

    public String getVtpNum() {
        return vtpNum;
    }

    public void setVtpNum(String vtpNum) {
        this.vtpNum = vtpNum;
    }

    public BigDecimal getVtpSeq() {
        return vtpSeq;
    }

    public void setVtpSeq(BigDecimal vtpSeq) {
        this.vtpSeq = vtpSeq;
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

    public Date getFinishedDate() {
        return finishedDate;
    }

    public void setFinishedDate(Date finishedDate) {
        this.finishedDate = finishedDate;
    }

    public Date getPlanFinishedDate() {
        return planFinishedDate;
    }

    public void setPlanFinishedDate(Date planFinishedDate) {
        this.planFinishedDate = planFinishedDate;
    }

    public Date getActuallyFinishedDate() {
        return actuallyFinishedDate;
    }

    public void setActuallyFinishedDate(Date actuallyFinishedDate) {
        this.actuallyFinishedDate = actuallyFinishedDate;
    }


}
