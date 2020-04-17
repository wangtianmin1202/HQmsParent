package com.hand.hqm.hqm_standard_op_item_h_his.mapper;

import java.util.List;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.hqm.hqm_standard_op_item_h_his.dto.StandardOpItemHHis;

public interface StandardOpItemHHisMapper extends Mapper<StandardOpItemHHis>{

	/**
	 * 查询历史数据中离审批时间最近的数据
	 * @author kai.li
	 * @param dto
	 * @return
	 */
	List<StandardOpItemHHis> selectbyheadIdTime(StandardOpItemHHis dto);
}