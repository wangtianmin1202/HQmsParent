package com.hand.hqm.hqm_pfmea_detail.controllers;

import org.springframework.stereotype.Controller;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hqm.hqm_dfmea_detail.dto.DfmeaDetail;
import com.hand.hqm.hqm_pfmea_detail.dto.PfmeaDetail;
import com.hand.hqm.hqm_pfmea_detail.service.IPfmeaDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import org.springframework.validation.BindingResult;
import java.util.List;

    @Controller
    public class PfmeaDetailController extends BaseController{

    @Autowired
    private IPfmeaDetailService service;

    /**
     * 页面查询
     * @param dto 查询内容
     * @param page 页码
     * @param pageSize 页大小
     * @param request 请求
     * @return 结果集
     */
    @RequestMapping(value = "/hqm/pfmea/detail/query")
    @ResponseBody
    public ResponseData query(PfmeaDetail dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
        @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.select(requestContext,dto,page,pageSize));
    }
    /**
     * 页面查询
     * @param dto 操作数据集
     * @param result 结果参数
     * @param request 请求
     * @return 操作结果
     */
    @RequestMapping(value = "/hqm/pfmea/detail/query/condition")
    @ResponseBody
    public ResponseData queryCondition(PfmeaDetail dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
    		@RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
    	IRequest requestContext = createRequestContext(request);
    	return new ResponseData(service.selectCondition(requestContext,dto,page,pageSize));
    }
    /**
     * 提交
     * @param dto 操作数据集
     * @param result 结果参数
     * @param request 请求
     * @return 操作结果
     */
    @RequestMapping(value = "/hqm/pfmea/detail/submit")
    @ResponseBody
    public ResponseData update(@RequestBody List<PfmeaDetail> dto, BindingResult result, HttpServletRequest request){
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
     * 删除
     * @param request
     * @param dto
     * @return
     */
    @RequestMapping(value = "/hqm/pfmea/detail/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request,@RequestBody List<PfmeaDetail> dto){
        service.batchDelete(dto);
        return new ResponseData();
    }
    
    /**
	 * @Author han.zhang
	 * @Description 查询附着对象层级维护 树形图数据
	 * @Date 11:39 2019/8/19
	 * @Param [dto, page, pageSize, request]
	 */
	@RequestMapping(value = "/hqm/pfmea/query/tree/data")
	@ResponseBody
	public ResponseData queryTreeData(PfmeaDetail dto, HttpServletRequest request) {
		IRequest requestContext = createRequestContext(request);
		return new ResponseData(service.queryTreeData(requestContext, dto));
	}
	
	/**
	 * @Author han.zhang
	 * @Description 打印
	 * @Date 18:10 2019/8/19
	 * @Param [dto, result, request]
	 */
	 @RequestMapping(value = "/hqm/pfmea/query/print")
	    @ResponseBody
	    public ResponseData queryprintData(PfmeaDetail dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
	        @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
	        IRequest requestContext = createRequestContext(request);
	        return new ResponseData(service.queryprintData(requestContext,dto,page,pageSize));
	    }

	 /**
		 * @Author han.zhang
		 * @Description 查询打印投标信息
		 * @Date 18:10 2019/8/19
		 * @Param [dto, result, request]
		 */
	 @RequestMapping(value = "/hqm/pfmea/print/header/query")
	    @ResponseBody
	    public PfmeaDetail query_header(@RequestParam Float kid,HttpServletRequest request) {
	        return  service.queryHeaderData(kid);//select(requestContext,dto,page,pageSize)
	    }
	/**
	 * @Author han.zhang
	 * @Description 附着对象更新、新增
	 * @Date 18:10 2019/8/19
	 * @Param [dto, result, request]
	 */
	@RequestMapping(value = "/hqm/pfmea/save/or/update")
	@ResponseBody
	public ResponseData updateOrSave(PfmeaDetail dto, HttpServletRequest request) {
		ResponseData responseData = new ResponseData();
		IRequest requestCtx = createRequestContext(request);
		try {
			responseData = service.updateOrAdd(requestCtx, dto);
		} catch (Exception e) {
			e.printStackTrace();
			responseData.setMessage(e.toString());
			responseData.setSuccess(false);
		}
		return responseData;
	}

	/**
	 * @Author han.zhang
	 * @Description 根据主键删除附着对象
	 * @Date 10:51 2019/8/20
	 * @Param [dto, request]
	 */
	@RequestMapping(value = "/hqm/pfmea/delete/row")
	@ResponseBody
	public ResponseData deleteRow(PfmeaDetail dto, HttpServletRequest request) {
		ResponseData responseData = new ResponseData(true);
		try {
			service.deleteRow(dto);
		} catch (Exception e) {
			e.printStackTrace();
			responseData.setMessage(e.getMessage());
			responseData.setSuccess(false);
		}
		return responseData;
	}
	/**
	 * @Author han.zhang
	 * @Description 提交
	 * @Date 10:51 2019/8/20
	 * @Param [dto, request]
	 */
	 @RequestMapping(value = "/hqm/pfmea/confirm")
	    @ResponseBody
	    public ResponseData confirm(HttpServletRequest request,@RequestParam("list") List<Float> dto){
	    	ResponseData responseData = new ResponseData();
	    	IRequest requestCtx = createRequestContext(request);
	    	try{
	    		responseData= service.confirm(requestCtx, dto);
	        	
	    	}catch(Exception e) {
	    		e.printStackTrace();
	    		responseData.setSuccess(false);
	    		responseData.setMessage("提交失败");
	    	}
	        return responseData;
	    }
	 /**
		 * @Author han.zhang
		 * @Description 获取信息
		 * @Date 10:51 2019/8/20
		 * @Param [dto, request]
		 */
	   @RequestMapping(value = "/hqm/pfmea/getmessage")
	    @ResponseBody
	    public ResponseData getdata(HttpServletRequest request,@RequestParam("list") List<Long> dto){
	    	ResponseData responseData = new ResponseData();
	    	IRequest requestCtx = createRequestContext(request);
	    	try{
	    		responseData= service.getdata(requestCtx, dto);
	        	
	    	}catch(Exception e) {
	    		e.printStackTrace();
	    		responseData.setSuccess(false);
	    		responseData.setMessage("提交失败");
	    	}
	        return responseData;
	    }
	   /**
		 * @Author han.zhang
		 * @Description pfmea 明细查询
		 * @Date 10:51 2019/8/20
		 * @Param [dto, request，page,pageSize]
		 */
	    @RequestMapping(value = "/hqm/pfmea/detail/select")
	    @ResponseBody
	    public ResponseData select(PfmeaDetail dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
	        @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
	        IRequest requestContext = createRequestContext(request);
	        return new ResponseData(service.myselect(requestContext,dto,page,pageSize));
	    }
    
    
    
    }