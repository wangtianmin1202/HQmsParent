package com.hand.npi.npi_route.mapper;

import java.util.List;
import java.util.Map;

import com.hand.hap.core.IRequest;
import com.hand.hap.mybatis.common.Mapper;
import com.hand.hcm.hcm_item_b.dto.ItemB;
import com.hand.npi.npi_route.dto.TechnologyWpAction;
import com.hand.npi.npi_technology.dto.EbomDetail;
import com.hand.npi.npi_technology.dto.TechnologySparePartDetails;
import com.hand.npi.npi_technology.dto.TechnologySpec;

public interface TechnologyWpActionMapper extends Mapper<TechnologyWpAction>{
	
	List<TechnologyWpAction> queryWpactionList(TechnologyWpAction dto);
	
	List<EbomDetail> queryItemLov(ItemB dto);

	List<TechnologySparePartDetails> getMatAttrByMat(TechnologyWpAction dto);
	List<TechnologySparePartDetails> getMatAttrByComp(TechnologyWpAction dto);
	
	List<TechnologySpec> getStrActionNumberByMat(String materielIds);
	
	/**
	 * 工艺动作的物料不在该sku的ebom中的 颜色为红色
	 */
	 List<TechnologyWpAction> queryData(TechnologyWpAction record);
	 
	 Map queryMaterielInfo(TechnologyWpAction user);
	 
	List<TechnologyWpAction> selectInfo(TechnologyWpAction dto);

}