package com.hand.hqm.hqm_sample_account.mapper;

import java.util.List;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.hqm.hqm_sample_account.dto.SampleAccount;
import com.hand.hqm.hqm_sample_scrapped.dto.SampleScrapped;

public interface SampleAccountMapper extends Mapper<SampleAccount>{
	/**
     * 页面查询
     * @param dto 查询内容
     */
	List<SampleAccount> myselect(SampleAccount dto);
	/**
     * 测试样品页面查询
     * @param dto 查询内容
     */
	List<SampleAccount> myselectforcs(SampleAccount dto);
	/**
     * 查询流水号最大流水
     * @param dto 查询内容
     */
	//获取最大编号后缀
	List<SampleAccount> selectMaxNumber(SampleAccount dto);
	/**
     * 查询流水号最大流水
     * @param dto 查询内容
     */
	List<SampleAccount> selectMaxCodeByDay(SampleAccount dto);
}