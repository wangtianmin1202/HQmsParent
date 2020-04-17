package com.hand.hcs.hcs_supply_plan.controllers;

import com.hand.sys.sys_function_button_control.service.IFunctionButtonControlService;
import org.springframework.stereotype.Controller;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.system.service.IPromptService;
import com.hand.hap.util.SystemApiMethod;
import com.hand.hcs.hcs_supply_demand.dto.SupplyDemand;
import com.hand.hcs.hcs_supply_plan.dto.SupplyPlan;
import com.hand.hcs.hcs_supply_plan.service.ISupplyPlanService;

import org.drools.core.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import org.springframework.validation.BindingResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class SupplyPlanController extends BaseController {

	@Autowired
	private ISupplyPlanService service;
	@Autowired
	IPromptService iPromptService;
	@Autowired
	IFunctionButtonControlService functionButtonControlService;

	private String requestUrl;

	/**
	 * 页面查询
	 * 
	 * @param dto      查询内容
	 * @param page     页码
	 * @param pageSize 页大小
	 * @param request  请求
	 * @return 结果集
	 */
	@RequestMapping(value = "/hcs/supply/plan/query")
	@ResponseBody
	public ResponseData query(SupplyPlan dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
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
	@RequestMapping(value = "/hcs/supply/plan/select")
	@ResponseBody
	public ResponseData select(SupplyPlan dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
			@RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
		requestUrl = request.getHeader("Referer");
		IRequest requestContext = createRequestContext(request);
		return new ResponseData(service.reSelect(requestContext, dto, page, pageSize));
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
	@RequestMapping(value = "/hcs/supply/plan/max/line/select")
	@ResponseBody
	public ResponseData maxLineNum(SupplyPlan dto, HttpServletRequest request) {
		IRequest requestContext = createRequestContext(request);
		return new ResponseData(service.maxLineNumSelect(requestContext, dto));
	}

	/**
	 * 提交
	 * 
	 * @param dto     操作数据集
	 * @param result  结果参数
	 * @param request 请求
	 * @return 操作结果
	 */
	@RequestMapping(value = "/hcs/supply/plan/submit")
	@ResponseBody
	public ResponseData update(@RequestBody List<SupplyPlan> dto, BindingResult result, HttpServletRequest request)
			throws Exception {
		IRequest requestCtx = createRequestContext(request);
		String requriredString = SystemApiMethod.getPromptDescription(requestCtx, iPromptService,
				"parameterconfig.required");
		getValidator().validate(dto, result);
		if (result.hasErrors()) {
			ResponseData responseData = new ResponseData(false);
			responseData.setMessage(getErrorMessage(result, request));
			return responseData;
		}
		for (SupplyPlan su : dto) {
			if (su.getSupplyPlanId() == null) {
				su.set__status("add");
			} else {
				su.set__status("update");
			}
			// 一些起始校验
			if ("Y".equals(su.getSplitFlag())) {
				if (su.getSupplierDeliveryTime() == null) {
					ResponseData responseData = new ResponseData(false);
					responseData.setMessage(SystemApiMethod.getPromptDescription(requestCtx, iPromptService,
							"supplyplan.supplierdeliverytime") + requriredString);
					return responseData;
				}
				if (su.getSupplierDeliveryQty() == null) {
					ResponseData responseData = new ResponseData(false);
					responseData.setMessage(SystemApiMethod.getPromptDescription(requestCtx, iPromptService,
							"supplyplan.supplierdeliveryqty") + requriredString);

					return responseData;
				} else {
					if (su.getSupplierDeliveryQty().intValue() < 1) {
						ResponseData responseData = new ResponseData(false);
						responseData.setMessage(SystemApiMethod.getPromptDescription(requestCtx, iPromptService,
								"error.srm_purchase_1072"));

						return responseData;
					}
				}
			}
			if (su.getItemId() == null) {
				ResponseData responseData = new ResponseData(false);
				responseData.setMessage(
						SystemApiMethod.getPromptDescription(requestCtx, iPromptService, "supplyplan.itemid")
								+ requriredString);
				return responseData;
			}
			if (su.getPurchaseType() == null) {
				ResponseData responseData = new ResponseData(false);
				responseData.setMessage(
						SystemApiMethod.getPromptDescription(requestCtx, iPromptService, "error.srm_purchase_1039"));
				return responseData;
			}
			if (su.getRequireTime() == null) {
				ResponseData responseData = new ResponseData(false);
				responseData.setMessage(
						SystemApiMethod.getPromptDescription(requestCtx, iPromptService, "supplyplan.requiretime")
								+ requriredString);
				return responseData;
			}
			if (su.getRequireQty() == null) {
				ResponseData responseData = new ResponseData(false);
				responseData.setMessage(
						SystemApiMethod.getPromptDescription(requestCtx, iPromptService, "supplyplan.requireqty")
								+ requriredString);
				return responseData;
			}
			if (su.getSupplierId() == null) {
				ResponseData responseData = new ResponseData(false);
				responseData.setMessage(
						SystemApiMethod.getPromptDescription(requestCtx, iPromptService, "supplyplan.suppliername")
								+ requriredString);
				return responseData;
			}
			if (su.getRequireQty() < 1) {
				ResponseData responseData = new ResponseData(false);
				responseData.setMessage(
						SystemApiMethod.getPromptDescription(requestCtx, iPromptService, "error.srm_purchase_1132"));
				return responseData;
			}
			if (su.getRealityShipQty() == null) {
				su.setRealityShipQty(su.getRequireQty());
			}
		}

		try {
			return new ResponseData(service.reBatchUpdate(requestCtx, dto));
		} catch (RuntimeException e1) {
			ResponseData responseData = new ResponseData(false);
			responseData.setMessage(e1.getMessage());
			return responseData;
		}
	}

	/**
	 * 删除
	 * 
	 * @description
	 * @author tianmin.wang
	 * @date 2019年11月22日
	 * @param request
	 * @param dto
	 * @return
	 */
	@RequestMapping(value = "/hcs/supply/plan/remove")
	@ResponseBody
	public ResponseData delete(HttpServletRequest request, @RequestBody List<SupplyPlan> dto) {
		service.batchDelete(dto);
		return new ResponseData();
	}

	/**
	 * st excel文件导入
	 * 
	 * @description
	 * @author tianmin.wang
	 * @date 2019年11月22日
	 * @param request
	 * @return
	 * @throws Throwable
	 */
	@RequestMapping(value = "/hcs/supply/plan/st/excelimport/upload")
	@ResponseBody
	public ResponseData stExcelImport(HttpServletRequest request) throws Throwable {
		ResponseData responseData = new ResponseData();
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		IRequest requestCtx = createRequestContext(request);
		// 获取文件map集合
		Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
		List<SupplyDemand> returnList = new ArrayList<SupplyDemand>();
		for (Map.Entry<String, MultipartFile> entry : fileMap.entrySet()) {
			MultipartFile forModel = entry.getValue();
			try {
				returnList.addAll(service.stExcelImport(requestCtx, forModel));
			} catch (RuntimeException e) {
				responseData = new ResponseData(false);
				responseData.setMessage(e.getMessage());
				return responseData;
			}
		}
		responseData.setRows(returnList);
		return responseData;
	}

	/**
	 * jit excel 文件导入
	 * 
	 * @description
	 * @author tianmin.wang
	 * @date 2019年11月22日
	 * @param request
	 * @return
	 * @throws Throwable
	 */
	@RequestMapping(value = "/hcs/supply/plan/jit/excelimport/upload")
	@ResponseBody
	public ResponseData jitExcelImport(HttpServletRequest request) throws Throwable {
		ResponseData responseData = new ResponseData();
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		IRequest requestCtx = createRequestContext(request);
		// 获取文件map集合
		Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
		List<SupplyDemand> returnList = new ArrayList<SupplyDemand>();
		for (Map.Entry<String, MultipartFile> entry : fileMap.entrySet()) {
			MultipartFile forModel = entry.getValue();
			try {
				returnList.addAll(service.jitExcelImport(requestCtx, forModel));
			} catch (RuntimeException e) {
				responseData = new ResponseData(false);
				responseData.setMessage(e.getMessage());
				return responseData;
			}
		}
		responseData.setRows(returnList);
		return responseData;
	}

	/**
	 * 生成供货计划
	 * 
	 * @description
	 * @author tianmin.wang
	 * @date 2019年11月22日
	 * @param list
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/hcs/supply/plan/generate")
	@ResponseBody
	public ResponseData generate(@RequestBody List<SupplyPlan> list, HttpServletRequest request) throws Exception {

		ResponseData responseData = new ResponseData();
		IRequest requestCtx = createRequestContext(request);
		try {
			service.generate(requestCtx, list);
		} catch (RuntimeException r) {
			responseData.setSuccess(false);
			responseData.setMessage(r.getMessage());
			return responseData;
		}
		return responseData;
	}

	/**
	 * 取消供货计划
	 * 
	 * @description
	 * @author tianmin.wang
	 * @date 2019年11月22日
	 * @param list
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/hcs/supply/plan/cancel")
	@ResponseBody
	public ResponseData cancel(@RequestBody List<SupplyPlan> list, HttpServletRequest request) throws Exception {

		ResponseData responseData = new ResponseData();
		IRequest requestCtx = createRequestContext(request);
		try {
			service.cancel(requestCtx, list);
		} catch (RuntimeException r) {
			responseData.setSuccess(false);
			responseData.setMessage(r.getMessage());
			return responseData;
		}
		return responseData;
	}

	/**
	 * 提交
	 * 
	 * @description
	 * @author tianmin.wang
	 * @date 2019年11月22日
	 * @param list
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/hcs/supply/plan/confirm")
	@ResponseBody
	public ResponseData confirm(@RequestBody List<SupplyPlan> list, HttpServletRequest request) throws Exception {

		ResponseData responseData = new ResponseData();
		IRequest requestCtx = createRequestContext(request);
		try {
			service.confirm(requestCtx, list);
		} catch (RuntimeException r) {
			responseData.setSuccess(false);
			responseData.setMessage(r.getMessage());
			return responseData;
		}
		return responseData;
	}

	@RequestMapping(value = "/hcs/supply/plan/get/sum/supplierdeliveryqty")
	@ResponseBody
	public Integer getSumSupplierdeliveryQty(@RequestParam String supplyPlanNum, @RequestParam String supplyPlanLineNum,
			HttpServletRequest request) throws Exception {

		return service.getSumSupplierdeliveryQty(supplyPlanNum, supplyPlanLineNum);
	}


	@RequestMapping(value = "hcs/supply/plan/get/buttonlist")
	@ResponseBody
	public ResponseData getButtonList(){
		return new ResponseData(functionButtonControlService.getButtonByFunctionCode(requestUrl));
	}
}