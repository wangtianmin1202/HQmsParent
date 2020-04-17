package com.hand.hqm.hqm_item_control.controllers;

import org.springframework.stereotype.Controller;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.system.service.IPromptService;
import com.hand.hap.util.SystemApiMethod;
import com.hand.hqm.hqm_item_control.dto.ItemControlQms;
import com.hand.hqm.hqm_item_control.service.IItemControlQmsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import org.springframework.validation.BindingResult;
import java.util.List;

@Controller
public class ItemControlControllerQms extends BaseController {

	@Autowired
	private IItemControlQmsService service;
	@Autowired
	private IPromptService iPromptService;

	/**
	 * 
	 * @description 基础查询
	 * @author tianmin.wang
	 * @date 2019年12月17日
	 * @param dto
	 * @param page
	 * @param pageSize
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/hqm/item/control/query")
	@ResponseBody
	public ResponseData query(ItemControlQms dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
			@RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
		IRequest requestContext = createRequestContext(request);
		return new ResponseData(service.reSelect(requestContext, dto, page, pageSize));
	}

	@RequestMapping(value = "/hqm/item/control/submit")
	@ResponseBody
	public ResponseData update(@RequestBody List<ItemControlQms> dto, BindingResult result,
			HttpServletRequest request) {
		getValidator().validate(dto, result);
		if (result.hasErrors()) {
			ResponseData responseData = new ResponseData(false);
			responseData.setMessage(getErrorMessage(result, request));
			return responseData;
		}
		IRequest requestCtx = createRequestContext(request);
		try {
			ResponseData responseData = new ResponseData();
			responseData.setRows(service.newBatchUpdate(requestCtx, dto));
			return responseData;
		} catch (Exception e) {
			ResponseData responseData = new ResponseData(false);
			if (e.getMessage().startsWith("nested")) {
				responseData.setMessage(SystemApiMethod.getPromptDescription(requestCtx, iPromptService,
						"error.hqm_inv_recheck_create01"));
				return responseData;
			} else {
				throw e;
			}

		}
	}

	@RequestMapping(value = "/hqm/item/control/remove")
	@ResponseBody
	public ResponseData delete(HttpServletRequest request, @RequestBody List<ItemControlQms> dto) {
		service.batchDelete(dto);
		return new ResponseData();
	}
}