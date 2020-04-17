package com.hand.hqm.hqm_nonconforming_order.mapper;

import java.util.List;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.hqm.hqm_iqc_inspection_h.dto.IqcInspectionH;
import com.hand.hqm.hqm_nonconforming_order.dto.NonconformingOrder;
import com.hand.hqm.hqm_supp_item_exemption.dto.SuppItemExemption;

public interface NonconformingOrderMapper extends Mapper<NonconformingOrder>{
   List<NonconformingOrder> myselect(NonconformingOrder dto);
   List<NonconformingOrder> selectMaxNumber(NonconformingOrder dto);
}