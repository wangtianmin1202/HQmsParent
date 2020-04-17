package com.hand.spc.ecr_main.controllers;

import org.springframework.stereotype.Controller;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.spc.ecr_main.dto.EcrDesignReview;
import com.hand.spc.ecr_main.service.IEcrDesignReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import org.springframework.validation.BindingResult;
import java.util.List;

    @Controller
    public class EcrDesignReviewController extends BaseController{

    @Autowired
    private IEcrDesignReviewService service;


    @RequestMapping(value = "/hpm/ecr/design/review/query")
    @ResponseBody
    public ResponseData query(EcrDesignReview dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
        @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.select(requestContext,dto,page,pageSize));
    }

    @RequestMapping(value = "/hpm/ecr/design/review/submit")
    @ResponseBody
    public ResponseData update(@RequestBody List<EcrDesignReview> dto, BindingResult result, HttpServletRequest request){
        getValidator().validate(dto, result);
        if (result.hasErrors()) {
        ResponseData responseData = new ResponseData(false);
        responseData.setMessage(getErrorMessage(result, request));
        return responseData;
        }
        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(service.batchUpdate(requestCtx, dto));
    }

    /**
     * 设计评审表-初始化数据
     * @param ecrno
     * @param request
     * @return
     */
    @RequestMapping(value = "/hpm/ecr/design/review/init")
    @ResponseBody
    public ResponseData init(@RequestParam("ecrno") String ecrno, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.initData(requestContext,ecrno));
    }
    
    /**
     * 设计评审表-发起工作流
     * @param ecrno
     * @param request
     * @return
     */
    @RequestMapping(value = "/hpm/ecr/design/review/startProcess")
    @ResponseBody
    public ResponseData startProcess(@RequestParam("ecrno") String ecrno, HttpServletRequest request) {
    	IRequest requestContext = createRequestContext(request);
    	String dutyby = requestContext.getEmployeeCode();
    	service.startProcess(requestContext,ecrno, dutyby);
    	return new ResponseData();
    }
    
    @RequestMapping(value = "/hpm/ecr/design/review/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request,@RequestBody List<EcrDesignReview> dto){
        service.batchDelete(dto);
        return new ResponseData();
    }
    }