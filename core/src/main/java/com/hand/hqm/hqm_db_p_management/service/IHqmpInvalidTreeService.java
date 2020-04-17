package com.hand.hqm.hqm_db_p_management.service;

import java.util.List;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.system.service.IBaseService;
import com.hand.hqm.hqm_db_p_management.dto.HQMPInvalidTree;
import com.hand.hqm.hqm_db_p_management.dto.HqmpdbMenuItem;

public interface IHqmpInvalidTreeService extends IBaseService<HQMPInvalidTree>, ProxySelf<IHqmpInvalidTreeService> {

    /**
     * @Author han.zhang
     * @Description 查询附着对象层级维护 树形图数据
     * @Date 11:40 2019/8/19
     * @Param [requestContext, dto]
     */
    List<HqmpdbMenuItem> queryTreeData(IRequest requestContext, HQMPInvalidTree dto);

    /**
     * @Author han.zhang
     * @Description 更新或者保存附着对象
     * @Date 19:40 2019/8/19
     * @Param [requestCtx, dto]
     */
    ResponseData updateOrAdd(IRequest requestCtx, HQMPInvalidTree dto);

    void deleteRow(HQMPInvalidTree dto);
}