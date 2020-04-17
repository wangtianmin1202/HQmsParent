package com.hand.hqm.hqm_standard_op_ins_h.service.impl;

import com.github.pagehelper.PageHelper;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.service.IBaseService;
import com.hand.hap.system.service.impl.BaseServiceImpl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hand.hqm.hqm_iqc_inspection_template_h.dto.IqcInspectionTemplateH;
import com.hand.hqm.hqm_standard_op_ins_h.dto.StandardOpInspectionH;
import com.hand.hqm.hqm_standard_op_ins_h.mapper.StandardOpInspectionHMapper;
import com.hand.hqm.hqm_standard_op_ins_h.service.IStandardOpInspectionHService;
import com.hand.hqm.hqm_standard_op_ins_l.dto.StandardOpInspectionL;
import com.hand.hqm.hqm_standard_op_ins_l.mapper.StandardOpInspectionLMapper;
import com.hand.hqm.hqm_standard_op_ins_l.service.IStandardOpInspectionLService;

import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class StandardOpInspectionHServiceImpl extends BaseServiceImpl<StandardOpInspectionH>
		implements IStandardOpInspectionHService {
	@Autowired
	StandardOpInspectionHMapper standardOpInspectionHMapper;
	@Autowired
	IStandardOpInspectionHService StandardOpInspectionHService;
	@Autowired
	IStandardOpInspectionLService StandardOpInspectionLService;
	@Autowired
	StandardOpInspectionLMapper standardOpInspectionLMapper;

	@Override
	public List<StandardOpInspectionH> myselect(IRequest requestContext, StandardOpInspectionH dto, int page,
			int pageSize) {
		PageHelper.startPage(page, pageSize);
		return standardOpInspectionHMapper.myselect(dto);

	}

	@Override
	public int reBatchDelete(List<StandardOpInspectionH> list) {

		int c = 0;
		for (StandardOpInspectionH t : list) {
			StandardOpInspectionL search = new StandardOpInspectionL();
			search.setHeadId(t.getHeadId());
			List<StandardOpInspectionL> lineList = standardOpInspectionLMapper.select(search);
			StandardOpInspectionLService.batchDelete(lineList);
			c += self().deleteByPrimaryKey(t);
		}
		return c;

	}

	@Override
	public List<StandardOpInspectionH> save(IRequest requestContext, StandardOpInspectionH dto) {
		// TODO Auto-generated method stub
		List<StandardOpInspectionH> result = new ArrayList<>();
		if (dto.getHeadId().intValue() == -1) {
			// 新增
			result.add(self().insertSelective(requestContext, dto));
		} else {
			// 更新
			result.add(self().updateByPrimaryKeySelective(requestContext, dto));
		}
		StandardOpInspectionH Search = new StandardOpInspectionH();
		Search.setHeadId(result.get(0).getHeadId());
		return standardOpInspectionHMapper.myselect(Search);

	}
}