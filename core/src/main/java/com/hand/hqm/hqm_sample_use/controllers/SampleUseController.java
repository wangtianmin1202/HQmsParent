package com.hand.hqm.hqm_sample_use.controllers;

import org.springframework.stereotype.Controller;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hqm.hqm_sample_account.dto.SampleAccount;
import com.hand.hqm.hqm_sample_scrapped.dto.SampleScrapped;
import com.hand.hqm.hqm_sample_use.dto.SampleUse;
import com.hand.hqm.hqm_sample_use.service.ISampleUseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import org.springframework.validation.BindingResult;
import java.util.List;

    @Controller
    public class SampleUseController extends BaseController{

    @Autowired
    private ISampleUseService service;

    /**
     * 页面查询
     * @param dto 查询内容
     * @param page 页码
     * @param pageSize 页大小
     * @param request 请求
     * @return 结果集
     */
    @RequestMapping(value = "/hqm/sample/use/query")
    @ResponseBody
    public ResponseData query(SampleUse dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
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
    @RequestMapping(value = "/hqm/sample/use/submit")
    @ResponseBody
    public ResponseData update(@RequestBody List<SampleUse> dto, BindingResult result, HttpServletRequest request){
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
    @RequestMapping(value = "/hqm/sample/use/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request,@RequestBody List<SampleUse> dto){
        service.batchDelete(dto);
        return new ResponseData();
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
    @RequestMapping(value = "/hqm/sample/use/select")
    @ResponseBody
    public ResponseData myselect(SampleUse dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
        @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.myselect(requestContext,dto,page,pageSize));
    }
    /**
     * 样品历史查询
     * @param dto 查询内容
     * @param page 页码
     * @param pageSize 页大小
     * @param request 请求
     * @return 结果集
     */
    @RequestMapping(value = "/hqm/sample/use/select_his")
    @ResponseBody
    public ResponseData select_his(SampleUse dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
        @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.select_his(requestContext,dto,page,pageSize));
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
    @RequestMapping(value = "/hqm/sample/use/comfirm")//提交 转化为待审核
    @ResponseBody
    public ResponseData comfirm(HttpServletRequest request,@RequestParam("list") List<String> dto){
    	ResponseData responseData = new ResponseData();
    	try{
    		IRequest requestContext = createRequestContext(request);
        	responseData=service.comfirm(requestContext, dto);
    	}catch(Exception e) {
    		e.printStackTrace();
    		responseData.setSuccess(false);
    		responseData.setMessage("确认失败");
    	}
        return responseData;
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
    @RequestMapping(value = "/hqm/sample/account/forUse")//领用
    @ResponseBody
    public ResponseData forUse(HttpServletRequest request,@RequestBody List<SampleAccount> dto){
    	ResponseData responseData = new ResponseData();
    	try{
    		IRequest requestContext = createRequestContext(request);
    		responseData=service.forUse(requestContext, dto);
    	}catch(Exception e) {
    		e.printStackTrace();
    		responseData.setSuccess(false);
    		responseData.setMessage("领用失败");
    	}
    	return responseData;
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
    @RequestMapping(value = "/hqm/sample/account/scrap")//报废
    @ResponseBody
    public ResponseData scrap(HttpServletRequest request,@RequestBody List<SampleAccount> dto){
    	ResponseData responseData = new ResponseData();
    	try{
    		IRequest requestContext = createRequestContext(request);
    		responseData=service.scrap(requestContext, dto);
    	}catch(Exception e) {
    		e.printStackTrace();
    		responseData.setSuccess(false);
    		responseData.setMessage("报废失败");
    	}
    	return responseData;
    }
    /**
     * 新建测试样品
     * @param dto 操作数据集
     * @param result 结果参数
     * @param request 请求
     * @return 操作结果
     */
    @RequestMapping(value = "/hqm/sample/use/addnewforcs")
   	@ResponseBody
   	public ResponseData addnewforcs(SampleUse dto, HttpServletRequest request) {
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
    
    }