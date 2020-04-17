package com.hand.hqm.hqm_online_sku_rule.mapper;

import java.util.List;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.hqm.hqm_online_sku_rule.dto.OnlineSkuRule;

public interface OnlineSkuRuleMapper extends Mapper<OnlineSkuRule>{

	/**
	 * @description
	 * @author tianmin.wang
	 * @date 2020年1月9日 
	 * @param dto
	 * @return
	 */
	List<OnlineSkuRule> reSelect(OnlineSkuRule dto);

}