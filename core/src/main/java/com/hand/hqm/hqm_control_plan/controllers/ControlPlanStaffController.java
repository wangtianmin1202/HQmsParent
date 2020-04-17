package com.hand.hqm.hqm_control_plan.controllers;

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
import com.hand.hqm.hqm_control_plan.dto.ControlPlanStaff;
import com.hand.hqm.hqm_control_plan.service.IControlPlanStaffService;

@Controller
public class ControlPlanStaffController extends BaseController {

	@Autowired
	private IControlPlanStaffService service;

	@RequestMapping(value = "/hqm/control/plan/staff/query")
	@ResponseBody
	public ResponseData query(ControlPlanStaff dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
			@RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
		IRequest requestContext = createRequestContext(request);
		return new ResponseData(service.select(requestContext, dto, page, pageSize));
	}

	@RequestMapping(value = "/hqm/control/plan/staff/submit")
	@ResponseBody
	public ResponseData update(@RequestBody List<ControlPlanStaff> dto, BindingResult result,
			HttpServletRequest request) {
		getValidator().validate(dto, result);
		if (result.hasErrors()) {
			ResponseData responseData = new ResponseData(false);
			responseData.setMessage(getErrorMessage(result, request));
			return responseData;
		}
		IRequest requestCtx = createRequestContext(request);
		ResponseData responseData =service.save(dto, requestCtx);
		return responseData;
	}

	@RequestMapping(value = "/hqm/control/plan/staff/remove")
	@ResponseBody
	public ResponseData delete(HttpServletRequest request, @RequestBody List<ControlPlanStaff> dto) {
		service.batchDelete(dto);
		return new ResponseData();
	}
}