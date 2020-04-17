package com.hand.hqm.hqm_pqc_warning.controllers;

import org.springframework.stereotype.Controller;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hqm.hqm_pqc_warning.dto.PqcWarning;
import com.hand.hqm.hqm_pqc_warning.service.IPqcWarningService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import org.springframework.validation.BindingResult;
import java.util.List;

@Controller
public class PqcWarningController extends BaseController {

	@Autowired
	private IPqcWarningService service;

	/**
	 * 
	 * @description 基础查询
	 * @author tianmin.wang
	 * @date 2019年12月26日
	 * @param dto
	 * @param page
	 * @param pageSize
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/hqm/pqc/warning/query")
	@ResponseBody
	public ResponseData query(PqcWarning dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
			@RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
		IRequest requestContext = createRequestContext(request);
		return new ResponseData(service.reSelect(requestContext, dto, page, pageSize));
	}

	@RequestMapping(value = "/hqm/pqc/warning/submit")
	@ResponseBody
	public ResponseData update(@RequestBody List<PqcWarning> dto, BindingResult result, HttpServletRequest request) {
		getValidator().validate(dto, result);
		if (result.hasErrors()) {
			ResponseData responseData = new ResponseData(false);
			responseData.setMessage(getErrorMessage(result, request));
			return responseData;
		}
		IRequest requestCtx = createRequestContext(request);
		try {
			return new ResponseData(service.reBatchUpdate(requestCtx, dto));
		} catch (RuntimeException e) {
			ResponseData runRes = new ResponseData(false);
			runRes.setMessage(e.getMessage());
			return runRes;
		} catch (Exception e) {
			throw e;
		}
	}

	@RequestMapping(value = "/hqm/pqc/warning/remove")
	@ResponseBody
	public ResponseData delete(HttpServletRequest request, @RequestBody List<PqcWarning> dto) {
		service.batchDelete(dto);
		return new ResponseData();
	}
}