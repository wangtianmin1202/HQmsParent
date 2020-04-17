package com.hand.npi.npi_technology.controllers;

import org.springframework.stereotype.Controller;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.npi.npi_technology.dto.PreventionMeasure;
import com.hand.npi.npi_technology.service.IPreventionMeasureService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import org.springframework.validation.BindingResult;
import java.util.List;

@Controller
public class PreventionMeasureController extends BaseController {

	@Autowired
	private IPreventionMeasureService service;

	@RequestMapping(value = "/hqm/prevention/measure/query")
	@ResponseBody
	public ResponseData query(PreventionMeasure dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
			@RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
		IRequest requestContext = createRequestContext(request);
		return new ResponseData(service.select(requestContext, dto, page, pageSize));
	}

	@RequestMapping(value = "/hqm/prevention/measure/submit")
	@ResponseBody
	public ResponseData update(@RequestBody List<PreventionMeasure> dto, BindingResult result,
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

	@RequestMapping(value = "/hqm/prevention/measure/remove")
	@ResponseBody
	public ResponseData delete(HttpServletRequest request, @RequestBody List<PreventionMeasure> dto) {
		service.batchDelete(dto);
		return new ResponseData();
	}

	@RequestMapping(value = "/hqm/prevention/measure/add")
	@ResponseBody
	public ResponseData add(@RequestBody PreventionMeasure dto, BindingResult result, HttpServletRequest request) {
		getValidator().validate(dto, result);
		if (result.hasErrors()) {
			ResponseData responseData = new ResponseData(false);
			responseData.setMessage(getErrorMessage(result, request));
			return responseData;
		}
		IRequest requestCtx = createRequestContext(request);
		return service.addNewPreventionMeasure(dto, requestCtx, request);
	}

	@RequestMapping(value = "/hqm/prevention/measure/occsev/query")
	@ResponseBody
	public ResponseData queryMeasureList(PreventionMeasure dto, HttpServletRequest request) {
		return new ResponseData(service.queryPreventionMeasureList(dto));
	}
	
	
	@RequestMapping(value = "/npi/prevention/measure/queryByPatId")
	@ResponseBody
	public ResponseData queryByPatId(PreventionMeasure dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
			@RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
		IRequest requestContext = createRequestContext(request);
		return new ResponseData(service.queryByPatId(requestContext, dto, page, pageSize));
	}
}