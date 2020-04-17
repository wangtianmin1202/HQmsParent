package com.hand.spc.pspc_attachment.service;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.system.service.IBaseService;
import com.hand.spc.pspc_attachment.dto.MenuItem;
import com.hand.spc.pspc_attachment.dto.SpcAttachment;

import java.util.List;

public interface ISpcAttachmentService extends IBaseService<SpcAttachment>, ProxySelf<ISpcAttachmentService> {

    /**
     *
     * @Description 根据附着对象组ID查找附着对象ID
     *
     * @author yuchao.wang
     * @date 2019/8/9 16:45
     * @param requestContext
     * @param dto
     * @param page
     * @param pageSize
     * @return java.util.List<com.hand.spc.pspc_attachment.dto.SpcAttachment>
     *
     */
    List<SpcAttachment> selectByCroupId(IRequest requestContext, SpcAttachment dto, int page, int pageSize);

    /**
     * @Author han.zhang
     * @Description 查询附着对象层级维护 树形图数据
     * @Date 11:40 2019/8/19
     * @Param [requestContext, dto]
     */
    List<MenuItem> queryTreeData(IRequest requestContext, SpcAttachment dto);

    /**
     * @Author han.zhang
     * @Description 更新或者保存附着对象
     * @Date 19:40 2019/8/19
     * @Param [requestCtx, dto]
     */
    ResponseData updateOrAdd(IRequest requestCtx, SpcAttachment dto);

    void deleteRow(SpcAttachment dto);
}