package com.hand.npi.npi_technology.controllers;

import org.springframework.stereotype.Controller;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.npi.npi_technology.dto.SopRouteRef;
import com.hand.npi.npi_technology.service.ISopRouteRefService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import org.springframework.validation.BindingResult;
import java.util.List;

    @Controller
    public class SopRouteRefController extends BaseController{

    @Autowired
    private ISopRouteRefService service;


    @RequestMapping(value = "/npi/sop/route/ref/query")
    @ResponseBody
    public ResponseData query(SopRouteRef dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
        @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.queryData(requestContext,dto,page,pageSize));
    }
    
    @RequestMapping(value = "/npi/sop/route/ref/hisQuery")
    @ResponseBody
    public ResponseData hisQuery(SopRouteRef dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
    		@RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
    	IRequest requestContext = createRequestContext(request);
    	return new ResponseData(service.hisQuery(requestContext,dto,page,pageSize));
    }

    @RequestMapping(value = "/npi/sop/route/ref/submit")
    @ResponseBody
    public ResponseData update(@RequestBody List<SopRouteRef> dto, BindingResult result, HttpServletRequest request){
        getValidator().validate(dto, result);
        if (result.hasErrors()) {
        ResponseData responseData = new ResponseData(false);
        responseData.setMessage(getErrorMessage(result, request));
        return responseData;
        }
        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(service.batchUpdate(requestCtx, dto));
    }

    @RequestMapping(value = "/npi/sop/route/ref/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request,@RequestBody List<SopRouteRef> dto){
        service.batchDelete(dto);
        return new ResponseData();
    }
    
    
    @RequestMapping(value = "/npi/sop/route/ref/upload")
   	@ResponseBody
   	public ResponseData fileUpload(HttpServletRequest request) {
   		
   		ResponseData responseData = new ResponseData();
   		
   		IRequest requestCtx = createRequestContext(request);
   		responseData = service.fileUpload(requestCtx,request);
       	
           return responseData;
   	
   	}
    
    @RequestMapping(value = "/npi/sop/route/ref/change")
   	@ResponseBody
   	public ResponseData sopChange(@RequestBody SopRouteRef dto,HttpServletRequest request) {
   		
   		ResponseData responseData = new ResponseData();
   		
   		IRequest requestCtx = createRequestContext(request);
   		responseData = service.sopChange(requestCtx, dto);
       	
           return responseData;
   	
   	}
    }