package com.hand.spc.ecr_main.controllers;

import org.springframework.stereotype.Controller;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.spc.ecr_main.dto.EcrSolutionMain;
import com.hand.spc.ecr_main.service.IEcrSolutionMainService;
import com.hand.spc.ecr_main.view.EcrSolutionMainV0;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import org.springframework.validation.BindingResult;
import java.util.List;

    @Controller
    public class EcrSolutionMainController extends BaseController{

    @Autowired
    private IEcrSolutionMainService service;


    @RequestMapping(value = "/hpm/ecr/solution/main/query")
    @ResponseBody
    public ResponseData query(EcrSolutionMain dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
        @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.select(requestContext,dto,page,pageSize));
    }

    
    @RequestMapping(value = "/hpm/ecr/solution/main/query/bs")
    @ResponseBody
    public ResponseData bsquery(@RequestBody EcrSolutionMainV0 dto, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        List<EcrSolutionMainV0> ms=service.baseQuery(requestContext, dto);
        if(ms!=null)
        	 return new ResponseData(ms);
        return new ResponseData();
    }
    
    
    @RequestMapping(value = "/hpm/ecr/solution/main/query/bss")
    @ResponseBody
    public List<EcrSolutionMainV0> bsquers(@RequestBody EcrSolutionMainV0 dto, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        List<EcrSolutionMainV0> ms=service.baseQuery(requestContext, dto);
        return ms;
    }

    @RequestMapping(value = "/hpm/ecr/solution/main/submit")
    @ResponseBody
    public ResponseData update(@RequestBody List<EcrSolutionMain> dto, BindingResult result, HttpServletRequest request){
        getValidator().validate(dto, result);
        if (result.hasErrors()) {
        ResponseData responseData = new ResponseData(false);
        responseData.setMessage(getErrorMessage(result, request));
        return responseData;
        }
        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(service.batchUpdate(requestCtx, dto));
    }

    @RequestMapping(value = "/hpm/ecr/solution/main/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request,@RequestBody List<EcrSolutionMain> dto){
        service.batchDelete(dto);
        return new ResponseData();
    }
  }