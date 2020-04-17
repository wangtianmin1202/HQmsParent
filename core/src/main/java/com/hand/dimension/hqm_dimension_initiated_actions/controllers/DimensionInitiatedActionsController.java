package com.hand.dimension.hqm_dimension_initiated_actions.controllers;

import org.springframework.stereotype.Controller;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.dimension.hqm_dimension_improving_actions.dto.DimensionImprovingActions;
import com.hand.dimension.hqm_dimension_initiated_actions.dto.DimensionInitiatedActions;
import com.hand.dimension.hqm_dimension_initiated_actions.service.IDimensionInitiatedActionsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import org.springframework.validation.BindingResult;

import java.io.IOException;
import java.util.List;

@Controller
public class DimensionInitiatedActionsController extends BaseController {

	@Autowired
	private IDimensionInitiatedActionsService service;

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
	@RequestMapping(value = "/hqm/8d/initiated/actions/query")
	@ResponseBody
	public ResponseData query(DimensionInitiatedActions dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
			@RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
		IRequest requestContext = createRequestContext(request);
		return new ResponseData(service.select(requestContext, dto, page, pageSize));
	}

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
	@RequestMapping(value = "/hqm/8d/initiated/actions/select")
	@ResponseBody
	public ResponseData select(DimensionInitiatedActions dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
			@RequestParam(defaultValue = "100") int pageSize, HttpServletRequest request) {
		IRequest requestContext = createRequestContext(request);
		return new ResponseData(service.reSelect(requestContext, dto, page, pageSize));
	}

	/**
	 * 
	 * @description 数据更新提交
	 * @author tianmin.wang
	 * @date 2019年11月21日 
	 * @param dto
	 * @param result
	 * @param request
	 * @return
	 * @throws Throwable
	 */
	@RequestMapping(value = "/hqm/8d/initiated/actions/submit")
	@ResponseBody
	public ResponseData update(@RequestBody List<DimensionInitiatedActions> dto, BindingResult result,
			HttpServletRequest request) throws Throwable {
		getValidator().validate(dto, result);
		if (result.hasErrors()) {
			ResponseData responseData = new ResponseData(false);
			responseData.setMessage(getErrorMessage(result, request));
			return responseData;
		}
		IRequest requestCtx = createRequestContext(request);
		return new ResponseData(service.reBatchUpdate(requestCtx, dto));
	}

	/**
	 * 
	 * @description 数据删除
	 * @author tianmin.wang
	 * @date 2019年11月21日 
	 * @param request
	 * @param dto
	 * @return
	 */
	@RequestMapping(value = "/hqm/8d/initiated/actions/remove")
	@ResponseBody
	public ResponseData delete(HttpServletRequest request, @RequestBody List<DimensionInitiatedActions> dto) {
		service.batchDelete(dto);
		return new ResponseData();
	}

	/**
	 * 
	 * @description 文件上传
	 * @author tianmin.wang
	 * @date 2019年11月21日 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/hqm/8d/initiated/actions/fileupload/upload")
	@ResponseBody
	public ResponseData fileUpload(HttpServletRequest request) {

		ResponseData responseData = new ResponseData();

		IRequest requestCtx = createRequestContext(request);
		try {
			responseData = service.fileUpload(requestCtx, request);
		} catch (IllegalStateException | IOException e) {
			// TODO Auto-generated catch block
			responseData.setMessage(e.getMessage());
			responseData.setSuccess(false);
		}

		return responseData;

	}

	/**
	 * 
	 * @description 请求入口 数据提交
	 * @author tianmin.wang
	 * @date 2019年11月21日 
	 * @param dto
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/hqm/8d/initiated/actions/commit")
	@ResponseBody
	public ResponseData commit(DimensionInitiatedActions dto, HttpServletRequest request) {
		IRequest requestContext = createRequestContext(request);
		ResponseData responseData = new ResponseData();
		try {
			responseData = service.commit(requestContext, dto);
		} catch (Exception e) {
			responseData.setSuccess(false);
			responseData.setMessage(e.getMessage());
		}
		return responseData;
	}
}