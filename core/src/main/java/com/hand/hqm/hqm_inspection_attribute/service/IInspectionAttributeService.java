package com.hand.hqm.hqm_inspection_attribute.service;

import java.io.InputStream;
import java.util.Collection;
import java.util.List;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;
import com.hand.hqm.hqm_inspection_attribute.dto.InspectionAttribute;

public interface IInspectionAttributeService extends IBaseService<InspectionAttribute>, ProxySelf<IInspectionAttributeService>{

	/**
	 * 
	 * @description excel数据保存
	 * @author tianmin.wang
	 * @date 2019年11月22日 
	 * @param requestCtx
	 * @param inputStream
	 * @return
	 * @throws Exception
	 */
	List<InspectionAttribute> inputDataFromExcel(IRequest requestCtx, InputStream inputStream) throws Exception;

	
	/**
	 * 
	 * @description 重写的查询
	 * @author tianmin.wang
	 * @date 2019年11月22日 
	 * @param requestContext
	 * @param dto
	 * @param page
	 * @param pageSize
	 * @return
	 */
	List<InspectionAttribute> reSelect(IRequest requestContext, InspectionAttribute dto, int page, int pageSize);


	/**
	 * @description
	 * @author tianmin.wang
	 * @date 2020年3月5日 
	 * @param requestCtx
	 * @param dto
	 * @return
	 */
	List<InspectionAttribute> reBatchUpdate(IRequest requestCtx, List<InspectionAttribute> dto);


	/**
	 * @description
	 * @author tianmin.wang
	 * @date 2020年3月5日 
	 * @param request
	 * @param t
	 * @return
	 */
	InspectionAttribute insertSelectiveRecord(IRequest request, InspectionAttribute t);


	/**
	 * @description
	 * @author tianmin.wang
	 * @date 2020年3月5日 
	 * @param request
	 * @param t
	 * @return
	 */
	InspectionAttribute updateByPrimaryKeySelectiveRecord(IRequest request, InspectionAttribute t);

}