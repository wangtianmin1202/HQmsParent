package com.hand.spc.pspc_ce_group.service;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.system.service.IBaseService;
import com.hand.spc.pspc_ce_group.dto.CeGroup;

import java.util.List;

public interface ICeGroupService extends IBaseService<CeGroup>, ProxySelf<ICeGroupService> {

    /**
     *
     * @Description 控制要素组副本保存
     *
     * @author yuchao.wang
     * @date 2019/8/9 16:08
     * @param requestCtx
     * @param dto
     * @return com.hand.hap.system.dto.ResponseData
     *
     */
    ResponseData copySave(IRequest requestCtx, CeGroup dto);

    /**
     *
     * @Description 删除控制要素组及其关系
     *
     * @author yuchao.wang
     * @date 2019/8/10 23:53
     * @param dto
     * @return com.hand.hap.system.dto.ResponseData
     *
     */
    ResponseData deleteCeGroupAndRelationship(CeGroup dto) throws Exception;

    /**
     *
     * @Description 模糊查询控制要素组
     *
     * @author yuchao.wang
     * @date 2019/8/21 22:10
     * @param requestContext
     * @param dto
     * @param page
     * @param pageSize
     * @return java.util.List<com.hand.spc.pspc_ce_group.dto.CeGroup>
     *
     */
    List<CeGroup> selectCeGroup(IRequest requestContext, CeGroup dto, int page, int pageSize);
}