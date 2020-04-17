package com.hand.hqm.hqm_switch_score.mapper;

import java.util.List;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.hqm.hqm_switch_score.dto.SwitchScore;

public interface SwitchScoreMapper extends Mapper<SwitchScore>{

	/**
	 * @description 基础查询
	 * @author tianmin.wang
	 * @date 2019年12月13日 
	 * @param dto
	 * @return
	 */
	List<SwitchScore> reSelect(SwitchScore dto);
	
}