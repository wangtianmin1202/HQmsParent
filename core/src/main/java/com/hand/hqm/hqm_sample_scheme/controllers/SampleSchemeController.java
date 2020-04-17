package com.hand.hqm.hqm_sample_scheme.controllers;

import org.springframework.stereotype.Controller;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.system.service.IPromptService;
import com.hand.hap.util.SystemApiMethod;
import com.hand.hqm.hqm_item_category_ext.dto.ItemCategoryExt;
import com.hand.hqm.hqm_item_category_ext.mapper.ItemCategoryExtMapper;
import com.hand.hqm.hqm_sample_scheme.dto.SampleScheme;
import com.hand.hqm.hqm_sample_scheme.service.ISampleSchemeService;

import jodd.util.StringUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.servlet.http.HttpServletRequest;
import org.springframework.validation.BindingResult;
import java.util.List;
import java.util.Locale;

/*
 * created by tianmin.wang on 2019/7/12.
 */
@Controller
public class SampleSchemeController extends BaseController {

	@Autowired
	private ISampleSchemeService service;
	@Autowired
	private ItemCategoryExtMapper itemCategoruExtMapper;
	@Autowired
	IPromptService iPromptService;
	/**
	 * 页面查询
	 * 
	 * @param dto      查询内容
	 * @param page     页码
	 * @param pageSize 页大小
	 * @param request  请求
	 * @return 结果集
	 */
	@RequestMapping(value = "/hqm/sample/scheme/query")
	@ResponseBody
	public ResponseData query(SampleScheme dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
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
	@RequestMapping(value = "/hqm/sample/scheme/select")
	@ResponseBody
	public ResponseData select(SampleScheme dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
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
	@RequestMapping(value = "/hqm/sample/scheme/submit")
	@ResponseBody

	public ResponseData update(@RequestBody List<SampleScheme> dto, BindingResult result, HttpServletRequest request) {
		getValidator().validate(dto, result);
		if (result.hasErrors()) {
			ResponseData responseData = new ResponseData(false);
			responseData.setMessage(getErrorMessage(result, request));
			return responseData;
		}
		for (SampleScheme validate : dto) {
			if ("0".equals(validate.getSampleProcedureType()) && validate.getSamplePlanType() == null) {
				ResponseData responseData = new ResponseData(false);
				responseData.setMessage(SystemApiMethod.getPromptDescription(request, iPromptService,
						"samples_cheme_update_error6"));//抽样计划类型不能为空
				return responseData;
			}
			if ("0".equals(validate.getSampleProcedureType()) && validate.getSampleSizeCodeLetter() == null) {
				ResponseData responseData = new ResponseData(false);
				responseData.setMessage(SystemApiMethod.getPromptDescription(request, iPromptService,
						"samples_cheme_update_error5"));//样本量字码不能为空
				return responseData;
			}
			if ("0".equals(validate.getSampleProcedureType()) && validate.getReValue() <= validate.getAcValue()) {
				ResponseData responseData = new ResponseData(false);
				responseData.setMessage(SystemApiMethod.getPromptDescription(request, iPromptService,
						"samples_cheme_update_error4"));//Re值必须大于Ac值
				return responseData;
			}
			/**
			 * commented by wtm 20191219
			 */
//			if (validate.getEnableFlag().equals("N")) {
//				ItemCategoryExt record = new ItemCategoryExt();
//				record.setEnableFlag("Y");
//				record.setSampleProcedureType(validate.getSampleProcedureType());
//				if (itemCategoruExtMapper.select(record) != null) {
//					ResponseData responseData = new ResponseData(false);
//					responseData.setMessage(SystemApiMethod.getPromptDescription(request, iPromptService,
//							"samples_cheme_update_error3"));//IQC物料类别检验项扩展属性的有效记录引用到该行记录
//					return responseData;
//				}
//			}
			if ("3".equals(validate.getSampleProcedureType())
					&& (StringUtil.isEmpty(validate.getAttribute3()) || StringUtil.isEmpty(validate.getAttribute4()))) {
				ResponseData responseData = new ResponseData(false);
				responseData.setMessage(SystemApiMethod.getPromptDescription(request, iPromptService,
						"samples_cheme_update_error2"));//成品量产中成品平台和抽样比例必输
				return responseData;
			}
		}
		IRequest requestCtx = createRequestContext(request);
		return new ResponseData(service.rebatchUpdate(requestCtx, dto));
	}

	/**
	 * 删除
	 * 
	 * @param request
	 * @param dto
	 * @return
	 */
	@RequestMapping(value = "/hqm/sample/scheme/remove")
	@ResponseBody
	public ResponseData delete(HttpServletRequest request, @RequestBody List<SampleScheme> dto) {
		service.batchDelete(dto);
		return new ResponseData();
	}
}