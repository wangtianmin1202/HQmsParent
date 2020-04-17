package com.hand.spc.ecr_main.controllers;

import org.springframework.stereotype.Controller;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.spc.ecr_main.dto.EcrProjectTracking;
import com.hand.spc.ecr_main.service.IEcrProjectTrackingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import org.springframework.validation.BindingResult;
import java.util.List;

    @Controller
    public class EcrProjectTrackingController extends BaseController{

    @Autowired
    private IEcrProjectTrackingService service;

    /**
     * 查询本表信息
     * @param dto
     * @param page
     * @param pageSize
     * @param request
     * @return
     */
    @RequestMapping(value = "/hpm/ecr/project/tracking/query")
    @ResponseBody
    public ResponseData query(EcrProjectTracking dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
        @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        if(dto.getEcrno() == null) {
        	return new ResponseData();
        }
        return new ResponseData(service.selectGrid(requestContext,dto,page,pageSize));
    }


    /**
     * 查询项目跟踪信息基本信息（根据ECR编码）
     * @param ecrno
     * @param request
     * @return
     */
    @RequestMapping(value = "/hpm/ecr/project/tracking/queryInfo")
    @ResponseBody
    public ResponseData queryInfo(@RequestParam("ecrno") String ecrno, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.selectInfo(requestContext, ecrno));
    } 
    
    /**
     * 新建数据-根据ECR编码
     * @param ecrno
     * @param request
     * @return
     */
    @RequestMapping(value = "/hpm/ecr/project/tracking/insertInfo")
    @ResponseBody
    public ResponseData insertInfo(@RequestParam("ecrno") String ecrno, HttpServletRequest request) {
    	IRequest requestContext = createRequestContext(request);
    	return new ResponseData(service.insertInfo(requestContext, ecrno));
    } 
    
    
    /**
     * ECR受影响物料价格趋势查询报表 
     * @param skuId
     * @param request
     * @return
     */
    @RequestMapping(value = "/hpm/ecr/project/tracking/selectInfluence")
    @ResponseBody
    public ResponseData selectInfluence(@RequestParam("skuId") String skuId, HttpServletRequest request) {
    	IRequest requestContext = createRequestContext(request);
    	return new ResponseData(service.selectInfluence(requestContext, skuId));
    } 
    
    /**
     * SKU 实际成本计算
     * @param ecrno
     * @param skuId
     * @param request
     * @return
     */
    @RequestMapping(value = "/hpm/ecr/project/tracking/calculateActualCost")
    @ResponseBody
    public ResponseData calculateActualCost(@RequestParam("ecrno") String ecrno, 
    		@RequestParam("skuId") String skuId, HttpServletRequest request) {
    	IRequest requestContext = createRequestContext(request);
    	service.calculateActualCost(requestContext, ecrno, skuId);
    	return new ResponseData();
    } 
    
    /**
     * QTP任务写到QTP任务表
     * @param request
     * @return
     */
    @RequestMapping(value = "/hpm/ecr/project/tracking/qtp")
    @ResponseBody
    public ResponseData qtp(HttpServletRequest request, @RequestParam("ecrno") String ecrno) {
    	IRequest requestContext = createRequestContext(request);
    	service.qtp(requestContext, ecrno);
    	return new ResponseData();
    } 
    
    /**
     * QTP 任务负责人分配
     * @param request
     * @return
     */
    @RequestMapping(value = "/hpm/ecr/project/tracking/qtpMainDuty")
    @ResponseBody
    public ResponseData qtpMainDuty(HttpServletRequest request,
    		@RequestParam("ecrno") String ecrno, 
    		@RequestParam("itemId") String itemId) {
    	IRequest requestContext = createRequestContext(request);
    	service.qtpMainDuty(requestContext,ecrno,itemId);
    	return new ResponseData();
    } 
    
    /**
     * VTP任务写到VTP任务表
     * @param request
     * @return
     */
    @RequestMapping(value = "/hpm/ecr/project/tracking/vtp")
    @ResponseBody
    public ResponseData vtp(HttpServletRequest request, @RequestParam("ecrno") String ecrno) {
    	IRequest requestContext = createRequestContext(request);
    	service.vtp(requestContext, ecrno);
    	return new ResponseData();
    } 
    
    
    /**
     * VTP 任务负责人分配
     * @param request
     * @return
     */
    @RequestMapping(value = "/hpm/ecr/project/tracking/vtpMainDuty")
    @ResponseBody
    public ResponseData vtpMainDuty(HttpServletRequest request,
    		@RequestParam("ecrno") String ecrno, 
    		@RequestParam("itemId") String itemId) {
    	IRequest requestContext = createRequestContext(request);
    	service.vtpMainDuty(requestContext,ecrno,itemId);
    	return new ResponseData();
    } 
    
    /**
     * PCI任务写到PCI任务表
     * @param request
     * @return
     */
    @RequestMapping(value = "/hpm/ecr/project/tracking/pci")
    @ResponseBody
    public ResponseData pci(HttpServletRequest request, @RequestParam("ecrno") String ecrno) {
    	IRequest requestContext = createRequestContext(request);
    	service.pci(requestContext, ecrno);
    	return new ResponseData();
    } 
    
    @RequestMapping(value = "/hpm/ecr/project/tracking/submit")
    @ResponseBody
    public ResponseData update(@RequestBody List<EcrProjectTracking> dto, BindingResult result, HttpServletRequest request){
        getValidator().validate(dto, result);
        if (result.hasErrors()) {
        ResponseData responseData = new ResponseData(false);
        responseData.setMessage(getErrorMessage(result, request));
        return responseData;
        }
        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(service.batchUpdate(requestCtx, dto));
    }

    @RequestMapping(value = "/hpm/ecr/project/tracking/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request,@RequestBody List<EcrProjectTracking> dto){
        service.batchDelete(dto);
        return new ResponseData();
    }
    }