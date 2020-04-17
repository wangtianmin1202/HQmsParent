package com.hand.hqm.hqm_db_management.controllers;

import org.springframework.stereotype.Controller;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hqm.hqm_db_management.dto.HQMInvalid;
import com.hand.hqm.hqm_db_management.service.IHQMInvalidService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import org.springframework.validation.BindingResult;
import java.util.List;

@Controller
public class HQMInvalidController extends BaseController {

	@Autowired
	private IHQMInvalidService service;

	@RequestMapping(value = "/hqm/invalid/query")
	@ResponseBody
	public ResponseData query(HQMInvalid dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
			@RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
		// IRequest requestContext = createRequestContext(request);
		// service.select(requestContext,dto,page,pageSize);
		return new ResponseData(service.query());
	}

	@RequestMapping(value = "/hqm/invalid/submit")
	@ResponseBody
	public ResponseData update(@RequestBody List<HQMInvalid> dto, BindingResult result, HttpServletRequest request) {
		getValidator().validate(dto, result);
		if (result.hasErrors()) {
			ResponseData responseData = new ResponseData(false);
			responseData.setMessage(getErrorMessage(result, request));
			return responseData;
		}
		IRequest requestCtx = createRequestContext(request);
		return new ResponseData(service.save(requestCtx, dto));
	}

	@RequestMapping(value = "/hqm/invalid/remove")
	@ResponseBody
	public ResponseData delete(HttpServletRequest request, @RequestBody List<HQMInvalid> dto) {
		service.delete(dto);
		return new ResponseData();
	}
	

}