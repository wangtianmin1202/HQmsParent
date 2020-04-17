package com.hand.npi.npi_route.controllers;

import org.springframework.stereotype.Controller;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.npi.npi_route.dto.TechnologyRoute;
import com.hand.npi.npi_route.service.ITechnologyRouteService;
import com.hand.npi.npi_technology.dto.QuickTechRouteDto;
import com.hand.wfl.util.DropDownListDto;

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
public class TechnologyRouteController extends BaseController {

	@Autowired
	private ITechnologyRouteService service;

	@RequestMapping(value = "/npi/technology/route/query")
	@ResponseBody
	public ResponseData query(TechnologyRoute dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
			@RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
		IRequest requestContext = createRequestContext(request);
		return new ResponseData(service.select(requestContext, dto, page, pageSize));
	}

	@RequestMapping(value = "/hqm/technology/route/submit")
	@ResponseBody
	public ResponseData update(@RequestBody List<TechnologyRoute> dto, BindingResult result,
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

	@RequestMapping(value = "/hqm/technology/route/remove")
	@ResponseBody
	public ResponseData delete(HttpServletRequest request, @RequestBody List<TechnologyRoute> dto) {
		service.batchDelete(dto);
		return new ResponseData();
	}
	
	
	@RequestMapping(value = "/npi/technology/route/addData")
	@ResponseBody
	public ResponseData addData(@RequestBody List<TechnologyRoute> dto, BindingResult result,
			HttpServletRequest request) {
		getValidator().validate(dto, result);
		if (result.hasErrors()) {
			ResponseData responseData = new ResponseData(false);
			responseData.setMessage(getErrorMessage(result, request));
			return responseData;
		}
		IRequest requestCtx = createRequestContext(request);
		List<TechnologyRoute> addData = service.addData(requestCtx, dto);
		ResponseData responseData = new ResponseData();
		if (null == addData) {
			responseData.setSuccess(false);
			responseData.setMessage("SKU和生产线的组合重复");
		}else {
			responseData.setRows(addData);
		}
		return responseData;
	}
	
	@RequestMapping(value = "/hqm/technology/route/update")
	@ResponseBody
	public ResponseData updateData(@RequestBody List<TechnologyRoute> dto, BindingResult result,
			HttpServletRequest request) {
		getValidator().validate(dto, result);
		if (result.hasErrors()) {
			ResponseData responseData = new ResponseData(false);
			responseData.setMessage(getErrorMessage(result, request));
			return responseData;
		}
		IRequest requestCtx = createRequestContext(request);
		for (TechnologyRoute technologyRoute : dto) {
			technologyRoute.set__status("update");
		}
		return new ResponseData(service.batchUpdate(requestCtx, dto));
	}
	
	/**
	 * 	快捷新建工艺路径的方法
	 */
	@RequestMapping(value = "/npi/technology/route/queryOldSku")
	@ResponseBody
	public List<DropDownListDto> queryOldSku(QuickTechRouteDto dto,
			HttpServletRequest request) {
		IRequest requestCtx = createRequestContext(request);
		return service.queryOldSku(requestCtx, dto);
	}
	
	@RequestMapping(value = "/npi/technology/route/queryRouteVersion")
	@ResponseBody
	public List<DropDownListDto> queryRouteVersion(QuickTechRouteDto dto,
			HttpServletRequest request) {
		IRequest requestCtx = createRequestContext(request);
		return service.queryRouteVersion(requestCtx, dto);
	}
	
	@RequestMapping(value = "/npi/technology/route/queryNewSku")
	@ResponseBody
	public List<DropDownListDto> queryNewSku(QuickTechRouteDto dto,
			HttpServletRequest request) {
		IRequest requestCtx = createRequestContext(request);
		return service.queryNewSku(requestCtx, dto);
	}
	
	@RequestMapping(value = "/npi/technology/route/queryOldEbom")
	@ResponseBody
	public ResponseData queryOldEbom(QuickTechRouteDto dto,
			HttpServletRequest request) {
		IRequest requestCtx = createRequestContext(request);
		return new ResponseData(service.queryOldEbom(requestCtx, dto));
	}
	
	@RequestMapping(value = "/npi/technology/route/queryNewEbom")
	@ResponseBody
	public ResponseData queryNewEbom(QuickTechRouteDto dto,
			HttpServletRequest request) {
		IRequest requestCtx = createRequestContext(request);
		return new ResponseData(service.queryNewEbom(requestCtx, dto));
	}
	
	@RequestMapping(value = "/npi/technology/route/checkData")
	@ResponseBody
	public Map<String, Object> checkData(@RequestBody QuickTechRouteDto dto,
			HttpServletRequest request) {
		IRequest requestCtx = createRequestContext(request);
		return service.checkData(requestCtx, dto);
	}
	
	@RequestMapping(value = "/npi/technology/route/copyData")
	@ResponseBody
	public ResponseData copyData(@RequestBody QuickTechRouteDto dto,
			HttpServletRequest request) {
		IRequest requestCtx = createRequestContext(request);
		return service.copyData(requestCtx, dto);
	}
	
}