package com.hand.hqm.hqm_stan_op_item_l.controllers;

import org.springframework.stereotype.Controller;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.system.service.IPromptService;
import com.hand.hap.util.SystemApiMethod;
import com.hand.hqm.hqm_iqc_inspection_template_l.dto.IqcInspectionTemplateL;
import com.hand.hqm.hqm_stan_op_item_l.dto.StandardOpItemL;
import com.hand.hqm.hqm_stan_op_item_l.service.IStandardOpItemLService;
import com.hand.hqm.hqm_standard_op_ins_l.dto.StandardOpInspectionL;
import com.mysql.jdbc.StringUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.servlet.http.HttpServletRequest;
import org.springframework.validation.BindingResult;

import java.util.Date;
import java.util.List;

@Controller
public class StandardOpItemLController extends BaseController {

	@Autowired
	private IStandardOpItemLService service;
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
	@RequestMapping(value = "/hqm/standard/op/item/l/query")
	@ResponseBody
	public ResponseData query(StandardOpItemL dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
			@RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
		IRequest requestContext = createRequestContext(request);
		return new ResponseData(service.select(requestContext, dto, page, pageSize));
	}

	/**
	 * 提交
	 * 
	 * @param dto     操作数据集
	 * @param result  结果参数
	 * @param request 请求
	 * @return 操作结果
	 */
	@RequestMapping(value = "/hqm/standard/op/item/l/submit")
	@ResponseBody
	public ResponseData update(@RequestBody List<StandardOpItemL> dto, BindingResult result,
			HttpServletRequest request) {
		getValidator().validate(dto, result);
		if (result.hasErrors()) {
			ResponseData responseData = new ResponseData(false);
			responseData.setMessage(getErrorMessage(result, request));
			return responseData;
		}
		int rowIndex = 0;
		for (StandardOpItemL dtoModel : dto) {
			rowIndex++;
			// 校验规格值从小于规格值至 add by jy 20190920
			if ("M".equals(dtoModel.getStandardType()) && (dtoModel.getStandradUom() == null)) {
				ResponseData responseData = new ResponseData(false);
				responseData.setMessage("规格类型为计量时的必输项:规格单位");
				return responseData;
			}
			if ("M".equals(dtoModel.getStandardType()) && StringUtils.isNullOrEmpty(dtoModel.getStandradFrom())
					&& StringUtils.isNullOrEmpty(dtoModel.getStandradTo())) {
				ResponseData responseData = new ResponseData(false);
				responseData.setMessage(rowIndex + "行"
						+ SystemApiMethod.getPromptDescription(request, iPromptService, "error_hqm_template_create02"));
				return responseData;
			}
			if ("M".equals(dtoModel.getStandardType()) && !StringUtils.isNullOrEmpty(dtoModel.getStandradFrom())
					&& !StringUtils.isNullOrEmpty(dtoModel.getStandradTo())
					&& Float.valueOf(dtoModel.getStandradFrom()) > Float.valueOf(dtoModel.getStandradTo())) {
				ResponseData responseData = new ResponseData(false);
				responseData.setMessage("规格值从要小于规格值至!");
				return responseData;
			}
			if ("M".equals(dtoModel.getStandardType()) && !StringUtils.isNullOrEmpty(dtoModel.getStandradFrom())
					&& !StringUtils.isNullOrEmpty(dtoModel.getStandradTo())) {
				if (Float.valueOf(dtoModel.getStandradFrom()) > Float.valueOf(dtoModel.getStandradTo())) {
					ResponseData responseData = new ResponseData(false);
					responseData.setMessage(SystemApiMethod.getPromptDescription(request, iPromptService,
							"error.standard_from_than_to"));
					return responseData;
				}
			}
			if ("M".equals(dtoModel.getStandardType()) && !StringUtils.isNullOrEmpty(dtoModel.getStandradFrom())
					&& !StringUtils.isNullOrEmpty(dtoModel.getStandradTo())) {
				if (!SystemApiMethod.judgePercision(dtoModel.getStandradFrom(), dtoModel.getStandradTo())) {
					ResponseData responseData = new ResponseData(false);
					responseData.setMessage(rowIndex + "行" + SystemApiMethod.getPromptDescription(request,
							iPromptService, "hqm_iqc_inspection_template_precision_01"));
					return responseData;
				}
//				dtoModel.setPrecision(Float.valueOf(SystemApiMethod.getPercision(dtoModel.getStandradFrom().toString())));
			}
			if (dtoModel.getDisableTime() != null) {
				if (dtoModel.getDisableTime().before(dtoModel.getEnableTime())
						|| dtoModel.getDisableTime().before(new Date())) {
					ResponseData responseData = new ResponseData(false);
					responseData.setMessage("失效时间应在生效时间及当前时间之后");
					return responseData;
				}
			}

		}
		IRequest requestCtx = createRequestContext(request);
		return new ResponseData(service.reBatchUpdate(requestCtx, dto));
	}

	/**
	 * 删除
	 * 
	 * @param request
	 * @param dto
	 * @return
	 */
	@RequestMapping(value = "/hqm/standard/op/item/l/remove")
	@ResponseBody
	public ResponseData delete(HttpServletRequest request, @RequestBody List<StandardOpItemL> dto) {
		IRequest requestCtx = createRequestContext(request);
		service.reBatchDelete(requestCtx, dto);
		return new ResponseData();
	}

	@RequestMapping(value = "/hqm/standard/op/item/l/select")
	@ResponseBody
	public ResponseData select(StandardOpItemL dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
			@RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
		IRequest requestContext = createRequestContext(request);
		return new ResponseData(service.myselect(requestContext, dto, page, pageSize));
	}

	@RequestMapping(value = "/hqm/standard/op/item/l/saveHeadLine")
	@ResponseBody
	public ResponseData saveHeadLine(HttpServletRequest request, @RequestBody List<StandardOpItemL> dto) {
		IRequest requestContext = createRequestContext(request);
		ResponseData responseData = new ResponseData();
		if (dto.get(0).getItemId() == null) {
			responseData.setMessage("未选择物料");
			responseData.setSuccess(false);
			return responseData;
		}
		try {
			service.saveHeadLine(requestContext, dto);
		} catch (Exception e) {
			responseData.setMessage(e.getMessage());
			responseData.setSuccess(false);
			e.printStackTrace();
		}

		return responseData;
	}
}