package com.hand.spc.job.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.hand.spc.repository.dto.UploadMessageDto;
import com.hand.spc.utils.MailUtil;

/**
 * Created by slj on 2019-07-06.
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class SendMailService {

    private org.slf4j.Logger logger = LoggerFactory.getLogger(SendMailService.class);


    public String sendEmail(Long tenantId,Long siteId,String messageTempleteCode,String serverCode,String receiver,String messageContext) {
        tenantId = tenantId == null ? 420188L : tenantId;
        siteId = siteId == null ? 1L : siteId;
        messageTempleteCode = messageTempleteCode == null ? "SPC_TEST_MSG" : messageTempleteCode;//消息模块代码(暂定邮件主题)
        serverCode = serverCode == null ? "SPC_MAIL" : serverCode;//账户代码
        receiver = receiver == null ? "sss.@hand-china.com" : receiver;
        logger.info("消息触发"+messageContext);
        UploadMessageDto tmp=new UploadMessageDto();
        tmp.setTenantId(tenantId);
        tmp.setSiteId(siteId);
        tmp.setUploadType("EMAIL");
        tmp.setUploadParamete("{'messageTempleteCode':'"+messageTempleteCode+"','serverCode':'"+serverCode+"','receiver':'"+receiver+"'}");
        //Map mapTypes = JSON.parseObject(tmp.getUploadParamete());
        //String messageTemplateCode = (mapTypes.get("messageTempleteCode")).toString();

        Map<String,String>messageMap=new HashMap<String,String>();
        logger.info("开始测试发送邮件");
        
        //20190830  这里改成使用hap邮件发送方式
        //org.hzero.boot.message.entity.Message msg = null;
        messageMap.put("message",messageContext);
            //msg = messageClient.sendEmail(tmp.getTenantId(), serverCode, messageTemplateCode, getEmailReceive(receiver), messageMap);

            //if(msg==null||msg.getSendFlag()==0){
                //throw new CommonException("邮件发送失败");
            //}
        
        MailUtil.sendMail(receiver,messageTempleteCode,messageContext);//     "SPC邮件主题测试"

        logger.info("结束测试发送邮件");
        //logger.info("邮件接口调用结果："  + JSON.toJSONString(msg));
        return "邮件发送完毕";
    }
}
