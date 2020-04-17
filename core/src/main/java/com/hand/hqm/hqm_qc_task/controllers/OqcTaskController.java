package com.hand.hqm.hqm_qc_task.controllers;

import org.springframework.stereotype.Controller;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.core.IRequest;
import com.hand.hap.core.impl.RequestHelper;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hqm.hqm_qc_task.dto.OqcTask;
import com.hand.hqm.hqm_qc_task.service.IOqcTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import org.springframework.validation.BindingResult;
import java.util.List;

@Controller
public class OqcTaskController extends BaseController {

	@Autowired
	private IOqcTaskService service;

	@RequestMapping(value = "/hqm/oqc/task/query")
	@ResponseBody
	public ResponseData query(OqcTask dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
			@RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
		IRequest requestContext = createRequestContext(request);
		return new ResponseData(service.reSelect(requestContext, dto, page, pageSize));
	}

	@RequestMapping(value = "/hqm/oqc/task/submit")
	@ResponseBody
	public ResponseData update(@RequestBody List<OqcTask> dto, BindingResult result, HttpServletRequest request) {
		getValidator().validate(dto, result);
		if (result.hasErrors()) {
			ResponseData responseData = new ResponseData(false);
			responseData.setMessage(getErrorMessage(result, request));
			return responseData;
		}
		IRequest requestCtx = createRequestContext(request);
		return new ResponseData(service.batchUpdate(requestCtx, dto));
	}

	@RequestMapping(value = "/hqm/oqc/task/remove")
	@ResponseBody
	public ResponseData delete(HttpServletRequest request, @RequestBody List<OqcTask> dto) {
		service.batchDelete(dto);
		return new ResponseData();
	}

	@RequestMapping(value = "/hqm/oqc/task/create/oqc")
	@ResponseBody
	public ResponseData createOqc(@RequestBody List<OqcTask> dto, HttpServletRequest request) throws Exception {
		ResponseData resd = new ResponseData();
		try {
			service.createOqc(request, dto);
			resd.setRows(dto);
		} catch (RuntimeException e) {
			ResponseData res = new ResponseData(false);
			res.setMessage(e.getMessage());
			dto.get(0).setErrorMsg(e.getMessage());
			service.updateByPrimaryKeySelective(RequestHelper.createServiceRequest(request), dto.get(0));
			return res;
		} catch (Exception e) {
			throw e;
		}
		return resd;
	}

}