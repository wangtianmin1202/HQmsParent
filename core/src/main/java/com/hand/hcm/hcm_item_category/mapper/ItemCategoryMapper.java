package com.hand.hcm.hcm_item_category.mapper;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.hcm.hcm_item_category.dto.ItemCategory;

public interface ItemCategoryMapper extends Mapper<ItemCategory>{
	Float getIdByCode(String code);
}