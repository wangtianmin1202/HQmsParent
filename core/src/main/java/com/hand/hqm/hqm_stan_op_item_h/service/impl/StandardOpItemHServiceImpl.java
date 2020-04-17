package com.hand.hqm.hqm_stan_op_item_h.service.impl;

import com.github.pagehelper.PageHelper;
import com.hand.hap.activiti.service.IActivitiService;
import com.hand.hap.core.IRequest;
import com.hand.hap.core.impl.ServiceRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.system.service.ICodeService;
import com.hand.hap.system.service.IPromptService;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import com.hand.hap.util.SystemApiMethod;
import com.hand.hcm.hcm_item.dto.Item;
import com.hand.hcm.hcm_item.mapper.ItemMapper;
import com.hand.hcm.hcm_item_b.dto.ItemB;
import com.hand.hcm.hcm_item_b.mapper.ItemBMapper;
import com.hand.hcm.hcm_object_events.dto.ObjectEvents;
import com.hand.hcm.hcm_object_events.mapper.ObjectEventsMapper;
import com.hand.hcm.hcm_object_events.service.IObjectEventsService;
import com.hand.hcm.hcm_plant.dto.Plant;
import com.hand.hcm.hcm_plant.mapper.PlantMapper;
import com.hand.hcm.hcm_production_line.dto.ProductionLine;
import com.hand.hcm.hcm_production_line.mapper.ProductionLineMapper;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.activiti.rest.service.api.runtime.process.ProcessInstanceCreateRequest;
import org.apache.ibatis.javassist.Modifier;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.drools.core.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.hand.hqm.hqm_inspection_attribute.dto.InspectionAttribute;
import com.hand.hqm.hqm_inspection_attribute.mapper.InspectionAttributeMapper;
import com.hand.hqm.hqm_sop_pqc_control_h.dto.SopPqcControlH;
import com.hand.hqm.hqm_sop_pqc_control_h.mapper.SopPqcControlHMapper;
import com.hand.hqm.hqm_sop_pqc_control_l.dto.SopPqcControlL;
import com.hand.hqm.hqm_sop_pqc_control_l.mapper.SopPqcControlLMapper;
import com.hand.hqm.hqm_stan_op_item_h.dto.PqcValidateInfo;
import com.hand.hqm.hqm_stan_op_item_h.dto.StandardOpItemH;
import com.hand.hqm.hqm_stan_op_item_h.mapper.StandardOpItemHMapper;
import com.hand.hqm.hqm_stan_op_item_h.service.IStandardOpItemHService;
import com.hand.hqm.hqm_stan_op_item_l.dto.StandardOpItemL;
import com.hand.hqm.hqm_stan_op_item_l.mapper.StandardOpItemLMapper;
import com.hand.hqm.hqm_stan_op_item_l.service.IStandardOpItemLService;
import com.hand.hqm.hqm_standard_op_item_h_his.dto.StandardOpItemHHis;
import com.hand.hqm.hqm_standard_op_item_h_his.mapper.StandardOpItemHHisMapper;
import com.hand.hqm.hqm_standard_op_item_h_his.service.IStandardOpItemHHisService;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class StandardOpItemHServiceImpl extends BaseServiceImpl<StandardOpItemH> implements IStandardOpItemHService {
	private String MiddleString = " 修改为 ";
	@Autowired
	StandardOpItemHMapper standardOpItemHMapper;
	@Autowired
	IStandardOpItemLService iStandardOpItemLService;
	@Autowired
	StandardOpItemLMapper standardOpItemLMapper;
	@Autowired
	PlantMapper plantMapper;
	@Autowired
	ProductionLineMapper productionLineMapper;
	@Autowired
	ItemMapper itemMapper;
	@Autowired
	ICodeService iCodeService;
	@Autowired
	SopPqcControlLMapper sopPqcControlLMapper;
	@Autowired
	SopPqcControlHMapper sopPqcControlHMapper;
	@Autowired
	InspectionAttributeMapper inspectionAttributeMapper;
	@Autowired
	private IActivitiService activitiService;
	@Autowired
	IObjectEventsService iObjectEventsService;
	@Autowired
	IPromptService iPromptService;
	@Autowired
	IStandardOpItemHHisService iStandardOpItemHHisService;
	@Autowired
	ItemBMapper itemBMapper;
	@Autowired
	StandardOpItemHHisMapper standardOpItemHHisMapper;
	@Autowired
	ObjectEventsMapper objectEventsMapper;

	@Override
	public List<StandardOpItemH> myselect(IRequest requestContext, StandardOpItemH dto, int page, int pageSize) {
		PageHelper.startPage(page, pageSize);
		return standardOpItemHMapper.myselect(dto);
	}

	@Override
	public int reBatchDelete(List<StandardOpItemH> list) {
		int c = 0;
		for (StandardOpItemH t : list) {
			StandardOpItemL search = new StandardOpItemL();
			search.setHeadId(t.getHeadId());
			List<StandardOpItemL> lineList = standardOpItemLMapper.select(search);
			iStandardOpItemLService.batchDelete(lineList);
			c += self().deleteByPrimaryKey(t);
		}
		return c;
	}

	@Override
	public List<StandardOpItemH> save(IRequest requestContext, StandardOpItemH dto) {
		// TODO Auto-generated method stub
		List<StandardOpItemH> result = new ArrayList<>();
		if (dto.getHeadId().intValue() == -1) {
			// 新增
			result.add(insertSelectiveRecord(requestContext, dto));
		} else {
			// 更新
			result.add(updateByPrimaryKeySelectiveRecord(requestContext, dto));
		}
		StandardOpItemH Search = new StandardOpItemH();
		Search.setHeadId(result.get(0).getHeadId());
		return standardOpItemHMapper.myselect(Search);
	}

	@Override
	public void updateStatus(IRequest requestCtx, List<StandardOpItemH> dto) {
		// TODO 发布
		for (StandardOpItemH in : dto) {
			StandardOpItemH search = new StandardOpItemH();
			search.setHeadId(in.getHeadId());
			search = standardOpItemHMapper.selectByPrimaryKey(search);
			StandardOpItemH updateDto = new StandardOpItemH();
			updateDto.setHeadId(search.getHeadId());
			updateDto.setVersion(String.valueOf(Integer.valueOf(search.getVersion()) + 1));
			updateDto.setStatus("2");
			updateByPrimaryKeySelectiveRecord(requestCtx, updateDto);
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public ResponseData inputDataFromExcel(HttpServletRequest request, IRequest requestContext, InputStream inputStream)
			throws Exception {
		ResponseData responseData = new ResponseData();
		// TODO 解析
		Sheet sheet;
		XSSFWorkbook workBook;

		workBook = new XSSFWorkbook(inputStream);
		sheet = workBook.getSheetAt(0);
		// 校验结构
		checkInport(sheet, responseData);
		if (responseData.isSuccess()) {
			for (int i = 1; i <= sheet.getLastRowNum(); i++) {
				// i=1,从第二行开始
				Row rows = sheet.getRow(i);
				// 新增检验模板头对象
				StandardOpItemH standardOpItemH = new StandardOpItemH();
				// 产线工序不存在则新增一条数据
				SopPqcControlH sopPqcControlH = new SopPqcControlH();
				ProductionLine productionLine = new ProductionLine();
				productionLine.setDescriptions(rows.getCell(1) == null ? null : rows.getCell(1).toString());
				List<ProductionLine> productionLineList = productionLineMapper.select(productionLine);
				if (productionLineList != null && productionLineList.size() > 0) {
					sopPqcControlH.setProdLineId(productionLineList.get(0).getProdLineId());
					standardOpItemH.setProdLineId(productionLineList.get(0).getProdLineId());
				}
				sopPqcControlH.setStandardOpCode(rows.getCell(2) == null ? null : rows.getCell(2).toString());
				List<SopPqcControlH> sopPqcControlHList = sopPqcControlHMapper.select(sopPqcControlH);
				if (sopPqcControlHList != null && sopPqcControlHList.size() > 0) {
					standardOpItemH.setStandardOpId(sopPqcControlHList.get(0).getStandardOpId());
					// 查询工厂
					Plant plant = new Plant();
					plant.setPlantCode(rows.getCell(0) == null ? null : rows.getCell(0).toString());
					List<Plant> plantList = plantMapper.select(plant);
					if (plantList != null && plantList.size() > 0) {
						standardOpItemH.setPlantId(String.valueOf(plantList.get(0).getPlantId()));
					}
				} else {// 不存在
					SopPqcControlH sopPqcControlHNew = new SopPqcControlH();
					// 查询工厂
					Plant plant = new Plant();
					plant.setPlantCode(rows.getCell(0) == null ? null : rows.getCell(0).toString());
					List<Plant> plantList = plantMapper.select(plant);
					if (plantList != null && plantList.size() > 0) {
						sopPqcControlHNew.setPlantId(plantList.get(0).getPlantId());
						standardOpItemH.setPlantId(String.valueOf(plantList.get(0).getPlantId()));
					}
					sopPqcControlHNew.setProdLineId(productionLineList.get(0).getProdLineId());
					sopPqcControlHNew
							.setStandardOpCode(rows.getCell(2) == null ? null : rows.getCell(2).toString());
					sopPqcControlHNew.setStandardOpDes(rows.getCell(3) == null ? null : rows.getCell(3).toString());
					sopPqcControlHNew.setKeyOpFlag("Y");
					sopPqcControlHNew.setPqcFlag("Y");
					sopPqcControlHNew.setIqFlag("Y");
					sopPqcControlHNew.setEnableFlag("Y");
					sopPqcControlHMapper.insertSelective(sopPqcControlHNew);

					standardOpItemH.setStandardOpId(sopPqcControlHNew.getStandardOpId());
				}

				// 工位不存在则新增一条数据
				SopPqcControlL sopPqcControlL = new SopPqcControlL();
				sopPqcControlL.setWorkstationCode(rows.getCell(4) == null ? null : rows.getCell(4).toString());
				List<SopPqcControlL> sopPqcControlLList = sopPqcControlLMapper.select(sopPqcControlL);
				if (sopPqcControlLList != null && sopPqcControlLList.size() > 0) {
					standardOpItemH.setWorkstationId(sopPqcControlLList.get(0).getWorkstationId());
				} else {// 不存在
					SopPqcControlL sopPqcControlLNew = new SopPqcControlL();
					sopPqcControlLNew.setStandardOpId(standardOpItemH.getStandardOpId());
					sopPqcControlLNew
							.setWorkstationCode(rows.getCell(4) == null ? null : rows.getCell(4).toString());
					sopPqcControlLNew
							.setWorkstationDes(rows.getCell(5) == null ? null : rows.getCell(5).toString());
					sopPqcControlLMapper.insertSelective(sopPqcControlLNew);

					standardOpItemH.setWorkstationId(sopPqcControlLNew.getWorkstationId());
				}

				standardOpItemH.setStatus("4");
				// 查询物料
				Item item = new Item();
				String itemCode = rows.getCell(6) == null ? null : rows.getCell(6).toString();
				if(itemCode.contains(".")) {
					item.setItemCode(itemCode.substring(0, itemCode.indexOf(".")));
				} else {
					item.setItemCode(itemCode);
				}
				Plant plant = new Plant();
				plant.setPlantCode(rows.getCell(0) == null ? null : rows.getCell(0).toString());
				List<Plant> plantList = plantMapper.select(plant);
				if (plantList != null && plantList.size() > 0) {
					item.setPlantId(plantList.get(0).getPlantId());
				}
				List<Item> itemList = itemMapper.select(item);
				if (itemList != null && itemList.size() > 0) {
					standardOpItemH.setItemId(itemList.get(0).getItemId());
				}
				standardOpItemH.setSourceType("7");
				standardOpItemH.setItemEdition(rows.getCell(8) == null ? null : rows.getCell(8).toString());
				String timeliS = rows.getCell(10) == null ? null : rows.getCell(10).toString();
				standardOpItemH.setTimeLimit(Float.valueOf(timeliS));
				standardOpItemH.setVersion("1");
				standardOpItemH.setEnableFlag("Y");
				
				
				// 新增检验模板行对象
				StandardOpItemL standardOpItemL = new StandardOpItemL();
				// 查看检验项目是否存在，不存在新增一条数据
				InspectionAttribute inspectionAttribute = new InspectionAttribute();
				inspectionAttribute
						.setInspectionAttribute(rows.getCell(11) == null ? null : rows.getCell(11).toString());
				List<InspectionAttribute> inspectionAttributeList = inspectionAttributeMapper
						.select(inspectionAttribute);
				Float attributeId;
				if (inspectionAttributeList != null && inspectionAttributeList.size() > 0) {
					attributeId = inspectionAttributeList.get(0).getAttributeId();
				} else {
					InspectionAttribute inspectionAttributeNew = new InspectionAttribute();
					inspectionAttributeNew
							.setInspectionAttribute(rows.getCell(11) == null ? null : rows.getCell(11).toString());
					ServiceRequest sr = new ServiceRequest();
					sr.setLocale("zh_CN");
					inspectionAttributeNew
							.setAttributeType(iCodeService.getCodeValueByMeaning(sr, "HQM_IQC_ATTRIBUTE_CATEGORY",
									rows.getCell(12) == null ? null : rows.getCell(12).toString()));
					inspectionAttributeNew.setQualityCharacterGrade(
							iCodeService.getCodeValueByMeaning(sr, "HQM_IQC_QUALITY_GRADE",
									rows.getCell(13) == null ? null : rows.getCell(13).toString()));
					inspectionAttributeNew
							.setStandardType(iCodeService.getCodeValueByMeaning(sr, "HQM_IQC_STANDARD_TYPE",
									rows.getCell(14) == null ? null : rows.getCell(14).toString()));
					inspectionAttributeNew
							.setInspectionTool(rows.getCell(16) == null ? null : rows.getCell(16).toString());
					inspectionAttributeNew
							.setInspectionMethod(iCodeService.getCodeValueByMeaning(sr, "HQM_INSPECTION_METHOD",
									rows.getCell(15) == null ? null : rows.getCell(15).toString()));

					inspectionAttributeMapper.insertSelective(inspectionAttributeNew);
					attributeId = inspectionAttributeNew.getAttributeId();
					standardOpItemH.setHsoHeadId(inspectionAttributeNew.getAttributeId());
				}
				
				//头数据不存在则新增一条数据
				StandardOpItemH standardOpItemHIsexist = new StandardOpItemH();
				standardOpItemHIsexist.setPlantId(standardOpItemH.getPlantId());
				standardOpItemHIsexist.setItemId(standardOpItemH.getItemId());
				standardOpItemHIsexist.setSourceType(standardOpItemH.getSourceType());
				List<StandardOpItemH> standardOpItemHList = standardOpItemHMapper.select(standardOpItemHIsexist);
				if(standardOpItemHList != null && standardOpItemHList.size() > 0) {
					standardOpItemL.setHeadId(standardOpItemHList.get(0).getHeadId());
				} else {//不存在，新增数据
					if(rows.getCell(10) == null ? null : rows.getCell(10).toString() != null
							&& !"".equals(rows.getCell(10) == null ? null : rows.getCell(10).toString())) {
						standardOpItemH.setTimeLimit(Float.valueOf(dealFloat(rows.getCell(10).toString())));
					}
					standardOpItemHMapper.insertSelective(standardOpItemH);
					standardOpItemL.setHeadId(standardOpItemH.getHeadId());
				}
				
				standardOpItemL.setAttributeId(attributeId);
				float sampleSize = 1;
				standardOpItemL.setSampleSize(sampleSize);
				rows.getCell(17).setCellType(CellType.STRING);
				rows.getCell(18).setCellType(CellType.STRING);
				standardOpItemL.setStandradFrom(rows.getCell(17) == null ? null : rows.getCell(17).getStringCellValue());
				standardOpItemL.setStandradTo(rows.getCell(18) == null ? null : rows.getCell(18).getStringCellValue());
				standardOpItemL.setStandradUom(rows.getCell(19) == null ? null : rows.getCell(19).toString());
				standardOpItemL.setTextStandrad(rows.getCell(20) == null ? null : rows.getCell(20).toString());
				standardOpItemL.setInspectionTool(rows.getCell(16) == null ? null : rows.getCell(16).toString());
				ServiceRequest sr = new ServiceRequest();
				sr.setLocale("zh_CN");
				standardOpItemL.setInspectionMethod(iCodeService.getCodeValueByMeaning(sr, "HQM_INSPECTION_METHOD",
						rows.getCell(15) == null ? null : rows.getCell(15).toString()));
				standardOpItemL.setRemark(rows.getCell(21) == null ? null : rows.getCell(21).toString());
				standardOpItemL.setEnableTime(new Date());

				//新增行数据
				standardOpItemLMapper.insertSelective(standardOpItemL);
			}
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
	
	public String dealFloat(String val) {
		if(val != null && !"".equals(val)) {
			float f = Float.valueOf(val);
			int i = (int)f;
			float y = f - (float)i;
			if(y == 0f) {
				return "" + i;
			} else {
				return val;
			}
		}else {
			return "";
		}
	}

	public void checkInport(Sheet sheet, ResponseData responseData) throws Exception {
		List<PqcValidateInfo> pqcValidateInfoList = new ArrayList<PqcValidateInfo>();
		for (int i = 1; i <= sheet.getLastRowNum(); i++) {
			// i=1,从第二行开始
			Row rows = sheet.getRow(i);
			if (rows == null) {
				responseData.setSuccess(false);
				responseData.setMessage("导入模板错误，请使用正确的模板进行导入！");
				return;
			} else {
				PqcValidateInfo pqcValidateInfo = new PqcValidateInfo();
				if (rows.getCell(0) == null) {
					continue;
				} else {
					// 结构
					pqcValidateInfo.setPlantCode(rows.getCell(0) == null ? null : rows.getCell(0).toString());
					pqcValidateInfo.setProdLine(rows.getCell(1) == null ? null : rows.getCell(1).toString());
					pqcValidateInfo.setStandardOp(rows.getCell(2) == null ? null : rows.getCell(2).toString());
					pqcValidateInfo.setWorkstation(rows.getCell(4) == null ? null : rows.getCell(4).toString());
					String itemCode = rows.getCell(6) == null ? null : rows.getCell(6).toString();
					if(itemCode == null) {
						responseData.setSuccess(false);
						responseData.setMessage("第" + i + "行，物料编码不存在！");
						return;
					}
					if(itemCode.contains(".")) {
						pqcValidateInfo.setItemCode(itemCode.substring(0, itemCode.indexOf(".")));
					} else {
						pqcValidateInfo.setItemCode(itemCode);
					}
					pqcValidateInfo.setAttributeName(rows.getCell(11) == null ? null : rows.getCell(11).toString());
					pqcValidateInfo.setAttributeTool(rows.getCell(16) == null ? null : rows.getCell(16).toString());
					pqcValidateInfoList.add(pqcValidateInfo);

					StandardOpItemH standardOpItemH = new StandardOpItemH();
					boolean isExist = true;
					// 校验工厂
					Plant plant = new Plant();
					plant.setPlantCode(pqcValidateInfo.getPlantCode());
					List<Plant> plantList = plantMapper.select(plant);
					if (plantList != null && plantList.size() > 0) {
						standardOpItemH.setPlantId(String.valueOf(plantList.get(0).getPlantId()));
					} else {
						responseData.setSuccess(false);
						responseData.setMessage("第" + i + "行，工厂编码不存在！");
						return;
					}

					// 校验生产线
					ProductionLine productionLine = new ProductionLine();
					productionLine.setDescriptions(pqcValidateInfo.getProdLine());
					List<ProductionLine> productionLineList = productionLineMapper.select(productionLine);
					if (productionLineList != null && productionLineList.size() > 0) {
						standardOpItemH.setProdLineId(productionLineList.get(0).getProdLineId());
					} else {
						responseData.setSuccess(false);
						responseData.setMessage("第" + i + "行，生产线不存在！");
						return;
					}

					// 校验物料编码
					Item item = new Item();
					item.setItemCode(pqcValidateInfo.getItemCode());
					item.setPlantId(plantList.get(0).getPlantId());
					List<Item> itemList = itemMapper.select(item);
					if (itemList != null && itemList.size() > 0) {
						standardOpItemH.setItemId(itemList.get(0).getItemId());
					} else {
						responseData.setSuccess(false);
						responseData.setMessage("第" + i + "行，物料编码不存在！");
						return;
					}

					// 校验工位编码
					SopPqcControlL sopPqcControlL = new SopPqcControlL();
					sopPqcControlL.setWorkstationCode(pqcValidateInfo.getWorkstation());
					List<SopPqcControlL> sopPqcControlLList = sopPqcControlLMapper.select(sopPqcControlL);
					if (sopPqcControlLList != null && sopPqcControlLList.size() > 0) {
						standardOpItemH.setWorkstationId(sopPqcControlLList.get(0).getWorkstationId());
					} else {
						isExist = false;
					}

					// 校验检验性质
					ServiceRequest sr = new ServiceRequest();
					sr.setLocale("zh_CN");
					String uri = iCodeService.getCodeValueByMeaning(sr, "HQM_PQC_SOURCE_TYPE",
							rows.getCell(9) == null ? null : rows.getCell(9).toString());
					if (uri == null) {
						responseData.setSuccess(false);
						responseData.setMessage("第" + i + "行，检验性质不存在！");
						return;
					}
					standardOpItemH.setSourceType(uri);
					
					//校验检验项类型
					if(iCodeService.getCodeValueByMeaning(sr, "HQM_IQC_ATTRIBUTE_CATEGORY",
							rows.getCell(12) == null ? null : rows.getCell(12).toString()) == null) {
						responseData.setSuccess(false);
						responseData.setMessage("第" + i + "行，检验项类型不存在！");
						return;
					}
					
					//校验质量等级
					if(iCodeService.getCodeValueByMeaning(sr, "HQM_IQC_QUALITY_GRADE",
							rows.getCell(13) == null ? null : rows.getCell(13).toString()) == null) {
						responseData.setSuccess(false);
						responseData.setMessage("第" + i + "行，质量等级不存在！");
						return;
					}

					// 校验规格类型
					sr.setLocale("zh_CN");
					if(iCodeService.getCodeValueByMeaning(sr, "HQM_IQC_STANDARD_TYPE",
							rows.getCell(14) == null ? null : rows.getCell(14).toString()) == null) {
						responseData.setSuccess(false);
						responseData.setMessage("第" + i + "行，规格类型不存在！");
						return;
					}
					if ("计量".equals(iCodeService.getCodeValueByMeaning(sr, "HQM_IQC_STANDARD_TYPE",
							rows.getCell(14) == null ? null : rows.getCell(14).toString()))) {
						if (rows.getCell(17) == null || rows.getCell(18) == null) {
							responseData.setSuccess(false);
							responseData.setMessage("第" + i + "行，规格上下限为空 ！");
							return;
						}
						float StandradFrom = Float
								.parseFloat(rows.getCell(17) == null ? null : rows.getCell(17).toString());
						float StandradTo = Float
								.parseFloat(rows.getCell(18) == null ? null : rows.getCell(18).toString());
						if (StandradTo <= StandradFrom) {
							responseData.setSuccess(false);
							responseData.setMessage("第" + i + "行，规格上下限错误 ！");
							return;
						}
					}
					
					//校验检验方法
					if(iCodeService.getCodeValueByMeaning(sr, "HQM_INSPECTION_METHOD",
							rows.getCell(15) == null ? null : rows.getCell(15).toString()) == null) {
						responseData.setSuccess(false);
						responseData.setMessage("第" + i + "行，检验方法不存在！");
						return;
					}

					// 校验产线工序信息是否存在
					SopPqcControlH sopPqcControlH = new SopPqcControlH();
					sopPqcControlH.setProdLineId(standardOpItemH.getProdLineId());
					sopPqcControlH.setStandardOpCode(pqcValidateInfo.getStandardOp());
					List<SopPqcControlH> sopPqcControlHList = sopPqcControlHMapper.select(sopPqcControlH);
					if (sopPqcControlHList != null && sopPqcControlHList.size() > 0) {
						standardOpItemH.setStandardOpId(sopPqcControlHList.get(0).getStandardOpId());
					} else {
						isExist = false;
					}

					// 校验检验项目是否已存在
					InspectionAttribute inspectionAttribute = new InspectionAttribute();
					inspectionAttribute.setInspectionAttribute(pqcValidateInfo.getAttributeName());
					inspectionAttribute.setInspectionTool(pqcValidateInfo.getAttributeTool());
					List<InspectionAttribute> inspectionAttributeList = inspectionAttributeMapper
							.select(inspectionAttribute);
					if (inspectionAttributeList != null && inspectionAttributeList.size() > 0) {
						// 数据存在则校验行数据重复
						StandardOpItemH standardOpItemHIsexist = new StandardOpItemH();
						standardOpItemHIsexist.setPlantId(standardOpItemH.getPlantId());//工厂
						standardOpItemHIsexist.setItemId(standardOpItemH.getItemId());//物料
						standardOpItemHIsexist.setSourceType(standardOpItemH.getSourceType());//来源类型
						standardOpItemHIsexist.setProdLineId(standardOpItemH.getProdLineId());//产线
						standardOpItemHIsexist.setStandardOpId(standardOpItemH.getStandardOpId());//工序
						standardOpItemHIsexist.setWorkstationId(standardOpItemH.getWorkstationId());//工位
						standardOpItemHIsexist.setItemEdition(rows.getCell(8) == null ? null : rows.getCell(8).toString());//物料版本
						List<StandardOpItemH> standardOpItemHList = standardOpItemHMapper.select(standardOpItemHIsexist);
						if(standardOpItemHList != null && standardOpItemHList.size() > 0) {
							responseData.setSuccess(false);
							responseData.setMessage("第" + i + "行，检验单模板数据已存在！");
							return;
						}
					} else {
						StandardOpItemH standardOpItemHIsexist = new StandardOpItemH();
						standardOpItemHIsexist.setPlantId(standardOpItemH.getPlantId());//工厂
						standardOpItemHIsexist.setItemId(standardOpItemH.getItemId());//物料
						standardOpItemHIsexist.setSourceType(standardOpItemH.getSourceType());//来源类型
						standardOpItemHIsexist.setProdLineId(standardOpItemH.getProdLineId());//产线
						standardOpItemHIsexist.setStandardOpId(standardOpItemH.getStandardOpId());//工序
						standardOpItemHIsexist.setWorkstationId(standardOpItemH.getWorkstationId());//工位
						standardOpItemHIsexist.setItemEdition(rows.getCell(8) == null ? null : rows.getCell(8).toString());//物料版本
						List<StandardOpItemH> standardOpItemHList = standardOpItemHMapper.select(standardOpItemHIsexist);
						if(standardOpItemHList != null && standardOpItemHList.size() > 0) {
							responseData.setSuccess(false);
							responseData.setMessage("第" + i + "行，检验单模板数据已存在！");
							return;
						}
					}
				}
			}
		}

		// 校验excel数据是否重复
		// excel维护结构校验
		if (!"".equals(validateSingleInExcel(pqcValidateInfoList))) {
			responseData.setSuccess(false);
			responseData.setMessage("excel内结构不能重复！" + validateSingleInExcel(pqcValidateInfoList));
		}
	}

	private String validateSingleInExcel(List<PqcValidateInfo> pvids) {
		if(pvids.size() == 1) {
			return "";
		}
		for (int i = 0; i < pvids.size(); i++) {
			PqcValidateInfo pvdi = pvids.get(i);

			for (int j = i + 1; j < pvids.size(); j++) {
				boolean check = false;
				if (!pvdi.getPlantCode().equals(pvids.get(j).getPlantCode())) {
					check = true;
				}
				if (!pvdi.getProdLine().equals(pvids.get(j).getProdLine())) {
					check = true;
				}
				if (!pvdi.getStandardOp().equals(pvids.get(j).getStandardOp())) {
					check = true;
				}
				if (!pvdi.getWorkstation().equals(pvids.get(j).getWorkstation())) {
					check = true;
				}
				if (!pvdi.getItemCode().equals(pvids.get(j).getItemCode())) {
					check = true;
				}
				if (!pvdi.getAttributeName().equals(pvids.get(j).getAttributeName())) {
					check = true;
				}
				if (!pvdi.getAttributeTool().equals(pvids.get(j).getAttributeTool())) {
					check = true;
				}

				if (!check) {
					return "第" + i + "行与第" + j + "行数据重复。";
				}
			}
		}
		return "";

	}

	@Override
	public void audit(IRequest requestCtx, List<StandardOpItemH> dto) {
		for (StandardOpItemH in : dto) {
			StandardOpItemH search = new StandardOpItemH();
			search.setHeadId(in.getHeadId());
			search = standardOpItemHMapper.selectOne(search);
			StandardOpItemH updateDto = new StandardOpItemH();
			updateDto.setHeadId(search.getHeadId());
			updateDto.setVersion(String.valueOf(Integer.valueOf(search.getVersion()) + 1));
			updateDto.setStatus("3");
			self().updateByPrimaryKeySelective(requestCtx, updateDto);
		}
	}
	
	@Override
	public void commit(IRequest requestCtx, List<StandardOpItemH> dto) {
		for (StandardOpItemH in : dto) {
			StandardOpItemH search = new StandardOpItemH();
			search.setHeadId(in.getHeadId());
			search = standardOpItemHMapper.selectOne(search);
			StandardOpItemH updateDto = new StandardOpItemH();
			updateDto.setHeadId(search.getHeadId());
			updateDto.setVersion(String.valueOf(Integer.valueOf(search.getVersion()) + 1));
			updateDto.setStatus("2");
			self().updateByPrimaryKeySelective(requestCtx, updateDto);
			
			ProcessInstanceCreateRequest instanceCreateRequest = new ProcessInstanceCreateRequest();
			instanceCreateRequest.setBusinessKey(String.valueOf(in.getHeadId()));
			instanceCreateRequest.setProcessDefinitionKey("pqcTaskExamine");
			//wtm 20200327 提交不开新工作流
			//activitiService.startProcess(requestCtx, instanceCreateRequest);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hand.hqm.hqm_pqc_warning.service.IPqcWarningService#
	 * updateByPrimaryKeySelectiveRecord(com.hand.hap.core.IRequest,
	 * com.hand.hqm.hqm_pqc_warning.dto.PqcWarning)
	 */
	@Override
	public StandardOpItemH updateByPrimaryKeySelectiveRecord(IRequest request, StandardOpItemH t) {
		StandardOpItemH now = mapper.selectByPrimaryKey(t);
		self().updateByPrimaryKeySelective(request, t);
		StandardOpItemH after = mapper.selectByPrimaryKey(t);
		StandardOpItemHHis his = new StandardOpItemHHis();
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
		iStandardOpItemHHisService.insertSelective(request, his);
		objectEventsRecord(now, after, his, request);
		return t;
	}

	private void objectEventsRecord(StandardOpItemH now, StandardOpItemH after, StandardOpItemHHis his,
			IRequest request) {
		ObjectEvents oe = new ObjectEvents();
		oe.setEventId(his.getEventId());
		oe.setEventTime(his.getEventTime());
		oe.setEventBy(his.getEventBy());
		oe.setEventTable("HQM_STANDARD_OP_ITEM_H");
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
								"standardopitemh." + nowField.getName().toLowerCase()) + ":"
								+ changeInfoGetNumber(request, nowFloat, afterFloat, nowField.getName()) + "<br/>";
					}
				} else if (nowField.getType() == String.class) {
					String nowString = nowField.get(now) == null ? "" : (String) nowField.get(now);
					String afterString = afterField.get(after) == null ? "" : (String) afterField.get(after);
					if (!(nowString).equals(afterString)) {
						eventContent += SystemApiMethod.getPromptDescription(request, iPromptService,
								"standardopitemh." + nowField.getName().toLowerCase()) + ":"
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
		} else if ("prodLineId".equals(fieldName)) {
			str1 = getProdLineCodeById(now);
			str2 = getProdLineCodeById(after);
		} else if ("standardOpId".equals(fieldName)) {
			str1 = getStandardOpCodeById(now);
			str2 = getStandardOpCodeById(after);
		} else if ("workstationId".equals(fieldName)) {
			str1 = getWorkstationCodeById(now);
			str2 = getWorkstationCodeById(after);
		} else {
			str1 = String.valueOf(now);
			str2 = String.valueOf(after);
		}

		return str1 + MiddleString + str2;
	}

	/**
	 * @description
	 * @author tianmin.wang
	 * @date 2020年3月10日
	 * @param standardOpId
	 * @param workstationId
	 * @return
	 */
	private String getWorkstationCodeById(Float workstationId) {
		// TODO Auto-generated method stub
		SopPqcControlL lSearch = new SopPqcControlL();
		lSearch.setWorkstationId(workstationId);
		return sopPqcControlLMapper.selectByPrimaryKey(lSearch).getWorkstationCode();
	}

	/**
	 * @description
	 * @author tianmin.wang
	 * @date 2020年3月10日
	 * @param standardOpId
	 * @return
	 */
	private String getStandardOpCodeById(Float standardOpId) {
		SopPqcControlH hSearch = new SopPqcControlH();
		hSearch.setStandardOpId(standardOpId);
		return sopPqcControlHMapper.selectByPrimaryKey(hSearch).getStandardOpCode();
	}

	private String changeInfoGetString(IRequest request, String now, String after, String fieldName) {
		String str1 = "";
		String str2 = "";
		if ("status".equals(fieldName)) {// HQM_PLM_SKU_TYPE
			str1 = iCodeService.getCodeMeaningByValue(request, "HQM_INSPECTION_TEMPLATE_STATUS", now);
			str2 = iCodeService.getCodeMeaningByValue(request, "HQM_INSPECTION_TEMPLATE_STATUS", after);
		} else if ("sourceType".equals(fieldName)) {
			str1 = iCodeService.getCodeMeaningByValue(request, "HQM_PQC_SOURCE_TYPE", now);
			str2 = iCodeService.getCodeMeaningByValue(request, "HQM_PQC_SOURCE_TYPE", after);
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
	public StandardOpItemH insertSelectiveRecord(IRequest request, StandardOpItemH t) {
		self().insertSelective(request, t);
		StandardOpItemH after = mapper.selectByPrimaryKey(t);
		StandardOpItemHHis his = new StandardOpItemHHis();
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
		iStandardOpItemHHisService.insertSelective(request, his);
		return t;
	}

	private Float getPlantId(String plantCode) {
		Plant ser = new Plant();
		ser.setPlantCode(plantCode);
		return plantMapper.select(ser).get(0).getPlantId();
	}

	private String getProdLineCodeById(Float id) {
		ProductionLine search = new ProductionLine();
		search.setProdLineId(id);
		return productionLineMapper.selectByPrimaryKey(search).getProdLineCode();
	}
	
	/**
	 * 审批时展示历史变更记录
	 * @author kai.li
	 * @param requestContext
	 * @param headId
	 * @param startTime
	 * @return
	 * @throws ParseException
	 */
	@Override
	public List<ObjectEvents> getHisContentPqc(IRequest requestContext, Float headId, String startTime) {
		List<ObjectEvents> objectEventsRes = new ArrayList<ObjectEvents>();
		
		StandardOpItemHHis standardOpItemHHis = new StandardOpItemHHis();
		standardOpItemHHis.setHeadId(headId);
		standardOpItemHHis.setStartDateString(startTime);
		List<StandardOpItemHHis> standardOpItemHHisList = standardOpItemHHisMapper.selectbyheadIdTime(standardOpItemHHis);
		if(standardOpItemHHisList != null && standardOpItemHHisList.size() > 0) {
			ObjectEvents objectEvents = new ObjectEvents();
			objectEvents.setEventId(standardOpItemHHisList.get(0).getEventId());
			List<ObjectEvents> objectEventsList = objectEventsMapper.getHisContentPqc(objectEvents);
			if(objectEventsList != null && objectEventsList.size() > 0) {
				for(ObjectEvents obe:objectEventsList) {
					objectEventsRes.add(obe);
				}
			} else {
				ObjectEvents objectEventsRe = new ObjectEvents();
				objectEventsRe.setEventContent("无历史数据");
				objectEventsRes.add(objectEventsRe);
			}
		} else {
			ObjectEvents objectEventsRe = new ObjectEvents();
			objectEventsRe.setEventContent("无历史数据");
			objectEventsRes.add(objectEventsRe);
		}
		
		return objectEventsRes;
	}
}