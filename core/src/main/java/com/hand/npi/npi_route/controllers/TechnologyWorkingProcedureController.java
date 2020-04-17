package com.hand.npi.npi_route.controllers;

import org.springframework.stereotype.Controller;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.npi.npi_route.dto.TechnologyWorkingProcedure;
import com.hand.npi.npi_route.service.ITechnologyWorkingProcedureService;
import com.hand.npi.npi_technology.dto.EbomMain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import org.springframework.validation.BindingResult;
import java.util.List;
import java.util.Map;

@Controller
public class TechnologyWorkingProcedureController extends BaseController {

	@Autowired
	private ITechnologyWorkingProcedureService service;

	@RequestMapping(value = "/npi/technology/working/procedure/query")
	@ResponseBody
	public ResponseData query(TechnologyWorkingProcedure dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
			@RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
		IRequest requestContext = createRequestContext(request);
		return new ResponseData(service.selectWpInfo(requestContext, dto, page, pageSize));
	}

	@RequestMapping(value = "/hqm/technology/working/procedure/submit")
	@ResponseBody
	public ResponseData update(@RequestBody List<TechnologyWorkingProcedure> dto, BindingResult result,
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

	@RequestMapping(value = "/hqm/technology/working/procedure/remove")
	@ResponseBody
	public ResponseData delete(HttpServletRequest request, @RequestBody List<TechnologyWorkingProcedure> dto) {
		service.batchDelete(dto);
		return new ResponseData();
	}
	
	@RequestMapping(value = "/npi/technology/working/procedure/addData")
	@ResponseBody
	public ResponseData addData(HttpServletRequest request, @RequestBody TechnologyWorkingProcedure dto) {
		IRequest requestCtx = createRequestContext(request);
		return service.addData(requestCtx,dto);
	}
	//查询最新的sku的版本
	@RequestMapping(value = "/npi/technology/working/procedure/queryEBomVersion")
	@ResponseBody
	public EbomMain queryEBomVersion(HttpServletRequest request, @RequestBody EbomMain dto) {
		IRequest requestCtx = createRequestContext(request);
		return service.queryEBomVersion(requestCtx, dto);
	}
	//查询这条sku的所有物料
	@RequestMapping(value = "/npi/technology/working/procedure/qeuryEBomPart")
	@ResponseBody
	public ResponseData qeuryEBomPart(HttpServletRequest request, EbomMain dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
			@RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize) {
		IRequest requestCtx = createRequestContext(request);
		return new ResponseData(service.qeuryEBomPart(requestCtx,dto,page,pageSize));
	}
	
	//保存修改后的工序
	@RequestMapping(value = "/npi/technology/working/procedure/updateData")
	@ResponseBody
	public ResponseData updateData(HttpServletRequest request, @RequestBody TechnologyWorkingProcedure dto) {
		IRequest requestCtx = createRequestContext(request);
		return service.updateData(requestCtx,dto);
	}
	
	
}