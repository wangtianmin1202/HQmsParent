package com.hand.hqm.hqm_scrapped_detail_after.mapper;

import java.util.List;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.hqm.hqm_scrapped_detail_after.dto.ScrappedDetailAfter;

public interface ScrappedDetailAfterMapper extends Mapper<ScrappedDetailAfter>{
	
	/**
	 * 基础查询
	 * @description
	 * @author tianmin.wang
	 * @date 2019年11月22日 
	 * @param dto
	 * @return
	 */
	List<ScrappedDetailAfter> reSelect(ScrappedDetailAfter dto);

}