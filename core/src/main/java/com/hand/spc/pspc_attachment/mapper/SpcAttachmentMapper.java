package com.hand.spc.pspc_attachment.mapper;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.spc.pspc_attachment.dto.SpcAttachment;

import java.util.List;

public interface SpcAttachmentMapper extends Mapper<SpcAttachment> {


    /**
     *
     * @Description 根据附着对象组ID查找附着对象ID
     *
     * @author yuchao.wang
     * @date 2019/8/9 16:50
     * @param dto
     * @return java.util.List<com.hand.spc.pspc_attachment.dto.SpcAttachment>
     *
     */
    List<SpcAttachment> selectAttachmentsByCroupId(SpcAttachment dto);

    /**
     * @Author han.zhang
     * @Description 查询根对象
     * @Date 14:08 2019/8/19
     * @Param []
     * @param dto
     */
    List<SpcAttachment> selectParentAttachment(SpcAttachment dto);

    /**
     * @Author han.zhang
     * @Description 附着对象lov查询
     * @Date 16:42 2019/8/19
     * @Param [dto]
     */
    List<SpcAttachment> selectAttachmentLov(SpcAttachment dto);
}