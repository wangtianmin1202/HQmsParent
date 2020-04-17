package com.hand.hqm.hqm_temporary_inspection.controllers;

import org.springframework.stereotype.Controller;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.system.service.IPromptService;
import com.hand.hqm.hqm_temporary_inspection.dto.TemporaryInspection;
import com.hand.hqm.hqm_temporary_inspection.service.ITemporaryInspectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import org.springframework.validation.BindingResult;
import java.util.List;

@Controller
public class TemporaryInspectionController extends BaseController {

	@Autowired
	private ITemporaryInspectionService service;
	
	
	/**
	 * 
	 * @description 基础查询
	 * @author tianmin.wang
	 * @date 2019年12月10日
	 * @param dto
	 * @param page
	 * @param pageSize
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/hqm/temporary/inspection/query")
	@ResponseBody
	public ResponseData query(TemporaryInspection dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
			@RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
		IRequest requestContext = createRequestContext(request);
		return new ResponseData(service.reSelect(requestContext, dto, page, pageSize));
	}

	/**
	 * 
	 * @description 保存修改
	 * @author tianmin.wang
	 * @date 2019年12月10日
	 * @param dto
	 * @param result
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/hqm/temporary/inspection/submit")
	@ResponseBody
	public ResponseData update(@RequestBody List<TemporaryInspection> dto, BindingResult result,
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
	 * @description 删除所选
	 * @author tianmin.wang
	 * @date 2019年12月10日
	 * @param request
	 * @param dto
	 * @return
	 */
	@RequestMapping(value = "/hqm/temporary/inspection/remove")
	@ResponseBody
	public ResponseData delete(HttpServletRequest request, @RequestBody List<TemporaryInspection> dto) {
		service.batchDelete(dto);
		return new ResponseData();
	}

	@RequestMapping(value = "/hqm/temporary/inspection/save")
	@ResponseBody
	public ResponseData addnew(TemporaryInspection dto, HttpServletRequest request) {
		IRequest requestContext = createRequestContext(request);
		ResponseData res = new ResponseData();
		try {
			service.addNew(requestContext, dto);
		} catch (RuntimeException e) {
			ResponseData ress = new ResponseData(false);
			ress.setMessage(e.getMessage());
			return ress;
		} catch (Exception e) {
			throw e;
		}
		return res;
	}

	/**
	 * 
	 * @description 批量发布
	 * @author tianmin.wang
	 * @date 2019年12月10日 
	 * @param dto
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/hqm/temporary/inspection/issue")
	@ResponseBody
	public ResponseData issue(@RequestBody List<TemporaryInspection> dto,
			HttpServletRequest request) {
		IRequest requestCtx = createRequestContext(request);
		ResponseData res = new ResponseData();
		try {
			service.batchIssue(requestCtx, dto);
		} catch (RuntimeException e) {
			ResponseData ress = new ResponseData(false);
			ress.setMessage(e.getMessage());
			return ress;
		} catch (Exception e) {
			throw e;
		}
		return res;
	}
	
	/**
	 * 
	 * @description 批量失效
	 * @author tianmin.wang
	 * @date 2019年12月10日 
	 * @param dto
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/hqm/temporary/inspection/disable")
	@ResponseBody
	public ResponseData disable(@RequestBody List<TemporaryInspection> dto,
			HttpServletRequest request) {
		IRequest requestCtx = createRequestContext(request);
		ResponseData res = new ResponseData();
		try {
			service.batchDisable(requestCtx, dto);
		} catch (RuntimeException e) {
			ResponseData ress = new ResponseData(false);
			ress.setMessage(e.getMessage());
			return ress;
		} catch (Exception e) {
			throw e;
		}
		return res;
	}
}