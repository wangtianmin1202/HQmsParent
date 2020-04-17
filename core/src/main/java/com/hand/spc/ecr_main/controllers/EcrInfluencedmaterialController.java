package com.hand.spc.ecr_main.controllers;

import org.springframework.stereotype.Controller;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.spc.ecr_main.dto.EcrInfluencedmaterial;
import com.hand.spc.ecr_main.dto.EcrMain;
import com.hand.spc.ecr_main.service.IEcrInfluencedmaterialService;
import com.hand.spc.ecr_main.view.EcrMaterialV0;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import org.springframework.validation.BindingResult;
import java.util.List;

    @Controller
    public class EcrInfluencedmaterialController extends BaseController{

    @Autowired
    private IEcrInfluencedmaterialService service;

    
    @RequestMapping(value = "/hpm/ecr/influencedmaterial/query")
    @ResponseBody
    public ResponseData query(EcrInfluencedmaterial dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
        @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.select(requestContext,dto,page,pageSize));
    }

    /*
     * 基础查询
     */
    @RequestMapping(value = "/hpm/ecr/influencedmaterial/bsquery")
    @ResponseBody
    public ResponseData basequery(EcrInfluencedmaterial dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
        @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.basequery(dto));
    }
/*
 * 基本一致 调用参数不一样
 * 
 */
    @RequestMapping(value = "/hpm/ecr/influencedmaterial/bsquerys")
    @ResponseBody
    public ResponseData basequery1(@RequestBody  EcrInfluencedmaterial dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
        @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.basequery(dto));
    }
    
    /*
     * 负责人对应查询
     */
    @RequestMapping(value = "/hpm/ecr/main/duty/single")
    @ResponseBody
    public ResponseData queryDuty(@RequestBody EcrMain dto, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.dutySingleOrder(requestContext, dto));
    }
    
   /*
    * 物料在BOM中所用的产品类别 这里以后要重写逻辑
    */
    @RequestMapping(value = "/hpm/ecr/influencedmaterial/bsdetailquery")
    @ResponseBody
    public ResponseData baseDetailquery(EcrInfluencedmaterial dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
        @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.detailProductquery(dto));
    }
    
    
    /*
     * 获取自动计算结果数据展示     
     */
     @RequestMapping(value = "/hpm/ecr/influencedmaterial/item/control/result")
     @ResponseBody
     public ResponseData getItemResult (@RequestBody EcrMain dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
         @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
         IRequest requestContext = createRequestContext(request);
         ResponseData responseData=new ResponseData();
         try {
        	 responseData=new 	ResponseData(service.getItemResult(requestContext, dto));
        	 responseData.setSuccess(true);
         }
         catch(Exception ex) {
        	 responseData.setSuccess(false);
        	 responseData.setMessage(ex.getMessage());
         }
         return responseData;
     }
     /*
      * 获取自动计算结果数据展示     
      */
      @RequestMapping(value = "/hpm/ecr/influencedmaterial/item/control/save")
      @ResponseBody
      public ResponseData saveItemStatus (@RequestBody List<EcrMaterialV0>  dto , HttpServletRequest request) {
          IRequest requestContext = createRequestContext(request);
          ResponseData responseData=new ResponseData();
          try {
         	 responseData= service.saveItemControl(requestContext, dto) ;
         	 responseData.setSuccess(true);
          }
          catch(Exception ex) {
         	 responseData.setSuccess(false);
         	 responseData.setMessage(ex.getMessage());
          }
          return responseData;
      }
    
    @RequestMapping(value = "/hpm/ecr/influencedmaterial/submit")
    @ResponseBody
    public ResponseData update(@RequestBody List<EcrInfluencedmaterial> dto, BindingResult result, HttpServletRequest request){
        getValidator().validate(dto, result);
        if (result.hasErrors()) {
        ResponseData responseData = new ResponseData(false);
        responseData.setMessage(getErrorMessage(result, request));
        return responseData;
        }
        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(service.batchUpdate(requestCtx, dto));
    }

    @RequestMapping(value = "/hpm/ecr/influencedmaterial/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request,@RequestBody List<EcrInfluencedmaterial> dto){
        service.batchDelete(dto);
        return new ResponseData();
    }
    }