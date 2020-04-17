package com.hand.spc.job.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hand.spc.repository.dto.BaseCalcResultVO;
import com.hand.spc.repository.dto.EntityR;
import com.hand.spc.utils.Utils;

public class MessageThread implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(MessageThread.class);

    private String uuid;

    private EntityR entity;

    private BaseCalcResultVO baseCalcResultVO;

    private InterfaceService interfaceService;

    public MessageThread(String uuid, EntityR entity, BaseCalcResultVO baseCalcResultVO, InterfaceService interfaceService) {
        this.uuid = uuid;
        this.entity = entity;
        this.baseCalcResultVO = baseCalcResultVO;
        this.interfaceService = interfaceService;
    }

    @Override
    public void run() {
        try {
            interfaceService.batchDealMessage(this.baseCalcResultVO.getMessageList(), this.entity, this.baseCalcResultVO.getMessageType(), this.baseCalcResultVO.getMessageCreationDate());
        } catch (Exception e) {
            logger.info(Utils.getLog(uuid, "发邮件失败：" + e.getMessage()));
        }
    }

}
