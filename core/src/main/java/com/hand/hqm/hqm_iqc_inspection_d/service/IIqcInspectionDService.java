package com.hand.hqm.hqm_iqc_inspection_d.service;

import java.util.List;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;
import com.hand.hqm.hqm_iqc_inspection_d.dto.IqcInspectionD;
import com.hand.hqm.hqm_iqc_inspection_l.dto.IqcInspectionL;

public interface IIqcInspectionDService extends IBaseService<IqcInspectionD>, ProxySelf<IIqcInspectionDService>{
	/**
	 * 
	 * @description 基于L表的批量创建
	 * @author tianmin.wang
	 * @date 2019年11月22日 
	 * @param requestCtx
	 * @param iqcInspectionL
	 * @return
	 */
	List<IqcInspectionD> batchCreateByInspectionL(IRequest requestCtx,IqcInspectionL iqcInspectionL);
}