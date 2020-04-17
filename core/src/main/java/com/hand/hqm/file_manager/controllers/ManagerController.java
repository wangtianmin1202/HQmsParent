package com.hand.hqm.file_manager.controllers;

import org.springframework.stereotype.Controller;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hqm.file_classify.dto.Classify;
import com.hand.hqm.file_manager.dto.Manager;
import com.hand.hqm.file_manager.service.IManagerService;
import com.hand.hqm.file_upload.dto.FileUpload;
import com.hand.hqm.hqm_dfmea_detail.dto.DfmeaDetail;
import com.hand.hqm.hqm_sample.dto.Sample;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import org.springframework.validation.BindingResult;
import java.util.List;

    @Controller
    public class ManagerController extends BaseController{

    @Autowired
    private IManagerService service;

    /**
     * 页面查询
     * @param dto 查询内容
     * @param page 页码
     * @param pageSize 页大小
     * @param request 请求
     * @return 结果集
     */
    @RequestMapping(value = "/file/manager/query")
    @ResponseBody
    public ResponseData query(Manager dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
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
    @RequestMapping(value = "/file/manager/submit")
    @ResponseBody
    public ResponseData update(@RequestBody List<Manager> dto, BindingResult result, HttpServletRequest request){
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
    @RequestMapping(value = "/file/manager/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request,@RequestBody List<Manager> dto){
        service.batchDelete(dto);
        return new ResponseData(dto);
    }
    
    @RequestMapping(value = "/file/manager/addnew")
	@ResponseBody
	public ResponseData addNew(Manager dto, HttpServletRequest request) {
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
    
    @RequestMapping(value = "/file/manager/select")
    @ResponseBody
    public ResponseData select(Manager dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
        @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.myselect(requestContext,dto,page,pageSize));
    }
    
    @RequestMapping(value = "/file/manager/fileupload/upload")
	@ResponseBody
	public ResponseData fileUpload(HttpServletRequest request) {
		
		ResponseData responseData = new ResponseData();
		
		IRequest requestCtx = createRequestContext(request);
		responseData = service.fileUpload(requestCtx,request);
    	
        return responseData;
	
	}
    
    /**
     * 
     * @Description:流程单据上传文件，只上传到服务器，不在前台展示
     * @param request
     * @return
     */
    @RequestMapping(value = "/file/manager/activity/upload")
	@ResponseBody
	public ResponseData activityUpload(HttpServletRequest request) {
		IRequest requestCtx = createRequestContext(request);
        return service.activityUpload(requestCtx,request);
	}
    
    /**
     * 
     * @Description:查询文件上传审批里面上传了的文件
     * @param dto
     * @param page
     * @param pageSize
     * @param request
     * @return
     */
    @RequestMapping(value = "/file/manager/queryFileUpload")
    @ResponseBody
    public ResponseData queryFileUpload(FileUpload dto, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.queryFileUpload(requestContext,dto));
    }
    }