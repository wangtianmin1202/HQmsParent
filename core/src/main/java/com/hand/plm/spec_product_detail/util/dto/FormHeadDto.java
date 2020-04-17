package com.hand.plm.spec_product_detail.util.dto;

import com.hand.plm.spec_product_detail.util.controllers.FillForegroundColorUtil;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;

/**
 * @author Mary
 * @version 1.0
 * @name FormHeadDto
 * @date 2019/12/10
 */
public class FormHeadDto {


    private String headName;//表头名
    private String fontName = "宋体";//字体
    private Short fontHeightInPoints = 10;//字体大小
    private Short isBoldweight = HSSFFont.BOLDWEIGHT_BOLD;//是否加粗
    private Short fontColor = FillForegroundColorUtil.getBackGround("X2");//字体颜色
    private Short fillForegroundColor = FillForegroundColorUtil.getBackGround("X50");//背景颜色
    private Short alignment = HSSFCellStyle.ALIGN_CENTER;//左右居中格式
    private Short verticalAlignment = HSSFCellStyle.VERTICAL_BOTTOM;//上下居中格式
    private Short rowHeight = 255;//行高
    public static final String CENTER = "center";

    public FormHeadDto(String headName) {
        super();
        this.headName = headName;
    }

    /**
     * 无参构造器
     */
    public FormHeadDto() {
    }

    public FormHeadDto(String headName, String fontName, Short fontHeightInPoints, boolean isBoldweight,
                       String fontColor, String fillForegroundColor, String alignment, String verticalAlignment, Short rowHeight) {
        super();
        this.headName = headName;
        this.fontName = fontName;
        this.rowHeight = rowHeight;
        this.fontHeightInPoints = fontHeightInPoints;
        if (isBoldweight) {
            this.isBoldweight = HSSFFont.BOLDWEIGHT_BOLD;
        } else {
            this.isBoldweight = HSSFFont.BOLDWEIGHT_NORMAL;
        }
        //通过工具类查看背景颜色
        this.fillForegroundColor = FillForegroundColorUtil.getBackGround(fillForegroundColor);
        //通过工具类查看字体颜色
        this.fontColor = FillForegroundColorUtil.getBackGround(fontColor);
        if ("left".equals(alignment)) {
            this.alignment = HSSFCellStyle.ALIGN_LEFT;
        } else if ("right".equals(alignment)) {
            this.alignment = HSSFCellStyle.ALIGN_RIGHT;
        } else if (CENTER.equals(alignment)) {
            this.alignment = HSSFCellStyle.ALIGN_CENTER;
        }

        if ("bottom".equals(verticalAlignment)) {
            this.verticalAlignment = HSSFCellStyle.VERTICAL_BOTTOM;
        } else if ("top".equals(verticalAlignment)) {
            this.verticalAlignment = HSSFCellStyle.VERTICAL_TOP;
        } else if (CENTER.equals(verticalAlignment)) {
            this.verticalAlignment = HSSFCellStyle.VERTICAL_CENTER;
        }
    }

    @Deprecated
    public Short getFillForegroundColor() {
        return fillForegroundColor;
    }

    @Deprecated
    public void setFillForegroundColor(String key) {
        //通过工具类查看背景颜色
        this.fillForegroundColor = FillForegroundColorUtil.getBackGround(key);
    }

    public String getHeadName() {
        return headName;
    }

    public void setHeadName(String headName) {
        this.headName = headName;
    }

    public String getFontName() {
        return fontName;
    }

    public void setFontName(String fontName) {
        this.fontName = fontName;
    }

    public Short getFontHeightInPoints() {
        return fontHeightInPoints;
    }

    public void setFontHeightInPoints(Short fontHeightInPoints) {
        this.fontHeightInPoints = fontHeightInPoints;
    }

    public Short getBoldweight() {
        return isBoldweight;
    }

    public void setBoldweight(boolean isBoldweight) {
        if (isBoldweight) {
            this.isBoldweight = HSSFFont.BOLDWEIGHT_BOLD;
        } else {
            this.isBoldweight = HSSFFont.BOLDWEIGHT_NORMAL;
        }
    }

    public Short getFontColor() {
        return fontColor;
    }

    public void setFontColor(String key) {
        //通过工具类查看字体颜色
        this.fontColor = FillForegroundColorUtil.getBackGround(key);
    }

    public Short getAlignment() {
        return alignment;
    }

    //alignment 左右居中格式
    public void setAlignment(String alignment) {
        if ("left".equals(alignment)) {
            this.alignment = HSSFCellStyle.ALIGN_LEFT;
        } else if ("right".equals(alignment)) {
            this.alignment = HSSFCellStyle.ALIGN_RIGHT;
        } else if (CENTER.equals(alignment)) {
            this.alignment = HSSFCellStyle.ALIGN_CENTER;
        }
    }

    public Short getVerticalAlignment() {
        return verticalAlignment;
    }

    //上下居中格式
    public void setVerticalAlignment(String verticalAlignment) {
        if ("bottom".equals(verticalAlignment)) {
            this.verticalAlignment = HSSFCellStyle.VERTICAL_BOTTOM;
        } else if ("top".equals(verticalAlignment)) {
            this.verticalAlignment = HSSFCellStyle.VERTICAL_TOP;
        } else if (CENTER.equals(verticalAlignment)) {
            this.verticalAlignment = HSSFCellStyle.VERTICAL_CENTER;
        }
    }

    public Short getRowHeight() {
        return rowHeight;
    }

    public void setRowHeight(Short rowHeight) {
        this.rowHeight = rowHeight;
    }


}
