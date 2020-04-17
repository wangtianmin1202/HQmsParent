package com.hand.hqm.hqm_msa_stability_value.service.impl;

import com.hand.hap.core.IRequest;
import com.hand.hap.system.service.impl.BaseServiceImpl;

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
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hand.hqm.hqm_msa_plan.dto.MsaPlan;
import com.hand.hqm.hqm_msa_plan.service.IMsaPlanService;
import com.hand.hqm.hqm_msa_stability_value.dto.MsaStabilityValue;
import com.hand.hqm.hqm_msa_stability_value.mapper.MsaStabilityValueMapper;
import com.hand.hqm.hqm_msa_stability_value.service.IMsaStabilityValueService;
import com.hand.utils.excelUtil.ImportDateFormatUtil;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

@Service
@Transactional(rollbackFor = Exception.class)
public class MsaStabilityValueServiceImpl extends BaseServiceImpl<MsaStabilityValue>
		implements IMsaStabilityValueService {

	@Autowired
	private MsaStabilityValueMapper mapper;
	@Autowired
	private IMsaPlanService msaPlanService;

	@Override
	public List<MsaStabilityValue> query(IRequest requestContext, MsaStabilityValue dto) {
		List<MsaStabilityValue> msaStabilityValueList = mapper.select(dto);

		if (msaStabilityValueList != null && msaStabilityValueList.size() > 0) {
			// 求列表头 (测量时间分组)
			Set<Date> measureDateSet = msaStabilityValueList.stream()
					.collect(Collectors.groupingBy(MsaStabilityValue::getMeasureDate)).keySet();
			List<Date> measureDateList = new ArrayList(measureDateSet);
			Collections.sort(measureDateList);
			// 求行表头 (测量次数分组)
			Set<Float> measureNumSet = msaStabilityValueList.stream()
					.collect(Collectors.groupingBy(MsaStabilityValue::getMeasureNum)).keySet();
			List<Float> measureNumList = new ArrayList(measureNumSet);
			Collections.sort(measureNumList);

			msaStabilityValueList.get(0).setMeasureDateList(measureDateList);
			msaStabilityValueList.get(0).setMeasureNumList(measureNumList);
		}

		return msaStabilityValueList;
	}

	@Override
	public List<MsaStabilityValue> submit(IRequest requestContext, List<MsaStabilityValue> dto) {
		MsaPlan masPlan = new MsaPlan();
		masPlan.setMsaPlanId(dto.get(0).getMsaPlanId());
		masPlan = msaPlanService.selectByPrimaryKey(requestContext, masPlan);
		if (masPlan == null) {
			throw new RuntimeException("计划信息不存在");
		}
		// 判断数据是否有变化
		/*
		 * if(masPlan.getMeasuredBy() != null &&
		 * dto.get(0).getMeasuredBy().equals(masPlan.getMeasuredBy()) &&
		 * masPlan.getSampleDescription() != null &&
		 * dto.get(0).getSampleDescription().equals(masPlan.getSampleDescription())) {
		 */
		masPlan.setMeasuredBy(dto.get(0).getMeasuredBy());
		masPlan.setSampleDescription(dto.get(0).getSampleDescription());
		// 更新计划信息
		msaPlanService.updateByPrimaryKeySelective(requestContext, masPlan);
		// }
		// 稳定性测量数据处理
		for (MsaStabilityValue msaStabilityValue : dto) {
			if(msaStabilityValue.getMeasureValue() != null) {				
				if (msaStabilityValue.getKid() != null) {
					// 更新
					MsaStabilityValue stabilityValue = new MsaStabilityValue();
					stabilityValue.setKid(msaStabilityValue.getKid());
					stabilityValue.setMeasureValue(msaStabilityValue.getMeasureValue());
					stabilityValue.setMeasureDate(msaStabilityValue.getMeasureDate());
					
					self().updateByPrimaryKeySelective(requestContext, stabilityValue);
				} else {
					// 新增
					MsaStabilityValue stabilityValue = new MsaStabilityValue();
					stabilityValue.setMeasureNum(msaStabilityValue.getMeasureNum());
					stabilityValue.setMeasureDate(msaStabilityValue.getMeasureDate());
					stabilityValue.setMeasureValue(msaStabilityValue.getMeasureValue());
					stabilityValue.setMsaPlanId(msaStabilityValue.getMsaPlanId());
					
					self().insertSelective(requestContext, stabilityValue);
				}
			}
		}
		return dto;
	}

	@Override
	public List<MsaStabilityValue> excelImport(HttpServletRequest request, IRequest requestContext) throws Exception {
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		// 获取文件map集合
		Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
		
		float msaPlanId = Float.parseFloat(request.getParameter("msaPlanId"));
		for (Map.Entry<String, MultipartFile> entry : fileMap.entrySet()) {
			List<MsaStabilityValue> msaStabilityValueList = new ArrayList();

			MultipartFile forModel = entry.getValue();
			Sheet sheet;
			XSSFWorkbook workBook;
			workBook = new XSSFWorkbook(forModel.getInputStream());
			sheet = workBook.getSheetAt(0);
			for (int i = 1; i <= sheet.getLastRowNum(); i++) {
				MsaStabilityValue msaStabilityValue = new MsaStabilityValue();
				// i=1,从第二行开始
				Row row = sheet.getRow(i);
				
				//测量时间
				checkEmpty(row.getCell(0).toString(), i, "测量时间");
				String dataStr = ImportDateFormatUtil.importByExcelForDate(row.getCell(0));
				Date measureDate = dateFormat(dataStr, (i + 1), "测量时间");
				
				//测量次数
				checkEmpty(row.getCell(1).toString(), i, "测量次数");
				float measureNum = parseNumber(row.getCell(1).toString(),(i + 1),"测量次数");
				
				//测量值
				checkEmpty(row.getCell(2).toString(), i, "测量值");
				float measureValue = parseNumber(row.getCell(2).toString(),(i + 1),"测量值");
				
				msaStabilityValue.setMeasureDate(measureDate);
				msaStabilityValue.setMeasureNum(measureNum);
				msaStabilityValue.setMeasureValue(measureValue);
				msaStabilityValue.setMsaPlanId(msaPlanId);
				
				msaStabilityValueList.add(msaStabilityValue);
			}
			//数据插入表
			for(MsaStabilityValue stabilityValut : msaStabilityValueList) {
				MsaStabilityValue msaStabilityValue = new MsaStabilityValue();
				msaStabilityValue.setMsaPlanId(msaPlanId);
				msaStabilityValue.setMeasureNum(stabilityValut.getMeasureNum());
				msaStabilityValue.setMeasureDate(stabilityValut.getMeasureDate());
				//检查是否存在数据
				MsaStabilityValue value = mapper.selectOne(msaStabilityValue);
				if(value!=null) {
					//更新
					value.setMeasureValue(stabilityValut.getMeasureValue());
					self().updateByPrimaryKeySelective(requestContext, value);
				}else {
					//新增
					self().insertSelective(requestContext, stabilityValut);
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
	 * @param i
	 * @param fieldName
	 */
	private void checkEmpty(String value, int i,String fieldName) {
		if (StringUtils.isEmpty(value)) {
			throw new RuntimeException("第" + (i+1) + "行"+ fieldName +"不能为空");
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
		Date date = new Date();
		try {
			date = sdf.parse(dateStr);
		} catch (ParseException e) {
			throw new RuntimeException("第" + rowNum + "行" + message + "格式必须为 yyyy/MM/dd");
		}
		return date;
	}
	/**
	 * 转换数字
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

	@Override
	public void removeByMsaPlanId(IRequest requestContext, MsaStabilityValue dto) {
		mapper.deleteByMsaPlanId(dto);
	}
}