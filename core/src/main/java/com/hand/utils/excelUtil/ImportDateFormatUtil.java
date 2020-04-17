package com.hand.utils.excelUtil;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;

public class ImportDateFormatUtil {
	public static String importByExcelForDate(Cell currentCell) {
		
		String currentCellValue = "";
		// 判断单元格数据是否是日期
		if ("yyyy/mm;@".equals(currentCell.getCellStyle().getDataFormatString())
				|| "m/d/yy".equals(currentCell.getCellStyle().getDataFormatString())
				|| "yy/m/d".equals(currentCell.getCellStyle().getDataFormatString())
				|| "mm/dd/yy".equals(currentCell.getCellStyle().getDataFormatString())
				|| "dd-mmm-yy".equals(currentCell.getCellStyle().getDataFormatString())
				|| "yyyy/m/d".equals(currentCell.getCellStyle().getDataFormatString())) {
			if (DateUtil.isCellDateFormatted(currentCell)) {
				// 用于转化为日期格式
				Date d = currentCell.getDateCellValue();
				DateFormat formater = new SimpleDateFormat("yyyy/MM/dd");
				currentCellValue = formater.format(d);
			}
		} else {
			// 不是日期原值返回
			currentCellValue = currentCell.toString();
		}
		return currentCellValue;
	}
}
