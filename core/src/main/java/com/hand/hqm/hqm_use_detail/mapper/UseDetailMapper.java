package com.hand.hqm.hqm_use_detail.mapper;

import java.util.List;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.hqm.hqm_use_detail.dto.UseDetail;

public interface UseDetailMapper extends Mapper<UseDetail>{
	/**
     * 查询
     * @param dto 查询内容
     * @param request 请求
     * @return 结果集
     */
	List<UseDetail> myselect(UseDetail dto);
	/**
     * 样品查询
     * @param dto 查询内容
     * @param request 请求
     * @return 结果集
     */
	List<UseDetail> selectforsample(UseDetail dto);
	
}