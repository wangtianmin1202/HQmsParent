package com.hand.hqm.hqm_inspection_group_l.controllers;

import org.springframework.stereotype.Controller;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.system.service.IPromptService;
import com.hand.hap.util.SystemApiMethod;
import com.hand.hqm.hqm_inspection_group_h.dto.InspectionGroupH;
import com.hand.hqm.hqm_inspection_group_l.dto.InspectionGroupL;
import com.hand.hqm.hqm_inspection_group_l.mapper.InspectionGroupLMapper;
import com.hand.hqm.hqm_inspection_group_l.service.IInspectionGroupLService;
import com.hand.hqm.hqm_iqc_inspection_template_l.dto.IqcInspectionTemplateL;
import com.hand.hqm.hqm_sam_use_order_l.dto.SamUseOrderL;

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
public class InspectionGroupLController extends BaseController {

	@Autowired
	private IInspectionGroupLService service;
	@Autowired
	InspectionGroupLMapper inspectionGroupLMapper;

	@Autowired
	IPromptService iPromptService;

	@RequestMapping(value = "/hqm/inspection/group/l/query")
	@ResponseBody
	public ResponseData query(InspectionGroupL dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
			@RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
		IRequest requestContext = createRequestContext(request);
		return new ResponseData(service.select(requestContext, dto, page, pageSize));
	}

	@RequestMapping(value = "/hqm/inspection/group/l/submit")
	@ResponseBody
	public ResponseData update(@RequestBody List<InspectionGroupL> dto, BindingResult result,
			HttpServletRequest request) throws Throwable {
		getValidator().validate(dto, result);
		if (result.hasErrors()) {
			ResponseData responseData = new ResponseData(false);
			responseData.setMessage(getErrorMessage(result, request));
			return responseData;
		}
		for (InspectionGroupL dtoModel : dto) {
			// 校验规格值从小于规格值至 add by jy 20190920
			if ("M".equals(dtoModel.getStandardType()) && (dtoModel.getStandradFrom() == null
					|| dtoModel.getStandradTo() == null || dtoModel.getStandradUom() == null)) {
				ResponseData responseData = new ResponseData(false);
				responseData.setMessage("规格类型为计量时的必输项:规格值从/规格至/规格单位");
				return responseData;
			}
			if ("M".equals(dtoModel.getStandardType())
					&& (dtoModel.getStandradFrom() != null && dtoModel.getStandradTo() != null)
					&& Float.valueOf(dtoModel.getStandradFrom()) > Float.valueOf(dtoModel.getStandradTo())) {
				ResponseData responseData = new ResponseData(false);
				responseData.setMessage("规格值从要小于规格值至");
				return responseData;
			}
			if ("M".equals(dtoModel.getStandardType())) {
				if (Float.valueOf(dtoModel.getStandradFrom()) > Float.valueOf(dtoModel.getStandradTo())) {
					ResponseData responseData = new ResponseData(false);
					responseData.setMessage(SystemApiMethod.getPromptDescription(request, iPromptService,
							"error.standard_from_than_to"));
					return responseData;
				}
			}
			if ("M".equals(dtoModel.getStandardType())) {
				if (!SystemApiMethod.judgePercision(dtoModel.getStandradFrom(), dtoModel.getStandradTo())) {
					ResponseData responseData = new ResponseData(false);
					responseData.setMessage(SystemApiMethod.getPromptDescription(request, iPromptService,
							"hqm_iqc_inspection_template_precision_01"));
					return responseData;
				}
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

	@RequestMapping(value = "/hqm/inspection/group/l/remove")
	@ResponseBody
	public ResponseData delete(HttpServletRequest request, @RequestBody List<InspectionGroupL> dto) {
		service.batchDelete(dto);
		return new ResponseData();
	}

	@RequestMapping(value = "/hqm/inspection/group/l/savehead")
	@ResponseBody
	public ResponseData saveHead(HttpServletRequest request, InspectionGroupL dto) {
		IRequest requestContext = createRequestContext(request);
		ResponseData responseData = new ResponseData();
		try {
			responseData = service.saveHead(requestContext, dto);
		} catch (Exception e) {
			e.printStackTrace();
			responseData.setMessage(e.getMessage());
			responseData.setSuccess(false);
		}
		return responseData;
	}

	/*
	 * @RequestMapping(value = "/hqm/inspection/group/l/saveline")
	 * 
	 * @ResponseBody public ResponseData saveLine(HttpServletRequest
	 * request, @RequestBody List<InspectionGroupL> dto){ IRequest requestContext =
	 * createRequestContext(request); ResponseData responseData = new
	 * ResponseData(); try { responseData =service.saveLine(requestContext, dto);
	 * }catch(Exception e) { e.printStackTrace();
	 * responseData.setMessage(e.getMessage()); responseData.setSuccess(false); }
	 * 
	 * return responseData; }
	 */

	@RequestMapping(value = "/hqm/inspection/group/l/update")
	@ResponseBody
	public ResponseData updateLine(HttpServletRequest request, @RequestBody List<InspectionGroupL> dto) {
		IRequest requestContext = createRequestContext(request);
		ResponseData responseData = new ResponseData();
		try {
			responseData = service.updateLine(requestContext, dto);
		} catch (Exception e) {
			e.printStackTrace();
			responseData.setMessage(e.getMessage());
			responseData.setSuccess(false);
		}

		return responseData;
	}

	@RequestMapping(value = "/hqm/inspection/group/l/select")
	@ResponseBody
	public ResponseData pselect(InspectionGroupL dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
			@RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
		IRequest requestContext = createRequestContext(request);
		return new ResponseData(service.myselect(requestContext, dto, page, pageSize));
	}

	@RequestMapping(value = "/hqm/inspection/group/l/selectheadincopy")
	@ResponseBody
	public ResponseData selectheadincopy(InspectionGroupL dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
			@RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
		IRequest requestContext = createRequestContext(request);
		return new ResponseData(service.selectheadincopy(requestContext, dto, page, pageSize));
	}

	@RequestMapping(value = "/hqm/inspection/group/l/selectTb")
	@ResponseBody
	public ResponseData selecttb(InspectionGroupL dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
			@RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
		IRequest requestContext = createRequestContext(request);
		return new ResponseData(service.selectTb(requestContext, dto, page, pageSize));
	}

	@RequestMapping(value = "/hqm/inspection/group/l/selecthead")
	@ResponseBody
	public ResponseData select(InspectionGroupL dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
			@RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
		IRequest requestContext = createRequestContext(request);
		return new ResponseData(service.selecthead(requestContext, dto, page, pageSize));
	}
}