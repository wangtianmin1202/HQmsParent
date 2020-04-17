package com.hand.hqm.hqm_category_inspection_template_h.service;

import javax.servlet.http.HttpServletRequest;

import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;
import com.hand.hqm.hqm_category_inspection_template_h.dto.CategoryInspectionTemplateH;

public interface ICategoryInspectionTemplateHService extends IBaseService<CategoryInspectionTemplateH>, ProxySelf<ICategoryInspectionTemplateHService>{

	/**
	 * @description
	 * @author tianmin.wang
	 * @date 2020年3月30日 
	 * @param request
	 * @param mainType
	 */
	void excelUpload(HttpServletRequest request, String mainType) throws Exception;

}