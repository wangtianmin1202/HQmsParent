package com.hand.plm.product_func_attr_basic.service;

import java.util.List;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;
import com.hand.plm.product_func_attr_basic.dto.ProductFuncAttrBasic;
import com.hand.plm.product_func_attr_basic.view.TreeVO;

public interface IProductFuncAttrBasicService
		extends IBaseService<ProductFuncAttrBasic>, ProxySelf<IProductFuncAttrBasicService> {
	/**
	 * 
	 * @param iRequest
	 * @return
	 */
	List<TreeVO> selectTreeDatas(IRequest iRequest);

	/**
	 * 根据参数查询产品树结
	 * 
	 * @param iRequest
	 * @param param    查询参数
	 * @return
	 */
	List<TreeVO> selectTreeDatasByParms(IRequest iRequest, String param);

	/**
	 * 查询下拉框
	 * 
	 * @param iRequest
	 * @param dto
	 * @return
	 */
	List<ProductFuncAttrBasic> queryLevelNum(IRequest iRequest, ProductFuncAttrBasic dto);

	/**
	 * 修改树节点名称
	 * 
	 * @param iRequest
	 * @param treeVO
	 */
	void renameTree(IRequest iRequest, TreeVO treeVO);

	void removeTree(IRequest iRequest, TreeVO treeVO);

	TreeVO addTree(IRequest iRequest, TreeVO treeVO);

}
