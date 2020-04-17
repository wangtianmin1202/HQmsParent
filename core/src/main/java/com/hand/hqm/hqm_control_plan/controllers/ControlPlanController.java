package com.hand.hqm.hqm_control_plan.controllers;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hand.hap.core.IRequest;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hqm.hqm_control_plan.dto.ControlPlan;
import com.hand.hqm.hqm_control_plan.dto.ControlPlanHis;
import com.hand.hqm.hqm_control_plan.service.IControlPlanHisService;
import com.hand.hqm.hqm_control_plan.service.IControlPlanService;

@Controller
public class ControlPlanController extends BaseController {

	@Autowired
	private IControlPlanService service;

	@Autowired
	private IControlPlanHisService controlPlanService;

	@RequestMapping(value = "/hqm/control/plan/query")
	@ResponseBody
	public ResponseData query(ControlPlan dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
			@RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
		IRequest requestContext = createRequestContext(request);
		return new ResponseData(service.selectControlPlan(requestContext, dto, page, pageSize));
	}

	@RequestMapping(value = "/hqm/control/plan/submit")
	@ResponseBody
	public ResponseData update(@RequestBody List<ControlPlan> dto, BindingResult result, HttpServletRequest request) {
		getValidator().validate(dto, result);
		if (result.hasErrors()) {
			ResponseData responseData = new ResponseData(false);
			responseData.setMessage(getErrorMessage(result, request));
			return responseData;
		}
		IRequest requestCtx = createRequestContext(request);
		
		//更新人员表选择状态		
		/*for(int i=0;i<dto.size();i++) {
			List<String> listMainstaff = Arrays.asList(dto.get(i).getMainStaffId().split(","));
			for(int j=0;j<listMainstaff.size();j++) {
				hREmployeeMapper.updateN(listMainstaff);
				hREmployeeMapper.updateY(listMainstaff);
				
			}
		}*/
		return new ResponseData(service.save(requestCtx, dto));// batchUpdate(requestCtx, dto)
	}

	@RequestMapping(value = "/hqm/control/plan/commit")
	@ResponseBody
	public ResponseData commit(ControlPlan dto, HttpServletRequest request) {
		IRequest requestCtx = createRequestContext(request);

		// 更新版本记录表
		ControlPlanHis controlPlanHis = new ControlPlanHis();
		controlPlanHis.setControlPlanId(dto.getControlPlanId());
		controlPlanHis.setControlVersion(dto.getControlVersion());
		controlPlanHis.setControlVersionTime(dto.getLastUpdateDate());
		controlPlanService.insertSelective(requestCtx, controlPlanHis);
		
		//更新控制计划版本号
		dto.setControlVersion(dto.getControlVersion() + 10);
		service.updateByPrimaryKeySelective(requestCtx, dto);
		
		return new ResponseData();// batchUpdate(requestCtx, dto)
	}

	@RequestMapping(value = "/hqm/control/plan/remove")
	@ResponseBody
	public ResponseData delete(HttpServletRequest request, @RequestBody List<ControlPlan> dto) {
		service.delete(dto);// batchDelete
		return new ResponseData();
	}
}