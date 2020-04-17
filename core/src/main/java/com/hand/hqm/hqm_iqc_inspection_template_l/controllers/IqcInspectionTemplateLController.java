package com.hand.hqm.hqm_iqc_inspection_template_l.controllers;

import org.springframework.stereotype.Controller;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.system.service.IPromptService;
import com.hand.hap.util.SystemApiMethod;
import com.hand.hqm.hqm_iqc_inspection_template_l.dto.IqcInspectionTemplateL;
import com.hand.hqm.hqm_iqc_inspection_template_l.service.IIqcInspectionTemplateLService;
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
public class IqcInspectionTemplateLController extends BaseController {

	@Autowired
	private IIqcInspectionTemplateLService service;

	@Autowired
	IPromptService iPromptService;

	/**
	 * 基础查询
	 * 
	 * @description
	 * @author tianmin.wang
	 * @date 2019年11月22日
	 * @param dto
	 * @param page
	 * @param pageSize
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/hqm/iqc/inspection/template/l/query")
	@ResponseBody
	public ResponseData query(IqcInspectionTemplateL dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
			@RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
		IRequest requestContext = createRequestContext(request);
		return new ResponseData(service.myselect(requestContext, dto, page, pageSize));
	}

	/**
	 * 
	 * @description 保存
	 * @author tianmin.wang
	 * @date 2019年11月22日
	 * @param dto
	 * @param result
	 * @param request
	 * @return
	 * @throws Throwable
	 */
	@RequestMapping(value = "/hqm/iqc/inspection/template/l/submit")
	@ResponseBody
	public ResponseData update(@RequestBody List<IqcInspectionTemplateL> dto, BindingResult result,
			HttpServletRequest request) throws Throwable {
		getValidator().validate(dto, result);
		if (result.hasErrors()) {
			ResponseData responseData = new ResponseData(false);
			responseData.setMessage(getErrorMessage(result, request));
			return responseData;
		}
		int rowIndex = 0;
		for (IqcInspectionTemplateL dtoModel : dto) {
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
		return new ResponseData(service.historynumberUpdate(requestCtx, dto));
	}

	/**
	 * 
	 * @description 删除
	 * @author tianmin.wang
	 * @date 2019年11月22日
	 * @param request
	 * @param dto
	 * @return
	 */
	@RequestMapping(value = "/hqm/iqc/inspection/template/l/remove")
	@ResponseBody
	public ResponseData delete(HttpServletRequest request, @RequestBody List<IqcInspectionTemplateL> dto) {
		IRequest requestCtx = createRequestContext(request);
		service.reBatchDelete(requestCtx, dto);
		return new ResponseData();
	}
}