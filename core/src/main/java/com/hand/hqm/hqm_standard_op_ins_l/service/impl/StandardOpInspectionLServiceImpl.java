package com.hand.hqm.hqm_standard_op_ins_l.service.impl;

import com.github.pagehelper.PageHelper;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.system.service.impl.BaseServiceImpl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hand.hqm.hqm_sam_use_order_h.service.ISamUseOrderHService;
import com.hand.hqm.hqm_sam_use_order_l.dto.SamUseOrderL;
import com.hand.hqm.hqm_sam_use_order_l.mapper.SamUseOrderLMapper;
import com.hand.hqm.hqm_standard_op_ins_h.dto.StandardOpInspectionH;
import com.hand.hqm.hqm_standard_op_ins_h.service.IStandardOpInspectionHService;
import com.hand.hqm.hqm_standard_op_ins_l.dto.StandardOpInspectionL;
import com.hand.hqm.hqm_standard_op_ins_l.mapper.StandardOpInspectionLMapper;
import com.hand.hqm.hqm_standard_op_ins_l.service.IStandardOpInspectionLService;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class StandardOpInspectionLServiceImpl extends BaseServiceImpl<StandardOpInspectionL>
		implements IStandardOpInspectionLService {

	@Autowired
	StandardOpInspectionLMapper standardOpInspectionLMapper;
	@Autowired
	private StandardOpInspectionLMapper mapper;
	@Autowired
	IStandardOpInspectionHService standardOpInspectionHService;

	@Override
	public List<StandardOpInspectionL> saveHeadLine(IRequest requestContext, List<StandardOpInspectionL> dto)
			throws Exception {
		// 头操作
		List<StandardOpInspectionL> returnList = new ArrayList<>();
		StandardOpInspectionH head = new StandardOpInspectionH();
		head.setPlantId(dto.get(0).getPlantId());
		head.setProdLineId(dto.get(0).getProdLineId());
		head.setStandardOpId(dto.get(0).getStandardOpId());
		head.setWorkstationId(dto.get(0).getWorkstationId());
		head.setVersion(dto.get(0).getVersion());
		head.setSourceType(dto.get(0).getSourceType());
		List<StandardOpInspectionH> headList = standardOpInspectionHService.myselect(requestContext, head, 0, 0);
		head.setEnableFlag(dto.get(0).getHeadEnableFlag());
		if (dto.get(0).getHeadId() == null) {
			standardOpInspectionHService.insertSelective(requestContext, head);
		}
		// 行操作
		for (StandardOpInspectionL line : dto) {
			StandardOpInspectionL lineData = new StandardOpInspectionL();
			lineData.setHeadId(head.getHeadId());
			lineData.setAttributeId(line.getAttributeId());
			lineData.setEnableFlag(line.getEnableFlag());
			if (lineData.getAttributeId() == null && lineData.getEnableFlag() == null) {
				throw new Exception("存在未输入的必输项:检验项目/有效性");
			} else {
				self().insertSelective(requestContext, lineData);
				returnList.add(lineData);
			}
		}
		return returnList;
	}

	@Override
	public List<StandardOpInspectionL> myselect(IRequest requestContext, StandardOpInspectionL dto, int page,
			int pageSize) {
		PageHelper.startPage(page, pageSize);
		return mapper.myselect(dto);
	}
}