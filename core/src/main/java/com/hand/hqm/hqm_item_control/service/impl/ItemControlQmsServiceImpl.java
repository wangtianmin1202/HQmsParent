package com.hand.hqm.hqm_item_control.service.impl;

import com.github.pagehelper.PageHelper;
import com.hand.hap.core.IRequest;
import com.hand.hap.core.annotation.StdWho;
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
import com.hand.hqm.hqm_item_control.dto.ItemControlQms;
import com.hand.hqm.hqm_item_control.mapper.ItemControlQmsMapper;
import com.hand.hqm.hqm_item_control.service.IItemControlQmsService;
import com.hand.hqm.hqm_item_control_his.dto.ItemControlHis;
import com.hand.hqm.hqm_item_control_his.service.IItemControlHisService;
import com.hand.hqm.hqm_platform_program.dto.PlatformProgram;
import com.hand.hqm.hqm_platform_program_his.dto.PlatformProgramHis;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.List;

import org.apache.ibatis.javassist.Modifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class ItemControlQmsServiceImpl extends BaseServiceImpl<ItemControlQms> implements IItemControlQmsService {
	private final static String MiddleString = "修改为";
	@Autowired
	ItemControlQmsMapper mapper;
	@Autowired
	IItemControlHisService iItemControlHisService;
	@Autowired
	IObjectEventsService iObjectEventsService;
	@Autowired
	IPromptService iPromptService;
	@Autowired
	ICodeService iCodeService;
	@Autowired
	PlantMapper plantMapper;
	@Autowired
	ItemBMapper itemBMapper;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.hand.hqm.hqm_item_control.service.IItemControlService#reSelect(com.hand.
	 * hap.core.IRequest, com.hand.hqm.hqm_item_control.dto.ItemControl, int, int)
	 */
	@Override
	public List<ItemControlQms> reSelect(IRequest requestContext, ItemControlQms dto, int page, int pageSize) {
		PageHelper.startPage(page, pageSize);
		return mapper.reSelect(dto);
	}

	@Override
	public List<ItemControlQms> reBatchUpdate(IRequest request, List<ItemControlQms> list) {
		for (ItemControlQms t : list) {
			switch (((BaseDTO) t).get__status()) {
			case DTOStatus.UPDATE:
				ItemControlQms search = new ItemControlQms();
				search.setPlantId(t.getPlantId());
				search.setItemId(t.getItemId());
				List<ItemControlQms> result = mapper.select(search);
				if (result == null || result.size() == 0) {// 新增这个工厂物料的数据
					self().insertSelective(request, t);
				} else {
					updateByUniqueKey(request, t);
				}
				break;
			default:
				break;
			}

		}
		return list;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.hand.hqm.hqm_item_control.service.IItemControlService#updateByUniqueKey(
	 * com.hand.hap.core.IRequest, com.hand.hqm.hqm_item_control.dto.ItemControl)
	 */
	@Override
	public void updateByUniqueKey(IRequest request, ItemControlQms t) {
		t.setLastUpdatedBy(request.getUserId());
		mapper.updateByUniqueKey(t);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.hand.hqm.hqm_item_control.service.IItemControlQmsService#newBatchUpdate(
	 * com.hand.hap.core.IRequest, java.util.List)
	 */
	@Override
	public List<ItemControlQms> newBatchUpdate(IRequest request, List<ItemControlQms> list) {
		for (ItemControlQms t : list) {
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hand.hqm.hqm_item_control.service.IItemControlQmsService#
	 * updateByPrimaryKeySelectiveRecord(com.hand.hap.core.IRequest,
	 * com.hand.hqm.hqm_item_control.dto.ItemControlQms)
	 */
	@Override
	public ItemControlQms updateByPrimaryKeySelectiveRecord(IRequest request, ItemControlQms t) {
		ItemControlQms now = mapper.selectByPrimaryKey(t);
		self().updateByPrimaryKeySelective(request, t);
		ItemControlQms after = mapper.selectByPrimaryKey(t);
		ItemControlHis his = new ItemControlHis();
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
		iItemControlHisService.insertSelective(request, his);
		objectEventsRecord(now, after, his, request);
		return t;
	}

	private void objectEventsRecord(ItemControlQms now, ItemControlQms after, ItemControlHis his, IRequest request) {
		ObjectEvents oe = new ObjectEvents();
		oe.setEventId(his.getEventId());
		oe.setEventTime(his.getEventTime());
		oe.setEventBy(his.getEventBy());
		oe.setEventTable("HQM_ITEM_CONTROL");
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
								"itemcontrolqms." + nowField.getName().toLowerCase()) + ":"
								+ changeInfoGetNumber(request, nowFloat, afterFloat, nowField.getName()) + "<br/>";
					}
				} else if (nowField.getType() == String.class) {
					String nowString = nowField.get(now) == null ? "" : (String) nowField.get(now);
					String afterString = afterField.get(after) == null ? "" : (String) afterField.get(after);
					if (!(nowString).equals(afterString)) {
						eventContent += SystemApiMethod.getPromptDescription(request, iPromptService,
								"itemcontrolqms." + nowField.getName().toLowerCase()) + ":"
								+ changeInfoGetString(request, nowString, afterString, nowField.getName()) + "<br/>";
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
		} else {
			str1 = String.valueOf(now.intValue());
			str2 = String.valueOf(after.intValue());
		}
		return str1 + MiddleString + str2;
	}

	private String changeInfoGetString(IRequest request, String now, String after, String fieldName) {
		String str1 = "";
		String str2 = "";
		if ("checkPlace".equals(fieldName)) {
			str1 = iCodeService.getCodeMeaningByValue(request, "HQM_CHECK_PLACE", now);
			str2 = iCodeService.getCodeMeaningByValue(request, "HQM_CHECK_PLACE", after);
		} else {
			str1 = now;
			str2 = after;
		}
		return str1 + MiddleString + str2;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hand.hqm.hqm_item_control.service.IItemControlQmsService#
	 * insertSelectiveRecord(com.hand.hap.core.IRequest,
	 * com.hand.hqm.hqm_item_control.dto.ItemControlQms)
	 */
	@Override
	public ItemControlQms insertSelectiveRecord(IRequest request, ItemControlQms t) {
		self().insertSelective(request, t);
		ItemControlQms after = mapper.selectByPrimaryKey(t);
		ItemControlHis his = new ItemControlHis();
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
		iItemControlHisService.insertSelective(request, his);
		return t;
	}

	private Float getPlantId(String plantCode) {
		Plant ser = new Plant();
		ser.setPlantCode(plantCode);
		return plantMapper.select(ser).get(0).getPlantId();
	}
}