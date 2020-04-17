package com.hand.hqm.hqm_qua_ins_time_l.mapper;

import java.util.List;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.hqm.hqm_qua_ins_time_l.dto.QuaInsTimeL;
import com.hand.hqm.hqm_standard_op_ins_l.dto.StandardOpInspectionL;

public interface QuaInsTimeLMapper extends Mapper<QuaInsTimeL>{
	/**
     * 行表数据查询
     * @param dto 查询内容
     * @param page 页码
     * @param pageSize 页大小
     * @param request 请求
     * @return 结果集
     */
	List<QuaInsTimeL> myselect(QuaInsTimeL dto);
	
	List<QuaInsTimeL> groupbyselect(QuaInsTimeL dto);
	List<QuaInsTimeL> shiftNowQuery(QuaInsTimeL dto);
}