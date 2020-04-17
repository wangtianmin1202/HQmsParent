package com.hand.hqm.hqm_sample_scrapped.controllers;

import org.springframework.stereotype.Controller;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hqm.hqm_sample_account.dto.SampleAccount;
import com.hand.hqm.hqm_sample_scrapped.dto.SampleScrapped;
import com.hand.hqm.hqm_sample_scrapped.service.ISampleScrappedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import org.springframework.validation.BindingResult;
import java.util.List;

    @Controller
    public class SampleScrappedController extends BaseController{

    @Autowired
    private ISampleScrappedService service;

    /**
     * 页面查询
     * @param dto 查询内容
     * @param page 页码
     * @param pageSize 页大小
     * @param request 请求
     * @return 结果集
     */
    @RequestMapping(value = "/hqm/sample/scrapped/query")
    @ResponseBody
    public ResponseData query(SampleScrapped dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
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
    @RequestMapping(value = "/hqm/sample/scrapped/submit")
    @ResponseBody
    public ResponseData update(@RequestBody List<SampleScrapped> dto, BindingResult result, HttpServletRequest request){
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
    @RequestMapping(value = "/hqm/sample/scrapped/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request,@RequestBody List<SampleScrapped> dto){
        service.batchDelete(dto);
        return new ResponseData();
    }
    /**
     * 新建
     * @param dto 操作数据集
     * @param result 结果参数
     * @param request 请求
     * @return 操作结果
     */
    @RequestMapping(value = "/hqm/sample/scrapped/addnew")
	@ResponseBody
	public ResponseData addNew(SampleScrapped dto, HttpServletRequest request) {
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
     * @param dto 操作数据集
     * @param result 结果参数
     * @param request 请求
     * @return 操作结果
     */
    @RequestMapping(value = "/hqm/sample/scrapped/addnewforcs")
	@ResponseBody
	public ResponseData addnewforcs(SampleScrapped dto, HttpServletRequest request) {
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
     * 提交
     * @param dto 操作数据集
     * @param result 结果参数
     * @param request 请求
     * @return 操作结果
     */
    @RequestMapping(value = "/hqm/sample/scrapped/comfirm")//提交 转化为待审核
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
     * 页面查询
     * @param dto 查询内容
     * @param page 页码
     * @param pageSize 页大小
     * @param request 请求
     * @return 结果集
     */
    @RequestMapping(value = "/hqm/sample/scrapped/select")
    @ResponseBody
    public ResponseData myselect(SampleScrapped dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
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
    @RequestMapping(value = "/hqm/sample/scrapped/selectforcs")
    @ResponseBody
    public ResponseData selectforcs(SampleScrapped dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
        @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.myselectforcs(requestContext,dto,page,pageSize));
    }
   
    
    }