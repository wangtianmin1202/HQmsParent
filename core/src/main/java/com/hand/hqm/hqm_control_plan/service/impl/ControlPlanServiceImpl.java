package com.hand.hqm.hqm_control_plan.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import com.hand.hqm.hqm_control_plan.dto.ControlPlan;
import com.hand.hqm.hqm_control_plan.mapper.ControlPlanMapper;
import com.hand.hqm.hqm_control_plan.service.IControlPlanService;

@Service
@Transactional(rollbackFor = Exception.class)
public class ControlPlanServiceImpl extends BaseServiceImpl<ControlPlan> implements IControlPlanService {

	@Autowired
	private ControlPlanMapper controlPlanMapper;
	
	
	@Override
	public List<ControlPlan> selectControlPlan(IRequest requestCtx,ControlPlan controlPlan, int page, int pageSize) {
		PageHelper.startPage(page, pageSize);
		return controlPlanMapper.selectControlPlan(controlPlan);
	}

	@Override
	public List<ControlPlan> save(IRequest requestCtx, List<ControlPlan> list) {

		List<ControlPlan> list1 =new ArrayList<ControlPlan>();
		
		list1 =self().batchUpdate(requestCtx, list);
		
		for (int i = 0; i < list1.size(); i++) {
			list1.get(i).setControlPlanCode(getSerialnum(list1.get(i).getControlPlanId()));
			self().updateByPrimaryKeySelective(requestCtx, list1.get(i));
		}
		return list1;//self().batchUpdate(requestCtx, list1)
	}

	@Override
	public int delete(List<ControlPlan> list) {
		for (int i = 0; i < list.size(); i++) {
			// 删除关联的特征表
			controlPlanMapper.deleteFeaturesByControlPlan(list.get(i).getControlPlanId());
		}
		
		//删除控制计划表
		self().batchDelete(list);
		
		return 0;
	}
	
	/**
	 *  生成 序列号
	 * @param id
	 * @return
	 */
	public String getSerialnum(Long id) {
		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
		String dateString = formatter.format(currentTime);
		//int x = (int) (Math.random() * 900) + 100;
		String serial = "KZ" + dateString.substring(2, 8) + id;
		return serial;

	}

	@Override
	public List<ControlPlan> commit(IRequest requestCtx, ControlPlan controlPlan) {
		// TODO Auto-generated method stub
		return null;
	}

}