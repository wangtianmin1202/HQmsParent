package com.hand.spc.ecr_main.controllers;

import org.springframework.stereotype.Controller;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.spc.ecr_main.dto.EcrRfq;
import com.hand.spc.ecr_main.service.IEcrRfqService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import org.springframework.validation.BindingResult;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

    @Controller
    public class EcrRfqController extends BaseController{

    @Autowired
    private IEcrRfqService service;


    @RequestMapping(value = "/hpm/ecr/rfq/query")
    @ResponseBody
    public ResponseData query(EcrRfq dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
        @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.select(requestContext,dto,page,pageSize));
    }

    @RequestMapping(value = "/hpm/ecr/rfq/submit")
    @ResponseBody
    public ResponseData update(@RequestBody List<EcrRfq> dto, BindingResult result, HttpServletRequest request){
        getValidator().validate(dto, result);
        if (result.hasErrors()) {
        ResponseData responseData = new ResponseData(false);
        responseData.setMessage(getErrorMessage(result, request));
        return responseData;
        }
        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(service.batchUpdate(requestCtx, dto));
    }

    @RequestMapping(value = "/hpm/ecr/rfq/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request,@RequestBody List<EcrRfq> dto){
        service.batchDelete(dto);
        return new ResponseData();
    }
    
    /**
     * RFQ 任务写到任务列表
     * @param dto
     * @param page
     * @param pageSize
     * @param request
     * @return
     */
    @RequestMapping(value = "/hpm/ecr/rfq/init")
    @ResponseBody
    public ResponseData initData(@RequestParam("ecrno") String ecrno,
    		@RequestParam("list") List<String> list,
    		HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        service.initData(requestContext, ecrno, list);
        return new ResponseData();
    }
    
    /**
     * - 查询 ECR 发送的任务信息
     * @param dto 
     * 	-参数：ecrno, taskType(工作流类型)
     * @return
     */
    @RequestMapping(value = "/hpm/ecr/rfq/task/process/query")
    @ResponseBody
    public ResponseData queryTask(EcrRfq dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
            @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
    	IRequest requestContext = createRequestContext(request);
    	return new ResponseData(service.selectTask(requestContext, dto, page, pageSize));
    }
    
    /**
     * - 保存负责人确认节点信息
     * @param dto
     * @param result
     * @param request
     * @return
     */
    @RequestMapping(value = "/hpm/ecr/rfq/task/process/submit")
    @ResponseBody
    public ResponseData saveTask(@RequestBody List<EcrRfq> dto, @RequestParam("taskType") String taskType
    		,BindingResult result, HttpServletRequest request){
        getValidator().validate(dto, result);
        if (result.hasErrors()) {
	        ResponseData responseData = new ResponseData(false);
	        responseData.setMessage(getErrorMessage(result, request));
	        return responseData;
        }
        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(service.saveTask(requestCtx, dto, taskType));
    }
    
    /**
     * - 查询任务完成状态，完成时将新版本和时间更新到任务表中,再结束流程
     * @param dto
     * @param result
     * @param request
     * @return
     */
    @RequestMapping(value = "/hpm/ecr/rfq/task/process/taskFinish")
    @ResponseBody
    public ResponseData taskFinish(HttpServletRequest request
    		,  @RequestParam("ecrno") String ecrno 
    	    ,  @RequestParam("taskType") String taskType ){
    	IRequest requestCtx = createRequestContext(request);
    	String newVersion = "taskFinishVersion";
    	String taskStatus = "FINISHED";
    	Date actFinishDate = new Date();
		service.taskFinish(requestCtx, taskType, ecrno, null, newVersion, taskStatus, actFinishDate);
    	return new ResponseData();
    }
    
    @RequestMapping(value = "/hpm/ecr/rfq/task/process/taskFinish/vtp")
    @ResponseBody
    public ResponseData taskFinishVtp(HttpServletRequest request
    		,  @RequestParam("ecrno") String ecrno ){
    	IRequest requestCtx = createRequestContext(request);
    	String newVersion = "taskFinishVersion";
    	String taskStatus = "FINISHED";
    	Date actFinishDate = new Date();
    	//for(int i=0;i<3;i++) {
    		service.taskFinish(requestCtx, "PFMEA", ecrno, null, newVersion, taskStatus, actFinishDate);
    		service.taskFinish(requestCtx, "控制计划", ecrno, null, newVersion, taskStatus, actFinishDate);
    		service.taskFinish(requestCtx, "工装夹具", ecrno, null, newVersion, taskStatus, actFinishDate);
    		service.taskFinish(requestCtx, "SOP", ecrno, null, newVersion, taskStatus, actFinishDate);
    		service.taskFinish(requestCtx, "QTP", ecrno, null, newVersion, taskStatus, actFinishDate);
    	//}
    	return new ResponseData();
    }
    
    @RequestMapping(value = "/hpm/ecr/rfq/task/process/taskFinish/pci")
    @ResponseBody
    public ResponseData taskFinishPci(HttpServletRequest request
    		,  @RequestParam("ecrno") String ecrno ){
    	IRequest requestCtx = createRequestContext(request);
    	String newVersion = "taskFinishVersion";
    	String taskStatus = "FINISHED";
    	Date actFinishDate = new Date();
    	//for(int i=0;i<3;i++) {     
    		service.taskFinish(requestCtx, "VTP", ecrno, null, newVersion, taskStatus, actFinishDate);
    	//}
    	return new ResponseData();
    }
    
    /**
     * RFQ 任务写到任务列表且启动工作流
     * @param dto`
     * @param page
     * @param pageSize
     * @param request
     * @return
     */
    @RequestMapping(value = "/hpm/ecr/rfq/process/rfqStart")
    @ResponseBody
    public ResponseData rfqStart(@RequestParam("ecrno") String ecrno,
    		@RequestParam("list") List<String> list,
    		HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        service.startProcessRfq(requestContext, ecrno, list);
        return new ResponseData();
    }
    
    /**
     * - 查询 isNeed 字段，更新 hpm_ecr_main 字段是否 QTP，VTP 要做
     * @param dto
     * @param result
     * @param request
     * @return
     */
    @RequestMapping(value = "/hpm/ecr/rfq/task/process/updateIsNeed")
    @ResponseBody
    public ResponseData updateIsNeed(HttpServletRequest request
    		,  @RequestParam("ecrno") String ecrno 
    	    ,  @RequestParam("taskType") String taskType
    	    ,  @RequestParam("id") String id
    	    ,  @RequestParam("dutyby") String dutyby
    	    ){
    	IRequest requestCtx = createRequestContext(request);
		service.updateIsNeed(requestCtx, ecrno, taskType, id, dutyby);
    	return new ResponseData();
    }
    
}