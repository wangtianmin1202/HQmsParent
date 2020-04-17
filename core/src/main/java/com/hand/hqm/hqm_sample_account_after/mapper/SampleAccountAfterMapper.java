package com.hand.hqm.hqm_sample_account_after.mapper;

import java.util.List;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.hqm.hqm_sample_account_after.dto.SampleAccountAfter;

public interface SampleAccountAfterMapper extends Mapper<SampleAccountAfter>{

	List<SampleAccountAfter> reSelect(SampleAccountAfter dto);

	/**
	 * @Description:查询报废内容
	 * @param dto
	 * @return
	 */
	List<SampleAccountAfter> selectBf(SampleAccountAfter dto);
	
	
	/**
	 * @Description:查询报废内容
	 * @param dto
	 * @return
	 */
	List<SampleAccountAfter> selectLy(SampleAccountAfter dto);

}