package com.hand.hcm.hcm_item.mapper;

import java.util.List;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.hcm.hcm_item.dto.Item;

public interface ItemMapper extends Mapper<Item>{
	/**
	 * 根据用户供应商、工厂查询物料
	 * @param item
	 * @return
	 */
	List<Item> itemLovByUser(Item item);
	
	/**
	 * guid
	 */
	String getGuid();
	/**
	 * 根据plantId itemId enableFlag = 'Y' 查询物料
	 * @param item
	 * @return 物料
	 */
	List<Item> query(Item item);
}