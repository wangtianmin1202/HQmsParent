package com.hand.hqm.hqm_item_category_ext.service.impl;

import com.github.pagehelper.PageHelper;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import com.hand.hqm.hqm_item_category_ext.mapper.ItemCategoryExtMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.hand.hqm.hqm_item_category_ext.dto.ItemCategoryExt;
import com.hand.hqm.hqm_item_category_ext.service.IItemCategoryExtService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class ItemCategoryExtServiceImpl extends BaseServiceImpl<ItemCategoryExt> implements IItemCategoryExtService{

    @Autowired
    ItemCategoryExtMapper itemCategoryExtMapper;
    @Override
    public List<ItemCategoryExt> myselect(IRequest requestContext, ItemCategoryExt dto, int page, int pageSize) {
        PageHelper.startPage(page, pageSize);
        return itemCategoryExtMapper.myselect(dto);
    }
	@Override
	public List<ItemCategoryExt> categoryquery(IRequest requestContext, ItemCategoryExt dto) {
		// TODO Auto-generated method stub
		return itemCategoryExtMapper.categroySelect(dto);
	}
	@Override
	public List<ItemCategoryExt> selectGroup(IRequest requestContext, ItemCategoryExt dto, int page, int pageSize) {
		// TODO Auto-generated method stub
		PageHelper.startPage(page, pageSize);
        return itemCategoryExtMapper.selectGroup(dto);
	}
	@Override
	public int reBatchDelete(List<ItemCategoryExt> dto) {
		// TODO Auto-generated method stub
		int count = 0;
		for(ItemCategoryExt itemCategoryExt : dto) {
		ItemCategoryExt sea = new ItemCategoryExt();
		sea.setItemCategory(itemCategoryExt.getItemCategory());
		sea.setSourceType(itemCategoryExt.getSourceType());
		List<ItemCategoryExt> deleteList = itemCategoryExtMapper.select(sea);
		count += self().batchDelete(deleteList);
		}
		return count;
	}
}