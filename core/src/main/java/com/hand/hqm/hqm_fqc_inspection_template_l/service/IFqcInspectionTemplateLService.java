package com.hand.hqm.hqm_fqc_inspection_template_l.service;

import java.util.List;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;
import com.hand.hqm.hqm_fqc_inspection_template_l.dto.FqcInspectionTemplateL;

public interface IFqcInspectionTemplateLService extends IBaseService<FqcInspectionTemplateL>, ProxySelf<IFqcInspectionTemplateLService>{
	
	/**
	 * 
	 * @description 基础查询
	 * @author tianmin.wang
	 * @date 2019年11月22日 
	 * @param requestContext
	 * @param dto
	 * @param page
	 * @param pageSize
	 * @return
	 */
	List<FqcInspectionTemplateL> myselect(IRequest requestContext, FqcInspectionTemplateL dto, int page, int pageSize);

	
	/**
	 * 
	 * @description 带更新historynumber的更新
	 * @author tianmin.wang
	 * @date 2019年11月22日 
	 * @param request
	 * @param list
	 * @return
	 */
	List<FqcInspectionTemplateL> historynumberUpdate(IRequest request, List<FqcInspectionTemplateL> list);
}