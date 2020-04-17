package com.hand.spc.repository.service;

import java.util.List;

import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;
import com.hand.spc.repository.dto.MessageUploadRelR;

/**
 * 资源库
 *
 * @author peng.hu04@hand-china.com 2019-07-07 11:32:28
 */
public interface IMessageUploadRelRService extends IBaseService<MessageUploadRelR>, ProxySelf<IMessageUploadRelRService> {

    /**
     * 批量保存消息命令
     *
     * @param messageUploadRelList
     * @return
     */
    public int batchInsertRows(List<MessageUploadRelR> messageUploadRelList);
}
