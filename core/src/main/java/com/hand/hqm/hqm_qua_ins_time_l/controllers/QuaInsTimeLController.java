package com.hand.hqm.hqm_qua_ins_time_l.controllers;

import org.springframework.stereotype.Controller;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.core.IRequest;
import com.hand.hap.core.exception.DatabaseException;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hqm.hqm_qua_ins_time_l.dto.QuaInsTimeL;
import com.hand.hqm.hqm_qua_ins_time_l.service.IQuaInsTimeLService;
import com.hand.hqm.hqm_standard_op_ins_l.dto.StandardOpInspectionL;

import org.apache.ibatis.exceptions.PersistenceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import org.springframework.validation.BindingResult;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

@Controller
public class QuaInsTimeLController extends BaseController {

	@Autowired
	private IQuaInsTimeLService service;
	/**
     * 页面查询
     * @param dto 查询内容
     * @param page 页码
     * @param pageSize 页大小
     * @param request 请求
     * @return 结果集
     */
	@RequestMapping(value = "/hqm/qua/ins/time/l/query")
	@ResponseBody
	public ResponseData query(QuaInsTimeL dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
			@RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
		IRequest requestContext = createRequestContext(request);
		return new ResponseData(service.select(requestContext, dto, page, pageSize));
	}
	/**
     * 提交
     * @param dto 操作数据集
     * @param result 结果参数
     * @param request 请求
     * @return 操作结果
     */
	@RequestMapping(value = "/hqm/qua/ins/time/l/submit")
	@ResponseBody
	public ResponseData update(@RequestBody List<QuaInsTimeL> dto, BindingResult result, HttpServletRequest request) {
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
	@RequestMapping(value = "/hqm/qua/ins/time/l/remove")
	@ResponseBody
	public ResponseData delete(HttpServletRequest request, @RequestBody List<QuaInsTimeL> dto) {
		service.batchDelete(dto);
		return new ResponseData();
	}
	/**
     * 保存行表数据
     * @param dto 查询内容
     * @param request 请求
     * @return 结果集
     */
	@RequestMapping(value = "/hqm/qua/ins/time/l/saveHeadLine")
	@ResponseBody
	public ResponseData saveHeadLine(HttpServletRequest request, @RequestBody List<QuaInsTimeL> dto) throws Throwable {
		IRequest requestContext = createRequestContext(request);
		ResponseData responseData = new ResponseData();
		service.saveHeadLine(requestContext, dto);
		return responseData;
	}
	 /**
     * 行表数据查询
     * @param dto 查询内容
     * @param page 页码
     * @param pageSize 页大小
     * @param request 请求
     * @return 结果集
     */
	@RequestMapping(value = "/hqm/qua/ins/time/l/select")
	@ResponseBody
	public ResponseData select(QuaInsTimeL dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
			@RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
		IRequest requestContext = createRequestContext(request);
		return new ResponseData(service.myselect(requestContext, dto, page, pageSize));
	}
}