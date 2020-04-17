package com.hand.spc.ecr_main.mapper;

import java.util.Date;
import java.util.List;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.spc.ecr_main.dto.EcrDetail;
import com.hand.spc.ecr_main.view.EcrDetailsVO;
import org.apache.ibatis.annotations.Param;

public interface EcrDetailMapper extends Mapper<EcrDetail>{
	
	/**
	 * 库存明细查询
	 * @param dto
	 * @return
	 */
	 List<EcrDetail> inventoryDetailsQuery(EcrDetail dto);

	 Long stockQuery(@Param("itemId") Long itemId);

	 Date finalDateQuery(@Param("itemId") Long itemId);
	 
	 public EcrDetail getSumQtys(EcrDetail dto);
}
