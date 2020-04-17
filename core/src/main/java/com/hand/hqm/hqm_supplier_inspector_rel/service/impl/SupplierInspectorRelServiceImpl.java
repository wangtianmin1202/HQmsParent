package com.hand.hqm.hqm_supplier_inspector_rel.service.impl;

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
import com.hand.hcm.hcm_plant.dto.Plant;
import com.hand.hcm.hcm_plant.mapper.PlantMapper;
import com.hand.hcs.hcs_suppliers.mapper.SuppliersMapper;
import com.hand.hcs.hcs_supply_demand.dto.SupplyDemand;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.hand.hqm.hqm_supplier_inspector_rel.dto.SupplierInspectorRel;
import com.hand.hqm.hqm_supplier_inspector_rel.mapper.SupplierInspectorRelMapper;
import com.hand.hqm.hqm_supplier_inspector_rel.service.ISupplierInspectorRelService;
import com.hand.hqm.hqm_supplier_inspector_rel_his.dto.SupplierInspectorRelHis;
import com.hand.hqm.hqm_supplier_inspector_rel_his.service.ISupplierInspectorRelHisService;
import com.hand.sys.sys_user.mapper.UserSysMapper;

import jodd.util.StringUtil;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional(rollbackFor = Exception.class)
public class SupplierInspectorRelServiceImpl extends BaseServiceImpl<SupplierInspectorRel>
		implements ISupplierInspectorRelService {
	private String MiddleString = " 修改为 ";
	@Autowired
	SupplierInspectorRelMapper mapper;
	@Autowired
	IPromptService iPromptService;
	@Autowired
	ICodeService iCodeService;
	@Autowired
	PlantMapper plantMapper;
	@Autowired
	UserSysMapper userSysMapper;
	@Autowired
	SuppliersMapper suppliersMapper;
	@Autowired
	ISupplierInspectorRelHisService iSupplierInspectorRelHisService;
	@Autowired
	IObjectEventsService iObjectEventsService;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.hand.hqm.hqm_supplier_inspector_rel.service.ISupplierInspectorRelService#
	 * reSelect(com.hand.hap.core.IRequest,
	 * com.hand.hqm.hqm_supplier_inspector_rel.dto.SupplierInspectorRel, int, int)
	 */
	@Override

	public List<SupplierInspectorRel> reSelect(IRequest requestContext, SupplierInspectorRel dto, int page,
			int pageSize) {
		PageHelper.startPage(page, pageSize);
		return mapper.reSelect(dto);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.hand.hqm.hqm_supplier_inspector_rel.service.ISupplierInspectorRelService#
	 * excelImport(com.hand.hap.core.IRequest,
	 * org.springframework.web.multipart.MultipartFile)
	 */
	@Override
	public List<SupplierInspectorRel> excelImport(IRequest requestCtx, MultipartFile forModel) {
		Sheet sheet;
		XSSFWorkbook workBook;
		List<SupplierInspectorRel> totalList = new ArrayList<SupplierInspectorRel>();
		try {
			workBook = new XSSFWorkbook(forModel.getInputStream());
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage());
		}
		sheet = workBook.getSheetAt(0);
		List<Plant> plants = plantMapper.selectAll();
		// 默认工厂
		// String defaultPlantCode = iCodeService.getCodeValuesByCode(requestCtx,
		// "HQM_DEFAULT_PLANT").get(0).getMeaning();
		// Plant searchPlant = new Plant();
		// searchPlant.setPlantCode(defaultPlantCode);
		// Float defaultPlantId = plantMapper.select(searchPlant).get(0).getPlantId();
		String errorMessage = SystemApiMethod.getPromptDescription(requestCtx, iPromptService,
				"hqm_supplier_inspector_rel_error1");
		String errorMessage2 = SystemApiMethod.getPromptDescription(requestCtx, iPromptService,
				"hqm_supplier_inspector_rel_error2");

		for (int i = 0; i <= sheet.getLastRowNum(); i++) {
			Row row = sheet.getRow(i);
			for (int j = 0; j < row.getLastCellNum(); j++) {
				if (row.getCell(j) != null) {
					row.getCell(j).setCellType(CellType.STRING);
				}
			}
		}
		for (int i = 1; i <= sheet.getLastRowNum(); i++) {// i=1 从第二行开始 忽略标题行

			Row row = sheet.getRow(i);

			if (row.getCell(0) == null || row.getCell(1) == null || row.getCell(3) == null
					|| StringUtil.isEmpty(row.getCell(0).getStringCellValue())
					|| StringUtil.isEmpty(row.getCell(1).getStringCellValue())
					|| StringUtil.isEmpty(row.getCell(3).getStringCellValue())) {
				throw new RuntimeException(errorMessage);
			}

			SupplierInspectorRel insert = new SupplierInspectorRel();
			// 数据项构建
			insert.setPlantId(getPlantIdByCode(plants, row.getCell(0).getStringCellValue()));

			insert.setSupplierId(getSupplierIdByCode(requestCtx, row.getCell(1).getStringCellValue()));

			insert.setInspectorId(getUserIdByEmployeeName(errorMessage2, row.getCell(3).getStringCellValue()));
			if (row.getCell(4) != null && !StringUtil.isEmpty(row.getCell(4).getStringCellValue())) {
				insert.setPreInspectorId(getUserIdByEmployeeName(errorMessage2, row.getCell(4).getStringCellValue()));
			}
			if (row.getCell(5) != null && !StringUtil.isEmpty(row.getCell(5).getStringCellValue())) {
				insert.setSqeInspectorId(getUserIdByEmployeeName(errorMessage2, row.getCell(5).getStringCellValue()));
			}
			if (row.getCell(6) == null || StringUtil.isEmpty(row.getCell(6).getStringCellValue())) {
				insert.setEnableFlag("Y");
			} else {
				insert.setEnableFlag(row.getCell(6).getStringCellValue());
			}
			self().insertSelective(requestCtx, insert);
			totalList.add(insert);
		}
		try {
			workBook.close();
		} catch (IOException e) {
		}
		return totalList;
	}

	/**
	 * @description
	 * @author tianmin.wang
	 * @date 2019年12月19日
	 * @param stringCellValue
	 * @return
	 */
	private Float getSupplierIdByCode(IRequest requestCtx, String stringCellValue) {
		Float ret = suppliersMapper.getSupplierIdByCode(stringCellValue);
		if (ret == null) {
			String errorMessage3 = MessageFormat.format(SystemApiMethod.getPromptDescription(requestCtx, iPromptService,
					"hqm_supplier_inspector_rel_error3"), stringCellValue);
			throw new RuntimeException(errorMessage3);
		}
		return ret;
	}

	public Float getPlantIdByCode(List<Plant> list, String plantCode) {
		List<Plant> rel = list.stream().filter(p -> p.getPlantCode().equals(plantCode)).collect(Collectors.toList());
		return rel.get(0).getPlantId();
	}

	public Float getUserIdByEmployeeName(String error, String employeeName) {
		Float result = userSysMapper.getUserIdByEmployeeName(employeeName);
		if (result == null) {
			throw new RuntimeException(MessageFormat.format(error, employeeName));
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.hand.hqm.hqm_supplier_inspector_rel.service.ISupplierInspectorRelService#
	 * batchSave(com.hand.hap.core.IRequest,
	 * com.hand.hqm.hqm_supplier_inspector_rel.dto.SupplierInspectorRel)
	 */
	@Override
	public void batchSave(IRequest requestContext, SupplierInspectorRel dto) {
		mapper.batchUpdateIns(dto);
		mapper.batchUpdatePre(dto);
		mapper.batchUpdateSqe(dto);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.hand.hqm.hqm_supplier_inspector_rel.service.ISupplierInspectorRelService#
	 * reBatchUpdate(com.hand.hap.core.IRequest, java.util.List)
	 */
	@Override
	public List<SupplierInspectorRel> reBatchUpdate(IRequest request, List<SupplierInspectorRel> list) {
		for (SupplierInspectorRel t : list) {
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
	public SupplierInspectorRel updateByPrimaryKeySelectiveRecord(IRequest request, SupplierInspectorRel t) {
		SupplierInspectorRel now = mapper.selectByPrimaryKey(t);
		self().updateByPrimaryKeySelective(request, t);
		SupplierInspectorRel after = mapper.selectByPrimaryKey(t);
		SupplierInspectorRelHis his = new SupplierInspectorRelHis();
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
		iSupplierInspectorRelHisService.insertSelective(request, his);
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
	private void objectEventsRecord(SupplierInspectorRel now, SupplierInspectorRel after, SupplierInspectorRelHis his,
			IRequest request) {
		ObjectEvents oe = new ObjectEvents();
		oe.setEventId(his.getEventId());
		oe.setEventTime(his.getEventTime());
		oe.setEventBy(his.getEventBy());
		oe.setEventTable("HQM_SUPPLIER_INSPECTOR_REL");
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
								"supplierinspectorrel." + nowField.getName().toLowerCase()) + ":"
								+ changeInfoGetNumber(nowFloat, afterFloat, nowField.getName()) + "<br/>";
					}
				} else if (nowField.getType() == String.class) {
					String nowString = nowField.get(now) == null ? "" : (String) nowField.get(now);
					String afterString = afterField.get(after) == null ? "" : (String) afterField.get(after);
					if (!(nowString).equals(afterString)) {
						eventContent += SystemApiMethod.getPromptDescription(request, iPromptService,
								"supplierinspectorrel." + nowField.getName().toLowerCase()) + ":"
								+ changeInfoGetString(nowString, afterString, nowField.getName()) + "<br/>";
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

	private String changeInfoGetNumber(Float now, Float after, String fieldName) {
		String str1 = "";
		String str2 = "";
		if ("inspectorId".equals(fieldName) || "preInspectorId".equals(fieldName)
				|| "sqeInspectorId".equals(fieldName)) {
			str1 = userSysMapper.getEmployeeNameByUserId(now);
			str2 = userSysMapper.getEmployeeNameByUserId(after);
		} else if ("supplierId".equals(fieldName)) {
			str1 = suppliersMapper.getSupplierNameById(now);
			str2 = suppliersMapper.getSupplierNameById(after);
		} else {
			str1 = String.valueOf(now.intValue());
			str2 = String.valueOf(after.intValue());
		}
		return str1 + MiddleString + str2;
	}

	private String changeInfoGetString(String now, String after, String fieldName) {
		return now + MiddleString + after;
	}

	@Override
	public SupplierInspectorRel insertSelectiveRecord(IRequest request, SupplierInspectorRel t) {
		self().insertSelective(request, t);
		SupplierInspectorRel after = mapper.selectByPrimaryKey(t);
		SupplierInspectorRelHis his = new SupplierInspectorRelHis();
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
		iSupplierInspectorRelHisService.insertSelective(request, his);
		return t;
	}
}