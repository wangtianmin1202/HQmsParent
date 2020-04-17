package com.hand.hqm.hqm_fqc_inspection_d.service;

import java.util.List;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;
import com.hand.hqm.hqm_fqc_inspection_d.dto.FqcInspectionD;
import com.hand.hqm.hqm_fqc_inspection_l.dto.FqcInspectionL;

public interface IFqcInspectionDService extends IBaseService<FqcInspectionD>, ProxySelf<IFqcInspectionDService>{
	/**
	 * 
	 * @description 根据L表的sampleQty生成对应提条数的D表数据
	 * @author tianmin.wang
	 * @date 2019年11月22日 
	 * @param requestCtx
	 * @param fqcInspectionL
	 * @return
	 */
	List<FqcInspectionD> batchCreateByInspectionL(IRequest requestCtx,FqcInspectionL fqcInspectionL);
}