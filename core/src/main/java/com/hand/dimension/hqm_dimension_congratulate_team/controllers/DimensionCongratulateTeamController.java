package com.hand.dimension.hqm_dimension_congratulate_team.controllers;

import org.springframework.stereotype.Controller;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.dimension.hqm_dimension_congratulate_team.dto.DimensionCongratulateTeam;
import com.hand.dimension.hqm_dimension_congratulate_team.service.IDimensionCongratulateTeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.validation.BindingResult;
import java.util.List;

@Controller
public class DimensionCongratulateTeamController extends BaseController {

	@Autowired
	private IDimensionCongratulateTeamService service;

	/**
	 * 
	 * @description 团队庆祝查询
	 * @author tianmin.wang
	 * @date 2019年11月21日 
	 * @param dto 查询实体
	 * @param page
	 * @param pageSize
	 * @param request 请求参数
	 * @return 结果集
	 */
	@RequestMapping(value = "/hqm/8d/congratulate/team/query")
	@ResponseBody
	public ResponseData query(DimensionCongratulateTeam dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
			@RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
		IRequest requestContext = createRequestContext(request);
		return new ResponseData(service.select(requestContext, dto, page, pageSize));
	}

	/**
	 * 
	 * @description 团队庆祝提交入口
	 * @author tianmin.wang
	 * @date 2019年11月21日 
	 * @param dto
	 * @param page
	 * @param pageSize
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/hqm/8d/congratulate/team/createbyteam")
	@ResponseBody
	public ResponseData createByTeam(DimensionCongratulateTeam dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
			@RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
		IRequest requestContext = createRequestContext(request);
		ResponseData responseData = new ResponseData();
		try {
			service.createByTeam(requestContext, dto.getOrderId());
		} catch (Exception e) {
			responseData.setSuccess(false);
			responseData.setMessage(e.getMessage());
		}
		return responseData;
	}

	/**
	 * 
	 * @description 普通查询 
	 * @author tianmin.wang
	 * @date 2019年11月21日 
	 * @param dto
	 * @param page
	 * @param pageSize
	 * @param request
	 * @return ResponseData 结果集合
	 */
	@RequestMapping(value = "/hqm/8d/congratulate/team/select")
	@ResponseBody
	public ResponseData commit(DimensionCongratulateTeam dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
			@RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
		IRequest requestContext = createRequestContext(request);
		return new ResponseData(service.reSelect(requestContext, dto, page, pageSize));
	}

	/**
	 * 
	 * @description 团队庆祝提交入口
	 * @author tianmin.wang
	 * @date 2019年11月21日 
	 * @param dto
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/hqm/8d/congratulate/team/commit")
	@ResponseBody
	public ResponseData commit(DimensionCongratulateTeam dto, HttpServletRequest request) {
		IRequest requestContext = createRequestContext(request);
		ResponseData responseData = new ResponseData();
		try {
			responseData = service.commit(requestContext, dto);
		} catch (Exception e) {
			responseData.setSuccess(false);
			responseData.setMessage(e.getMessage());
		}
		return responseData;
	}

	/**
	 * 
	 * @description 数据提交入口
	 * @author tianmin.wang
	 * @date 2019年11月21日 
	 * @param dto
	 * @param result
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/hqm/8d/congratulate/team/submit")
	@ResponseBody
	public ResponseData update(@RequestBody List<DimensionCongratulateTeam> dto, BindingResult result,
			HttpServletRequest request) {
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
	 * 
	 * @description 删除入口
	 * @author tianmin.wang
	 * @date 2019年11月21日 
	 * @param request
	 * @param dto
	 * @return
	 */
	@RequestMapping(value = "/hqm/8d/congratulate/team/remove")
	@ResponseBody
	public ResponseData delete(HttpServletRequest request, @RequestBody List<DimensionCongratulateTeam> dto) {
		service.batchDelete(dto);
		return new ResponseData();
	}
}