package com.hand.hqm.hqm_control_plan.controllers;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hand.hap.core.IRequest;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hqm.hqm_control_plan.dto.ControlPlanFeatures;
import com.hand.hqm.hqm_control_plan.service.IControlPlanFeaturesService;

    @Controller
    public class ControlPlanFeaturesController extends BaseController{

    @Autowired
    private IControlPlanFeaturesService service;


    @RequestMapping(value = "/hqm/control/plan/features/query")
    @ResponseBody
    public ResponseData query(ControlPlanFeatures dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
        @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.queryPrintData(dto));//select(requestContext,dto,page,pageSize)
    }
    
    @RequestMapping(value = "/hqm/control/plan/features/header/query")
    @ResponseBody
    public ControlPlanFeatures query_header(@RequestParam Long controlPlanId,HttpServletRequest request) {
        return  service.queryHeaderData(controlPlanId);//select(requestContext,dto,page,pageSize)
    }

    @RequestMapping(value = "/hqm/control/plan/features/submit")
    @ResponseBody
    public ResponseData update(@RequestBody List<ControlPlanFeatures> dto, BindingResult result, HttpServletRequest request){
        getValidator().validate(dto, result);
        if (result.hasErrors()) {
        ResponseData responseData = new ResponseData(false);
        responseData.setMessage(getErrorMessage(result, request));
        return responseData;
        }
        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(service.batchUpdate(requestCtx, dto));
    }

    @RequestMapping(value = "/hqm/control/plan/features/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request,@RequestBody List<ControlPlanFeatures> dto){
        service.batchDelete(dto);
        return new ResponseData();
    }
    }