package com.hand.npi.npi_technology.service.impl;

import com.github.pagehelper.PageHelper;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.system.service.impl.BaseServiceImpl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hand.npi.npi_technology.dto.InvalidCause;
import com.hand.npi.npi_technology.dto.PreventionMeasure;
import com.hand.npi.npi_technology.mapper.PreventionMeasureMapper;
import com.hand.npi.npi_technology.service.IPreventionMeasureService;

import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class PreventionMeasureServiceImpl extends BaseServiceImpl<PreventionMeasure> implements IPreventionMeasureService{
	
	@Autowired
	PreventionMeasureMapper preventionMeasureMapper;

	@Override
	public ResponseData addNewPreventionMeasure(PreventionMeasure dto, IRequest requestCtx,
			HttpServletRequest request) {
		if ("add".equals(dto.get__status())) {
			Date date = new Date();
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMM");
			String time = dateFormat.format(date);
			String preventionMeasureNumber = "PM" + time;
			preventionMeasureNumber = getPreventionMeasureNumber(preventionMeasureNumber);
			dto.setPreventionMeasureNumber(preventionMeasureNumber);
			
			insertSelective(requestCtx, dto);
		} else if ("update".equals(dto.get__status())) {
			updateByPrimaryKey(requestCtx, dto);
		}
		return new ResponseData(true);
	}
	
	@Override
	public String getPreventionMeasureNumber(String preventionMeasureNumber) {
		PreventionMeasure  preventionMeasure = new PreventionMeasure();
		preventionMeasure.setPreventionMeasureNumber(preventionMeasureNumber);
		List<PreventionMeasure> preventionMeasureList = preventionMeasureMapper.selectMaxNumber(preventionMeasure);
		if(preventionMeasureList.isEmpty()) {
			preventionMeasureNumber = preventionMeasureNumber +"0001";
		}else {
			int intNumber = Integer.parseInt(preventionMeasureList.get(0).getPreventionMeasureNumber().substring(8));
			intNumber++;
			String stringNumber = String.valueOf(intNumber);
			for(int i =0;i<3;i++) {
				stringNumber = stringNumber.length() < 4 ? "0" + stringNumber : stringNumber;
			}
			preventionMeasureNumber = preventionMeasureNumber + stringNumber;
		}
		
		return preventionMeasureNumber;
	}

	@Override
	public List<PreventionMeasure> queryPreventionMeasureList(PreventionMeasure dto) {
		return preventionMeasureMapper.queryPreventionMeasureList(dto);
	}

	@Override
	public List<PreventionMeasure> queryByPatId(IRequest request, PreventionMeasure condition, int pageNum,
			int pageSize) {
		// 通过parrentId找到预防措施
		PageHelper.startPage(pageNum, pageSize);
		List<PreventionMeasure> queryByPatId = preventionMeasureMapper.queryByPatId(condition);
		return queryByPatId;
	}

}