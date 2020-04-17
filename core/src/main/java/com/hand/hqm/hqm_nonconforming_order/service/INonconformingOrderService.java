package com.hand.hqm.hqm_nonconforming_order.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.system.service.IBaseService;
import com.hand.hqm.hqm_iqc_inspection_h.dto.IqcInspectionH;
import com.hand.hqm.hqm_nonconforming_order.dto.NonconformingOrder;
import com.hand.hqm.hqm_supp_item_exemption.dto.SuppItemExemption;

public interface INonconformingOrderService
		extends IBaseService<NonconformingOrder>, ProxySelf<INonconformingOrderService> {
	/**
	 * 页面查询
	 * 
	 * @param dto      查询内容
	 * @param page     页码
	 * @param pageSize 页大小
	 * @param request  请求
	 * @return 结果集
	 */
	List<NonconformingOrder> myselect(IRequest requestContext, NonconformingOrder dto, int page, int pageSize);

	/**
	 * 新建
	 * 
	 * @description
	 * @date 2019年11月22日
	 * @param dto
	 * @param request
	 * @return
	 */
	ResponseData addNewInspection(NonconformingOrder dto, IRequest requestCtx, HttpServletRequest request);

	/**
	 * 更新
	 * 
	 * @param dto     操作数据集
	 * @param result  结果参数
	 * @param request 请求
	 * @return 操作结果
	 */
	ResponseData updateInspection(NonconformingOrder dto, IRequest requestCtx, HttpServletRequest request);

	/**
	 * 发布
	 * 
	 * @param dto     操作数据集
	 * @param request 请求
	 * @return 操作结果
	 */
	ResponseData publish(NonconformingOrder dto, IRequest requestCtx, HttpServletRequest request);

	/**
	 * @description 执行审核
	 * @author tianmin.wang
	 * @date 2019年12月24日
	 * @param requestCtx
	 * @param dto
	 */
	void audit(IRequest requestCtx, List<NonconformingOrder> dto , HttpServletRequest request);

	/**
	 * @description 拒绝
	 * @author tianmin.wang
	 * @date 2019年12月24日 
	 * @param requestCtx
	 * @param dto
	 */
	void refuse(IRequest requestCtx, List<NonconformingOrder> dto);

	/**
	 * @description
	 * @author tianmin.wang
	 * @date 2020年4月1日 
	 * @param dto
	 */
	void pushToMes(NonconformingOrder dto);
}