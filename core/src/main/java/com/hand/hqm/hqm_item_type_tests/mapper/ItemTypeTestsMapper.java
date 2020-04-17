package com.hand.hqm.hqm_item_type_tests.mapper;

import java.util.List;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.hqm.hqm_item_type_tests.dto.ItemTypeTests;

public interface ItemTypeTestsMapper extends Mapper<ItemTypeTests> {

	List<ItemTypeTests> reSelect(ItemTypeTests dto);
}