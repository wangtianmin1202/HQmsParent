package com.hand.spc.ecr_main.controllers;

import org.springframework.stereotype.Controller;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.spc.ecr_main.dto.EcrItemResult;
import com.hand.spc.ecr_main.dto.EcrMain;
import com.hand.spc.ecr_main.service.IApproveEcrService;
import com.hand.spc.ecr_main.service.IEcrItemResultService;
import com.hand.spc.ecr_main.service.IEcrMainService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import org.springframework.validation.BindingResult;
import java.util.List;

    @Controller
    public class ApproveEcrController extends BaseController{

    @Autowired
    private IApproveEcrService service;
    
    @Autowired
    private IEcrMainService mainService;


    /**
     * 流程中"再提交"按钮的业务功能
     * @param dto
     * @param request
     * @return
     */
    @RequestMapping(value = "/hpm/ecr/item/result/approve/commit")
    @ResponseBody
    public ResponseData query(@RequestBody EcrItemResult dto, HttpServletRequest request,
    		@RequestParam("kid") String kid) {
        IRequest requestContext = createRequestContext(request);
        service.updateState(requestContext,dto,kid);
        return new ResponseData();
    }

    /**
     * 启动流程入口
     * @param request
     * @return
     */
    @RequestMapping(value = "/hpm/ecr/item/result/approve/startProcess")
    @ResponseBody
    public ResponseData startProcess(HttpServletRequest request) {
    	IRequest requestCtx = createRequestContext(request);
    	EcrMain dto = new EcrMain();
    	dto.setKid(1234L);
    	dto.setEcrno("ECR20200034");
    	dto.setProcessEmployeeCode("wl");
    	mainService.startProcess(requestCtx, dto);
        return new ResponseData();
    }

    /**
     * 更新流程状态
     * @param request
     * @return
     */
    @RequestMapping(value = "/hpm/ecr/item/result/approve/updateApplyStatus")
    @ResponseBody
    public ResponseData updateApplyStatus(HttpServletRequest request, 
    		@RequestParam("kid") String kid, 
    		@RequestParam("apply") String apply,
    		@RequestParam("flowno") String flowno) {
    	IRequest requestCtx = createRequestContext(request);
    	service.updateApplyStatus(requestCtx, kid, apply, flowno);
        return new ResponseData();
    }

    }