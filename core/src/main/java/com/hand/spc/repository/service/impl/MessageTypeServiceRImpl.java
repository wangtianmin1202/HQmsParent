package com.hand.spc.repository.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import com.hand.hap.core.IRequest;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import com.hand.spc.repository.dto.MessageTypeR;
import com.hand.spc.repository.dto.MessageTypeDetailR;
import com.hand.spc.repository.dto.MessageTypeUploadRelDTO;
import com.hand.spc.repository.mapper.MessageTypeDetailRMapper;
import com.hand.spc.repository.mapper.MessageTypeRMapper;
import com.hand.spc.repository.service.IMessageTypeRService;
@Service
@Transactional(rollbackFor = Exception.class)
public class MessageTypeServiceRImpl extends BaseServiceImpl<MessageTypeR> implements IMessageTypeRService{
	private Logger logger = LoggerFactory.getLogger(MessageTypeServiceRImpl.class);

	@Autowired
    private MessageTypeRMapper messageTypeMapper;
	
	@Autowired
	private MessageTypeDetailRMapper messageTypeDetailMapper;
		

    @Override
    public List<MessageTypeUploadRelDTO> selectMessageUploadConfig(MessageTypeR messageType) throws Exception {
        //拦截器需要编写的逻辑
        if (StringUtils.isEmpty(messageType.getMessageTypeCode())) {
            throw new Exception("消息类型为空");//
        }
        return messageTypeMapper.selectMessageUploadConfig(messageType);

    }
    
    @Override
    public MessageTypeR listMessageType(MessageTypeR messageTypeQuery) throws Exception {

        //拦截器需要编写的逻辑
        if (messageTypeQuery.getTenantId() == null) {
            throw new Exception("pspc.error.tenant.null");//租户ID为空
        }
        if (messageTypeQuery.getSiteId() == null) {
            throw new Exception("pspc.error.tenant.null");//租户ID为空
        }
        if (StringUtils.isEmpty(messageTypeQuery.getMessageTypeCode())) {
            throw new Exception("pspc.error.messagetype_null");//消息类型编码为空
        }
        MessageTypeR messageType = messageTypeMapper.selectOne(messageTypeQuery);
        if (!ObjectUtils.isEmpty(messageType)) {

            MessageTypeDetailR messageTypeDetail = new MessageTypeDetailR();
            messageTypeDetail.setTenantId(messageType.getTenantId());
            messageTypeDetail.setSiteId(messageType.getSiteId());
            messageTypeDetail.setMessageTypeId(messageType.getMessageTypeId());
            List<MessageTypeDetailR> messageTypeDetailList = messageTypeDetailMapper.select(messageTypeDetail);
            if (messageTypeDetailList.size() > 0) {
                List<MessageTypeDetailR> themeList = new ArrayList<MessageTypeDetailR>();
                List<MessageTypeDetailR> contentList = new ArrayList<MessageTypeDetailR>();
                for (MessageTypeDetailR detail : messageTypeDetailList) {
                    if ("THEME".equals(detail.getElementCategory())) {
                        themeList.add(detail);
                    } else if ("CONTENT".equals(detail.getElementCategory())) {
                        contentList.add(detail);
                    }
                }
                if (themeList.size() > 0) {
                    messageType.setThemeDetailList(themeList);
                }
                if (contentList.size() > 0) {
                    messageType.setContentDetailList(contentList);
                }
            }
        }
        return messageType;
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public MessageTypeR saveMessageType(MessageTypeR messageType) {

        List<MessageTypeDetailR> insertDataList = new ArrayList<MessageTypeDetailR>();
        List<MessageTypeDetailR> updateDataList = new ArrayList<MessageTypeDetailR>();

        //保存消息类型头
        if (StringUtils.isEmpty(messageType.getMessageTypeId())) {
        	messageTypeMapper.insert(messageType);

            //消息明细行(主题)
            if (!ObjectUtils.isEmpty(messageType.getThemeDetailList()) && messageType.getThemeDetailList().size() > 0) {
                for (MessageTypeDetailR detail : messageType.getThemeDetailList()) {
                    detail.setMessageTypeId(messageType.getMessageTypeId());
                    detail.setTenantId(messageType.getTenantId());
                    detail.setSiteId(messageType.getSiteId());
                    detail.setElementCategory("THEME");
                    insertDataList.add(detail);
                }
            }
            //消息明细行(内容)
            if (!ObjectUtils.isEmpty(messageType.getContentDetailList()) && messageType.getContentDetailList().size() > 0) {
                for (MessageTypeDetailR detail : messageType.getContentDetailList()) {
                    detail.setMessageTypeId(messageType.getMessageTypeId());
                    detail.setTenantId(messageType.getTenantId());
                    detail.setSiteId(messageType.getSiteId());
                    detail.setElementCategory("CONTENT");
                    insertDataList.add(detail);
                }
            }
        } else {
        	messageTypeMapper.updateByPrimaryKey(messageType);
            //查询消息类型存在的所有消息类型明细数据
            MessageTypeDetailR detailQuery = new MessageTypeDetailR();
            detailQuery.setTenantId(messageType.getTenantId());
            detailQuery.setSiteId(messageType.getSiteId());
            detailQuery.setMessageTypeId(messageType.getMessageTypeId());
            List<MessageTypeDetailR> messageTypeDetailList = messageTypeDetailMapper.select(detailQuery);

            //消息明细行(主题)
            if (!ObjectUtils.isEmpty(messageType.getThemeDetailList()) && messageType.getThemeDetailList().size() > 0) {
                for (MessageTypeDetailR detail : messageType.getThemeDetailList()) {
                    List<MessageTypeDetailR> detailList = messageTypeDetailList.stream().filter(messageTypeDetail -> "THEME".equals(messageTypeDetail.getElementCategory()) && messageTypeDetail.getElementCode().equals(detail.getElementCode())).collect(Collectors.toList());
                    if (detailList.size() > 0) {
                        detailList.get(0).setElementStatus(detail.getElementStatus());
                        updateDataList.add(detailList.get(0));
                    } else {
                        detail.setMessageTypeId(messageType.getMessageTypeId());
                        detail.setTenantId(messageType.getTenantId());
                        detail.setSiteId(messageType.getSiteId());
                        detail.setElementCategory("THEME");
                        insertDataList.add(detail);
                    }
                }
            }
            //消息明细行(内容)
            if (!ObjectUtils.isEmpty(messageType.getContentDetailList()) && messageType.getContentDetailList().size() > 0) {
                for (MessageTypeDetailR detail : messageType.getContentDetailList()) {
                    List<MessageTypeDetailR> detailList = messageTypeDetailList.stream().filter(messageTypeDetail -> "CONTENT".equals(messageTypeDetail.getElementCategory()) && messageTypeDetail.getElementCode().equals(detail.getElementCode())).collect(Collectors.toList());
                    if (detailList.size() > 0) {
                        detailList.get(0).setElementStatus(detail.getElementStatus());
                        updateDataList.add(detailList.get(0));
                    } else {
                        detail.setMessageTypeId(messageType.getMessageTypeId());
                        detail.setTenantId(messageType.getTenantId());
                        detail.setSiteId(messageType.getSiteId());
                        detail.setElementCategory("CONTENT");
                        insertDataList.add(detail);
                    }
                }
            }
        }
        if (insertDataList.size() > 0) {
        	for(int i=0;i<insertDataList.size();i++) {
        		messageTypeDetailMapper.insert(insertDataList.get(i));
        	}
            //messageTypeDetailRepository.batchInsert(insertDataList);
        }
        if (updateDataList.size() > 0) {
        	
        	for(int i=0;i<updateDataList.size();i++) {
        		messageTypeDetailMapper.updateByPrimaryKey(updateDataList.get(i));
        	}
            //messageTypeDetailRepository.batchUpdateByPrimaryKey(updateDataList);
        }
        return messageType;
    }
    
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public MessageTypeR saveMessageTypebyhap(MessageTypeR messageType) {

        List<MessageTypeDetailR> insertDataList = new ArrayList<MessageTypeDetailR>();
        List<MessageTypeDetailR> updateDataList = new ArrayList<MessageTypeDetailR>();

        //保存消息类型头
        if (StringUtils.isEmpty(messageType.getMessageTypeId())) {
        	messageTypeMapper.insert(messageType);

            //消息明细行(主题)
            if (!ObjectUtils.isEmpty(messageType.getThemeDetailList()) && messageType.getThemeDetailList().size() > 0) {
                for (MessageTypeDetailR detail : messageType.getThemeDetailList()) {
                    detail.setMessageTypeId(messageType.getMessageTypeId());
                    detail.setTenantId(messageType.getTenantId());
                    detail.setSiteId(messageType.getSiteId());
                    detail.setElementCategory("THEME");
                    insertDataList.add(detail);
                }
            }
            //消息明细行(内容)
            if (!ObjectUtils.isEmpty(messageType.getContentDetailList()) && messageType.getContentDetailList().size() > 0) {
                for (MessageTypeDetailR detail : messageType.getContentDetailList()) {
                    detail.setMessageTypeId(messageType.getMessageTypeId());
                    detail.setTenantId(messageType.getTenantId());
                    detail.setSiteId(messageType.getSiteId());
                    detail.setElementCategory("CONTENT");
                    insertDataList.add(detail);
                }
            }
        } else {
        	messageTypeMapper.updateByPrimaryKey(messageType);
            //查询消息类型存在的所有消息类型明细数据
            MessageTypeDetailR detailQuery = new MessageTypeDetailR();
            detailQuery.setTenantId(messageType.getTenantId());
            detailQuery.setSiteId(messageType.getSiteId());
            detailQuery.setMessageTypeId(messageType.getMessageTypeId());
            List<MessageTypeDetailR> messageTypeDetailList = messageTypeDetailMapper.select(detailQuery);

            //消息明细行(主题)
            if (!ObjectUtils.isEmpty(messageType.getThemeDetailList()) && messageType.getThemeDetailList().size() > 0) {
                for (MessageTypeDetailR detail : messageType.getThemeDetailList()) {
                    List<MessageTypeDetailR> detailList = messageTypeDetailList.stream().filter(messageTypeDetail -> "THEME".equals(messageTypeDetail.getElementCategory()) && messageTypeDetail.getElementCode().equals(detail.getElementCode())).collect(Collectors.toList());
                    if (detailList.size() > 0) {
                        detailList.get(0).setElementStatus(detail.getElementStatus());
                        updateDataList.add(detailList.get(0));
                    } else {
                        detail.setMessageTypeId(messageType.getMessageTypeId());
                        detail.setTenantId(messageType.getTenantId());
                        detail.setSiteId(messageType.getSiteId());
                        detail.setElementCategory("THEME");
                        insertDataList.add(detail);
                    }
                }
            }
            //消息明细行(内容)
            if (!ObjectUtils.isEmpty(messageType.getContentDetailList()) && messageType.getContentDetailList().size() > 0) {
                for (MessageTypeDetailR detail : messageType.getContentDetailList()) {
                    List<MessageTypeDetailR> detailList = messageTypeDetailList.stream().filter(messageTypeDetail -> "CONTENT".equals(messageTypeDetail.getElementCategory()) && messageTypeDetail.getElementCode().equals(detail.getElementCode())).collect(Collectors.toList());
                    if (detailList.size() > 0) {
                        detailList.get(0).setElementStatus(detail.getElementStatus());
                        updateDataList.add(detailList.get(0));
                    } else {
                        detail.setMessageTypeId(messageType.getMessageTypeId());
                        detail.setTenantId(messageType.getTenantId());
                        detail.setSiteId(messageType.getSiteId());
                        detail.setElementCategory("CONTENT");
                        insertDataList.add(detail);
                    }
                }
            }
        }
        if (insertDataList.size() > 0) {
        	for(int i=0;i<insertDataList.size();i++) {
        		messageTypeDetailMapper.insert(insertDataList.get(i));
        	}
            //messageTypeDetailRepository.batchInsert(insertDataList);
        }
        if (updateDataList.size() > 0) {
        	
        	for(int i=0;i<updateDataList.size();i++) {
        		messageTypeDetailMapper.updateByPrimaryKey(updateDataList.get(i));
        	}
            //messageTypeDetailRepository.batchUpdateByPrimaryKey(updateDataList);
        }
        return messageType;
    }
    
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<MessageTypeR> batchUpdate(IRequest request, List<MessageTypeR> messageTypeList) {
        for(MessageTypeR messageType: messageTypeList){
            if(messageType.getMessageTypeId() == null){
            	//MessageTypeUploadRel messageTypeUploadRel = new MessageTypeUploadRel();
            	//messageTypeUploadRel.setMessageTypeId(messageType.getMessageTypeId());
                self().insertSelective(request,messageType);
            }else{
                self().updateByPrimaryKey(request,messageType);
            }
        }
        return messageTypeList;
    }
}