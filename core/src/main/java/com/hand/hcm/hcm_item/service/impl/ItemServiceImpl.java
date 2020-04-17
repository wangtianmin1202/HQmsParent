package com.hand.hcm.hcm_item.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hand.hap.system.service.impl.BaseServiceImpl;
import com.hand.hcm.hcm_item.dto.Item;
import com.hand.hcm.hcm_item.mapper.ItemMapper;
import com.hand.hcm.hcm_item.service.IItemService;

@Service
@Transactional(rollbackFor = Exception.class)
public class ItemServiceImpl extends BaseServiceImpl<Item> implements IItemService{
	
	@Autowired
	private ItemMapper mapper;
	@Override
	public List<Item> query(Item item) {
		// TODO Auto-generated method stub
		return mapper.query(item);
	}

}