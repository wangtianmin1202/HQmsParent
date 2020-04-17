package com.hand.npi.npi_technology.service.impl;

import com.github.pagehelper.PageHelper;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.service.impl.BaseServiceImpl;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.hand.npi.npi_technology.dto.InvalidPatternHis;
import com.hand.npi.npi_technology.mapper.InvalidPatternHisMapper;
import com.hand.npi.npi_technology.service.IInvalidPatternHisService;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class InvalidPatternHisServiceImpl extends BaseServiceImpl<InvalidPatternHis> implements IInvalidPatternHisService{
	@Autowired
	InvalidPatternHisMapper invalidPatternHisMapper;
	
	/**
	 * @author likai 2020.03.26
	 * @description 查询历史数据
	 * @param dto
	 * @param page
	 * @param pageSize
	 * @param requestCtx
	 * @param request
	 * @return
	 */
	@Override
	public List<InvalidPatternHis> selectByLastUpdateDate(InvalidPatternHis dto, int page, int pageSize, IRequest requestCtx,
			HttpServletRequest request) {
		PageHelper.startPage(page, pageSize);
		return invalidPatternHisMapper.selectByLastUpdateDate(dto);
	}
	
}