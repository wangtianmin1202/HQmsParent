package com.hand.hqm.hqm_pqc_warning.service.impl;

import com.github.pagehelper.PageHelper;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.BaseDTO;
import com.hand.hap.system.dto.DTOStatus;
import com.hand.hap.system.service.IPromptService;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import com.hand.hap.util.SystemApiMethod;
import com.hand.hcm.hcm_object_events.dto.ObjectEvents;
import com.hand.hcm.hcm_object_events.service.IObjectEventsService;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.javassist.Modifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.hand.hqm.hqm_pqc_warning.dto.PqcWarning;
import com.hand.hqm.hqm_pqc_warning.mapper.PqcWarningMapper;
import com.hand.hqm.hqm_pqc_warning.service.IPqcWarningService;
import com.hand.hqm.hqm_pqc_warning_his.dto.PqcWarningHis;
import com.hand.hqm.hqm_pqc_warning_his.service.IPqcWarningHisService;
import com.hand.sys.sys_user.mapper.UserSysMapper;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class PqcWarningServiceImpl extends BaseServiceImpl<PqcWarning> implements IPqcWarningService {
	private final static String MiddleString = " 修改为 ";
	@Autowired
	PqcWarningMapper mapper;
	@Autowired
	UserSysMapper userSysMapper;
	@Autowired
	IPromptService iPromptService;
	@Autowired
	IPqcWarningHisService iPqcWarningHisService;
	@Autowired
	IObjectEventsService iObjectEventsService;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.hand.hqm.hqm_pqc_warning.service.IPqcWarningService#reSelect(com.hand.hap
	 * .core.IRequest, com.hand.hqm.hqm_pqc_warning.dto.PqcWarning, int, int)
	 */
	@Override
	public List<PqcWarning> reSelect(IRequest requestContext, PqcWarning dto, int page, int pageSize) {
		// TODO 查询 包含预警对象的id转名字的操作
		PageHelper.startPage(page, pageSize);
		List<PqcWarning> retu = mapper.reSelect(dto);
		operationIdToName(retu);
		return retu;
	}

	/**
	 * 
	 * @description 把预警对象中的userid 转换为 员工name 用 , 拼接
	 * @author tianmin.wang
	 * @date 2019年12月26日
	 * @param retu
	 */
	public void operationIdToName(List<PqcWarning> retu) {
		Map<String, String> cache = new HashMap<String, String>();
		for (PqcWarning pw : retu) {
			String warningObjectName = "";
			List<String> warningObjectList = Arrays.asList(pw.getWarningObject().split(","));
			for (String p : warningObjectList) {
				String name = cache.get(p);
				if (name == null) {
					name = userSysMapper.getEmployeeNameByUserId(Float.valueOf(p));
					cache.put(p, name == null ? "" : name);
				}
				warningObjectName = warningObjectName + "," + name;
			}
			pw.setWarningObjectName(warningObjectName.substring(1));
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.hand.hqm.hqm_pqc_warning.service.IPqcWarningService#reBatchUpdate(com.
	 * hand.hap.core.IRequest, java.util.List)
	 */
	@Override
	public List<PqcWarning> reBatchUpdate(IRequest request, List<PqcWarning> list) {
		for (PqcWarning t : list) {
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

		for (PqcWarning t : list) {
			PqcWarning serach = new PqcWarning();
			serach.setPlantId(t.getPlantId());
			serach.setWarningLevel(t.getWarningLevel());
			List<?> lis = mapper.select(serach);
			if (lis != null && lis.size() > 1) {
				throw new RuntimeException(
						SystemApiMethod.getPromptDescription(request, iPromptService, "hqm_pqc_warning_save.uk.error"));
			}
		}
		return list;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hand.hqm.hqm_pqc_warning.service.IPqcWarningService#
	 * updateByPrimaryKeySelectiveRecord(com.hand.hap.core.IRequest,
	 * com.hand.hqm.hqm_pqc_warning.dto.PqcWarning)
	 */
	@Override
	public PqcWarning updateByPrimaryKeySelectiveRecord(IRequest request, PqcWarning t) {
		PqcWarning now = mapper.selectByPrimaryKey(t);
		self().updateByPrimaryKeySelective(request, t);
		PqcWarning after = mapper.selectByPrimaryKey(t);
		PqcWarningHis his = new PqcWarningHis();
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
		iPqcWarningHisService.insertSelective(request, his);
		objectEventsRecord(now, after, his, request);
		return t;
	}

	private void objectEventsRecord(PqcWarning now, PqcWarning after, PqcWarningHis his, IRequest request) {
		ObjectEvents oe = new ObjectEvents();
		oe.setEventId(his.getEventId());
		oe.setEventTime(his.getEventTime());
		oe.setEventBy(his.getEventBy());
		oe.setEventTable("HQM_PQC_WARNING");
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
								"pqcwarning." + nowField.getName().toLowerCase()) + ":"
								+ changeInfoGetNumber(request, nowFloat, afterFloat, nowField.getName()) + "<br/>";
					}
				} else if (nowField.getType() == String.class) {
					String nowString = nowField.get(now) == null ? "" : (String) nowField.get(now);
					String afterString = afterField.get(after) == null ? "" : (String) afterField.get(after);
					if (!(nowString).equals(afterString)) {
						eventContent += SystemApiMethod.getPromptDescription(request, iPromptService,
								"pqcwarning." + nowField.getName().toLowerCase()) + ":"
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
		str1 = String.valueOf(now.intValue());
		str2 = String.valueOf(after.intValue());
		return str1 + MiddleString + str2;
	}

	private String changeInfoGetString(IRequest request, String now, String after, String fieldName) {
		String str1 = "";
		String str2 = "";
		if ("warningObject".equals(fieldName)) {
			Map<String, String> cache = new HashMap<String, String>();// 一个小缓存
			String warningObjectName = "";
			List<String> warningObjectList = Arrays.asList(now.split(","));
			for (String p : warningObjectList) {
				String name = cache.get(p);
				if (name == null) {
					name = userSysMapper.getEmployeeNameByUserId(Float.valueOf(p));
					cache.put(p, name == null ? "" : name);
				}
				warningObjectName = warningObjectName + "," + name;
			}
			str1 = warningObjectName.substring(1);
			warningObjectName = "";
			warningObjectList = Arrays.asList(after.split(","));
			for (String p : warningObjectList) {
				String name = cache.get(p);
				if (name == null) {
					name = userSysMapper.getEmployeeNameByUserId(Float.valueOf(p));
					cache.put(p, name == null ? "" : name);
				}
				warningObjectName = warningObjectName + "," + name;
			}
			str2 = warningObjectName.substring(1);
		} else {
			str1 = now;
			str2 = after;
		}
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
	public PqcWarning insertSelectiveRecord(IRequest request, PqcWarning t) {
		self().insertSelective(request, t);
		PqcWarning after = mapper.selectByPrimaryKey(t);
		PqcWarningHis his = new PqcWarningHis();
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
		iPqcWarningHisService.insertSelective(request, his);
		return t;
	}

}