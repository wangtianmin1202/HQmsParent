package com.hand.hqm.hqm_qc_task.controllers;

import org.springframework.stereotype.Controller;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hqm.hqm_qc_task.dto.IqcTask;
import com.hand.hqm.hqm_qc_task.service.IIqcTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import org.springframework.validation.BindingResult;
import java.util.List;

@Controller
public class IqcTaskController extends BaseController {

	@Autowired
	private IIqcTaskService service;

	/**
	 * 页面查询
	 * 
	 * @param dto      查询内容
	 * @param page     页码
	 * @param pageSize 页大小
	 * @param request  请求
	 * @return 结果集
	 */
	@RequestMapping(value = "/hqm/iqc/task/query")
	@ResponseBody
	public ResponseData query(IqcTask dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
			@RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
		IRequest requestContext = createRequestContext(request);
		return new ResponseData(service.select(requestContext, dto, page, pageSize));
	}

	/**
	 * 页面查询
	 * 
	 * @param dto      查询内容
	 * @param page     页码
	 * @param pageSize 页大小
	 * @param request  请求
	 * @return 结果集
	 */
	@RequestMapping(value = "/hqm/iqc/task/select")
	@ResponseBody
	public ResponseData select(IqcTask dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
			@RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
		IRequest requestContext = createRequestContext(request);
		return new ResponseData(service.reSelect(requestContext, dto, page, pageSize));
	}

	/**
	 * 提交
	 * 
	 * @param dto     操作数据集
	 * @param result  结果参数
	 * @param request 请求
	 * @return 操作结果
	 */
	@RequestMapping(value = "/hqm/iqc/task/submit")
	@ResponseBody
	public ResponseData update(@RequestBody List<IqcTask> dto, BindingResult result, HttpServletRequest request) {
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
	 * 
	 * @param request
	 * @param dto
	 * @return
	 */
	@RequestMapping(value = "/hqm/iqc/task/remove")
	@ResponseBody
	public ResponseData delete(HttpServletRequest request, @RequestBody List<IqcTask> dto) {
		service.batchDelete(dto);
		return new ResponseData();
	}

	/**
	 * 生成报告
	 * 
	 * @param request
	 * @param dto
	 * @return
	 */
	@RequestMapping(value = "/hqm/iqc/task/create/iqc")
	@ResponseBody
	public ResponseData createIqc(HttpServletRequest request, @RequestBody List<IqcTask> dto) {
		ResponseData ress = new ResponseData();
		try {
			service.createIqc(dto, request);
			ress.setRows(dto);
		} catch (RuntimeException e) {
			ResponseData res = new ResponseData();
			res.setSuccess(false);
			res.setMessage(e.getMessage());
			IRequest requestContext = createRequestContext(request);
			dto.get(0).setErrorMsg(res.getMessage());
			service.updateByPrimaryKeySelective(requestContext, dto.get(0));
			return res;
		} catch (Exception e) {
			ress.setSuccess(false);
			ress.setMessage(e.getMessage());
		}
		return ress;
	}
}