package com.hand.hqm.hqm_nonconforming_order.controllers;

import org.springframework.stereotype.Controller;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hqm.hqm_iqc_inspection_h.dto.IqcInspectionH;
import com.hand.hqm.hqm_nonconforming_order.dto.NonconformingOrder;
import com.hand.hqm.hqm_nonconforming_order.service.INonconformingOrderService;
import com.hand.hqm.hqm_supp_item_exemption.dto.SuppItemExemption;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import org.springframework.validation.BindingResult;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
public class NonconformingOrderController extends BaseController {

	@Autowired
	private INonconformingOrderService service;

	/**
	 * 页面查询
	 * 
	 * @param dto      查询内容
	 * @param page     页码
	 * @param pageSize 页大小
	 * @param request  请求
	 * @return 结果集
	 */
	@RequestMapping(value = "/hqm/nonconforming/order/query")
	@ResponseBody
	public ResponseData query(NonconformingOrder dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
			@RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
		IRequest requestContext = createRequestContext(request);
		return new ResponseData(service.select(requestContext, dto, page, pageSize));
	}

	/**
	 * 提交
	 * 
	 * @param dto     操作数据集
	 * @param result  结果参数
	 * @param request 请求
	 * @return 操作结果
	 */
	@RequestMapping(value = "/hqm/nonconforming/order/submit")
	@ResponseBody
	public ResponseData update(@RequestBody List<NonconformingOrder> dto, BindingResult result,
			HttpServletRequest request) {
		getValidator().validate(dto, result);
		if (result.hasErrors()) {
			ResponseData responseData = new ResponseData(false);
			responseData.setMessage(getErrorMessage(result, request));
			return responseData;
		}
		IRequest requestCtx = createRequestContext(request);
		return new ResponseData(service.batchUpdate(requestCtx, dto));
	}

	/**
	 * 删除
	 * 
	 * @param request
	 * @param dto
	 * @return
	 */
	@RequestMapping(value = "/hqm/nonconforming/order/remove")
	@ResponseBody
	public ResponseData delete(HttpServletRequest request, @RequestBody List<NonconformingOrder> dto) {
		service.batchDelete(dto);
		return new ResponseData();
	}

	/**
	 * 页面查询
	 * 
	 * @param dto      查询内容
	 * @param page     页码
	 * @param pageSize 页大小
	 * @param request  请求
	 * @return 结果集
	 */
	@RequestMapping(value = "/hqm/nonconforming/order/select")
	@ResponseBody
	public ResponseData select(NonconformingOrder dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
			@RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
		IRequest requestContext = createRequestContext(request);
		return new ResponseData(service.myselect(requestContext, dto, page, pageSize));
	}

	/**
	 * 新建
	 * 
	 * @description
	 * 
	 * @date 2019年11月22日
	 * @param dto
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/hqm/nonconforming/order/addnew")
	@ResponseBody
	public ResponseData addNewInspection(NonconformingOrder dto, HttpServletRequest request) {
		ResponseData responseData = new ResponseData();
		IRequest requestContext = createRequestContext(request);
		try {
			responseData = service.addNewInspection(dto, requestContext, request);
		} catch (Exception e) {
			responseData.setSuccess(false);
			responseData.setMessage(e.getMessage());
		}
		return responseData;
	}

	/**
	 * 更新
	 * 
	 * @param dto     操作数据集
	 * @param result  结果参数
	 * @param request 请求
	 * @return 操作结果
	 */
	@RequestMapping(value = "/hqm/nonconforming/order/update")
	@ResponseBody
	public ResponseData updateInspection(NonconformingOrder dto, HttpServletRequest request) {
		ResponseData responseData = new ResponseData();
		IRequest requestContext = createRequestContext(request);
		try {
			responseData = service.updateInspection(dto, requestContext, request);
		} catch (RuntimeException e) {
			responseData.setSuccess(false);
			responseData.setMessage(e.getMessage());
		} catch (Exception e) {
			responseData.setSuccess(false);
			responseData.setMessage(e.getMessage());
		}
		return responseData;
	}

	/**
	 * 发布
	 * 
	 * @param dto     操作数据集
	 * @param request 请求
	 * @return 操作结果
	 */
	@RequestMapping(value = "/hqm/nonconforming/order/publish")
	@ResponseBody
	public ResponseData publish(NonconformingOrder dto, HttpServletRequest request) {
		ResponseData responseData = new ResponseData();
		IRequest requestContext = createRequestContext(request);
		try {
			responseData = service.publish(dto, requestContext, request);
		} catch (Exception e) {
			responseData.setSuccess(false);
			responseData.setMessage(e.getMessage());
		}
		return responseData;
	}

	/**
	 * 
	 * @description 不合格单据审核
	 * @author tianmin.wang
	 * @date 2019年12月24日
	 * @param dto
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/hqm/nonconforming/order/audit")
	@ResponseBody
	public ResponseData audit(@RequestBody List<NonconformingOrder> dto, HttpServletRequest request) {
		IRequest requestCtx = createRequestContext(request);
		ResponseData responseData = new ResponseData();
		try {
			service.audit(requestCtx, dto , request);
		} catch (RuntimeException e) {
			responseData.setSuccess(false);
			responseData.setMessage(e.getMessage());
		} catch (Exception e) {
			responseData.setSuccess(false);
			responseData.setMessage(e.getMessage());
		}
		return responseData;
	}

	/**
	 * 
	 * @description 拒绝
	 * @author tianmin.wang
	 * @date 2019年12月24日
	 * @param dto
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/hqm/nonconforming/order/refuse")
	@ResponseBody
	public ResponseData refuse(@RequestBody List<NonconformingOrder> dto, HttpServletRequest request) {
		IRequest requestCtx = createRequestContext(request);
		ResponseData responseData = new ResponseData();
		try {
			service.refuse(requestCtx, dto);
		} catch (RuntimeException e) {
			responseData.setSuccess(false);
			responseData.setMessage(e.getMessage());
		} catch (Exception e) {
			throw e;
		}
		return responseData;
	}

}