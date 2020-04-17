package com.hand.hqm.hqm_use_detail_after.service.impl;

import com.github.pagehelper.PageHelper;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.service.impl.BaseServiceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.hand.hqm.hqm_use_detail_after.dto.UseDetailAfter;
import com.hand.hqm.hqm_use_detail_after.mapper.UseDetailAfterMapper;
import com.hand.hqm.hqm_use_detail_after.service.IUseDetailAfterService;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class UseDetailAfterServiceImpl extends BaseServiceImpl<UseDetailAfter> implements IUseDetailAfterService{

	@Autowired
	UseDetailAfterMapper mapper;
	
	@Override
	public List<UseDetailAfter> reSelect(IRequest requestContext, UseDetailAfter dto, int page, int pageSize) {
		// TODO Auto-generated method stub
		PageHelper.startPage(page, pageSize);
		return mapper.reSelect(dto);
	}

	@Override
	public UseDetailAfter addOne(IRequest requestCtx, UseDetailAfter dto) {
		// TODO Auto-generated method stub
		self().insertSelective(requestCtx, dto);
		return dto;
	}

}