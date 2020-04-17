package com.hand.hcm.hcm_object_events.controllers;

import org.springframework.stereotype.Controller;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hcm.hcm_object_events.dto.ObjectEvents;
import com.hand.hcm.hcm_object_events.service.IObjectEventsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import org.springframework.validation.BindingResult;
import java.util.List;

@Controller
public class ObjectEventsController extends BaseController {

	@Autowired
	private IObjectEventsService service;

	@RequestMapping(value = "/hcm/object/events/query/{tableName}")
	@ResponseBody
	public ResponseData query(ObjectEvents dto, @PathVariable String tableName, HttpServletRequest request) {
		IRequest requestContext = createRequestContext(request);
		return new ResponseData(service.query(requestContext, dto, tableName));
	}

	@RequestMapping(value = "/hcm/object/events/submit")
	@ResponseBody
	public ResponseData update(@RequestBody List<ObjectEvents> dto, BindingResult result, HttpServletRequest request) {
		getValidator().validate(dto, result);
		if (result.hasErrors()) {
			ResponseData responseData = new ResponseData(false);
			responseData.setMessage(getErrorMessage(result, request));
			return responseData;
		}
		IRequest requestCtx = createRequestContext(request);
		return new ResponseData(service.batchUpdate(requestCtx, dto));
	}

	@RequestMapping(value = "/hcm/object/events/remove")
	@ResponseBody
	public ResponseData delete(HttpServletRequest request, @RequestBody List<ObjectEvents> dto) {
		service.batchDelete(dto);
		return new ResponseData();
	}
}