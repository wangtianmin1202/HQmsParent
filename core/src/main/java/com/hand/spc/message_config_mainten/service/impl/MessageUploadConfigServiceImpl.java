package com.hand.spc.message_config_mainten.service.impl;

import com.github.pagehelper.PageHelper;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import com.hand.spc.message_config_mainten.mapper.MessageUploadConfigMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.hand.spc.message_config_mainten.dto.MessageUploadConfig;
import com.hand.spc.message_config_mainten.service.IMessageUploadConfigService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class MessageUploadConfigServiceImpl extends BaseServiceImpl<MessageUploadConfig> implements IMessageUploadConfigService{

    @Autowired
    private MessageUploadConfigMapper messageUploadConfigMapper;
    @Override
    public List<MessageUploadConfig> queryData(IRequest request, MessageUploadConfig dto, int page, int pageSize) {
        PageHelper.startPage(page,pageSize);
        List<MessageUploadConfig> result=messageUploadConfigMapper.queryData(dto);

        for(int i=0;i<result.size();i++){
            result.get(i).setRowNum(i+1);
        }
        return result;
    }

    @Override
    public ResponseData update(IRequest request, MessageUploadConfig dto) {
        //判断是否违反唯一索引
        MessageUploadConfig query=new MessageUploadConfig();
        query.setConfigCode(dto.getConfigCode());
        MessageUploadConfig queryOne=mapper.selectOne(query);
        if(queryOne!=null&&dto.getUploadConfigId()==null){
            throw new RuntimeException("已经存在配置编码：【"+dto.getConfigCode()+"】的数据");
        }
        //insert
        if(dto.getUploadConfigId()==null){
            messageUploadConfigMapper.insertSelective(dto);
        }else{
            messageUploadConfigMapper.updateByPrimaryKeySelective(dto);
        }
        return new ResponseData();
    }
}