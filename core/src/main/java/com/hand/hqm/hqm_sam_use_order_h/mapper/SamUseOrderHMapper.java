package com.hand.hqm.hqm_sam_use_order_h.mapper;

import java.util.List;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.hcs.hcs_delivery_ticket.dto.DeliveryTicketH;
import com.hand.hqm.hqm_nonconforming_order.dto.NonconformingOrder;
import com.hand.hqm.hqm_sam_use_order_h.dto.SamUseOrderH;
import com.hand.hqm.hqm_sample.dto.Sample;

public interface SamUseOrderHMapper extends Mapper<SamUseOrderH>{
	 /**
     * 行表数据查询
     * @param dto 查询内容
     * @return 结果集
     */
	List<SamUseOrderH> myselect(SamUseOrderH dto);
	/**
     * 根据主键查询头表数据
     * @param dto 查询内容
     * @return 结果集
     */
	List<SamUseOrderH> queryByTicketId(SamUseOrderH samUseOrderH);
	/**
     * 查询最大流水号流水
     * @param dto 查询内容
     * @return 结果集
     */
	List<SamUseOrderH> selectMaxNumber(SamUseOrderH dto);
}