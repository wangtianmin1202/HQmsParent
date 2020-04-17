package com.hand.hqm.hqm_measure_tool_use.controllers;

import org.springframework.stereotype.Controller;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.activiti.exception.TaskActionException;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hqm.hqm_measure_tool_use.dto.MeasureToolUse;
import com.hand.hqm.hqm_measure_tool_use.service.IMeasureToolUseService;
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
    public class MeasureToolUseController extends BaseController{

    @Autowired
    private IMeasureToolUseService service;


    @RequestMapping(value = "/hqm/measure/tool/use/query")
    @ResponseBody
    public ResponseData query(MeasureToolUse dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
        @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.select(requestContext,dto,page,pageSize));
    }

    @RequestMapping(value = "/hqm/measure/tool/use/submit")
    @ResponseBody
    public ResponseData update(@RequestBody List<MeasureToolUse> dto, BindingResult result, HttpServletRequest request){
        getValidator().validate(dto, result);
        if (result.hasErrors()) {
        ResponseData responseData = new ResponseData(false);
        responseData.setMessage(getErrorMessage(result, request));
        return responseData;
        }
        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(service.batchUpdate(requestCtx, dto));
    }

    @RequestMapping(value = "/hqm/measure/tool/use/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request,@RequestBody List<MeasureToolUse> dto){
        service.batchDelete(dto);
        return new ResponseData();
    }
    
    /**
     * 
     * @Description:领用单主界面查询
     * @param dto
     * @param page
     * @param pageSize
     * @param request
     * @return
     */
    @RequestMapping(value = "/hqm/measure/tool/use/queryByCondition")
    @ResponseBody
    public ResponseData queryByCondition(MeasureToolUse dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
        @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.queryByCondition(requestContext,dto,page,pageSize));
    }
    
    /**
     * 
     * @Description:量具领用单新增
     * @param dto
     * @param result
     * @param request
     * @return
     */
    @RequestMapping(value = "/hqm/measure/tool/use/add")
    @ResponseBody
    public ResponseData add(@RequestBody MeasureToolUse dto, BindingResult result, HttpServletRequest request){
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
     * @Description:创建领用单工作流
     * @param dto
     * @param result
     * @param request
     * @return
     */
    @RequestMapping(value = "/hqm/measure/tool/use/createProcessInstance")
    @ResponseBody
    public ResponseData createProcessInstance(@RequestBody MeasureToolUse dto, BindingResult result, HttpServletRequest request){
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
    @RequestMapping(value = "/hqm/measure/tool/use/approveProcess")
    @ResponseBody
    public ResponseData approveProcess(@RequestBody WflRequest<MeasureToolUse> param, 
    		@RequestParam String processInstanceId, @RequestParam Integer flowNum, HttpServletRequest request) throws TaskActionException{
        
        IRequest requestCtx = createRequestContext(request);
        return service.approveProcess(requestCtx, param.getDto(), param.getActionRequest(), processInstanceId, flowNum);
    }
    
    /**
     * 
     * @Description:归还按钮
     * @param dto
     * @param request
     * @return
     */
    @RequestMapping(value = "/hqm/measure/tool/use/confirmReturn")
    @ResponseBody
    public ResponseData confirmReturn(MeasureToolUse dto, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.confirmReturn(requestContext,dto));
    }
    }