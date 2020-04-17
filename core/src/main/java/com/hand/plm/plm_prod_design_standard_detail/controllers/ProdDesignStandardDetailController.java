package com.hand.plm.plm_prod_design_standard_detail.controllers;

import org.springframework.stereotype.Controller;
import com.hand.hap.system.controllers.BaseController;
import com.google.common.base.Throwables;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.plm.plm_prod_design_standard_detail.dto.ProdDesignStandardDetail;
import com.hand.plm.plm_prod_design_standard_detail.service.IProdDesignStandardDetailService;
import com.hand.plm.plm_prod_design_standard_detail.view.ProdDesignStandardDetailVO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import org.springframework.validation.BindingResult;
import java.util.List;

@Controller
public class ProdDesignStandardDetailController extends BaseController {

	@Autowired
	private IProdDesignStandardDetailService service;

	@RequestMapping(value = "/plm/prod/design/standard/detail/query")
	@ResponseBody
	public ResponseData query(ProdDesignStandardDetail dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
			@RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
		IRequest requestContext = createRequestContext(request);
		return new ResponseData(service.select(requestContext, dto, page, pageSize));
	}

	@RequestMapping(value = "/plm/prod/design/standard/detail/submit")
	@ResponseBody
	public ResponseData update(@RequestBody List<ProdDesignStandardDetail> dto, BindingResult result,
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

	@RequestMapping(value = "/plm/prod/design/standard/detail/remove")
	@ResponseBody
	public ResponseData delete(HttpServletRequest request, @RequestBody List<ProdDesignStandardDetail> dto) {
		service.batchDelete(dto);
		return new ResponseData();
	}

	@RequestMapping(value = "/plm/prod/design/standard/detail/queryAll")
	@ResponseBody
	public ResponseData queryAll(ProdDesignStandardDetailVO dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
			@RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
		IRequest requestContext = createRequestContext(request);
		return new ResponseData(service.queryAll(requestContext, dto, page, pageSize));
	}

	/**
	 * 废止
	 * 
	 * @param detailIdList 废止的明细数据ID集合
	 * @param request
	 * @return 草稿表对象集合
	 */
	@RequestMapping(value = "/plm/prod/design/standard/detail/invalid")
	@ResponseBody
	public ResponseData invalid(@RequestBody List<String> detailIdList, HttpServletRequest request) {
		IRequest requestContext = createRequestContext(request);
		ResponseData responseData = new ResponseData();
		try {
			responseData.setSuccess(true);
			responseData.setMessage("成功");
			responseData.setRows(service.invalid(requestContext, detailIdList));
		} catch (Exception ex) {
			responseData.setSuccess(false);
			responseData.setMessage(Throwables.getRootCause(ex).getMessage());
		}
		return responseData;
	}

	/**
	 * 生效作废的数据
	 * 
	 * @param detailIdList
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/plm/prod/design/standard/detail/effective")
	@ResponseBody
	public ResponseData effective(@RequestBody List<String> detailIdList, HttpServletRequest request) {
		IRequest requestContext = createRequestContext(request);
		ResponseData responseData = new ResponseData();
		try {
			responseData.setSuccess(true);
			responseData.setMessage("成功");
			responseData.setRows(service.effective(requestContext, detailIdList));
		} catch (Exception ex) {
			responseData.setSuccess(false);
			responseData.setMessage(Throwables.getRootCause(ex).getMessage());
		}
		return responseData;
	}
}