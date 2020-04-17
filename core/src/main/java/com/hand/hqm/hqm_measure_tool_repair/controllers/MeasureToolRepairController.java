package com.hand.hqm.hqm_measure_tool_repair.controllers;

import org.springframework.stereotype.Controller;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.code.rule.exception.CodeRuleException;
import com.hand.hap.code.rule.service.ISysCodeRuleProcessService;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hqm.hqm_measure_tool_repair.dto.MeasureToolRepair;
import com.hand.hqm.hqm_measure_tool_repair.service.IMeasureToolRepairService;

import net.bytebuddy.asm.Advice.This;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import org.springframework.validation.BindingResult;
import java.util.List;
	
    @Controller
    public class MeasureToolRepairController extends BaseController{

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    
    @Autowired
    private IMeasureToolRepairService service;

    @Autowired
    ISysCodeRuleProcessService codeRuleProcessService;

    @RequestMapping(value = "/hqm/measure/tool/repair/query")
    @ResponseBody
    public ResponseData query(MeasureToolRepair dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
        @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.select(requestContext,dto,page,pageSize));
    }

    @RequestMapping(value = "/hqm/measure/tool/repair/submit")
    @ResponseBody
    public ResponseData update(@RequestBody List<MeasureToolRepair> dto, BindingResult result, HttpServletRequest request){
        getValidator().validate(dto, result);
        if (result.hasErrors()) {
        ResponseData responseData = new ResponseData(false);
        responseData.setMessage(getErrorMessage(result, request));
        return responseData;
        }
        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(service.batchUpdate(requestCtx, dto));
    }

    @RequestMapping(value = "/hqm/measure/tool/repair/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request,@RequestBody List<MeasureToolRepair> dto){
        service.batchDelete(dto);
        return new ResponseData();
    }
    
    /**
     * 
     * @Description:量具维修单页面主查询
     * @param dto
     * @param page
     * @param pageSize
     * @param request
     * @return
     */
    @RequestMapping(value = "/hqm/measure/tool/repair/queryByCondition")
    @ResponseBody
    public ResponseData queryByCondition(MeasureToolRepair dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
        @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.queryByCondition(requestContext,dto,page,pageSize));
    }
    
    /**
     * 
     * @Description:量具维修单新增页面，保存
     * @param dto
     * @param result
     * @param request
     * @return
     */
    @RequestMapping(value = "/hqm/measure/tool/repair/add")
    @ResponseBody
    public ResponseData add(@RequestBody MeasureToolRepair dto, BindingResult result, HttpServletRequest request){
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
     * @Description:根据编码规则，生成所需要的编码
     * @param code
     * @param request
     * @return
     */
    @RequestMapping("/hqm/measure/tool/repair/getCode")
    @ResponseBody
    public String getCode(@RequestParam String code, HttpServletRequest request) {
    	try {
    		return codeRuleProcessService.getRuleCode(code);
    	} catch (CodeRuleException e) {
			logger.error(">>>获取编码失败:" + e.getMessage());
		}
    	return null;
    }
    
    /**
     * 
     * @Description:确认
     * @param dto
     * @param result
     * @param request
     * @return
     */
    @RequestMapping(value = "/hqm/measure/tool/repair/confirmRepair")
    @ResponseBody
    public ResponseData confirmRepair(MeasureToolRepair dto, BindingResult result, HttpServletRequest request){
        getValidator().validate(dto, result);
        if (result.hasErrors()) {
        ResponseData responseData = new ResponseData(false);
        responseData.setMessage(getErrorMessage(result, request));
        return responseData;
        }
        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(service.confirmRepair(requestCtx, dto));
    }
    }