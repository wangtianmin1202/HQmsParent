package com.hand.npi.npi_route.controllers;

import org.springframework.stereotype.Controller;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.npi.npi_route.dto.TechnologyWpAction;
import com.hand.npi.npi_route.dto.TechnologyWpStandardActionDetail;
import com.hand.npi.npi_route.service.ITechnologyWpStandardActionDetailService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import org.springframework.validation.BindingResult;
import java.util.List;

@Controller
public class TechnologyWpStandardActionDetailController extends BaseController {

	@Autowired
	private ITechnologyWpStandardActionDetailService service;

	@RequestMapping(value = "/npi/technology/wp/standard/action/detail/query")
	@ResponseBody
	public ResponseData query(TechnologyWpStandardActionDetail dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
			@RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
		IRequest requestContext = createRequestContext(request);
		return new ResponseData(service.select(requestContext, dto, page, pageSize));
	}

	@RequestMapping(value = "/npi/technology/wp/standard/action/detail/submit")
	@ResponseBody
	public ResponseData update(@RequestBody List<TechnologyWpStandardActionDetail> dto, BindingResult result,
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

	@RequestMapping(value = "/npi/technology/wp/standard/action/detail/remove")
	@ResponseBody
	public ResponseData delete(HttpServletRequest request, @RequestBody List<TechnologyWpStandardActionDetail> dto) {
		service.batchDelete(dto);
		return new ResponseData();
	}

	// 查询标准动作
	@RequestMapping(value = "/npi/technology/wp/standard/action/queryActionInfo")
	@ResponseBody
	public ResponseData queryActionInfo(HttpServletRequest request, @RequestBody TechnologyWpAction dto) {
		IRequest requestCtx = createRequestContext(request);
		return new ResponseData(service.queryActionInfo(requestCtx, dto));
	}
}