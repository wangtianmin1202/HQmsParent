package com.hand.hqm.hqm_iqc_inspection_template_l.service.impl;

import com.github.pagehelper.PageHelper;
import com.hand.hap.core.IRequest;
import com.hand.hap.core.annotation.StdWho;
import com.hand.hap.system.dto.BaseDTO;
import com.hand.hap.system.dto.DTOStatus;
import com.hand.hap.system.service.IBaseService;
import com.hand.hap.system.service.ICodeService;
import com.hand.hap.system.service.IPromptService;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import com.hand.hap.util.SystemApiMethod;
import com.hand.hcm.hcm_item_b.dto.ItemB;
import com.hand.hcm.hcm_object_events.dto.ObjectEvents;
import com.hand.hcm.hcm_object_events.service.IObjectEventsService;
import com.hand.hcm.hcm_plant.dto.Plant;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.ibatis.javassist.Modifier;
import org.drools.core.util.StringUtils;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hand.hqm.hqm_inspection_attribute.dto.InspectionAttribute;
import com.hand.hqm.hqm_inspection_attribute.mapper.InspectionAttributeMapper;
import com.hand.hqm.hqm_iqc_inspection_template_h.dto.IqcInspectionTemplateH;
import com.hand.hqm.hqm_iqc_inspection_template_h.service.IIqcInspectionTemplateHService;
import com.hand.hqm.hqm_iqc_inspection_template_h_his.dto.IqcInspectionTemplateHHis;
import com.hand.hqm.hqm_iqc_inspection_template_h_his.service.IIqcInspectionTemplateHHisService;
import com.hand.hqm.hqm_iqc_inspection_template_l.dto.IqcInspectionTemplateL;
import com.hand.hqm.hqm_iqc_inspection_template_l.mapper.IqcInspectionTemplateLMapper;
import com.hand.hqm.hqm_iqc_inspection_template_l.service.IIqcInspectionTemplateLService;
import com.hand.hqm.hqm_iqc_inspection_template_l_his.dto.IqcInspectionTemplateLHis;
import com.hand.hqm.hqm_iqc_inspection_template_l_his.service.IIqcInspectionTemplateLHisService;
import com.hand.hqm.hqm_sample_manage.dto.SampleManage;
import com.hand.hqm.hqm_sample_manage.mapper.SampleManageMapper;

import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class IqcInspectionTemplateLServiceImpl extends BaseServiceImpl<IqcInspectionTemplateL>
		implements IIqcInspectionTemplateLService {
	private String MiddleString = " 修改为 ";
	@Autowired
	IqcInspectionTemplateLMapper iqcInspectionTemplateLMapper;
	@Autowired
	IIqcInspectionTemplateHService iIqcInspectionTemplateHService;
	@Autowired
	IIqcInspectionTemplateHHisService iIqcInspectionTemplateHHisService;
	@Autowired
	IIqcInspectionTemplateLHisService iIqcInspectionTemplateLHisService;
	@Autowired
	IObjectEventsService iObjectEventsService;
	@Autowired
	IPromptService iPromptService;
	@Autowired
	ICodeService iCodeService;
	@Autowired
	InspectionAttributeMapper inspectionAttributeMapper;
	@Autowired
	SampleManageMapper sampleManageMapper;

	@Override
	public List<IqcInspectionTemplateL> myselect(IRequest requestContext, IqcInspectionTemplateL dto, int page,
			int pageSize) {
		// TODO Auto-generated method stub
		PageHelper.startPage(page, pageSize);
		return iqcInspectionTemplateLMapper.myselect(dto);
	}

	@Override
	public int reBatchDelete(IRequest request, List<IqcInspectionTemplateL> list) {
		int c = 0;
		IqcInspectionTemplateHHis his = new IqcInspectionTemplateHHis();
		his.setEventBy(request.getUserId());
		his.setEventTime(new Date());
		his.setHeaderId(list.get(0).getHeaderId());
		iIqcInspectionTemplateHHisService.insertSelective(request, his);
		for (IqcInspectionTemplateL t : list) {
			c++;
			deleteByPrimaryKeyRecoed(request, t, his.getEventId());
		}
		return c;
	}

	@Override
	public List<IqcInspectionTemplateL> historynumberUpdate(IRequest request, @StdWho List<IqcInspectionTemplateL> list)
			throws Exception {
//        IBaseService<IqcInspectionTemplateL> self = ((IBaseService<IqcInspectionTemplateL>) AopContext.currentProxy());
		List<IqcInspectionTemplateL> returnList = new ArrayList<IqcInspectionTemplateL>();
		IqcInspectionTemplateHHis his = new IqcInspectionTemplateHHis();
		his.setEventBy(request.getUserId());
		his.setEventTime(new Date());
		his.setHeaderId(list.get(0).getHeaderId());
		iIqcInspectionTemplateHHisService.insertSelective(request, his);
		for (IqcInspectionTemplateL t : list) {
			switch (t.get__status()) {
			case DTOStatus.ADD:
				t.setHistoryNum(10f);
				insertSelectiveRecord(request, t, his.getEventId());
				returnList.add(t);
				break;
			case DTOStatus.UPDATE:
				if (useSelectiveUpdate()) {
					t.setHistoryNum(t.getHistoryNum() + 1);
					updateByPrimaryKeySelectiveRecord(request, t, his.getEventId());
				} else {
					t.setHistoryNum(t.getHistoryNum() + 1);
					self().updateByPrimaryKey(request, t);
				}
				returnList.add(t);
				break;
			case DTOStatus.DELETE:
				deleteByPrimaryKeyRecoed(request, t, his.getEventId());
				break;
			default:
				break;
			}
		}
		for (IqcInspectionTemplateL t : list) {
			IqcInspectionTemplateL sear = new IqcInspectionTemplateL();
			sear.setHeaderId(t.getHeaderId());
			sear.setAttributeId(t.getAttributeId());
			List<IqcInspectionTemplateL> resli = iqcInspectionTemplateLMapper.select(sear);
			if (resli != null && resli.size() > 1) {
				throw new Exception("存在重复数据");
			}
		}
		// 更新头状态为未发布
		IqcInspectionTemplateH updateor = new IqcInspectionTemplateH();
		updateor.setHeaderId(list.get(0).getHeaderId());
		updateor.setStatus("1");
		iIqcInspectionTemplateHService.updateByPrimaryKeySelective(request, updateor);
		return returnList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hand.hqm.hqm_pqc_warning.service.IPqcWarningService#
	 * updateByPrimaryKeySelectiveRecord(com.hand.hap.core.IRequest,
	 * com.hand.hqm.hqm_pqc_warning.dto.PqcWarning)
	 */
	@Override
	public IqcInspectionTemplateL updateByPrimaryKeySelectiveRecord(IRequest request, IqcInspectionTemplateL t,
			Float eventHId) {
		if (eventHId == null) {
			IqcInspectionTemplateHHis his = new IqcInspectionTemplateHHis();
			his.setEventBy(request.getUserId());
			his.setEventTime(new Date());
			his.setHeaderId(t.getHeaderId());
			iIqcInspectionTemplateHHisService.insertSelective(request, his);
			eventHId = his.getEventId();
		}
		IqcInspectionTemplateL now = mapper.selectByPrimaryKey(t);
		self().updateByPrimaryKeySelective(request, t);
		IqcInspectionTemplateL after = mapper.selectByPrimaryKey(t);
		IqcInspectionTemplateLHis his = new IqcInspectionTemplateLHis();
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
		his.setEventHId(eventHId);
		his.setEventBy(request.getUserId());
		his.setEventTime(new Date());
		iIqcInspectionTemplateLHisService.insertSelective(request, his);
		objectEventsRecord(now, after, his, request);
		return t;
	}

	private void objectEventsRecord(IqcInspectionTemplateL now, IqcInspectionTemplateL after,
			IqcInspectionTemplateLHis his, IRequest request) {
		ObjectEvents oe = new ObjectEvents();
		oe.setEventId(his.getEventId());
		oe.setEventTime(his.getEventTime());
		oe.setEventBy(his.getEventBy());
		oe.setEventTable("HQM_IQC_INSPECTION_TEMPLATE_L");
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
								"iqcinspectiontemplatel." + nowField.getName().toLowerCase()) + ":"
								+ changeInfoGetNumber(request, nowFloat, afterFloat, nowField.getName()) + ",";
					}
				} else if (nowField.getType() == String.class) {
					String nowString = nowField.get(now) == null ? "" : (String) nowField.get(now);
					String afterString = afterField.get(after) == null ? "" : (String) afterField.get(after);
					if (StringUtils.isEmpty(nowString) && StringUtils.isEmpty(afterString))
						continue;
					if (!(nowString).equals(afterString)) {
						eventContent += SystemApiMethod.getPromptDescription(request, iPromptService,
								"iqcinspectiontemplatel." + nowField.getName().toLowerCase()) + ":"
								+ changeInfoGetString(request, nowString, afterString, nowField.getName()) + ",";
					}
				} else if (nowField.getType() == Date.class) {
					Date nowDate = new Date(0);
					Date nowString = nowField.get(now) == null ? nowDate : (Date) nowField.get(now);
					Date afterString = afterField.get(after) == null ? nowDate : (Date) afterField.get(after);
					if (nowString.compareTo(afterString) != 0) {
						eventContent += SystemApiMethod.getPromptDescription(request, iPromptService,
								"suppitemexemption." + nowField.getName().toLowerCase()) + ":"
								+ changeInfoGetDate(request, nowString, afterString, nowField.getName()) + ",";
					}
				} else {
					continue;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (!StringUtils.isEmpty(eventContent)) {
			eventContent = "检验项:" + getInspectionAttribute(after.getAttributeId()) + " "
					+ eventContent.substring(0, eventContent.length() - 1);
			oe.setEventContent(eventContent);
			iObjectEventsService.insertSelective(request, oe);
		}
	}

	private String getInspectionAttribute(Float attributeId) {
		InspectionAttribute ser = new InspectionAttribute();
		ser.setAttributeId(attributeId);
		return inspectionAttributeMapper.selectByPrimaryKey(ser).getInspectionAttribute();
	}

	private String changeInfoGetNumber(IRequest request, Float now, Float after, String fieldName) {
		String str1 = "";
		String str2 = "";
		if ("attributeId".equals(fieldName)) {// HQM_PLM_SKU_TYPE
			InspectionAttribute ser = new InspectionAttribute();
			ser.setAttributeId(now);
			InspectionAttribute itembNow = inspectionAttributeMapper.selectByPrimaryKey(ser);
			str1 = itembNow.getInspectionAttribute();
			ser.setAttributeId(after);
			InspectionAttribute itembAfter = inspectionAttributeMapper.selectByPrimaryKey(ser);
			str2 = itembAfter.getInspectionAttribute();
		} else if ("sampleWayId".equals(fieldName)) {
			SampleManage search = new SampleManage();
			search.setSampleWayId(now);
			str1 = sampleManageMapper.selectByPrimaryKey(search).getSampleWayCode();
			search.setSampleWayId(after);
			str2 = sampleManageMapper.selectByPrimaryKey(search).getSampleWayCode();
		} else {
			str1 = String.valueOf(now);
			str2 = String.valueOf(after);
		}

		return str1 + MiddleString + str2;
	}

	private String changeInfoGetString(IRequest request, String now, String after, String fieldName) {
		String str1 = "";
		String str2 = "";
		if ("attributeType".equals(fieldName)) {// HQM_PLM_SKU_TYPE
			str1 = iCodeService.getCodeMeaningByValue(request, "HQM_IQC_ATTRIBUTE_CATEGORY", now);
			str2 = iCodeService.getCodeMeaningByValue(request, "HQM_IQC_ATTRIBUTE_CATEGORY", after);
		} else if ("standardType".equals(fieldName)) {
			str1 = iCodeService.getCodeMeaningByValue(request, "HQM_IQC_STANDARD_TYPE", now);
			str2 = iCodeService.getCodeMeaningByValue(request, "HQM_IQC_STANDARD_TYPE", after);
		} else if ("inspectionMethod".equals(fieldName)) {
			str1 = iCodeService.getCodeMeaningByValue(request, "HQM_INSPECTION_METHOD", now);
			str2 = iCodeService.getCodeMeaningByValue(request, "HQM_INSPECTION_METHOD", after);
		} else if ("standradUom".equals(fieldName)) {
			str1 = iCodeService.getCodeMeaningByValue(request, "HQM_STANDARD_UOM", now);
			str2 = iCodeService.getCodeMeaningByValue(request, "HQM_STANDARD_UOM", after);
		} else if ("fillInType".equals(fieldName)) {
			str1 = iCodeService.getCodeMeaningByValue(request, "HQM_IQC_FILL_IN_TYPE", now);
			str2 = iCodeService.getCodeMeaningByValue(request, "HQM_IQC_FILL_IN_TYPE", after);
		} else if ("qualityCharacterGrade".equals(fieldName)) {
			str1 = iCodeService.getCodeMeaningByValue(request, "HQM_IQC_QUALITY_GRADE", now);
			str2 = iCodeService.getCodeMeaningByValue(request, "HQM_IQC_QUALITY_GRADE", after);
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
	public IqcInspectionTemplateL deleteByPrimaryKeyRecoed(IRequest request, IqcInspectionTemplateL t, Float eventHId) {
		if (eventHId == null) {
			IqcInspectionTemplateHHis his = new IqcInspectionTemplateHHis();
			his.setEventBy(request.getUserId());
			his.setEventTime(new Date());
			his.setHeaderId(t.getHeaderId());
			iIqcInspectionTemplateHHisService.insertSelective(request, his);
			eventHId = his.getEventId();
		}
		IqcInspectionTemplateL after = mapper.selectByPrimaryKey(t);
		self().deleteByPrimaryKey(t);
		IqcInspectionTemplateLHis his = new IqcInspectionTemplateLHis();
		for (Field hisField : his.getClass().getDeclaredFields()) {
			if ("eventId".equals(hisField.getName()) || "eventBy".equals(hisField.getName())
					|| "eventTime".equals(hisField.getName()) || "eventHId".equals(hisField.getName()))
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
		his.setEventHId(eventHId);
		his.setEventBy(request.getUserId());
		his.setEventTime(new Date());
		iIqcInspectionTemplateLHisService.insertSelective(request, his);
		objectEventsRecordInsertAndDelete(after, his, request, false);
		return t;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.hand.hqm.hqm_pqc_warning.service.IPqcWarningService#insertSelectiveRecord
	 * (com.hand.hap.core.IRequest, com.hand.hqm.hqm_pqc_warning.dto.PqcWarning)
	 */
	@Override
	public IqcInspectionTemplateL insertSelectiveRecord(IRequest request, IqcInspectionTemplateL t, Float eventHId) {
		if (eventHId == null) {
			IqcInspectionTemplateHHis his = new IqcInspectionTemplateHHis();
			his.setEventBy(request.getUserId());
			his.setEventTime(new Date());
			his.setHeaderId(t.getHeaderId());
			iIqcInspectionTemplateHHisService.insertSelective(request, his);
			eventHId = his.getEventId();
		}
		self().insertSelective(request, t);
		IqcInspectionTemplateL after = mapper.selectByPrimaryKey(t);
		IqcInspectionTemplateLHis his = new IqcInspectionTemplateLHis();
		for (Field hisField : his.getClass().getDeclaredFields()) {
			if ("eventId".equals(hisField.getName()) || "eventBy".equals(hisField.getName())
					|| "eventTime".equals(hisField.getName()) || "eventHId".equals(hisField.getName()))
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
		his.setEventHId(eventHId);
		his.setEventBy(request.getUserId());
		his.setEventTime(new Date());
		iIqcInspectionTemplateLHisService.insertSelective(request, his);
		objectEventsRecordInsertAndDelete(after, his, request, true);
		return t;
	}

	private void objectEventsRecordInsertAndDelete(IqcInspectionTemplateL now, IqcInspectionTemplateLHis his,
			IRequest request, boolean flag) {
		ObjectEvents oe = new ObjectEvents();
		oe.setEventId(his.getEventId());
		oe.setEventTime(his.getEventTime());
		oe.setEventBy(his.getEventBy());
		oe.setEventTable("HQM_IQC_INSPECTION_TEMPLATE_L");
		String eventContent = "";
		Field[] nowFields = now.getClass().getDeclaredFields();
		for (Field nowField : nowFields) {
			try {
				if (nowField.isAnnotationPresent(javax.persistence.Transient.class)
						|| nowField.isAnnotationPresent(javax.persistence.Id.class)
						|| Modifier.isFinal(nowField.getModifiers()) || Modifier.isStatic(nowField.getModifiers()))
					continue;
				if ("headerId".equals(nowField.getName()))
					continue;
				nowField.setAccessible(true);
				if (nowField.getType() == float.class || nowField.getType() == Float.class) {
					Float nowFloat = nowField.get(now) == null ? 0 : (Float) nowField.get(now);
					eventContent += SystemApiMethod.getPromptDescription(request, iPromptService,
							"iqcinspectiontemplatel." + nowField.getName().toLowerCase()) + ":"
							+ changeInfoGetNumber(request, nowFloat, nowField.getName()) + ",";
				} else if (nowField.getType() == String.class) {
					String nowString = nowField.get(now) == null ? "" : (String) nowField.get(now);
					eventContent += SystemApiMethod.getPromptDescription(request, iPromptService,
							"iqcinspectiontemplatel." + nowField.getName().toLowerCase()) + ":"
							+ changeInfoGetString(request, nowString, nowField.getName()) + ",";

				} else if (nowField.getType() == Date.class) {
					Date nowDate = new Date(0);
					Date nowString = nowField.get(now) == null ? nowDate : (Date) nowField.get(now);
					eventContent += SystemApiMethod.getPromptDescription(request, iPromptService,
							"suppitemexemption." + nowField.getName().toLowerCase()) + ":"
							+ changeInfoGetDate(request, nowString, nowField.getName()) + ",";

				} else {
					continue;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (!StringUtils.isEmpty(eventContent)) {
			eventContent = flag ? "新增"
					: "删除" + "检验项:" + getInspectionAttribute(now.getAttributeId()) + " "
							+ eventContent.substring(0, eventContent.length() - 1);
			oe.setEventContent(eventContent);
			iObjectEventsService.insertSelective(request, oe);
		}
	}

	private String changeInfoGetNumber(IRequest request, Float now, String fieldName) {
		String str1 = "";
		if ("attributeId".equals(fieldName)) {// HQM_PLM_SKU_TYPE
			InspectionAttribute ser = new InspectionAttribute();
			ser.setAttributeId(now);
			InspectionAttribute itembNow = inspectionAttributeMapper.selectByPrimaryKey(ser);
			str1 = itembNow.getInspectionAttribute();
		} else if ("sampleWayId".equals(fieldName)) {
			SampleManage search = new SampleManage();
			search.setSampleWayId(now);
			str1 = sampleManageMapper.selectByPrimaryKey(search).getSampleWayCode();
		} else {
			str1 = String.valueOf(now);
		}
		return str1;
	}

	private String changeInfoGetString(IRequest request, String now, String fieldName) {
		String str1 = "";
		if ("attributeType".equals(fieldName)) {// HQM_PLM_SKU_TYPE
			str1 = iCodeService.getCodeMeaningByValue(request, "HQM_IQC_ATTRIBUTE_CATEGORY", now);
		} else if ("standardType".equals(fieldName)) {
			str1 = iCodeService.getCodeMeaningByValue(request, "HQM_IQC_STANDARD_TYPE", now);
		} else if ("inspectionMethod".equals(fieldName)) {
			str1 = iCodeService.getCodeMeaningByValue(request, "HQM_INSPECTION_METHOD", now);
		} else if ("standradUom".equals(fieldName)) {
			str1 = iCodeService.getCodeMeaningByValue(request, "HQM_STANDARD_UOM", now);
		} else if ("fillInType".equals(fieldName)) {
			str1 = iCodeService.getCodeMeaningByValue(request, "HQM_IQC_FILL_IN_TYPE", now);
		} else if ("qualityCharacterGrade".equals(fieldName)) {
			str1 = iCodeService.getCodeMeaningByValue(request, "HQM_IQC_QUALITY_GRADE", now);
		} else {
			str1 = now;
		}
		return str1;
	}

	private String changeInfoGetDate(IRequest request, Date now, String fieldName) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String str1 = "";
		str1 = sdf.format(now);
		return str1;
	}
}