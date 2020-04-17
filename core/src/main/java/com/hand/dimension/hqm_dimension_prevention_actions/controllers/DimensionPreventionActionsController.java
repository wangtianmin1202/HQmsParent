package com.hand.dimension.hqm_dimension_prevention_actions.controllers;

import org.springframework.stereotype.Controller;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.dimension.hqm_dimension_prevention_actions.dto.DimensionPreventionActions;
import com.hand.dimension.hqm_dimension_prevention_actions.service.IDimensionPreventionActionsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import org.springframework.validation.BindingResult;
import java.util.List;

@Controller
public class DimensionPreventionActionsController extends BaseController {

	@Autowired
	private IDimensionPreventionActionsService service;

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
	@RequestMapping(value = "/hqm/8d/prevention/actions/query")
	@ResponseBody
	public ResponseData query(DimensionPreventionActions dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
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
	@RequestMapping(value = "/hqm/8d/prevention/actions/selectDelete")
	@ResponseBody
	public ResponseData selectDelete(DimensionPreventionActions dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
			@RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
		IRequest requestContext = createRequestContext(request);
		return new ResponseData(service.selectDelete(requestContext, dto, page, pageSize));
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
	@RequestMapping(value = "/hqm/8d/prevention/actions/select")
	@ResponseBody
	public ResponseData select(DimensionPreventionActions dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
			@RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
		IRequest requestContext = createRequestContext(request);
		return new ResponseData(service.reSelect(requestContext, dto, page, pageSize));
	}

	/**
	 * 
	 * @description 数据更新保存提交
	 * @author tianmin.wang
	 * @date 2019年11月21日
	 * @param dto
	 * @param result
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/hqm/8d/prevention/actions/submit")
	@ResponseBody
	public ResponseData update(@RequestBody List<DimensionPreventionActions> dto, BindingResult result,
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
	 * @description 发布入口
	 * @author tianmin.wang
	 * @date 2019年11月21日
	 * @param dto
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/hqm/8d/prevention/actions/issue")
	@ResponseBody
	public ResponseData issue(@RequestBody List<DimensionPreventionActions> dto, HttpServletRequest request) {
		IRequest requestCtx = createRequestContext(request);
		return new ResponseData(service.batchIssue(requestCtx, dto));
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
	@RequestMapping(value = "/hqm/8d/prevention/actions/remove")
	@ResponseBody
	public ResponseData delete(HttpServletRequest request, @RequestBody List<DimensionPreventionActions> dto) {
		service.batchUpdateById(dto);
		return new ResponseData();
	}

	/**
	 * 
	 * @description 提交当前步骤
	 * @author tianmin.wang
	 * @date 2019年11月21日 
	 * @param dto
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/hqm/8d/prevention/actions/commit")
	@ResponseBody
	public ResponseData commit(DimensionPreventionActions dto, HttpServletRequest request) {
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