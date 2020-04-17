package com.hand.spc.ecr_main.controllers;

import org.springframework.stereotype.Controller;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.spc.ecr_main.dto.EcrMain;
import com.hand.spc.ecr_main.service.IEcrMainService;
import com.hand.spc.ecr_main.view.EcrMainV0;
import com.hand.spc.ecr_main.view.EcrMainV1;
import com.hand.spc.ecr_main.view.EcrMainV2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import org.springframework.validation.BindingResult;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

    @Controller
    public class EcrMainController extends BaseController{

    @Autowired
    private IEcrMainService service;


    @RequestMapping(value = "/hpm/ecr/main/query")
    @ResponseBody
    public ResponseData query(EcrMainV1 dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
        @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.baseQuery(requestContext, dto, page, pageSize));
     //   return new ResponseData(service.select(requestContext,dto,page,pageSize));
    }

    
    @RequestMapping(value = "/hpm/ecr/main/query/single")
    @ResponseBody
    public ResponseData query(@RequestBody EcrMain dto, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.selectSingleOrder(requestContext, dto));
    }
    
    
    @RequestMapping(value = "/hpm/ecr/main/query/plan/date")
    @ResponseBody
    public ResponseData finishedDate(@RequestBody EcrMain dto, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        ResponseData rd=new ResponseData();
        SimpleDateFormat planDate = new SimpleDateFormat("yyyy-MM-dd");
        rd.setMessage(planDate.format(service.getChangeFinishedDate(requestContext, dto.getIssueType(), new Date(), dto.getRiskdegree())));
        rd.setSuccess(true);
        return rd;
    }
    
    
    @RequestMapping(value = "/hpm/ecr/main/submit")
    @ResponseBody
    public ResponseData update(@RequestBody List<EcrMain> dto, BindingResult result, HttpServletRequest request){
        getValidator().validate(dto, result);
        if (result.hasErrors()) {
        ResponseData responseData = new ResponseData(false);
        responseData.setMessage(getErrorMessage(result, request));
        return responseData;
        }
        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(service.batchUpdate(requestCtx, dto));
    }

    @RequestMapping(value = "/hpm/ecr/main/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request,@RequestBody List<EcrMain> dto){
        service.batchDelete(dto);
        return new ResponseData();
    }
    
    @RequestMapping(value = "/hpm/ecr/main/commfirm")
    @ResponseBody
    public ResponseData createOrder(HttpServletRequest request,@RequestBody EcrMainV0 dto){
    	 IRequest requestCtx = createRequestContext(request);
    	 ResponseData responseData=new ResponseData();
    	 try {
    		 responseData =service.createOrder(requestCtx, dto) ;    		  
    		 	return responseData;
    	 }
    	 catch(Exception ex) {
    		 responseData.setMessage(ex.getMessage());
    		 responseData.setSuccess(false);
    		 return responseData;    				 
    	 } 
    }
    
    @RequestMapping(value = "/hpm/ecr/main/duty")
    @ResponseBody
    public ResponseData confirm(HttpServletRequest request,@RequestBody EcrMainV2 dto){
    	 IRequest requestCtx = createRequestContext(request);
    	 ResponseData responseData=new ResponseData();
    	 try {
    		 responseData =service.saveMainDuty(requestCtx, dto) ;    		  
    		 	return responseData;
    	 }
    	 catch(Exception ex) {
    		 responseData.setMessage(ex.getMessage());
    		 responseData.setSuccess(false);
    		 return responseData;    				 
    	 } 
    }
    

    @RequestMapping(value = "/hpm/ecr/main/startProcess")
    @ResponseBody
    public ResponseData startProcess(HttpServletRequest request) {
    	IRequest requestCtx = createRequestContext(request);
    	service.startProcess(requestCtx,new EcrMain());
        return new ResponseData();
    }
}