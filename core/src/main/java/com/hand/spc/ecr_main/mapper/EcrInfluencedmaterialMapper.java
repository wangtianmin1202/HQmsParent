package com.hand.spc.ecr_main.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.hand.hap.core.IRequest;
import com.hand.hap.mybatis.common.Mapper;
import com.hand.spc.ecr_main.dto.EcrInfluencedmaterial;
import com.hand.spc.ecr_main.dto.EcrMain;
import com.hand.spc.ecr_main.view.EcrApsV0;
import com.hand.spc.ecr_main.view.EcrMainV0;
import com.hand.spc.ecr_main.view.EcrMaterialV0;

public interface EcrInfluencedmaterialMapper extends Mapper<EcrInfluencedmaterial>{
	public List<EcrMaterialV0> basequery(EcrInfluencedmaterial dto) ;
	public   List<EcrMaterialV0>  detailProductquery(EcrInfluencedmaterial dto);
	/*
	 * 主负责人维护进入界面的数据获取
	 */
	public List<EcrMainV0> dutySingleOrder( EcrMain dto);
	
	/*
	 * 获取计划数量
	 */
	public List<EcrApsV0> getApsData(@Param("itemIds") List<String> itemIds);
}