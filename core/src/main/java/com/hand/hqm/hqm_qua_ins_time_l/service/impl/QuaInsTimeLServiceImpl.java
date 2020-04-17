package com.hand.hqm.hqm_qua_ins_time_l.service.impl;

import com.github.pagehelper.PageHelper;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.service.impl.BaseServiceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hand.hqm.hqm_qua_ins_time_h.dto.QuaInsTimeH;
import com.hand.hqm.hqm_qua_ins_time_h.service.IQuaInsTimeHService;
import com.hand.hqm.hqm_qua_ins_time_l.dto.QuaInsTimeL;
import com.hand.hqm.hqm_qua_ins_time_l.mapper.QuaInsTimeLMapper;
import com.hand.hqm.hqm_qua_ins_time_l.service.IQuaInsTimeLService;
import com.hand.hqm.hqm_standard_op_ins_h.dto.StandardOpInspectionH;
import com.hand.hqm.hqm_standard_op_ins_h.service.IStandardOpInspectionHService;
import com.hand.hqm.hqm_standard_op_ins_l.dto.StandardOpInspectionL;
import com.hand.hqm.hqm_standard_op_ins_l.mapper.StandardOpInspectionLMapper;

import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class QuaInsTimeLServiceImpl extends BaseServiceImpl<QuaInsTimeL> implements IQuaInsTimeLService {

	@Autowired
	QuaInsTimeLMapper quaInsTimeLMapper;
	@Autowired
	private QuaInsTimeLMapper mapper;
	@Autowired
	IQuaInsTimeHService iQuaInsTimeHService;

	@Override
	public List<QuaInsTimeL> saveHeadLine(IRequest requestContext, List<QuaInsTimeL> dto) throws Exception {
		// 头操作

		QuaInsTimeH head = new QuaInsTimeH();
		head.setTimeId(dto.get(0).getTimeId());
		head.setPlantId(dto.get(0).getPlantId());
		head.setProdLineId(dto.get(0).getProdLineId());
		head.setShiftCode(dto.get(0).getShiftCode());
		head.setEnableFlag(dto.get(0).getEnableFlag());
		head.setInspectorId(dto.get(0).getInspectorId());
		head.setInspectionTime(dto.get(0).getInspectionTime());
		if (dto.get(0).getTimeId() == -1) {
			head = iQuaInsTimeHService.insertSelective(requestContext, head);
		} else {
			iQuaInsTimeHService.updateByPrimaryKeySelective(requestContext, head);
		}
		// 行操作
		for (QuaInsTimeL line : dto) {
			if (line.getLineId() == null) {
				// insert
				line.setTimeId(head.getTimeId());
				self().insertSelective(requestContext, line);
			} else {
				// update
				self().updateByPrimaryKeySelective(requestContext, line);
			}
		}
		return dto;
	}

	@Override
	public List<QuaInsTimeL> myselect(IRequest requestContext, QuaInsTimeL dto, int page, int pageSize) {
		PageHelper.startPage(page, pageSize);
		return mapper.myselect(dto);
	}

	@Override
	public List<QuaInsTimeL> groupbyselect(QuaInsTimeL dto) {
		return mapper.groupbyselect(dto);
	}
}