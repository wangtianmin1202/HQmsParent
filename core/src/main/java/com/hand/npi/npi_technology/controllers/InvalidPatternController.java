package com.hand.npi.npi_technology.controllers;

import org.springframework.stereotype.Controller;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.npi.npi_technology.dto.InvalidPattern;
import com.hand.npi.npi_technology.dto.TechnologySpec;
import com.hand.npi.npi_technology.service.IInvalidPatternService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import org.springframework.validation.BindingResult;
import java.util.List;

@Controller
public class InvalidPatternController extends BaseController {

	@Autowired
	private IInvalidPatternService service;

	@RequestMapping(value = "/hqm/invalid/pattern/query")
	@ResponseBody
	public ResponseData query(InvalidPattern dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
			@RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
		IRequest requestContext = createRequestContext(request);
		return new ResponseData(service.select(requestContext, dto, page, pageSize));
	}

	@RequestMapping(value = "/hqm/invalid/pattern/submit")
	@ResponseBody
	public ResponseData update(@RequestBody List<InvalidPattern> dto, BindingResult result,
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

	@RequestMapping(value = "/hqm/invalid/pattern/remove")
	@ResponseBody
	public ResponseData delete(HttpServletRequest request, @RequestBody List<InvalidPattern> dto) {
		service.batchDelete(dto);
		return new ResponseData();
	}

	@RequestMapping(value = "/hqm/invalid/pattern/add")
	@ResponseBody
	public ResponseData add(@RequestBody InvalidPattern dto, BindingResult result, HttpServletRequest request) {
		getValidator().validate(dto, result);
		if (result.hasErrors()) {
			ResponseData responseData = new ResponseData(false);
			responseData.setMessage(getErrorMessage(result, request));
			return responseData;
		}
		IRequest requestCtx = createRequestContext(request);
		return service.addNewInvalidPattern(dto, requestCtx, request);
	}

	@RequestMapping(value = "/hqm/invalid/pattern/delete/row")
	@ResponseBody
	public ResponseData deleteRow(InvalidPattern dto, HttpServletRequest request) {
		ResponseData responseData = new ResponseData(true);
		try {
			service.deleteRow(dto);
		} catch (Exception e) {
			e.printStackTrace();
			responseData.setMessage(e.toString());
			responseData.setSuccess(false);
		}
		return responseData;
	}

	@RequestMapping(value = "/npi/invalid/pattern/addData")
	@ResponseBody
	public ResponseData addData(@RequestBody List<InvalidPattern> dtos, BindingResult result,
			HttpServletRequest request) {
		getValidator().validate(dtos, result);
		if (result.hasErrors()) {
			ResponseData responseData = new ResponseData(false);
			responseData.setMessage(getErrorMessage(result, request));
			return responseData;
		}
		IRequest requestCtx = createRequestContext(request);
		return service.addInvalidPatterns(dtos, requestCtx, request);
	}
	@RequestMapping(value = "/npi/invalid/pattern/editData")
	@ResponseBody
	public ResponseData editData(@RequestBody List<InvalidPattern> dtos, BindingResult result,
			HttpServletRequest request) {
		/*getValidator().validate(dtos, result);
		if (result.hasErrors()) {
			ResponseData responseData = new ResponseData(false);
			responseData.setMessage(getErrorMessage(result, request));
			return responseData;
		}*/
		IRequest requestCtx = createRequestContext(request);
		return new ResponseData(service.editInvalidPatterns(dtos, requestCtx, request));
	}
	
	@RequestMapping(value = "/hqm/invalid/pattern/queryData")
	@ResponseBody
	public ResponseData queryData(InvalidPattern dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
			@RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
		IRequest requestContext = createRequestContext(request);
		return new ResponseData(service.queryData(requestContext, dto, page, pageSize));
	}
	
	@RequestMapping(value = "/npi/invalid/pattern/queryHisData")
    @ResponseBody
    public ResponseData queryHisData(InvalidPattern dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
        @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.queryHisData(dto, page, pageSize, requestContext, request));
    }
	@RequestMapping(value = "/npi/invalid/pattern/delDatas")
	@ResponseBody
	public ResponseData delDatas(@RequestBody List<InvalidPattern> dto, HttpServletRequest request) {
		service.deleteDatas(dto);
		return new ResponseData();
	}
    
	/**
	 * @description 执行审批，启动流程
	 * @author likai 2020.03.26
	 * @param dtos
	 * @param requestCtx
	 * @return
	 */
    @RequestMapping(value = "/npi/invalid/pattern/audit")
	@ResponseBody
    public ResponseData audit(@RequestBody List<InvalidPattern> dtos, HttpServletRequest request) {
    	IRequest requestCtx = createRequestContext(request);
		ResponseData responseData = new ResponseData();
		try {
			service.audit(dtos, requestCtx);
		} catch (Exception e) {
			responseData.setSuccess(false);
			responseData.setMessage(e.getMessage());
		}
		return responseData;
    }
    
    /**
	 * @author likai 2020.03.26
	 * @description 发起历史变更 新增或修改
	 * @param request
	 * @param dtos
	 * @return
	 */
    @RequestMapping(value = "/npi/invalid/pattern/addOrEditData")
    @ResponseBody
    public ResponseData addOrEditData(@RequestBody List<InvalidPattern> dtos, BindingResult result, HttpServletRequest request){
    	getValidator().validate(dtos, result);
        if (result.hasErrors()) {
        ResponseData responseData = new ResponseData(false);
        responseData.setMessage(getErrorMessage(result, request));
        return responseData;
        }
        IRequest requestCtx = createRequestContext(request);
        return service.addOrEditData(requestCtx, dtos);
    }
}