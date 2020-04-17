package com.hand.hqm.hqm_position_analyst_rel.service.impl;

import com.github.pagehelper.PageHelper;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.service.impl.BaseServiceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.hand.hqm.hqm_position_analyst_rel.dto.PositionAnalystRel;
import com.hand.hqm.hqm_position_analyst_rel.mapper.PositionAnalystRelMapper;
import com.hand.hqm.hqm_position_analyst_rel.service.IPositionAnalystRelService;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class PositionAnalystRelServiceImpl extends BaseServiceImpl<PositionAnalystRel> implements IPositionAnalystRelService{
	
	@Autowired
	private PositionAnalystRelMapper mapper;
	
	@Override
	public List<PositionAnalystRel> query(IRequest requestContext, PositionAnalystRel dto, int page, int pageSize) {
		PageHelper.startPage(page, pageSize);
		return mapper.query(dto);
	}

}