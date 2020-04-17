package com.hand.spc.pspc_attachment_relation.service;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.system.service.IBaseService;
import com.hand.spc.pspc_attachment_relation.dto.AttachmentRelation;

import java.util.List;

public interface IAttachmentRelationService extends IBaseService<AttachmentRelation>, ProxySelf<IAttachmentRelationService> {

    /**
     *
     * @Description 保存附着对象组与附着对象关系
     *
     * @author yuchao.wang
     * @date 2019/8/10 14:59
     * @param requestCtx
     * @param dto
     * @return com.hand.hap.system.dto.ResponseData
     *
     */
    ResponseData saveAttachmentRelation(IRequest requestCtx, List<AttachmentRelation> dto);

    /**
     *
     * @Description 删除附着对象组与附着对象关系
     *
     * @author yuchao.wang
     * @date 2019/8/10 14:59
     * @param requestCtx
     * @param dto
     * @return
     *
     */
    void deleteRelation(IRequest requestCtx, List<AttachmentRelation> dto);

    /**
     *
     * @Description 更新附着对象组描述
     *
     * @author yuchao.wang
     * @date 2019/8/10 14:59
     * @param requestCtx
     * @param attachmentGroupId
     * @return
     *
     */
    void updateAttachmentGroupDescription(IRequest requestCtx, Long attachmentGroupId);
}