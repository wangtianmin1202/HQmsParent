package com.hand.plm.plm_product_design_standard.service;

import java.util.List;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;
import com.hand.plm.plm_product_design_standard.dto.ProductDesignStandard;
import com.hand.plm.product_func_attr_basic.view.TreeVO;

public interface IProductDesignStandardService
		extends IBaseService<ProductDesignStandard>, ProxySelf<IProductDesignStandardService> {
	/**
	 * 
	 * @param iRequest
	 * @return
	 */
	List<TreeVO> selectTreeDatas(IRequest iRequest);

	/**
	 * 修改树节点名称
	 * 
	 * @param iRequest
	 * @param treeVO
	 */
	void renameTree(IRequest iRequest, TreeVO treeVO);

	void removeTree(IRequest iRequest, TreeVO treeVO);

	TreeVO addTree(IRequest iRequest, TreeVO treeVO);

	/**
	 * 零件下拉框取值
	 * 
	 * @param iRequest
	 * @return
	 */
	List<ProductDesignStandard> queryrelatedParts(IRequest iRequest);
}