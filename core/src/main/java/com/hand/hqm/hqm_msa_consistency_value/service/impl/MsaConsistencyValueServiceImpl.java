package com.hand.hqm.hqm_msa_consistency_value.service.impl;

import com.hand.hap.core.IRequest;
import com.hand.hap.system.service.impl.BaseServiceImpl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.hand.hqm.hqm_msa_consistency_value.dto.MsaConsistencyValue;
import com.hand.hqm.hqm_msa_consistency_value.mapper.MsaConsistencyValueMapper;
import com.hand.hqm.hqm_msa_consistency_value.service.IMsaConsistencyValueService;
import com.hand.hqm.hqm_msa_plan.dto.MsaPlan;
import com.hand.hqm.hqm_msa_plan.service.IMsaPlanService;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

@Service
@Transactional(rollbackFor = Exception.class)
public class MsaConsistencyValueServiceImpl extends BaseServiceImpl<MsaConsistencyValue>
		implements IMsaConsistencyValueService {
	@Autowired
	private MsaConsistencyValueMapper mapper;
	@Autowired
	private IMsaPlanService msaPlanService;

	@Override
	public List<MsaConsistencyValue> query(IRequest requestContext, MsaConsistencyValue dto) {
		List<MsaConsistencyValue> msaConsistencyValueList = mapper.select(dto);
		List<MsaConsistencyValue> list = new ArrayList();
		if (msaConsistencyValueList != null && msaConsistencyValueList.size() > 0) {
			// 求列表头
			Map<String, Map<Float, List<MsaConsistencyValue>>> map = new HashMap();
			// 根据测量人分组
			Map<String, List<MsaConsistencyValue>> measureByMap = msaConsistencyValueList.stream()
					.collect(Collectors.groupingBy(MsaConsistencyValue::getMeasuredBy));
			for (Map.Entry<String, List<MsaConsistencyValue>> entry : measureByMap.entrySet()) {
				// 根据测量次数分组
				Map<Float, List<MsaConsistencyValue>> measureNumMap = entry.getValue().stream()
						.collect(Collectors.groupingBy(MsaConsistencyValue::getMeasureNum));
				TreeMap treemap = new TreeMap(measureNumMap);
				map.put(entry.getKey(), treemap);
			}

			// 求列表行
			Set<String> sampleAndStandardSet = msaConsistencyValueList.stream()
					.collect(Collectors.groupingBy(MsaConsistencyValue::getSampleAndStandard)).keySet();
			List<String> sampleAndStandardList = new ArrayList(sampleAndStandardSet);
			sampleAndStandardList = sortList(sampleAndStandardList);
			//Collections.sort(sampleAndStandardList);
			//零件和测量人分组
			Map<String, Map<Float, List<MsaConsistencyValue>>> sampleNumAndmeasuredByMap = new HashMap();
			//根据测量人分组
			Map<String, List<MsaConsistencyValue>> measureUserMap = msaConsistencyValueList.stream()
					.collect(Collectors.groupingBy(MsaConsistencyValue::getMeasuredBy));
			for (Map.Entry<String, List<MsaConsistencyValue>> entry : measureUserMap.entrySet()) {
				// 根据测sampleAndStandardList数分组
				Map<String, List<MsaConsistencyValue>> sampleNumMap  = entry.getValue().stream()
						.collect(Collectors.groupingBy(MsaConsistencyValue::getSampleNum));
				TreeMap treemap = new TreeMap(sampleNumMap);
				sampleNumAndmeasuredByMap.put(entry.getKey(), treemap);
			}
			
			//根据零件分组
			Map<String, List<MsaConsistencyValue>> bySampleNumMap = msaConsistencyValueList.stream()
					.collect(Collectors.groupingBy(MsaConsistencyValue::getSampleNum));
			
			MsaConsistencyValue consistencyValue = new MsaConsistencyValue();
			consistencyValue.setMap(map);
			consistencyValue.setSampleAndStandardList(sampleAndStandardList);
			consistencyValue.setSampleNumAndmeasuredByMap(sampleNumAndmeasuredByMap);
			consistencyValue.setSampleNumMap(bySampleNumMap);
			list.add(consistencyValue);
		}
		return list;
	}
	/**
	 * List<String> 排序  String格式： 1,abc
	 * 根据逗号前的数字升序
	 * @param list
	 * @return
	 */
	private List<String> sortList(List<String> list){
		Collections.sort(list,new Comparator<String>() {
			@Override
            public int compare(String o1, String o2) {
				if(Long.parseLong((o1.split(",")[0])) > Long.parseLong((o2.split(",")[0]))) {
					return 1;
				}else if(Long.parseLong((o1.split(",")[0])) < Long.parseLong((o2.split(",")[0]))) {
					return -1;
				}else {
					return 0;
				}
            }
		});
		return list;
	}
	@Override
	public List<MsaConsistencyValue> submit(IRequest requestContext, List<MsaConsistencyValue> dto) {
		MsaPlan masPlan = new MsaPlan();
		masPlan.setMsaPlanId(dto.get(0).getMsaPlanId());
		masPlan = msaPlanService.selectByPrimaryKey(requestContext, masPlan);
		if(masPlan == null) {
			throw new RuntimeException("计划信息不存在");
		}
		masPlan.setSampleDescription(dto.get(0).getSampleDescription());
		masPlan.setMeasureCharacter(dto.get(0).getMeasureCharacter());
		//更新计划信息
		msaPlanService.updateByPrimaryKeySelective(requestContext, masPlan);
		
		//一致性分析数据处理
		for(MsaConsistencyValue msaConsistencyValue : dto) {
			if(msaConsistencyValue.getMeasureValue() != null) {				
				if(msaConsistencyValue.getMsaConsistencyValueId() != null) {
					//更新
					MsaConsistencyValue consistencyValue = new MsaConsistencyValue();
					consistencyValue.setMsaConsistencyValueId(msaConsistencyValue.getMsaConsistencyValueId());
					consistencyValue.setMeasuredBy(msaConsistencyValue.getMeasuredBy());
					consistencyValue.setStandardValue(msaConsistencyValue.getStandardValue());
					consistencyValue.setMeasureValue(msaConsistencyValue.getMeasureValue());
					
					self().updateByPrimaryKeySelective(requestContext, consistencyValue);
				}else {
					//新增
					MsaConsistencyValue consistencyValue = new MsaConsistencyValue();
					consistencyValue.setMeasuredBy(msaConsistencyValue.getMeasuredBy());
					consistencyValue.setStandardValue(msaConsistencyValue.getStandardValue());
					consistencyValue.setMeasureValue(msaConsistencyValue.getMeasureValue());
					consistencyValue.setMsaPlanId(msaConsistencyValue.getMsaPlanId());
					consistencyValue.setMeasureNum(msaConsistencyValue.getMeasureNum());
					consistencyValue.setSampleNum(msaConsistencyValue.getSampleNum());
					
					self().insertSelective(requestContext, consistencyValue);
				}
			}
		}
		
		return dto;
	}

	@Override
	public List<MsaConsistencyValue> excelImport(HttpServletRequest request, IRequest requestContext) throws Exception {
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		// 获取文件map集合
		Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
		
		float msaPlanId = Float.parseFloat(request.getParameter("msaPlanId"));
		for (Map.Entry<String, MultipartFile> entry : fileMap.entrySet()) {
			List<MsaConsistencyValue> msaConsistencyValueList = new ArrayList();
			
			MultipartFile forModel = entry.getValue();
			Sheet sheet;
			XSSFWorkbook workBook;
			workBook = new XSSFWorkbook(forModel.getInputStream());
			sheet = workBook.getSheetAt(0);
			for (int i = 1; i <= sheet.getLastRowNum(); i++) {
				MsaConsistencyValue msaConsistencyValue = new MsaConsistencyValue();
				
				// i=1,从第二行开始
				Row row = sheet.getRow(i);
				
				//测量人
				checkEmpty(row.getCell(0).toString(), i, "测量人");
				String measureBy = row.getCell(0).toString();
				
				//零件编号
				checkEmpty(row.getCell(1).toString(), i, "零件编号");
				String sampleNum = row.getCell(1).toString().split("\\.")[0];
				
				//测量次数
				checkEmpty(row.getCell(2).toString(), i, "测量次数");
				float measureNum = parseNumber(row.getCell(2).toString(),(i + 1),"测量次数");
				
				//测量值
				checkEmpty(row.getCell(3).toString(), i, "测量值");
				String measureValue = row.getCell(3).toString();
				
				//基准值
				checkEmpty(row.getCell(4).toString(), i, "基准值");
				String standardValue = row.getCell(4).toString();
				
				msaConsistencyValue.setMeasuredBy(measureBy);
				msaConsistencyValue.setSampleNum(sampleNum);
				msaConsistencyValue.setMeasureNum(measureNum);
				msaConsistencyValue.setMeasureValue(measureValue);
				msaConsistencyValue.setStandardValue(standardValue);
				msaConsistencyValue.setMsaPlanId(msaPlanId);
				
				msaConsistencyValueList.add(msaConsistencyValue);
			}
			//数据插入表
			for(MsaConsistencyValue consistencyValue : msaConsistencyValueList) {
				MsaConsistencyValue value = new MsaConsistencyValue();
				value.setMsaPlanId(msaPlanId);
				value.setMeasuredBy(consistencyValue.getMeasuredBy());
				value.setSampleNum(consistencyValue.getSampleNum());
				value.setMeasureNum(consistencyValue.getMeasureNum());
				//检查是否存在数据
				value = mapper.selectOne(value);
				if(value != null) {
					value.setMeasureValue(consistencyValue.getMeasureValue());
					value.setStandardValue(consistencyValue.getStandardValue());
					//更新
					self().updateByPrimaryKeySelective(requestContext, value);
				}else {
					//新增
					self().insertSelective(requestContext, consistencyValue);
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
	public void removeByMsaPlanId(IRequest requestContext, MsaConsistencyValue dto) {
		// TODO Auto-generated method stub
		mapper.delete(dto);
	}
}