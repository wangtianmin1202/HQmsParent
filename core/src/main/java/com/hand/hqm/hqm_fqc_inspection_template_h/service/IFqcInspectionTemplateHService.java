package com.hand.hqm.hqm_fqc_inspection_template_h.service;

import java.util.List;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;
import com.hand.hqm.hqm_fqc_inspection_template_h.dto.FqcInspectionTemplateH;

public interface IFqcInspectionTemplateHService extends IBaseService<FqcInspectionTemplateH>, ProxySelf<IFqcInspectionTemplateHService>{
	/**
	 * 
	 * @description 查询
	 * @author tianmin.wang
	 * @date 2019年11月22日 
	 * @param requestContext
	 * @param dto
	 * @param page
	 * @param pageSize
	 * @return
	 */
	List<FqcInspectionTemplateH> myselect(IRequest requestContext, FqcInspectionTemplateH dto, int page, int pageSize);

	/**
	 * 
	 * @description 版本号自增的批量更新
	 * @author tianmin.wang
	 * @date 2019年11月22日 
	 * @param requestCtx
	 * @param dto
	 * @return
	 */
	List<FqcInspectionTemplateH> versionNumberBatchUpdate(IRequest requestCtx, List<FqcInspectionTemplateH> dto);

	/**
	 * 
	 * @description 更新状态
	 * @author tianmin.wang
	 * @date 2019年11月22日 
	 * @param requestCtx
	 * @param dto
	 */
	void updateStatus(IRequest requestCtx, List<FqcInspectionTemplateH> dto);

	/**
	 * 
	 * @description 批量删除
	 * @author tianmin.wang
	 * @date 2019年11月22日 
	 * @param dto
	 * @return
	 */
	int reBatchDelete(List<FqcInspectionTemplateH> dto);

}