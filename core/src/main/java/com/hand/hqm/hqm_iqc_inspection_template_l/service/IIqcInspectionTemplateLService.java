package com.hand.hqm.hqm_iqc_inspection_template_l.service;

import java.util.List;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;
import com.hand.hqm.hqm_iqc_inspection_template_l.dto.IqcInspectionTemplateL;

public interface IIqcInspectionTemplateLService extends IBaseService<IqcInspectionTemplateL>, ProxySelf<IIqcInspectionTemplateLService>{

	
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
	List<IqcInspectionTemplateL> myselect(IRequest requestContext, IqcInspectionTemplateL dto, int page, int pageSize);

	/**
	 * 历史版本号自增的更新
	 * @description
	 * @author tianmin.wang
	 * @date 2019年11月22日 
	 * @param request
	 * @param list
	 * @return
	 * @throws Exception
	 */
	List<IqcInspectionTemplateL> historynumberUpdate(IRequest request, List<IqcInspectionTemplateL> list) throws Exception;



	/**
	 * @description
	 * @author tianmin.wang
	 * @date 2020年3月9日 
	 * @param request
	 * @param t
	 * @param eventHId
	 * @return
	 */
	IqcInspectionTemplateL updateByPrimaryKeySelectiveRecord(IRequest request, IqcInspectionTemplateL t,
			Float eventHId);

	/**
	 * @description
	 * @author tianmin.wang
	 * @date 2020年3月9日 
	 * @param request
	 * @param t
	 * @param eventHId
	 * @return
	 */
	IqcInspectionTemplateL insertSelectiveRecord(IRequest request, IqcInspectionTemplateL t, Float eventHId);

	/**
	 * @description
	 * @author tianmin.wang
	 * @date 2020年3月13日 
	 * @param request
	 * @param t
	 * @param eventHId
	 * @return
	 */
	IqcInspectionTemplateL deleteByPrimaryKeyRecoed(IRequest request, IqcInspectionTemplateL t, Float eventHId);

	/**
	 * @description
	 * @author tianmin.wang
	 * @date 2020年3月13日 
	 * @param request
	 * @param list
	 * @return
	 */
	int reBatchDelete(IRequest request, List<IqcInspectionTemplateL> list);

}