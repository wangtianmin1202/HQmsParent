package com.hand.hqm.hqm_sop_pqc_control_h.service.impl;
import java.util.List;
import com.github.pagehelper.PageHelper;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.service.impl.BaseServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.hand.hqm.hqm_sop_pqc_control_h.dto.SopPqcControlH;
import com.hand.hqm.hqm_sop_pqc_control_h.service.ISopPqcControlHService;
import com.hand.hqm.hqm_sop_pqc_control_h.mapper.SopPqcControlHMapper;


import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class SopPqcControlHServiceImpl extends BaseServiceImpl<SopPqcControlH> implements ISopPqcControlHService{
	@Autowired
	SopPqcControlHMapper sopPqcControlHMapper;
	 @Override
	 public List<SopPqcControlH> myselect(IRequest requestContext,SopPqcControlH dto,int page, int pageSize){
		 
		 PageHelper.startPage(page, pageSize);
	        return sopPqcControlHMapper.myselect(dto);
		 
	 }
}