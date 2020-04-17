package com.hand.hqm.hqm_position_analyst_rel.service;

import java.util.List;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;
import com.hand.hqm.hqm_position_analyst_rel.dto.PositionAnalystRel;

public interface IPositionAnalystRelService extends IBaseService<PositionAnalystRel>, ProxySelf<IPositionAnalystRelService>{
	/**
	 * 关键岗位与分析人关系维护  查询
	 * @param requestContext
	 * @param dto
	 * @param page
	 * @param pageSize
	 * @return
	 */
	List<PositionAnalystRel> query(IRequest requestContext,PositionAnalystRel dto, int page, int pageSize);
}