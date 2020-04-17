package com.hand.hcm.hcm_object_events.service.impl;

import com.hand.hap.core.IRequest;
import com.hand.hap.system.service.impl.BaseServiceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.hand.hcm.hcm_object_events.dto.ObjectEvents;
import com.hand.hcm.hcm_object_events.mapper.ObjectEventsMapper;
import com.hand.hcm.hcm_object_events.service.IObjectEventsService;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class ObjectEventsServiceImpl extends BaseServiceImpl<ObjectEvents> implements IObjectEventsService {

	@Autowired
	ObjectEventsMapper mapper;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.hand.hcm.hcm_object_events.service.IObjectEventsService#query(com.hand.
	 * hap.core.IRequest, com.hand.hcm.hcm_object_events.dto.ObjectEvents,
	 * java.lang.String)
	 */
	@Override
	public List<ObjectEvents> query(IRequest requestContext, ObjectEvents dto, String tableName) {
		// TODO Auto-generated method stub
		switch (tableName) {
		case "supplier_inspector_rel":
			return mapper.selectSupplierInspectorRel(dto);
		case "platform_program":
			return mapper.selectPlatformProgram(dto);
		case "item_control":
			return mapper.selectItemControl(dto);
		case "pqc_warning":
			return mapper.selectPqcWarning(dto);
		case "inspection_attribute":
			return mapper.selectInspectionAttribute(dto);
		case "online_sku_rule":
			return mapper.selectOnlineSkuRule(dto);
		case "program_sku_rel":
			return mapper.selectProgramSkuRel(dto);
		case "supp_item_exemption":
			return mapper.selectSuppItemExemption(dto);
		case "fqc_sample_switch":
			return mapper.selectFqcSampleSwitch(dto);
		case "sample_switch_rule":
			return mapper.selectSampleSwitchRule(dto);
		case "inspection_template"://IFQC检验单模板
			return mapper.selectInspectionTemplate(dto);
		case "standard_op_item"://PQC检验单模板
			return mapper.selectStandardOpItem(dto);
		case "item_type_tests"://
			return mapper.selectItemTypeTests(dto);
		default:
			return null;
		}
	}

}