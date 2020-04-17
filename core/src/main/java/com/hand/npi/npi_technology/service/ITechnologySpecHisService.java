package com.hand.npi.npi_technology.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.system.service.IBaseService;
import com.hand.npi.npi_technology.dto.TechnologySpecHis;

public interface ITechnologySpecHisService extends IBaseService<TechnologySpecHis>, ProxySelf<ITechnologySpecHisService>{

	/**
	 * @author likai 2020.03.21
	 * @description 查询组装动作历史数据
	 * @param dto
	 * @param page
	 * @param pageSize
	 * @param requestCtx
	 * @param request
	 * @return
	 */
	 List<TechnologySpecHis> selectByLastUpdateDate(TechnologySpecHis dto, int page, int pageSize, IRequest requestCtx,
			HttpServletRequest request);
	 
	 /**
	 * @author likai 2020.03.23
	 * @description 发起历史变更 新增或修改
	 * @param request
	 * @param list
	 * @return
	 */
	 ResponseData addOrEditData(IRequest request, List<TechnologySpecHis> list);
	
}