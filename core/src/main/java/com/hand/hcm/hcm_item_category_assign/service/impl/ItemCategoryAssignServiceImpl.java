package com.hand.hcm.hcm_item_category_assign.service.impl;

import com.hand.hap.system.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;
import com.hand.hcm.hcm_item_category_assign.dto.ItemCategoryAssign;
import com.hand.hcm.hcm_item_category_assign.service.IItemCategoryAssignService;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class ItemCategoryAssignServiceImpl extends BaseServiceImpl<ItemCategoryAssign> implements IItemCategoryAssignService{

}