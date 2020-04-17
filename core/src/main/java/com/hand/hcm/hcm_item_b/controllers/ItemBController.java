package com.hand.hcm.hcm_item_b.controllers;

import org.springframework.stereotype.Controller;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.util.SystemApiMethod;
import com.hand.hcm.hcm_item_b.dto.ItemB;
import com.hand.hcm.hcm_item_b.service.IItemBService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.ContextLoader;

import javax.servlet.http.HttpServletRequest;
import org.springframework.validation.BindingResult;
import java.util.List;

@Controller
public class ItemBController extends BaseController {

	@Autowired
	private IItemBService service;

	@RequestMapping(value = "/hcm/item/b/query")
	@ResponseBody
	public List<?> query(ItemB dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
			@RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
		IRequest requestContext = createRequestContext(request);
		return service.teselect(requestContext, dto, page, pageSize);
	}

	@RequestMapping(value = "/hcm/item/b/submit")
	@ResponseBody
	public ResponseData update(@RequestBody List<ItemB> dto, BindingResult result, HttpServletRequest request) {
		getValidator().validate(dto, result);
		if (result.hasErrors()) {
			ResponseData responseData = new ResponseData(false);
			responseData.setMessage(getErrorMessage(result, request));
			return responseData;
		}
		IRequest requestCtx = createRequestContext(request);
		return new ResponseData(service.batchUpdate(requestCtx, dto));
	}

	@RequestMapping(value = "/hcm/item/b/remove")
	@ResponseBody
	public ResponseData delete(HttpServletRequest request, @RequestBody List<ItemB> dto) {
		service.batchDelete(dto);
		return new ResponseData();
	}

	@RequestMapping(value = "/hcm/item/b/test1/demo")
	@ResponseBody
	public ResponseData test1(HttpServletRequest request) {
		ResponseData res = new ResponseData();
		res.setMessage(SystemApiMethod.getPromptDescription("iqcinspectionh.getprocess"));
		return res;
	}

}