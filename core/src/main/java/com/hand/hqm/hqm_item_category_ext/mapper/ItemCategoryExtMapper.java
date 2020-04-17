package com.hand.hqm.hqm_item_category_ext.mapper;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.hqm.hqm_item_category_ext.dto.ItemCategoryExt;

import java.util.List;

public interface ItemCategoryExtMapper extends Mapper<ItemCategoryExt>{

	/**
	 * 基础查询
	 * @description
	 * @author tianmin.wang
	 * @date 2019年11月22日 
	 * @param dto
	 * @return
	 */
    List<ItemCategoryExt> myselect(ItemCategoryExt dto);

    /**
     * 
     * @description
     * @author tianmin.wang
     * @date 2019年11月22日 
     * @param dto
     * @return
     */
	List<ItemCategoryExt> categroySelect(ItemCategoryExt dto);

	/**
	 * 条件分组查询
	 * @description
	 * @author tianmin.wang
	 * @date 2019年11月22日 
	 * @param dto
	 * @return
	 */
	List<ItemCategoryExt> selectGroup(ItemCategoryExt dto);
}