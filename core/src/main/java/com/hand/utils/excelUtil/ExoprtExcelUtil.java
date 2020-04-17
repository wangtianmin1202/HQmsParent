package com.hand.utils.excelUtil;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;
import sun.misc.BASE64Decoder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 *
 * @Description 封装的一些关于导出Excel的方法
 *
 * @author yuchao.wang
 * @date 2019/8/27 8:48
 *
 */
public class ExoprtExcelUtil {


    /**
     *
     * @Description 将图片URL放入Excel中
     *
     * @author yuchao.wang
     * @date 2019/8/27 10:01
     * @param imgUrl 图片URL
     * @param sheet sheet页对象
     * @param workbook workbook对象
     * @param rowBegin 开始行号 索引从0开始
     * @param rowEnd 结束行号 索引从0开始
     * @param colBegin 开始列号 索引从0开始
     * @param colEnd 结束列号 索引从0开始
     * @return void
     *
     */
    public static void addPictureIntoExcel(String imgUrl, XSSFSheet sheet, XSSFWorkbook workbook,
                                    int rowBegin, int rowEnd, int colBegin, int colEnd) throws IOException {
        // Base64解码
        byte[] b = new BASE64Decoder().decodeBuffer(imgUrl);
        InputStream input = new ByteArrayInputStream(b);
        //循环读取图片插入到excel
        //画图的顶级管理器，一个sheet只能获取一个（一定要注意这点）
        XSSFDrawing patriarch = sheet.createDrawingPatriarch();
        ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();

        BufferedImage bufferImg = ImageIO.read(input);
        ImageIO.write(bufferImg, "png", byteArrayOut);
        //anchor主要用于设置图片的属性
        XSSFClientAnchor anchor = new XSSFClientAnchor(0, 0, 255, 255, colBegin, rowBegin, colEnd, rowEnd);
        //插入图片
        patriarch.createPicture(anchor, workbook.addPicture(byteArrayOut.toByteArray(), XSSFWorkbook.PICTURE_TYPE_PNG));
    }

    /**
     *
     * @Description 获取默认单元格样式
     *
     * @author yuchao.wang
     * @date 2019/8/27 10:10
     * @param workbook workbook对象
     * @return org.apache.poi.xssf.usermodel.XSSFCellStyle
     *
     */
    public static XSSFCellStyle getDefaultCellStyle(XSSFWorkbook workbook){
        XSSFCellStyle style = workbook.createCellStyle();
        style.setWrapText(true);
        style.setAlignment(HorizontalAlignment.CENTER);//水平居中
        style.setVerticalAlignment(VerticalAlignment.CENTER);//垂直居中
        style.setBorderTop(CellStyle.BORDER_THIN);//顶部边框
        style.setTopBorderColor(IndexedColors.BLACK.getIndex());//顶部边框颜色
        style.setBorderBottom(CellStyle.BORDER_THIN);//底部边框
        style.setBottomBorderColor(IndexedColors.BLACK.getIndex());//底部边框颜色
        style.setBorderLeft(CellStyle.BORDER_THIN);//左侧边框
        style.setLeftBorderColor(IndexedColors.BLACK.getIndex());//左侧边框颜色
        style.setBorderRight(CellStyle.BORDER_THIN);//右侧边框
        style.setRightBorderColor(IndexedColors.BLACK.getIndex());//右侧边框颜色
        return style;
    }

    /**
     *
     * @Description 获取单元格样式-可定义背景色
     *
     * @author yuchao.wang
     * @date 2019/8/27 10:11
     * @param workbook workbook对象
     * @param pattern 填充样式
     * @param color 填充颜色
     * @return org.apache.poi.xssf.usermodel.XSSFCellStyle
     *
     */
    public static XSSFCellStyle getCellStyle(XSSFWorkbook workbook, FillPatternType pattern, XSSFColor color){
        XSSFCellStyle style = workbook.createCellStyle();
        style.setWrapText(true);
        style.setAlignment(HorizontalAlignment.CENTER);//水平居中
        style.setVerticalAlignment(VerticalAlignment.CENTER);//垂直居中
        style.setBorderTop(CellStyle.BORDER_THIN);//顶部边框
        style.setTopBorderColor(IndexedColors.BLACK.getIndex());//顶部边框颜色
        style.setBorderBottom(CellStyle.BORDER_THIN);//底部边框
        style.setBottomBorderColor(IndexedColors.BLACK.getIndex());//底部边框颜色
        style.setBorderLeft(CellStyle.BORDER_THIN);//左侧边框
        style.setLeftBorderColor(IndexedColors.BLACK.getIndex());//左侧边框颜色
        style.setBorderRight(CellStyle.BORDER_THIN);//右侧边框
        style.setRightBorderColor(IndexedColors.BLACK.getIndex());//右侧边框颜色
        style.setFillPattern(pattern);
        style.setFillForegroundColor(color);
        return style;
    }
}