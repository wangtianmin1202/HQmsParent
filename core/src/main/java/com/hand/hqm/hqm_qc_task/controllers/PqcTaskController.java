package com.hand.hqm.hqm_qc_task.controllers;

import org.springframework.stereotype.Controller;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.core.IRequest;
import com.hand.hap.core.impl.RequestHelper;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hqm.hqm_pqc_inspection_h.dto.PqcInspectionH;
import com.hand.hqm.hqm_qc_task.dto.PqcTask;
import com.hand.hqm.hqm_qc_task.service.IPqcTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import org.springframework.validation.BindingResult;

import java.util.ArrayList;
import java.util.List;

@Controller
public class PqcTaskController extends BaseController {

	@Autowired
	private IPqcTaskService service;

	@RequestMapping(value = "/hqm/pqc/task/query")
	@ResponseBody
	public ResponseData query(PqcTask dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
			@RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
		IRequest requestContext = createRequestContext(request);
		return new ResponseData(service.select(requestContext, dto, page, pageSize));
	}

	/**
	 * 
	 * @description 主界面查询
	 * @author tianmin.wang
	 * @date 2019年12月27日
	 * @param dto
	 * @param page
	 * @param pageSize
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/hqm/pqc/task/select")
	@ResponseBody
	public ResponseData select(PqcTask dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
			@RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
		IRequest requestContext = createRequestContext(request);
		return new ResponseData(service.reSelect(requestContext, dto, page, pageSize));
	}

	@RequestMapping(value = "/hqm/pqc/task/submit")
	@ResponseBody
	public ResponseData update(@RequestBody List<PqcTask> dto, BindingResult result, HttpServletRequest request) {
		getValidator().validate(dto, result);
		if (result.hasErrors()) {
			ResponseData responseData = new ResponseData(false);
			responseData.setMessage(getErrorMessage(result, request));
			return responseData;
		}
		IRequest requestCtx = createRequestContext(request);
		return new ResponseData(service.batchUpdate(requestCtx, dto));
	}

	@RequestMapping(value = "/hqm/pqc/task/remove")
	@ResponseBody
	public ResponseData delete(HttpServletRequest request, @RequestBody List<PqcTask> dto) {
		service.batchDelete(dto);
		return new ResponseData();
	}

	@RequestMapping(value = "/hqm/pqc/task/create/pqc")
	@ResponseBody
	public ResponseData createPqc(HttpServletRequest request, @RequestBody List<PqcTask> dto) {
		try {
			PqcTask pt = dto.get(0);
			List<PqcInspectionH> rows = new ArrayList<PqcInspectionH>();
			PqcInspectionH h = new PqcInspectionH();
			h.setInspectionNum(service.createPqc(request, pt));
			rows.add(h);
			ResponseData resd = new ResponseData();
			resd.setRows(rows);
			return resd;
		} catch (RuntimeException e) {
			ResponseData resd = new ResponseData();
			resd.setSuccess(false);
			resd.setMessage(e.getMessage());
			dto.get(0).setErrorMsg(e.getMessage());
			service.updateByPrimaryKeySelective(RequestHelper.createServiceRequest(request), dto.get(0));
			return resd;
		} catch (Exception e) {
			ResponseData resd = new ResponseData();
			resd.setSuccess(false);
			resd.setMessage(e.getMessage());
			return resd;
		}
	}

}