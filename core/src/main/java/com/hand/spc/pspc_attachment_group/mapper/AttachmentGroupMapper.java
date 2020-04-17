package com.hand.spc.pspc_attachment_group.mapper;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.spc.pspc_attachment_group.dto.AttachmentGroup;

import java.util.List;

public interface AttachmentGroupMapper extends Mapper<AttachmentGroup> {

    /**
     *
     * @Description 查询附着要素组及明细
     *
     * @author yuchao.wang
     * @date 2019/8/9 17:55
     * @param dto
     * @return java.util.List<com.hand.spc.pspc_attachment_group.dto.AttachmentGroup>
     *
     */
    List<AttachmentGroup> queryAttachmentGroup(AttachmentGroup dto);

    /**
     * @Author han.zhang
     * @Description 附着对象组lov查询，支持模糊查询
     * @Date 21:46 2019/8/12
     * @Param 均为可选参数 [attachmentGroup.ceGroupId,attachmentGroup.attachmentGroupDescription]
     */
    List<AttachmentGroup> selectAttacmentGroupLov(AttachmentGroup attachmentGroup);
}