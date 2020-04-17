package com.hand.hqm.hqm_sample_use_after.mapper;

import java.util.List;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.hqm.hqm_sample_use_after.dto.SampleUseAfter;

public interface SampleUseAfterMapper extends Mapper<SampleUseAfter>{
	
	/**
	 * 最大流水
	 * @description
	 * @author tianmin.wang
	 * @date 2019年11月22日 
	 * @param dto
	 * @return
	 */
	List<SampleUseAfter> selectMaxNumber(SampleUseAfter dto);

	
	/**
	 * 基础查询
	 * @description
	 * @author tianmin.wang
	 * @date 2019年11月22日 
	 * @param dto
	 * @return
	 */
	List<SampleUseAfter> reSelect(SampleUseAfter dto);
}