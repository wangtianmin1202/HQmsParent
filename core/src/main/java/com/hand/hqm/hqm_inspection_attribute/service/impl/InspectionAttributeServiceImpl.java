package com.hand.hqm.hqm_inspection_attribute.service.impl;

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

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.hand.hqm.hqm_inspection_attribute.dto.InspectionAttribute;
import com.hand.hqm.hqm_inspection_attribute.mapper.InspectionAttributeMapper;
import com.hand.hqm.hqm_inspection_attribute.service.IInspectionAttributeService;
import com.hand.hqm.hqm_inspection_attribute_his.dto.InspectionAttributeHis;
import com.hand.hqm.hqm_inspection_attribute_his.service.IInspectionAttributeHisService;
import com.hand.hqm.hqm_platform_program.dto.PlatformProgram;
import com.hand.hqm.hqm_platform_program_his.dto.PlatformProgramHis;

import redis.clients.jedis.Jedis;

import org.springframework.transaction.annotation.Transactional;
import org.apache.ibatis.javassist.Modifier;
import org.apache.poi.ss.usermodel.Row;

@Service
@Transactional(rollbackFor = Exception.class)
public class InspectionAttributeServiceImpl extends BaseServiceImpl<InspectionAttribute>
		implements IInspectionAttributeService {
	private String MiddleString = " 修改为 ";
	@Autowired
	ICodeService iCodeService;
	@Autowired
	IInspectionAttributeHisService iInspectionAttributeHisService;
	@Autowired
	InspectionAttributeMapper mapper;
	@Autowired
	IPromptService iPromptService;
	@Autowired
	IObjectEventsService iObjectEventsService;

	@Override
	public List<InspectionAttribute> inputDataFromExcel(IRequest requestCtx, InputStream inputStream)
			throws Exception, SQLException {
		// TODO 解析
		Sheet sheet;
		XSSFWorkbook workBook;
		List<InspectionAttribute> totalList = new ArrayList<>();
		workBook = new XSSFWorkbook(inputStream);
		sheet = workBook.getSheetAt(0);
		for (int i = 1; i <= sheet.getLastRowNum(); i++) {
			// i=1,从第二行开始
			Row row = sheet.getRow(i);
			InspectionAttribute rowModel = new InspectionAttribute();
			// 行解析
			String inVal;
			// 检验项目
			rowModel.setInspectionAttribute(row.getCell(0) == null ? "" : row.getCell(0).toString());
			// 检验项类
			inVal = iCodeService.getCodeValueByMeaning(requestCtx, "HQM_IQC_ATTRIBUTE_CATEGORY",
					(row.getCell(1) == null ? "" : row.getCell(1)).toString());
			if (inVal == null) {
				throw new Exception("未能找到匹配值从[HQM_IQC_ATTRIBUTE_CATEGORY]中匹配["
						+ (row.getCell(1) == null ? "" : row.getCell(1)).toString() + "]时");
			}
			rowModel.setAttributeType(inVal);
			// 质量特性等级
			inVal = iCodeService.getCodeValueByMeaning(requestCtx, "HQM_IQC_QUALITY_GRADE",
					(row.getCell(2) == null ? "" : row.getCell(2)).toString());
			if (inVal == null) {
				throw new Exception("未能找到匹配值从[HQM_IQC_QUALITY_GRADE]中匹配["
						+ (row.getCell(2) == null ? "" : row.getCell(2)).toString() + "]时");
			}
			rowModel.setQualityCharacterGrade(inVal);
			// 规格类型
			inVal = iCodeService.getCodeValueByMeaning(requestCtx, "HQM_IQC_STANDARD_TYPE",
					(row.getCell(3) == null ? "" : row.getCell(3)).toString());
			if (inVal == null) {
				throw new Exception("未能找到匹配值从[HQM_IQC_STANDARD_TYPE]中匹配["
						+ (row.getCell(3) == null ? "" : row.getCell(3)).toString() + "]时");
			}
			rowModel.setStandardType(inVal);
			// 填写类型(结果记录)
			inVal = iCodeService.getCodeValueByMeaning(requestCtx, "HQM_FILL_IN_TYPE",
					(row.getCell(4) == null ? "" : row.getCell(4)).toString());
			if (inVal == null) {
				inVal = (row.getCell(4) == null ? "" : row.getCell(4)).toString();
			}
			rowModel.setFillInType(inVal);
//			rowModel.setPrecision("".equals(row.getCell(5).toString()) || row.getCell(5) == null
//					? Float.valueOf(row.getCell(5).toString())
//					: null);
			// 精度
//			if (row.getCell(5) != null && !"".equals(row.getCell(5).toString())) {
//				rowModel.setPrecision(Float.valueOf(row.getCell(5).toString()));
//			}
			// 检验方法
			inVal = iCodeService.getCodeValueByMeaning(requestCtx, "HQM_INSPECTION_METHOD",
					(row.getCell(5) == null ? "" : row.getCell(5)).toString());
			if (inVal == null) {
				inVal = (row.getCell(5) == null ? "" : row.getCell(5)).toString();
			}
			rowModel.setInspectionMethod(inVal);
			rowModel.setInspectionTool(row.getCell(6) == null ? "" : row.getCell(6).toString());
			// 备注
			rowModel.setRemark(row.getCell(7) == null ? "" : row.getCell(7).toString());
			totalList.add(rowModel);
		}
		try {
			workBook.close();
		} catch (Exception e) {

		}
		for (InspectionAttribute inspectionAttribute : totalList) {
			self().insertSelective(requestCtx, inspectionAttribute);
		}
		return totalList;
	}

	@Override
	public List<InspectionAttribute> reSelect(IRequest requestContext, InspectionAttribute dto, int page,
			int pageSize) {
		// TODO Auto-generated method stub
		PageHelper.startPage(page, pageSize);
		return mapper.reSelect(dto);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.hand.hqm.hqm_inspection_attribute.service.IInspectionAttributeService#
	 * reBatchUpdate(com.hand.hap.core.IRequest, java.util.List)
	 */
	@Override
	public List<InspectionAttribute> reBatchUpdate(IRequest request, List<InspectionAttribute> list) {
		for (InspectionAttribute t : list) {
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
	public InspectionAttribute updateByPrimaryKeySelectiveRecord(IRequest request, InspectionAttribute t) {
		InspectionAttribute now = mapper.selectByPrimaryKey(t);
		self().updateByPrimaryKeySelective(request, t);
		InspectionAttribute after = mapper.selectByPrimaryKey(t);
		InspectionAttributeHis his = new InspectionAttributeHis();
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
		iInspectionAttributeHisService.insertSelective(request, his);
		objectEventsRecord(now, after, his, request);
		return t;
	}

	private void objectEventsRecord(InspectionAttribute now, InspectionAttribute after, InspectionAttributeHis his,
			IRequest request) {
		ObjectEvents oe = new ObjectEvents();
		oe.setEventId(his.getEventId());
		oe.setEventTime(his.getEventTime());
		oe.setEventBy(his.getEventBy());
		oe.setEventTable("HQM_INSPECTION_ATTRIBUTE");
		String eventContent = "";
		Field[] nowFields = now.getClass().getDeclaredFields();
		// System.arraycopy(nowFields, 0, request, 0, 0);
		nowFields = Arrays.copyOf(nowFields, nowFields.length + 2);
		try {
			nowFields[nowFields.length - 2] = now.getClass().getSuperclass().getDeclaredField("attribute1");
			nowFields[nowFields.length - 1] = now.getClass().getSuperclass().getDeclaredField("attribute2");
			for (Field nowField : nowFields) {

				if (nowField.isAnnotationPresent(javax.persistence.Transient.class)
						|| nowField.isAnnotationPresent(javax.persistence.Id.class)
						|| Modifier.isFinal(nowField.getModifiers()) || Modifier.isStatic(nowField.getModifiers()))
					continue;
				nowField.setAccessible(true);
				Field afterField = ("attribute1".equals(nowField.getName()) || "attribute2".equals(nowField.getName()))
						? after.getClass().getSuperclass().getDeclaredField(nowField.getName())
						: after.getClass().getDeclaredField(nowField.getName());
				afterField.setAccessible(true);
				if (nowField.getType() == float.class || nowField.getType() == Float.class) {
					Float nowFloat = nowField.get(now) == null ? 0 : (Float) nowField.get(now);
					Float afterFloat = afterField.get(after) == null ? 0 : (Float) afterField.get(after);
					if (nowFloat.floatValue() != afterFloat.floatValue()) {
						eventContent += SystemApiMethod.getPromptDescription(request, iPromptService,
								"inspectionattribute." + nowField.getName().toLowerCase()) + ":"
								+ changeInfoGetNumber(request, nowFloat, afterFloat, nowField.getName()) + "<br/>";
					}
				} else if (nowField.getType() == String.class) {
					String nowString = nowField.get(now) == null ? "" : (String) nowField.get(now);
					String afterString = afterField.get(after) == null ? "" : (String) afterField.get(after);
					if (!(nowString).equals(afterString)) {
						eventContent += SystemApiMethod.getPromptDescription(request, iPromptService,
								"inspectionattribute." + nowField.getName().toLowerCase()) + ":"
								+ changeInfoGetString(request, nowString, afterString, nowField.getName()) + "<br/>";
					}
				} else {
					continue;
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
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
		if ("sampleProcedureType".equals(fieldName)) {
			str1 = iCodeService.getCodeMeaningByValue(request, "HQM_SAMPLE_STANDARD_TYPE", now);
			str2 = iCodeService.getCodeMeaningByValue(request, "HQM_SAMPLE_STANDARD_TYPE", after);
		} else if ("attributeType".equals(fieldName)) {
			str1 = iCodeService.getCodeMeaningByValue(request, "HQM_IQC_ATTRIBUTE_CATEGORY", now);
			str2 = iCodeService.getCodeMeaningByValue(request, "HQM_IQC_ATTRIBUTE_CATEGORY", after);
		} else if ("inspectionLevels".equals(fieldName)) {
			str1 = iCodeService.getCodeMeaningByValue(request, "HQM_IQC_INSPECTION_LEVELS", now);
			str2 = iCodeService.getCodeMeaningByValue(request, "HQM_IQC_INSPECTION_LEVELS", after);
		} else if ("qualityCharacterGrade".equals(fieldName)) {
			str1 = iCodeService.getCodeMeaningByValue(request, "HQM_IQC_QUALITY_GRADE", now);
			str2 = iCodeService.getCodeMeaningByValue(request, "HQM_IQC_QUALITY_GRADE", after);
		} else if ("acceptanceQualityLimit".equals(fieldName)) {
			str1 = iCodeService.getCodeMeaningByValue(request, "HQM_IQC_AQL", now);
			str2 = iCodeService.getCodeMeaningByValue(request, "HQM_IQC_AQL", after);
		} else if ("sourceType".equals(fieldName)) {
			str1 = iCodeService.getCodeMeaningByValue(request, "HQM_SOURCE_TYPE", now);
			str2 = iCodeService.getCodeMeaningByValue(request, "HQM_SOURCE_TYPE", after);
		} else if ("standardType".equals(fieldName)) {
			str1 = iCodeService.getCodeMeaningByValue(request, "HQM_STANDARD_TYPE", now);
			str2 = iCodeService.getCodeMeaningByValue(request, "HQM_STANDARD_TYPE", after);
		} else if ("fillInType".equals(fieldName)) {
			str1 = iCodeService.getCodeMeaningByValue(request, "HQM_FILL_IN_TYPE", now);
			str2 = iCodeService.getCodeMeaningByValue(request, "HQM_FILL_IN_TYPE", after);
		} else if ("inspectionMethod".equals(fieldName)) {
			str1 = iCodeService.getCodeMeaningByValue(request, "HQM_INSPECTION_METHOD", now);
			str2 = iCodeService.getCodeMeaningByValue(request, "HQM_INSPECTION_METHOD", after);
		} else if ("frequencyType".equals(fieldName)) {
			str1 = iCodeService.getCodeMeaningByValue(request, "HQM_FREQUENCY_TYPE", now);
			str2 = iCodeService.getCodeMeaningByValue(request, "HQM_FREQUENCY_TYPE", after);
		} else if ("attribute1".equals(fieldName)) {
			str1 = iCodeService.getCodeMeaningByValue(request, "HQM_INSPECTION_PLACE", now);
			str2 = iCodeService.getCodeMeaningByValue(request, "HQM_INSPECTION_PLACE", after);
		} else if ("attribute2".equals(fieldName)) {
			str1 = iCodeService.getCodeMeaningByValue(request, "HQM_INSPECTION_ATTRIBUTE_TYPE", now);
			str2 = iCodeService.getCodeMeaningByValue(request, "HQM_INSPECTION_ATTRIBUTE_TYPE", after);
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
	public InspectionAttribute insertSelectiveRecord(IRequest request, InspectionAttribute t) {
		self().insertSelective(request, t);
		InspectionAttribute after = mapper.selectByPrimaryKey(t);
		InspectionAttributeHis his = new InspectionAttributeHis();
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
		iInspectionAttributeHisService.insertSelective(request, his);
		return t;
	}

}