package com.hand.hqm.hqm_program_sku_rel.controllers;

import org.springframework.stereotype.Controller;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hqm.hqm_program_sku_rel.dto.ProgramSkuRel;
import com.hand.hqm.hqm_program_sku_rel.service.IProgramSkuRelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import org.springframework.validation.BindingResult;
import java.util.List;

@Controller
public class ProgramSkuRelController extends BaseController {

	@Autowired
	private IProgramSkuRelService service;

	@RequestMapping(value = "/hqm/program/sku/rel/query")
	@ResponseBody
	public ResponseData query(ProgramSkuRel dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
			@RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
		IRequest requestContext = createRequestContext(request);
		return new ResponseData(service.reSelect(requestContext, dto, page, pageSize));
	}

	@RequestMapping(value = "/hqm/program/sku/rel/submit")
	@ResponseBody
	public ResponseData update(@RequestBody List<ProgramSkuRel> dto, BindingResult result, HttpServletRequest request) {
		getValidator().validate(dto, result);
		if (result.hasErrors()) {
			ResponseData responseData = new ResponseData(false);
			responseData.setMessage(getErrorMessage(result, request));
			return responseData;
		}
		IRequest requestCtx = createRequestContext(request);
		ResponseData responseData = new ResponseData();
		try {
			responseData.setRows(service.reBatchUpdate(requestCtx, dto));
		} catch (RuntimeException e) {
			ResponseData resp = new ResponseData(false);
			resp.setMessage(e.getMessage());
			return resp;
		} catch (Exception e) {
			throw e;
		}
		return responseData;
	}

	@RequestMapping(value = "/hqm/program/sku/rel/remove")
	@ResponseBody
	public ResponseData delete(HttpServletRequest request, @RequestBody List<ProgramSkuRel> dto) {
		service.batchDelete(dto);
		return new ResponseData();
	}
}