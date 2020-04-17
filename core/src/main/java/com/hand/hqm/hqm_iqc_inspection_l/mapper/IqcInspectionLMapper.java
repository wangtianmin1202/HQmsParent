package com.hand.hqm.hqm_iqc_inspection_l.mapper;

import java.util.List;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.hqm.hqm_iqc_inspection_l.dto.IqcInspectionL;

public interface IqcInspectionLMapper extends Mapper<IqcInspectionL>{

	
	/**
	 * 
	 * @description 基础查询
	 * @author tianmin.wang
	 * @date 2019年11月22日 
	 * @param dto
	 * @return
	 */
	List<IqcInspectionL> reSelect(IqcInspectionL dto);
	
	
	
	List<IqcInspectionL> reSelectFornon(IqcInspectionL dto);
	
	/**
	 * 
	 * @description
	 * @author tianmin.wang
	 * @date 2020年2月20日 
	 * @param dto
	 * @return
	 */
	List<IqcInspectionL> timeItemSelectN(IqcInspectionL dto);



	/**
	 * @description
	 * @author tianmin.wang
	 * @date 2020年3月30日 
	 * @param dto
	 * @return
	 */
	List<IqcInspectionL> selectAttribute(IqcInspectionL dto);
}