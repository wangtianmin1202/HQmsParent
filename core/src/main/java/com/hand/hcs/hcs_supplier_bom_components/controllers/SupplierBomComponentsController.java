package com.hand.hcs.hcs_supplier_bom_components.controllers;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hand.hap.core.IRequest;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hcs.hcs_supplier_bom_components.dto.SupplierBomComponents;
import com.hand.hcs.hcs_supplier_bom_components.service.ISupplierBomComponentsService;

@Controller
public class SupplierBomComponentsController extends BaseController {

	@Autowired
	private ISupplierBomComponentsService service;

	/**
	 * 页面查询
	 * 
	 * @param dto      查询内容
	 * @param page     页码
	 * @param pageSize 页大小
	 * @param request  请求
	 * @return 结果集
	 */
	@RequestMapping(value = "/hcs/supplier/bom/components/query")
	@ResponseBody
	public ResponseData query(SupplierBomComponents dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
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
	@RequestMapping(value = "/hcs/supplier/bom/components/submit")
	@ResponseBody
	public ResponseData update(@RequestBody List<SupplierBomComponents> dto, BindingResult result,
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
	@RequestMapping(value = "/hcs/supplier/bom/components/remove")
	@ResponseBody
	public ResponseData delete(HttpServletRequest request, @RequestBody List<SupplierBomComponents> dto) {
		service.batchDelete(dto);
		return new ResponseData();
	}

	/**
	 * 关键件校验
	 * 
	 * @param request
	 * @param dto
	 * @return
	 */
	@RequestMapping(value = "/hcs/supplier/bom/components/checkData")
	@ResponseBody
	public ResponseData checkData(HttpServletRequest request, SupplierBomComponents dto) {
		ResponseData responseData = new ResponseData();
		IRequest requestCtx = createRequestContext(request);
		try {
			SupplierBomComponents supplierBomComponents = service.checkData(requestCtx, dto);
			List<SupplierBomComponents> list = new ArrayList();
			list.add(supplierBomComponents);
			responseData.setRows(list);
		} catch (RuntimeException e) {
			responseData.setMessage(e.getMessage());
			responseData.setSuccess(false);
			return responseData;
		} catch (Exception e) {
			throw e;
		}
		return responseData;
	}

	/**
	 * 新增关键件
	 * 
	 * @param request
	 * @param dto
	 * @return
	 */
	@RequestMapping(value = "/hcs/supplier/bom/components/addData")
	@ResponseBody
	public ResponseData addData(HttpServletRequest request, SupplierBomComponents dto) {
		ResponseData responseData = new ResponseData();
		IRequest requestCtx = createRequestContext(request);
		try {
			SupplierBomComponents supplierBomComponents = service.addData(requestCtx, dto);
			List<SupplierBomComponents> list = new ArrayList();
			list.add(supplierBomComponents);
			responseData.setRows(list);
		} catch (RuntimeException e) {
			e.printStackTrace();
			responseData.setMessage(e.getMessage());
			responseData.setSuccess(false);
		}
		return responseData;
	}

	/**
	 * 删除
	 * 
	 * @param request
	 * @param dto
	 * @return
	 */
	@RequestMapping(value = "/hcs/supplier/bom/components/deleteData")
	@ResponseBody
	public ResponseData deleteData(HttpServletRequest request, SupplierBomComponents dto) {
		ResponseData responseData = new ResponseData();
		IRequest requestCtx = createRequestContext(request);
		try {
			SupplierBomComponents supplierBomComponents = service.deleteData(requestCtx, dto);
			List<SupplierBomComponents> list = new ArrayList();
			list.add(supplierBomComponents);
			responseData.setRows(list);
		} catch (RuntimeException e) {
			e.printStackTrace();
			responseData.setMessage(e.getMessage());
			responseData.setSuccess(false);
		}
		return responseData;
	}
}