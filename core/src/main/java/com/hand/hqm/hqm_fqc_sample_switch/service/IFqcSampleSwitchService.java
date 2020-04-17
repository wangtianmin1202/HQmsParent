package com.hand.hqm.hqm_fqc_sample_switch.service;

import java.util.List;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;
import com.hand.hqm.hqm_fqc_sample_switch.dto.FqcSampleSwitch;

public interface IFqcSampleSwitchService extends IBaseService<FqcSampleSwitch>, ProxySelf<IFqcSampleSwitchService>{

	/**
	 * @description
	 * @author tianmin.wang
	 * @date 2020年3月6日 
	 * @param requestCtx
	 * @param dto
	 * @return
	 */
	List<FqcSampleSwitch> reBatchUpdate(IRequest requestCtx, List<FqcSampleSwitch> dto);

	/**
	 * @description
	 * @author tianmin.wang
	 * @date 2020年3月6日 
	 * @param request
	 * @param t
	 * @return
	 */
	FqcSampleSwitch insertSelectiveRecord(IRequest request, FqcSampleSwitch t);

	/**
	 * @description
	 * @author tianmin.wang
	 * @date 2020年3月6日 
	 * @param request
	 * @param t
	 * @return
	 */
	FqcSampleSwitch updateByPrimaryKeySelectiveRecord(IRequest request, FqcSampleSwitch t);

}