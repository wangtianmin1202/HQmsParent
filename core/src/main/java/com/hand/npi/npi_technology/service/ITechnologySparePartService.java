package com.hand.npi.npi_technology.service;

import java.util.List;
import java.util.Map;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;
import com.hand.npi.npi_technology.dto.SparePartMenuItem;
import com.hand.npi.npi_technology.dto.TechnologySparePart;

public interface ITechnologySparePartService extends IBaseService<TechnologySparePart>, ProxySelf<ITechnologySparePartService>{

	/**
	 * @Description:主界面查询，树状结构
	 * @param requestContext
	 * @param dto
	 * @return
	 */
	List<SparePartMenuItem> queryTreeData(IRequest requestContext, TechnologySparePart dto);

	/**
	 * @Description:新增和编辑组件
	 * @param requestCtx
	 * @param dto
	 * @return
	 */
	List<TechnologySparePart> add(IRequest requestCtx, TechnologySparePart dto);
	
	/**
	 * @Description:根据主键删除零件组件
	 * @param requestCtx
	 * @param dto
	 * @return
	 */
	Map<String,Object> deleteSparePartById(IRequest requestCtx, TechnologySparePart dto);

}