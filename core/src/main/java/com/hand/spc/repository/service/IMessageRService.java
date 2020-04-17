package com.hand.spc.repository.service;

import java.util.List;

import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;
import com.hand.spc.repository.dto.MessageR;

public interface IMessageRService extends IBaseService<MessageR>, ProxySelf<IMessageRService> {

    /**
     * 批量保存消息
     *
     * @param messageList
     * @return
     */
    public int batchInsertRows(List<MessageR> messageList);



}
