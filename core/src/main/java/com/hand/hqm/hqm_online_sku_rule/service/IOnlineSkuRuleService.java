package com.hand.hqm.hqm_online_sku_rule.service;

import java.util.List;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;
import com.hand.hqm.hqm_online_sku_rule.dto.OnlineSkuRule;
import com.hand.hqm.hqm_pqc_warning.dto.PqcWarning;

public interface IOnlineSkuRuleService extends IBaseService<OnlineSkuRule>, ProxySelf<IOnlineSkuRuleService>{

	/**
	 * @description
	 * @author tianmin.wang
	 * @date 2020年1月9日 
	 * @param requestContext
	 * @param dto
	 * @param page
	 * @param pageSize
	 * @return
	 */
	List<OnlineSkuRule> reSelect(IRequest requestContext, OnlineSkuRule dto, int page, int pageSize);

	/**
	 * @description
	 * @author tianmin.wang
	 * @date 2020年3月5日 
	 * @param requestCtx
	 * @param dto
	 * @return
	 */
	List<OnlineSkuRule> reBatchUpdate(IRequest requestCtx, List<OnlineSkuRule> dto);

	/**
	 * @description
	 * @author tianmin.wang
	 * @date 2020年3月5日 
	 * @param request
	 * @param t
	 * @return
	 */
	OnlineSkuRule updateByPrimaryKeySelectiveRecord(IRequest request, OnlineSkuRule t);

	/**
	 * @description
	 * @author tianmin.wang
	 * @date 2020年3月5日 
	 * @param request
	 * @param t
	 * @return
	 */
	OnlineSkuRule insertSelectiveRecord(IRequest request, OnlineSkuRule t);

}