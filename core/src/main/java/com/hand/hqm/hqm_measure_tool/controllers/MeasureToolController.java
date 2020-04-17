package com.hand.hqm.hqm_measure_tool.controllers;

import org.springframework.stereotype.Controller;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hqm.hqm_measure_tool.dto.MeasureTool;
import com.hand.hqm.hqm_measure_tool.dto.MeasureToolVO;
import com.hand.hqm.hqm_measure_tool.service.IMeasureToolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import org.springframework.validation.BindingResult;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

    @Controller
    public class MeasureToolController extends BaseController{

    @Autowired
    private IMeasureToolService service;

    /**
     * 页面查询
     * @param dto 查询内容
     * @param page 页码
     * @param pageSize 页大小
     * @param request 请求
     * @return 结果集
     */
    @RequestMapping(value = "/hqm/measure/tool/query")
    @ResponseBody
    public ResponseData query(MeasureTool dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
        @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.query(requestContext,dto,page,pageSize));
    }
    @RequestMapping(value = "/hqm/measure/tool/queryByCode")
    @ResponseBody
    public ResponseData queryByCode(MeasureTool dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
    		@RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
    	IRequest requestContext = createRequestContext(request);
    	return new ResponseData(service.queryByCode(requestContext,dto,page,pageSize));
    }
    @RequestMapping(value = "/hqm/measure/tool/queryReport")
    @ResponseBody
    public List<MeasureToolVO> queryReport(MeasureTool dto,HttpServletRequest request) {
    	IRequest requestContext = createRequestContext(request);
    	return service.queryReport(requestContext,dto);
    }
    @RequestMapping(value = "/hqm/measure/tool/queryByToolType")
    @ResponseBody
    public ResponseData queryByToolType(MeasureTool dto,HttpServletRequest request, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
    		@RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize) {
    	IRequest requestContext = createRequestContext(request);
    	return new ResponseData(service.queryByToolType(requestContext,dto,page,pageSize));
    }
    
    
    @RequestMapping(value = "/hqm/measure/tool/queryMsaGridReport")
    @ResponseBody
    public ResponseData queryMsaGridReport(MeasureTool dto,HttpServletRequest request, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
    		@RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize) {
    	IRequest requestContext = createRequestContext(request);
    	return new ResponseData(service.queryMsaGridReport(requestContext,dto,page,pageSize));
    }
    
    @RequestMapping(value = "/hqm/measure/tool/queryMsaReport")
    @ResponseBody
    public List<MeasureToolVO> queryMsaReport(MeasureTool dto,HttpServletRequest request) {
    	IRequest requestContext = createRequestContext(request);
    	return service.queryMsaReport(requestContext,dto);
    }
    
    
    @RequestMapping(value = "/hqm/measure/tool/statisticsquery")
    @ResponseBody
    public ResponseData statisticsquery(MeasureTool dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
        @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.statisticsSelect(requestContext,dto,page,pageSize));
    }
    /**
     * 提交
     * @param dto 操作数据集
     * @param result 结果参数
     * @param request 请求
     * @return 操作结果
     */
    @RequestMapping(value = "/hqm/measure/tool/submit")
    @ResponseBody
    public ResponseData update(@RequestBody List<MeasureTool> dto, BindingResult result, HttpServletRequest request){
        getValidator().validate(dto, result);
        if (result.hasErrors()) {
        ResponseData responseData = new ResponseData(false);
        responseData.setMessage(getErrorMessage(result, request));
        return responseData;
        }
        ResponseData responseData = new ResponseData();
        IRequest requestCtx = createRequestContext(request);
        try {
        	dto.stream().filter(data -> (data.getUpdateFlag() != null && "N".equals(data.getUpdateFlag())))
			.findAny().ifPresent(s -> {throw new RuntimeException("使用部门和管理人必须同时修改");});
        	responseData.setRows(service.batchSave(requestCtx, dto));
        }catch(RuntimeException e) {
        	/*boolean isThrow = true;
        	Throwable cause =e.getCause();
        	if(cause instanceof java.sql.SQLIntegrityConstraintViolationException) {
        		isThrow = false;
        		String errMsg = ((java.sql.SQLIntegrityConstraintViolationException)cause).getMessage();
        		System.out.println("errMsg			" + errMsg);
        	}*/
        	e.printStackTrace();
        	responseData.setMessage(e.getMessage());
        	responseData.setSuccess(false);
        }
        
        return responseData;
    }
    /**
     * 删除
     * @param request
     * @param dto
     * @return
     */
    @RequestMapping(value = "/hqm/measure/tool/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request,@RequestBody List<MeasureTool> dto){
        service.batchDelete(dto);
        return new ResponseData();
    }
    /**
     * 保存
     * @param request 请求
     * @param dto 保存数据
     * @return 数据返回对象
     */
    @RequestMapping(value = "/hqm/measure/tool/saveData")
    @ResponseBody
    public ResponseData saveData(HttpServletRequest request,MeasureTool dto){
    	IRequest requestContext = createRequestContext(request);
        ResponseData responseData = new ResponseData();
        try {
        	service.saveData(requestContext,dto);
        }catch(RuntimeException e) {
        	/*boolean isThrow = true;
        	Throwable cause =e.getCause();
        	if(cause instanceof java.sql.SQLIntegrityConstraintViolationException) {
        		isThrow = false;
        		String errMsg = ((java.sql.SQLIntegrityConstraintViolationException)cause).getMessage();
        		System.out.println("errMsg			" + errMsg);
        	}*/
        	e.printStackTrace();
        	responseData.setMessage(e.getMessage());
        	responseData.setSuccess(false);
        }
        return responseData;
    }
    
    @RequestMapping(value = "/hqm/measure/tool/returnData")
    @ResponseBody
    public ResponseData returnData(HttpServletRequest request,@RequestBody List<MeasureTool> dto){
    	IRequest requestContext = createRequestContext(request);
    	service.returnData(requestContext, dto);
        return new ResponseData();
    }
    /**
     * 报废
     * @param request 请求
     * @param dto 量具信息
     * @return
     */
    @RequestMapping(value = "/hqm/measure/tool/scrap")
    @ResponseBody
    public ResponseData scrap(HttpServletRequest request,@RequestBody List<MeasureTool> dto){
    	IRequest requestContext = createRequestContext(request);
    	service.scrap(requestContext, dto);
        return new ResponseData();
    }
    /**
     * 校验结果
     * @param request 请求
     * @param dto 量具集合
     * @return
     */
    @RequestMapping(value = "/hqm/measure/tool/checkResult")
    @ResponseBody
    public ResponseData checkResult(HttpServletRequest request,@RequestBody List<MeasureTool> dto){
    	IRequest requestContext = createRequestContext(request);
    	service.checkResult(requestContext, dto);
        return new ResponseData();
    }
    
    /**
     * 获取领用部门、领用人
     * @param request 请求
     * @param dto 量具信息
     * @return
     */
    @RequestMapping(value = "/hqm/measure/tool/queryUnit")
    @ResponseBody
    public ResponseData queryUnit(HttpServletRequest request,MeasureTool dto){
    	ResponseData responseDate = new ResponseData();
    	try {
    		responseDate.setRows(service.queryUnit(dto));
    	}catch(Exception e) {
    		responseDate.setSuccess(false);
    		responseDate.setMessage(e.getMessage());
    	}
        return responseDate;
    }
    /**
     *  领用
     * @param request 请求 
     * @param dto 量具信息
     * @return
     */
    @RequestMapping(value = "/hqm/measure/tool/forUse")
    @ResponseBody
    public ResponseData forUse(HttpServletRequest request,@RequestBody List<MeasureTool> dto){
    	ResponseData responseDate = new ResponseData();
    	IRequest requestContext = createRequestContext(request);
    	try {
    		service.forUse(requestContext,dto);
    	}catch(Exception e) {
    		responseDate.setSuccess(false);
    		responseDate.setMessage(e.getMessage());
    	}
        return responseDate;
    }
    /**
     * 编辑
     * @param request 请求
     * @param dto 量具信息
     * @return
     */
    @RequestMapping(value = "/hqm/measure/tool/updateData")
    @ResponseBody
    public ResponseData updateData(HttpServletRequest request,MeasureTool dto){
    	IRequest requestContext = createRequestContext(request);
        ResponseData responseData = new ResponseData();
        try {
        	service.updateData(requestContext,dto);
        }catch(RuntimeException e) {
        	/*boolean isThrow = true;
        	Throwable cause =e.getCause();
        	if(cause instanceof java.sql.SQLIntegrityConstraintViolationException) {
        		isThrow = false;
        		String errMsg = ((java.sql.SQLIntegrityConstraintViolationException)cause).getMessage();
        		System.out.println("errMsg			" + errMsg);
        	}*/
        	e.printStackTrace();
        	responseData.setMessage(e.getMessage());
        	responseData.setSuccess(false);
        }
        return responseData;
    }
    /**
     * 维修
     * @param request 请求
     * @param dto 量具信息
     * @return
     */
    @RequestMapping(value = "/hqm/measure/tool/repair")
    @ResponseBody
    public ResponseData repair(HttpServletRequest request,MeasureTool dto){
    	IRequest requestContext = createRequestContext(request);
        ResponseData responseData = new ResponseData();
        try {
        	service.repair(requestContext,dto);
        }catch(RuntimeException e) {
        	e.printStackTrace();
        	responseData.setMessage(e.getMessage());
        	responseData.setSuccess(false);
        }
        return responseData;
    }
    /**
     * 编辑msa
     * @param request 请求
     * @param dto 量具信息
     * @return
     */
    @RequestMapping(value = "/hqm/measure/tool/saveMsa")
    @ResponseBody
    public ResponseData saveMsa(HttpServletRequest request,MeasureTool dto){
    	IRequest requestContext = createRequestContext(request);
        ResponseData responseData = new ResponseData();
        try {
        	service.saveMsa(requestContext,dto);
        }catch(RuntimeException e) {
        	e.printStackTrace();
        	responseData.setMessage(e.getMessage());
        	responseData.setSuccess(false);
        }
        return responseData;
    }
    /**
     * 格式化日期
     */
    @InitBinder  
    protected void initBinder(WebDataBinder binder) {  
		    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");  
		    dateFormat.setLenient(false);  
		    binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));  
	}
    }