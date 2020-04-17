package com.hand.plm.spec_product_detail.util.dto;

import com.hand.plm.spec_product_detail.util.controllers.FillForegroundColorUtil;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;

/**
 * @author Mary
 * @version 1.0
 * @name FormTitleDto
 * @date 2019/12/10
 */
public class FormTitleDto {

    private String textName;//报表展示的标题
    private String colName;//对应查询dto的属性名
    private String fontName = "宋体";//字体
    private Short fontHeightInPoints = 10;//字体大小
    private Short isBoldweight = HSSFFont.BOLDWEIGHT_BOLD;//是否加粗
    private Short fontColor = FillForegroundColorUtil.getBackGround("X2");//字体颜色
    private Short fillForegroundColor = FillForegroundColorUtil.getBackGround("X16");//背景颜色
    private Short alignment = HSSFCellStyle.ALIGN_CENTER;//左右居中格式
    private Short verticalAlignment = HSSFCellStyle.VERTICAL_CENTER;//上下居中格式
    private Short rowHeight = 255;//行高
    private Short columnWidth = 3600;//属性宽度
    private boolean wrapText = false;//换行
    private String xyLocation;// 对应excel中的行和列，下标从0开始{"开始行,结束行,开始列,结束列"}
    public static final String CENTER="center";

    public FormTitleDto(String textName, String colName) {
        super();
        this.textName = textName;
        this.colName = colName;
    }

    public FormTitleDto(String textName, String colName, String xyLocation) {
        super();
        this.textName = textName;
        this.colName = colName;
        this.xyLocation = xyLocation;
    }

    public FormTitleDto(String textName, String colName, Short columnWidth, String xyLocation) {
        super();
        this.textName = textName;
        this.colName = colName;
        this.columnWidth = columnWidth;
        this.xyLocation = xyLocation;
    }

    public FormTitleDto() {
    }
    public FormTitleDto(String textName, String colName, String fontName , String xyLocation) {
        super();
        this.textName = textName;
        this.colName = colName;
        this.fontName=fontName;
        this.xyLocation = xyLocation;
    }

    public FormTitleDto(String textName, String colName, String fontName, Short fontHeightInPoints, boolean isBoldweight,
                        String fontColor, String fillForegroundColor, String alignment, String verticalAlignment, Short rowHeight, Short columnWidth, boolean wrapText) {
        super();
        this.textName = textName;
        this.colName = colName;
        this.fontName = fontName;
        this.rowHeight = rowHeight;
        this.columnWidth = columnWidth;
        this.wrapText = wrapText;
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

    public Short getFillForegroundColor() {
        return fillForegroundColor;
    }

    public void setFillForegroundColor(String key) {
        //通过工具类查看背景颜色
        this.fillForegroundColor = FillForegroundColorUtil.getBackGround(key);
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

    public String getTextName() {
        return textName;
    }

    public void setTextName(String textName) {
        this.textName = textName;
    }

    public String getColName() {
        return colName;
    }

    public void setColName(String colName) {
        this.colName = colName;
    }

    public Short getColumnWidth() {
        return columnWidth;
    }

    public void setColumnWidth(Short columnWidth) {
        this.columnWidth = columnWidth;
    }

    public boolean isWrapText() {
        return wrapText;
    }

    public void setWrapText(boolean wrapText) {
        this.wrapText = wrapText;
    }

    public String getXyLocation() {
        return xyLocation;
    }

    public void setXyLocation(String xyLocation) {
        this.xyLocation = xyLocation;
    }


}
