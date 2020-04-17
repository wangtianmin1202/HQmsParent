package com.hand.dimension.hqm_dimension_root_cause_l.controllers;

import org.springframework.stereotype.Controller;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.dimension.hqm_dimension_prevention_actions.dto.DimensionPreventionActions;
import com.hand.dimension.hqm_dimension_root_cause_l.dto.DimensionRootCauseL;
import com.hand.dimension.hqm_dimension_root_cause_l.service.IDimensionRootCauseLService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import org.springframework.validation.BindingResult;
import java.util.List;

@Controller
public class DimensionRootCauseLController extends BaseController {

	@Autowired
	private IDimensionRootCauseLService service;

	/**
	 * 
	 * @description 查询
	 * @author tianmin.wang
	 * @date 2019年11月21日
	 * @param dto
	 * @param page
	 * @param pageSize
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/hqm/8d/root/cause/l/query")
	@ResponseBody
	public ResponseData query(DimensionRootCauseL dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
			@RequestParam(defaultValue = "1000") int pageSize, HttpServletRequest request) {
		IRequest requestContext = createRequestContext(request);
		return new ResponseData(service.reSelect(requestContext, dto, page, pageSize));
	}

	/**
	 * 
	 * @description 数据保存
	 * @author tianmin.wang
	 * @date 2019年11月21日 
	 * @param dto
	 * @param result
	 * @param request
	 * @return
	 * @throws Throwable
	 */
	@RequestMapping(value = "/hqm/8d/root/cause/l/submit")
	@ResponseBody
	public ResponseData update(@RequestBody List<DimensionRootCauseL> dto, BindingResult result,
			HttpServletRequest request) throws Throwable {
		getValidator().validate(dto, result);
		if (result.hasErrors()) {
			ResponseData responseData = new ResponseData(false);
			responseData.setMessage(getErrorMessage(result, request));
			return responseData;
		}
		IRequest requestCtx = createRequestContext(request);

		return new ResponseData(service.reBatchUpdate(requestCtx, dto));

	}

	/**
	 * 
	 * @description 删除
	 * @author tianmin.wang
	 * @date 2019年11月21日 
	 * @param request
	 * @param dto
	 * @return
	 */
	@RequestMapping(value = "/hqm/8d/root/cause/l/remove")
	@ResponseBody
	public ResponseData delete(HttpServletRequest request, @RequestBody List<DimensionRootCauseL> dto) {
		service.batchUpdateById(request, dto);
		return new ResponseData();
	}
}