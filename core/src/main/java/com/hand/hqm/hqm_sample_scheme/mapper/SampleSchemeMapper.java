package com.hand.hqm.hqm_sample_scheme.mapper;

import java.util.List;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.hqm.hqm_sample_scheme.dto.SampleScheme;

public interface SampleSchemeMapper extends Mapper<SampleScheme>{

	List<SampleScheme> selectSampleSize(SampleScheme dto);
	/**
     * 页面查询
     * @param dto 查询内容
     * @return 结果集
     */
	List<SampleScheme> reSelect(SampleScheme dto);
}