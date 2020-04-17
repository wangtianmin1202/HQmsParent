package com.hand.spc.pspc_attachment_group.service;

import com.hand.hap.attachment.dto.Attachment;
import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.system.service.IBaseService;
import com.hand.spc.pspc_attachment.dto.SpcAttachment;
import com.hand.spc.pspc_attachment_group.dto.AttachmentGroup;

import java.util.List;

public interface IAttachmentGroupService extends IBaseService<AttachmentGroup>, ProxySelf<IAttachmentGroupService> {

    /**
     *
     * @Description 查询附着要素组及明细
     *
     * @author yuchao.wang
     * @date 2019/8/9 17:53
     * @param requestContext
     * @param dto
     * @param page
     * @param pageSize
     * @return java.util.List<com.hand.spc.pspc_attachment_group.dto.AttachmentGroup>
     *
     */
    List<AttachmentGroup> queryGroupDetail(IRequest requestContext, AttachmentGroup dto, int page, int pageSize);

    /**
     *
     * @Description 删除附着对象组与控制要素组关系
     *
     * @author yuchao.wang
     * @date 2019/8/9 23:40
     * @param dto
     * @return com.hand.hap.system.dto.ResponseData
     *
     */
    ResponseData deleteRelationship(List<AttachmentGroup> dto) throws Exception;

    /**
     * @Author han.zhang
     * @Description 控制要素组维护中新增 附着对象组
     * @Date 下午3:04 2019/10/18
     * @Param [requestCtx, dto]
     **/
    ResponseData addAttachGroup(IRequest requestCtx, List<SpcAttachment> dtos);
}