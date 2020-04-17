package com.hand.hqm.hqm_iqc_inspection_h.mapper;

import java.util.List;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.hqm.hqm_iqc_inspection_h.dto.IqcInspectionH;
import com.hand.hqm.hqm_temporary_inspection.dto.TemporaryInspection;

public interface IqcInspectionHMapper extends Mapper<IqcInspectionH>{
	
	/**
	 * 
	 * @description 获取最大流水号
	 * @author tianmin.wang
	 * @date 2019年11月22日 
	 * @param dto
	 * @return
	 */
	List<IqcInspectionH> selectMaxNumber(IqcInspectionH dto);
	
	/**
	 * 
	 * @description 基础查询
	 * @author tianmin.wang
	 * @date 2019年11月22日 
	 * @param dto
	 * @return
	 */
	List<IqcInspectionH> selectByNumber(IqcInspectionH dto);
	
	
	List<IqcInspectionH> selectFornon(IqcInspectionH dto);
	
	
	List<IqcInspectionH> selectForOther(IqcInspectionH dto);
	
	/**
	 * 
	 * @description 归总条数
	 * @author tianmin.wang
	 * @date 2019年11月22日 
	 * @param dto
	 * @return
	 */
	List<IqcInspectionH> getLimitCount(IqcInspectionH dto);
	/**
	 * 检验单综合查询(iqc)
	 * @param dto
	 * @return
	 */
	List<IqcInspectionH> qmsQuery(IqcInspectionH dto);
	/**
	 * 检验单综合查询（fqc）
	 * @param dto
	 * @return
	 */
	List<IqcInspectionH> qmsFqcQuery(IqcInspectionH dto);
	/**
	 * 检验单综合查询(pqc)
	 * @param dto
	 * @return
	 */
	List<IqcInspectionH> qmsPqcQuery(IqcInspectionH dto);
	
	/**
	 * 
	 * @description 添加临时检验单用到的查询 根据物料组查询
	 * @author tianmin.wang
	 * @date 2019年12月10日 
	 * @param dto
	 * @return
	 */
	List<IqcInspectionH> selectByCategoryId(TemporaryInspection dto);
}