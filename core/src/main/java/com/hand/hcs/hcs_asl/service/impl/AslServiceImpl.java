package com.hand.hcs.hcs_asl.service.impl;

import com.github.pagehelper.PageHelper;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.service.impl.BaseServiceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.hand.hcs.hcs_asl.dto.Asl;
import com.hand.hcs.hcs_asl.mapper.AslMapper;
import com.hand.hcs.hcs_asl.service.IAslService;
import com.hand.hcs.hcs_asl_control.dto.AslControl;
import com.hand.hcs.hcs_asl_control.service.IAslControlService;

import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class AslServiceImpl extends BaseServiceImpl<Asl> implements IAslService{
	@Autowired
	private AslMapper aslMapper;
	@Autowired
    private IAslControlService aslControlservice;
	
	
	@Override
	public List<Asl> query(IRequest requestContext, Asl asl, int page, int pageSize) {
		PageHelper.startPage(page, pageSize);
		return aslMapper.query(asl);
	}
	
	@Override
	public List<Asl> selectDatas(IRequest requestContext, Asl asl, int page, int pageSize) {
		PageHelper.startPage(page, pageSize);
		return aslMapper.selectDatas(asl);
	}
	
	@Override
	public List<Asl> IQCControlSelectDatas(IRequest requestContext, Asl asl, int page, int pageSize) {
		PageHelper.startPage(page, pageSize);
		return aslMapper.IQCControlSelectDatas(asl);
	}

	@Override
	public List<Asl> controlUpdate(IRequest requestContext, List<Asl> dto) {
		for(Asl asl : dto) {
			AslControl aslControl = new AslControl();
			aslControl.setAslId(asl.getAslId());
			aslControl.setLeadTime(asl.getLeadTime());
			aslControl.setUrgentLeadTime(asl.getUrgentLeadTime());
			aslControl.setTotalCapacity(asl.getTotalCapacity());
			aslControlservice.updateByPrimaryKeySelective(requestContext, aslControl);
		}
		return dto;
	}

}