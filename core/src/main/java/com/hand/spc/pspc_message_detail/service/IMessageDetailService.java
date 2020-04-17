package com.hand.spc.pspc_message_detail.service;

import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;
import com.hand.spc.pspc_message_detail.dto.MessageDetail;

public interface IMessageDetailService extends IBaseService<MessageDetail>, ProxySelf<IMessageDetailService> {

    /**
     * @param messageDetail 传入参数
     * @return : void
     * @Description: 数据删除 by detailId
     * @author: ywj
     * @date 2019/8/20 20:33
     * @version 1.0
     */
    void deleteByPrimaryId(MessageDetail messageDetail);
}