package com.hand.spc.ecr_main.controllers;

import org.springframework.stereotype.Controller;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.spc.ecr_main.dto.EcrSolutionSku;
import com.hand.spc.ecr_main.dto.EcrInfluencedmaterial;
import com.hand.spc.ecr_main.dto.EcrMain;
import com.hand.spc.ecr_main.dto.EcrSolutionMain;
import com.hand.spc.ecr_main.dto.EcrSolutionSku;
import com.hand.spc.ecr_main.service.IEcrSolutionSkuService;
import com.hand.spc.ecr_main.view.EcrSolutionSkuV1;
import com.hand.spc.ecr_main.view.EcrSolutionSkuV2;
import com.hand.spc.ecr_main.view.EcrSolutionSkuV4;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import org.springframework.validation.BindingResult;
import java.util.List;

    @Controller
    public class EcrSolutionSkuController extends BaseController{

    @Autowired
    private IEcrSolutionSkuService service;


    @RequestMapping(value = "/hpm/ecr/solution/sku/query")
    @ResponseBody
    public ResponseData query(EcrSolutionSku dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
        @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.select(requestContext,dto,page,pageSize));
    }

    @RequestMapping(value = "/hpm/ecr/solution/sku/submit")
    @ResponseBody
    public ResponseData update(@RequestBody List<EcrSolutionSku> dto, BindingResult result, HttpServletRequest request){
        getValidator().validate(dto, result);
        if (result.hasErrors()) {
        ResponseData responseData = new ResponseData(false);
        responseData.setMessage(getErrorMessage(result, request));
        return responseData;
        }
        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(service.batchUpdate(requestCtx, dto));
    }

    @RequestMapping(value = "/hpm/ecr/solution/sku/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request,@RequestBody List<EcrSolutionSku> dto){
        service.batchDelete(dto);
        return new ResponseData();
    }
    @RequestMapping(value = "/hpm/ecr/item/sku/query/head")
    @ResponseBody
    public ResponseData queryHead(EcrMain dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
        @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.getHeadSku(requestContext,dto,page,pageSize));
    } 
    
    @RequestMapping(value = "/hpm/ecr/item/sku/save")
    @ResponseBody
    public ResponseData saveHead(@RequestBody EcrSolutionSkuV2 dto, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return  service.saveResult(requestContext,dto);
    } 
    
    
    
    @RequestMapping(value = "/hpm/ecr/item/sku/commit")
    @ResponseBody
    public ResponseData commitHead(@RequestBody EcrSolutionMain dto, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return  service.commitSolution(requestContext, dto);
    } 
    
    @RequestMapping(value = "/hpm/ecr/item/sku/query/line")
    @ResponseBody
    public ResponseData queryLine(@RequestBody EcrSolutionSku dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
        @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.getLineSku(requestContext,dto,page,pageSize));
    } 
    
    
    @RequestMapping(value = "/hpm/ecr/item/drop/list")
    @ResponseBody
    public List<EcrSolutionSkuV1>  queryDropList(@RequestBody EcrMain dto , HttpServletRequest request) {       
        return service.getSolutionList(dto);
    } 
   
    
    @RequestMapping(value = "/hpm/ecr/item/rfq/detail")
    @ResponseBody
    public ResponseData queryRfqQuery(@RequestBody EcrInfluencedmaterial dto,  HttpServletRequest request) {        
        return new ResponseData(service.getDetailRfq(dto));
    } 
   
    @RequestMapping(value = "/hpm/ecr/item/rfq/commit")
    @ResponseBody
    public List<EcrSolutionSkuV1> queryRfqCommit( @RequestBody EcrMain dto,  HttpServletRequest request) {        
        return  service.getRfqCommitList(dto) ;
    } 
    
    @RequestMapping(value = "/hpm/ecr/item/rfq/commit/process")
    @ResponseBody
    public ResponseData queryRfqCommitProcess(@RequestBody   EcrSolutionSkuV4 dto,  HttpServletRequest request) {    
    	  IRequest requestContext = createRequestContext(request);
          return  service.rfqCommitProcess(requestContext, dto) ;
    } 
  }