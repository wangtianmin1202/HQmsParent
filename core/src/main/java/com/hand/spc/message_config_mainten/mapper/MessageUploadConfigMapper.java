package com.hand.spc.message_config_mainten.mapper;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.spc.message_config_mainten.dto.MessageUploadConfig;

import java.util.List;

public interface MessageUploadConfigMapper extends Mapper<MessageUploadConfig>{
    List<MessageUploadConfig> queryData(MessageUploadConfig dto);
}