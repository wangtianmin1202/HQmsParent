package com.hand.plm.product_func_attr_basic.service.impl;

import com.github.pagehelper.PageHelper;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.service.impl.BaseServiceImpl;

import com.hand.plm.product_func_attr_basic.view.UpdateDetailVO;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.hand.plm.product_func_attr_basic.dto.ProductFuncAttrDetail;
import com.hand.plm.product_func_attr_basic.mapper.ProductFuncAttrDetailMapper;
import com.hand.plm.product_func_attr_basic.service.IProductFuncAttrDetailService;
import com.hand.plm.product_func_attr_basic.view.ProductFuncAttrDetailVO;

import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class ProductFuncAttrDetailServiceImpl extends BaseServiceImpl<ProductFuncAttrDetail>
        implements IProductFuncAttrDetailService {
    @Autowired
    private ProductFuncAttrDetailMapper mapper;

    @Override
    public List<ProductFuncAttrDetailVO> queryDetail(IRequest request, ProductFuncAttrDetailVO vo, int pageNum,
                                                     int pageSize) {
        // TODO Auto-generated method stub
        PageHelper.startPage(pageNum, pageSize);
        return mapper.queryDetail(vo);
    }

    @Override
    public List<UpdateDetailVO> queryUpdateDetail(IRequest request, UpdateDetailVO vo, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        return mapper.getUpdateDetailData(vo);
    }

}