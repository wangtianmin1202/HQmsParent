package com.hand.hqm.file_classify.mapper;

import java.util.List;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.hqm.file_classify.dto.Classify;


public interface FileClassifyMapper extends Mapper<Classify>{
	/** 
	 * @Description 查询
	 * @Param [dto, result, request]
	 */
	  List<Classify> myselect(Classify dto);
	  /**
		 * @Author han.zhang
		 * @Description 查询父节点
		 * @Date 18:10 2019/8/19
		 * @Param [dto, result, request]
		 */
	  List<Classify> selectParentInvalid(Classify dto);
	  /**
		 * @Author han.zhang
		 * @Description 更具父节点查询
		 * @Date 18:10 2019/8/19
		 * @Param [dto, result, request]
		 */
	  List<Classify> selectInvalidByParent(Classify dto);
	  /**
		 * @Author han.zhang
		 * @Description 查询第一层级数据
		 * @Date 18:10 2019/8/19
		 * @Param [dto, result, request]
		 */
	  List<Classify> myselectone(Classify dto);
	  /**
		 * @Author han.zhang
		 * @Description 查询第二层级数据
		 * @Date 18:10 2019/8/19
		 * @Param [dto, result, request]
		 */
	  List<Classify> myselecttwo(Classify dto);
	  /**
		 * @Author han.zhang
		 * @Description 查询第三层级数据
		 * @Date 18:10 2019/8/19
		 * @Param [dto, result, request]
		 */
	  List<Classify> myselectthr(Classify dto);
	  /**
		 * @Author han.zhang
		 * @Description 查询叶子节点信息
		 * @Date 18:10 2019/8/19
		 * @Param [dto, result, request]
		 */
	  List<Classify> myselectforleaf(Classify dto);
	  
	  /**
	   * 
	   * @Description:条件查询
	   * @param dto
	   * @return
	   */
	  List<Classify> queryByCondition(Classify dto);
}