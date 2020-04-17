package com.hand.spc.repository.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.spc.repository.dto.MessageR;
import com.hand.spc.repository.dto.MessageUploadDTO;

public interface MessageRMapper extends Mapper<MessageR> {

    /**
     * 批量保存消息
     *
     * @param messageList
     * @return
     */
    public int batchInsertRows(List<MessageR> messageList);

    public List<MessageUploadDTO>queryUploadMessage(MessageR dto);

    public void updateByConfig(@Param(value = "configId")Long configId,@Param(value = "ids") List<String> ids);
    
    public  List<MessageUploadDTO> queryBatchUploadMessage(@Param("messageList")List<MessageR> messageList);


}
