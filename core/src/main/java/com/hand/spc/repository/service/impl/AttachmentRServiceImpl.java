package com.hand.spc.repository.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hand.hap.system.service.impl.BaseServiceImpl;
import com.hand.spc.repository.dto.AttachmentGroupR;
import com.hand.spc.repository.dto.AttachmentLovDTO;
import com.hand.spc.repository.dto.AttachmentR;
import com.hand.spc.repository.dto.AttachmentResponseDTO;
import com.hand.spc.repository.mapper.AttachmentRMapper;
import com.hand.spc.repository.service.IAttachmentRService;

@Service
@Transactional(rollbackFor = Exception.class)
public class AttachmentRServiceImpl extends BaseServiceImpl<AttachmentR> implements IAttachmentRService {

    @Autowired
    private AttachmentRMapper attachmentMapper;


    @Override
    public List<AttachmentR> listAttachment(Long tenantId, Long siteId, Long attachmentGroupId) {
        return attachmentMapper.listAttachment(tenantId,siteId,attachmentGroupId);
    }

    @Override
    public List<AttachmentR> getAttachmentByGroup(AttachmentGroupR attachmentGroup){
        return attachmentMapper.getAttachmentByGroup(attachmentGroup);
    }

    @Override
    public List<AttachmentResponseDTO> queryAttachment(AttachmentR attachment) {
        return attachmentMapper.queryAttachment(attachment);
    }

    @Override
    public List<AttachmentLovDTO> lovByAttachment(AttachmentLovDTO attachmentLovDTO) {
        return attachmentMapper.lovByAttachment(attachmentLovDTO);
    }


}
