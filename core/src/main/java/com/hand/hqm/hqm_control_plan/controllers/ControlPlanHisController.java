package com.hand.hqm.hqm_control_plan.controllers;

import org.springframework.stereotype.Controller;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hqm.hqm_control_plan.dto.ControlPlan;
import com.hand.hqm.hqm_control_plan.dto.ControlPlanHis;
import com.hand.hqm.hqm_control_plan.service.IControlPlanHisService;
import com.hand.hqm.hqm_control_plan.service.IControlPlanService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import org.springframework.validation.BindingResult;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

    @Controller
    public class ControlPlanHisController extends BaseController{

    @Autowired
    private IControlPlanHisService service;

    @Autowired
	private IControlPlanService controlPlanService;
    
    @RequestMapping(value = "/hqm/control/plan/his/query")
    @ResponseBody
    public ResponseData query(ControlPlanHis dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
        @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        ControlPlan controlPlan = new ControlPlan();
        controlPlan.setControlPlanId(dto.getControlPlanId());
        controlPlan=controlPlanService.selectByPrimaryKey(requestContext, controlPlan);
        List<ControlPlanHis> controlPlanHisList = new ArrayList<ControlPlanHis>();
        controlPlanHisList=service.select(requestContext,dto,page,pageSize);
        ControlPlanHis controlPlanHis = new ControlPlanHis();
        controlPlanHis.setControlVersion(controlPlan.getControlVersion());
        controlPlanHis.setControlVersionTime(controlPlan.getLastUpdateDate());
        controlPlanHisList.add(controlPlanHis);
        return new ResponseData(controlPlanHisList.stream().sorted(Comparator.comparing(ControlPlanHis::getControlVersion).reversed()).collect(Collectors.toList()));
    }

    @RequestMapping(value = "/hqm/control/plan/his/submit")
    @ResponseBody
    public ResponseData update(@RequestBody List<ControlPlanHis> dto, BindingResult result, HttpServletRequest request){
        getValidator().validate(dto, result);
        if (result.hasErrors()) {
        ResponseData responseData = new ResponseData(false);
        responseData.setMessage(getErrorMessage(result, request));
        return responseData;
        }
        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(service.batchUpdate(requestCtx, dto));
    }

    @RequestMapping(value = "/hqm/control/plan/his/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request,@RequestBody List<ControlPlanHis> dto){
        service.batchDelete(dto);
        return new ResponseData();
    }
    }