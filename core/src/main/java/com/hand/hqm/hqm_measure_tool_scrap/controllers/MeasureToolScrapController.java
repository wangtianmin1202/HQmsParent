package com.hand.hqm.hqm_measure_tool_scrap.controllers;

import org.springframework.stereotype.Controller;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.activiti.exception.TaskActionException;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hqm.hqm_measure_tool_scrap.dto.MeasureToolScrap;
import com.hand.hqm.hqm_measure_tool_scrap.service.IMeasureToolScrapService;
import com.hand.hqm.hqm_measure_tool_use.dto.MeasureToolUse;
import com.hand.wfl.util.WflRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import org.springframework.validation.BindingResult;
import java.util.List;

    @Controller
    public class MeasureToolScrapController extends BaseController{

    @Autowired
    private IMeasureToolScrapService service;


    @RequestMapping(value = "/hqm/measure/tool/scrap/query")
    @ResponseBody
    public ResponseData query(MeasureToolScrap dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
        @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.select(requestContext,dto,page,pageSize));
    }

    @RequestMapping(value = "/hqm/measure/tool/scrap/submit")
    @ResponseBody
    public ResponseData update(@RequestBody List<MeasureToolScrap> dto, BindingResult result, HttpServletRequest request){
        getValidator().validate(dto, result);
        if (result.hasErrors()) {
        ResponseData responseData = new ResponseData(false);
        responseData.setMessage(getErrorMessage(result, request));
        return responseData;
        }
        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(service.batchUpdate(requestCtx, dto));
    }

    @RequestMapping(value = "/hqm/measure/tool/scrap/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request,@RequestBody List<MeasureToolScrap> dto){
        service.batchDelete(dto);
        return new ResponseData();
    }
    
    /**
     * 
     * @Description:报废单主查询
     * @param dto
     * @param page
     * @param pageSize
     * @param request
     * @return
     */
    @RequestMapping(value = "/hqm/measure/tool/scrap/queryByCondition")
    @ResponseBody
    public ResponseData queryByCondition(MeasureToolScrap dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
        @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.queryByCondition(requestContext,dto,page,pageSize));
    }
    
    /**
     * 
     * @Description:新建报废单
     * @param dto
     * @param request
     * @return
     */
    @RequestMapping(value = "/hqm/measure/tool/scrap/add")
    @ResponseBody
    public ResponseData add(@RequestBody MeasureToolScrap dto, BindingResult result, HttpServletRequest request) {
    	getValidator().validate(dto, result);
        if (result.hasErrors()) {
        ResponseData responseData = new ResponseData(false);
        responseData.setMessage(getErrorMessage(result, request));
        return responseData;
        }
        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(service.add(requestCtx, dto));
    }
    
    /**
     * 
     * @Description:创建报废单工作流
     * @param dto
     * @param result
     * @param request
     * @return
     */
    @RequestMapping(value = "/hqm/measure/tool/scrap/createProcessInstance")
    @ResponseBody
    public ResponseData createProcessInstance(@RequestBody MeasureToolScrap dto, BindingResult result, HttpServletRequest request){
        getValidator().validate(dto, result);
        if (result.hasErrors()) {
        ResponseData responseData = new ResponseData(false);
        responseData.setMessage(getErrorMessage(result, request));
        return responseData;
        }
        IRequest requestCtx = createRequestContext(request);
        service.createProcessInstance(requestCtx, dto);
        return new ResponseData();
    }
    
    /**
     * 
     * @Description:审批工作流
     * @param param
     * @param processInstanceId
     * @param flowNum
     * @param request
     * @return
     * @throws TaskActionException
     */
    @RequestMapping(value = "/hqm/measure/tool/scrap/approveProcess")
    @ResponseBody
    public ResponseData approveProcess(@RequestBody WflRequest<MeasureToolScrap> param, 
    		@RequestParam String processInstanceId, @RequestParam Integer flowNum, HttpServletRequest request) throws TaskActionException{
        
        IRequest requestCtx = createRequestContext(request);
        return service.approveProcess(requestCtx, param.getDto(), param.getActionRequest(), processInstanceId, flowNum);
    }
    
    @RequestMapping(value = "/hqm/measure/tool/scrap/queryTask")
    @ResponseBody
    public ResponseData queryTask(@RequestParam String processInstanceId, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.queryTask(requestContext,processInstanceId));
    }
    }