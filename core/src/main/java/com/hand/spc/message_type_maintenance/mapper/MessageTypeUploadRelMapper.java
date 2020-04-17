package com.hand.spc.message_type_maintenance.mapper;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.spc.message_type_maintenance.dto.MessageTypeUploadRel;

import java.util.List;

public interface MessageTypeUploadRelMapper extends Mapper<MessageTypeUploadRel>{

    List<MessageTypeUploadRel> selectData(Float messageTypeId);
}