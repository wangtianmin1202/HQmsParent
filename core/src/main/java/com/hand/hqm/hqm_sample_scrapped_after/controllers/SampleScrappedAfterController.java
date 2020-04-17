package com.hand.hqm.hqm_sample_scrapped_after.controllers;

import org.springframework.stereotype.Controller;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hqm.hqm_sample_scrapped_after.dto.SampleScrappedAfter;
import com.hand.hqm.hqm_sample_scrapped_after.service.ISampleScrappedAfterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import org.springframework.validation.BindingResult;
import java.util.List;

    @Controller
    public class SampleScrappedAfterController extends BaseController{

    @Autowired
    private ISampleScrappedAfterService service;

    /**
     * 页面查询
     * @param dto 查询内容
     * @param page 页码
     * @param pageSize 页大小
     * @param request 请求
     * @return 结果集
     */
    @RequestMapping(value = "/hqm/sample/scrapped/after/query")
    @ResponseBody
    public ResponseData query(SampleScrappedAfter dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
        @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.reSelect(requestContext,dto,page,pageSize));
    }
    /**
     * 提交
     * @param dto 操作数据集
     * @param result 结果参数
     * @param request 请求
     * @return 操作结果
     */
    @RequestMapping(value = "/hqm/sample/scrapped/after/submit")
    @ResponseBody
    public ResponseData update(@RequestBody List<SampleScrappedAfter> dto, BindingResult result, HttpServletRequest request){
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
    @RequestMapping(value = "/hqm/sample/scrapped/after/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request,@RequestBody List<SampleScrappedAfter> dto){
        service.batchDelete(dto);
        return new ResponseData();
    }
    
    @RequestMapping(value = "/hqm/sample/scrapped/after/addone")
    @ResponseBody
    public ResponseData addOne(SampleScrappedAfter dto, HttpServletRequest request) {
    	/*   IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.addOne(requestContext,dto));*/
        
    	/*jrf*/
        ResponseData responseData=new ResponseData();
		IRequest requestContext = createRequestContext(request);
		try {
		responseData = service.addOne(dto,requestContext,request);
		}
		catch(Exception e){
			responseData.setSuccess(false);
			responseData.setMessage(e.getMessage());
		}
		return responseData;
    }
    
    /**
     * 提交
     * @description
     * @author tianmin.wang
     * @date 2019年11月22日 
     * @param dto
     * @param page
     * @param pageSize
     * @param request
     * @return
     */
    @RequestMapping(value = "/hqm/sample/scrapped/after/commit")
    @ResponseBody
    public ResponseData commit(SampleScrappedAfter dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
        @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        try {
        	return new ResponseData(service.commit(requestContext,dto));
        }catch(Exception e) {
        	ResponseData responseData = new ResponseData(false);
        	responseData.setMessage(e.getMessage());
        	return responseData;
        }
    }
    /**
     * 
     * @description
     * @author tianmin.wang
     * @date 2019年11月22日 
     * @param dto
     * @param page
     * @param pageSize
     * @param request
     * @return
     * @throws Exception
     */
    
    @RequestMapping(value = "/hqm/sample/scrapped/after/approval")
    @ResponseBody
    public ResponseData approval(SampleScrappedAfter dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
        @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) throws Exception {
        IRequest requestContext = createRequestContext(request);
        try {
        	return new ResponseData(service.approval(requestContext,dto));
        }catch(Exception e) {
        	ResponseData responseData = new ResponseData(false);
        	responseData.setMessage(e.getMessage());
        	return responseData;
        }
    }
    
    
    /**
     * 回退
     * @description
     * @author tianmin.wang
     * @date 2019年11月22日 
     * @param dto
     * @param page
     * @param pageSize
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/hqm/sample/scrapped/after/turndown")
    @ResponseBody
    public ResponseData turnDown(SampleScrappedAfter dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
        @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) throws Exception {
        IRequest requestContext = createRequestContext(request);
        try {
        	return new ResponseData(service.turnDown(requestContext,dto));
        }catch(Exception e) {
        	ResponseData responseData = new ResponseData(false);
        	responseData.setMessage(e.getMessage());
        	return responseData;
        }
        
    }
    
    
    /**
     * 关闭
     * @description
     * @author tianmin.wang
     * @date 2019年11月22日 
     * @param dto
     * @param page
     * @param pageSize
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/hqm/sample/scrapped/after/close")
    @ResponseBody
    public ResponseData close(SampleScrappedAfter dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
        @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) throws Exception {
        IRequest requestContext = createRequestContext(request);
        try {
        	return new ResponseData(service.close(requestContext,dto));
        }catch(Exception e) {
        	ResponseData responseData = new ResponseData(false);
        	responseData.setMessage(e.getMessage());
        	return responseData;
        }
    }
    }