package com.hand.npi.npi_technology.controllers;

import org.springframework.stereotype.Controller;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.npi.npi_technology.dto.InvalidCause;
import com.hand.npi.npi_technology.service.IInvalidCauseService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import org.springframework.validation.BindingResult;
import java.util.List;

    @Controller
    public class InvalidCauseController extends BaseController{

    @Autowired
    private IInvalidCauseService service;


    @RequestMapping(value = "/hqm/invalid/cause/query")
    @ResponseBody
    public ResponseData query(InvalidCause dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
        @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.select(requestContext,dto,page,pageSize));
    }

    @RequestMapping(value = "/hqm/invalid/cause/submit")
    @ResponseBody
    public ResponseData update(@RequestBody List<InvalidCause> dto, BindingResult result, HttpServletRequest request){
        getValidator().validate(dto, result);
        if (result.hasErrors()) {
        ResponseData responseData = new ResponseData(false);
        responseData.setMessage(getErrorMessage(result, request));
        return responseData;
        }
        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(service.batchUpdate(requestCtx, dto));
    }

    @RequestMapping(value = "/hqm/invalid/cause/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request,@RequestBody List<InvalidCause> dto){
        service.batchDelete(dto);
        return new ResponseData();
    }
    
    @RequestMapping(value = "/hqm/invalid/cause/add")
    @ResponseBody
    public ResponseData add(@RequestBody InvalidCause dto, BindingResult result, HttpServletRequest request){
        getValidator().validate(dto, result);
        if (result.hasErrors()) {
        ResponseData responseData = new ResponseData(false);
        responseData.setMessage(getErrorMessage(result, request));
        return responseData;
        }
        IRequest requestCtx = createRequestContext(request);
        return service.addNewInvalidCause(dto, requestCtx, request);
    }
    
    @RequestMapping(value = "/hqm/invalid/cause/delete/row")
	@ResponseBody
	public ResponseData deleteInvalidCause(InvalidCause dto, HttpServletRequest request) {
		ResponseData responseData = new ResponseData(true);
		try {
			service.deleteInvalidCause(dto);
		} catch (Exception e) {
			e.printStackTrace();
			responseData.setMessage(e.toString());
			responseData.setSuccess(false);
		}
		return responseData;
	}
    
    
    @RequestMapping(value = "/hqm/invalid/causeList/query")
    @ResponseBody
    public ResponseData queryCauseList(InvalidCause dto, HttpServletRequest request) {
        return new ResponseData(service.queryInvalidCauseList(dto));
    }
    
    }