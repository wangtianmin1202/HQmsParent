package com.hand.spc.message_type_maintenance.mapper;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.spc.message_type_maintenance.dto.MessageType;
import com.hand.spc.message_type_maintenance.dto.MessageTypeDetail;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MessageTypeMapper extends Mapper<MessageType>{

    /**
    * @Description 前台查询
    * @author hch
    * @date 2019/8/27 10:11
    * @Param [dto]
    * @return java.util.List<com.hand.spc.message_type_maintenance.dto.MessageType>
    * @version 1.0
    */
    List<MessageType> selectData(MessageType dto);

    int selectTypeCodeCount(@Param("messageTypeCode") String messageTypeCode);


    List<MessageType> queryElementStatus(Float messageTypeId);
}