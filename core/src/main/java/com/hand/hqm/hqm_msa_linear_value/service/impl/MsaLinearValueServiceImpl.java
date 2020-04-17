package com.hand.hqm.hqm_msa_linear_value.service.impl;

import com.hand.hap.core.IRequest;
import com.hand.hap.system.service.impl.BaseServiceImpl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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

import com.hand.hqm.hqm_msa_grr_value.dto.MsaGrrValue;
import com.hand.hqm.hqm_msa_linear_value.dto.MsaLinearValue;
import com.hand.hqm.hqm_msa_linear_value.mapper.MsaLinearValueMapper;
import com.hand.hqm.hqm_msa_linear_value.service.IMsaLinearValueService;
import com.hand.hqm.hqm_msa_plan.dto.MsaPlan;
import com.hand.hqm.hqm_msa_plan.service.IMsaPlanService;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

@Service
@Transactional(rollbackFor = Exception.class)
public class MsaLinearValueServiceImpl extends BaseServiceImpl<MsaLinearValue> implements IMsaLinearValueService{
	
	@Autowired
	private MsaLinearValueMapper mapper;
	@Autowired
	private IMsaPlanService msaPlanService;
	
	@Override
	public List<MsaLinearValue> query(IRequest requestContext, MsaLinearValue dto) {
		List<MsaLinearValue> msaLinearValueList = mapper.select(dto);
		List<MsaLinearValue> list = new ArrayList();
		if(msaLinearValueList != null && msaLinearValueList.size() > 0) {
			//求列表头(根据零件编号和基准值分组)
			Set<String> sampleNumAndstandardValueSet = msaLinearValueList.stream().
					collect(Collectors.groupingBy(MsaLinearValue :: getSampleNumAndstandardValue)).keySet();
			List<String> sampleNumAndstandardValueList = new ArrayList(sampleNumAndstandardValueSet);
			sampleNumAndstandardValueList = sortList(sampleNumAndstandardValueList);
			//求列表行(根据测量次数分组)
			Map<Float,List<MsaLinearValue>> map = msaLinearValueList.stream()
					.collect(Collectors.groupingBy(MsaLinearValue :: getMeasureNum,TreeMap::new,Collectors.toList()));
			
			MsaLinearValue linerValue = new MsaLinearValue();
			linerValue.setMap(map);
			linerValue.setSampleNumAndstandardValueList(sampleNumAndstandardValueList);
			list.add(linerValue);
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
	public List<MsaLinearValue> submit(IRequest requestContext, List<MsaLinearValue> dto) {
		MsaPlan masPlan = new MsaPlan();
		masPlan.setMsaPlanId(dto.get(0).getMsaPlanId());
		masPlan = msaPlanService.selectByPrimaryKey(requestContext, masPlan);
		if(masPlan == null) {
			throw new RuntimeException("计划信息不存在");
		}
		masPlan.setMeasuredBy(dto.get(0).getMeasuredBy());
		masPlan.setSampleDescription(dto.get(0).getSampleDescription());
		masPlan.setExpectedDeterioration(dto.get(0).getExpectedDeterioration());
		//更新计划信息
		msaPlanService.updateByPrimaryKeySelective(requestContext, masPlan);
		
		//线性分析数据处理
		for(MsaLinearValue msaLinearValue : dto) {
			if(msaLinearValue.getMeasureValue() != null) {				
				if(msaLinearValue.getMsaLinearValueId() != null) {
					//更新
					MsaLinearValue linearValue = new MsaLinearValue();
					linearValue.setMsaLinearValueId(msaLinearValue.getMsaLinearValueId());
					linearValue.setMeasureValue(msaLinearValue.getMeasureValue());
					linearValue.setStandardValue(msaLinearValue.getStandardValue());
					linearValue.setBias(msaLinearValue.getMeasureValue() - msaLinearValue.getStandardValue());
					
					self().updateByPrimaryKeySelective(requestContext, linearValue);
				}else {
					//新增
					MsaLinearValue linearValue = new MsaLinearValue();
					linearValue.setMsaPlanId(msaLinearValue.getMsaPlanId());
					linearValue.setMeasureValue(msaLinearValue.getMeasureValue());
					linearValue.setStandardValue(msaLinearValue.getStandardValue());
					linearValue.setBias(msaLinearValue.getMeasureValue() - msaLinearValue.getStandardValue());
					linearValue.setSampleNum(msaLinearValue.getSampleNum());
					linearValue.setMeasureNum(msaLinearValue.getMeasureNum());
					
					self().insertSelective(requestContext, linearValue);
				}
			}
		}
		return dto;
	}

	@Override
	public List<MsaLinearValue> excelImport(HttpServletRequest request, IRequest requestContext) throws Exception {
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		// 获取文件map集合
		Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
		
		float msaPlanId = Float.parseFloat(request.getParameter("msaPlanId"));
		for (Map.Entry<String, MultipartFile> entry : fileMap.entrySet()) {
			List<MsaLinearValue> msaLinearValueList = new ArrayList();
			
			MultipartFile forModel = entry.getValue();
			Sheet sheet;
			XSSFWorkbook workBook;
			workBook = new XSSFWorkbook(forModel.getInputStream());
			sheet = workBook.getSheetAt(0);
			
			for (int i = 1; i <= sheet.getLastRowNum(); i++) {
				MsaLinearValue msaLinearValue = new MsaLinearValue();
				// i=1,从第二行开始
				Row row = sheet.getRow(i);
				
				//零件编号
				checkEmpty(row.getCell(0).toString(), i, "零件编号");
				String sampleNum = row.getCell(0).toString().split("\\.")[0];
				
				//基准值
				checkEmpty(row.getCell(1).toString(), i, "基准值");
				float standardValue = parseNumber(row.getCell(1).toString(),(i + 1),"基准值");
				
				//测量次数
				checkEmpty(row.getCell(2).toString(), i, "测量次数");
				float measureNum = parseNumber(row.getCell(2).toString(),(i + 1),"测量次数");
				
				//测量值
				checkEmpty(row.getCell(3).toString(), i, "测量值");
				float measureValue = parseNumber(row.getCell(3).toString(),(i + 1),"测量值");
			
				msaLinearValue.setSampleNum(sampleNum);
				msaLinearValue.setStandardValue(standardValue);
				msaLinearValue.setMeasureNum(measureNum);
				msaLinearValue.setMeasureValue(measureValue);
				msaLinearValue.setBias(measureValue - standardValue);
				msaLinearValue.setMsaPlanId(msaPlanId);
				
				msaLinearValueList.add(msaLinearValue);
			}
			//数据插入表
			for(MsaLinearValue linearValue : msaLinearValueList) {
				MsaLinearValue value = new MsaLinearValue();
				value.setMeasureNum(linearValue.getMeasureNum());
				value.setSampleNum(linearValue.getSampleNum());
				
				//检查数据是否存在
				value = mapper.selectOne(value);
				if(value != null) {
					MsaLinearValue lineValue = new MsaLinearValue();
					lineValue.setSampleNum(value.getSampleNum());
					lineValue.setStandardValue(value.getStandardValue());
					lineValue.setMsaPlanId(value.getMsaPlanId());
					if(!checkUn(lineValue)) {
						try {
							workBook.close();
						} catch (Exception e) {
							
						}
						throw new RuntimeException("同一个零件只能有一个基准值");
					}
					linearValue.setMsaLinearValueId(value.getMsaLinearValueId());
					//更新
					self().updateByPrimaryKeySelective(requestContext, linearValue);
				}else {
					//新增
					self().insertSelective(requestContext, linearValue);
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
	 * 校验唯一性（零件+基准值）
	 * @param linearValue
	 * @return
	 */
	private boolean checkUn(MsaLinearValue linearValue) {
		MsaLinearValue lineValue = new MsaLinearValue();
		lineValue.setMsaPlanId(linearValue.getMsaPlanId());
		lineValue.setSampleNum(linearValue.getSampleNum());
		List<MsaLinearValue> lineValueList = mapper.select(lineValue);
		
		List<MsaLinearValue> linearValueList = mapper.select(linearValue);
		if(linearValueList!=null && (lineValueList == null || lineValueList.size() == 0)) {
			return false;
		}else{
			return true;
		}
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
	public void removeByMsaPlanId(IRequest requestContext, MsaLinearValue dto) {
		// TODO Auto-generated method stub
		mapper.delete(dto);
	}
}