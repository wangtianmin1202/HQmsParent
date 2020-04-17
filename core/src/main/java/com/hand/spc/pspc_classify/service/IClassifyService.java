package com.hand.spc.pspc_classify.service;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.system.service.IBaseService;
import com.hand.spc.pspc_classify.dto.Classify;

import java.util.List;

public interface IClassifyService extends IBaseService<Classify>, ProxySelf<IClassifyService>{
    /**
     * @Author han.zhang
     * @Description 根据分类组id查询分类项
     * @Date 9:52 2019/8/7
     * @Param [requestContext, dto, page, pageSize]
     */
    ResponseData selectClassify(IRequest requestContext, Classify dto, int page, int pageSize);
    /**
     * @Author han.zhang
     * @Description 分类项新增或保存
     * @Date 20:41 2019/8/7
     * @Param [requestContext, dto]
     */
    ResponseData saveAndUpdateClassify(IRequest requestContext, Classify dto);
    /**
     * @Author han.zhang
     * @Description 根据控制要素id找对应的分类项
     * @Date 13:48 2019/8/13
     * @Param [requestContext, dto]
     */
    ResponseData getClassifyByParameterId(IRequest requestContext, Classify dto);
}