package com.hand.plm.plm_prod_design_standard_approve.controllers;

import org.springframework.stereotype.Controller;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.plm.plm_prod_design_standard_approve.dto.ProdDesignStandardApprove;
import com.hand.plm.plm_prod_design_standard_approve.service.IProdDesignStandardApproveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import org.springframework.validation.BindingResult;
import java.util.List;

@Controller
public class ProdDesignStandardApproveController extends BaseController {

	@Autowired
	private IProdDesignStandardApproveService service;

	@RequestMapping(value = "/plm/prod/design/standard/approve/query")
	@ResponseBody
	public ResponseData query(ProdDesignStandardApprove dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
			@RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
		IRequest requestContext = createRequestContext(request);
		return new ResponseData(service.select(requestContext, dto, page, pageSize));
	}

	@RequestMapping(value = "/plm/prod/design/standard/approve/submit")
	@ResponseBody
	public ResponseData update(@RequestBody List<ProdDesignStandardApprove> dto, BindingResult result,
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

	@RequestMapping(value = "/plm/prod/design/standard/approve/remove")
	@ResponseBody
	public ResponseData delete(HttpServletRequest request, @RequestBody List<ProdDesignStandardApprove> dto) {
		service.batchDelete(dto);
		return new ResponseData();
	}
}