package com.hand.hqm.hqm_msa_bias_value.service.impl;

import com.hand.hap.core.IRequest;
import com.hand.hap.system.service.impl.BaseServiceImpl;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.hand.hqm.hqm_msa_bias_value.dto.MsaBiasValue;
import com.hand.hqm.hqm_msa_bias_value.mapper.MsaBiasValueMapper;
import com.hand.hqm.hqm_msa_bias_value.service.IMsaBiasValueService;
import com.hand.hqm.hqm_msa_plan.dto.MsaPlan;
import com.hand.hqm.hqm_msa_plan.service.IMsaPlanService;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

@Service
@Transactional(rollbackFor = Exception.class)
public class MsaBiasValueServiceImpl extends BaseServiceImpl<MsaBiasValue> implements IMsaBiasValueService {

	@Autowired
	private MsaBiasValueMapper mapper;
	@Autowired
	private IMsaPlanService msaPlanService;

	@Override
	public List<MsaBiasValue> query(IRequest requestContext, MsaBiasValue dto) {
		List<MsaBiasValue> msaBiasValueList = mapper.select(dto);

		if (msaBiasValueList != null && msaBiasValueList.size() > 0) {
			// 求列表头
			Set<String> groupAndDateSet = msaBiasValueList.stream()
					.collect(Collectors.groupingBy(MsaBiasValue::getGroupAndDate)).keySet();
			List<String> groupAndDateList = new ArrayList(groupAndDateSet);
			groupAndDateList = sortList(groupAndDateList);

			// 求列表行数
			Set<Float> measureNumSet = msaBiasValueList.stream()
					.collect(Collectors.groupingBy(MsaBiasValue::getMeasureNum)).keySet();
			List<Float> measureNumList = new ArrayList(measureNumSet);
			Collections.sort(measureNumList);

			msaBiasValueList.get(0).setGroupAndDateList(groupAndDateList);
			msaBiasValueList.get(0).setMeasureNumList(measureNumList);
		}
		return msaBiasValueList;
	}

	/**
	 * List<String> 排序 String格式： 1,abc 根据逗号前的数字升序
	 * 
	 * @param list
	 * @return
	 */
	private List<String> sortList(List<String> list) {
		Collections.sort(list, new Comparator<String>() {
			@Override
			public int compare(String o1, String o2) {
				if (Long.parseLong((o1.split(",")[0])) > Long.parseLong((o2.split(",")[0]))) {
					return 1;
				} else if (Long.parseLong((o1.split(",")[0])) < Long.parseLong((o2.split(",")[0]))) {
					return -1;
				} else {
					return 0;
				}
			}
		});
		return list;
	}

	@Override
	public List<MsaBiasValue> submit(IRequest requestContext, List<MsaBiasValue> dto) {
		MsaPlan masPlan = new MsaPlan();
		masPlan.setMsaPlanId(dto.get(0).getMsaPlanId());
		masPlan = msaPlanService.selectByPrimaryKey(requestContext, masPlan);
		if (masPlan == null) {
			throw new RuntimeException("计划信息不存在");
		}
		masPlan.setExpectedDeterioration(dto.get(0).getExpectedDeterioration());
		masPlan.setSampleDescription(dto.get(0).getSampleDescription());
		masPlan.setMeasuredBy(dto.get(0).getMeasuredBy());
		// 更新计划信息
		msaPlanService.updateByPrimaryKeySelective(requestContext, masPlan);

		// 偏倚分析测量数据处理
		for (MsaBiasValue msaBiasValue : dto) {
			if (msaBiasValue.getMeasureValue() != null) {
				if (msaBiasValue.getMsaBiasValueId() != null) {
					// 更新
					MsaBiasValue biasValue = new MsaBiasValue();
					biasValue.setMsaBiasValueId(msaBiasValue.getMsaBiasValueId());
					biasValue.setMeasureValue(msaBiasValue.getMeasureValue());
					biasValue.setMeasureDate(msaBiasValue.getMeasureDate());
					biasValue.setGroupNum(msaBiasValue.getGroupNum());
					biasValue.setStandardValue(msaBiasValue.getStandardValue());

					self().updateByPrimaryKeySelective(requestContext, biasValue);
				} else {
					// 新增
					MsaBiasValue biasValue = new MsaBiasValue();
					biasValue.setMeasureNum(msaBiasValue.getMeasureNum());
					biasValue.setMeasureValue(msaBiasValue.getMeasureValue());
					biasValue.setMeasureDate(msaBiasValue.getMeasureDate());
					biasValue.setGroupNum(msaBiasValue.getGroupNum());
					biasValue.setStandardValue(msaBiasValue.getStandardValue());
					biasValue.setMsaPlanId(msaBiasValue.getMsaPlanId());

					self().insertSelective(requestContext, biasValue);
				}
			}
		}
		return dto;
	}

	@Override
	public List<MsaBiasValue> excelImport(HttpServletRequest request, IRequest requestContext) throws Exception {
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		// 获取文件map集合
		Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();

		float msaPlanId = Float.parseFloat(request.getParameter("msaPlanId"));

		for (Map.Entry<String, MultipartFile> entry : fileMap.entrySet()) {
			List<MsaBiasValue> msaBiasValueList = new ArrayList();

			MultipartFile forModel = entry.getValue();
			Sheet sheet;
			XSSFWorkbook workBook;
			workBook = new XSSFWorkbook(forModel.getInputStream());

			sheet = workBook.getSheetAt(0);

			for (int i = 1; i <= sheet.getLastRowNum(); i++) {
				MsaBiasValue msaBiasValue = new MsaBiasValue();
				// i=1,从第二行开始
				Row row = sheet.getRow(i);

				// 测量时间
				checkEmpty(row.getCell(0).toString(), i, "测量时间");
				String dataStr = importByExcelForDate(row.getCell(0));
				Date measureDate = dateFormat(dataStr, (i + 1), "测量时间");

				// 测量次数
				checkEmpty(row.getCell(1).toString(), i, "子组号");
				long groupNum = (long) parseNumber(row.getCell(1).toString(), (i + 1), "子组号");

				// 测量次数
				checkEmpty(row.getCell(1).toString(), i, "测量次数");
				float measureNum = parseNumber(row.getCell(2).toString(), (i + 1), "测量次数");

				// 测量值
				checkEmpty(row.getCell(2).toString(), i, "测量值");
				float measureValue = parseNumber(row.getCell(3).toString(), (i + 1), "测量值");

				msaBiasValue.setMsaPlanId(msaPlanId);
				msaBiasValue.setGroupNum(groupNum);
				msaBiasValue.setMeasureNum(measureNum);
				msaBiasValue.setMeasureValue(measureValue);
				msaBiasValue.setMeasureDate(measureDate);

				msaBiasValueList.add(msaBiasValue);
			}
			// 数据插入表
			for (MsaBiasValue biasValue : msaBiasValueList) {
				MsaBiasValue value = new MsaBiasValue();
				value.setMsaPlanId(biasValue.getMsaPlanId());
				value.setGroupNum(biasValue.getGroupNum());
				value.setMeasureNum(biasValue.getMeasureNum());
				// 检查是否存在数据
				MsaBiasValue msaBiasValue = mapper.selectOne(value);
				if (msaBiasValue != null) {
					if (!"-1".equals(request.getParameter("standardValue"))) {
						float standardValue = Float.parseFloat(request.getParameter("standardValue"));
						biasValue.setStandardValue(standardValue);
					}
					msaBiasValue.setMeasureDate(biasValue.getMeasureDate());
					msaBiasValue.setMeasureValue(biasValue.getMeasureValue());
					// 更新
					self().updateByPrimaryKeySelective(requestContext, msaBiasValue);
				} else {
					if (!"-1".equals(request.getParameter("standardValue"))) {
						float standardValue = Float.parseFloat(request.getParameter("standardValue"));
						biasValue.setStandardValue(standardValue);
					}
					// 新增
					self().insertSelective(requestContext, biasValue);
				}
			}
			try {
				workBook.close();
			} catch (Exception e) {
				
			}
		}

		return new ArrayList();
	}

	/**
	 * 空校验
	 * 
	 * @param i
	 * @param fieldName
	 */
	private void checkEmpty(String value, int i, String fieldName) {
		if (StringUtils.isEmpty(value)) {
			throw new RuntimeException("第" + (i + 1) + "行" + fieldName + "不能为空");
		}
	}

	/**
	 * 校验时间
	 * 
	 * @param dateStr
	 * @param rowNum
	 * @param message
	 * @return
	 */
	private Date dateFormat(String dateStr, int rowNum, String message) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
		Date date;
		try {
			date = sdf.parse(dateStr);
		} catch (ParseException e) {
			throw new RuntimeException("第" + rowNum + "行" + message + "格式必须为 yyyy/MM/dd");
		}
		return date;
	}

	/**
	 * 转换数字
	 * 
	 * @param measureNumStr
	 * @param rowNum
	 * @param message
	 * @return
	 */
	private float parseNumber(String measureNumStr, int rowNum, String message) {
		try {
			float value = Float.parseFloat(measureNumStr);
			return value;
		} catch (RuntimeException e) {
			e.printStackTrace();
			throw new RuntimeException("第" + rowNum + "行" + message + "必须为数字");
		}
	}

	private String importByExcelForDate(Cell currentCell) {
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

	@Override
	public void removeByMsaPlanId(IRequest requestContext, MsaBiasValue dto) {
		mapper.delete(dto);
	}
}