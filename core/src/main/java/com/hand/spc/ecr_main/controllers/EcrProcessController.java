package com.hand.spc.ecr_main.controllers;

import org.springframework.stereotype.Controller;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.spc.ecr_main.dto.EcrItemResult;
import com.hand.spc.ecr_main.dto.EcrMain;
import com.hand.spc.ecr_main.dto.EcrProcess;
import com.hand.spc.ecr_main.service.IEcrProcessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import org.springframework.validation.BindingResult;
import java.util.List;

    @Controller
    public class EcrProcessController extends BaseController{

    @Autowired
    private IEcrProcessService service;


    @RequestMapping(value = "/hpm/ecr/process/query")
    @ResponseBody
    public ResponseData query(EcrProcess dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
        @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.select(requestContext,dto,page,pageSize));
    }

    @RequestMapping(value = "/hpm/ecr/process/submit")
    @ResponseBody
    public ResponseData update(@RequestBody List<EcrProcess> dto, BindingResult result, HttpServletRequest request){
        getValidator().validate(dto, result);
        if (result.hasErrors()) {
        ResponseData responseData = new ResponseData(false);
        responseData.setMessage(getErrorMessage(result, request));
        return responseData;
        }
        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(service.batchUpdate(requestCtx, dto));
    }

    @RequestMapping(value = "/hpm/ecr/process/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request,@RequestBody List<EcrProcess> dto){
        service.batchDelete(dto);
        return new ResponseData();
    }
    
    /**
     *  1.1 启动流程入口(ECR&ECN 物料管控)
     * @param request
     * @return
     */
    @RequestMapping(value = "/hpm/ecr/process/matScrap/start")
    @ResponseBody
    public ResponseData startMatScrap(HttpServletRequest request) {
    	IRequest requestCtx = createRequestContext(request);
    	EcrMain dto = new EcrMain();
    	dto.setKid(1234L);
    	dto.setEcrno("ECR20200034");
    	dto.setProcessEmployeeCode("wl");
    	service.startMatScrap(requestCtx, dto);
        return new ResponseData();
    }
    

    /**
     * 1.2 流程中"再提交"按钮的业务功能(ECR&ECN 物料管控)
     * @param dto
     * @param request
     * @return
     */
    @RequestMapping(value = "/hpm/ecr/process/matScrap/commit")
    @ResponseBody
    public ResponseData matScrapCommit(@RequestBody EcrItemResult dto, HttpServletRequest request,
    		@RequestParam("ecrno") String ecrno) {
        IRequest requestContext = createRequestContext(request);
        service.matScrapCommit(requestContext,dto,ecrno);
        return new ResponseData();
    }


    /**
     * 1.3 更新流程状态(ECR&ECN 物料管控)
     * @param request
     * @return
     */
    @RequestMapping(value = "/hpm/ecr/process/matScrap/status")
    @ResponseBody
    public ResponseData matScrapStatus(HttpServletRequest request, 
    		@RequestParam("ecrno") String ecrno, 
    		@RequestParam("apply") String apply,
    		@RequestParam("flowno") String flowno) {
    	IRequest requestCtx = createRequestContext(request);
    	service.matScrapStatus(requestCtx, ecrno, apply, flowno);
        return new ResponseData();
    }

    
    /**
     *  2.1 启动流程入口(ECR 改善方案)
     * @param request
     * @return
     */
    @RequestMapping(value = "/hpm/ecr/process/solution/start")
    @ResponseBody
    public ResponseData startSolution(HttpServletRequest request) {
    	IRequest requestCtx = createRequestContext(request);
    	EcrMain dto = new EcrMain();
    	dto.setKid(1235L);
    	dto.setEcrno("ECR20200034");
    	dto.setProcessEmployeeCode("wl");
    	service.startSolution(requestCtx, dto);
    	return new ResponseData();
    }
    
    /**
     * 2.2  更新流程状态(ECR 改善方案)
     * @param request
     * @return
     */
    @RequestMapping(value = "/hpm/ecr/process/solution/updateStatus")
    @ResponseBody
    public ResponseData updateSolution(HttpServletRequest request, 
    		@RequestParam("ecrno") String ecrno, 
    		@RequestParam("approveResult") String approveResult,
    		@RequestParam("flowno") String flowno,
    		@RequestParam("skuCost") Double skuCost) {
    	IRequest requestCtx = createRequestContext(request);
    	service.updateSolution(requestCtx, ecrno, approveResult, flowno, skuCost);
        return new ResponseData();
    }
    
    
    /**
     *  3.1 启动流程入口(发布任务工作流)
     * @param request
     * @return
     */
    @RequestMapping(value = "/hpm/ecr/process/task/start")
    @ResponseBody
    public ResponseData startTask(HttpServletRequest request
    		, @RequestParam("processCode") String processCode
    		, @RequestParam("processEmployeeCode") String processEmployeeCode
    		, @RequestParam("id") Long id
    		, @RequestParam("ecrno") String ecrno) {
    	IRequest requestCtx = createRequestContext(request);
    	service.startTask(requestCtx,processCode, processEmployeeCode, id, ecrno);
    	return new ResponseData();
    }
    }