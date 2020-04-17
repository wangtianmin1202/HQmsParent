package com.hand.hqm.hqm_switch_score.service;

import java.util.List;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;
import com.hand.hqm.hqm_switch_score.dto.SwitchScore;

public interface ISwitchScoreService extends IBaseService<SwitchScore>, ProxySelf<ISwitchScoreService>{

	/**
	 * @description
	 * @author tianmin.wang
	 * @date 2019年12月13日 
	 * @param requestContext
	 * @param dto
	 * @param page
	 * @param pageSize
	 * @return
	 */
	List<SwitchScore> reSelect(IRequest requestContext, SwitchScore dto, int page, int pageSize);

}