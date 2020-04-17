package com.hand.hqm.hqm_platform_program.service.impl;

import com.github.pagehelper.PageHelper;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.hand.hqm.hqm_platform_program.dto.PlatformProgram;
import com.hand.hqm.hqm_platform_program.mapper.PlatformProgramMapper;
import com.hand.hqm.hqm_platform_program.service.IPlatformProgramService;
import com.hand.hqm.hqm_platform_program_his.dto.PlatformProgramHis;
import com.hand.hqm.hqm_platform_program_his.service.IPlatformProgramHisService;
import com.hand.hqm.hqm_supplier_inspector_rel.dto.SupplierInspectorRel;
import com.hand.hqm.hqm_supplier_inspector_rel_his.dto.SupplierInspectorRelHis;

import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class PlatformProgramServiceImpl extends BaseServiceImpl<PlatformProgram> implements IPlatformProgramService {
	private final static String MiddleString = " 修改为 ";
	@Autowired
	PlatformProgramMapper mapper;
	@Autowired
	IPlatformProgramHisService iPlatformProgramHisService;
	@Autowired
	IObjectEventsService iObjectEventsService;
	@Autowired
	IPromptService iPromptService;
	@Autowired
	ICodeService iCodeService;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.hand.hqm.hqm_platform_program.service.IPlatformProgramService#reSelect(
	 * com.hand.hap.core.IRequest,
	 * com.hand.hqm.hqm_platform_program.dto.PlatformProgram, int, int)
	 */
	@Override
	public List<PlatformProgram> reSelect(IRequest requestContext, PlatformProgram dto, int page, int pageSize) {
		PageHelper.startPage(page, pageSize);
		return mapper.reSelect(dto);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hand.hqm.hqm_platform_program.service.IPlatformProgramService#
	 * reBatchUpdate(com.hand.hap.core.IRequest, java.util.List)
	 */
	@Override
	public List<PlatformProgram> reBatchUpdate(IRequest request, List<PlatformProgram> list) {
		for (PlatformProgram t : list) {
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
	 * @see com.hand.hqm.hqm_platform_program.service.IPlatformProgramService#
	 * updateByPrimaryKeySelectiveRecord(com.hand.hap.core.IRequest,
	 * com.hand.hqm.hqm_platform_program.dto.PlatformProgram)
	 */
	@Override
	public PlatformProgram updateByPrimaryKeySelectiveRecord(IRequest request, PlatformProgram t) {
		PlatformProgram now = mapper.selectByPrimaryKey(t);
		self().updateByPrimaryKeySelective(request, t);
		PlatformProgram after = mapper.selectByPrimaryKey(t);
		PlatformProgramHis his = new PlatformProgramHis();
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
		iPlatformProgramHisService.insertSelective(request, his);
		objectEventsRecord(now, after, his, request);
		return t;
	}

	private void objectEventsRecord(PlatformProgram now, PlatformProgram after, PlatformProgramHis his,
			IRequest request) {
		ObjectEvents oe = new ObjectEvents();
		oe.setEventId(his.getEventId());
		oe.setEventTime(his.getEventTime());
		oe.setEventBy(his.getEventBy());
		oe.setEventTable("HQM_PLATFORM_PROGRAM");
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
								"platformprogram." + nowField.getName().toLowerCase()) + ":"
								+ changeInfoGetNumber(request, nowFloat, afterFloat, nowField.getName()) + "<br/>";
					}
				} else if (nowField.getType() == String.class) {
					String nowString = nowField.get(now) == null ? "" : (String) nowField.get(now);
					String afterString = afterField.get(after) == null ? "" : (String) afterField.get(after);
					if (!(nowString).equals(afterString)) {
						eventContent += SystemApiMethod.getPromptDescription(request, iPromptService,
								"platformprogram." + nowField.getName().toLowerCase()) + ":"
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
		return str1 + MiddleString + str2;
	}

	private String changeInfoGetString(IRequest request, String now, String after, String fieldName) {
		String str1 = "";
		String str2 = "";
		if ("programType".equals(fieldName)) {
			str1 = iCodeService.getCodeMeaningByValue(request, "HQM_INSPECTION_ATTRIBUTE_TYPE", now);
			str2 = iCodeService.getCodeMeaningByValue(request, "HQM_INSPECTION_ATTRIBUTE_TYPE", after);
		} else if ("sampleType".equals(fieldName)) {
			str1 = iCodeService.getCodeMeaningByValue(request, "HQM_FG_SAMPLE_PLAN_TYPE", now);
			str2 = iCodeService.getCodeMeaningByValue(request, "HQM_FG_SAMPLE_PLAN_TYPE", after);
		} else if ("platformType".equals(fieldName)) {
			str1 = iCodeService.getCodeMeaningByValue(request, "HQM_ITEM_PLATFORM", now);
			str2 = iCodeService.getCodeMeaningByValue(request, "HQM_ITEM_PLATFORM", after);
		} else {
			str1 = now;
			str2 = after;
		}
		return str1 + MiddleString + str2;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hand.hqm.hqm_platform_program.service.IPlatformProgramService#
	 * insertSelectiveRecord(com.hand.hap.core.IRequest,
	 * com.hand.hqm.hqm_platform_program.dto.PlatformProgram)
	 */
	@Override
	public PlatformProgram insertSelectiveRecord(IRequest request, PlatformProgram t) {
		self().insertSelective(request, t);
		PlatformProgram after = mapper.selectByPrimaryKey(t);
		PlatformProgramHis his = new PlatformProgramHis();
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
		iPlatformProgramHisService.insertSelective(request, his);
		return t;
	}

}