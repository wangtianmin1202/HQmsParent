package com.hand.hqm.hqm_item_control.mapper;

import java.util.List;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.hqm.hqm_item_control.dto.ItemControlQms;

public interface ItemControlQmsMapper extends Mapper<ItemControlQms> {
	List<ItemControlQms> reSelect(ItemControlQms dto);

	/**
	 * @description 通过工厂的物料的唯一性更新数据
	 * @author tianmin.wang
	 * @date 2019年12月17日 
	 * @param search
	 */
	void updateByUniqueKey(ItemControlQms search);
}