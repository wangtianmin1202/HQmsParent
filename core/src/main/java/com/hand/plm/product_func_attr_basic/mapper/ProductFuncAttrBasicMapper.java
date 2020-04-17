package com.hand.plm.product_func_attr_basic.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.plm.product_func_attr_basic.dto.ProductFuncAttrBasic;
import com.hand.plm.product_func_attr_basic.view.TreeVO;

public interface ProductFuncAttrBasicMapper extends Mapper<ProductFuncAttrBasic> {
	/**
	 * 查询产品树
	 * 
	 * @return
	 */
	List<TreeVO> selectTreeDatas();

	/**
	 * 根据参数查询产品树结
	 * 
	 * @param param 查询参数
	 * @return
	 */
	List<TreeVO> selectTreeDatasByParms(@Param("param") String param);
}