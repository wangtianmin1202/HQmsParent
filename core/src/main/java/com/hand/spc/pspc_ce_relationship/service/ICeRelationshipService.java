package com.hand.spc.pspc_ce_relationship.service;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.system.service.IBaseService;
import com.hand.spc.pspc_ce_relationship.dto.CeRelationship;

import java.util.List;

public interface ICeRelationshipService extends IBaseService<CeRelationship>, ProxySelf<ICeRelationshipService> {

    /**
     *
     * @Description 删除控制要素与控制要素组关系
     *
     * @author yuchao.wang
     * @date 2019/8/9 23:08
     * @param dto
     * @return com.hand.hap.system.dto.ResponseData
     *
     */
    ResponseData deleteRelationship(List<CeRelationship> dto) throws Exception;

    /**
     *
     * @Description 更新控制要素与控制要素组关系
     *
     * @author yuchao.wang
     * @date 2019/9/5 14:13
     * @param requestCtx
     * @param dto
     * @return com.hand.hap.system.dto.ResponseData
     *
     */
    ResponseData updateRelationship(IRequest requestCtx, List<CeRelationship> dto);
}