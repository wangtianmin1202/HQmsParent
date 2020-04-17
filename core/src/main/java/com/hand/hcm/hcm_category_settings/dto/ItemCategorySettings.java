package com.hand.hcm.hcm_category_settings.dto;

/**
 * Auto Generated By Hap Code Generator
 **/

import com.hand.hap.mybatis.annotation.ExtensionAttribute;
import com.hand.hap.system.dto.BaseDTO;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@ExtensionAttribute(disable = true)
@Table(name = "HCM_ITEM_CATEGORY_SETTINGS")
public class ItemCategorySettings extends BaseDTO {

    public static final String FIELD_KID = "kid";
    public static final String FIELD_ITEM_ID = "itemId";
    public static final String FIELD_CATEGORY_FIRST_ID = "categoryFirstId";
    public static final String FIELD_CATEGORY_SECOND_ID = "categorySecondId";
    public static final String FIELD_CATEGORY_THIRD_ID = "categoryThirdId";


    @Id
    @GeneratedValue
    private Long kid;

    private Long itemId;

    private Long categoryFirstId;

    private Long categorySecondId;

    private Long categoryThirdId;


    @Transient
    private String itemCode;

    @Transient
    private String itemDescription;

    @Transient
    private Long cost;

    @Transient
    private String itemType;

    @Transient
    private String makeBuyCode;

    @Transient
    private String itemCategory;

    @Transient
    private String priceUnit;

    @Transient
    private String firstCode;
    @Transient
    private String secondCode;
    @Transient
    private String thirdCode;

    @Transient
    private Long itemEdition;
    @Transient
    private float plantId;


    public Long getKid() {
        return kid;
    }

    public void setKid(Long kid) {
        this.kid = kid;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public Long getCategoryFirstId() {
        return categoryFirstId;
    }

    public void setCategoryFirstId(Long categoryFirstId) {
        this.categoryFirstId = categoryFirstId;
    }

    public Long getCategorySecondId() {
        return categorySecondId;
    }

    public void setCategorySecondId(Long categorySecondId) {
        this.categorySecondId = categorySecondId;
    }

    public Long getCategoryThirdId() {
        return categoryThirdId;
    }

    public void setCategoryThirdId(Long categoryThirdId) {
        this.categoryThirdId = categoryThirdId;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    public Long getCost() {
        return cost;
    }

    public void setCost(Long cost) {
        this.cost = cost;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public String getMakeBuyCode() {
        return makeBuyCode;
    }

    public void setMakeBuyCode(String makeBuyCode) {
        this.makeBuyCode = makeBuyCode;
    }

    public String getItemCategory() {
        return itemCategory;
    }

    public void setItemCategory(String itemCategory) {
        this.itemCategory = itemCategory;
    }

    public String getPriceUnit() {
        return priceUnit;
    }

    public void setPriceUnit(String priceUnit) {
        this.priceUnit = priceUnit;
    }

    public String getFirstCode() {
        return firstCode;
    }

    public void setFirstCode(String firstCode) {
        this.firstCode = firstCode;
    }

    public String getSecondCode() {
        return secondCode;
    }

    public void setSecondCode(String secondCode) {
        this.secondCode = secondCode;
    }

    public String getThirdCode() {
        return thirdCode;
    }

    public void setThirdCode(String thirdCode) {
        this.thirdCode = thirdCode;
    }

    public Long getItemEdition() {
        return itemEdition;
    }

    public void setItemEdition(Long itemEdition) {
        this.itemEdition = itemEdition;
    }

    public float getPlantId() {
        return plantId;
    }

    public void setPlantId(float plantId) {
        this.plantId = plantId;
    }
}
