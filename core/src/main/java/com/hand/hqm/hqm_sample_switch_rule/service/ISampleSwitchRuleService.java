package com.hand.hqm.hqm_sample_switch_rule.service;

import java.util.List;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;
import com.hand.hqm.hqm_sample_switch_rule.dto.SampleSwitchRule;

public interface ISampleSwitchRuleService extends IBaseService<SampleSwitchRule>, ProxySelf<ISampleSwitchRuleService>{

	/**
	 * @description
	 * @author tianmin.wang
	 * @date 2020年3月6日 
	 * @param requestCtx
	 * @param dto
	 * @return
	 */
	List<SampleSwitchRule> reBatchUpdate(IRequest requestCtx, List<SampleSwitchRule> dto);

	/**
	 * @description
	 * @author tianmin.wang
	 * @date 2020年3月6日 
	 * @param request
	 * @param t
	 * @return
	 */
	SampleSwitchRule insertSelectiveRecord(IRequest request, SampleSwitchRule t);

	/**
	 * @description
	 * @author tianmin.wang
	 * @date 2020年3月6日 
	 * @param request
	 * @param t
	 * @return
	 */
	SampleSwitchRule updateByPrimaryKeySelectiveRecord(IRequest request, SampleSwitchRule t);

}