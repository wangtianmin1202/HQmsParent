package com.hand.hqm.hqm_sample_manage.service;

import java.util.List;

import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;
import com.hand.hqm.hqm_sample_manage.dto.SampleManage;

public interface ISampleManageService extends IBaseService<SampleManage>, ProxySelf<ISampleManageService>{
	public List<SampleManage> querySampleManage(SampleManage sampleManage);
}