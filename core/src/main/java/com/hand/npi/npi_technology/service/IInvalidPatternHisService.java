package com.hand.npi.npi_technology.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;
import com.hand.npi.npi_technology.dto.InvalidPatternHis;

public interface IInvalidPatternHisService extends IBaseService<InvalidPatternHis>, ProxySelf<IInvalidPatternHisService>{

	/**
	 * @author likai 2020.03.26
	 * @description 查询历史数据
	 * @param dto
	 * @param page
	 * @param pageSize
	 * @param requestCtx
	 * @param request
	 * @return
	 */
	List<InvalidPatternHis> selectByLastUpdateDate(InvalidPatternHis dto, int page, int pageSize, IRequest requestCtx,
			HttpServletRequest request);
}