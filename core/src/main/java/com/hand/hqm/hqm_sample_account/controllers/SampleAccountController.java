package com.hand.hqm.hqm_sample_account.controllers;

import org.springframework.stereotype.Controller;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hqm.hqm_pfmea_detail.dto.PfmeaDetail;
import com.hand.hqm.hqm_sample.dto.Sample;
import com.hand.hqm.hqm_sample_account.dto.SampleAccount;
import com.hand.hqm.hqm_sample_account.service.ISampleAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import org.springframework.validation.BindingResult;
import java.util.List;

    @Controller
    public class SampleAccountController extends BaseController{

    @Autowired
    private ISampleAccountService service;

    /**
     * 页面查询
     * @param dto 查询内容
     * @param page 页码
     * @param pageSize 页大小
     * @param request 请求
     * @return 结果集
     */
    @RequestMapping(value = "/hqm/sample/account/query")
    @ResponseBody
    public ResponseData query(SampleAccount dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
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
    @RequestMapping(value = "/hqm/sample/account/submit")
    @ResponseBody
    public ResponseData update(@RequestBody List<SampleAccount> dto, BindingResult result, HttpServletRequest request){
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
    @RequestMapping(value = "/hqm/sample/account/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request,@RequestBody List<SampleAccount> dto){
        service.batchDelete(dto);
        return new ResponseData();
    }
    /**
     * 页面查询
     * @param dto 查询内容
     * @param page 页码
     * @param pageSize 页大小
     * @param request 请求
     * @return 结果集
     */
    @RequestMapping(value = "/hqm/sample/account/select")
    @ResponseBody
    public ResponseData myselect(SampleAccount dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
        @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.myselect(requestContext,dto,page,pageSize));
    }
    
    /**
     * 测试样品页面查询
     * @param dto 查询内容
     * @param page 页码
     * @param pageSize 页大小
     * @param request 请求
     * @return 结果集
     */
    @RequestMapping(value = "/hqm/sample/account/selectforcs")
    @ResponseBody
    public ResponseData myselectforcs(SampleAccount dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
        @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.myselectforcs(requestContext,dto,page,pageSize));
    }
    
    /**
     * 新建
     * @param dto 查询内容
     * @param request 请求
     * @return 结果集
     */
   @RequestMapping(value = "/hqm/sample/account/addnew")
	@ResponseBody
	public ResponseData addNew(SampleAccount dto, HttpServletRequest request) {
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
    * 测试样品新建
    * @param dto 查询内容
    * @param request 请求
    * @return 结果集
    */
   
   @RequestMapping(value = "/hqm/sample/account/addnewforcs")
  	@ResponseBody
  	public ResponseData addNewforcs(SampleAccount dto, HttpServletRequest request) {
  		ResponseData responseData=new ResponseData();
  		IRequest requestContext = createRequestContext(request);
  		try {
  		responseData = service.addNewforcs(dto,requestContext,request);
  		}
  		catch(Exception e){
  			responseData.setSuccess(false);
  			responseData.setMessage(e.getMessage());
  		}
  		return responseData;
  	}
   /**
    * 购买样品新建
    * @param dto 查询内容
    * @param request 请求
    * @return 结果集
    */
   @RequestMapping(value = "/hqm/sample/account/addnewforgm")
 	@ResponseBody
 	public ResponseData addnewforgm(SampleAccount dto, HttpServletRequest request) {
 		ResponseData responseData=new ResponseData();
 		IRequest requestContext = createRequestContext(request);
 		try {
 		responseData = service.addnewforgm(dto,requestContext,request);
 		}
 		catch(Exception e){
 			responseData.setSuccess(false);
 			responseData.setMessage(e.getMessage());
 		}
 		return responseData;
 	}
    }