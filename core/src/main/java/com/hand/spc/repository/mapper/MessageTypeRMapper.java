package com.hand.spc.repository.mapper;



import java.util.List;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.spc.repository.dto.MessageTypeR;
import com.hand.spc.repository.dto.MessageTypeUploadRelDTO;

public interface MessageTypeRMapper extends Mapper<MessageTypeR> {

	List<MessageTypeUploadRelDTO> selectMessageUploadConfig(MessageTypeR messageType);

    MessageTypeR selectByTypeCode(MessageTypeR messageType);

    public List<MessageTypeUploadRelDTO> selectMessageUploadConfigById(MessageTypeR messageType);

}
