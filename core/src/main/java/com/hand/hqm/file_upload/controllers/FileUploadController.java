package com.hand.hqm.file_upload.controllers;

import org.springframework.stereotype.Controller;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.activiti.exception.TaskActionException;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hqm.file_upload.dto.FileUpload;
import com.hand.hqm.file_upload.service.IFileUploadService;
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
    public class FileUploadController extends BaseController{

    @Autowired
    private IFileUploadService service;


    @RequestMapping(value = "/file/upload/query")
    @ResponseBody
    public ResponseData query(FileUpload dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
        @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.select(requestContext,dto,page,pageSize));
    }

    @RequestMapping(value = "/file/upload/submit")
    @ResponseBody
    public ResponseData update(@RequestBody List<FileUpload> dto, BindingResult result, HttpServletRequest request){
        getValidator().validate(dto, result);
        if (result.hasErrors()) {
        ResponseData responseData = new ResponseData(false);
        responseData.setMessage(getErrorMessage(result, request));
        return responseData;
        }
        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(service.batchUpdate(requestCtx, dto));
    }

    @RequestMapping(value = "/file/upload/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request,@RequestBody List<FileUpload> dto){
        service.batchDelete(dto);
        return new ResponseData();
    }
    
    /**
     * 
     * @Description:申请单界面主查询
     * @param dto
     * @param page
     * @param pageSize
     * @param request
     * @return
     */
    @RequestMapping(value = "/file/upload/queryByCondition")
    @ResponseBody
    public ResponseData queryByCondition(FileUpload dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
        @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.queryByCondition(requestContext,dto,page,pageSize));
    }
    
    @RequestMapping(value = "/file/upload/add")
    @ResponseBody
    public ResponseData add(@RequestBody FileUpload dto, HttpServletRequest request){
        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(service.add(requestCtx, dto));
    }
    
    /**
     * 
     * @Description:创建工作流
     * @param dto
     * @param request
     * @return
     */
    @RequestMapping(value = "/file/upload/createProcessInstance")
    @ResponseBody
    public ResponseData createProcessInstance(@RequestBody FileUpload dto, HttpServletRequest request){
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
    @RequestMapping(value = "/file/upload/approveProcess")
    @ResponseBody
    public ResponseData approveProcess(@RequestBody WflRequest<FileUpload> param, 
    		@RequestParam String processInstanceId, @RequestParam Integer flowNum, HttpServletRequest request) throws TaskActionException{
        
        IRequest requestCtx = createRequestContext(request);
        return service.approveProcess(requestCtx, param.getDto(), param.getActionRequest(), processInstanceId, flowNum);
        //return new ResponseData();
    }
    }