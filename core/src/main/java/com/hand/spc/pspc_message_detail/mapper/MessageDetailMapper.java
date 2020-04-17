package com.hand.spc.pspc_message_detail.mapper;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.spc.pspc_message_detail.dto.MessageDetail;

import java.util.List;

public interface MessageDetailMapper extends Mapper<MessageDetail> {

    /**
     * @param messageDetail 限制条件
     * @return : void
     * @Description: 数据删除
     * @author: ywj
     * @date 2019/8/20 20:33
     * @version 1.0
     */
    void deleteByPrimaryId(MessageDetail messageDetail);
}