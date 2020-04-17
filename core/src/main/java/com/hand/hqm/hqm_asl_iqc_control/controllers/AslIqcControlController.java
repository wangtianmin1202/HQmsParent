package com.hand.hqm.hqm_asl_iqc_control.controllers;

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
import com.hand.hap.system.service.IPromptService;
import com.hand.hap.util.SystemApiMethod;
import com.hand.hqm.hqm_asl_iqc_control.dto.AslIqcControl;
import com.hand.hqm.hqm_asl_iqc_control.service.IAslIqcControlService;

	/*
	 * created by tianmin.wang on 2019/7/9.
	 */
    @Controller
    public class AslIqcControlController extends BaseController{

    @Autowired
    private IAslIqcControlService service;
    
    @Autowired 
    private IPromptService iPromptService;

    /**
     * 页面查询
     * @param dto 查询内容
     * @param page 页码
     * @param pageSize 页大小
     * @param request 请求
     * @return 结果集
     */
    @RequestMapping(value = "/hqm/asl/iqc/control/query")
    @ResponseBody
    public ResponseData query(AslIqcControl dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
        @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.select(requestContext,dto,page,pageSize));
    }

    /*
     * @param null
     * @return
     * @author tianmin.wang
     * @date 11:33 2019/7/9
     * @description  一次更新双表结构，四键关联确定唯一，已修改
     **/
    @RequestMapping(value = "/hqm/asl/iqc/control/submit")
    @ResponseBody
    public ResponseData update(@RequestBody List<AslIqcControl> dto, BindingResult result, HttpServletRequest request){
        getValidator().validate(dto, result);
        IRequest requestContext = createRequestContext(request);
        if (result.hasErrors()) {
        ResponseData responseData = new ResponseData(false);
        responseData.setMessage(getErrorMessage(result, request));
        return responseData;
        }
        
        try {
        	IRequest requestCtx = createRequestContext(request);
        	List<AslIqcControl> reList = service.batchTablesUpdate(requestCtx, dto);
        	return new ResponseData(reList);
        	
        }
        catch(Exception e) {
        	ResponseData responseData = new ResponseData(false);
            if(e.getMessage().contains("nested")) {
            	responseData.setMessage(SystemApiMethod.getPromptDescription(request,iPromptService,"error.hqm_infract_only"));
            }else {
            	responseData.setMessage(e.getMessage());
            }
            return responseData;
        }
		
        
    }
    /**
     * 删除
     * @param request
     * @param dto
     * @return
     */
    @RequestMapping(value = "/hqm/asl/iqc/control/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request,@RequestBody List<AslIqcControl> dto){
    	IRequest requestContext = createRequestContext(request);
        service.batchTablesUpdate(requestContext,dto);//modified by jy 20190920 
        return new ResponseData();
    }
        @RequestMapping(value = "/hqm/asl/iqc/control/select")
        @ResponseBody
        public ResponseData select(AslIqcControl dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                                  @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
            IRequest requestContext = createRequestContext(request);
            return new ResponseData(service.myselect(requestContext,dto,page,pageSize));
        }
    }