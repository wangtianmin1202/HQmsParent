package com.hand.npi.npi_technology.service.impl;

import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.system.service.impl.BaseServiceImpl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hand.npi.npi_technology.dto.TechnologySpec;
import com.hand.npi.npi_technology.dto.TechnologyStandardAction;
import com.hand.npi.npi_technology.mapper.TechnologyStandardActionMapper;
import com.hand.npi.npi_technology.service.ITechnologyStandardActionService;

import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class TechnologyStandardActionServiceImpl extends BaseServiceImpl<TechnologyStandardAction> implements ITechnologyStandardActionService{
	
	
	@Autowired
	TechnologyStandardActionMapper technologyStandardActionMapper;

	@Override
	public ResponseData addNewTechnologyStandardAction(TechnologyStandardAction dto, IRequest requestCtx,
			HttpServletRequest request) {
		if (StringUtils.isBlank(dto.getTechnologyActionNumber())) {
			if(StringUtils.isNotEmpty(dto.getAssemblingDetail())){
				dto.getAssemblingDetail().replace("\n", "###");
			}
			
			String technologyActionNumber = "SA" ;
			technologyActionNumber = getTechnologyActionNumber(technologyActionNumber);
			dto.setTechnologyActionNumber(technologyActionNumber);
			
			insertSelective(requestCtx, dto);
		} else {
			updateByPrimaryKey(requestCtx, dto);
		}
		return new ResponseData(true);
	}
	
	public String getTechnologyActionNumber(String technologyActionNumber) {
		TechnologyStandardAction  technologyStandard = new TechnologyStandardAction();
		technologyStandard.setTechnologyActionNumber(technologyActionNumber);
		List<TechnologyStandardAction> technologyStandardList = technologyStandardActionMapper.selectMaxNumber(technologyStandard);
		if(technologyStandardList.isEmpty()) {
			technologyActionNumber = technologyActionNumber +"00001";
		}else {
			int intNumber = Integer.parseInt(technologyStandardList.get(0).getTechnologyActionNumber().substring(2));
			intNumber++;
			String stringNumber = String.valueOf(intNumber);
			for(int i =0;i<4;i++) {
				stringNumber = stringNumber.length() < 5 ? "0" + stringNumber : stringNumber;
			}
			technologyActionNumber = technologyActionNumber + stringNumber;
		}
		
		return technologyActionNumber;
	}

}