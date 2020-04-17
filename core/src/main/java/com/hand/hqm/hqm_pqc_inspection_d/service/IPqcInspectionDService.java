package com.hand.hqm.hqm_pqc_inspection_d.service;

import java.util.List;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;
import com.hand.hqm.hqm_pqc_inspection_c.dto.PqcInspectionC;
import com.hand.hqm.hqm_pqc_inspection_d.dto.PqcInspectionD;

public interface IPqcInspectionDService extends IBaseService<PqcInspectionD>, ProxySelf<IPqcInspectionDService>{
	
	
	/**
	 * 批量创建
	 * @description
	 * @author tianmin.wang
	 * @date 2019年11月22日 
	 * @param requestCtx
	 * @param pqcInspectionC
	 * @return
	 */
	List<PqcInspectionD> batchCreateByInspectionC(IRequest requestCtx,PqcInspectionC pqcInspectionC);

	/**
	 * 批量删除
	 * @description
	 * @author tianmin.wang
	 * @date 2019年11月22日 
	 * @param addorInsc
	 */
	void batchDeleteInspectionC(PqcInspectionC addorInsc);
}