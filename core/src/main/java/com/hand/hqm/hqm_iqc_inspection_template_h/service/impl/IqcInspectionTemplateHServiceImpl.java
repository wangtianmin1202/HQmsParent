package com.hand.hqm.hqm_iqc_inspection_template_h.service.impl;

import com.github.pagehelper.PageHelper;
import com.hand.hap.activiti.service.IActivitiService;
import com.hand.hap.core.IRequest;
import com.hand.hap.core.annotation.StdWho;
import com.hand.hap.core.impl.RequestHelper;
import com.hand.hap.core.impl.ServiceRequest;
import com.hand.hap.system.dto.BaseDTO;
import com.hand.hap.system.dto.DTOStatus;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.system.service.ICodeService;
import com.hand.hap.system.service.IPromptService;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import com.hand.hap.util.SystemApiMethod;
import com.hand.hcm.hcm_item.dto.Item;
import com.hand.hcm.hcm_item.mapper.ItemMapper;
import com.hand.hcm.hcm_item_b.dto.ItemB;
import com.hand.hcm.hcm_item_b.mapper.ItemBMapper;
import com.hand.hcm.hcm_item_category.mapper.ItemCategoryMapper;
import com.hand.hcm.hcm_object_events.dto.ObjectEvents;
import com.hand.hcm.hcm_object_events.mapper.ObjectEventsMapper;
import com.hand.hcm.hcm_object_events.service.IObjectEventsService;
import com.hand.hcm.hcm_plant.dto.Plant;
import com.hand.hcm.hcm_plant.mapper.PlantMapper;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.drools.core.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.hand.hqm.hqm_inspection_attribute.dto.InspectionAttribute;
import com.hand.hqm.hqm_inspection_attribute.mapper.InspectionAttributeMapper;
import com.hand.hqm.hqm_iqc_inspection_template_h.dto.IqcInspectionTemplateH;
import com.hand.hqm.hqm_iqc_inspection_template_h.mapper.IqcInspectionTemplateHMapper;
import com.hand.hqm.hqm_iqc_inspection_template_h.service.IIqcInspectionTemplateHService;
import com.hand.hqm.hqm_iqc_inspection_template_h_his.dto.IqcInspectionTemplateHHis;
import com.hand.hqm.hqm_iqc_inspection_template_h_his.mapper.IqcInspectionTemplateHHisMapper;
import com.hand.hqm.hqm_iqc_inspection_template_h_his.service.IIqcInspectionTemplateHHisService;
import com.hand.hqm.hqm_iqc_inspection_template_l.dto.IqcInspectionTemplateL;
import com.hand.hqm.hqm_iqc_inspection_template_l.dto.ValidateInfo;
import com.hand.hqm.hqm_iqc_inspection_template_l.mapper.IqcInspectionTemplateLMapper;
import com.hand.hqm.hqm_iqc_inspection_template_l.service.IIqcInspectionTemplateLService;
import com.hand.hqm.hqm_item_category_ext.dto.ItemCategoryExt;
import com.hand.hqm.hqm_item_category_ext.mapper.ItemCategoryExtMapper;
import com.hand.hqm.hqm_sample_manage.dto.SampleManage;
import com.hand.hqm.hqm_sample_manage.mapper.SampleManageMapper;
import org.springframework.transaction.annotation.Transactional;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.activiti.rest.service.api.engine.variable.RestVariable;
import org.activiti.rest.service.api.runtime.process.ProcessInstanceCreateRequest;
import org.apache.ibatis.javassist.Modifier;

@Service
@Transactional(rollbackFor = Exception.class)
public class IqcInspectionTemplateHServiceImpl extends BaseServiceImpl<IqcInspectionTemplateH>
		implements IIqcInspectionTemplateHService {
	private String MiddleString = " 修改为 ";
	@Autowired
	IqcInspectionTemplateHMapper iqcInspectionTemplateHMapper;
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
	private IActivitiService activitiService;
	@Autowired
	IObjectEventsService iObjectEventsService;
	@Autowired
	IIqcInspectionTemplateHHisService iIqcInspectionTemplateHHisService;
	@Autowired
	ItemBMapper itemBMapper;
	@Autowired
	IqcInspectionTemplateHHisMapper iqcInspectionTemplateHHisMapper;
	@Autowired
	ObjectEventsMapper objectEventsMapper;

	@Override
	public void exportExcel(IqcInspectionTemplateH dto, IRequest requestContext, HttpServletRequest request,
			HttpServletResponse response) throws Throwable {

		// TODO Auto-generated method stub
		response.addHeader("Content-Disposition",
				"attachment;filename=\"" + URLEncoder.encode("检验计划" + ".xlsx", "UTF-8") + "\"");
		response.setContentType("application/vnd.ms-excel;charset=" + "UTF-8" + "");
		response.setHeader("Accept-Ranges", "bytes");
		ServletOutputStream outputStream = response.getOutputStream();
		XSSFWorkbook wb = new XSSFWorkbook();
		Sheet sheet = wb.createSheet("sheet1");
		createRows(dto, sheet, requestContext);
//		Row firstRow = sheet.createRow(0);
//		Cell cell = firstRow.createCell(0);
//		cell.setCellValue("TEST");
		wb.write(outputStream);
		outputStream.flush();
		outputStream.close();
		try {
			wb.close();
		} catch (Exception e) {

		}
	}

	public void createRows(IqcInspectionTemplateH dto, Sheet sheet, IRequest requestContext) {
		Row firstRow = sheet.createRow(0);
		titleCreat(firstRow);
		contentCreat(dto, sheet, requestContext);
	}

	private void contentCreat(IqcInspectionTemplateH dto, Sheet sheet, IRequest requestContext) {
		int rowIndex = 0;
		List<IqcInspectionTemplateH> hList = iqcInspectionTemplateHMapper.myselect(dto);
		if (hList != null) {
			for (IqcInspectionTemplateH templateH : hList) {
				IqcInspectionTemplateL se = new IqcInspectionTemplateL();
				se.setHeaderId(templateH.getHeaderId());
				List<IqcInspectionTemplateL> lList = iqcInspectionTemplateLMapper.myselect(se);
				if (lList != null) {
					for (IqcInspectionTemplateL templateL : lList) {
						rowIndex++;
						Row lineRow = sheet.createRow(rowIndex);
						rowCellCreat(lineRow, templateH, templateL, requestContext);
					}
				}
			}
		}

	}

	public void rowCellCreat(Row row, IqcInspectionTemplateH templateH, IqcInspectionTemplateL templateL,
			IRequest requestContext) {
		row.createCell(0).setCellValue(templateH.getPlantCode() == null ? "" : templateH.getPlantCode());
		row.createCell(1).setCellValue(templateH.getItemCode() == null ? "" : templateH.getItemCode());
		row.createCell(2).setCellValue(templateH.getItemDescriptions() == null ? "" : templateH.getItemDescriptions());
		row.createCell(3)
				.setCellValue(getCodeMeaningByValue("HQM_IQC_SOURCE_TYPE", templateH.getSourceType(), requestContext));
		row.createCell(4).setCellValue(
				getCodeMeaningByValue("HQM_INSPECTION_TEMPLATE_STATUS", templateH.getStatus(), requestContext));
		row.createCell(5).setCellValue(templateH.getVersionNum() == null ? "" : templateH.getItemDescriptions());
		row.createCell(6).setCellValue(templateH.getTimeLimit() == null ? "" : templateH.getItemDescriptions());
		row.createCell(7).setCellValue(getCodeMeaningByValue("SYS.YES_NO", templateH.getEnableFlag(), requestContext));
		row.createCell(8)
				.setCellValue(templateL.getInspectionAttribute() == null ? "" : templateL.getInspectionAttribute());
		row.createCell(9).setCellValue(
				getCodeMeaningByValue("HQM_IQC_ATTRIBUTE_CATEGORY", templateL.getAttributeType(), requestContext));
		row.createCell(10).setCellValue(
				getCodeMeaningByValue("HQM_IQC_STANDARD_TYPE", templateL.getStandardType(), requestContext));
		row.createCell(11)
				.setCellValue(getCodeMeaningByValue("HQM_SAMPLE_TYPE", templateL.getSampleType(), requestContext));
		row.createCell(12).setCellValue(templateL.getInspectionTool() == null ? "" : templateL.getInspectionTool());
		row.createCell(13).setCellValue(
				getCodeMeaningByValue("HQM_INSPECTION_METHOD", templateL.getInspectionMethod(), requestContext));
		row.createCell(14)
				.setCellValue(templateL.getStandradFrom() == null ? "" : String.valueOf(templateL.getStandradFrom()));
		row.createCell(15).setCellValue(templateL.getStandradTo() == null ? "" : templateL.getStandradTo().toString());
		row.createCell(16)
				.setCellValue(getCodeMeaningByValue("HQM_STANDARD_UOM", templateL.getStandradUom(), requestContext));
		row.createCell(17).setCellValue(templateL.getTextStandrad() == null ? "" : templateL.getTextStandrad());
		row.createCell(18)
				.setCellValue(getCodeMeaningByValue("HQM_FILL_IN_TYPE", templateL.getFillInType(), requestContext));
		row.createCell(19).setCellValue(templateL.getPrecision() == null ? "" : templateL.getPrecision().toString());
		row.createCell(10).setCellValue(
				getCodeMeaningByValue("HQM_IQC_QUALITY_GRADE", templateL.getQualityCharacterGrade(), requestContext));
		row.createCell(21)
				.setCellValue(getCodeMeaningByValue("HQM_IQC_ENABLE_TYPE", templateL.getEnableType(), requestContext));
		row.createCell(22).setCellValue(templateL.getEnableTime() == null ? "" : templateL.getEnableTime().toString());
		row.createCell(23)
				.setCellValue(templateL.getDisableTime() == null ? "" : templateL.getDisableTime().toString());
		row.createCell(24).setCellValue(getCodeMeaningByValue("SYS.YES_NO", templateL.getIsSync(), requestContext));
		row.createCell(25).setCellValue(templateL.getRemark() == null ? "" : templateL.getRemark());
	}

	public String getCodeMeaningByValue(String codeName, String value, IRequest requestContext) {
		String inVal = iCodeService.getCodeMeaningByValue(requestContext, codeName, value);
		if (inVal == null) {
			return value == null ? "" : value;
		} else {
			return inVal;
		}

	}

	public void titleCreat(Row firstRow) {
		firstRow.createCell(0).setCellValue("工厂");
		firstRow.createCell(1).setCellValue("物料编码");
		firstRow.createCell(2).setCellValue("物料描述");
		firstRow.createCell(3).setCellValue("来源类型");
		firstRow.createCell(4).setCellValue("状态");
		firstRow.createCell(5).setCellValue("版本号");
		firstRow.createCell(6).setCellValue("检验时效");
		firstRow.createCell(7).setCellValue("有效性");
		firstRow.createCell(8).setCellValue("检验项目");
		firstRow.createCell(9).setCellValue("检验项类型");
		firstRow.createCell(10).setCellValue("规格类型");
		firstRow.createCell(11).setCellValue("抽样方式");
		firstRow.createCell(12).setCellValue("检验工具");
		firstRow.createCell(13).setCellValue("检验方法");
		firstRow.createCell(14).setCellValue("规格值从");
		firstRow.createCell(15).setCellValue("规格值至");
		firstRow.createCell(16).setCellValue("规格单位");
		firstRow.createCell(17).setCellValue("文本规格值");
		firstRow.createCell(18).setCellValue("结果记录");
		firstRow.createCell(19).setCellValue("精度");
		firstRow.createCell(20).setCellValue("质量等级");
		firstRow.createCell(21).setCellValue("生效类型");
		firstRow.createCell(22).setCellValue("生效时间");
		firstRow.createCell(23).setCellValue("失效时间");
		firstRow.createCell(24).setCellValue("同步检验项");
		firstRow.createCell(25).setCellValue("备注");
	}

	@Override
	public List<IqcInspectionTemplateH> myselect(IRequest requestContext, IqcInspectionTemplateH dto, int page,
			int pageSize) {
		PageHelper.startPage(page, pageSize);
		return iqcInspectionTemplateHMapper.myselect(dto);
	}

	@Override
	public List<IqcInspectionTemplateH> selectforCopy(IRequest requestContext, IqcInspectionTemplateH dto, int page,
			int pageSize) {
		PageHelper.startPage(page, pageSize);
		return iqcInspectionTemplateHMapper.selectforCopy(dto);
	}

	@Override
	public List<IqcInspectionTemplateH> versionNumberBatchUpdate(IRequest request,
			@StdWho List<IqcInspectionTemplateH> list) throws Exception {
		// TODO Auto-generated method stub
//		IBaseService<IqcInspectionTemplateH> self = ((IBaseService<IqcInspectionTemplateH>) AopContext.currentProxy());
		for (IqcInspectionTemplateH t : list) {
			switch (((BaseDTO) t).get__status()) {
			case DTOStatus.ADD:
				insertSelectiveRecord(request, t);
				this.createTemplateL(t);
				t.setVersionNum("0");
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

	public void createTemplateL(IqcInspectionTemplateH templateH) throws Exception {
		ItemCategoryExt categoryQuery = new ItemCategoryExt();
		categoryQuery.setItemCategory(templateH.getItemCategory());
		categoryQuery.setSourceType(templateH.getSourceType());
		List<ItemCategoryExt> categoryList = itemCategoryExtMapper.select(categoryQuery);
		if (categoryList == null)
			return;
		Float orderCode = 0f;
		for (ItemCategoryExt itemCategoryExt : categoryList) {
			orderCode++;
			InspectionAttribute inspectionAttributeSearch = new InspectionAttribute();
			inspectionAttributeSearch.setAttributeId(itemCategoryExt.getAttributeId());
			InspectionAttribute inspectionAttributeResult = inspectionAttributeMapper
					.selectByPrimaryKey(inspectionAttributeSearch);
			if (inspectionAttributeResult == null)
				continue;
			IqcInspectionTemplateL addTemplateL = new IqcInspectionTemplateL();
			addTemplateL.setHeaderId(templateH.getHeaderId());
			addTemplateL.setEnableType("D");
			addTemplateL.setOrderCode(orderCode);
			addTemplateL.setEnableTime(new Date());
			addTemplateL.setAttributeId(inspectionAttributeResult.getAttributeId());
			iqcInspectionTemplateLMapper.insertSelective(addTemplateL);

		}

	}

	@Override
	public void updateStatus(IRequest requestCtx, List<IqcInspectionTemplateH> dto) {

		for (IqcInspectionTemplateH in : dto) {
			IqcInspectionTemplateH search = new IqcInspectionTemplateH();
			search.setHeaderId(in.getHeaderId());
			search = iqcInspectionTemplateHMapper.selectOne(search);
			IqcInspectionTemplateH updateDto = new IqcInspectionTemplateH();
			updateDto.setHeaderId(search.getHeaderId());
			updateDto.setHistoryNum(search.getHistoryNum() + 1);
			updateDto.setVersionNum(String.valueOf(Integer.valueOf(search.getVersionNum()) + 1));
			updateDto.setStatus("4");
			updateByPrimaryKeySelectiveRecord(requestCtx, updateDto);
		}

	}

	@Override
	public void updateStatus_l(IRequest requestCtx, IqcInspectionTemplateH dto) {
		IqcInspectionTemplateH search = new IqcInspectionTemplateH();
		search.setHeaderId(dto.getHeaderId());
		search = iqcInspectionTemplateHMapper.selectOne(search);
		IqcInspectionTemplateH updateDto = new IqcInspectionTemplateH();
		updateDto.setHeaderId(search.getHeaderId());
		updateDto.setHistoryNum(search.getHistoryNum() + 1);
		updateDto.setVersionNum(String.valueOf(Integer.valueOf(search.getVersionNum()) + 1));
		updateDto.setStatus("4");
		updateByPrimaryKeySelectiveRecord(requestCtx, updateDto);

	}

	@Override
	public List<IqcInspectionTemplateH> reSave(IRequest requestContext, IqcInspectionTemplateH dto) {
		// TODO Auto-generated method stub
		List<IqcInspectionTemplateH> result = new ArrayList<>();
		if (dto.getHeaderId().intValue() == -1) {
			// 新增
			result.add(insertSelectiveRecord(requestContext, dto));
		} else {
			// 更新
			result.add(updateByPrimaryKeySelectiveRecord(requestContext, dto));
			refreshSampleWay(requestContext, dto);
		}
		return result;
	}

	/**
	 * 
	 * @description 刷新一次行的sample way id 为 null 的数据
	 * @author tianmin.wang
	 * @date 2019年12月18日
	 */
	public void refreshSampleWay(IRequest requestContext, IqcInspectionTemplateH dto) {
		IqcInspectionTemplateL lSearch = new IqcInspectionTemplateL();
		lSearch.setHeaderId(dto.getHeaderId());
		List<IqcInspectionTemplateL> result = iqcInspectionTemplateLMapper.select(lSearch);
		result.stream().filter(p -> p.getSampleWayId() == null).forEach(p -> {
			p.setSampleWayId(dto.getSampleWayId());
			iqcInspectionTemplateLMapper.updateByPrimaryKeySelective(p);
		});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hand.hqm.hqm_iqc_inspection_template_h.service.
	 * IIqcInspectionTemplateHService#reback(com.hand.hap.core.IRequest,
	 * java.util.List)
	 */
	@Override
	public void reback(IRequest requestCtx, List<IqcInspectionTemplateH> dto) {
		for (IqcInspectionTemplateH in : dto) {
			IqcInspectionTemplateH search = new IqcInspectionTemplateH();
			search.setHeaderId(in.getHeaderId());
			search = iqcInspectionTemplateHMapper.selectOne(search);
			IqcInspectionTemplateH updateDto = new IqcInspectionTemplateH();
			updateDto.setHeaderId(search.getHeaderId());
			updateDto.setHistoryNum(search.getHistoryNum() + 1);
			updateDto.setVersionNum(String.valueOf(Integer.valueOf(search.getVersionNum()) + 1));
			updateDto.setStatus("1");
			self().updateByPrimaryKeySelective(requestCtx, updateDto);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hand.hqm.hqm_iqc_inspection_template_h.service.
	 * IIqcInspectionTemplateHService#audit(com.hand.hap.core.IRequest,
	 * java.util.List)
	 */
	@Override
	public void audit(IRequest requestCtx, List<IqcInspectionTemplateH> dto) {
		for (IqcInspectionTemplateH in : dto) {
			IqcInspectionTemplateH search = new IqcInspectionTemplateH();
			search.setHeaderId(in.getHeaderId());
			search = iqcInspectionTemplateHMapper.selectOne(search);
			IqcInspectionTemplateH updateDto = new IqcInspectionTemplateH();
			updateDto.setHeaderId(search.getHeaderId());
			updateDto.setHistoryNum(search.getHistoryNum() + 1);
			updateDto.setVersionNum(String.valueOf(Integer.valueOf(search.getVersionNum()) + 1));
			updateDto.setStatus("3");
			self().updateByPrimaryKeySelective(requestCtx, updateDto);
		}
	}

	/**
	 * 审批时展示历史变更记录
	 * 
	 * @author kai.li
	 * @param requestContext
	 * @param headId
	 * @param startTime
	 * @return
	 * @throws ParseException
	 */
	@Override
	public List<ObjectEvents> getHisContent(IRequest requestContext, Float headId, String startTime) {
		List<ObjectEvents> objectEventsRes = new ArrayList<ObjectEvents>();

		IqcInspectionTemplateHHis iqcInspectionTemplateHHis = new IqcInspectionTemplateHHis();
		iqcInspectionTemplateHHis.setHeaderId(headId);
		iqcInspectionTemplateHHis.setStartDateString(startTime);
		List<IqcInspectionTemplateHHis> iqcInspectionTemplateHHisList = iqcInspectionTemplateHHisMapper
				.selectbyheadIdTime(iqcInspectionTemplateHHis);
		if (iqcInspectionTemplateHHisList != null && iqcInspectionTemplateHHisList.size() > 0) {
			ObjectEvents objectEvents = new ObjectEvents();
			objectEvents.setEventId(iqcInspectionTemplateHHisList.get(0).getEventId());
			List<ObjectEvents> objectEventsList = objectEventsMapper.getHisContent(objectEvents);
			if (objectEventsList != null && objectEventsList.size() > 0) {
				for (ObjectEvents obe : objectEventsList) {
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hand.hqm.hqm_iqc_inspection_template_h.service.
	 * IIqcInspectionTemplateHService#commit(com.hand.hap.core.IRequest,
	 * java.util.List)
	 */
	@Override
	public void commit(IRequest requestCtx, List<IqcInspectionTemplateH> dto) {
		for (IqcInspectionTemplateH in : dto) {
			IqcInspectionTemplateH search = new IqcInspectionTemplateH();
			search.setHeaderId(in.getHeaderId());
			search = iqcInspectionTemplateHMapper.selectOne(search);
			IqcInspectionTemplateH updateDto = new IqcInspectionTemplateH();
			updateDto.setHeaderId(search.getHeaderId());
			updateDto.setHistoryNum(search.getHistoryNum() + 1);
			updateDto.setVersionNum(String.valueOf(Integer.valueOf(search.getVersionNum()) + 1));
			updateDto.setStatus("2");
			self().updateByPrimaryKeySelective(requestCtx, updateDto);

			// 启动审核流程
			ProcessInstanceCreateRequest instanceCreateRequest = new ProcessInstanceCreateRequest();
			instanceCreateRequest.setBusinessKey(String.valueOf(in.getHeaderId()));

			List<RestVariable> restVariableList = new ArrayList<RestVariable>();
			RestVariable restVariable = new RestVariable();

			IqcInspectionTemplateH iqcInspectionTemplateH = new IqcInspectionTemplateH();
			iqcInspectionTemplateH.setHeaderId(in.getHeaderId());
			List<IqcInspectionTemplateH> iqcInspectionTemplateHList = iqcInspectionTemplateHMapper
					.select(iqcInspectionTemplateH);
			if (iqcInspectionTemplateHList != null && iqcInspectionTemplateHList.size() > 0) {
				if ("IQC".equals(iqcInspectionTemplateHList.get(0).getMainType())) {
					instanceCreateRequest.setProcessDefinitionKey("iqcTaskExamine");

					restVariable.setName("mainType");
					restVariable.setValue("IQC");
					restVariableList.add(restVariable);
					instanceCreateRequest.setVariables(restVariableList);
				} else if ("FQC".equals(iqcInspectionTemplateHList.get(0).getMainType())) {
					instanceCreateRequest.setProcessDefinitionKey("fqcTaskExamine");

					restVariable.setName("mainType");
					restVariable.setValue("FQC");
					restVariableList.add(restVariable);
					instanceCreateRequest.setVariables(restVariableList);
				}
				// wtm 20200327 注释 不开启工作流
				// activitiService.startProcess(requestCtx, instanceCreateRequest);
			} else {
				return;
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hand.hqm.hqm_iqc_inspection_template_h.service.
	 * IIqcInspectionTemplateHService#vtpQuery(com.hand.hqm.
	 * hqm_iqc_inspection_template_h.dto.IqcInspectionTemplateH,
	 * javax.servlet.http.HttpServletRequest)
	 */
	@Override
	public List<IqcInspectionTemplateH> vtpQuery(IqcInspectionTemplateH dto, HttpServletRequest request)
			throws Exception {
		IqcInspectionTemplateH search = new IqcInspectionTemplateH();
		search.setStatus("4");
		search.setSourceType("15");
		search.setPlantId(dto.getPlantId());
		search.setItemId(dto.getItemId());
		search.setItemEdition(dto.getItemEdition());
		search.setMainType(dto.getMainType());
		List<IqcInspectionTemplateH> result = iqcInspectionTemplateHMapper.myselect(search);
		if (result == null || result.size() == 0)
			throw new RuntimeException(
					SystemApiMethod.getPromptDescription(request, iPromptService, "error.hqm_fqc_vtp_create01"));
		return result;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hand.hqm.hqm_iqc_inspection_template_h.service.
	 * IIqcInspectionTemplateHService#vtpCreateSave(java.util.List,
	 * javax.servlet.http.HttpServletRequest)
	 */
	@Override
	public void vtpCreateSave(List<IqcInspectionTemplateL> dto, HttpServletRequest request) throws Exception {
		IRequest ire = RequestHelper.createServiceRequest(request);
		IqcInspectionTemplateH searchh = new IqcInspectionTemplateH();
		searchh.setHeaderId(dto.get(0).getHeaderId());
		IqcInspectionTemplateH addH = iqcInspectionTemplateHMapper.selectByPrimaryKey(searchh);
		addH.setStatus("2");
		addH.setSourceType("16");
		addH.setVtpNumebr(dto.get(0).getVtpNumber());
		self().insertSelective(ire, addH);
		List<IqcInspectionTemplateL> addLines = new ArrayList<IqcInspectionTemplateL>();
		for (IqcInspectionTemplateL line : dto) {
			IqcInspectionTemplateL addL = iqcInspectionTemplateLMapper.selectByPrimaryKey(line);
			addL.setHeaderId(addH.getHeaderId());
			addLines.add(addL);
		}
		addLines.forEach(p -> {
			iIqcInspectionTemplateLService.insertSelective(ire, p);
		});
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public ResponseData inputDataFromExcel(HttpServletRequest request, IRequest requestContext, InputStream inputStream,
			String mainType) throws Exception {
		ResponseData responseData = new ResponseData();
		// TODO 解析
		Sheet sheet;
		XSSFWorkbook workBook;

		workBook = new XSSFWorkbook(inputStream);
		sheet = workBook.getSheetAt(0);
		// 校验结构
		checkInport(sheet, responseData, mainType);
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
					IqcInspectionTemplateH iqcInspectionTemplateH = new IqcInspectionTemplateH();
					Plant plant = new Plant();// 关联查询工厂
					plant.setPlantCode(rows.getCell(0) == null ? null : rows.getCell(0).toString());
					List<Plant> plantList = plantMapper.select(plant);
					if (plantList != null && plantList.size() > 0) {
						iqcInspectionTemplateH.setPlantId(plantList.get(0).getPlantId());
					}
					Item item = new Item();// 关联查询物料
					String itemCode = rows.getCell(1) == null ? null : rows.getCell(1).toString();
					if (itemCode.contains(".")) {
						item.setItemCode(itemCode.substring(0, itemCode.indexOf(".")));
					} else {
						item.setItemCode(itemCode);
					}
					item.setPlantId(plantList.get(0).getPlantId());
					List<Item> itemList = itemMapper.select(item);
					if (itemList != null && itemList.size() > 0) {
						iqcInspectionTemplateH.setItemId(itemList.get(0).getItemId());
					}
					iqcInspectionTemplateH.setItemEdition(rows.getCell(3) == null ? null : rows.getCell(3).toString());
					iqcInspectionTemplateH.setSourceType(iCodeService.getCodeValueByMeaning(requestContext,
							"HQM_SOURCE_TYPE_TEMP", rows.getCell(4) == null ? null : rows.getCell(4).toString()));
					List<IqcInspectionTemplateH> iqcInspectionTemplateHList = iqcInspectionTemplateHMapper
							.select(iqcInspectionTemplateH);
					if (iqcInspectionTemplateHList != null && iqcInspectionTemplateHList.size() > 0) {
						// 新增行数据
						IqcInspectionTemplateL iqcInspectionTemplateL = getIqcInspectionTemplateL(rows,
								inspectionAttributeNew, iqcInspectionTemplateHList.get(0), mainType);
						iqcInspectionTemplateL.setPrecision(
								Float.valueOf(SystemApiMethod.getPercision(iqcInspectionTemplateL.getStandradFrom())));
						iqcInspectionTemplateLMapper.insertSelective(iqcInspectionTemplateL);
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
						
						if(rows.getCell(5) == null ? null : rows.getCell(5).toString() != null
								&& !"".equals(rows.getCell(5) == null ? null : rows.getCell(5).toString())) {
							iqcInspectionTemplateH.setTimeLimit(Float.valueOf(dealFloat(rows.getCell(5).toString())));
						}
						iqcInspectionTemplateHMapper.insertSelective(iqcInspectionTemplateH);

						// 新增行数据
						IqcInspectionTemplateL iqcInspectionTemplateL = getIqcInspectionTemplateL(rows,
								inspectionAttributeNew, iqcInspectionTemplateH, mainType);
						iqcInspectionTemplateL.setPrecision(
								Float.valueOf(SystemApiMethod.getPercision(iqcInspectionTemplateL.getStandradFrom())));
						iqcInspectionTemplateLMapper.insertSelective(iqcInspectionTemplateL);
					}
				} else {
					// 头数据不存在则新增一条数据
					IqcInspectionTemplateH iqcInspectionTemplateH = new IqcInspectionTemplateH();
					Plant plant = new Plant();// 关联查询工厂
					plant.setPlantCode(rows.getCell(0) == null ? null : rows.getCell(0).toString());
					List<Plant> plantList = plantMapper.select(plant);
					if (plantList != null && plantList.size() > 0) {
						iqcInspectionTemplateH.setPlantId(plantList.get(0).getPlantId());
					}
					Item item = new Item();// 关联查询物料
					String itemCode = rows.getCell(1) == null ? null : rows.getCell(1).toString();
					if (itemCode.contains(".")) {
						item.setItemCode(itemCode.substring(0, itemCode.indexOf(".")));
					} else {
						item.setItemCode(itemCode);
					}
					item.setPlantId(plantList.get(0).getPlantId());
					List<Item> itemList = itemMapper.select(item);
					if (itemList != null && itemList.size() > 0) {
						iqcInspectionTemplateH.setItemId(itemList.get(0).getItemId());
					}
					iqcInspectionTemplateH.setSourceType(iCodeService.getCodeValueByMeaning(requestContext,
							"HQM_SOURCE_TYPE_TEMP", rows.getCell(4) == null ? null : rows.getCell(4).toString()));
					iqcInspectionTemplateH.setItemEdition(rows.getCell(3) == null ? null : rows.getCell(3).toString());
					List<IqcInspectionTemplateH> iqcInspectionTemplateHList = iqcInspectionTemplateHMapper
							.select(iqcInspectionTemplateH);
					if (iqcInspectionTemplateHList != null && iqcInspectionTemplateHList.size() > 0) {
						// 新增行数据
						InspectionAttribute inspectionAttributeR = inspectionAttributeList.get(0);
						IqcInspectionTemplateL iqcInspectionTemplateL = getIqcInspectionTemplateL(rows,
								inspectionAttributeR, iqcInspectionTemplateHList.get(0), mainType);
						iqcInspectionTemplateL.setPrecision(
								Float.valueOf(SystemApiMethod.getPercision(iqcInspectionTemplateL.getStandradFrom())));
						iqcInspectionTemplateLMapper.insertSelective(iqcInspectionTemplateL);
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
						if(rows.getCell(5) == null ? null : rows.getCell(5).toString() != null
								&& !"".equals(rows.getCell(5) == null ? null : rows.getCell(5).toString())) {
							iqcInspectionTemplateH.setTimeLimit(Float.valueOf(dealFloat(rows.getCell(5).toString())));
						}
						iqcInspectionTemplateHMapper.insertSelective(iqcInspectionTemplateH);

						// 新增行数据
						InspectionAttribute inspectionAttributeR = inspectionAttributeList.get(0);
						IqcInspectionTemplateL iqcInspectionTemplateL = getIqcInspectionTemplateL(rows,
								inspectionAttributeR, iqcInspectionTemplateH, mainType);
						iqcInspectionTemplateL.setPrecision(
								Float.valueOf(SystemApiMethod.getPercision(iqcInspectionTemplateL.getStandradFrom())));
						iqcInspectionTemplateLMapper.insertSelective(iqcInspectionTemplateL);
					}
				}
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

	public IqcInspectionTemplateL getIqcInspectionTemplateL(Row rows, InspectionAttribute inspectionAttributeR,
			IqcInspectionTemplateH iqcInspectionTemplateH, String mainType) throws Exception {
		IqcInspectionTemplateL iqcInspectionTemplateL = new IqcInspectionTemplateL();
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
						responseData.setMessage("第" + i + "行，物料编码为空！");
						return;
					}
					if (itemCode.contains(".")) {
						validateInfo.setItemCode(itemCode.substring(0, itemCode.indexOf(".")));
					} else {
						validateInfo.setItemCode(itemCode);
					}
					validateInfo.setItemVersion(rows.getCell(3) == null ? null : rows.getCell(3).toString());
					validateInfo.setAttributeName(rows.getCell(6) == null ? null : rows.getCell(6).toString());
					validateInfo.setAttributeTool(rows.getCell(11) == null ? null : rows.getCell(11).toString());
					validateInfoList.add(validateInfo);

					IqcInspectionTemplateH iqcInspectionTemplateH = new IqcInspectionTemplateH();
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
					Item item = new Item();
					item.setItemCode(validateInfo.getItemCode());
					List<Item> itemList = itemMapper.select(item);
					if (itemList != null && itemList.size() > 0) {
						iqcInspectionTemplateH.setItemId(itemList.get(0).getItemId());
					} else {
						responseData.setSuccess(false);
						responseData.setMessage("第" + i + "行，物料编码不存在！");
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
					List<IqcInspectionTemplateH> iqcInspectionTemplateHList = iqcInspectionTemplateHMapper
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
					if (iCodeService.getCodeValueByMeaning(sr, "HQM_IQC_QUALITY_GRADE",
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
						if (rows.getCell(14) == null || rows.getCell(15) == null) {
							responseData.setSuccess(false);
							responseData.setMessage("第" + i + "行，规格上下限为空 ！");
							return;
						}
						
						float StandradFrom = Float
								.parseFloat(rows.getCell(14) == null ? null : rows.getCell(14).toString());
						float StandradTo = Float
								.parseFloat(rows.getCell(15) == null ? null : rows.getCell(15).toString());
						if (StandradTo <= StandradFrom) {
							responseData.setSuccess(false);
							responseData.setMessage("第" + i + "行，规格上下限错误 ！");
							return;
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
				if (!vdi.getItemCode().equals(vdis.get(j).getItemCode())) {
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hand.hqm.hqm_pqc_warning.service.IPqcWarningService#
	 * updateByPrimaryKeySelectiveRecord(com.hand.hap.core.IRequest,
	 * com.hand.hqm.hqm_pqc_warning.dto.PqcWarning)
	 */
	@Override
	public IqcInspectionTemplateH updateByPrimaryKeySelectiveRecord(IRequest request, IqcInspectionTemplateH t) {
		IqcInspectionTemplateH now = mapper.selectByPrimaryKey(t);
		self().updateByPrimaryKeySelective(request, t);
		IqcInspectionTemplateH after = mapper.selectByPrimaryKey(t);
		IqcInspectionTemplateHHis his = new IqcInspectionTemplateHHis();
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
		iIqcInspectionTemplateHHisService.insertSelective(request, his);
		objectEventsRecord(now, after, his, request);
		return t;
	}

	private void objectEventsRecord(IqcInspectionTemplateH now, IqcInspectionTemplateH after,
			IqcInspectionTemplateHHis his, IRequest request) {
		ObjectEvents oe = new ObjectEvents();
		oe.setEventId(his.getEventId());
		oe.setEventTime(his.getEventTime());
		oe.setEventBy(his.getEventBy());
		oe.setEventTable("HQM_IQC_INSPECTION_TEMPLATE_H");
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
								"iqcinspectiontemplateh." + nowField.getName().toLowerCase()) + ":"
								+ changeInfoGetNumber(request, nowFloat, afterFloat, nowField.getName()) + "<br/>";
					}
				} else if (nowField.getType() == String.class) {
					String nowString = nowField.get(now) == null ? "" : (String) nowField.get(now);
					String afterString = afterField.get(after) == null ? "" : (String) afterField.get(after);
					if (!(nowString).equals(afterString)) {
						eventContent += SystemApiMethod.getPromptDescription(request, iPromptService,
								"iqcinspectiontemplateh." + nowField.getName().toLowerCase()) + ":"
								+ changeInfoGetString(request, nowString, afterString, nowField.getName(),
										now.getMainType())
								+ "<br/>";
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

	private String changeInfoGetString(IRequest request, String now, String after, String fieldName, String mainType) {
		String str1 = "";
		String str2 = "";
		if ("sourceType".equals(fieldName)) {// HQM_PLM_SKU_TYPE
			str1 = iCodeService.getCodeMeaningByValue(request,
					"IQC".equals(mainType) ? "HQM_IQC_SOURCE_TYPE" : "HQM_FQC_SOURCE_TYPE", now);
			str2 = iCodeService.getCodeMeaningByValue(request,
					"IQC".equals(mainType) ? "HQM_IQC_SOURCE_TYPE" : "HQM_FQC_SOURCE_TYPE", after);
		} else if ("status".equals(fieldName)) {
			str1 = iCodeService.getCodeMeaningByValue(request, "HQM_INSPECTION_TEMPLATE_STATUS", now);
			str2 = iCodeService.getCodeMeaningByValue(request, "HQM_INSPECTION_TEMPLATE_STATUS", after);
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
	public IqcInspectionTemplateH insertSelectiveRecord(IRequest request, IqcInspectionTemplateH t) {
		self().insertSelective(request, t);
		IqcInspectionTemplateH after = mapper.selectByPrimaryKey(t);
		IqcInspectionTemplateHHis his = new IqcInspectionTemplateHHis();
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
		iIqcInspectionTemplateHHisService.insertSelective(request, his);
		return t;
	}

	private Float getPlantId(String plantCode) {
		Plant ser = new Plant();
		ser.setPlantCode(plantCode);
		return plantMapper.select(ser).get(0).getPlantId();
	}
}