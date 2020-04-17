package com.hand.hqm.hqm_scrapped_detail_after.service.impl;

import com.github.pagehelper.PageHelper;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.service.impl.BaseServiceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.hand.hqm.hqm_scrapped_detail_after.dto.ScrappedDetailAfter;
import com.hand.hqm.hqm_scrapped_detail_after.mapper.ScrappedDetailAfterMapper;
import com.hand.hqm.hqm_scrapped_detail_after.service.IScrappedDetailAfterService;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class ScrappedDetailAfterServiceImpl extends BaseServiceImpl<ScrappedDetailAfter> implements IScrappedDetailAfterService{

	@Autowired
	ScrappedDetailAfterMapper mapper;
	
	@Override
	public ScrappedDetailAfter addOne(IRequest requestCtx,ScrappedDetailAfter dto) {
		// TODO Auto-generated method stub
		self().insertSelective(requestCtx, dto);
		return dto;
	}

	@Override
	public List<ScrappedDetailAfter> reSelect(IRequest requestContext, ScrappedDetailAfter dto, int page,
			int pageSize) {
		// TODO Auto-generated method stub
		PageHelper.startPage(page, pageSize);
		return mapper.reSelect(dto);
	}

}