package com.hand.hqm.hqm_sample_scrapped_after.mapper;

import java.util.List;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.hqm.hqm_sample_scrapped_after.dto.SampleScrappedAfter;

public interface SampleScrappedAfterMapper extends Mapper<SampleScrappedAfter>{
	
	/**
	 * 最大流水
	 * @description
	 * @author tianmin.wang
	 * @date 2019年11月22日 
	 * @param dto
	 * @return
	 */
	List<SampleScrappedAfter> selectMaxNumber(SampleScrappedAfter dto);
	
	/**
	 * 基础查询
	 * @description
	 * @author tianmin.wang
	 * @date 2019年11月22日 
	 * @param dto
	 * @return
	 */
	List<SampleScrappedAfter> reSelect(SampleScrappedAfter dto);
}