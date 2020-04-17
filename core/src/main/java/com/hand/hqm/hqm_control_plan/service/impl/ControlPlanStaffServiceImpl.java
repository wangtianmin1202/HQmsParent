package com.hand.hqm.hqm_control_plan.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import com.hand.hqm.hqm_control_plan.dto.ControlPlan;
import com.hand.hqm.hqm_control_plan.dto.ControlPlanStaff;
import com.hand.hqm.hqm_control_plan.service.IControlPlanService;
import com.hand.hqm.hqm_control_plan.service.IControlPlanStaffService;

@Service
@Transactional(rollbackFor = Exception.class)
public class ControlPlanStaffServiceImpl extends BaseServiceImpl<ControlPlanStaff> implements IControlPlanStaffService {
	@Autowired
	private IControlPlanStaffService service;

	@Autowired
	private IControlPlanService controlPlanService;

	@Override
	public ResponseData save(List<ControlPlanStaff> dto, IRequest requestCtx) {
		ResponseData responseData = new ResponseData();
		Boolean flag = false;
		for (int i = 0; i < dto.size(); i++) {
			if ("add".equals(dto.get(i).get__status())) {
				flag = true;
			}
		}
		if (flag) {
			if (checkStaff(dto, requestCtx,responseData).isSuccess()) {
				responseData.setSuccess(true);
				responseData.setRows(service.batchUpdate(requestCtx, dto));
				// 更新主要参与人员到controlPlan
				List<ControlPlanStaff> dtos = new ArrayList<ControlPlanStaff>();
				ControlPlan controlPlan = new ControlPlan();
				String selectedid = "";
				String selectedName = "";
				ControlPlanStaff controlPlanStaff = new ControlPlanStaff();
				controlPlanStaff.setControlPlanId(dto.get(0).getControlPlanId());
				dtos = service.select(requestCtx, controlPlanStaff, 0, 0);
				for (int j = 0; j < dtos.size(); j++) {
					selectedid = selectedid + "," + String.valueOf(dtos.get(j).getEmployeeId());
					selectedName = selectedName + "," + dtos.get(j).getEmployeeName();
				}
				controlPlan.setControlPlanId(dto.get(0).getControlPlanId());
				controlPlan.setMainStaffId(selectedid.substring(1, selectedid.length()));
				controlPlan.setMainStaff(selectedName.substring(1, selectedName.length()));
				controlPlanService.updateByPrimaryKeySelective(requestCtx, controlPlan);
			}
		} else {
			responseData.setSuccess(true);
			responseData.setRows(service.batchUpdate(requestCtx, dto));
			// 更新主要参与人员到controlPlan
			List<ControlPlanStaff> dtos = new ArrayList<ControlPlanStaff>();
			ControlPlan controlPlan = new ControlPlan();
			String selectedid = "";
			String selectedName = "";
			ControlPlanStaff controlPlanStaff = new ControlPlanStaff();
			controlPlanStaff.setControlPlanId(dto.get(0).getControlPlanId());
			dtos = service.select(requestCtx, controlPlanStaff, 0, 0);
			for (int j = 0; j < dtos.size(); j++) {
				selectedid = selectedid + "," + String.valueOf(dtos.get(j).getEmployeeId());
				selectedName = selectedName + "," + dtos.get(j).getEmployeeName();
			}
			controlPlan.setControlPlanId(dto.get(0).getControlPlanId());
			controlPlan.setMainStaffId(selectedid.substring(1, selectedid.length()));
			controlPlan.setMainStaff(selectedName.substring(1, selectedName.length()));
			controlPlanService.updateByPrimaryKeySelective(requestCtx, controlPlan);
		}
		return responseData;
	}

	public ResponseData checkStaff(List<ControlPlanStaff> dto, IRequest requestCtx,ResponseData responseData ) {

		List<String> list = new ArrayList<String>();
		for (int i = 0; i < dto.size(); i++) {
			if ("add".equals(dto.get(i).get__status())) {
				list.add(dto.get(i).getEmployeeName());
				ControlPlanStaff controlPlanStaff = new ControlPlanStaff();
				controlPlanStaff.setControlPlanId(dto.get(i).getControlPlanId());
				controlPlanStaff.setEmployeeId(dto.get(i).getEmployeeId());
				controlPlanStaff.setEmployeeName(dto.get(i).getEmployeeName());
				if (service.select(requestCtx, controlPlanStaff, 0, 0).size() > 0) {
					responseData.setSuccess(false);
					responseData.setMessage("当前人员已存在，请重新选择人员！");
					return responseData;
				}
			}
		}

		Set<String> set = new HashSet<String>(list);
		boolean results = list.size() == set.size() ? true : false;
		if (!results) {
			responseData.setSuccess(false);
			responseData.setMessage("人员重复，请重新选择！");
			return responseData;
		}
		return responseData;

	}

}