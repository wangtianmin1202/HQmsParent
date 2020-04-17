package com.hand.spc.message_type_maintenance.mapper;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.hap.system.dto.ResponseData;
import com.hand.spc.message_type_maintenance.dto.MessageType;
import com.hand.spc.message_type_maintenance.dto.MessageTypeDetail;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MessageTypeDetailMapper extends Mapper<MessageTypeDetail>{

    /**
    * @Description 查找显示类型主题
    * @author hch
    * @date 2019/8/12 14:06
    * @Param [messageTypeId]
    * @return com.hand.hap.system.dto.ResponseData
    * @version 1.0
    */
    List<MessageTypeDetail> queryMessageType(@Param("messageTypeId") Float messageTypeId);

    List<MessageTypeDetail> queryMessageTypeContent(@Param("messageTypeId")Float messageTypeId);

    void deleteAll(@Param("messageTypeId")Float messageTypeId);

}