package com.hand.hqm.hqm_msa_bias_value.controllers;

import org.springframework.stereotype.Controller;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hqm.hqm_msa_bias_value.dto.MsaBiasValue;
import com.hand.hqm.hqm_msa_bias_value.service.IMsaBiasValueService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import org.springframework.validation.BindingResult;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
public class MsaBiasValueController extends BaseController {

	@Autowired
	private IMsaBiasValueService service;
	/**
     * 页面查询
     * @param dto 查询内容
     * @param page 页码
     * @param pageSize 页大小
     * @param request 请求
     * @return 结果集
     */
	@RequestMapping(value = "/hqm/msa/bias/value/query")
	@ResponseBody
	public ResponseData query(MsaBiasValue dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
			@RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
		IRequest requestContext = createRequestContext(request);
		return new ResponseData(service.query(requestContext, dto));
	}
	/**
     * 提交
     * @param dto 操作数据集
     * @param result 结果参数
     * @param request 请求
     * @return 操作结果
     */
	@RequestMapping(value = "/hqm/msa/bias/value/submit")
	@ResponseBody
	public ResponseData update(@RequestBody List<MsaBiasValue> dto, BindingResult result, HttpServletRequest request) {
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
	@RequestMapping(value = "/hqm/msa/bias/value/remove")
	@ResponseBody
	public ResponseData delete(HttpServletRequest request, @RequestBody List<MsaBiasValue> dto) {
		service.batchDelete(dto);
		return new ResponseData();
	}
	/**
	 * 保存
	 * @param dto
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/hqm/msa/bias/value/save")
	@ResponseBody
	public ResponseData submit(@RequestBody List<MsaBiasValue> dto, HttpServletRequest request) {
		ResponseData responseData = new ResponseData();
		IRequest requestCtx = createRequestContext(request);
		try {
			service.submit(requestCtx, dto);
		} catch (Exception e) {
			e.printStackTrace();
			responseData.setMessage(e.getMessage());
			responseData.setSuccess(false);
		}
		return responseData;
	}

	/**
	 * 导入
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/hqm/msa/bias/value/upload")
	@ResponseBody
	public ResponseData excelImport(HttpServletRequest request) {
		ResponseData responseData = new ResponseData();
		IRequest requestCtx = createRequestContext(request);
		try {
			service.excelImport(request, requestCtx);
			responseData.setMessage("导入成功");
		} catch (Exception e) {
			responseData.setMessage(e.getMessage());
			responseData.setSuccess(false);
		} 
		return responseData;
	}
	/**
	 * 偏倚删除
	 * @param request
	 * @param dto
	 * @return
	 */
	@RequestMapping(value = "/hqm/msa/bias/value/removeByMsaPlanId")
	@ResponseBody
	public ResponseData removeByMsaPlanId(HttpServletRequest request, MsaBiasValue dto) {
		IRequest requestCtx = createRequestContext(request);
		service.removeByMsaPlanId(requestCtx, dto);
		return new ResponseData();
	}
	/**
	 * 日期格式化
	 */
	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		dateFormat.setLenient(false);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
	}
}