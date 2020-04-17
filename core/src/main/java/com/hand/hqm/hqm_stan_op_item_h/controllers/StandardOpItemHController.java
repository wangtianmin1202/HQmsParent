package com.hand.hqm.hqm_stan_op_item_h.controllers;

import org.springframework.stereotype.Controller;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hqm.hqm_stan_op_item_h.dto.StandardOpItemH;
import com.hand.hqm.hqm_stan_op_item_h.service.IStandardOpItemHService;
import com.hand.hqm.hqm_standard_op_ins_h.dto.StandardOpInspectionH;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import org.springframework.validation.BindingResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

    @Controller
    public class StandardOpItemHController extends BaseController{

    @Autowired
    private IStandardOpItemHService service;

    /**
     * 页面查询
     * @param dto 查询内容
     * @param page 页码
     * @param pageSize 页大小
     * @param request 请求
     * @return 结果集
     */
    @RequestMapping(value = "/hqm/standard/op/item/h/query")
    @ResponseBody
    public ResponseData query(StandardOpItemH dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
        @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.select(requestContext,dto,page,pageSize));
    }
    
    @RequestMapping(value = "/hqm/standard/op/item/h/save")
    @ResponseBody
    public ResponseData save(StandardOpItemH dto, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.save(requestContext,dto));
    }
    /**
     * 提交
     * @param dto 操作数据集
     * @param result 结果参数
     * @param request 请求
     * @return 操作结果
     */
    @RequestMapping(value = "/hqm/standard/op/item/h/submit")
    @ResponseBody
    public ResponseData update(@RequestBody List<StandardOpItemH> dto, BindingResult result, HttpServletRequest request){
        getValidator().validate(dto, result);
        if (result.hasErrors()) {
        ResponseData responseData = new ResponseData(false);
        responseData.setMessage(getErrorMessage(result, request));
        return responseData;
        }
        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(service.batchUpdate(requestCtx, dto));
    }

    @RequestMapping(value = "/hqm/standard/op/item/h/issue")
    @ResponseBody
    public ResponseData issue(@RequestBody List<StandardOpItemH> dto, HttpServletRequest request){
    	IRequest requestCtx = createRequestContext(request);
		ResponseData responseData = new ResponseData();
		try {
			service.updateStatus(requestCtx, dto);
		} catch (Exception e) {
			responseData.setSuccess(false);
			responseData.setMessage(e.getMessage());
		}
		return responseData;
    }
    /**
     * 删除
     * @param request
     * @param dto
     * @return
     */
    @RequestMapping(value = "/hqm/standard/op/item/h/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request,@RequestBody List<StandardOpItemH> dto){
        service.reBatchDelete(dto);
        return new ResponseData();
    }
    @RequestMapping(value = "/hqm/standard/op/item/h/select")
    @ResponseBody
    public ResponseData select(StandardOpItemH dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                              @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.myselect(requestContext,dto,page,pageSize));
    }
    
    /**
	 * excel数据导入
	 * @author kai.li
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/hqm/standard/op/item/h/excelimport")
	@ResponseBody
    public ResponseData excelImport(HttpServletRequest request) {
    	IRequest requestContext = createRequestContext(request);
		ResponseData responseData = new ResponseData();
		
		MultipartResolver resolver = new CommonsMultipartResolver(request.getSession().getServletContext());
		MultipartHttpServletRequest multipartRequest = resolver.resolveMultipart(request);

		// 获取文件map集合
		Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
		for (Map.Entry<String, MultipartFile> entry : fileMap.entrySet()) {
			MultipartFile forModel = entry.getValue();
			try {
				responseData = service.inputDataFromExcel(request, requestContext, forModel.getInputStream());
			} catch (Exception e1) {
				// TODO 解析异常
				responseData.setMessage(e1.getMessage());
				responseData.setSuccess(false);
				return responseData;
			}
		}
		return responseData;
    }
    
	/**
	 * 
	 * @description 审核
	 * @author kai.li
	 * @date 2020年3月4日
	 * @param dto
	 * @param request
	 * @return
	 */
	@RequestMapping("/hqm/standard/op/item/h/audit")
	@ResponseBody
	public ResponseData audit(@RequestBody List<StandardOpItemH> dto, HttpServletRequest request) {
		IRequest requestCtx = createRequestContext(request);
		ResponseData responseData = new ResponseData();
		try {
			service.audit(requestCtx, dto);
		} catch (Exception e) {
			responseData.setSuccess(false);
			responseData.setMessage(e.getMessage());
			e.printStackTrace();
		}
		return responseData;
	}
	
	/**
	 * 
	 * @description 提交
	 * @author kai.li
	 * @date 2020年3月4日
	 * @param dto
	 * @param request
	 * @return
	 */
	@RequestMapping("/hqm/standard/op/item/h/commit")
	@ResponseBody
	public ResponseData commit(@RequestBody List<StandardOpItemH> dto, HttpServletRequest request) {
		IRequest requestCtx = createRequestContext(request);
		ResponseData responseData = new ResponseData();
		try {
			service.commit(requestCtx, dto);
		} catch (Exception e) {
			responseData.setSuccess(false);
			responseData.setMessage(e.getMessage());
			e.printStackTrace();
		}
		return responseData;
	}
	
	@RequestMapping("/hqm/standard/op/item/h/getHisContentPqc")
	@ResponseBody
	public ResponseData getHisContentPqc(HttpServletRequest request) {
		IRequest requestContext = createRequestContext(request);
		return new ResponseData(service.getHisContentPqc(requestContext, Float.parseFloat(request.getParameter("headId")), request.getParameter("startTime")));
	}
	
    }