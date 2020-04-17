package com.hand.npi.npi_route.service.impl;

import com.hand.hap.core.IRequest;
import com.hand.hap.system.service.impl.BaseServiceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hand.npi.npi_route.dto.TechnologyWpAction;
import com.hand.npi.npi_route.dto.TechnologyWpActionEquipDetail;
import com.hand.npi.npi_route.dto.TechnologyWpStandardActionDetail;
import com.hand.npi.npi_route.mapper.TechnologyWpActionEquipDetailMapper;
import com.hand.npi.npi_route.mapper.TechnologyWpStandardActionDetailMapper;
import com.hand.npi.npi_route.service.ITechnologyWpStandardActionDetailService;

import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class TechnologyWpStandardActionDetailServiceImpl extends BaseServiceImpl<TechnologyWpStandardActionDetail> implements ITechnologyWpStandardActionDetailService{

	@Autowired
	TechnologyWpStandardActionDetailMapper technologyWpStandardActionDetailMapper;
	
	@Autowired
	TechnologyWpActionEquipDetailMapper technologyWpActionEquipDetailMapper;
	
	@Override
	public List<TechnologyWpStandardActionDetail> queryActionInfo(IRequest request, TechnologyWpAction condition) {
		// TODO Auto-generated method stub
		List<TechnologyWpStandardActionDetail> queryActionInfo = technologyWpStandardActionDetailMapper.queryActionInfo(condition);
		TechnologyWpActionEquipDetail equipDetail=new TechnologyWpActionEquipDetail();
		for (TechnologyWpStandardActionDetail dto : queryActionInfo) {
			equipDetail.setWpActionId(dto.getWpStdActDetailId());
			//查询出他的工装设备
			List<TechnologyWpActionEquipDetail> select2 = technologyWpActionEquipDetailMapper.selectInfoSta(equipDetail);
			dto.setEquipList(select2);
		}
		return queryActionInfo;
	}

}