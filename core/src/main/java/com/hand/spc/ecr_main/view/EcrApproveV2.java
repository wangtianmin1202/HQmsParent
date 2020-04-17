package com.hand.spc.ecr_main.view;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * description  ECR批准生效-item显示类
 *
 * @author KOCDZX0 2020/03/09 3:03 PM
 */
public class EcrApproveV2 implements Serializable {

    private String ecrno;

    private BigDecimal itemId;

    private String itemCode;


    private String itemDescriptions;

    private String itemVersion;

    private Date firstRecieveTime;

    public String getEcrno() {
        return ecrno;
    }

    public void setEcrno(String ecrno) {
        this.ecrno = ecrno;
    }

    public BigDecimal getItemId() {
        return itemId;
    }

    public void setItemId(BigDecimal itemId) {
        this.itemId = itemId;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getItemDescriptions() {
        return itemDescriptions;
    }

    public void setItemDescriptions(String itemDescriptions) {
        this.itemDescriptions = itemDescriptions;
    }

    public String getItemVersion() {
        return itemVersion;
    }

    public void setItemVersion(String itemVersion) {
        this.itemVersion = itemVersion;
    }

    public Date getFirstRecieveTime() {
        return firstRecieveTime;
    }

    public void setFirstRecieveTime(Date firstRecieveTime) {
        this.firstRecieveTime = firstRecieveTime;
    }
}
