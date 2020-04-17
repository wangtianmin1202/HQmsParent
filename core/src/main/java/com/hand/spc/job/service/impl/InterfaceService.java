package com.hand.spc.job.service.impl;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.hand.spc.repository.dto.EntityR;
import com.hand.spc.repository.dto.MessageR;
import com.hand.spc.repository.dto.MessageUploadDTO;
import com.hand.spc.repository.mapper.MessageRMapper;
import com.hand.spc.utils.MtException;

/**
 * Created by slj on 2019-07-04.
 */
@Service
public class InterfaceService {

    @Autowired
    private MessageRMapper messageMapper;
    
    @Autowired
    private SendMailService sendMailService;

    private static final Logger logger = LoggerFactory.getLogger(InterfaceService.class);

    /**
     * 数据发送对外开放接口
     *
     * @param siteId
     * @param tenantId
     * @param groupCode
     * @param type
     */
    public void uploadMessage(Long siteId, Long tenantId, String groupCode, EntityR entity, String type,
                    Date messageCreationDate) {
        try {
            entity.setTenantId(tenantId);
            MessageR message = new MessageR();
            message.setSiteId(siteId);
            message.setTenantId(tenantId);
            message.setMessageStatus("N");
            message.setCreationDate(messageCreationDate);
            if (!StringUtils.isEmpty(groupCode)) {
                message.setGroupCode(groupCode);
            }
            List<MessageUploadDTO> messageList = messageMapper.queryUploadMessage(message);
            for (MessageUploadDTO tmp : messageList) {
                logger.info("uploadMessage开始邮件发送:" + tmp.getMessageText());
                send(tmp, entity, type);
            }
        } catch (Exception e) {
            throw new MtException(e.getMessage());
        }
    }
    
    public void sendMessageByJob(Long siteId, Long tenantId) {
        try {
            MessageR message = new MessageR();
            message.setSiteId(siteId);
            message.setTenantId(tenantId);
            message.setMessageStatus("N");

            List<MessageUploadDTO> messageList = messageMapper.queryUploadMessage(message);
            if(CollectionUtils.isNotEmpty(messageList)){
                messageList.stream().forEach(m->send(m,null,null));
            }
        } catch (Exception e) {
            throw new MtException(e.getMessage());
        }
    }
    
    
    public void batchDealMessage(List<MessageR> messageList, EntityR entity, String type, Date messageCreationDate) {
        try {
            messageList.stream().forEach(t -> {
                t.setMessageStatus("N");
                t.setCreationDate(messageCreationDate);
            });
            List<MessageUploadDTO> messageUploadDTOList = messageMapper.queryBatchUploadMessage(messageList);
            messageUploadDTOList.stream().forEach(c -> send(c, entity, type));
        } catch (Exception e) {
            throw new MtException(e.getMessage());
        }
    }

    /**
     * 数据发送
     *
     * @param dto
     */
    @SuppressWarnings("unchecked")
	@Transactional(rollbackFor = Exception.class)
    public void send(MessageUploadDTO dto,EntityR entity, String type) {
    	 Map<String, String> parameter = null;
         String totalMsg = dto.getMessageText();
         // 消息ID处理
         List<String> msgids = Arrays.asList(dto.getMessageIds().split("\\|"));

         StringBuffer newContent = new StringBuffer();
         if (!StringUtils.isEmpty(dto.getConfigCommand())) {
             String[] messageTexts = totalMsg.split("\\|");
             for (int i = 0; i < messageTexts.length; i++) {
                 newContent.append(messageTexts[i] + ";" + "消息命令: " + dto.getConfigCommand() + "|");
             }
         }
         if (!StringUtils.isEmpty(newContent)) {
             totalMsg = newContent.toString();
         }
         String msg = "";
        try {
            switch (dto.getConfigType()) {
//                case "MQTT":
//                    parameter = (Map<String, String>) JSON.parse(dto.getConfigValue());
//                    mqttService.sendMsg(parameter, totalMsg);
//                    break;
//                case "KAFKA":
//                    parameter = (Map<String, String>) JSON.parse(dto.getConfigValue());
//                    kafkaService.sendMsg(parameter, totalMsg);
//                    break;
                case "EMAIL":
                    parameter = (Map<String, String>) JSON.parse(dto.getConfigValue());
                    String messageTemplateCode = (parameter.get("messageTempleteCode")).toString();
                    String serverCode = (parameter.get("serverCode")).toString();
                    String receiver = (parameter.get("receiver")).toString();
                    StringBuffer msgText = new StringBuffer();
                    String[] messageTexts = totalMsg.split("\\|");
                    for (int i = 0; i < messageTexts.length; i++) {
                        String[] messageDetails = messageTexts[i].split(";");
                        for (int j = 0; j < messageDetails.length; j++) {
                            msgText.append(messageDetails[j] + " <br>");
                        }
                        msgText.append(" <br>");
                    }
                    // 增加图形展示的链接
                    //String typeOocId = "count".equalsIgnoreCase(type) ? "&countOocId=" + dto.getOocId() : "&oocId=" + dto.getOocId();
                    //String url = "entityCode=" + entity.getEntityCode() + "&entityVersion=" + entity.getEntityVersion() + "&startDate=" + dto.getStartTime() + "&endDate=" + dto.getEndTime() + typeOocId;
                    // String encryptUrl = URLEncoder.encode(url,"utf-8");
                    //msgText.append("<a href='http://192.168.16.232:9320/spc/XBarR?" + url + "'>详情链接</a>");
                    msg = sendMailService.sendEmail(dto.getTenantId(), dto.getSiteId(), messageTemplateCode, serverCode, receiver, msgText.toString());
                    logger.info("邮件返回消息是:" + msg);
                    break;
                case "SMS":
                    break;
                default:
                    break;
            }
            long begin = System.currentTimeMillis();
            messageMapper.updateByConfig(dto.getUploadConfigId(), msgids);
            logger.info("更新用时：" + (System.currentTimeMillis() - begin));
        } catch (Exception e) {
            logger.info("数据上传异常(" + dto.getMessageIds() + "):" + e.toString());
        }
    }


    /**
     * 启动后台的监听进程
     *
     * @param dto
     */
//    public void start(MessageDownDTO dto) {
//        Map<String, String> parameter = new HashMap<String, String>();
//        switch (dto.getComType()) {
//            case "KAFKA":
//                KafkaConfigHead configHead = new KafkaConfigHead();
//                configHead.setKafkaConfigHeadId(dto.getHeadId());
//                configHead = kafkaConfigHeadRepository.selectOne(configHead);
//                if (configHead != null) {
//                    if (!Constant.checkInterface(dto.getComType(), configHead.getKafkaConfigHeadCode())) {
//                        throw new MtException("PSPC_ERROR_58", errorMessageRepository.getErrorMessageWithModule(
//                                        dto.getTenantId(), "PSPC_ERROR_58", "GENERAL", "【API:InterFaceService:stop】"));
//                    }
//                    startKafka(parameter, configHead);
//                }
//                break;
//            case "MQTT":
//                MqttConfigHead mqttConfigHead = new MqttConfigHead();
//                mqttConfigHead.setMqttConfigHeadId(dto.getHeadId());
//                mqttConfigHead = mqttConfigHeadRepository.selectOne(mqttConfigHead);
//                if (mqttConfigHead != null) {
//                    if (!Constant.checkInterface(dto.getComType(), mqttConfigHead.getMqttConfigHeadCode())) {
//                        throw new MtException("PSPC_ERROR_58", errorMessageRepository.getErrorMessageWithModule(
//                                        dto.getTenantId(), "PSPC_ERROR_58", "GENERAL", "【API:InterFaceService:stop】"));
//                    }
//                    if (mqttConfigHead.getStatus().equals("Y")) {
//                        throw new MtException("PSPC_ERROR_59", errorMessageRepository.getErrorMessageWithModule(
//                                        dto.getTenantId(), "PSPC_ERROR_59", "GENERAL", "【API:InterFaceService:start】"));
//                    }
//                    parameter.put("host", mqttConfigHead.getIpAdress());
//                    parameter.put("username", mqttConfigHead.getUsername());
//                    parameter.put("password", mqttConfigHead.getPassword());
//                    parameter.put("siteId", mqttConfigHead.getSiteId().toString());
//                    parameter.put("tenantId", mqttConfigHead.getTenantId().toString());
//                    parameter.put("mqttConfigHeadId", mqttConfigHead.getMqttConfigHeadId().toString());
//                    MqttConfigLine linetemp = new MqttConfigLine();
//                    linetemp.setMqttConfigHeadId(mqttConfigHead.getMqttConfigHeadId());
//                    List<MqttConfigLine> lineList = mqttConfigLineRepository.select(linetemp);
//                    StringBuffer topic = new StringBuffer();
//                    for (MqttConfigLine line : lineList) {
//                        Map<String, Object> lineParameter = JSON.parseObject(line.getParameterValue());
//                        topic.append(lineParameter.get("topic") + ";");
//                    }
//                    topic = new StringBuffer(topic.substring(0, topic.length() - 1));
//                    parameter.put("topic", topic.toString());
//                    mqttService.startMqttMsgListener(parameter);
//                }
//                break;
//            default:
//                break;
//        }
//    }

    /**
     * 关闭后台的监听进程
     *
     * @param dto
     */
//    public void stop(MessageDownDTO dto) {
//        // 关闭监听
//        Map<String, String> parameter = new HashMap<String, String>();
//        switch (dto.getComType()) {
//            case "KAFKA":
//                KafkaConfigHead kafkaConfigHead = new KafkaConfigHead();
//                kafkaConfigHead.setKafkaConfigHeadId(dto.getHeadId());
//                kafkaConfigHead = kafkaConfigHeadRepository.selectOne(kafkaConfigHead);
//                if (kafkaConfigHead != null) {
//                    if (!Constant.checkInterface(dto.getComType(), kafkaConfigHead.getKafkaConfigHeadCode())) {
//                        throw new MtException("PSPC_ERROR_58", errorMessageRepository.getErrorMessageWithModule(
//                                        dto.getTenantId(), "PSPC_ERROR_58", "GENERAL", "【API:InterFaceService:stop】"));
//                    }
//                    parameter.put("username", kafkaConfigHead.getUsername());
//                    parameter.put("password", kafkaConfigHead.getPassword());
//                    parameter.put("host", kafkaConfigHead.getIpAdress());
//                    parameter.put("kafkaConfigHeadId", kafkaConfigHead.getKafkaConfigHeadId().toString());
//
//                } else {
//                    throw new MtException("PSPC_ERROR_60", errorMessageRepository.getErrorMessageWithModule(
//                                    dto.getTenantId(), "PSPC_ERROR_60", "GENERAL", "【API:InterFaceService:stop】"));
//                }
//                break;
//            case "MQTT":
//                MqttConfigHead mqttConfigHead = new MqttConfigHead();
//                mqttConfigHead.setMqttConfigHeadId(dto.getHeadId());
//                mqttConfigHead = mqttConfigHeadRepository.selectOne(mqttConfigHead);
//                if (mqttConfigHead != null) {
//                    if (!Constant.checkInterface(dto.getComType(), mqttConfigHead.getMqttConfigHeadCode())) {
//                        throw new MtException("PSPC_ERROR_58", errorMessageRepository.getErrorMessageWithModule(
//                                        dto.getTenantId(), "PSPC_ERROR_58", "GENERAL", "【API:InterFaceService:stop】"));
//                    }
//                    parameter.put("host", mqttConfigHead.getIpAdress());
//                    parameter.put("mqttConfigHeadId", mqttConfigHead.getMqttConfigHeadId().toString());
//                    mqttService.closeMqttMsgListener(parameter);
//                    mqttService.closeMqttDealMsgListener(mqttConfigHead.getMqttConfigHeadId());
//                } else {
//                    throw new MtException("PSPC_ERROR_60", errorMessageRepository.getErrorMessageWithModule(
//                                    dto.getTenantId(), "PSPC_ERROR_60", "GENERAL", "【API:InterFaceService:stop】"));
//                }
//                break;
//            default:
//                break;
//        }
//    }
    
    
//    private void startKafka(Map<String, String> parameter, KafkaConfigHead configHead) {
//        if (configHead.getStatus().equals("Y")) {
//            throw new MtException("PSPC_ERROR_59", errorMessageRepository.getErrorMessageWithModule(
//                            configHead.getTenantId(), "PSPC_ERROR_59", "GENERAL", "【API:startKafka】"));
//        }
//        KafkaConfigLine lineTemp = new KafkaConfigLine();
//        lineTemp.setKafkaConfigHeadId(configHead.getKafkaConfigHeadId());
//        List<KafkaConfigLine> lineList = kafkaConfigLineRepository.select(lineTemp);
//        StringBuffer topic = new StringBuffer();
//        for (KafkaConfigLine line : lineList) {
//            @SuppressWarnings("unchecked")
//            Map<String, Object> lineParameter = JSONObject.parseObject(line.getParameterValue(), Map.class);
//            topic.append(lineParameter.get("topic") + ";");
//        }
//        // 暂时无法指定分区
//        topic = new StringBuffer(topic.substring(0, topic.length() - 1));
//        parameter.put("username", configHead.getUsername());
//        parameter.put("password", configHead.getPassword());
//        parameter.put("topic", topic.toString());
//        parameter.put("kafkaConfigHeadId", configHead.getKafkaConfigHeadId().toString());
//        parameter.put("siteId", configHead.getSiteId().toString());
//        parameter.put("tenantId", configHead.getTenantId().toString());
//        parameter.put("host", configHead.getIpAdress());
//        kafkaService.startKafkaMsgListener(parameter);
//    }

}
