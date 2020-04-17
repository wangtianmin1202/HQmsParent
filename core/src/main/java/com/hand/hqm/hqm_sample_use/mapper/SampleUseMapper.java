package com.hand.hqm.hqm_sample_use.mapper;

import java.util.List;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.hqm.hqm_sample_use.dto.SampleUse;

public interface SampleUseMapper extends Mapper<SampleUse>{
	/**
	 * 基础查询
	 * @description
	 * @author tianmin.wang
	 * @date 2019年11月22日 
	 * @param dto
	 * @return
	 */
	 List<SampleUse> myselect(SampleUse dto);
	 
	 /**
	     * 查询历史
	     * @param dto 查询内容
	     * @return 结果集
	     */
	 List<SampleUse> select_his(SampleUse dto);
	 
	 /**
	  * 最大流水
	  * @description
	  * @author tianmin.wang
	  * @date 2019年11月22日 
	  * @param dto
	  * @return
	  */
	 List<SampleUse> selectMaxNumber(SampleUse dto);
}