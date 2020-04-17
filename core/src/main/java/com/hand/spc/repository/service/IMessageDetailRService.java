package com.hand.spc.repository.service;

import java.util.List;

import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;
import com.hand.spc.repository.dto.MessageDetailR;

public interface IMessageDetailRService extends IBaseService<MessageDetailR>, ProxySelf<IMessageDetailRService> {

    /**
     * 批量保存消息明细
     *
     * @param messageDetailList
     * @return
     */
    public int batchInsertRows(List<MessageDetailR> messageDetailList);

}
