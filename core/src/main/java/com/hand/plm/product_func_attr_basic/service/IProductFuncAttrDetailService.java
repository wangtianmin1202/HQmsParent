package com.hand.plm.product_func_attr_basic.service;

import com.hand.plm.product_func_attr_basic.view.UpdateDetailVO;
import java.util.List;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;
import com.hand.plm.product_func_attr_basic.dto.ProductFuncAttrDetail;
import com.hand.plm.product_func_attr_basic.view.ProductFuncAttrDetailVO;

public interface IProductFuncAttrDetailService
        extends IBaseService<ProductFuncAttrDetail>, ProxySelf<IProductFuncAttrDetailService> {

    /**
     * 查询产品功能属性明细
     *
     * @param request
     * @param vo
     * @param pageNum
     * @param pageSize
     * @return
     */
    List<ProductFuncAttrDetailVO> queryDetail(IRequest request, ProductFuncAttrDetailVO vo, int pageNum, int pageSize);

    List<UpdateDetailVO> queryUpdateDetail(IRequest request, UpdateDetailVO vo, int pageNum, int pageSize);
}