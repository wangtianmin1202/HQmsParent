package com.hand.hqm.hqm_switch_score.controllers;

import org.springframework.stereotype.Controller;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hqm.hqm_switch_score.dto.SwitchScore;
import com.hand.hqm.hqm_switch_score.service.ISwitchScoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import org.springframework.validation.BindingResult;
import java.util.List;

@Controller
public class SwitchScoreController extends BaseController {

	@Autowired
	private ISwitchScoreService service;

	/**
	 * 页面查询
	 * 
	 * @param dto      查询内容
	 * @param page     页码
	 * @param pageSize 页大小
	 * @param request  请求
	 * @return 结果集
	 */
	@RequestMapping(value = "/hqm/switch/score/query")
	@ResponseBody
	public ResponseData query(SwitchScore dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
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
	@RequestMapping(value = "/hqm/switch/score/submit")
	@ResponseBody
	public ResponseData update(@RequestBody List<SwitchScore> dto, BindingResult result, HttpServletRequest request) {
		getValidator().validate(dto, result);
		if (result.hasErrors()) {
			ResponseData responseData = new ResponseData(false);
			responseData.setMessage(getErrorMessage(result, request));
			return responseData;
		}
		IRequest requestCtx = createRequestContext(request);
		try {
			return new ResponseData(service.batchUpdate(requestCtx, dto));
		} catch (RuntimeException e) {
			ResponseData res = new ResponseData(false);
			res.setMessage(e.getMessage());
			return res;
		}
	}

	/**
	 * 删除
	 * 
	 * @param request
	 * @param dto
	 * @return
	 */
	@RequestMapping(value = "/hqm/switch/score/remove")
	@ResponseBody
	public ResponseData delete(HttpServletRequest request, @RequestBody List<SwitchScore> dto) {
		service.batchDelete(dto);
		return new ResponseData();
	}
}