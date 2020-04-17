package com.hand.hqm.file_classify.controllers;

import org.springframework.stereotype.Controller;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hqm.file_classify.dto.Classify;
import com.hand.hqm.file_classify.service.IFileClassifyService;
import com.hand.hqm.file_manager.dto.Manager;
import com.hand.hqm.hqm_dfmea_detail.dto.DfmeaDetail;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import org.springframework.validation.BindingResult;
import java.util.List;

    @Controller
    public class FileClassifyController extends BaseController{

    @Autowired
    private IFileClassifyService service;

    /**
     * 默认查询方法
     * @param dto 文件表集合
     * @param request 请求
     * @param page 页码
     * @param pageSize 每页数量
     * @return 请求结果
     */

    @RequestMapping(value = "/file/classify/query")
    @ResponseBody
    public ResponseData query(Classify dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
        @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.select(requestContext,dto,page,pageSize));
    }
    /**
     * 默认更新，新增方法
     * @param dto 文件表集合
     * @param request 请求
     * @return 请求结果
     */
    @RequestMapping(value = "/file/classify/submit")
    @ResponseBody
    public ResponseData update(@RequestBody List<Classify> dto, BindingResult result, HttpServletRequest request){
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
     * 默认删除，新增方法
     * @param dto 文件表集合
     * @param request 请求
     * @return 请求结果
     */
    @RequestMapping(value = "/file/classify/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request,@RequestBody List<Classify> dto){
        service.batchDelete(dto);
        return new ResponseData();
    }
    
    
    /**
	 * @Author han.zhang
	 * @Description 查询附着对象层级维护 树形图数据
	 * @Date 11:39 2019/8/19
	 * @Param [dto, page, pageSize, request]
	 */
	@RequestMapping(value = "/file/classify/data")
	@ResponseBody
	public ResponseData queryTreeData(Classify dto, HttpServletRequest request) {
		IRequest requestContext = createRequestContext(request);
		return new ResponseData(service.queryTreeData(requestContext, dto));
	}
	
	/**
	 * @Author han.zhang
	 * @Description 附着对象更新、新增
	 * @Date 18:10 2019/8/19
	 * @Param [dto, result, request]
	 */
	@RequestMapping(value = "/file/classify/save/or/update")
	@ResponseBody
	public ResponseData updateOrSave(Classify dto, HttpServletRequest request) {
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
	 * @Description 查询第一层级数据
	 * @Date 18:10 2019/8/19
	 * @Param [dto, result, request]
	 */
	 @RequestMapping(value = "/file/manager/selectone")
	    @ResponseBody
	    public ResponseData select(Classify dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
	        @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
	        IRequest requestContext = createRequestContext(request);
	        return new ResponseData(service.myselect1(requestContext,dto,page,pageSize));
	    }
	
	 /**
		 * @Author han.zhang
		 * @Description 查询第二层级数据
		 * @Date 18:10 2019/8/19
		 * @Param [dto, result, request]
		 */
	 @RequestMapping(value = "/file/manager/selecttwo")
	    @ResponseBody
	    public ResponseData selecttwo(Classify dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
	        @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
	        IRequest requestContext = createRequestContext(request);
	        return new ResponseData(service.myselect2(requestContext,dto,page,pageSize));
	    }
	 
	 /**
		 * @Author han.zhang
		 * @Description 查询第三层级数据
		 * @Date 18:10 2019/8/19
		 * @Param [dto, result, request]
		 */
	 @RequestMapping(value = "/file/manager/selectthr")
	    @ResponseBody
	    public ResponseData selectthr(Classify dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
	        @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
	        IRequest requestContext = createRequestContext(request);
	        return new ResponseData(service.myselect3(requestContext,dto,page,pageSize));
	    }
	    
    }