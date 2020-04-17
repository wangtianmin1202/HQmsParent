package com.hand.hqm.hqm_fqc_sample_switch.service.impl;

import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.BaseDTO;
import com.hand.hap.system.dto.DTOStatus;
import com.hand.hap.system.service.ICodeService;
import com.hand.hap.system.service.IPromptService;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import com.hand.hap.util.SystemApiMethod;
import com.hand.hcm.hcm_object_events.dto.ObjectEvents;
import com.hand.hcm.hcm_object_events.service.IObjectEventsService;
import java.lang.reflect.Field;
import java.util.Date;
import java.util.List;
import org.apache.ibatis.javassist.Modifier;
import org.drools.core.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.hand.hqm.hqm_fqc_sample_switch.dto.FqcSampleSwitch;
import com.hand.hqm.hqm_fqc_sample_switch.mapper.FqcSampleSwitchMapper;
import com.hand.hqm.hqm_fqc_sample_switch.service.IFqcSampleSwitchService;
import com.hand.hqm.hqm_fqc_sample_switch_his.dto.FqcSampleSwitchHis;
import com.hand.hqm.hqm_fqc_sample_switch_his.service.IFqcSampleSwitchHisService;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class FqcSampleSwitchServiceImpl extends BaseServiceImpl<FqcSampleSwitch> implements IFqcSampleSwitchService {
	private String MiddleString = " 修改为 ";
	@Autowired
	FqcSampleSwitchMapper mapper;
	@Autowired
	IFqcSampleSwitchHisService iFqcSampleSwitchHisService;
	@Autowired
	IObjectEventsService iObjectEventsService;
	@Autowired
	ICodeService iCodeService;
	@Autowired
	IPromptService iPromptService;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hand.hqm.hqm_fqc_sample_switch.service.IFqcSampleSwitchService#
	 * reBatchUpdate(com.hand.hap.core.IRequest, java.util.List)
	 */
	@Override
	public List<FqcSampleSwitch> reBatchUpdate(IRequest request, List<FqcSampleSwitch> list) {
		for (FqcSampleSwitch t : list) {
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
	public FqcSampleSwitch updateByPrimaryKeySelectiveRecord(IRequest request, FqcSampleSwitch t) {
		FqcSampleSwitch now = mapper.selectByPrimaryKey(t);
		self().updateByPrimaryKeySelective(request, t);
		FqcSampleSwitch after = mapper.selectByPrimaryKey(t);
		FqcSampleSwitchHis his = new FqcSampleSwitchHis();
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
		iFqcSampleSwitchHisService.insertSelective(request, his);
		objectEventsRecord(now, after, his, request);
		return t;
	}

	private void objectEventsRecord(FqcSampleSwitch now, FqcSampleSwitch after, FqcSampleSwitchHis his,
			IRequest request) {
		ObjectEvents oe = new ObjectEvents();
		oe.setEventId(his.getEventId());
		oe.setEventTime(his.getEventTime());
		oe.setEventBy(his.getEventBy());
		oe.setEventTable("HQM_FQC_SAMPLE_SWITCH");
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
								"fqcsampleswitch." + nowField.getName().toLowerCase()) + ":"
								+ changeInfoGetNumber(request, nowFloat, afterFloat, nowField.getName()) + "<br/>";
					}
				} else if (nowField.getType() == String.class) {
					String nowString = nowField.get(now) == null ? "" : (String) nowField.get(now);
					String afterString = afterField.get(after) == null ? "" : (String) afterField.get(after);
					if (!(nowString).equals(afterString)) {
						eventContent += SystemApiMethod.getPromptDescription(request, iPromptService,
								"fqcsampleswitch." + nowField.getName().toLowerCase()) + ":"
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
		if (!StringUtils.isEmpty(eventContent))
			iObjectEventsService.insertSelective(request, oe);
	}

	private String changeInfoGetNumber(IRequest request, Float now, Float after, String fieldName) {
		String str1 = "";
		String str2 = "";
		str1 = String.valueOf(now.intValue());
		str2 = String.valueOf(after.intValue());
		return str1 + MiddleString + str2;
	}

	private String changeInfoGetString(IRequest request, String now, String after, String fieldName) {
		String str1 = "";
		String str2 = "";
		str1 = now;
		str2 = after;
		return str1 + MiddleString + str2;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.hand.hqm.hqm_pqc_warning.service.IPqcWarningService#insertSelectiveRecord
	 * (com.hand.hap.core.IRequest, com.hand.hqm.hqm_pqc_warning.dto.PqcWarning)
	 */
	@Override
	public FqcSampleSwitch insertSelectiveRecord(IRequest request, FqcSampleSwitch t) {
		self().insertSelective(request, t);
		FqcSampleSwitch after = mapper.selectByPrimaryKey(t);
		FqcSampleSwitchHis his = new FqcSampleSwitchHis();
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
		iFqcSampleSwitchHisService.insertSelective(request, his);
		return t;
	}

}