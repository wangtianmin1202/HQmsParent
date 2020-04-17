package com.hand.hqm.hqm_sample_use_after.controllers;

import org.springframework.stereotype.Controller;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hqm.hqm_sample_use_after.dto.SampleUseAfter;
import com.hand.hqm.hqm_sample_use_after.service.ISampleUseAfterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import org.springframework.validation.BindingResult;
import java.util.List;

    @Controller
    public class SampleUseAfterController extends BaseController{

    @Autowired
    private ISampleUseAfterService service;

    /**
     * 页面查询
     * @param dto 查询内容
     * @param page 页码
     * @param pageSize 页大小
     * @param request 请求
     * @return 结果集
     */
    @RequestMapping(value = "/hqm/sample/use/after/query")
    @ResponseBody
    public ResponseData query(SampleUseAfter dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
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
    @RequestMapping(value = "/hqm/sample/use/after/submit")
    @ResponseBody
    public ResponseData update(@RequestBody List<SampleUseAfter> dto, BindingResult result, HttpServletRequest request){
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
    @RequestMapping(value = "/hqm/sample/use/after/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request,@RequestBody List<SampleUseAfter> dto){
        service.batchDelete(dto);
        return new ResponseData();
    }
    
    
    /**
     * 添加
     * @description
     * @author tianmin.wang
     * @date 2019年11月22日 
     * @param dto
     * @param page
     * @param pageSize
     * @param request
     * @return
     */
    @RequestMapping(value = "/hqm/sample/use/after/addone")
    @ResponseBody
    public ResponseData addOne(SampleUseAfter dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
        @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.addOne(requestContext,dto,page,pageSize));
    }
    
    /**
     * 提交
     * @description
     * @author tianmin.wang
     * @date 2019年11月22日 
     * @param dto
     * @param request
     * @return
     */
    @RequestMapping(value = "/hqm/use/after/commit")
    @ResponseBody
    public ResponseData commit(SampleUseAfter dto, HttpServletRequest request) {
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
     * 审核
     * @description
     * @author tianmin.wang
     * @date 2019年11月22日 
     * @param dto
     * @param request
     * @return
     * @throws Throwable
     */
    @RequestMapping(value = "/hqm/use/after/approval")
    @ResponseBody
    public ResponseData approval(SampleUseAfter dto,HttpServletRequest request) throws Throwable {
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
     * 拒绝
     * @description
     * @author tianmin.wang
     * @date 2019年11月22日 
     * @param dto
     * @param request
     * @return
     * @throws Throwable
     */
    @RequestMapping(value = "/hqm/use/after/turndown")
    @ResponseBody
    public ResponseData turnDown(SampleUseAfter dto,HttpServletRequest request) throws Throwable {
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
     * 执行
     * @description
     * @author tianmin.wang
     * @date 2019年11月22日 
     * @param dto
     * @param request
     * @return
     * @throws Throwable
     */
    @RequestMapping(value = "/hqm/use/after/execute")
    @ResponseBody
    public ResponseData execute(SampleUseAfter dto,HttpServletRequest request) throws Throwable {
        IRequest requestContext = createRequestContext(request);
        try {
        	return new ResponseData(service.execute(requestContext,dto));
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
     * @param request
     * @return
     * @throws Throwable
     */
    @RequestMapping(value = "/hqm/use/after/close")
    @ResponseBody
    public ResponseData close(SampleUseAfter dto,HttpServletRequest request) throws Throwable {
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