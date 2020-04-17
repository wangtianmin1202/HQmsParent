package com.hand.hqm.hqm_sample_manage.mapper;

import java.util.List;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.hqm.hqm_sample_manage.dto.SampleManage;

public interface SampleManageMapper extends Mapper<SampleManage>{
	public List<SampleManage> querySampleManage(SampleManage sampleManage);
}