package com.hand.npi.npi_technology.service.impl;

import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.system.service.impl.BaseServiceImpl;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hand.npi.npi_technology.dto.TechnologyAuxiliaryAction;
import com.hand.npi.npi_technology.dto.TechnologyStandardAction;
import com.hand.npi.npi_technology.mapper.TechnologyAuxiliaryActionMapper;
import com.hand.npi.npi_technology.service.ITechnologyAuxiliaryActionService;

import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class TechnologyAuxiliaryActionServiceImpl extends BaseServiceImpl<TechnologyAuxiliaryAction> implements ITechnologyAuxiliaryActionService{

	@Autowired
	TechnologyAuxiliaryActionMapper technologyAuxiliaryActionMapper;
	
	@Override
	public ResponseData addNewTechnologyAuxiliaryAction(List<TechnologyAuxiliaryAction> dto, IRequest requestCtx,
			HttpServletRequest request) {
		String auxiliaryActionNumber = "AX" ;
		auxiliaryActionNumber = getAuxiliaryActionNumber(auxiliaryActionNumber);
		dto.get(0).setAuxiliaryActionNumber(auxiliaryActionNumber);
		
		insertSelective(requestCtx, dto.get(0));
		return  new ResponseData(true);
	}
	
	public String getAuxiliaryActionNumber(String auxiliaryActionNumber) {
		TechnologyAuxiliaryAction  technologyAuxiliary = new TechnologyAuxiliaryAction();
		technologyAuxiliary.setAuxiliaryActionNumber(auxiliaryActionNumber);
		List<TechnologyAuxiliaryAction> technologyAuxiliaryActionList = technologyAuxiliaryActionMapper.selectMaxNumber(technologyAuxiliary);
		if(technologyAuxiliaryActionList.isEmpty()) {
			auxiliaryActionNumber = auxiliaryActionNumber +"00001";
		}else {
			int intNumber = Integer.parseInt(technologyAuxiliaryActionList.get(0).getAuxiliaryActionNumber().substring(2));
			intNumber++;
			String stringNumber = String.valueOf(intNumber);
			for(int i =0;i<4;i++) {
				stringNumber = stringNumber.length() < 5 ? "0" + stringNumber : stringNumber;
			}
			auxiliaryActionNumber = auxiliaryActionNumber + stringNumber;
		}
		
		return auxiliaryActionNumber;
	}

}