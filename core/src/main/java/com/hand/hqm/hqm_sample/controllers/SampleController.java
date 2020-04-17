package com.hand.hqm.hqm_sample.controllers;

import org.springframework.stereotype.Controller;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hqm.hqm_nonconforming_order.dto.NonconformingOrder;
import com.hand.hqm.hqm_sam_use_order_l.dto.SamUseOrderL;
import com.hand.hqm.hqm_sample.dto.Sample;
import com.hand.hqm.hqm_sample.service.ISampleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import org.springframework.validation.BindingResult;
import java.util.List;

    @Controller
    public class SampleController extends BaseController{

    @Autowired
    private ISampleService service;

    /**
     * 页面查询
     * @param dto 查询内容
     * @param page 页码
     * @param pageSize 页大小
     * @param request 请求
     * @return 结果集
     */
    @RequestMapping(value = "/hqm/sample/query")
    @ResponseBody
    public ResponseData query(Sample dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
        @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.select(requestContext,dto,page,pageSize));
    }
    /**
     * 提交
     * @param dto 操作数据集
     * @param result 结果参数
     * @param request 请求
     * @return 操作结果
     */
    @RequestMapping(value = "/hqm/sample/submit")
    @ResponseBody
    public ResponseData update(@RequestBody List<Sample> dto, BindingResult result, HttpServletRequest request){
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
    @RequestMapping(value = "/hqm/sample/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request,@RequestBody List<Sample> dto){
        service.batchDelete(dto);
        return new ResponseData();
    }
    
    
    /**
     * 新建
     * @description
     * @author tianmin.wang
     * @date 2019年11月22日 
     * @param dto
     * @param request
     * @return
     */
    @RequestMapping(value = "/hqm/sample/addnew")
	@ResponseBody
	public ResponseData addNew(Sample dto, HttpServletRequest request) {
		ResponseData responseData=new ResponseData();
		IRequest requestContext = createRequestContext(request);
		try {
		responseData = service.addNew(dto,requestContext,request);
		}
		catch(Exception e){
			responseData.setSuccess(false);
			responseData.setMessage(e.getMessage());
		}
		return responseData;
	}
    
    /**
     * 基础查询
     * @description
     * @author tianmin.wang
     * @date 2019年11月22日 
     * @param dto
     * @param page
     * @param pageSize
     * @param request
     * @return
     */
    @RequestMapping(value = "/hqm/sample/select")
    @ResponseBody
    public ResponseData select(Sample dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                              @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.myselect(requestContext,dto,page,pageSize));
    }
    
    
    /**
     * 提交
     * @description
     * @author tianmin.wang
     * @date 2019年11月22日 
     * @param request
     * @param dto
     * @return
     */
    @RequestMapping(value = "/hqm/sample/confirm")
    @ResponseBody
    public ResponseData confirm(HttpServletRequest request,@RequestParam("list") List<Float> dto){
    	ResponseData responseData = new ResponseData();
    	try{
    		IRequest requestContext = createRequestContext(request);
        	responseData.setRows(service.confirm(requestContext, dto));
        	responseData.setMessage("确认成功");
    	}catch(Exception e) {
    		e.printStackTrace();
    		responseData.setSuccess(false);
    		responseData.setMessage("确认失败");
    	}
        return responseData;
    }
    
    
    /**
     * 确认操作
     * @description
     * @author tianmin.wang
     * @date 2019年11月22日 
     * @param request
     * @param dto
     * @return
     */
    @RequestMapping(value = "/hqm/sample/deal")
    @ResponseBody
    public ResponseData deal(HttpServletRequest request,@RequestParam("list") List<String> dto){
    	ResponseData responseData = new ResponseData();
    	try{
    		IRequest requestContext = createRequestContext(request);
        	responseData.setRows(service.deal(requestContext, dto));
        	responseData.setMessage("确认成功");
    	}catch(Exception e) {
    		e.printStackTrace();
    		responseData.setSuccess(false);
    		responseData.setMessage("确认失败");
    	}
        return responseData;
    }
    
    
    /**
     * //样品领用执行操作
     * @description
     * @author tianmin.wang
     * @date 2019年11月22日 
     * @param request
     * @param dto
     * @return
     */
    @RequestMapping(value = "/hqm/sample/useDeal")
    @ResponseBody
    public ResponseData useDeal(HttpServletRequest request,@RequestBody List<Sample> dto){
    	ResponseData responseData = new ResponseData();
    	try{
    		IRequest requestContext = createRequestContext(request);
        	responseData.setRows(service.useDeal(requestContext, dto));
        	responseData.setMessage("执行成功");
        	responseData.setSuccess(true);
    	}catch(Exception e) {
    		e.printStackTrace();
    		responseData.setSuccess(false);
    		responseData.setMessage("执行失败");
    	}
        return responseData;
    }
    @RequestMapping(value = "/hqm/sample/selectforuse")
    @ResponseBody
    public ResponseData selectforuse(Sample dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                              @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.selectforuse(requestContext,dto,page,pageSize));
    }
    }