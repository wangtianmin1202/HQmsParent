package com.hand.hqm.hqm_item_type_tests.service.impl;

import com.github.pagehelper.PageHelper;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.BaseDTO;
import com.hand.hap.system.dto.DTOStatus;
import com.hand.hap.system.service.IBaseService;
import com.hand.hap.system.service.ICodeService;
import com.hand.hap.system.service.IPromptService;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import com.hand.hap.util.SystemApiMethod;
import com.hand.hcm.hcm_item_b.dto.ItemB;
import com.hand.hcm.hcm_item_b.mapper.ItemBMapper;
import com.hand.hcm.hcm_item_category.dto.ItemCategory;
import com.hand.hcm.hcm_item_category.mapper.ItemCategoryMapper;
import com.hand.hcm.hcm_object_events.dto.ObjectEvents;
import com.hand.hcm.hcm_object_events.service.IObjectEventsService;
import com.hand.hcm.hcm_plant.dto.Plant;
import com.hand.hcm.hcm_plant.mapper.PlantMapper;

import java.io.IOException;
import java.lang.reflect.Field;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.ibatis.javassist.Modifier;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.drools.core.util.StringUtils;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.hand.hqm.hqm_item_type_tests.dto.ItemTypeTests;
import com.hand.hqm.hqm_item_type_tests.mapper.ItemTypeTestsMapper;
import com.hand.hqm.hqm_item_type_tests.service.IItemTypeTestsService;
import com.hand.hqm.hqm_item_type_tests_his.dto.ItemTypeTestsHis;
import com.hand.hqm.hqm_item_type_tests_his.service.IItemTypeTestsHisService;
import com.hand.hqm.hqm_program_sku_rel.dto.ProgramSkuRel;
import com.hand.hqm.hqm_program_sku_rel_his.dto.ProgramSkuRelHis;

import jodd.util.StringUtil;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional(rollbackFor = Exception.class)
public class ItemTypeTestsServiceImpl extends BaseServiceImpl<ItemTypeTests> implements IItemTypeTestsService {

	String MiddleString = " 修改为 ";
	@Autowired
	ItemTypeTestsMapper mapper;
	@Autowired
	IPromptService iPromptService;
	@Autowired
	ICodeService iCodeService;
	@Autowired
	PlantMapper plantMapper;
	@Autowired
	ItemCategoryMapper itemCategoryMapper;
	@Autowired
	ItemBMapper itemBMapper;
	@Autowired
	IItemTypeTestsHisService iItemTypeTestsHisService;
	@Autowired
	IObjectEventsService iObjectEventsService;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.hand.hqm.hqm_item_type_tests.service.IItemTypeTestsService#reSelect(com.
	 * hand.hap.core.IRequest, com.hand.hqm.hqm_item_type_tests.dto.ItemTypeTests,
	 * int, int)
	 */
	@Override
	public List<ItemTypeTests> reSelect(IRequest requestContext, ItemTypeTests dto, int page, int pageSize) {
		PageHelper.startPage(page, pageSize);
		return mapper.reSelect(dto);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.hand.hqm.hqm_item_type_tests.service.IItemTypeTestsService#excelImport(
	 * com.hand.hap.core.IRequest, org.springframework.web.multipart.MultipartFile)
	 */
	@Override
	public List<ItemTypeTests> excelImport(IRequest requestCtx, MultipartFile forModel) {
		// TODO Auto-generated method stub
		XSSFWorkbook workBook;
		try {
			workBook = new XSSFWorkbook(forModel.getInputStream());
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage());
		}

		List<ItemTypeTests> result = retrieveExcelData(requestCtx, workBook);

		try {
			workBook.close();
		} catch (Exception e) {

		}
		return result;
	}

	/**
	 * @description excel 数据转换
	 * @author tianmin.wang
	 * @date 2019年12月17日
	 * @param workBook
	 * @return
	 */
	private List<ItemTypeTests> retrieveExcelData(IRequest requestCtx, XSSFWorkbook workBook) {
		List<ItemTypeTests> result = new ArrayList<ItemTypeTests>();
		// 缓存工厂
		List<Plant> plants = plantMapper.selectAll();
		Sheet sheet;
		sheet = workBook.getSheetAt(0);
		for (int i = 0; i <= sheet.getLastRowNum(); i++) {
			Row row = sheet.getRow(i);
			for (int j = 0; j < row.getLastCellNum(); j++) {
				if (row.getCell(j) != null) {
					row.getCell(j).setCellType(CellType.STRING);
				}
			}
		}
		String errorMessage = SystemApiMethod.getPromptDescription(requestCtx, iPromptService,
				"hqm_item_type_tests_error1");// 导入的数据存在为空的数据
		String errorMessage2 = SystemApiMethod.getPromptDescription(requestCtx, iPromptService,
				"hqm_item_type_tests_error2");
		for (int i = 1; i <= sheet.getLastRowNum(); i++) {// i=1 从第二行开始 忽略标题行
			Row row = sheet.getRow(i);
			if (row.getCell(0) == null || row.getCell(1) == null || row.getCell(5) == null
					|| StringUtil.isEmpty(row.getCell(0).getStringCellValue())
					|| StringUtil.isEmpty(row.getCell(1).getStringCellValue())
					|| StringUtil.isEmpty(row.getCell(5).getStringCellValue())) {
				throw new RuntimeException(errorMessage);
			}
			ItemTypeTests insert = new ItemTypeTests();
			// 数据项构建
			insert.setPlantId(getPlantIdByCode(plants, row.getCell(0).getStringCellValue()));
			String codeValue = iCodeService.getCodeValueByMeaning(requestCtx, "HQM_TEST_TYPE",
					row.getCell(1).getStringCellValue());
			if (StringUtil.isEmpty(codeValue)) {
				throw new RuntimeException(MessageFormat.format(errorMessage2, row.getCell(1).getStringCellValue()));
			}
			insert.setTestType(codeValue);
			if (codeValue.equals("2")) {
				insert.setCategoryId(getCategoryIdByCode(requestCtx, row.getCell(2).getStringCellValue()));
			}
			if (codeValue.equals("1")) {
				insert.setItemId(getItemIdByCode(requestCtx, row.getCell(3).getStringCellValue(), insert.getPlantId()));
			}
			insert.setTriggerNum(Float.valueOf(row.getCell(5).getStringCellValue()));
			insert.setTypeChangeTime(Float.valueOf(row.getCell(6).getStringCellValue()));
			insert.setEnableFlag(row.getCell(7).getStringCellValue().equals("Y") ? "Y" : "N");
			self().insertSelective(requestCtx, insert);
			result.add(insert);
		}
		return result;
	}

	public Float getPlantIdByCode(List<Plant> list, String plantCode) {
		List<Plant> rel = list.stream().filter(p -> p.getPlantCode().equals(plantCode.trim()))
				.collect(Collectors.toList());
		return rel.get(0).getPlantId();
	}

	public Float getCategoryIdByCode(IRequest requestCtx, String code) {
		String errorMessage3 = SystemApiMethod.getPromptDescription(requestCtx, iPromptService,
				"hqm_item_type_tests_error3");
		Float result = itemCategoryMapper.getIdByCode(code);
		if (result == null) {
			throw new RuntimeException(MessageFormat.format(errorMessage3, code));
		}
		return result;
	}

	public Float getItemIdByCode(IRequest requestCtx, String code, Float plantId) {
		String errorMessage4 = SystemApiMethod.getPromptDescription(requestCtx, iPromptService,
				"hqm_item_type_tests_error4");
		ItemB itemB = new ItemB();
		itemB.setPlantId(plantId);
		itemB.setItemCode(code);
		List<ItemB> res = itemBMapper.select(itemB);
		if (res == null || res.size() == 0) {
			throw new RuntimeException(MessageFormat.format(errorMessage4, code));
		}
		return res.get(0).getItemId();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.hand.hqm.hqm_item_type_tests.service.IItemTypeTestsService#reBatchUpdate(
	 * com.hand.hap.core.IRequest, java.util.List)
	 */
	@Override
	public List<ItemTypeTests> reBatchUpdate(IRequest request, List<ItemTypeTests> dto) {
		for (ItemTypeTests t : dto) {
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
		return dto;
	}

	@Override
	public ItemTypeTests updateByPrimaryKeySelectiveRecord(IRequest request, ItemTypeTests t) {
		ItemTypeTests now = mapper.selectByPrimaryKey(t);
		self().updateByPrimaryKeySelective(request, t);
		ItemTypeTests after = mapper.selectByPrimaryKey(t);
		ItemTypeTestsHis his = new ItemTypeTestsHis();
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
		iItemTypeTestsHisService.insertSelective(request, his);
		objectEventsRecord(now, after, his, request);
		return t;
	}

	private void objectEventsRecord(ItemTypeTests now, ItemTypeTests after, ItemTypeTestsHis his, IRequest request) {
		ObjectEvents oe = new ObjectEvents();
		oe.setEventId(his.getEventId());
		oe.setEventTime(his.getEventTime());
		oe.setEventBy(his.getEventBy());
		oe.setEventTable("HQM_ITEM_TYPE_TESTS");
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
								"itemtypetests." + nowField.getName().toLowerCase()) + ":"
								+ changeInfoGetNumber(request, nowFloat, afterFloat, nowField.getName()) + "<br/>";
					}
				} else if (nowField.getType() == String.class) {
					String nowString = nowField.get(now) == null ? "" : (String) nowField.get(now);
					String afterString = afterField.get(after) == null ? "" : (String) afterField.get(after);
					if (!(nowString).equals(afterString)) {
						eventContent += SystemApiMethod.getPromptDescription(request, iPromptService,
								"itemtypetests." + nowField.getName().toLowerCase()) + ":"
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
		if ("itemId".equals(fieldName)) {// HQM_PLM_SKU_TYPE
			Float plantId = getPlantId("CNKE");
			ItemB ser = new ItemB();
			ser.setPlantId(plantId);
			ser.setItemId(now);
			ItemB itembNow = itemBMapper.reSelect(ser).get(0);
			str1 = itembNow.getItemCode() + ":" + itembNow.getItemDescriptions();
			ser.setItemId(after);
			ItemB itembAfter = itemBMapper.reSelect(ser).get(0);
			str2 = itembAfter.getItemCode() + ":" + itembAfter.getItemDescriptions();
		} else if ("categoryId".equals(fieldName)) {// HQM_PLM_SKU_TYPE
			ItemCategory ser = new ItemCategory();
			ser.setCategoryId(now);
			ItemCategory itembNow = itemCategoryMapper.select(ser).get(0);
			str1 = itembNow.getCategoryCode() + ":" + itembNow.getDescription();
			ser.setCategoryId(after);
			ItemCategory itembAfter = itemCategoryMapper.select(ser).get(0);
			str2 = itembAfter.getCategoryCode() + ":" + itembAfter.getDescription();
		} else {
			str1 = String.valueOf(now);
			str2 = String.valueOf(after);
		}
		return str1 + MiddleString + str2;
	}

	private String changeInfoGetString(IRequest request, String now, String after, String fieldName) {
		String str1 = "";
		String str2 = "";
		if ("testType".equals(fieldName)) {// HQM_PLM_SKU_TYPE
			str1 = iCodeService.getCodeMeaningByValue(request, "HQM_TEST_TYPE", now);
			str2 = iCodeService.getCodeMeaningByValue(request, "HQM_TEST_TYPE", after);
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
	public ItemTypeTests insertSelectiveRecord(IRequest request, ItemTypeTests t) {
		self().insertSelective(request, t);
		ItemTypeTests after = mapper.selectByPrimaryKey(t);
		ItemTypeTestsHis his = new ItemTypeTestsHis();
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
		iItemTypeTestsHisService.insertSelective(request, his);
		return t;
	}

	private Float getPlantId(String plantCode) {
		Plant ser = new Plant();
		ser.setPlantCode(plantCode);
		return plantMapper.select(ser).get(0).getPlantId();
	}
}