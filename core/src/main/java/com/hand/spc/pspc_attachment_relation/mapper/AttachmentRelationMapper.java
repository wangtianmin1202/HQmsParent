package com.hand.spc.pspc_attachment_relation.mapper;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.spc.pspc_attachment.dto.SpcAttachment;
import com.hand.spc.pspc_attachment_relation.dto.AttachmentRelation;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AttachmentRelationMapper extends Mapper<AttachmentRelation> {

    /**
     *
     * @Description 通过附着对象组ID查找关系
     *
     * @author yuchao.wang
     * @date 2019/8/9 20:07
     * @param attachmentGroupId
     * @return java.util.List<com.hand.spc.pspc_attachment_relation.dto.AttachmentRelation>
     *
     */
    List<AttachmentRelation> selectRelationByCroupId(@Param("attachmentGroupId") Long attachmentGroupId);
}