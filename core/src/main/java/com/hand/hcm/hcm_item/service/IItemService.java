package com.hand.hcm.hcm_item.service;

import java.util.List;

import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;
import com.hand.hcm.hcm_item.dto.Item;

public interface IItemService extends IBaseService<Item>, ProxySelf<IItemService>{
	/**
	 * 根据plantId itemId enableFlag = 'Y' 查询物料
	 * @param item
	 * @return 物料
	 */
	List<Item> query(Item item);
}