package com.hand.dimension.hqm_dimension_problem_description.controllers;

import org.springframework.stereotype.Controller;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.util.SystemApiMethod;
import com.hand.hqm.hqm_inspection_attribute.dto.InspectionAttribute;
import com.hand.dimension.hqm_dimension_problem_description.dto.DimensionProblemDescription;
import com.hand.dimension.hqm_dimension_problem_description.service.IDimensionProblemDescriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import org.springframework.validation.BindingResult;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

@Controller
public class DimensionProblemDescriptionController extends BaseController {

	@Autowired
	private IDimensionProblemDescriptionService service;

	/**
	 * 
	 * @description 基础查询
	 * @author tianmin.wang
	 * @date 2019年11月21日 
	 * @param dto
	 * @param page
	 * @param pageSize
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/hqm/8d/problem/description/query")
	@ResponseBody
	public ResponseData query(DimensionProblemDescription dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
			@RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
		IRequest requestContext = createRequestContext(request);
		return new ResponseData(service.select(requestContext, dto, page, pageSize));
	}
	/**
	 * 
	 * @description 物料依据查询 入口
	 * @author tianmin.wang
	 * @date 2019年11月21日 
	 * @param dto
	 * @param page
	 * @param pageSize
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/hqm/8d/problem/description/selectdimensionbyitem")
	@ResponseBody
	public ResponseData selectDimensionByItem(DimensionProblemDescription dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
			@RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
		IRequest requestContext = createRequestContext(request);
		return new ResponseData(service.selectDimensionByItem(dto));
	}
	/**
	 * 
	 * @description 物料组查询
	 * @author tianmin.wang
	 * @date 2019年11月21日 
	 * @param dto
	 * @param page
	 * @param pageSize
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/hqm/8d/problem/description/selectdimensionbyitemgroupcount")
	@ResponseBody
	public ResponseData selectDimensionByItemGroupCount(DimensionProblemDescription dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
			@RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
		IRequest requestContext = createRequestContext(request);
		return new ResponseData(service.selectDimensionByItemGroupCount(dto));
	}
	/**
	 * 
	 * @description 更新提交
	 * @author tianmin.wang
	 * @date 2019年11月21日 
	 * @param dto
	 * @param result
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/hqm/8d/problem/description/submit")
	@ResponseBody
	public ResponseData update(@RequestBody List<DimensionProblemDescription> dto, BindingResult result,
			HttpServletRequest request) {
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
	 * 
	 * @description 删除
	 * @author tianmin.wang
	 * @date 2019年11月21日 
	 * @param request
	 * @param dto
	 * @return
	 */
	@RequestMapping(value = "/hqm/8d/problem/description/remove")
	@ResponseBody
	public ResponseData delete(HttpServletRequest request, @RequestBody List<DimensionProblemDescription> dto) {
		service.batchDelete(dto);
		return new ResponseData();
	}

	/**
	 * 
	 * @description 基础查询
	 * @author tianmin.wang
	 * @date 2019年11月21日 
	 * @param dto
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/hqm/8d/problem/description/select")
	@ResponseBody
	public ResponseData select(DimensionProblemDescription dto, HttpServletRequest request) {
		return new ResponseData(service.reSelect(dto));
	}

	/**
	 * 
	 * @description 保存
	 * @author tianmin.wang
	 * @date 2019年11月21日 
	 * @param dto
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/hqm/8d/problem/description/save")
	@ResponseBody
	public ResponseData save(DimensionProblemDescription dto, HttpServletRequest request) {
		IRequest requestContext = createRequestContext(request);
		ResponseData responseData = new ResponseData();
		try {
			responseData.setRows(service.saveDetail(requestContext, dto));
		}catch(DataRetrievalFailureException e) {
			responseData.setSuccess(false);
			responseData.setMessage("违反唯一性");
			return responseData;
		} catch (Exception e) {
			responseData.setSuccess(false);
			if(e.getMessage().contains("nested")) {
				responseData.setMessage("违反唯一性");
			}else {
				responseData.setMessage(e.getMessage());
			}
	        return responseData;
		}
		return responseData;
	}

	/**
	 * 
	 * @description 步骤提交
	 * @author tianmin.wang
	 * @date 2019年11月21日 
	 * @param dto
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/hqm/8d/problem/description/commit")
	@ResponseBody
	public ResponseData commit(DimensionProblemDescription dto, HttpServletRequest request) {
		IRequest requestContext = createRequestContext(request);
		ResponseData responseData = new ResponseData();
		try {
		responseData = service.commitDetail(requestContext, dto);
		}catch(Exception e) {
			responseData.setSuccess(false);
			responseData.setMessage(e.getMessage());
		}
		return responseData;
	}
	/**
	 * 
	 * @description 文件上传
	 * @author tianmin.wang
	 * @date 2019年11月21日 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/hqm/8d/problem/description/fileupload/upload")
	@ResponseBody
	public ResponseData fileUpload(HttpServletRequest request) {
		ResponseData responseData = new ResponseData();
		IRequest requestCtx = createRequestContext(request);
		responseData = service.fileUpload(requestCtx,request);
        return responseData;
	
	}
}