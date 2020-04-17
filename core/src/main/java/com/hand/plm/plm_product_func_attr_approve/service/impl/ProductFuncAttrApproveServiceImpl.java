package com.hand.plm.plm_product_func_attr_approve.service.impl;

import com.github.pagehelper.PageHelper;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.service.impl.BaseServiceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.hand.plm.plm_product_func_attr_approve.dto.ProductFuncAttrApprove;
import com.hand.plm.plm_product_func_attr_approve.mapper.ProductFuncAttrApproveMapper;
import com.hand.plm.plm_product_func_attr_approve.service.IProductFuncAttrApproveService;
import com.hand.plm.plm_product_func_attr_approve.view.ProductFuncAttrApproveVO;
import com.hand.plm.product_func_attr_basic.mapper.ProductFuncAttrDetailMapper;

import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class ProductFuncAttrApproveServiceImpl extends BaseServiceImpl<ProductFuncAttrApprove> implements IProductFuncAttrApproveService{
	@Autowired
    private ProductFuncAttrApproveMapper mapper;
	
	
	@Override
	public List<ProductFuncAttrApproveVO> queryNew(IRequest request, ProductFuncAttrApproveVO vo, int pageNum,
			int pageSize) {
		// TODO Auto-generated method stub
        PageHelper.startPage(pageNum, pageSize);

		return mapper.queryNew(vo);
	}

}