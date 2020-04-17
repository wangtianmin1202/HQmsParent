package com.hand.hqm.hqm_sample_manage.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hand.hap.system.service.impl.BaseServiceImpl;
import com.hand.hqm.hqm_sample_manage.dto.SampleManage;
import com.hand.hqm.hqm_sample_manage.mapper.SampleManageMapper;
import com.hand.hqm.hqm_sample_manage.service.ISampleManageService;

@Service
@Transactional(rollbackFor = Exception.class)
public class SampleManageServiceImpl extends BaseServiceImpl<SampleManage> implements ISampleManageService{

	@Autowired
	private  SampleManageMapper sampleManageMapper;
	@Override
	public List<SampleManage> querySampleManage(SampleManage sampleManage) {
		return sampleManageMapper.querySampleManage(sampleManage);
	}

}