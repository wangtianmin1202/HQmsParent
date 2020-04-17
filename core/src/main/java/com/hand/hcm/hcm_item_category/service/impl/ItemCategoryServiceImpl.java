package com.hand.hcm.hcm_item_category.service.impl;

import com.hand.hap.system.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;
import com.hand.hcm.hcm_item_category.dto.ItemCategory;
import com.hand.hcm.hcm_item_category.service.IItemCategoryService;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class ItemCategoryServiceImpl extends BaseServiceImpl<ItemCategory> implements IItemCategoryService{

}