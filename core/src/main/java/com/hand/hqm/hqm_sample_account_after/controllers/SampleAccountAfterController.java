package com.hand.hqm.hqm_sample_account_after.controllers;

import org.springframework.stereotype.Controller;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hqm.hqm_sample_account.dto.SampleAccount;
import com.hand.hqm.hqm_sample_account_after.dto.SampleAccountAfter;
import com.hand.hqm.hqm_sample_account_after.service.ISampleAccountAfterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import org.springframework.validation.BindingResult;
import java.util.List;

    @Controller
    public class SampleAccountAfterController extends BaseController{

    @Autowired
    private ISampleAccountAfterService service;

    /**
     * 页面查询
     * @param dto 查询内容
     * @param page 页码
     * @param pageSize 页大小
     * @param request 请求
     * @return 结果集
     */
    @RequestMapping(value = "/hqm/sample/account/after/query")
    @ResponseBody
    public ResponseData query(SampleAccountAfter dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
        @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.select(requestContext,dto,page,pageSize));
    }
    /**
     * 页面查询
     * @param dto 查询内容
     * @param page 页码
     * @param pageSize 页大小
     * @param request 请求
     * @return 结果集
     */
    @RequestMapping(value = "/hqm/sample/account/after/select")
    @ResponseBody
    public ResponseData select(SampleAccountAfter dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
        @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.reSelect(requestContext,dto,page,pageSize));
    }
    /**
     * 页面查询
     * @param dto 查询报废内容
     * @param page 页码
     * @param pageSize 页大小
     * @param request 请求
     * @return 结果集
     */
    @RequestMapping(value = "/hqm/sample/account/after/selectBf")
    @ResponseBody
    public ResponseData selectBf(SampleAccountAfter dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
    		@RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
    	IRequest requestContext = createRequestContext(request);
    	return new ResponseData(service.selectBf(requestContext,dto,page,pageSize));
    }
    
    
    /**
     * 页面查询
     * @param dto 查询报废内容
     * @param page 页码
     * @param pageSize 页大小
     * @param request 请求
     * @return 结果集
     */
    @RequestMapping(value = "/hqm/sample/account/after/selectLy")
    @ResponseBody
    public ResponseData selectLy(SampleAccountAfter dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
    		@RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
    	IRequest requestContext = createRequestContext(request);
    	return new ResponseData(service.selectLy(requestContext,dto,page,pageSize));
    }
    /**
     * 权限校验
     * @param userId  权限校验
     * @param request 请求
     * @return 结果集
     */
    @RequestMapping(value = "/hqm/sample/account/after/checkUserId")
    @ResponseBody
    public ResponseData checkUserId(Float userId, HttpServletRequest request) {
		IRequest requestContext = createRequestContext(request);
		return service.checkUserId(requestContext,userId);
    }
    /**
     * 提交
     * @param dto 操作数据集
     * @param result 结果参数
     * @param request 请求
     * @return 操作结果
     */
    @RequestMapping(value = "/hqm/sample/account/after/submit")
    @ResponseBody
    public ResponseData update(@RequestBody List<SampleAccountAfter> dto, BindingResult result, HttpServletRequest request){
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
    @RequestMapping(value = "/hqm/sample/account/after/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request,@RequestBody List<SampleAccountAfter> dto){
        service.batchDelete(dto);
        return new ResponseData();
    }
    
    /**
     * 新建
     * @description
     * @author tianmin.wang
     * @date 2019年11月22日 
     * @param dto
     * @param page
     * @param pageSize
     * @param request
     * @return
     */
    @RequestMapping(value = "/hqm/sample/account/after/createsave")
    @ResponseBody
    public ResponseData create(SampleAccountAfter dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
        @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.createSave(requestContext,dto));
    }
    
    /**
     * 保存
     * @description
     * @author tianmin.wang
     * @date 2019年11月22日 
     * @param dto
     * @param page
     * @param pageSize
     * @param request
     * @return
     */
    @RequestMapping(value = "/hqm/sample/account/after/updateData")
    @ResponseBody
    public ResponseData updateData(SampleAccountAfter dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
    		@RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
    	ResponseData responseData=new ResponseData();
    	IRequest requestContext = createRequestContext(request);
    	try {
    		responseData = service.updateData(requestContext,dto);
    		}
    		catch(Exception e){
    			responseData.setSuccess(false);
    			responseData.setMessage(e.getMessage());
    		}
    		return responseData;
    }
    @RequestMapping(value = "/hqm/sample/account/after/savefx")
	@ResponseBody
	public ResponseData saveFx(SampleAccountAfter dto, HttpServletRequest request) {
		ResponseData responseData=new ResponseData();
		IRequest requestContext = createRequestContext(request);
		try {
		responseData = service.saveFx(dto,requestContext,request);
		}
		catch(Exception e){
			responseData.setSuccess(false);
			responseData.setMessage(e.getMessage());
		}
		return responseData;
	}
    }