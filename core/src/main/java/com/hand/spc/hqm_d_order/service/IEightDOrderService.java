package com.hand.spc.hqm_d_order.service;

import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;
import com.hand.spc.hqm_d_order.dto.EightDOrder;

public interface IEightDOrderService extends IBaseService<EightDOrder>, ProxySelf<IEightDOrderService>{
    /**
     * @Author han.zhang
     * @Description 获得当前序列号 + 1
     * @Date 上午10:54 2019/10/17
     * @Param []
     **/
    Long getSeqInter();
}