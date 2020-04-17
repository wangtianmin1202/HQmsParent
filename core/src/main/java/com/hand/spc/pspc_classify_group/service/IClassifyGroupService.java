package com.hand.spc.pspc_classify_group.service;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.system.service.IBaseService;
import com.hand.spc.pspc_classify_group.dto.ClassifyGroup;

public interface IClassifyGroupService extends IBaseService<ClassifyGroup>, ProxySelf<IClassifyGroupService>{
    /**
     * @Author han.zhang
     * @Description 删除分类组及其下的分类项和控制要素关系
     * @Date 17:03 2019/8/8
     * @Param [request, dto]
     */
    ResponseData deleteGroupAndLine(IRequest request,ClassifyGroup dto);

    /**
     * @Author han.zhang
     * @Description 分类组保存，新建、修改、副本保存
     * @Date 18:02 2019/8/8
     * @Param [dto]
     */
    ResponseData saveOrUpdate(ClassifyGroup dto);
}