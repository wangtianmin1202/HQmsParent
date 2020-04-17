package com.hand.hqm.hqm_measure_tool_his.service.impl;

import com.github.pagehelper.PageHelper;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import com.hand.hcs.hcs_suppliers.dto.Suppliers;
import com.hand.hcs.hcs_suppliers.mapper.SuppliersMapper;

import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hand.hqm.hqm_measure_tool.dto.MeasureTool;
import com.hand.hqm.hqm_measure_tool.mapper.MeasureToolMapper;
import com.hand.hqm.hqm_measure_tool_his.dto.MeasureToolHis;
import com.hand.hqm.hqm_measure_tool_his.dto.MeasureToolHisVO;
import com.hand.hqm.hqm_measure_tool_his.mapper.MeasureToolHisMapper;
import com.hand.hqm.hqm_measure_tool_his.service.IMeasureToolHisService;
import com.hand.hqm.hqm_msa_check_plan.mapper.MetCheckPlanLineMapper;

import oracle.net.aso.e;

import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class MeasureToolHisServiceImpl extends BaseServiceImpl<MeasureToolHis> implements IMeasureToolHisService{
	@Autowired
	private MeasureToolHisMapper mapper;
	@Autowired
	private MeasureToolMapper toolMapper;
	@Autowired
	private SuppliersMapper supplierdMapper;
	
	@Override
	public List<MeasureToolHis> query(IRequest requestContext, MeasureToolHis measureToolHis, int page, int pageSize) {
		PageHelper.startPage(page, pageSize);
		return mapper.query(measureToolHis);
	}

	@Override
	public List<MeasureToolHisVO> queryCheckType(IRequest requestContext, MeasureToolHis measureToolHis) {
		// TODO Auto-generated method stub
		List<MeasureToolHisVO> list = mapper.queryCheckType(measureToolHis);
		list.forEach(e -> {
			String checkType = e.getCheckType();
			if (isInteger(checkType)) {
				Suppliers suppliers = supplierdMapper.selectByPrimaryKey(checkType);
				e.setCheckType(suppliers.getSupplierName());
			}
		});
		return list;
	}
	
	//判断一个字符串是否为数字型
	public static boolean isInteger(String str) {  
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");  
        return pattern.matcher(str).matches();  
	}

	@Override
	public List<MeasureToolHis> queryCheckTypeGrid(IRequest requestContext, MeasureToolHis measureToolHis, int page,int pageSize) {
		// TODO Auto-generated method stub
		PageHelper.startPage(page, pageSize);
		List<MeasureToolHis> list = mapper.queryCheckTypeGrid(measureToolHis);
//		list.forEach( e -> {
//			String checkType = e.getCheckType();
//			switch (checkType) {
//			case "1":
//				e.setCheckType("上海科勒电子科技有限公司");
//				break;
//			case "2":
//				String checkedBy = e.getCheckedBy();
//				if (StringUtils.isNotBlank(checkedBy)) {
//					Suppliers suppliers = supplierdMapper.selectByPrimaryKey(checkedBy);
//					e.setCheckType(suppliers.getSupplierName());	
//				}
//			}
//		});
		return list;
	}

	@Override
	public List<MeasureToolHisVO> queryDepartmentUsage(IRequest requestContext, MeasureToolHis measureToolHis) {
		// TODO Auto-generated method stub
		return mapper.queryDepartmentUsage(measureToolHis);
	}

	@Override
	public List<MeasureToolHis> queryDepartmentUsageGrid(IRequest requestContext, MeasureToolHis measureToolHis,
			int page, int pageSize) {
		// TODO Auto-generated method stub
		PageHelper.startPage(page, pageSize);
		return mapper.queryDepartmentUsageGrid(measureToolHis);
	}

}