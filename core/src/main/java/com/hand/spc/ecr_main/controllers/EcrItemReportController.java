package com.hand.spc.ecr_main.controllers;

import org.springframework.stereotype.Controller;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.spc.ecr_main.dto.EcrItemReport;
import com.hand.spc.ecr_main.service.IEcrItemReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import org.springframework.validation.BindingResult;
import java.util.List;

    @Controller
    public class EcrItemReportController extends BaseController{

    @Autowired
    private IEcrItemReportService service;


    @RequestMapping(value = "/hpm/ecr/item/report/query")
    @ResponseBody
    public ResponseData query(EcrItemReport dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
        @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.select(requestContext,dto,page,pageSize));
    }

    @RequestMapping(value = "/hpm/ecr/item/report/submit")
    @ResponseBody
    public ResponseData update(@RequestBody List<EcrItemReport> dto, BindingResult result, HttpServletRequest request){
        getValidator().validate(dto, result);
        if (result.hasErrors()) {
        ResponseData responseData = new ResponseData(false);
        responseData.setMessage(getErrorMessage(result, request));
        return responseData;
        }
        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(service.batchUpdate(requestCtx, dto));
    }

    @RequestMapping(value = "/hpm/ecr/item/report/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request,@RequestBody List<EcrItemReport> dto){
        service.batchDelete(dto);
        return new ResponseData();
    }
    
    @RequestMapping(value = "/hpm/ecr/item/report/get/head")
    @ResponseBody
  	public ResponseData getHead(@RequestBody EcrItemReport dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
    		        @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
    	IRequest requestContext = createRequestContext(request);
    	return new ResponseData(service.reportQuery(dto,page,pageSize));
    }

    @RequestMapping(value = "/hpm/ecr/item/report/get/detail")
    @ResponseBody
  	public ResponseData getDetail(@RequestBody EcrItemReport dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
    		        @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
    	IRequest requestContext = createRequestContext(request);
    	return new ResponseData(service.reportDetailQuery(dto,page,pageSize));
    }
    
    
    
    @RequestMapping(value = "/hpm/ecr/item/report/job/create")
    @ResponseBody
  	public ResponseData createReport(@RequestBody EcrItemReport dto , HttpServletRequest request) {
    	IRequest requestContext = createRequestContext(request);
    	service.checkEcrProcess(requestContext,dto.getEcrno());
    	ResponseData responseData=new ResponseData();
    	responseData.setMessage("OK");
    	return responseData;
    }
}