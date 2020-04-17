package com.hand.hqm.hqm_item_type_tests.controllers;

import org.springframework.stereotype.Controller;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.system.service.IPromptService;
import com.hand.hap.util.SystemApiMethod;
import com.hand.hqm.hqm_item_type_tests.dto.ItemTypeTests;
import com.hand.hqm.hqm_item_type_tests.service.IItemTypeTestsService;
import com.hand.hqm.hqm_supplier_inspector_rel.dto.SupplierInspectorRel;

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

@Controller
public class ItemTypeTestsController extends BaseController {

	@Autowired
	private IItemTypeTestsService service;
	@Autowired
	IPromptService iPromptService;
	/**
	 * 
	 * @description 主界面查询
	 * @author tianmin.wang
	 * @date 2019年12月17日
	 * @param dto
	 * @param page
	 * @param pageSize
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/hqm/item/type/tests/query")
	@ResponseBody
	public ResponseData query(ItemTypeTests dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
			@RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
		IRequest requestContext = createRequestContext(request);
		return new ResponseData(service.reSelect(requestContext, dto, page, pageSize));
	}

	/**
	 * 
	 * @description 保存
	 * @author tianmin.wang
	 * @date 2019年12月17日
	 * @param dto
	 * @param result
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/hqm/item/type/tests/submit")
	@ResponseBody
	public ResponseData update(@RequestBody List<ItemTypeTests> dto, BindingResult result, HttpServletRequest request) {
		getValidator().validate(dto, result);
		if (result.hasErrors()) {
			ResponseData responseData = new ResponseData(false);
			responseData.setMessage(getErrorMessage(result, request));
			return responseData;
		}
		for (ItemTypeTests validator : dto) {
			if (validator.getTestType().equals("1")
					&& (validator.getItemId() == null || validator.getItemId().intValue() == -1)) {
				ResponseData responseData = new ResponseData(false);
				responseData.setMessage(SystemApiMethod.getPromptDescription(request, iPromptService, "hqm_item_type_tests_error101"));//为1时必须维护物料
				return responseData;
			}
			if (validator.getTestType().equals("2")
					&& (validator.getCategoryId() == null || validator.getCategoryId().intValue() == -1)) {
				ResponseData responseData = new ResponseData(false);
				responseData.setMessage(SystemApiMethod.getPromptDescription(request, iPromptService, "hqm_item_type_tests_error102"));//为2时必须维护物料类别
				return responseData;
			}
		}
		IRequest requestCtx = createRequestContext(request);
		return new ResponseData(service.reBatchUpdate(requestCtx, dto));
	}

	/**
	 * 
	 * @description 删除
	 * @author tianmin.wang
	 * @date 2019年12月17日
	 * @param request
	 * @param dto
	 * @return
	 */
	@RequestMapping(value = "/hqm/item/type/tests/remove")
	@ResponseBody
	public ResponseData delete(HttpServletRequest request, @RequestBody List<ItemTypeTests> dto) {
		service.batchDelete(dto);
		return new ResponseData();
	}

	@RequestMapping(value = "/hqm/item/type/tests/excel/upload")
	@ResponseBody
	public ResponseData excelUpload(HttpServletRequest request) {
		ResponseData responseData = new ResponseData();
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		IRequest requestCtx = createRequestContext(request);
		// 获取文件map集合
		Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
		List<ItemTypeTests> returnList = new ArrayList<ItemTypeTests>();
		for (Map.Entry<String, MultipartFile> entry : fileMap.entrySet()) {
			MultipartFile forModel = entry.getValue();
			try {
				returnList.addAll(service.excelImport(requestCtx, forModel));
			} catch (RuntimeException e) {
				responseData = new ResponseData(false);
				responseData.setMessage(e.getMessage());
				return responseData;
			}
		}
		responseData.setRows(returnList);
		return responseData;
	}

}