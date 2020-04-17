package com.hand.hqm.hqm_pqc_inspection_l.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.system.service.IBaseService;
import com.hand.hqm.hqm_pqc_inspection_l.dto.PqcInspectionL;

public interface IPqcInspectionLService extends IBaseService<PqcInspectionL>, ProxySelf<IPqcInspectionLService>{

	
	/**
	 * 基础查询
	 * @description
	 * @author tianmin.wang
	 * @date 2019年11月22日 
	 * @param requestContext
	 * @param dto
	 * @param page
	 * @param pageSize
	 * @return
	 */
	List<PqcInspectionL> myselect(IRequest requestContext, PqcInspectionL dto, int page, int pageSize);

	
	/**
	 * 明细保存
	 * @description
	 * @author tianmin.wang
	 * @date 2019年11月22日 
	 * @param dto
	 * @param request
	 * @return
	 * @throws Exception
	 */
	ResponseData detailSave(PqcInspectionL dto, HttpServletRequest request) throws Exception;
	
	

	List<PqcInspectionL> selectfornon(IRequest requestContext, PqcInspectionL dto, int page, int pageSize);

}