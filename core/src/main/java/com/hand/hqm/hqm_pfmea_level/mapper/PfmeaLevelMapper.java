package com.hand.hqm.hqm_pfmea_level.mapper;

import java.util.List;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.hqm.hqm_pfmea_level.dto.PfmeaLevel;

public interface PfmeaLevelMapper extends Mapper<PfmeaLevel>{
	 /**
     * 根据父亲ID查询儿子节点
     * @param dto 查询内容
     * @return 结果集
     */
	List<PfmeaLevel> selectParentInvalid(PfmeaLevel dto);
	 /**
     * 根据ID查询节点
     * @param dto 
     * @return 结果集
     */
	List<PfmeaLevel> selectInvalidByParent(PfmeaLevel dto);

	/**
	 * @Description: 根据levelCode查询层级数据
	 * @param levelCode
	 * @return List<PfmeaLevel>
	 */
	List<PfmeaLevel> selectFmeaLevelByLevelCode(String levelCode);
}