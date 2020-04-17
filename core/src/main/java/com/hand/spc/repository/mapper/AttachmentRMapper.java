package com.hand.spc.repository.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.spc.repository.dto.AttachmentGroupR;
import com.hand.spc.repository.dto.AttachmentLovDTO;
import com.hand.spc.repository.dto.AttachmentR;
import com.hand.spc.repository.dto.AttachmentResponseDTO;
import com.hand.spc.repository.dto.SPCCeGroup;

public interface AttachmentRMapper extends Mapper<AttachmentR> {

    /**
     * 根据附着对象组ID获取附着对象
     * @param tenantId
     * @param siteId
     * @param attachmentGroupId
     * @return
     */
    public List<AttachmentR> listAttachment(@Param("tenantId")Long tenantId, @Param("siteId")Long siteId, @Param("attachmentGroupId")Long attachmentGroupId);

    /*
     * 根据附着对象组查询附着对象
     *
     * */
    public List<AttachmentR> getAttachmentByGroup(AttachmentGroupR attachmentGroup);

    List<AttachmentResponseDTO> queryAttachment(AttachmentR attachment);

    public void batchDeleteList(AttachmentR attachment);

    /**
     * 附着对象自定义值集
     * @param attachmentLovDTO
     * @return
     */
    List<AttachmentLovDTO> lovByAttachment(AttachmentLovDTO attachmentLovDTO);
    
    AttachmentR selectByIndex(AttachmentR a);

    List<AttachmentR> selectAttachmentByCeGroup(SPCCeGroup SPCCeGroupIn);
}
