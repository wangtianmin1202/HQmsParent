package com.hand.plm.plm_product_design_standard.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.plm.plm_product_design_standard.dto.ProductDesignStandard;
import com.hand.plm.product_func_attr_basic.view.TreeVO;

public interface ProductDesignStandardMapper extends Mapper<ProductDesignStandard> {
	/**
	 * 查询树
	 * 
	 * @return
	 */
	List<TreeVO> selectTreeDatas();

	/**
	 * 通过父节点查找它下边所有的子节点（包含父节点本身）
	 * 
	 * @param kid 父节点
	 * @return
	 */
	List<ProductDesignStandard> getAllTreeByPid(@Param("kid") String kid);
	
	/**
	 * 通过层级关系，获取某一层下所有的树，包含该层
	 * @param levelNum
	 * @return
	 */
	List<ProductDesignStandard> getAllTreeByLevelNum(@Param("levelNum") String levelNum);
	
}