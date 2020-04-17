package com.hand.spc.hqm_d_order.mapper;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.spc.hqm_d_order.dto.EightDOrder;

public interface EightDOrderMapper extends Mapper<EightDOrder>{
    /**
     * @Author han.zhang
     * @Description 获得当前日期最大序列号 + 1
     * @Date 上午10:54 2019/10/17
     * @Param []
     **/
    Long getSeqInter();
}