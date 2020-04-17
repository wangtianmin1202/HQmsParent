package com.hand.hqm.hqm_msa_grr_value.service.impl;

import com.hand.hap.core.IRequest;
import com.hand.hap.system.service.impl.BaseServiceImpl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hand.hqm.hqm_msa_consistency_value.dto.MsaConsistencyValue;
import com.hand.hqm.hqm_msa_grr_value.dto.MsaGrrValue;
import com.hand.hqm.hqm_msa_grr_value.mapper.MsaGrrValueMapper;
import com.hand.hqm.hqm_msa_grr_value.service.IMsaGrrValueService;
import com.hand.hqm.hqm_msa_plan.dto.MsaPlan;
import com.hand.hqm.hqm_msa_plan.service.IMsaPlanService;
import com.hand.hqm.hqm_utils.Finvs;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

@Service
@Transactional(rollbackFor = Exception.class)
public class MsaGrrValueServiceImpl extends BaseServiceImpl<MsaGrrValue> implements IMsaGrrValueService{
	
	@Autowired
	private IMsaPlanService msaPlanService;
	@Autowired
	private MsaGrrValueMapper mapper;
	
	@Override
	public List<MsaGrrValue> query(IRequest requestContext, MsaGrrValue dto) {
		List<MsaGrrValue> msaGrrValueList = mapper.select(dto);
		List<MsaGrrValue> list = new ArrayList();
		if(msaGrrValueList != null && msaGrrValueList.size() > 0) {
			//求列表头
			Set<String> sampleNumSet = msaGrrValueList
					.stream().collect(Collectors.groupingBy(MsaGrrValue :: getSampleNum)).keySet();
			List<String> smapleNumList = new ArrayList(sampleNumSet);
			smapleNumList = sortList(smapleNumList);
			//求列表行数
			Map<String,Map<Float,List<MsaGrrValue>>> map = new HashMap();
			//根据测量人分组
			Map<String,List<MsaGrrValue>> measureByMap = msaGrrValueList
					.stream().collect(Collectors.groupingBy(MsaGrrValue :: getMeasuredBy));
			for(Map.Entry<String, List<MsaGrrValue>> entry : measureByMap.entrySet()) {
				//根据测量次数分组
				Map<Float,List<MsaGrrValue>>  measureNumMap = entry.getValue()
						.stream().collect(Collectors.groupingBy(MsaGrrValue :: getMeasureNum));
				TreeMap treemap = new TreeMap(measureNumMap);
				map.put(entry.getKey(), treemap);
			}
			MsaGrrValue grrValue = new MsaGrrValue();
			grrValue.setMap(map);
			grrValue.setSampleNumList(smapleNumList);
			list.add(grrValue);
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
				if(Long.parseLong((o1)) > Long.parseLong((o2))) {
					return 1;
				}else if(Long.parseLong((o1)) < Long.parseLong((o2))) {
					return -1;
				}else {
					return 0;
				}
            }
		});
		return list;
	}
	@Override
	public List<MsaGrrValue> submit(IRequest requestContext, List<MsaGrrValue> dto) {
		MsaPlan masPlan = new MsaPlan();
		masPlan.setMsaPlanId(dto.get(0).getMsaPlanId());
		masPlan = msaPlanService.selectByPrimaryKey(requestContext, masPlan);
		if(masPlan == null) {
			throw new RuntimeException("计划信息不存在");
		}
		masPlan.setTolerance(dto.get(0).getTolerance());
		masPlan.setSampleDescription(dto.get(0).getSampleDescription());
		masPlan.setMeasureCharacter(dto.get(0).getMeasureCharacter());
		//更新计划信息
		msaPlanService.updateByPrimaryKeySelective(requestContext, masPlan);
		
		//GRR数据处理
		for(MsaGrrValue msaGrrValue : dto) {
			if(msaGrrValue.getMeasureValue() != null) {				
				if(msaGrrValue.getMsaGrrValueId() != null) {
					//更新
					MsaGrrValue grrValue = new MsaGrrValue();
					grrValue.setMsaGrrValueId(msaGrrValue.getMsaGrrValueId());
					grrValue.setMeasuredBy(msaGrrValue.getMeasuredBy());
					grrValue.setMeasureValue(msaGrrValue.getMeasureValue());
					grrValue.setSampleNum(msaGrrValue.getSampleNum());
					
					self().updateByPrimaryKeySelective(requestContext, grrValue);
				}else{
					//新增
					MsaGrrValue grrValue = new MsaGrrValue();
					grrValue.setMeasuredBy(msaGrrValue.getMeasuredBy());
					grrValue.setMeasureValue(msaGrrValue.getMeasureValue());
					grrValue.setSampleNum(msaGrrValue.getSampleNum());
					grrValue.setMeasureNum(msaGrrValue.getMeasureNum());
					grrValue.setMsaPlanId(msaGrrValue.getMsaPlanId());
					
					self().insertSelective(requestContext, grrValue);
				}
			}
		}
		return dto;
	}

	@Override
	public Double finvs(IRequest requestContext, MsaGrrValue dto) {
		double finv = -1;
		Finvs finvs = new Finvs();
		finv = finvs.Finv(dto.getA(), dto.getN1(), dto.getN2());
		return finv;
	}

	@Override
	public List<MsaGrrValue> excelImport(HttpServletRequest request, IRequest requestContext) throws Exception {
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		// 获取文件map集合
		Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
		
		float msaPlanId = Float.parseFloat(request.getParameter("msaPlanId"));
		for (Map.Entry<String, MultipartFile> entry : fileMap.entrySet()) {
			List<MsaGrrValue> msaGrrValueList = new ArrayList();
			
			MultipartFile forModel = entry.getValue();
			Sheet sheet;
			XSSFWorkbook workBook;
			workBook = new XSSFWorkbook(forModel.getInputStream());
			sheet = workBook.getSheetAt(0);
			
			for (int i = 1; i <= sheet.getLastRowNum(); i++) {
				MsaGrrValue msaGrrValue = new MsaGrrValue();
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
				float measureValue = parseNumber(row.getCell(3).toString(),(i + 1),"测量值");
			
				msaGrrValue.setMeasuredBy(measureBy);
				msaGrrValue.setSampleNum(sampleNum);
				msaGrrValue.setMeasureNum(measureNum);
				msaGrrValue.setMeasureValue(measureValue);
				msaGrrValue.setMsaPlanId(msaPlanId);
				
				msaGrrValueList.add(msaGrrValue);
			}
			//数据插入表
			for(MsaGrrValue msaGrrValue : msaGrrValueList) {
				MsaGrrValue value = new MsaGrrValue();
				value.setSampleNum(msaGrrValue.getSampleNum());
				value.setMeasuredBy(msaGrrValue.getMeasuredBy());
				value.setMeasureNum(msaGrrValue.getMeasureNum());
				value.setMsaPlanId(msaGrrValue.getMsaPlanId());
				//检查数据是否存在
				value = mapper.selectOne(value);
				if(value != null) {
					msaGrrValue.setMsaGrrValueId(value.getMsaGrrValueId());
					//更新
					self().updateByPrimaryKeySelective(requestContext, msaGrrValue);
				}else {
					//新增
					self().insertSelective(requestContext, msaGrrValue);
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
	public void removeByMsaPlanId(IRequest requestContext, MsaGrrValue dto) {
		// TODO Auto-generated method stub
		mapper.delete(dto);
	}
}