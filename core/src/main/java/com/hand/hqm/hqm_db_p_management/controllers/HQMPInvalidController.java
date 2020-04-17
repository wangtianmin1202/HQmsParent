package com.hand.hqm.hqm_db_p_management.controllers;

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
import com.hand.hqm.hqm_db_p_management.dto.HQMPInvalid;
import com.hand.hqm.hqm_db_p_management.service.IHQMPInvalidService;

@Controller
public class HQMPInvalidController extends BaseController {

	@Autowired
	private IHQMPInvalidService service;

	@RequestMapping(value = "/hqmp/invalid/query")
	@ResponseBody
	public ResponseData query(HQMPInvalid dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
			@RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
		// IRequest requestContext = createRequestContext(request);
		// service.select(requestContext,dto,page,pageSize);
		return new ResponseData(service.query());
	}

	@RequestMapping(value = "/hqmp/invalid/submit")
	@ResponseBody
	public ResponseData update(@RequestBody List<HQMPInvalid> dto, BindingResult result, HttpServletRequest request) {
		getValidator().validate(dto, result);
		if (result.hasErrors()) {
			ResponseData responseData = new ResponseData(false);
			responseData.setMessage(getErrorMessage(result, request));
			return responseData;
		}
		IRequest requestCtx = createRequestContext(request);
		return new ResponseData(service.save(requestCtx, dto));
	}

	@RequestMapping(value = "/hqmp/invalid/remove")
	@ResponseBody
	public ResponseData delete(HttpServletRequest request, @RequestBody List<HQMPInvalid> dto) {
		service.delete(dto);
		return new ResponseData();
	}
}