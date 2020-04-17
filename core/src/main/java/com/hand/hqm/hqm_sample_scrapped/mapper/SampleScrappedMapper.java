package com.hand.hqm.hqm_sample_scrapped.mapper;

import java.util.List;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.hqm.hqm_sample_account.dto.SampleAccount;
import com.hand.hqm.hqm_sample_scrapped.dto.SampleScrapped;

public interface SampleScrappedMapper extends Mapper<SampleScrapped>{
	 /**
     * 查询最大流水号
     * @param dto 查询内容
     * @return 结果集
     */
	List<SampleScrapped> selectMaxNumber(SampleScrapped dto);
	 /**
     * 页面查询
     * @param dto 查询内容
     * @return 结果集
     */
	List<SampleScrapped> myselect(SampleScrapped dto);
	 /**
     * 测试样品页面查询
     * @param dto 查询内容
     * @return 结果集
     */
    List<SampleScrapped> myselectforcs(SampleScrapped dto);
}