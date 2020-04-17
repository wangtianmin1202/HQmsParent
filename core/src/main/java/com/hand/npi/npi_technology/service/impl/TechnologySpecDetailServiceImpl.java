package com.hand.npi.npi_technology.service.impl;

import com.github.pagehelper.PageHelper;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.service.impl.BaseServiceImpl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hand.npi.npi_technology.dto.SopRouteRef;
import com.hand.npi.npi_technology.dto.TechnologySpec;
import com.hand.npi.npi_technology.dto.TechnologySpecDetail;
import com.hand.npi.npi_technology.dto.TechnologySpecHis;
import com.hand.npi.npi_technology.mapper.SopRouteRefMapper;
import com.hand.npi.npi_technology.mapper.TechnologySpecDetailMapper;
import com.hand.npi.npi_technology.mapper.TechnologySpecHisMapper;
import com.hand.npi.npi_technology.mapper.TechnologySpecMapper;
import com.hand.npi.npi_technology.mapper.TechnologySpecMatDetailMapper;
import com.hand.npi.npi_technology.service.ITechnologySpecDetailService;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class TechnologySpecDetailServiceImpl extends BaseServiceImpl<TechnologySpecDetail> implements ITechnologySpecDetailService{

	@Autowired
	private TechnologySpecDetailMapper technologySpecDetailMapper;
	@Autowired
	TechnologySpecMapper technologySpecMapper;
	@Autowired
	TechnologySpecHisMapper technologySpecHisMapper;
	
	@Override
	public List<TechnologySpecDetail> queryData(IRequest requestContext, TechnologySpecDetail dto, int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		
		if(dto.getSpecVersion() == null || "".equals(dto.getSpecVersion())) {
			TechnologySpecHis technologySpecHis = new TechnologySpecHis();
			technologySpecHis.setSpecId(dto.getSpecId());
			List<TechnologySpecHis> list = technologySpecHisMapper.selectByLastUpdateDate(technologySpecHis);
			if(list != null && list.size() > 0) {
				dto.setSpecVersion(list.get(0).getSpecVersion());
			}
		}
		List<TechnologySpecDetail>  dataQuery = technologySpecDetailMapper.dataQuery(dto);
		
		return dataQuery;
	}


	@Override
	public List<TechnologySpecDetail> queryBySkuCode(IRequest requestContext, TechnologySpecDetail dto, int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		List<TechnologySpecDetail> specDetails = new ArrayList<TechnologySpecDetail>();/*technologySpecDetailMapper.selectSpecDetailBySkuCode(dto);*/
		return specDetails;
	}


}