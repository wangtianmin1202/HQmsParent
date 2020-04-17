package com.hand.plm.plm_product_design_standard.controllers;

import org.springframework.stereotype.Controller;
import com.hand.hap.system.controllers.BaseController;
import com.google.common.base.Throwables;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.plm.plm_product_design_standard.dto.ProductDesignStandard;
import com.hand.plm.plm_product_design_standard.service.IProductDesignStandardService;
import com.hand.plm.product_func_attr_basic.dto.ProductFuncAttrBasic;
import com.hand.plm.product_func_attr_basic.view.TreeVO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import org.springframework.validation.BindingResult;

import java.util.Arrays;
import java.util.List;

@Controller
public class ProductDesignStandardController extends BaseController {

	@Autowired
	private IProductDesignStandardService service;

	@RequestMapping(value = "/plm/product/design/standard/query")
	@ResponseBody
	public ResponseData query(ProductDesignStandard dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
			@RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
		IRequest requestContext = createRequestContext(request);
		return new ResponseData(service.select(requestContext, dto, page, pageSize));
	}

	@RequestMapping(value = "/plm/product/design/standard/submit")
	@ResponseBody
	public ResponseData update(@RequestBody List<ProductDesignStandard> dto, BindingResult result,
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

	@RequestMapping(value = "/plm/product/design/standard/remove")
	@ResponseBody
	public ResponseData delete(HttpServletRequest request, @RequestBody List<ProductDesignStandard> dto) {
		service.batchDelete(dto);
		return new ResponseData();
	}

	/**
	 * 查询产品规则树
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/plm/product/design/standard/queryTreeDatas")
	@ResponseBody
	public ResponseData queryTreeDatas(HttpServletRequest request) {
		IRequest requestContext = createRequestContext(request);
		return new ResponseData(service.selectTreeDatas(requestContext));
	}

	@RequestMapping(value = "/plm/product/design/standard/addTree")
	@ResponseBody
	public ResponseData addTree(HttpServletRequest request, @RequestBody TreeVO treeVO) {
		IRequest iRequest = createRequestContext(request);
		ResponseData responseData = new ResponseData();
		try {
			TreeVO newTree = service.addTree(iRequest, treeVO);
			responseData.setSuccess(true);
			responseData.setMessage("成功");
			responseData.setRows(Arrays.asList(newTree));
		} catch (RuntimeException ex) {
			responseData.setSuccess(false);
			responseData.setMessage(ex.getMessage());
		}
		return responseData;
	}

	@RequestMapping(value = "/plm/product/design/standard/renameTree")
	@ResponseBody
	public ResponseData renameTree(HttpServletRequest request, @RequestBody TreeVO treeVO) {
		IRequest iRequest = createRequestContext(request);
		ResponseData responseData = new ResponseData();
		try {
			service.renameTree(iRequest, treeVO);
			responseData.setSuccess(true);
			responseData.setMessage("成功");
		} catch (RuntimeException ex) {
			responseData.setSuccess(false);
			responseData.setMessage(ex.getMessage());
		}
		return responseData;
	}

	@RequestMapping(value = "/plm/product/design/standard/removeTree")
	@ResponseBody
	public ResponseData removeTree(HttpServletRequest request, @RequestBody TreeVO treeVO) {
		IRequest iRequest = createRequestContext(request);
		ResponseData responseData = new ResponseData();
		try {
			service.removeTree(iRequest, treeVO);
			responseData.setSuccess(true);
			responseData.setMessage("成功");
		} catch (RuntimeException ex) {
			responseData.setSuccess(false);
			responseData.setMessage(ex.getMessage());
		}
		return responseData;
	}

	/**
	 * 零件下拉框取值
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/plm/product/design/standard/query/relatedParts")
	@ResponseBody
	public ResponseData queryrelatedParts(HttpServletRequest request) {
		IRequest requestContext = createRequestContext(request);
		return new ResponseData(service.queryrelatedParts(requestContext));
	}
}