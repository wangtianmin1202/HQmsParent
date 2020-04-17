package com.hand.spc.repository.service;

import java.util.List;

import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;
import com.hand.spc.repository.dto.AttachmentGroupR;
import com.hand.spc.repository.dto.AttachmentLovDTO;
import com.hand.spc.repository.dto.AttachmentR;
import com.hand.spc.repository.dto.AttachmentResponseDTO;

public interface IAttachmentRService extends IBaseService<AttachmentR>, ProxySelf<IAttachmentRService> {

    /**
     * 根据附着对象组ID获取附着对象
     *
     * @param tenantId
     * @param siteId
     * @param attachmentGroupId
     * @return
     */
    public List<AttachmentR> listAttachment(Long tenantId, Long siteId, Long attachmentGroupId);

    /*
    * 根据附着对象组查询附着对象
    *
    * */
    public List<AttachmentR> getAttachmentByGroup(AttachmentGroupR attachmentGroup);

    List<AttachmentResponseDTO> queryAttachment(AttachmentR attachment);

    /**
     * 附着对象自定义值集
     * @param attachmentLovDTO
     * @return
     */
    List<AttachmentLovDTO> lovByAttachment(AttachmentLovDTO attachmentLovDTO);

}
