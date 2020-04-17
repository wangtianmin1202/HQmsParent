package com.hand.hcm.hcm_item_b.mapper;

import java.util.List;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.hcm.hcm_item_b.dto.ItemB;

public interface ItemBMapper extends Mapper<ItemB> {

	Float teselect(Float f);

	Float getSequence();
	
	List<ItemB> reSelect(ItemB dto);

	List<ItemB> interfaceSelect(ItemB dto);

	void refreshEnableFlag(ItemB ibsearch);
	
	void updateByItemPlantId(ItemB ibu);
	
	void updatePurchaseUomByItemPlantId(ItemB ibu);
	
	void disableByItemPlantId(ItemB ibu);
}