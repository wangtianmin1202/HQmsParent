package com.hand.dimension.hqm_dimension_immediate_actions_line.service.impl;

import com.hand.hap.core.IRequest;
import com.hand.hap.system.service.impl.BaseServiceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.hand.dimension.hqm_dimension_immediate_actions_line.dto.DimensionImmediateActionsLine;
import com.hand.dimension.hqm_dimension_immediate_actions_line.mapper.DimensionImmediateActionsLineMapper;
import com.hand.dimension.hqm_dimension_immediate_actions_line.service.IDimensionImmediateActionsLineService;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class DimensionImmediateActionsLineServiceImpl extends BaseServiceImpl<DimensionImmediateActionsLine> implements IDimensionImmediateActionsLineService{

	@Autowired
	DimensionImmediateActionsLineMapper mapper;
	@Override
	public List<DimensionImmediateActionsLine> reSelect(IRequest requestContext, DimensionImmediateActionsLine dto,
			int page, int pageSize) {
		return mapper.reSelect(dto);
	}

}