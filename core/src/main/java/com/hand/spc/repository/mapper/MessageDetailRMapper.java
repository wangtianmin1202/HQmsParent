package com.hand.spc.repository.mapper;

import java.util.List;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.spc.repository.dto.MessageDetailR;

public interface MessageDetailRMapper extends Mapper<MessageDetailR> {

    /**
     * 批量保存消息明细
     *
     * @param messageDetailList
     * @return
     */
    public int batchInsertRows(List<MessageDetailR> messageDetailList);
}
