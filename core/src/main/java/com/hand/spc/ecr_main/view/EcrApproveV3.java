package com.hand.spc.ecr_main.view;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * description ECR批准生效-sku显示类
 *
 * @author KOCDZX0 2020/03/09 4:20 PM
 */
public class EcrApproveV3 implements Serializable {

    private BigDecimal itemId;

    private String skuCode;

    private String skuDescription;

    private String skuVersion;

    public BigDecimal getItemId() {
        return itemId;
    }

    public void setItemId(BigDecimal itemId) {
        this.itemId = itemId;
    }

    public String getSkuCode() {
        return skuCode;
    }

    public void setSkuCode(String skuCode) {
        this.skuCode = skuCode;
    }

    public String getSkuDescription() {
        return skuDescription;
    }

    public void setSkuDescription(String skuDescription) {
        this.skuDescription = skuDescription;
    }

    public String getSkuVersion() {
        return skuVersion;
    }

    public void setSkuVersion(String skuVersion) {
        this.skuVersion = skuVersion;
    }
}
