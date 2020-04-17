package com.hand.dimension.hqm_dimension_order.service;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.system.service.IBaseService;
import com.hand.hqm.hqm_fqc_inspection_h.dto.FqcInspectionH;
import com.hand.hqm.hqm_iqc_inspection_h.dto.IqcInspectionH;
import com.hand.hqm.hqm_pqc_inspection_h.dto.PqcInspectionH;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.hand.dimension.hqm_dimension_order.dto.DimensionOrder;

public interface IDimensionOrderService extends IBaseService<DimensionOrder>, ProxySelf<IDimensionOrderService>{

	/**
	 * 
	 * @description 查询
	 * @author tianmin.wang
	 * @date 2019年11月21日 
	 * @param requestContext
	 * @param dto
	 * @param page
	 * @param pageSize
	 * @return
	 */
	List<DimensionOrder> reselect(IRequest requestContext, DimensionOrder dto, int page, int pageSize);

	/**
	 * 
	 * @description 添加新8d
	 * @author tianmin.wang
	 * @date 2019年11月21日 
	 * @param requestContext
	 * @param dto
	 * @return
	 */
	DimensionOrder addNew(IRequest requestContext,DimensionOrder dto);
	
	/**
	 * 
	 * @description 修改执行步骤
	 * @author tianmin.wang
	 * @date 2019年11月21日 
	 * @param requestContext
	 * @param orderId
	 * @param currentStep
	 * @param nextStep
	 * @return
	 * @throws Exception
	 */
	boolean changeStep(IRequest requestContext,Float orderId,int currentStep,int nextStep) throws Exception;

	/**
	 * 
	 * @description 批量关闭
	 * @author tianmin.wang
	 * @date 2019年11月21日 
	 * @param request
	 * @param requestCtx
	 * @param dto
	 * @return
	 * @throws Exception
	 */
	int batchClose(HttpServletRequest request, IRequest requestCtx, List<DimensionOrder> dto) throws Exception;

	/**
	 * 
	 * @description 提交
	 * @author tianmin.wang
	 * @date 2019年11月21日 
	 * @param request
	 * @param requestCtx
	 * @param dto
	 * @return
	 * @throws Exception
	 */
	int commit(HttpServletRequest request, IRequest requestCtx, DimensionOrder dto) throws Exception;

	/**
	 *  
	 * @description 保存单个数据
	 * @author tianmin.wang
	 * @date 2019年11月21日 
	 * @param request
	 * @param requestCtx
	 * @param dto
	 * @return
	 */
	int saveOne(HttpServletRequest request, IRequest requestCtx, DimensionOrder dto);

	/**
	 * 
	 * @description 改变某一步骤的执行状态 status
	 * @author tianmin.wang
	 * @date 2019年11月21日 
	 * @param request
	 * @param orderId
	 * @param currentStep
	 * @return
	 * @throws Exception
	 */
	boolean changeStepStatus(IRequest request, Float orderId, int currentStep) throws Exception;

	/**
	 * 
	 * @description fqc oqc 发起8D
	 * @author tianmin.wang
	 * @date 2019年11月21日 
	 * @param request
	 * @param requestCtx
	 * @param dto
	 * @return
	 * @throws Exception
	 */
	ResponseData foSponsor(HttpServletRequest request, IRequest requestCtx, FqcInspectionH dto) throws Exception;

	/**
	 * 
	 * @description iqc 发起8D
	 * @author tianmin.wang
	 * @date 2019年11月21日 
	 * @param request
	 * @param requestCtx
	 * @param dto
	 * @return
	 * @throws Exception
	 */
	ResponseData iSponsor(HttpServletRequest request, IRequest requestCtx, IqcInspectionH dto) throws Exception;

	/**
	 * pqc发起8D
	 * @description
	 * @author tianmin.wang
	 * @date 2019年11月21日 
	 * @param request
	 * @param requestCtx
	 * @param dto
	 * @return
	 * @throws Exception
	 */
	ResponseData pSponsor(HttpServletRequest request, IRequest requestCtx, PqcInspectionH dto) throws Exception;
}