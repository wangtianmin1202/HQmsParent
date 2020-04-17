package com.hand.npi.npi_technology.controllers;

import org.springframework.stereotype.Controller;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.npi.npi_route.dto.TechnologyWpAction;
import com.hand.npi.npi_technology.dto.ComposeProduct;
import com.hand.npi.npi_technology.service.IComposeProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import org.springframework.validation.BindingResult;
import java.util.List;

@Controller
public class ComposeProductController extends BaseController {

	@Autowired
	private IComposeProductService service;

	@RequestMapping(value = "/npi/compose/product/query")
	@ResponseBody
	public ResponseData query(ComposeProduct dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
			@RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
		IRequest requestContext = createRequestContext(request);
		return new ResponseData(service.select(requestContext, dto, page, pageSize));
	}

	@RequestMapping(value = "/npi/compose/product/submit")
	@ResponseBody
	public ResponseData update(@RequestBody List<ComposeProduct> dto, BindingResult result,
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

	@RequestMapping(value = "/npi/compose/product/remove")
	@ResponseBody
	public ResponseData delete(HttpServletRequest request, @RequestBody List<ComposeProduct> dto) {
		service.batchDelete(dto);
		return new ResponseData();
	}
	
	
	@RequestMapping(value = "/npi/compose/product/addData")
	@ResponseBody
	public ResponseData addData(@RequestBody List<ComposeProduct> dtos, BindingResult result,
			HttpServletRequest request) {
		IRequest requestCtx = createRequestContext(request);
		return new ResponseData(service.addData(requestCtx, dtos));
	}
}