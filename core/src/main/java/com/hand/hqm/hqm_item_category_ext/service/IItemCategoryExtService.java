package com.hand.hqm.hqm_item_category_ext.service;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;
import com.hand.hqm.hqm_item_category_ext.dto.ItemCategoryExt;

import java.util.List;

public interface IItemCategoryExtService extends IBaseService<ItemCategoryExt>, ProxySelf<IItemCategoryExtService>{
	/**
	 * 
	 * @description 基础查询
	 * @author tianmin.wang
	 * @date 2019年11月22日 
	 * @param requestContext
	 * @param dto
	 * @param page
	 * @param pageSize
	 * @return
	 */
    List<ItemCategoryExt> myselect(IRequest requestContext, ItemCategoryExt dto, int page, int pageSize);

	/**
	 * 
	 * @description 
	 * @author tianmin.wang
	 * @date 2019年11月22日 
	 * @param requestContext
	 * @param dto
	 * @return
	 */
	List<ItemCategoryExt> categoryquery(IRequest requestContext, ItemCategoryExt dto);

	/**
	 * 
	 * @description 分组条件查询
	 * @author tianmin.wang
	 * @date 2019年11月22日 
	 * @param requestContext
	 * @param dto
	 * @param page
	 * @param pageSize
	 * @return
	 */
	List<ItemCategoryExt> selectGroup(IRequest requestContext, ItemCategoryExt dto, int page, int pageSize);

	/**
	 * 批量删除
	 * @description
	 * @author tianmin.wang
	 * @date 2019年11月22日 
	 * @param dto
	 * @return
	 */
	int reBatchDelete(List<ItemCategoryExt> dto);
}