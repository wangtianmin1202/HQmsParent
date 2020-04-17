package com.hand.plm.plm_product_func_attr_approve.controllers;

import org.springframework.stereotype.Controller;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.plm.plm_product_func_attr_approve.dto.ProductFuncAttrApprove;
import com.hand.plm.plm_product_func_attr_approve.service.IProductFuncAttrApproveService;
import com.hand.plm.plm_product_func_attr_approve.view.ProductFuncAttrApproveVO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import org.springframework.validation.BindingResult;
import java.util.List;

@Controller
public class ProductFuncAttrApproveController extends BaseController {

	@Autowired
	private IProductFuncAttrApproveService service;

	@RequestMapping(value = "/plm/product/func/attr/approve/query")
	@ResponseBody
	public ResponseData query(ProductFuncAttrApprove dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
			@RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
		IRequest requestContext = createRequestContext(request);
		return new ResponseData(service.select(requestContext, dto, page, pageSize));
	}

	@RequestMapping(value = "/plm/product/func/attr/approve/submit")
	@ResponseBody
	public ResponseData update(@RequestBody List<ProductFuncAttrApprove> dto, BindingResult result,
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

	@RequestMapping(value = "/plm/product/func/attr/approve/remove")
	@ResponseBody
	public ResponseData delete(HttpServletRequest request, @RequestBody List<ProductFuncAttrApprove> dto) {
		service.batchDelete(dto);
		return new ResponseData();
	}

	@RequestMapping(value = "/plm/product/func/attr/approve/queryNew")
	@ResponseBody
	public ResponseData queryNew(ProductFuncAttrApproveVO dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
			@RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
		IRequest requestContext = createRequestContext(request);
		return new ResponseData(service.queryNew(requestContext, dto, page, pageSize));
	}

}