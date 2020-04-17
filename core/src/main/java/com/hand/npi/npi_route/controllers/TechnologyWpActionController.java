package com.hand.npi.npi_route.controllers;

import org.springframework.stereotype.Controller;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hcm.hcm_item_b.dto.ItemB;
import com.hand.npi.npi_route.dto.TechnologyWpAction;
import com.hand.npi.npi_route.service.ITechnologyWpActionService;
import com.hand.npi.npi_technology.dto.EbomMain;
import com.hand.npi.npi_technology.dto.TechnologySpecDetail;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import org.springframework.validation.BindingResult;

import java.util.LinkedList;
import java.util.List;

@Controller
public class TechnologyWpActionController extends BaseController {

	@Autowired
	private ITechnologyWpActionService service;

	@RequestMapping(value = "/hqm/technology/wp/action/query")
	@ResponseBody
	public ResponseData query(TechnologyWpAction dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
			@RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
		IRequest requestContext = createRequestContext(request);
		List<TechnologyWpAction> select = service.queryData(requestContext, dto, page, pageSize);
		return new ResponseData(select);
	}

	@RequestMapping(value = "/hqm/technology/wp/action/submit")
	@ResponseBody
	public ResponseData update(@RequestBody List<TechnologyWpAction> dto, BindingResult result,
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

	@RequestMapping(value = "/hqm/technology/wp/action/remove")
	@ResponseBody
	public ResponseData delete(HttpServletRequest request, @RequestBody List<TechnologyWpAction> dto) {
		service.batchDelete(dto);
		return new ResponseData();
	}

	@RequestMapping(value = "/hqm/technology/wpaction/list/query")
	@ResponseBody
	public ResponseData queryWpaction(TechnologyWpAction dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
			@RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
		return new ResponseData(service.queryWpactionList(dto));
	}

	// 获取物料的lov
	@RequestMapping(value = "/npi/technology/wpaction/getItem")
	@ResponseBody
	public ResponseData getItemLov(EbomMain dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
			@RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
		IRequest requestCtx = createRequestContext(request);
		return new ResponseData(service.queryItemLov(requestCtx, dto, page, pageSize));
	}

	// 获取物料属性
	@RequestMapping(value = "/npi/technology/wpaction/getMatAttr")
	@ResponseBody
	public ResponseData getMaterielDataLov(TechnologyWpAction dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
			@RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
		IRequest requestCtx = createRequestContext(request);
		return new ResponseData(service.queryMatAttrLov(requestCtx, dto, page, pageSize));
	}

	// 带出标准动作的其他属性
	@RequestMapping(value = "/npi/technology/wpaction/getSpecMatAttr")
	@ResponseBody
	public ResponseData getSpecMat(@RequestParam(value="materielIds",required=false)String materielIds,
			@RequestParam(value="id",required=false)String id, HttpServletRequest request) {
		IRequest requestCtx = createRequestContext(request);
		List<TechnologySpecDetail> checkMatAttr = service.checkMatAttr(materielIds, id);
		List<TechnologySpecDetail> returnList=new LinkedList<>();
		if (checkMatAttr != null) {
			return new ResponseData(checkMatAttr);
		}
		return new ResponseData(returnList);
	}
	
	//工艺动作首页 工序展示工艺动作的detailInit方法
	@RequestMapping(value = "/npi/technology/wp/action/detailInit")
	@ResponseBody
	public ResponseData wpActionDetailInit(TechnologyWpAction dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
			@RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
		IRequest requestContext = createRequestContext(request);
		List<TechnologyWpAction> select = service.queryData(requestContext, dto, page, pageSize);
		return new ResponseData(select);
	}
	
	//查询出工序的工艺动作信息和物料属性
	@RequestMapping(value = "/npi/technology/wp/action/queryActionInfo")
	@ResponseBody
	public ResponseData queryActionInfo(HttpServletRequest request,@RequestBody TechnologyWpAction dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
			@RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize) {
		IRequest requestCtx = createRequestContext(request);
		return new ResponseData(service.queryActionInfo(requestCtx,dto,page,pageSize));
	}
}