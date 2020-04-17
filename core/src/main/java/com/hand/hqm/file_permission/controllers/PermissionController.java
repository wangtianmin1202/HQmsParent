package com.hand.hqm.file_permission.controllers;

import org.springframework.stereotype.Controller;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hqm.file_classify.dto.Classify;
import com.hand.hqm.file_permission.dto.Permission;
import com.hand.hqm.file_permission.service.IPermissionService;
import com.hand.hqm.hqm_fmea.dto.Fmea;
import com.hand.hqm.hqm_nonconforming_order.dto.NonconformingOrder;
import com.hand.hqm.hqm_qua_ins_time_l.dto.QuaInsTimeL;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import org.springframework.validation.BindingResult;
import java.util.List;

    @Controller
    public class PermissionController extends BaseController{

    @Autowired
    private IPermissionService service;

    /**
     * 页面查询
     * @param dto 查询内容
     * @param page 页码
     * @param pageSize 页大小
     * @param request 请求
     * @return 结果集
     */
    @RequestMapping(value = "/file/permission/query")
    @ResponseBody
    public ResponseData query(Permission dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
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
    @RequestMapping(value = "/file/permission/submit")
    @ResponseBody
    public ResponseData update(@RequestBody List<Permission> dto, BindingResult result, HttpServletRequest request){
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
    @RequestMapping(value = "/file/permission/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request,@RequestBody List<Permission> dto){
        service.batchDelete(dto);
        return new ResponseData();
    }
    
    
    @RequestMapping(value = "/file/permission/selecthead")
    @ResponseBody
    public ResponseData selecthead(Permission dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                              @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.myselect(requestContext,dto,page,pageSize));
    }
    
    @RequestMapping(value = "/file/permission/selectline")
    @ResponseBody
    public ResponseData selectline(Permission dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                              @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.myselectline(requestContext,dto,page,pageSize));
    }
    
    @RequestMapping(value = "/file/permission/check")
	@ResponseBody
	public ResponseData updateOrSave(Permission dto, HttpServletRequest request) {
		ResponseData responseData = new ResponseData();
		IRequest requestCtx = createRequestContext(request);
		try {
			responseData = service.check(requestCtx, dto);
		} catch (Exception e) {
			e.printStackTrace();
			responseData.setMessage(e.toString());
			responseData.setSuccess(false);
		}
		return responseData;
	}
    @RequestMapping(value = "/file/permission/addnew")
    @ResponseBody
    public ResponseData addNewInspection(HttpServletRequest request,@RequestBody List<Permission> dto){
    	IRequest requestContext = createRequestContext(request);
    	ResponseData responseData = new ResponseData();
    	try {
    		responseData =  service.addNewInspection(requestContext, dto);
    	}catch(Exception e) {
    		responseData.setSuccess(false);
   			responseData.setMessage(e.getMessage());
    	}
    	
        return responseData;
    }
   /* @RequestMapping(value = "/file/permission/addnew")
   	@ResponseBody
   	public ResponseData addNewInspection(Permission dto, HttpServletRequest request) {
   		ResponseData responseData=new ResponseData();
   		IRequest requestContext = createRequestContext(request);
   		try {
   		responseData = service.addNewInspection(dto,requestContext,request);
   		}
   		catch(Exception e){
   			responseData.setSuccess(false);
   			responseData.setMessage(e.getMessage());
   		}
   		return responseData;
   	}*/
    
    }