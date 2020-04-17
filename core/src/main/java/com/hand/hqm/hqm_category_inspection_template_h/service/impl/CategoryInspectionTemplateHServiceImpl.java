package com.hand.hqm.hqm_category_inspection_template_h.service.impl;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.impl.RequestHelper;
import com.hand.hap.core.impl.ServiceRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.system.service.ICodeService;
import com.hand.hap.system.service.IPromptService;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import com.hand.hap.util.SystemApiMethod;
import com.hand.hcm.hcm_item.dto.Item;
import com.hand.hcm.hcm_item.mapper.ItemMapper;
import com.hand.hcm.hcm_item_b.mapper.ItemBMapper;
import com.hand.hcm.hcm_item_category.dto.ItemCategory;
import com.hand.hcm.hcm_item_category.mapper.ItemCategoryMapper;
import com.hand.hcm.hcm_item_category_assign.dto.ItemCategoryAssign;
import com.hand.hcm.hcm_item_category_assign.mapper.ItemCategoryAssignMapper;
import com.hand.hcm.hcm_object_events.mapper.ObjectEventsMapper;
import com.hand.hcm.hcm_plant.dto.Plant;
import com.hand.hcm.hcm_plant.mapper.PlantMapper;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.hand.hqm.hqm_category_inspection_template_h.dto.CategoryInspectionTemplateH;
import com.hand.hqm.hqm_category_inspection_template_h.mapper.CategoryInspectionTemplateHMapper;
import com.hand.hqm.hqm_category_inspection_template_h.service.ICategoryInspectionTemplateHService;
import com.hand.hqm.hqm_category_inspection_template_l.dto.CategoryInspectionTemplateL;
import com.hand.hqm.hqm_category_inspection_template_l.mapper.CategoryInspectionTemplateLMapper;
import com.hand.hqm.hqm_inspection_attribute.dto.InspectionAttribute;
import com.hand.hqm.hqm_inspection_attribute.mapper.InspectionAttributeMapper;
import com.hand.hqm.hqm_iqc_inspection_template_h.dto.IqcInspectionTemplateH;
import com.hand.hqm.hqm_iqc_inspection_template_h.mapper.IqcInspectionTemplateHMapper;
import com.hand.hqm.hqm_iqc_inspection_template_h.service.IIqcInspectionTemplateHService;
import com.hand.hqm.hqm_iqc_inspection_template_l.dto.IqcInspectionTemplateL;
import com.hand.hqm.hqm_iqc_inspection_template_l.dto.ValidateInfo;
import com.hand.hqm.hqm_iqc_inspection_template_l.mapper.IqcInspectionTemplateLMapper;
import com.hand.hqm.hqm_iqc_inspection_template_l.service.IIqcInspectionTemplateLService;
import com.hand.hqm.hqm_item_category_ext.mapper.ItemCategoryExtMapper;
import com.hand.hqm.hqm_sample_manage.dto.SampleManage;
import com.hand.hqm.hqm_sample_manage.mapper.SampleManageMapper;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

@Service
@Transactional(rollbackFor = Exception.class)
public class CategoryInspectionTemplateHServiceImpl extends BaseServiceImpl<CategoryInspectionTemplateH>
		implements ICategoryInspectionTemplateHService {
	@Autowired
	IqcInspectionTemplateHMapper iqcInspectionTemplateHMapper;
	@Autowired
	IIqcInspectionTemplateHService iIqcInspectionTemplateHService;
	@Autowired
	IqcInspectionTemplateLMapper iqcInspectionTemplateLMapper;
	@Autowired
	ItemCategoryMapper itemCategoryMapper;
	@Autowired
	ItemCategoryExtMapper itemCategoryExtMapper;
	@Autowired
	InspectionAttributeMapper inspectionAttributeMapper;
	@Autowired
	ICodeService iCodeService;
	@Autowired
	IPromptService iPromptService;
	@Autowired
	IIqcInspectionTemplateLService iIqcInspectionTemplateLService;
	@Autowired
	PlantMapper plantMapper;
	@Autowired
	ItemMapper itemMapper;
	@Autowired
	SampleManageMapper sampleManageMapper;
	@Autowired
	ItemBMapper itemBMapper;
	@Autowired
	ObjectEventsMapper objectEventsMapper;
	@Autowired
	CategoryInspectionTemplateLMapper categoryInspectionTemplateLMapper;
	@Autowired
	CategoryInspectionTemplateHMapper categoryInspectionTemplateHMapper;
	@Autowired
	ItemCategoryAssignMapper itemCategoryAssignMapper;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hand.hqm.hqm_category_inspection_template_h.service.
	 * ICategoryInspectionTemplateHService#excelUpload(javax.servlet.http.
	 * HttpServletRequest, java.lang.String)
	 */
	@Override
	public void excelUpload(HttpServletRequest request, String mainType) throws Exception {
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
		for (Map.Entry<String, MultipartFile> entry : fileMap.entrySet()) {
			MultipartFile forModel = entry.getValue();
			ResponseData res = inputDataFromExcel(request, RequestHelper.createServiceRequest(request),
					forModel.getInputStream(), mainType);
			if (!res.isSuccess())
				throw new RuntimeException(res.getMessage());
		}
	}

	public ResponseData inputDataFromExcel(HttpServletRequest request, IRequest requestContext, InputStream inputStream,
			String mainType) throws Exception {

		ResponseData responseData = new ResponseData();
		// TODO 解析
		Sheet sheet;
		XSSFWorkbook workBook;
		List<CategoryInspectionTemplateH> dealList = new ArrayList<CategoryInspectionTemplateH>();
		workBook = new XSSFWorkbook(inputStream);
		sheet = workBook.getSheetAt(0);
		// 校验结构
		checkInport(sheet, responseData, mainType);
		categoryInspectionTemplateLMapper.deleteAll();
		categoryInspectionTemplateHMapper.deleteAll();
		if (responseData.isSuccess()) {
			for (int i = 1; i <= sheet.getLastRowNum(); i++) {
				// i=1,从第二行开始
				Row rows = sheet.getRow(i);
				InspectionAttribute inspectionAttribute = new InspectionAttribute();
				inspectionAttribute.setInspectionAttribute(rows.getCell(6) == null ? null : rows.getCell(6).toString());
				inspectionAttribute.setInspectionTool(rows.getCell(11) == null ? null : rows.getCell(11).toString());
				List<InspectionAttribute> inspectionAttributeList = inspectionAttributeMapper
						.select(inspectionAttribute);
				if (inspectionAttributeList == null || inspectionAttributeList.size() == 0) {// 新增检验项目
					InspectionAttribute inspectionAttributeNew = new InspectionAttribute();
					inspectionAttributeNew
							.setInspectionAttribute(rows.getCell(6) == null ? null : rows.getCell(6).toString());
					ServiceRequest sr = new ServiceRequest();
					sr.setLocale("zh_CN");
					inspectionAttributeNew.setAttributeType(iCodeService.getCodeValueByMeaning(sr,
							"HQM_IQC_ATTRIBUTE_CATEGORY", rows.getCell(7) == null ? null : rows.getCell(7).toString()));
					inspectionAttributeNew.setQualityCharacterGrade(iCodeService.getCodeValueByMeaning(sr,
							"HQM_IQC_QUALITY_GRADE", rows.getCell(8) == null ? null : rows.getCell(8).toString()));
					inspectionAttributeNew.setStandardType(iCodeService.getCodeValueByMeaning(sr,
							"HQM_IQC_STANDARD_TYPE", rows.getCell(9) == null ? null : rows.getCell(9).toString()));
					inspectionAttributeNew
							.setInspectionTool(rows.getCell(11) == null ? null : rows.getCell(11).toString());
					inspectionAttributeNew
							.setInspectionMethod(rows.getCell(10) == null ? null : rows.getCell(10).toString());
					inspectionAttributeNew.setAttribute1(iCodeService.getCodeValueByMeaning(sr, "HQM_INSPECTION_PLACE",
							rows.getCell(12) == null ? null : rows.getCell(12).toString()));
					inspectionAttributeMapper.insertSelective(inspectionAttributeNew);

					// 头数据不存在则新增一条数据
					CategoryInspectionTemplateH iqcInspectionTemplateH = new CategoryInspectionTemplateH();
					Plant plant = new Plant();// 关联查询工厂
					plant.setPlantCode(rows.getCell(0) == null ? null : rows.getCell(0).toString());
					List<Plant> plantList = plantMapper.select(plant);
					if (plantList != null && plantList.size() > 0) {
						iqcInspectionTemplateH.setPlantId(plantList.get(0).getPlantId());
					}
					ItemCategory item = new ItemCategory();// 关联查询物料组
					String itemCode = rows.getCell(1) == null ? null : rows.getCell(1).toString();
					item.setCategoryCode(itemCode);
					item.setPlantId(plantList.get(0).getPlantId());
					List<ItemCategory> itemList = itemCategoryMapper.select(item);
					if (itemList != null && itemList.size() > 0) {
						iqcInspectionTemplateH.setCategoryId(itemList.get(0).getCategoryId());
					}
					if ("IQC".equals(mainType))
						iqcInspectionTemplateH
								.setItemEdition(rows.getCell(3) == null ? null : rows.getCell(3).toString());
					iqcInspectionTemplateH.setSourceType(iCodeService.getCodeValueByMeaning(requestContext,
							"HQM_SOURCE_TYPE_TEMP", rows.getCell(4) == null ? null : rows.getCell(4).toString()));
					List<CategoryInspectionTemplateH> iqcInspectionTemplateHList = categoryInspectionTemplateHMapper
							.select(iqcInspectionTemplateH);
					if (iqcInspectionTemplateHList != null && iqcInspectionTemplateHList.size() > 0) {
						// 新增行数据
						CategoryInspectionTemplateL iqcInspectionTemplateL = getCategoryInspectionTemplateL(rows,
								inspectionAttributeNew, iqcInspectionTemplateHList.get(0), mainType);
						iqcInspectionTemplateL.setPrecision(
								Float.valueOf(SystemApiMethod.getPercision(iqcInspectionTemplateL.getStandradFrom())));
						categoryInspectionTemplateLMapper.insertSelective(iqcInspectionTemplateL);
					} else {
						// 新增头数据
						iqcInspectionTemplateH.setStatus("4");
						iqcInspectionTemplateH.setMainType(mainType);
						iqcInspectionTemplateH.setEnableFlag("Y");
						SampleManage sampleManage = new SampleManage();
						sampleManage.setDescription(
								mainType.equals("IQC") ? (rows.getCell(13) == null ? null : rows.getCell(13).toString())
										: "成品量产抽样");
						List<SampleManage> sampleManageList = sampleManageMapper.select(sampleManage);
						if (sampleManageList != null && sampleManageList.size() > 0) {
							iqcInspectionTemplateH.setSampleWayId(sampleManageList.get(0).getSampleWayId());
						}
						if (rows.getCell(5) == null ? null
								: rows.getCell(5).toString() != null
										&& !"".equals(rows.getCell(5) == null ? null : rows.getCell(5).toString())) {
							iqcInspectionTemplateH.setTimeLimit(Float.valueOf(dealFloat(rows.getCell(5).toString())));
						}
						categoryInspectionTemplateHMapper.insertSelective(iqcInspectionTemplateH);
						dealList.add(iqcInspectionTemplateH);
						// 新增行数据
						CategoryInspectionTemplateL iqcInspectionTemplateL = getCategoryInspectionTemplateL(rows,
								inspectionAttributeNew, iqcInspectionTemplateH, mainType);
						iqcInspectionTemplateL.setPrecision(
								Float.valueOf(SystemApiMethod.getPercision(iqcInspectionTemplateL.getStandradFrom())));
						categoryInspectionTemplateLMapper.insertSelective(iqcInspectionTemplateL);
					}
				} else {
					// 更新检验项
					ServiceRequest sr = new ServiceRequest();
					sr.setLocale("zh_CN");
					InspectionAttribute iaUpdate = inspectionAttributeList.get(0);
					iaUpdate.setInspectionMethod(rows.getCell(10) == null ? null : rows.getCell(10).toString());
					iaUpdate.setStandardType(iCodeService.getCodeValueByMeaning(sr, "HQM_IQC_STANDARD_TYPE",
							rows.getCell(9) == null ? null : rows.getCell(9).toString()));
					iaUpdate.setAttribute1(iCodeService.getCodeValueByMeaning(sr, "HQM_INSPECTION_PLACE",
							rows.getCell(12) == null ? null : rows.getCell(12).toString()));
					iaUpdate.setQualityCharacterGrade(iCodeService.getCodeValueByMeaning(sr, "HQM_IQC_QUALITY_GRADE",
							rows.getCell(8) == null ? null : rows.getCell(8).toString()));
					inspectionAttributeMapper.updateByPrimaryKeySelective(iaUpdate);
					// 头数据不存在则新增一条数据
					CategoryInspectionTemplateH iqcInspectionTemplateH = new CategoryInspectionTemplateH();
					Plant plant = new Plant();// 关联查询工厂
					plant.setPlantCode(rows.getCell(0) == null ? null : rows.getCell(0).toString());
					List<Plant> plantList = plantMapper.select(plant);
					if (plantList != null && plantList.size() > 0) {
						iqcInspectionTemplateH.setPlantId(plantList.get(0).getPlantId());
					}
					ItemCategory item = new ItemCategory();// 关联查询物料组
					String itemCode = rows.getCell(1) == null ? null : rows.getCell(1).toString();
					item.setCategoryCode(itemCode);
					item.setPlantId(plantList.get(0).getPlantId());
					List<ItemCategory> itemList = itemCategoryMapper.select(item);
					if (itemList != null && itemList.size() > 0) {
						iqcInspectionTemplateH.setCategoryId(itemList.get(0).getCategoryId());
					}
					iqcInspectionTemplateH.setSourceType(iCodeService.getCodeValueByMeaning(requestContext,
							"HQM_SOURCE_TYPE_TEMP", rows.getCell(4) == null ? null : rows.getCell(4).toString()));
					if ("IQC".equals(mainType))
						iqcInspectionTemplateH
								.setItemEdition(rows.getCell(3) == null ? null : rows.getCell(3).toString());
					List<CategoryInspectionTemplateH> iqcInspectionTemplateHList = categoryInspectionTemplateHMapper
							.select(iqcInspectionTemplateH);
					if (iqcInspectionTemplateHList != null && iqcInspectionTemplateHList.size() > 0) {
						// 新增行数据
						InspectionAttribute inspectionAttributeR = inspectionAttributeList.get(0);
						CategoryInspectionTemplateL iqcInspectionTemplateL = getCategoryInspectionTemplateL(rows,
								iaUpdate, iqcInspectionTemplateHList.get(0), mainType);
						iqcInspectionTemplateL.setPrecision(
								Float.valueOf(SystemApiMethod.getPercision(iqcInspectionTemplateL.getStandradFrom())));
						categoryInspectionTemplateLMapper.insertSelective(iqcInspectionTemplateL);
					} else {
						// 新增头数据
						iqcInspectionTemplateH.setStatus("4");
						iqcInspectionTemplateH.setMainType(mainType);
						iqcInspectionTemplateH.setEnableFlag("Y");
						SampleManage sampleManage = new SampleManage();
						sampleManage.setDescription(
								mainType.equals("IQC") ? (rows.getCell(13) == null ? null : rows.getCell(13).toString())
										: "成品量产抽样");
						List<SampleManage> sampleManageList = sampleManageMapper.select(sampleManage);
						if (sampleManageList != null && sampleManageList.size() > 0) {
							iqcInspectionTemplateH.setSampleWayId(sampleManageList.get(0).getSampleWayId());
						}
						if (rows.getCell(5) == null ? null
								: rows.getCell(5).toString() != null
										&& !"".equals(rows.getCell(5) == null ? null : rows.getCell(5).toString())) {
							iqcInspectionTemplateH.setTimeLimit(Float.valueOf(dealFloat(rows.getCell(5).toString())));
						}
						categoryInspectionTemplateHMapper.insertSelective(iqcInspectionTemplateH);
						dealList.add(iqcInspectionTemplateH);
						// 新增行数据
						InspectionAttribute inspectionAttributeR = inspectionAttributeList.get(0);
						CategoryInspectionTemplateL iqcInspectionTemplateL = getCategoryInspectionTemplateL(rows,
								iaUpdate, iqcInspectionTemplateH, mainType);
						iqcInspectionTemplateL.setPrecision(
								Float.valueOf(SystemApiMethod.getPercision(iqcInspectionTemplateL.getStandradFrom())));
						categoryInspectionTemplateLMapper.insertSelective(iqcInspectionTemplateL);
					}
				}
			}

			/*
			 * 对新增的数据处理 删除检验单模板 并新增模板
			 */
			dealInsertData(dealList, requestContext, mainType);
			responseData.setMessage("导入成功" + sheet.getLastRowNum() + "条");

			try {
				workBook.close();
			} catch (Exception e) {

			}
			return responseData;
		} else {
			try {
				workBook.close();
			} catch (Exception e) {

			}
			return responseData;
		}
	}

	/**
	 * @description
	 * @author tianmin.wang
	 * @date 2020年3月30日
	 * @param dealList
	 * @param requestContext
	 * @param mainType
	 * @throws SecurityException
	 * @throws Exception
	 */
	private void dealInsertData(List<CategoryInspectionTemplateH> dealList, IRequest requestContext, String mainType)
			throws Exception {
		for (CategoryInspectionTemplateH hdto : dealList) {
			CategoryInspectionTemplateL searchL = new CategoryInspectionTemplateL();
			searchL.setHeaderId(hdto.getHeaderId());
			List<CategoryInspectionTemplateL> lResult = categoryInspectionTemplateLMapper.select(searchL);
			ItemCategoryAssign search = new ItemCategoryAssign();
			search.setCategoryId(hdto.getCategoryId());
			List<ItemCategoryAssign> assigns = itemCategoryAssignMapper.select(search);
			if (assigns.size() == 0)
				continue;
			for (ItemCategoryAssign assign : assigns) {// 删除数据
				// 查询需要删除的模板头
				IqcInspectionTemplateH temhSearch = new IqcInspectionTemplateH();
				temhSearch.setItemId(assign.getItemId());
				temhSearch.setSourceType(hdto.getSourceType());
				temhSearch.setMainType(mainType);
				List<IqcInspectionTemplateH> temhResult = iqcInspectionTemplateHMapper.select(temhSearch);
				for (IqcInspectionTemplateH temh : temhResult) {
					IqcInspectionTemplateL delete = new IqcInspectionTemplateL();
					delete.setHeaderId(temh.getHeaderId());
					List<IqcInspectionTemplateL> lList = iqcInspectionTemplateLMapper.select(delete);
					lList.forEach(p -> {
						iqcInspectionTemplateLMapper.deleteByPrimaryKey(p);
					});
					iqcInspectionTemplateHMapper.deleteByPrimaryKey(temh);
				}
				IqcInspectionTemplateH insert = new IqcInspectionTemplateH();
				Field[] fields = hdto.getClass().getDeclaredFields();
				for (Field field : fields) {
					field.setAccessible(true);
					if (field.getName().equals("categoryId"))
						continue;
					Field fieldNew = insert.getClass().getDeclaredField(field.getName());
					fieldNew.setAccessible(true);
					fieldNew.set(insert, field.get(hdto));
				}
				insert.setItemId(assign.getItemId());
				iIqcInspectionTemplateHService.insertSelective(requestContext, insert);
				for (CategoryInspectionTemplateL teml : lResult) {
					teml.setHeaderId(insert.getHeaderId());
					iIqcInspectionTemplateLService.insertSelective(requestContext, (IqcInspectionTemplateL) teml);
				}

			}
			// 生成数据
		}
	}

	public CategoryInspectionTemplateL getCategoryInspectionTemplateL(Row rows,
			InspectionAttribute inspectionAttributeR, CategoryInspectionTemplateH iqcInspectionTemplateH,
			String mainType) throws Exception {
		CategoryInspectionTemplateL iqcInspectionTemplateL = new CategoryInspectionTemplateL();
		iqcInspectionTemplateL.setHeaderId(iqcInspectionTemplateH.getHeaderId());
		iqcInspectionTemplateL.setAttributeId(inspectionAttributeR.getAttributeId());
		iqcInspectionTemplateL.setEnableType("D");
		iqcInspectionTemplateL.setInspectionAttribute(rows.getCell(6) == null ? null : rows.getCell(6).toString());
		iqcInspectionTemplateL.setSampleProcedureType(inspectionAttributeR.getSampleProcedureType());
		iqcInspectionTemplateL.setInspectionLevels(inspectionAttributeR.getInspectionLevels());
		iqcInspectionTemplateL.setQualityCharacterGrade(inspectionAttributeR.getQualityCharacterGrade());
		iqcInspectionTemplateL.setAcceptanceQualityLimit(inspectionAttributeR.getAcceptanceQualityLimit());
		iqcInspectionTemplateL.setStandardType(inspectionAttributeR.getStandardType());
		iqcInspectionTemplateL.setTextStandrad(rows.getCell(17) == null ? null : rows.getCell(17).toString());
		rows.getCell(14).setCellType(CellType.STRING);
		rows.getCell(15).setCellType(CellType.STRING);
		iqcInspectionTemplateL.setStandradFrom(rows.getCell(14) == null ? null : rows.getCell(14).getStringCellValue());
		iqcInspectionTemplateL.setStandradTo(rows.getCell(15) == null ? null : rows.getCell(15).getStringCellValue());
		iqcInspectionTemplateL.setStandradUom(rows.getCell(16) == null ? null : rows.getCell(16).toString());
		iqcInspectionTemplateL.setAttributeType(inspectionAttributeR.getAttributeType());
		iqcInspectionTemplateL.setFrequencyType(inspectionAttributeR.getFrequencyType());
		iqcInspectionTemplateL.setFrequency(inspectionAttributeR.getFrequency());
		iqcInspectionTemplateL.setInspectionTool(inspectionAttributeR.getInspectionTool());
		iqcInspectionTemplateL.setRemark(rows.getCell(18) == null ? null : rows.getCell(18).toString());
		iqcInspectionTemplateL.setEnableTime(new Date());
		iqcInspectionTemplateL.setSourceType(inspectionAttributeR.getSourceType());
		iqcInspectionTemplateL.setInspectionMethod(inspectionAttributeR.getInspectionMethod());
		if (rows.getCell(5) == null ? null
				: rows.getCell(5).toString() != null
						&& !"".equals(rows.getCell(5) == null ? null : rows.getCell(5).toString())) {
			iqcInspectionTemplateL.setTimeLimit(Float.valueOf(dealFloat(rows.getCell(5).toString())));
		}
		SampleManage sampleManage = new SampleManage();
		sampleManage.setDescription(
				mainType.equals("IQC") ? (rows.getCell(13) == null ? null : rows.getCell(13).toString()) : "成品量产抽样");
		List<SampleManage> sampleManageList = sampleManageMapper.select(sampleManage);
		if (sampleManageList != null && sampleManageList.size() > 0) {
			iqcInspectionTemplateL.setSampleWayId(sampleManageList.get(0).getSampleWayId());
		}

		return iqcInspectionTemplateL;
	}

	public String dealFloat(String val) {
		if (val != null && !"".equals(val)) {
			float f = Float.valueOf(val);
			int i = (int) f;
			float y = f - (float) i;
			if (y == 0f) {
				return "" + i;
			} else {
				return val;
			}
		} else {
			return "";
		}
	}

	public void checkInport(Sheet sheet, ResponseData responseData, String mainType) throws Exception {
		List<ValidateInfo> validateInfoList = new ArrayList<ValidateInfo>();
		for (int i = 1; i <= sheet.getLastRowNum(); i++) {
			// i=1,从第二行开始
			Row rows = sheet.getRow(i);
			if (rows == null) {
				responseData.setSuccess(false);
				responseData.setMessage("导入模板错误，请使用正确的模板进行导入！");
				return;
			} else {
				ValidateInfo validateInfo = new ValidateInfo();
				// 行解析
				if (rows.getCell(0) == null) {
					continue;
				} else {
					// 结构
					validateInfo.setPlant(rows.getCell(0) == null ? null : rows.getCell(0).toString());
					String itemCode = rows.getCell(1) == null ? null : rows.getCell(1).toString();
					if (itemCode == null) {
						responseData.setSuccess(false);
						responseData.setMessage("第" + i + "行，物料组编码为空！");
						return;
					}
					validateInfo.setCategoryCode(itemCode);
					validateInfo.setItemVersion(rows.getCell(3) == null ? null : rows.getCell(3).toString());
					validateInfo.setAttributeName(rows.getCell(6) == null ? null : rows.getCell(6).toString());
					validateInfo.setAttributeTool(rows.getCell(11) == null ? null : rows.getCell(11).toString());
					validateInfoList.add(validateInfo);

					CategoryInspectionTemplateH iqcInspectionTemplateH = new CategoryInspectionTemplateH();
					// 校验工厂
					Plant plant = new Plant();
					plant.setPlantCode(validateInfo.getPlant());
					List<Plant> plantList = plantMapper.select(plant);
					if (plantList != null && plantList.size() > 0) {
						iqcInspectionTemplateH.setPlantId(plantList.get(0).getPlantId());
					} else {
						responseData.setSuccess(false);
						responseData.setMessage("第" + i + "行，工厂编码不存在！");
						return;
					}

					// 校验物料
					ItemCategory item = new ItemCategory();
					item.setCategoryCode(validateInfo.getCategoryCode());
					List<ItemCategory> itemList = itemCategoryMapper.select(item);
					if (itemList != null && itemList.size() > 0) {
						iqcInspectionTemplateH.setCategoryId(itemList.get(0).getCategoryId());
					} else {
						responseData.setSuccess(false);
						responseData.setMessage("第" + i + "行，物料组编码不存在！");
						return;
					}

					// 校验检验性质
					ServiceRequest sr = new ServiceRequest();
					sr.setLocale("zh_CN");
					String uri = iCodeService.getCodeValueByMeaning(sr, "HQM_SOURCE_TYPE_TEMP",
							rows.getCell(4) == null ? null : rows.getCell(4).toString());
					iqcInspectionTemplateH.setSourceType(uri);
					validateInfo.setSourceType(iqcInspectionTemplateH.getSourceType());
					if (uri == null) {
						responseData.setSuccess(false);
						responseData.setMessage("第" + i + "行，检验性质不存在！");
						return;
					}
					// 校验检验项目
					iqcInspectionTemplateH.setItemEdition(rows.getCell(3) == null ? null : rows.getCell(3).toString());
//					InspectionAttribute inspectionAttribute = new InspectionAttribute();
//					inspectionAttribute.setInspectionAttribute(validateInfo.getAttributeName());
//					inspectionAttribute.setInspectionTool(validateInfo.getAttributeTool());
					List<CategoryInspectionTemplateH> iqcInspectionTemplateHList = categoryInspectionTemplateHMapper
							.select(iqcInspectionTemplateH);
					if (iqcInspectionTemplateHList != null && iqcInspectionTemplateHList.size() > 0) {
						responseData.setSuccess(false);
						responseData.setMessage("第" + i + "行，检验数据重复！");
						return;
					}
					// 校验检验项类型
					if (iCodeService.getCodeValueByMeaning(sr, "HQM_IQC_ATTRIBUTE_CATEGORY",
							rows.getCell(7) == null ? null : rows.getCell(7).toString()) == null) {
						responseData.setSuccess(false);
						responseData.setMessage("第" + i + "行，检验项类型不存在 ！");
						return;
					}
					// 校验质量等级
					if ("FQC".equals(mainType) && iCodeService.getCodeValueByMeaning(sr, "HQM_IQC_QUALITY_GRADE",
							rows.getCell(8) == null ? null : rows.getCell(8).toString()) == null) {
						responseData.setSuccess(false);
						responseData.setMessage("第" + i + "行，质量等级不存在 ！");
						return;
					}

					// 校验规格类型
					sr.setLocale("zh_CN");
					if (iCodeService.getCodeValueByMeaning(sr, "HQM_IQC_STANDARD_TYPE",
							rows.getCell(9) == null ? null : rows.getCell(9).toString()) == null) {
						responseData.setSuccess(false);
						responseData.setMessage("第" + i + "行，规格类型不存在 ！");
						return;
					}
					if ("计量".equals(iCodeService.getCodeValueByMeaning(sr, "HQM_IQC_STANDARD_TYPE",
							rows.getCell(9) == null ? null : rows.getCell(9).toString()))) {
						rows.getCell(14).setCellType(CellType.STRING);
						rows.getCell(15).setCellType(CellType.STRING);
						if (StringUtils.isEmpty(rows.getCell(14).getStringCellValue())
								&& StringUtils.isEmpty(rows.getCell(15).getStringCellValue())) {
							responseData.setSuccess(false);
							responseData.setMessage("第" + i + "行，规格上下限不能同时为空 ！");
							return;
						}

						if (!StringUtils.isEmpty(rows.getCell(14).getStringCellValue())
								&& !StringUtils.isEmpty(rows.getCell(15).getStringCellValue())) {// 两个值都不为空时 开始 比较精度
																									// 精度需要保持一致
							if (!SystemApiMethod.judgePercision(rows.getCell(14).getStringCellValue(),
									rows.getCell(15).getStringCellValue())) {
								responseData.setSuccess(false);
								responseData.setMessage("第" + i + "行，规格上下限精度不一致");
								return;
							}
						}

					}
					// 校验委托/自测
					if (iCodeService.getCodeValueByMeaning(sr, "HQM_INSPECTION_PLACE",
							rows.getCell(12) == null ? null : rows.getCell(12).toString()) == null) {
						responseData.setSuccess(false);
						responseData.setMessage("第" + i + "行，委托/自测值不存在 ！");
						return;
					}
					if (mainType.equals("IQC")) {
						// 校验抽样方式
						SampleManage sampleManage = new SampleManage();
						sampleManage.setDescription(rows.getCell(13) == null ? null : rows.getCell(13).toString());
						List<SampleManage> sampleManageList = sampleManageMapper.select(sampleManage);
						if (sampleManageList == null || sampleManageList.size() == 0) {
							responseData.setSuccess(false);
							responseData.setMessage("第" + i + "行，抽样方式不存在 ！");
							return;
						}
					}
				}
			}

		}

		// excel维护结构校验
		if (!"".equals(validateSingleInExcel(validateInfoList))) {
			responseData.setSuccess(false);
			responseData.setMessage("excel内结构不能重复！" + validateSingleInExcel(validateInfoList));
		}
	}

	private String validateSingleInExcel(List<ValidateInfo> vdis) {
		if (vdis.size() == 1) {
			return "";
		}
		for (int i = 0; i < vdis.size(); i++) {
			ValidateInfo vdi = vdis.get(i);

			for (int j = i + 1; j < vdis.size(); j++) {
				boolean check = false;
				if (!vdi.getPlant().equals(vdis.get(j).getPlant())) {
					check = true;
				}
				if (!vdi.getCategoryCode().equals(vdis.get(j).getCategoryCode())) {
					check = true;
				}
				if (!vdi.getItemVersion().equals(vdis.get(j).getItemVersion())) {
					check = true;
				}
				if (!vdi.getAttributeName().equals(vdis.get(j).getAttributeName())) {
					check = true;
				}
				if (!vdi.getAttributeTool().equals(vdis.get(j).getAttributeTool())) {
					check = true;
				}
				if (!vdi.getSourceType().equals(vdis.get(j).getSourceType())) {
					check = true;
				}
				if (!check) {
					return "第" + i + "行与第" + j + "行数据重复。";
				}
			}
		}
		return "";
	}

}