package com.hand.hcs.hcs_supply_demand.controllers;

import org.springframework.stereotype.Controller;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hcs.hcs_supply_demand.dto.SupplyDemand;
import com.hand.hcs.hcs_supply_demand.service.ISupplyDemandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import org.springframework.validation.BindingResult;
import java.util.List;

@Controller
public class SupplyDemandController extends BaseController {

	@Autowired
	private ISupplyDemandService service;

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
	@RequestMapping(value = "/hcs/supply/demand/query")
	@ResponseBody
	public ResponseData query(SupplyDemand dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
			@RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
		IRequest requestContext = createRequestContext(request);
		return new ResponseData(service.select(requestContext, dto, page, pageSize));
	}

	/**
	 * 保存
	 * 
	 * @description
	 * @author tianmin.wang
	 * @date 2019年11月22日
	 * @param dto
	 * @param result
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/hcs/supply/demand/submit")
	@ResponseBody
	public ResponseData update(@RequestBody List<SupplyDemand> dto, BindingResult result, HttpServletRequest request) {
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
	 * @description
	 * @author tianmin.wang
	 * @date 2019年11月22日
	 * @param request
	 * @param dto
	 * @return
	 */
	@RequestMapping(value = "/hcs/supply/demand/remove")
	@ResponseBody
	public ResponseData delete(HttpServletRequest request, @RequestBody List<SupplyDemand> dto) {
		service.batchDelete(dto);
		return new ResponseData();
	}
}