package com.hand.spc.ecr_main.view;

import java.io.Serializable;
import java.util.Date;

/**
 * description ECR批准生效-ecr显示类
 *
 * @author KOCDZX0 2020/03/09 3:00 PM
 */
public class EcrApproveV1 implements Serializable {

    private String ecrno;

    private Date planFinishedDate;

    private String status;

    public String getEcrno() {
        return ecrno;
    }

    public void setEcrno(String ecrno) {
        this.ecrno = ecrno;
    }

    public Date getPlanFinishedDate() {
        return planFinishedDate;
    }

    public void setPlanFinishedDate(Date planFinishedDate) {
        this.planFinishedDate = planFinishedDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
