package com.hand.hqm.hqm_supp_item_exemption.service.impl;

import com.github.pagehelper.PageHelper;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.BaseDTO;
import com.hand.hap.system.dto.DTOStatus;
import com.hand.hap.system.service.ICodeService;
import com.hand.hap.system.service.IPromptService;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import com.hand.hap.util.SystemApiMethod;
import com.hand.hcm.hcm_item_b.dto.ItemB;
import com.hand.hcm.hcm_item_b.mapper.ItemBMapper;
import com.hand.hcm.hcm_object_events.dto.ObjectEvents;
import com.hand.hcm.hcm_object_events.service.IObjectEventsService;
import com.hand.hcm.hcm_plant.dto.Plant;
import com.hand.hcm.hcm_plant.mapper.PlantMapper;
import com.hand.hcs.hcs_suppliers.mapper.SuppliersMapper;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.ibatis.javassist.Modifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

//import com.hand.hqm.hqm_supp_item_exemption.dto.SuppItemExemptionController;
import com.hand.hqm.hqm_supp_item_exemption.dto.SuppItemExemption;
import com.hand.hqm.hqm_supp_item_exemption.mapper.SuppItemExemptionMapper;
import com.hand.hqm.hqm_supp_item_exemption.service.ISuppItemExemptionService;
import com.hand.hqm.hqm_supp_item_exemption_his.dto.SuppItemExemptionHis;
import com.hand.hqm.hqm_supp_item_exemption_his.service.ISuppItemExemptionHisService;
import com.hand.hqm.hqm_supplier_inspector_rel.dto.SupplierInspectorRel;
import com.hand.hqm.hqm_supplier_inspector_rel_his.dto.SupplierInspectorRelHis;

import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class SuppItemExemptionServiceImpl extends BaseServiceImpl<SuppItemExemption>
		implements ISuppItemExemptionService {
	private String MiddleString = " 修改为 ";
	@Autowired
	SuppItemExemptionMapper suppItemExemptionMapper;
	@Autowired
	ISuppItemExemptionHisService iSuppItemExemptionHisService;
	@Autowired
	IPromptService iPromptService;
	@Autowired
	IObjectEventsService iObjectEventsService;
	@Autowired
	SuppliersMapper suppliersMapper;
	@Autowired
	ICodeService iCodeService;
	@Autowired
	PlantMapper plantMapper;
	@Autowired
	ItemBMapper itemBMapper;

	@Override
	public List<SuppItemExemption> myselect(IRequest requestContext, SuppItemExemption dto, int page, int pageSize) {
		PageHelper.startPage(page, pageSize);
		return suppItemExemptionMapper.myselect(dto);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hand.hqm.hqm_supp_item_exemption.service.ISuppItemExemptionService#
	 * reBatchUpdate(com.hand.hap.core.IRequest, java.util.List)
	 */
	@Override
	public List<SuppItemExemption> reBatchUpdate(IRequest request, List<SuppItemExemption> list) {
		for (SuppItemExemption t : list) {
			switch (((BaseDTO) t).get__status()) {
			case DTOStatus.ADD:
				insertSelectiveRecord(request, t);
				break;
			case DTOStatus.UPDATE:
				if (useSelectiveUpdate()) {
					updateByPrimaryKeySelectiveRecord(request, t);
				} else {
					self().updateByPrimaryKey(request, t);
				}
				break;
			case DTOStatus.DELETE:
				self().deleteByPrimaryKey(t);
				break;
			default:
				break;
			}
		}
		return list;
	}

	@Override
	public SuppItemExemption updateByPrimaryKeySelectiveRecord(IRequest request, SuppItemExemption t) {
		SuppItemExemption now = mapper.selectByPrimaryKey(t);
		self().updateByPrimaryKeySelective(request, t);
		SuppItemExemption after = mapper.selectByPrimaryKey(t);
		SuppItemExemptionHis his = new SuppItemExemptionHis();
		for (Field hisField : his.getClass().getDeclaredFields()) {
			if ("eventId".equals(hisField.getName()) || "eventBy".equals(hisField.getName())
					|| "eventTime".equals(hisField.getName()))
				continue;
			if (hisField.isAnnotationPresent(javax.persistence.Transient.class))
				continue;
			try {
				hisField.setAccessible(true);
				Field afterField = after.getClass().getDeclaredField(hisField.getName());
				afterField.setAccessible(true);
				if (afterField.get(after) == null)
					continue;
				hisField.set(his, afterField.get(after));
			} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		his.setEventBy(request.getUserId());
		his.setEventTime(new Date());
		iSuppItemExemptionHisService.insertSelective(request, his);
		objectEventsRecord(now, after, his, request);
		return t;
	}

	/**
	 * 
	 * @description 由 什么 变为 什么的记录 的方法
	 * @author tianmin.wang
	 * @date 2020年3月4日
	 * @param now
	 * @param after
	 * @param his
	 * @param request
	 */
	private void objectEventsRecord(SuppItemExemption now, SuppItemExemption after, SuppItemExemptionHis his,
			IRequest request) {
		ObjectEvents oe = new ObjectEvents();
		oe.setEventId(his.getEventId());
		oe.setEventTime(his.getEventTime());
		oe.setEventBy(his.getEventBy());
		oe.setEventTable("HQM_SUPP_ITEM_EXEMPTION");
		String eventContent = "";
		Field[] nowFields = now.getClass().getDeclaredFields();
		for (Field nowField : nowFields) {
			try {
				if (nowField.isAnnotationPresent(javax.persistence.Transient.class)
						|| nowField.isAnnotationPresent(javax.persistence.Id.class)
						|| Modifier.isFinal(nowField.getModifiers()) || Modifier.isStatic(nowField.getModifiers()))
					continue;
				nowField.setAccessible(true);
				Field afterField = after.getClass().getDeclaredField(nowField.getName());
				afterField.setAccessible(true);
				if (nowField.getType() == float.class || nowField.getType() == Float.class) {
					Float nowFloat = nowField.get(now) == null ? 0 : (Float) nowField.get(now);
					Float afterFloat = afterField.get(after) == null ? 0 : (Float) afterField.get(after);
					if (nowFloat.floatValue() != afterFloat.floatValue()) {
						eventContent += SystemApiMethod.getPromptDescription(request, iPromptService,
								"suppitemexemption." + nowField.getName().toLowerCase()) + ":"
								+ changeInfoGetNumber(request, nowFloat, afterFloat, nowField.getName()) + "<br/>";
					}
				} else if (nowField.getType() == String.class) {
					String nowString = nowField.get(now) == null ? "" : (String) nowField.get(now);
					String afterString = afterField.get(after) == null ? "" : (String) afterField.get(after);
					if (!(nowString).equals(afterString)) {
						eventContent += SystemApiMethod.getPromptDescription(request, iPromptService,
								"suppitemexemption." + nowField.getName().toLowerCase()) + ":"
								+ changeInfoGetString(request, nowString, afterString, nowField.getName()) + "<br/>";
					}
				} else if (nowField.getType() == Date.class) {
					Date nowDate = new Date(0);
					Date nowString = nowField.get(now) == null ? nowDate : (Date) nowField.get(now);
					Date afterString = afterField.get(after) == null ? nowDate : (Date) afterField.get(after);
					if (nowString.compareTo(afterString) != 0) {
						eventContent += SystemApiMethod.getPromptDescription(request, iPromptService,
								"suppitemexemption." + nowField.getName().toLowerCase()) + ":"
								+ changeInfoGetDate(request, nowString, afterString, nowField.getName()) + "<br/>";
					}
				} else {
					continue;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		oe.setEventContent(eventContent);
		iObjectEventsService.insertSelective(request, oe);
	}

	private String changeInfoGetNumber(IRequest request, Float now, Float after, String fieldName) {
		String str1 = "";
		String str2 = "";
		if ("itemId".equals(fieldName)) {
			Float plantId = getPlantId("CNKE");
			ItemB ser = new ItemB();
			ser.setPlantId(plantId);
			ser.setItemId(now);
			ItemB itembNow = itemBMapper.reSelect(ser).get(0);
			str1 = itembNow.getItemCode() + ":" + itembNow.getItemDescriptions();
			ser.setItemId(after);
			ItemB itembAfter = itemBMapper.reSelect(ser).get(0);
			str2 = itembAfter.getItemCode() + ":" + itembAfter.getItemDescriptions();
		} else if ("supplierId".equals(fieldName)) {
			str1 = suppliersMapper.getSupplierNameById(now);
			str2 = suppliersMapper.getSupplierNameById(after);
		} else {
			str1 = String.valueOf(now.intValue());
			str2 = String.valueOf(after.intValue());
		}
		return str1 + MiddleString + str2;
	}

	private String changeInfoGetString(IRequest request, String now, String after, String fieldName) {
		String str1 = "";
		String str2 = "";
		if ("exemptionFlag".equals(fieldName)) {// HQM_EXEMPTION_FLAG
			str1 = iCodeService.getCodeMeaningByValue(request, "HQM_EXEMPTION_FLAG", now);
			str2 = iCodeService.getCodeMeaningByValue(request, "HQM_EXEMPTION_FLAG", after);
		} else {
			str1 = now;
			str2 = after;
		}
		return str1 + MiddleString + str2;
	}

	private String changeInfoGetDate(IRequest request, Date now, Date after, String fieldName) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String str1 = "";
		String str2 = "";
		str1 = sdf.format(now);
		str2 = sdf.format(after);
		return str1 + MiddleString + str2;
	}

	@Override
	public SuppItemExemption insertSelectiveRecord(IRequest request, SuppItemExemption t) {
		self().insertSelective(request, t);
		SuppItemExemption after = mapper.selectByPrimaryKey(t);
		SuppItemExemptionHis his = new SuppItemExemptionHis();
		for (Field hisField : his.getClass().getDeclaredFields()) {
			if ("eventId".equals(hisField.getName()) || "eventBy".equals(hisField.getName())
					|| "eventTime".equals(hisField.getName()))
				continue;
			if (hisField.isAnnotationPresent(javax.persistence.Transient.class))
				continue;
			try {
				hisField.setAccessible(true);
				Field afterField = after.getClass().getDeclaredField(hisField.getName());
				afterField.setAccessible(true);
				hisField.set(his, afterField.get(after));
			} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		his.setEventBy(request.getUserId());
		his.setEventTime(new Date());
		iSuppItemExemptionHisService.insertSelective(request, his);
		return t;
	}

	private Float getPlantId(String plantCode) {
		Plant ser = new Plant();
		ser.setPlantCode(plantCode);
		return plantMapper.select(ser).get(0).getPlantId();
	}
}