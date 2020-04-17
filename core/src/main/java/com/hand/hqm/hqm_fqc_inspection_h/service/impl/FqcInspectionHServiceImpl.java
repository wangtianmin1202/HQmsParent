package com.hand.hqm.hqm_fqc_inspection_h.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.StringUtil;
import com.hand.hap.activiti.dto.ProcessInstanceResponseExt;
import com.hand.hap.activiti.service.IActivitiService;
import com.hand.hap.core.IRequest;
import com.hand.hap.core.impl.RequestHelper;
import com.hand.hap.core.impl.ServiceRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.system.service.ICodeService;
import com.hand.hap.system.service.IPromptService;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import com.hand.hap.util.SoapPostUtil;
import com.hand.hap.util.SystemApiMethod;
import com.hand.hcm.hcm_item.dto.Item;
import com.hand.hcm.hcm_item.mapper.ItemMapper;
import com.hand.hcm.hcm_item_b.dto.ItemB;
import com.hand.hcm.hcm_item_b.mapper.ItemBMapper;
import com.hand.hcm.hcm_plant.dto.Plant;
import com.hand.hcm.hcm_plant.mapper.PlantMapper;
import com.hand.hcs.hcs_suppliers.dto.Suppliers;
import com.hand.hcs.hcs_suppliers.mapper.SuppliersMapper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;

import com.hand.hqm.hqm_fqc_inspection_h.dto.FqcInspectionH;
import com.hand.hqm.hqm_fqc_inspection_h.dto.FqcTestConclusionInfo;
import com.hand.hqm.hqm_fqc_inspection_h.dto.OqcTestConclusionInfo;
import com.hand.hqm.hqm_fqc_inspection_h.dto.SamplingFeedbackInfo;
import com.hand.hqm.hqm_fqc_inspection_h.idto.FqcInspectionResult;
import com.hand.hqm.hqm_fqc_inspection_h.service.IFqcInspectionHService;
import com.hand.hqm.hqm_asl_iqc_control.mapper.AslIqcControlMapper;
import com.hand.hqm.hqm_fqc_inspection_d.dto.FqcInspectionD;
import com.hand.hqm.hqm_fqc_inspection_d.mapper.FqcInspectionDMapper;
import com.hand.hqm.hqm_fqc_inspection_d.service.IFqcInspectionDService;
import com.hand.hqm.hqm_fqc_inspection_h.mapper.FqcInspectionHMapper;
import com.hand.hqm.hqm_fqc_inspection_l.dto.FqcInspectionL;
import com.hand.hqm.hqm_fqc_inspection_l.mapper.FqcInspectionLMapper;
import com.hand.hqm.hqm_fqc_inspection_l.service.IFqcInspectionLService;
import com.hand.hqm.hqm_fqc_inspection_template_h.mapper.FqcInspectionTemplateHMapper;
import com.hand.hqm.hqm_fqc_inspection_template_l.dto.FqcInspectionTemplateL;
import com.hand.hqm.hqm_fqc_inspection_template_l.mapper.FqcInspectionTemplateLMapper;
import com.hand.hqm.hqm_fqc_sample_switch.dto.FqcSampleSwitch;
import com.hand.hqm.hqm_fqc_sample_switch.mapper.FqcSampleSwitchMapper;
import com.hand.hqm.hqm_inspection_attribute.dto.InspectionAttribute;
import com.hand.hqm.hqm_inspection_attribute.mapper.InspectionAttributeMapper;
import com.hand.hqm.hqm_iqc_inspection_template_h.dto.IqcInspectionTemplateH;
import com.hand.hqm.hqm_iqc_inspection_template_h.mapper.IqcInspectionTemplateHMapper;
import com.hand.hqm.hqm_iqc_inspection_template_l.dto.IqcInspectionTemplateL;
import com.hand.hqm.hqm_iqc_inspection_template_l.mapper.IqcInspectionTemplateLMapper;
import com.hand.hqm.hqm_iqc_inspection_template_l.service.IIqcInspectionTemplateLService;
import com.hand.hqm.hqm_item_control.dto.ItemControlQms;
import com.hand.hqm.hqm_item_control.mapper.ItemControlQmsMapper;
import com.hand.hqm.hqm_nonconforming_order.dto.NonconformingOrder;
import com.hand.hqm.hqm_nonconforming_order.mapper.NonconformingOrderMapper;
import com.hand.hqm.hqm_nonconforming_order.service.impl.NonconformingOrderServiceImpl;
import com.hand.hqm.hqm_online_sku_rule.dto.OnlineSkuRule;
import com.hand.hqm.hqm_online_sku_rule.mapper.OnlineSkuRuleMapper;
import com.hand.hqm.hqm_platform_program.dto.PlatformProgram;
import com.hand.hqm.hqm_platform_program.mapper.PlatformProgramMapper;
import com.hand.hqm.hqm_pqc_calendar_a.dto.PqcCalendarA;
import com.hand.hqm.hqm_pqc_calendar_a.service.IPqcCalendarAService;
import com.hand.hqm.hqm_pqc_calendar_b.dto.PqcCalendarB;
import com.hand.hqm.hqm_pqc_calendar_b.service.IPqcCalendarBService;
import com.hand.hqm.hqm_pqc_calendar_c.dto.PqcCalendarC;
import com.hand.hqm.hqm_pqc_calendar_c.service.IPqcCalendarCService;
import com.hand.hqm.hqm_program_sku_rel.dto.ProgramSkuRel;
import com.hand.hqm.hqm_program_sku_rel.mapper.ProgramSkuRelMapper;
import com.hand.hqm.hqm_program_sku_rel.service.IProgramSkuRelService;
import com.hand.hqm.hqm_qc_task.dto.FqcTask;
import com.hand.hqm.hqm_qc_task.dto.IqcTask;
import com.hand.hqm.hqm_qc_task.dto.OqcTask;
import com.hand.hqm.hqm_qc_task.mapper.FqcTaskMapper;
import com.hand.hqm.hqm_qc_task.mapper.IqcTaskMapper;
import com.hand.hqm.hqm_qc_task.mapper.OqcTaskMapper;
import com.hand.hqm.hqm_qc_task.service.IFqcTaskService;
import com.hand.hqm.hqm_sample_manage.dto.SampleManage;
import com.hand.hqm.hqm_sample_manage.mapper.SampleManageMapper;
import com.hand.hqm.hqm_sample_scheme.dto.SampleScheme;
import com.hand.hqm.hqm_sample_scheme.mapper.SampleSchemeMapper;
import com.hand.hqm.hqm_sample_size_code_letter.dto.SampleSizeCodeLetter;
import com.hand.hqm.hqm_sample_size_code_letter.mapper.SampleSizeCodeLetterMapper;
import com.hand.hqm.hqm_switch_score.dto.SwitchScore;
import com.hand.hqm.hqm_switch_score.mapper.SwitchScoreMapper;
import com.hand.hqm.hqm_switch_score.service.ISwitchScoreService;
import com.hand.sys.sys_if_invoke_outbound.dto.IfInvokeOutbound;
import com.hand.sys.sys_if_invoke_outbound.mapper.IfInvokeOutboundMapper;

import org.springframework.transaction.annotation.Transactional;
import org.activiti.rest.service.api.runtime.process.ProcessInstanceCreateRequest;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@Transactional(rollbackFor = Exception.class)
public class FqcInspectionHServiceImpl extends BaseServiceImpl<FqcInspectionH> implements IFqcInspectionHService {
	@Autowired
	FqcInspectionHMapper fqcInspectionHMapper;
	@Autowired
	InspectionAttributeMapper inspectionAttributeMapper;
	@Autowired
	IqcTaskMapper iqcTaskMapper;
	@Autowired
	FqcTaskMapper fqcTaskMapper;
	@Autowired
	FqcInspectionDMapper fqcInspectionDMapper;
	@Autowired
	FqcInspectionLMapper fqcInspectionLMapper;
	@Autowired
	IPromptService iPromptService;
	@Autowired
	AslIqcControlMapper aslFqcControlMapper;
	@Autowired
	FqcInspectionTemplateHMapper fqcInspectionTemplateHMapper;
	@Autowired
	IqcInspectionTemplateHMapper iqcInspectionTemplateHMapper;
	@Autowired
	FqcInspectionTemplateLMapper fqcInspectionTemplateLMapper;
	@Autowired
	IqcInspectionTemplateLMapper iqcInspectionTemplateLMapper;
	@Autowired
	ISwitchScoreService iSwitchScoreService;
	@Autowired
	SwitchScoreMapper switchScoreMapper;
	@Autowired
	SuppliersMapper suppliersMapper;
	@Autowired
	SampleSizeCodeLetterMapper sampleSizeCodeLetterMapper;
	@Autowired
	SampleSchemeMapper sampleSchemeMapper;
	@Autowired
	SampleManageMapper sampleManageMapper;
	@Autowired
	IFqcInspectionDService iFqcInspectionDService;
	@Autowired
	IFqcInspectionLService iFqcInspectionLService;
	@Autowired
	PlatformProgramMapper platformProgramMapper;
	@Autowired
	IPqcCalendarAService iPqcCalendarAService;
	@Autowired
	IPqcCalendarBService iPqcCalendarBService;
	@Autowired
	IPqcCalendarCService iPqcCalendarCService;
	@Autowired
	ProgramSkuRelMapper programSkuRelMapper;
	@Autowired
	IFqcTaskService iFqcTaskService;
	@Autowired
	OnlineSkuRuleMapper onlineSkuRuleMapper;
	@Autowired
	IProgramSkuRelService iProgramSkuRelService;
	@Autowired
	FqcSampleSwitchMapper fqcSampleSwitchMapper;
	@Autowired
	IIqcInspectionTemplateLService iIqcInspectionTemplateLService;
	@Autowired
	ICodeService iCodeService;
	@Autowired
	IfInvokeOutboundMapper ifInvokeOutboundMapper;
	@Autowired
	ItemControlQmsMapper itemControlQmsMapper;
	@Autowired
	PlantMapper plantMapper;
	@Autowired
	ItemBMapper itemBMapper;
	@Autowired
	FqcTaskMapper ffqcTaskMapper;
	@Autowired
	ItemMapper itemMapper;
	@Autowired
	NonconformingOrderMapper nonconformingOrderMapper;
	@Autowired
	private IActivitiService activitiService;
	@Autowired
	OqcTaskMapper oqcTaskMapper;
	@Resource(name = "taskExecutor")
	private TaskExecutor taskExecutor;
	Logger logger = LoggerFactory.getLogger(FqcInspectionHServiceImpl.class);

	@Override
	public List<FqcInspectionH> selectByNumber(IRequest requestContext, FqcInspectionH dto) {
		// TODO 查询检验单号
		return fqcInspectionHMapper.selectByNumber(dto);
	}

	/**
	 * 
	 * tianmin.wang 20190725
	 * 
	 * @throws Exception 暂挂检验单
	 */
	@Override
	public ResponseData pauseInspection(FqcInspectionH dto, List<FqcInspectionL> lineList, HttpServletRequest request)
			throws Exception {

		// TODO 检验单暂挂
		ResponseData responseData = new ResponseData();
		FqcInspectionH updaterFqcInspectionH = dto;
		updaterFqcInspectionH.setInspectionStatus("3");
		// 更新检验单的状态为 检测中3
		fqcInspectionHMapper.updateByPrimaryKeySelective(updaterFqcInspectionH);

		IqcTask updateFqcTask = new IqcTask();
		IqcTask searchFqcTask = new IqcTask();
		searchFqcTask.setInspectionNum(updaterFqcInspectionH.getInspectionNum());
		List<IqcTask> fqcTaskList = new ArrayList<IqcTask>();
		fqcTaskList = iqcTaskMapper.select(searchFqcTask);
		if (fqcTaskList.size() != 0) {
			updateFqcTask = fqcTaskList.get(0);
			updateFqcTask.setSolveStatus("3");
			// 更新检验任务的状态为 检测中3
			iqcTaskMapper.updateByPrimaryKey(updateFqcTask);
		}

		for (FqcInspectionL fqcInspectionL : lineList) {
			// 保存每项检验项状态并同时保存每项检验项的值 至 HQM_FQC_INSPECTION_D表内
//			String[] hisArray = new String[] {};
//			if (fqcInspectionL.getInspectionHistory() != null) {
//				hisArray = fqcInspectionL.getInspectionHistory().split(",");
//			}
//			for (int i = 0; i < hisArray.length; i++) {
//				saveInspectionD(fqcInspectionL.getLineId(), String.valueOf(i), hisArray[i], fqcInspectionL);
//			}
			fqcInspectionLMapper.updateByPrimaryKeySelective(fqcInspectionL);
		}
		return responseData;
	}

	public void saveInspectionD(Float lineId, String orderNum, String data, FqcInspectionL fqcInspectionL) {
		FqcInspectionD search = new FqcInspectionD();
		search.setLineId(lineId);
		search.setOrderNum(orderNum);
		List<FqcInspectionD> li = new ArrayList<FqcInspectionD>();
		String judgement = new String();
		if ("M".equals(fqcInspectionL.getStandardType())) {
			// M型需要依照规格值从和规格值至计算judgement
			if (Float.valueOf(data).floatValue() < Float.valueOf(fqcInspectionL.getStandradFrom())
					|| Float.valueOf(data).floatValue() > Float.valueOf(fqcInspectionL.getStandradTo())) {
				judgement = "NG";
			} else {
				judgement = "OK";
			}

		} else {
			judgement = data;
		}
		li = fqcInspectionDMapper.select(search);
		if (li.size() == 0) {
			FqcInspectionD insert = new FqcInspectionD();
			insert.setLineId(lineId);
			insert.setOrderNum(orderNum);
			insert.setData(data);
			insert.setJudgement(judgement);
			fqcInspectionDMapper.insertSelective(insert);
		} else {
			FqcInspectionD updater = li.get(0);
			updater.setData(data);
			updater.setJudgement(judgement);
			fqcInspectionDMapper.updateByPrimaryKeySelective(updater);
		}
	}

	/**
	 * tianmin.wang 20190725 提交检验单
	 */
	@Override
	public ResponseData commitInspection(FqcInspectionH dto, List<FqcInspectionL> lineList, HttpServletRequest request,
			IRequest requestContext) throws Exception {
		// TODO 检验单提交
		ResponseData responseData = new ResponseData();
		FqcInspectionH updaterFqcInspectionH = dto;
		String inspectionStatus = "4";
		if ("OK".equals(dto.getInspectionJudge())) {
			inspectionStatus = "5";

			updaterFqcInspectionH.setDetailStatus("C");
			updaterFqcInspectionH.setInspectionStatus("4");
			updaterFqcInspectionH.setInspectionDate(new Date());
			// 更新检验单的状态为 待审核4
			fqcInspectionHMapper.updateByPrimaryKeySelective(updaterFqcInspectionH);
		} else if ("NG".equals(dto.getInspectionJudge())) {
			inspectionStatus = "4";
		} else {

		}
		/*
		 * updaterFqcInspectionH.setDetailStatus("C");
		 * updaterFqcInspectionH.setInspectionStatus("4");
		 * updaterFqcInspectionH.setInspectionDate(new Date()); // 更新检验单的状态为 待审核4
		 * fqcInspectionHMapper.updateByPrimaryKeySelective(updaterFqcInspectionH);
		 */

		IqcTask updateFqcTask = new IqcTask();
		IqcTask searchFqcTask = new IqcTask();
		searchFqcTask.setInspectionNum(updaterFqcInspectionH.getInspectionNum());
		List<IqcTask> fqcTaskList = new ArrayList<IqcTask>();

		fqcTaskList = iqcTaskMapper.select(searchFqcTask);
		if (fqcTaskList.size() > 0) {
			updateFqcTask = fqcTaskList.get(0);
			updateFqcTask.setSolveStatus(inspectionStatus);
			// 更新检验任务的状态为 待审核4
			iqcTaskMapper.updateByPrimaryKey(updateFqcTask);
		}
		FqcInspectionL max = new FqcInspectionL();
		max.setSampleQty(0f);
		Float passQuantity = 0f;
		Float ngQuantity = 0f;
		for (FqcInspectionL fqcInspectionL : lineList) {
			FqcInspectionD sear = new FqcInspectionD();
			sear.setLineId(fqcInspectionL.getLineId());
			List<FqcInspectionD> result = fqcInspectionDMapper.select(sear);
			if (fqcInspectionL.getSampleQty() > max.getSampleQty()) {
				passQuantity = 0f;
				ngQuantity = 0f;
				max = fqcInspectionL;
				for (FqcInspectionD ddto : result) {
					if ("NG".equals(ddto.getSnStatus())) {
						ngQuantity++;
					} else {
						passQuantity++;
					}
					if (StringUtils.isEmpty(ddto.getData())) {
						throw new RuntimeException(SystemApiMethod.getPromptDescription(request, iPromptService,
								"error.hqm_iqc_order_submit_02"));
					}
				}
			} else {
				for (FqcInspectionD ddto : result) {
					if (StringUtils.isEmpty(ddto.getData())) {
						throw new RuntimeException(SystemApiMethod.getPromptDescription(request, iPromptService,
								"error.hqm_iqc_order_submit_02"));
					}
				}
			}
			fqcInspectionLMapper.updateByPrimaryKeySelective(fqcInspectionL);
		}
		FqcInspectionH update = new FqcInspectionH();
		update.setHeaderId(dto.getHeaderId());
		update.setPassQuantity(passQuantity);
		update.setNgQuantity(ngQuantity);
		update.setInspectionDate(new Date());
		self().updateByPrimaryKeySelective(RequestHelper.createServiceRequest(request), update);
		// 如果检验为OK则默认执行一次审核
		if ("5".equals(inspectionStatus)) {
			updaterFqcInspectionH.setApprovalResult("OK");
			this.auditInspection(updaterFqcInspectionH, lineList, request);
		} else if ("4".equals(inspectionStatus)) {// 不合格时发起流程
			updaterFqcInspectionH.setInspectionStatus("5");
			updaterFqcInspectionH.setInspectionDate(new Date());
			updaterFqcInspectionH.setDetailStatus("C");
			updaterFqcInspectionH.setApprovalResult("NG");
			this.auditInspection(updaterFqcInspectionH, lineList, request);

			// 生成不合格单号
			String noNum;// 检验单号
			Date date = new Date();
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
			String time = dateFormat.format(date);
			String noNumStart = "NG" + dto.getPlantCode() + "-" + time.substring(2) + "-";
			// 流水一个检验单号
			noNum = getNoNumber(noNumStart);

			NonconformingOrder nonconformingOrder = new NonconformingOrder();
			nonconformingOrder.setNoNum(noNum);
			nonconformingOrder.setInspectionType(dto.getInspectionType());
			nonconformingOrder.setInspectionId(dto.getHeaderId());
			nonconformingOrder.setPlantId(dto.getPlantId());
			nonconformingOrder.setItemId(dto.getItemId());
			nonconformingOrder.setNoStatus("0");

			if (dto.getProductionBatch() != null) {
				nonconformingOrder.setProductionBatch(dto.getProductionBatch());
			}

			if (dto.getProduceDate() != null) {
				nonconformingOrder.setReceiveDate(dto.getProduceDate());
			}

			nonconformingOrder.setTotalityQty(dto.getProduceQty());
			nonconformingOrder.setSampleSize(dto.getSampleQty());

			if (dto.getInspectionDate() != null) {
				nonconformingOrder.setInspectionDate(dto.getInspectionDate());
			}

			if (dto.getInspectorId() != null) {
				nonconformingOrder.setApprovalId(dto.getInspectorId());
			}
			nonconformingOrder.setNoSourceType("1");
			nonconformingOrderMapper.insertSelective(nonconformingOrder);

			// 启动流程
			ProcessInstanceCreateRequest instanceCreateRequest = new ProcessInstanceCreateRequest();
			instanceCreateRequest.setBusinessKey(String.valueOf(nonconformingOrder.getNoId()));
			ProcessInstanceResponseExt responseExt = new ProcessInstanceResponseExt();

			if ("IQC".equals(dto.getInspectionType())) {
				instanceCreateRequest.setProcessDefinitionKey("iqcUnqualityProcess");
				responseExt = (ProcessInstanceResponseExt) activitiService.startProcess(requestContext,
						instanceCreateRequest);
			} else if ("FQC".equals(dto.getInspectionType())) {
				instanceCreateRequest.setProcessDefinitionKey("fqcUnqualityProcess");
				responseExt = (ProcessInstanceResponseExt) activitiService.startProcess(requestContext,
						instanceCreateRequest);
			}

			nonconformingOrder.setProcessInstanceId(responseExt.getId());
			nonconformingOrderMapper.updateByPrimaryKeySelective(nonconformingOrder);
		}

		/**
		 * wtm-20200218 检验性质为20-在线新品检验的检验单提交后，根据检验单的工厂与物料在hqm_program_sku_rel中进行记录
		 */
		if ("20".equals(updaterFqcInspectionH.getSourceType()))
			programSkuRelCommmit(request, updaterFqcInspectionH);

		return responseData;
	}

	public String getNoNumber(String inspectionNumStart) {
		Integer count = 1;
		NonconformingOrder sr = new NonconformingOrder();
		sr.setNoNum(inspectionNumStart);
		List<NonconformingOrder> list = new ArrayList<NonconformingOrder>();
		list = nonconformingOrderMapper.selectMaxNumber(sr);
		if (list != null && list.size() > 0) {
			String NoNum = list.get(0).getNoNum();
			String number = NoNum.substring(NoNum.length() - 5);// 流水
			count = Integer.valueOf(number) + 1;
		}
		String str = String.format("%05d", count);
		return inspectionNumStart + str;// 最终检验单号
	}

	/**
	 * @description
	 * @author tianmin.wang
	 * @date 2020年2月18日
	 * @param request
	 * @param updaterFqcInspectionH
	 */
	private void programSkuRelCommmit(HttpServletRequest request, FqcInspectionH fqc) {
		ProgramSkuRel skuRelSearch = new ProgramSkuRel();
		skuRelSearch.setItemId(fqc.getItemId());
		skuRelSearch.setPlantId(fqc.getPlantId());
		List<ProgramSkuRel> result = programSkuRelMapper.select(skuRelSearch);
		if (result != null && result.size() > 0) {
			OnlineSkuRule skuRuleSearch = new OnlineSkuRule();
			skuRuleSearch.setPlantId(result.get(0).getPlantId());
			skuRuleSearch.setSkuType(result.get(0).getSkuType());
			List<OnlineSkuRule> resultRule = onlineSkuRuleMapper.select(skuRuleSearch);
			result.get(0).setTotalQty(
					result.get(0).getTotalQty() == null ? 0 : result.get(0).getTotalQty() + fqc.getProduceQty());
			result.get(0).setOkQty(result.get(0).getOkQty() == null ? 0 : result.get(0).getOkQty() + fqc.getOkQty());
			if (result.get(0)
					.getTotalQty() >= ((resultRule != null && resultRule.size() > 0) ? resultRule.get(0).getInspectQty()
							: 0)) {
				// 累计合格数/累计样本总量是否大于等于目标合格率
				if (result.get(0).getOkQty() / result.get(0).getTotalQty()
						* 100 >= ((resultRule != null && resultRule.size() > 0) ? resultRule.get(0).getPassPercent()
								: 0)) {
					// 若大于则更新hqm_program_sku_rel表is_pass字段为 Y
					result.get(0).setIsPass("Y");
				} else {
					// 否则清空样本总量和合格数
					result.get(0).setTotalQty(0f);
					result.get(0).setOkQty(0f);
				}
			}
			// 更新数据
			iProgramSkuRelService.updateByPrimaryKeySelective(RequestHelper.createServiceRequest(request),
					skuRelSearch);
		}

	}

	@Override
	public ResponseData auditInspection(FqcInspectionH dto, List<FqcInspectionL> lineList, HttpServletRequest request)
			throws Exception {
		// TODO 检验单审核
		ResponseData responseData = new ResponseData();

		FqcInspectionH updaterFqcInspectionH = dto;
// wtm-20200219 
//		if (!StringUtils.isEmpty(dto.getChangedValue()) && !"-1".equals(dto.getChangedValue())) {
//			// 抽样方案逻辑
//			SwitchScore searchSwitchScore = new SwitchScore();
//			searchSwitchScore.setItemId(dto.getItemId());
//			searchSwitchScore.setPlantId(dto.getPlantId());
//			List<SwitchScore> resultSwitchScoreList = new ArrayList<>();
//			resultSwitchScoreList = switchScoreMapper.select(searchSwitchScore);
//			if (resultSwitchScoreList == null || resultSwitchScoreList.size() == 0) {
//				/**
//				 * 如果由其他状态 变更为 正常检验 那么 resultSwitchScoreList 是肯定存在的 不会导致新增操作
//				 */
//				SwitchScore ssInsert = new SwitchScore();
//				ssInsert.setItemId(dto.getItemId());
//				ssInsert.setPlantId(dto.getPlantId());
//				ssInsert.setSamplePlanTypeNow("N");
//				ssInsert.setSamplePlanTypeNext(dto.getChangedValue());
//				switchScoreMapper.insertSelective(ssInsert);
//			} else {
//				SwitchScore updaterSwitchScore = new SwitchScore();
//				updaterSwitchScore.setScoreId(resultSwitchScoreList.get(0).getScoreId());
//				updaterSwitchScore.setSamplePlanTypeNow(resultSwitchScoreList.get(0).getSamplePlanTypeNext());
//				updaterSwitchScore.setSamplePlanTypeNext(dto.getChangedValue());
//				switchScoreMapper.updateByPrimaryKeySelective(updaterSwitchScore);
//			}
//		}
		updaterFqcInspectionH.setInspectionStatus("5");
		// 更新检验单的状态为 已完成5
		fqcInspectionHMapper.updateByPrimaryKeySelective(updaterFqcInspectionH);

		IqcTask updateFqcTask = new IqcTask();
		IqcTask searchFqcTask = new IqcTask();
		searchFqcTask.setInspectionNum(updaterFqcInspectionH.getInspectionNum());
		List<IqcTask> fqcTaskList = new ArrayList<IqcTask>();

		fqcTaskList = iqcTaskMapper.select(searchFqcTask);
		if (fqcTaskList.size() > 0) {
			updateFqcTask = fqcTaskList.get(0);
			updateFqcTask.setSolveStatus("C");
			updateFqcTask.setInspectionRes(updaterFqcInspectionH.getApprovalResult());
			// 更新检验任务的状态为 已完成5 20191220 已完成变成了C
			iqcTaskMapper.updateByPrimaryKey(updateFqcTask);
		}

		// 审核时不再需要跟新行状态
//				for(FqcInspectionL fqcInspectionL : lineList) {
//					//保存每项检验项状态并同时保存每项检验项的值 至 HQM_IQC_INSPECTION_D表内
//					String[] hisArray = new String[] {};
//					if(fqcInspectionL.getInspectionHistory() != null){
//						hisArray = fqcInspectionL.getInspectionHistory().split(",");
//					}
//					if(hisArray.length != dto.getSampleSize() ) {
//						throw new Exception(getMessageByPromptCode(request,"error.hqm_fqc_order_submit_02"));
//					}
//					for(int i=0;i<hisArray.length;i++) {
//						saveInspectionD(fqcInspectionL.getLineId(),String.valueOf(i),hisArray[i]);
//					}
//					fqcInspectionLMapper.updateByPrimaryKeySelective(fqcInspectionL);
//				}

		// wtm-20200224 FQC检验单审核完成时 执行 FQC检验结论 接口调用的逻辑
		if ("6".equals(dto.getSourceType())) {// 为 FQC.首次检验 触发抽样方案变更 必须放在检验单变更之后执行
			FqcInspectionL searchL = new FqcInspectionL();
			searchL.setHeaderId(dto.getHeaderId());
			lineList = fqcInspectionLMapper.select(searchL);// 获取所有检验项
			responseData.setMessage(changeSamlePlan(request, dto, lineList));
		}
		pushToMes(updaterFqcInspectionH);

		// 审核后反馈检验结论
		chooseInspectionType(dto, lineList);

		return responseData;
	}

	/**
	 * @author kai.li
	 * @param dto
	 * @param lineList
	 */
	@Override
	public void chooseInspectionType(FqcInspectionH dto, List<FqcInspectionL> lineList) {
		if ("FQC".equals(dto.getInspectionType())) {// FQC检验单
			conclusionToWMS(dto, lineList);
		} else if ("OQC".equals(dto.getInspectionType())) {// OQC检验单
			oqcConclusionToWMS(dto, lineList);
		}
	}

	/**
	 * OQC反馈检验结论
	 * 
	 * @author kai.li
	 * @param dto
	 * @param lineList
	 */
	@Transactional(rollbackFor = Exception.class)
	public void oqcConclusionToWMS(FqcInspectionH dto, List<FqcInspectionL> lineList) {
		IfInvokeOutbound iio = new IfInvokeOutbound();
		OqcTestConclusionInfo otci = new OqcTestConclusionInfo();
		// 关联工厂表查询
		Plant plant = new Plant();
		plant.setPlantId(dto.getPlantId());
		List<Plant> plantList = plantMapper.select(plant);
		if (plantList != null && plantList.size() > 0) {
			otci.setPlantCode(plantList.get(0).getPlantCode());
		} else {
			// 工厂编码在工厂表中找不到！
			logger.warn("工厂编码在工厂表中找不到！");
			return;
		}

		// 关联检验通知表查询
		OqcTask fqcTask = new OqcTask();
		fqcTask.setInspectionNum(dto.getInspectionNum());
		List<OqcTask> fqcTaskList = oqcTaskMapper.select(fqcTask);
		if (fqcTaskList != null && fqcTaskList.size() > 0) {
			OqcTask fqcTaskR = fqcTaskList.get(0);
			// 关联物料表查询
			Item item = new Item();
			item.setItemId(fqcTaskR.getItemId());
			List<Item> itemList = itemMapper.select(item);
			if (itemList != null && itemList.size() > 0) {
				otci.setItemCode(itemList.get(0).getItemCode());
			} else {
				// 物料编码在物料表中找不到！
				logger.warn("物料编码在物料表中找不到！");
				return;
			}
			otci.setItemVersion(fqcTaskR.getItemVersion());
			otci.setSpreading(fqcTaskR.getSpreading());
			otci.setLotNumber(fqcTaskR.getProductionBatch());
		} else {
			// 无法关联检验通知表！
			logger.warn("无法关联检验通知表！");
			return;
		}
		otci.setInspectionResult(dto.getInspectionJudge());
		taskExecutor.execute(new Runnable() {
			@Override
			public void run() {
				// 转为JSON String发送
				ObjectMapper omapper = new ObjectMapper();
				String post;
				try {
					ServiceRequest sr = new ServiceRequest();
					sr.setLocale("zh_CN");
					String uri = iCodeService.getCodeValueByMeaning(sr, "HAP.SYSTEM", "WMS_WS_URI");// 获取调用地址
					post = omapper.writeValueAsString(otci);
					SoapPostUtil.Response re = SoapPostUtil.ticketSrmToWms("getOqcInspectionResult", post, iio, uri);
					ifInvokeOutboundMapper.insertSelective(iio);
					logger.info(SoapPostUtil.getStringFromResponse(re));
				} catch (Exception e) {
					iio.setResponseContent(e.getMessage());
					iio.setResponseCode("E");
					ifInvokeOutboundMapper.insertSelective(iio);
					logger.warn(e.getMessage());
				}
			}
		});
	}

	/**
	 * FQC反馈检验结论
	 * 
	 * @author kai.li
	 * @param dto
	 * @param lineList
	 */
	@Transactional(rollbackFor = Exception.class)
	public void conclusionToWMS(FqcInspectionH dto, List<FqcInspectionL> lineList) {
		IfInvokeOutbound iio = new IfInvokeOutbound();
		FqcTestConclusionInfo ftci = new FqcTestConclusionInfo();
		// 关联工厂表查询
		Plant plant = new Plant();
		plant.setPlantId(dto.getPlantId());
		List<Plant> plantList = plantMapper.select(plant);
		if (plantList != null && plantList.size() > 0) {
			ftci.setPlantCode(plantList.get(0).getPlantCode());
		} else {
			// 工厂编码在工厂表中找不到！
			logger.warn("工厂编码在工厂表中找不到！");
			return;
		}
		ftci.setOrderNo(dto.getSourceOrder());
		ftci.setOrderCheckResult(dto.getInspectionJudge());
		ftci.setInspectionNum(dto.getInspectionNum());
		// 取物料
		ItemB itemBSearch = new ItemB();
		itemBSearch.setItemId(dto.getItemId());
		itemBSearch.setPlantId(dto.getPlantId());
		List<ItemB> itemBResult = itemBMapper.select(itemBSearch);
		if (itemBResult != null && itemBResult.size() > 0) {
			ftci.setItemCode(itemBResult.get(0).getItemCode());
		}
		ftci.setLotNumber(dto.getProductionBatch());
		ftci.setItemVersion(dto.getItemEdition());
		// 从FQC task中找 spreading
		FqcTask fqcTaskSearch = new FqcTask();
		fqcTaskSearch.setInspectionNum(dto.getInspectionNum());
		List<FqcTask> fqcTaskSearchResult = fqcTaskMapper.select(fqcTaskSearch);
		if (fqcTaskSearchResult != null && fqcTaskSearchResult.size() > 0)
			ftci.setSpreading(fqcTaskSearchResult.get(0).getSpreading());// 挡位号
		ftci.setOkQty(String.valueOf(dto.getOkQty()));
		ftci.setNgQty(String.valueOf(dto.getNgQty()));
		// 从不合格单中找 dealmethod
		NonconformingOrder noSearch = new NonconformingOrder();
		noSearch.setInspectionCode(dto.getInspectionNum());
		noSearch.setInspectionType("FQC");
		List<NonconformingOrder> noResult = nonconformingOrderMapper.select(noSearch);
		if (noResult != null && noResult.size() > 0) {
			ftci.setDealMethod(noResult.get(0).getDealMethod());
		}
		ftci.setResult(dto.getInspectionJudge());
		String serialNum = "";
		String snResult = "";
		if (lineList != null && lineList.size() > 0) {
			FqcInspectionL fsl = lineList.stream().max((p1, p2) -> Float.compare(p1.getSampleQty(), p2.getSampleQty()))
					.get();
			// 关联检验单明细表
			FqcInspectionD fqcInspectionD = new FqcInspectionD();
			fqcInspectionD.setLineId(fsl.getLineId());
			List<FqcInspectionD> fqcInspectionDList = fqcInspectionDMapper.select(fqcInspectionD);
			if (fqcInspectionDList != null && fqcInspectionDList.size() > 0) {
				for (FqcInspectionD fid : fqcInspectionDList) {
					if ("".equals(serialNum)) {
						serialNum += fid.getSerialNumber();
					} else {
						serialNum = serialNum + "," + fid.getSerialNumber();
					}
					if ("".equals(snResult)) {
						snResult += fid.getJudgement();
					} else {
						snResult = snResult + "," + fid.getJudgement();
					}
				}
			}

		}

		ftci.setSerialNumber(serialNum);
		ftci.setSnCheckResult(snResult);
		taskExecutor.execute(new Runnable() {
			@Override
			public void run() {
				// 转为JSON String发送
				ObjectMapper omapper = new ObjectMapper();
				String post;
				try {
					ServiceRequest sr = new ServiceRequest();
					sr.setLocale("zh_CN");
					String uri = iCodeService.getCodeValueByMeaning(sr, "HAP.SYSTEM", "WMS_WS_URI");// 获取调用地址
					post = omapper.writeValueAsString(ftci);
					SoapPostUtil.Response re = SoapPostUtil.ticketSrmToWms("getFqcInspectionResult", post, iio, uri);
					ifInvokeOutboundMapper.insertSelective(iio);
					logger.info(SoapPostUtil.getStringFromResponse(re));
				} catch (Exception e) {
					iio.setResponseContent(e.getMessage());
					iio.setResponseCode("E");
					ifInvokeOutboundMapper.insertSelective(iio);
					logger.warn(e.getMessage());
				}
			}
		});
	}

	/**
	 * fqcInspectionResult
	 * 
	 * @description FQC检验结论 mes接口调用
	 * @author tianmin.wang
	 * @date 2020年2月24日
	 * @param dto
	 */
	public void pushToMes(FqcInspectionH dto) {
		FqcInspectionResult fir = new FqcInspectionResult();
		fir.plantCode = dto.getPlantCode();
		fir.orderNo = dto.getSourceOrder();
		fir.orderCheckResult = dto.getInspectionJudge();
		fir.inspectionNum = dto.getInspectionNum();
		List<FqcInspectionD> max = new ArrayList<FqcInspectionD>();
		FqcInspectionL lSearch = new FqcInspectionL();
		lSearch.setHeaderId(dto.getHeaderId());
		List<FqcInspectionL> lineResult = fqcInspectionLMapper.select(lSearch);
		for (FqcInspectionL line : lineResult) {
			FqcInspectionD dataSearch = new FqcInspectionD();
			dataSearch.setLineId(line.getLineId());
			List<FqcInspectionD> dataResult = fqcInspectionDMapper.select(dataSearch);
			if (dataResult != null && dataResult.size() > max.size())
				max = dataResult;
		}
		fir.serialNumber = StringUtils
				.join(max.stream().map(FqcInspectionD::getSerialNumber).collect(Collectors.toList()), ",");
		fir.snCheckResult = StringUtils.join(max.stream().map(FqcInspectionD::getSnStatus).collect(Collectors.toList()),
				",");

		taskExecutor.execute(() -> {
			IfInvokeOutbound iio = new IfInvokeOutbound();
			ObjectMapper obj = new ObjectMapper();
			try {
				String param = obj.writeValueAsString(fir);
				ServiceRequest sr = new ServiceRequest();
				sr.setLocale("zh_CN");
				String uri = iCodeService.getCodeValueByMeaning(sr, "HAP.SYSTEM", "MES_WS_URI");// 获取调用地址
				SoapPostUtil.Response re = SoapPostUtil.ticketSrmToMes(uri, "getInspectionResult", param, iio);
				ifInvokeOutboundMapper.insertSelective(iio);
				logger.info(SoapPostUtil.getStringFromResponse(re));
			} catch (Exception e) {
				iio.setResponseContent(e.getMessage());
				iio.setResponseCode("E");
				ifInvokeOutboundMapper.insertSelective(iio);
				logger.warn(e.getMessage());
			}
		});
	}

	/**
	 * @description 抽样方案变更逻辑
	 * @author tianmin.wang
	 * @date 2020年2月19日
	 * @param request
	 * @param lineList
	 */
	private String changeSamlePlan(HttpServletRequest request, FqcInspectionH head, List<FqcInspectionL> lineList) {
		IRequest ist = RequestHelper.createServiceRequest(request);
		String message = "successs";
		for (FqcInspectionL model : lineList) {
			String blo = "";
			if ("NG".equals(model.getAttributeInspectionRes()))
				blo = changeSamlePlanNg(ist, head, model);
			else
				blo = changeSamlePlanOk(ist, head, model);
			if (!"successs".equals(blo))
				message = blo;
		}
		return message;
	}

	/**
	 * @description
	 * @author tianmin.wang
	 * @date 2020年2月19日
	 * @param request
	 * @param model
	 */
	private String changeSamlePlanOk(IRequest request, FqcInspectionH head, FqcInspectionL model) {
		String message = "success";
		switch (StringUtil.isEmpty(model.getSamplePlan()) ? "N" : model.getSamplePlan()) {
		case "S":// 加严S变更为正常N
			if (!getNMValue("S", model))
				return message;
			List<FqcInspectionL> sList = fqcInspectionLMapper.timeItemSelectN(model);
			if (sList.stream().filter(p -> "OK".equals(p.getAttributeInspectionRes())).count() >= model.getValueM()) {
				if (sList.stream()
						.filter(p -> !"S".equals(StringUtils.isEmpty(p.getSamplePlan()) ? "N" : p.getSamplePlan()))
						.count() > 0)
					return message;
				IqcInspectionTemplateL upda = new IqcInspectionTemplateL();
				upda.setLineId(model.getTemplateLineId());
				upda.setSamplePlan("N");
				iIqcInspectionTemplateLService.updateByPrimaryKeySelective(request, upda);
			}
			break;
		case "N":// 正常N变更为放宽L
			if (!getNMValue("N", model))
				return message;
			List<FqcInspectionL> nList = fqcInspectionLMapper.timeItemSelectN(model);
			if (nList.stream().filter(p -> "OK".equals(p.getAttributeInspectionRes())).count() >= model.getValueM()) {
				if (nList.stream()
						.filter(p -> !"N".equals(StringUtils.isEmpty(p.getSamplePlan()) ? "N" : p.getSamplePlan()))
						.count() > 0)
					return message;
				IqcInspectionTemplateL upda = new IqcInspectionTemplateL();
				upda.setLineId(model.getTemplateLineId());
				upda.setSamplePlan("L");
				iIqcInspectionTemplateLService.updateByPrimaryKeySelective(request, upda);
			}
			break;
		case "L":// 放宽L变更为免检
			if (!getNMValue("L", model))
				return message;
			List<FqcInspectionL> lList = fqcInspectionLMapper.timeItemSelectN(model);
			if (lList.stream().filter(p -> "OK".equals(p.getAttributeInspectionRes())).count() >= model.getValueM()) {
				if (lList.stream()
						.filter(p -> !"L".equals(StringUtils.isEmpty(p.getSamplePlan()) ? "N" : p.getSamplePlan()))
						.count() > 0)
					return message;
				IqcInspectionTemplateL upda = new IqcInspectionTemplateL();
				upda.setLineId(model.getTemplateLineId());
				upda.setSamplePlan("K");
				iIqcInspectionTemplateLService.updateByPrimaryKeySelective(request, upda);
				message = "检验单的模板行的抽样方案抽样方案由\"正常检验\"变更为\"免检\"";
			}
			break;
		}
		return message;
	}

	/**
	 * @description
	 * @author tianmin.wang
	 * @date 2020年2月19日
	 * @param request
	 * @param model
	 */
	private String changeSamlePlanNg(IRequest request, FqcInspectionH head, FqcInspectionL model) {
		String message = "success";
		switch (StringUtil.isEmpty(model.getSamplePlan()) ? "N" : model.getSamplePlan()) {
		case "S"://
			break;
		case "N":// 正常N变更为加严S
			if (!getNMValue("N", model))
				return message;
			List<FqcInspectionL> nList = fqcInspectionLMapper.timeItemSelectN(model);
			if (nList.stream().filter(p -> "NG".equals(p.getAttributeInspectionRes())).count() >= model.getValueM()) {
				if (nList.stream()
						.filter(p -> !"N".equals(StringUtils.isEmpty(p.getSamplePlan()) ? "N" : p.getSamplePlan()))
						.count() > 0)
					return message;
				IqcInspectionTemplateL upda = new IqcInspectionTemplateL();
				upda.setLineId(model.getTemplateLineId());
				upda.setSamplePlan("S");
				iIqcInspectionTemplateLService.updateByPrimaryKeySelective(request, upda);
			}
			break;
		case "L":// 放宽L变更为正常N
			if (!getNMValue("L", model))
				return message;
			List<FqcInspectionL> lList = fqcInspectionLMapper.timeItemSelectN(model);
			if (lList.stream().filter(p -> "NG".equals(p.getAttributeInspectionRes())).count() >= model.getValueM()) {
				if (lList.stream()
						.filter(p -> !"L".equals(StringUtils.isEmpty(p.getSamplePlan()) ? "N" : p.getSamplePlan()))
						.count() > 0)
					return message;
				IqcInspectionTemplateL upda = new IqcInspectionTemplateL();
				upda.setLineId(model.getTemplateLineId());
				upda.setSamplePlan("N");
				iIqcInspectionTemplateLService.updateByPrimaryKeySelective(request, upda);
			}
			break;
		}
		return message;
	}

	private boolean getNMValue(String sourceSampleType, FqcInspectionL model) {

		FqcSampleSwitch search = new FqcSampleSwitch();
		search.setSourceSampleType(sourceSampleType);
		search.setInspectionJudge(model.getAttributeInspectionRes());
		List<FqcSampleSwitch> result = fqcSampleSwitchMapper.select(search);
		if (result == null || result.size() == 0)
			return false;
		model.setValueM(Integer.valueOf(result.get(0).getSwitchRuleValueM()));
		model.setValueN(Integer.valueOf(result.get(0).getSwitchRuleValueN().intValue()));
		return true;
	}

	/**
	 * 
	 * @description 更新日历表的数据
	 * @author tianmin.wang
	 * @date 2019年11月26日
	 */
	public void updateCalendar(FqcInspectionH dto, IRequest requestCtx) {
		if (dto.getForkId() == Float.valueOf(-1f) || StringUtil.isEmpty(dto.getForkType())) {
			return;
		}
		switch (dto.getForkType()) {
		case "A":
			PqcCalendarA pa = new PqcCalendarA();
			pa.setCalendarAId(dto.getForkId());
			pa.setLayeredStatus("R");
			pa.setAttribute1(dto.getInspectionNum());
			dto.setSourceType("14");
			iPqcCalendarAService.updateByPrimaryKeySelective(requestCtx, pa);
			break;
		case "B":
			PqcCalendarB pb = new PqcCalendarB();
			pb.setCalendarBId(dto.getForkId());
			pb.setSafetyStatus("R");
			dto.setSourceType("13");
			pb.setAttribute1(dto.getInspectionNum());
			iPqcCalendarBService.updateByPrimaryKeySelective(requestCtx, pb);
			break;
		case "C":
			PqcCalendarC pc = new PqcCalendarC();
			pc.setCalendarCId(dto.getForkId());
			pc.setPwaStatus("R");
			pc.setAttribute1(dto.getInspectionNum());
			dto.setSourceType("5");
			iPqcCalendarCService.updateByPrimaryKeySelective(requestCtx, pc);
			break;
		}
	}

	/**
	 * updated by tianmin.wang: FQC检验单新建使用的检验单头行模板修改为使用IQC的
	 * 
	 * @throws Exception
	 * 
	 */
	@Override
	public ResponseData addNewInspection(FqcInspectionH dto, IRequest requestCtx, HttpServletRequest request)
			throws Exception {
		// TODO 新增检验单FQC
		FqcInspectionH dao = dto;
		ResponseData responseData = new ResponseData();

		IqcInspectionTemplateH templateHeadSearch = new IqcInspectionTemplateH();
		templateHeadSearch.setItemId(dao.getItemId());
		templateHeadSearch.setSourceType(dao.getSourceType());
		templateHeadSearch.setPlantId(dao.getPlantId());
		templateHeadSearch.setEnableFlag("Y");
		// 检验单头模板查找
		List<IqcInspectionTemplateH> templateHeadList = iqcInspectionTemplateHMapper.myselect(templateHeadSearch);
		if (templateHeadList == null || templateHeadList.size() == 0) {
			// 未找到有效检验单模板，请先维护检验单模板
			responseData.setSuccess(false);
			responseData.setMessage(
					SystemApiMethod.getPromptDescription(request, iPromptService, "error.hqm_fqc_order_create_05"));
			return responseData;
		}
		if (!"4".equals(templateHeadList.get(0).getStatus())) {
			responseData.setSuccess(false);
			responseData.setMessage("检验单模板未发布不能生成检验单");
			return responseData;
		}
		IqcInspectionTemplateL templateLineSearch = new IqcInspectionTemplateL();
		templateLineSearch.setHeaderId(templateHeadList.get(0).getHeaderId());
		// 检验单行模板查找

		List<IqcInspectionTemplateL> templateLineList = iqcInspectionTemplateLMapper.myselect(templateLineSearch);

		String inspectionNum;// 检验单号
		Date date = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		String time = dateFormat.format(date);
		String inspectionNumStart = dao.getPlantCode() + "-" + dao.getInspectionType() + "-" + time.substring(2) + "-";
		// 流水一个检验单号
		inspectionNum = getInspectionNumber(inspectionNumStart);

		// 获取抽样计划类型
		String samplePlan;// 抽样计划类型(抽样方案)
		SwitchScore scoreSearch = new SwitchScore();
		SwitchScore switchScore;
		scoreSearch.setItemId(dao.getItemId());
		scoreSearch.setPlantId(dao.getPlantId());
		List<SwitchScore> switchScoreList = switchScoreMapper.select(scoreSearch);
		if (switchScoreList == null || switchScoreList.size() == 0) {
			// 如果不存在则新增一个抽样计划
//			SwitchScore scoreAdder = new SwitchScore();
//			scoreAdder.setItemId(dao.getItemId());
//			scoreAdder.setPlantId(dao.getPlantId());
//			scoreAdder.setSamplePlanTypeNow("N");
//			scoreAdder.setSamplePlanTypeNext("N");
//			scoreAdder.setSwitchScore(0f);
//			scoreAdder.setNonnconformingLot(0f);
//			scoreAdder.setConsecutiveConformingLot(0f);
//			scoreAdder.setChangeFlag("Y");
//			iSwitchScoreService.self().insertSelective(requestCtx, scoreAdder);
//			samplePlan = scoreAdder.getSamplePlanTypeNow();
			// 该SKU不存在其对应的平台，无法生成抽样数量
			throw new RuntimeException(
					SystemApiMethod.getPromptDescription(request, iPromptService, "error.hqm_fqc_inspection_sample01"));
		} else {
			switchScore = switchScoreList.get(0);
			samplePlan = switchScoreList.get(0).getSamplePlanTypeNow();
		}

		List<FqcInspectionL> inspectionLAdderList = new ArrayList<FqcInspectionL>();// 所有检验项模型

		String isUrgency = "N";// 是否加急
		String timeLimit = iCodeService.getCodeValueByMeaning(requestCtx, "HQM_INSPECTION_TIME_LIMIT",
				"HQM_INSPECTION_TIME_LIMIT");
		if (StringUtils.isEmpty(timeLimit))
			throw new RuntimeException("未维护检验时效的编码");
		Float timeLimitFloat = Float.valueOf(timeLimit);
		// 检验项数据构建
		Float sampleSize = 1f;// 头的抽样数量
		for (IqcInspectionTemplateL inspectionTemplateL : templateLineList) {
			// 判断是否加急
			if (inspectionTemplateL.getTimeLimit() != null
					&& inspectionTemplateL.getTimeLimit().compareTo(timeLimitFloat) > 0)
				isUrgency = "Y";
			FqcInspectionL inspectionL = new FqcInspectionL();// 新的检验项
			// 找抽样方式
			SampleManage smSearch = new SampleManage();
			smSearch.setSampleWayId(Float.valueOf(inspectionTemplateL.getSampleWayId()));
			SampleManage sampleManageResult = sampleManageMapper.selectByPrimaryKey(smSearch);
			if (sampleManageResult.getSampleProcedureType() == null) {
				throw new RuntimeException(
						SystemApiMethod.getPromptDescription(request, iPromptService, "error.samplemanage_is_null"));
			}
			if (dao.getProduceQty() == 1) {
				// 生产数量为1时
				inspectionL.setSampleQty(1f);// 抽样数量
			} else {
				// 生产数量大于1时

				switch (sampleManageResult.getSampleType()) {
				case "0":
					inspectionL.setSampleQty(Float.valueOf(sampleManageResult.getParameter()));
					if (sampleSize < inspectionL.getSampleQty()) {
						sampleSize = inspectionL.getSampleQty();
					}
					break;
				case "2":
					inspectionL.setSampleQty(dto.getProduceQty());
					if (sampleSize < inspectionL.getSampleQty()) {
						sampleSize = inspectionL.getSampleQty();
					}
					break;
				case "1":
					SampleSizeCodeLetter searchCodeLetter = new SampleSizeCodeLetter();
					searchCodeLetter.setInspectionLevels(sampleManageResult.getInspectionLevels());
					searchCodeLetter.setLotSizeFrom(dao.getProduceQty());
					searchCodeLetter.setLotSizeTo(dao.getProduceQty());
					// 查找对应的样本量字码
					List<SampleSizeCodeLetter> sizeCodeResultList = new ArrayList<SampleSizeCodeLetter>();
					List<SampleScheme> sampleSchemeResultList = new ArrayList<SampleScheme>();
					if (sampleManageResult.getSampleProcedureType() == null) {
						throw new RuntimeException(SystemApiMethod.getPromptDescription(request, iPromptService,
								"error.sampleproceduretype_is_null"));
					}
					if (inspectionTemplateL.getSampleProcedureType().equals("0")) {
						// 抽样标准为0时
						sizeCodeResultList = sampleSizeCodeLetterMapper.selectCodeSizeLetter(searchCodeLetter);
						SampleScheme sampleSchemeSearch = new SampleScheme();
						sampleSchemeSearch.setSampleSizeCodeLetter(sizeCodeResultList.get(0).getSizeCodeLetter());
						sampleSchemeSearch.setSamplePlanType(samplePlan);
						sampleSchemeSearch.setAcceptanceQualityLimit(sampleManageResult.getAcceptanceQualityLimit());
						// 查找确定检验项的抽样数量、AC、RE值
						sampleSchemeResultList = sampleSchemeMapper.select(sampleSchemeSearch);
						if (sampleSchemeResultList == null || sampleSchemeResultList.size() == 0) {
							throw new RuntimeException(SystemApiMethod.getPromptDescription(request, iPromptService,
									"error.sample_scheme_is_null"));
						}
						if (sampleSchemeResultList.get(0).getSampleSize() > dao.getProduceQty()
								|| sampleSchemeResultList.get(0).getSampleSize().intValue() == (-1)) {
							// 若计算出的抽样数量大于生产数量 或者为-1的话 则抽样数量取值为生产数量
							sampleSchemeResultList.get(0).setSampleSize(dao.getProduceQty());
						}
						if (sampleSize < sampleSchemeResultList.get(0).getSampleSize()) {
							sampleSize = sampleSchemeResultList.get(0).getSampleSize();
						}
						inspectionL.setSizeCodeLetter(sizeCodeResultList.get(0).getSizeCodeLetter());// 样本量字码
						inspectionL.setSampleQty(sampleSchemeResultList.get(0).getSampleSize());// 抽样数量
						inspectionL.setAc(String.valueOf(sampleSchemeResultList.get(0).getAcValue()));// AC值
						inspectionL.setRe(String.valueOf(sampleSchemeResultList.get(0).getReValue()));// RE值
					} else if (inspectionTemplateL.getSampleProcedureType().equals("1")) {
						// 抽样标准为1时
						SampleScheme sampleSchemeSearch = new SampleScheme();
						sampleSchemeSearch.setAcceptanceQualityLimit(sampleManageResult.getAcceptanceQualityLimit());
						sampleSchemeSearch.setAttribute1(String.valueOf(dao.getProduceQty()));
						sampleSchemeSearch.setAttribute2(String.valueOf(dao.getProduceQty()));
						sampleSchemeResultList = sampleSchemeMapper.selectSampleSize(sampleSchemeSearch);
						if (sampleSchemeResultList == null || sampleSchemeResultList.size() == 0) {
							throw new RuntimeException(SystemApiMethod.getPromptDescription(request, iPromptService,
									"error.sample_scheme_is_null"));
						}
						if (sampleSchemeResultList.get(0).getSampleSize() > dao.getProduceQty()
								|| sampleSchemeResultList.get(0).getSampleSize() == (-1)) {
							// 若计算出的抽样数量大于生产数量 或者为-1的话 则抽样数量取值为生产数量
							sampleSchemeResultList.get(0).setSampleSize(dao.getProduceQty());
						}
						if (sampleSize < sampleSchemeResultList.get(0).getSampleSize()) {
							sampleSize = sampleSchemeResultList.get(0).getSampleSize();
						}
						inspectionL.setSampleQty(sampleSchemeResultList.get(0).getSampleSize());// 抽样数量
					} else if (inspectionTemplateL.getSampleProcedureType().equals("3")) {
						/**
						 * 抽样标准为3的逻辑 add by wtm 20191220 switchScore
						 */
						// 查询 检验项目
						InspectionAttribute iaSearch = new InspectionAttribute();
						iaSearch.setAttributeId(inspectionTemplateL.getAttributeId());
						List<InspectionAttribute> iaResult = inspectionAttributeMapper.select(iaSearch);
						PlatformProgram ppSearch = new PlatformProgram();
						ppSearch.setPlantId(dao.getPlantId());
						ppSearch.setPlatformType(switchScore.getPlatform());
						ppSearch.setProgramType(iaResult.get(0).getAttribute1());
						List<PlatformProgram> ppResult = platformProgramMapper.select(ppSearch);
						if (ppResult == null || ppResult.size() == 0) {
							throw new RuntimeException(SystemApiMethod.getPromptDescription(request, iPromptService,
									"error.platform_program_is_null"));
						}
						String samplePlanType = "";
						switch (StringUtil.isEmpty(inspectionTemplateL.getSamplePlan()) ? "N"
								: inspectionTemplateL.getSamplePlan()) {// 获取抽样方案 wtm-20200219
						case "K":
							switch (ppResult.get(0).getSampleType()) {
							case "6":
							case "5":
								samplePlanType = "K";
								break;
							case "4":
							case "3":
								samplePlanType = "L";
								break;
							case "2":
							case "1":
								samplePlanType = "N";
								break;
							}
							break;
						case "N":
							samplePlanType = "N";
							break;
						case "S":
							switch (ppResult.get(0).getSampleType()) {
							case "2":
							case "4":
							case "6":
								samplePlanType = "S";
								break;
							case "1":
							case "3":
							case "5":
								samplePlanType = "N";
								break;
							}
							break;
						case "L":
							switch (ppResult.get(0).getSampleType()) {
							case "6":
							case "5":
							case "4":
							case "3":
								samplePlanType = "L";
								break;
							case "2":
							case "1":
								samplePlanType = "N";
								break;
							}
							break;
						}
						SampleScheme hssSearch = new SampleScheme();
						hssSearch.setAttribute3(switchScore.getPlatform());// 平台
						hssSearch.setSamplePlanType(samplePlanType);
						List<SampleScheme> hssResult = sampleSchemeMapper.select(hssSearch);
						if (hssResult == null || hssResult.size() == 0) {
							throw new RuntimeException(SystemApiMethod.getPromptDescription(request, iPromptService,
									"error.sample_scheme_is_null"));
						}
						Double sampleQty = Math
								.ceil(Float.valueOf(hssResult.get(0).getAttribute4()) * dao.getProduceQty());
						if (sampleSize < sampleQty) {
							sampleSize = sampleQty.floatValue();
						}
						inspectionL.setSampleQty(sampleQty.floatValue());// 抽样数量
					} else {
						// 抽样标准为2时
						inspectionL.setSampleQty(1f);// 抽样数量
					}
					break;
				}

			}
			inspectionL.setSamplePlan(inspectionTemplateL.getSamplePlan());
			inspectionL.setTemplateLineId(inspectionTemplateL.getLineId());
			inspectionL.setInspectionMethod(inspectionTemplateL.getInspectionMethod());
			inspectionL.setPrecision(inspectionTemplateL.getPrecision());
			inspectionL.setAttributeId(inspectionTemplateL.getAttributeId());
			inspectionL.setAttributeType(inspectionTemplateL.getAttributeType());
			inspectionL.setOrderCode(inspectionTemplateL.getOrderCode());
			inspectionL.setInspectionAttribute(inspectionTemplateL.getInspectionAttribute());
			inspectionL.setInspectionTool(inspectionTemplateL.getInspectionTool());
			inspectionL.setSampleProcedureType(inspectionTemplateL.getSampleProcedureType());
			inspectionL.setInspectionLevels(inspectionTemplateL.getInspectionLevels());
			inspectionL.setQualityCharacterGrade(inspectionTemplateL.getQualityCharacterGrade());
			inspectionL.setStandardType(inspectionTemplateL.getStandardType());
			inspectionL.setStandradFrom(inspectionTemplateL.getStandradFrom());
			inspectionL.setStandradTo(inspectionTemplateL.getStandradTo());
			inspectionL.setStandradUom(inspectionTemplateL.getStandradUom());
			inspectionL.setTextStandrad(inspectionTemplateL.getTextStandrad());
			// inspectionL.setDataFrom(inspectionTemplateL.getFillInType());
			inspectionL.setAcceptanceQualityLimit(inspectionTemplateL.getAcceptanceQualityLimit());
			inspectionL.setSampleWayId(Float.valueOf(inspectionTemplateL.getSampleWayId()));
			inspectionL.setSampleType(Float.parseFloat(sampleManageResult.getSampleType()));
			if (sampleManageResult.getParameter() != null && !StringUtils.isBlank(sampleManageResult.getParameter())) {
				inspectionL.setParameter(Float.parseFloat(sampleManageResult.getParameter()));
			}
			inspectionL.setFillInType(inspectionTemplateL.getFillInType());
			inspectionLAdderList.add(inspectionL);
		}
		// 更新日历表
		dto.setInspectionNum(inspectionNum);
		updateCalendar(dto, requestCtx);

		FqcInspectionH fqcInspectionHAdder = new FqcInspectionH();
		fqcInspectionHAdder.setInspectorId(dao.getInspectorId());
		fqcInspectionHAdder.setInspectionRes("P");
		fqcInspectionHAdder.setInspectionJudge("P");
		fqcInspectionHAdder.setApprovalResult("P");
		fqcInspectionHAdder.setSamplePlan(samplePlan);
		fqcInspectionHAdder.setInspectionNum(inspectionNum);
		fqcInspectionHAdder.setInspectionType(dao.getInspectionType());
		fqcInspectionHAdder.setPlantId(dao.getPlantId());
		fqcInspectionHAdder.setProdLineId(dao.getProdLineId());
		fqcInspectionHAdder.setSourceType(dao.getSourceType());
		fqcInspectionHAdder.setEmergencyFlag(isUrgency);
		fqcInspectionHAdder.setSourceOrder(dao.getSourceOrder());
		fqcInspectionHAdder.setVersionNum(templateHeadList.get(0).getVersionNum());
		fqcInspectionHAdder.setTemplateHeadId(templateHeadList.get(0).getHeaderId());
		fqcInspectionHAdder.setItemId(dao.getItemId());
		fqcInspectionHAdder.setProduceQty(dao.getProduceQty());
		fqcInspectionHAdder.setProductionBatch(dao.getProductionBatch());
		fqcInspectionHAdder.setInspectionStatus("2");
		fqcInspectionHAdder.setSampleQty(sampleSize);
		dto.setSampleQty(sampleSize);
		fqcInspectionHAdder.setProduceDate(dao.getProduceDate());
		fqcInspectionHAdder.setInspectionFrom(dao.getInspectionFrom());
		// fqcInspectionHAdder.setInspectionDate(new Date());
		fqcInspectionHAdder.setPlanTime(getPlanTime(templateHeadList.get(0).getTimeLimit()));
		fqcInspectionHAdder.setRemark(dao.getRemark());
		// fqcInspectionHAdder.setScoreFlag(dao.getSourceType().equals("F") ? "Y" :
		// "N");

		/**
		 * added by wtm 20191218 新增了一个维护字段 itemEdition
		 */
		fqcInspectionHAdder.setItemEdition(dao.getItemEdition());
		// 生成检验单头
		fqcInspectionHMapper.insertSelective(fqcInspectionHAdder);
		for (FqcInspectionL ldto : inspectionLAdderList) {
			// 生成检验单行
			ldto.setHeaderId(fqcInspectionHAdder.getHeaderId());
			fqcInspectionLMapper.insertSelective(ldto);
			iFqcInspectionDService.batchCreateByInspectionL(requestCtx, ldto);
		}

		/**
		 * wtm-20200218 成功创建检验性质为VTP试验的检验单后(来源类型 为 15),
		 * 根据检验单的工厂+物料，查找hqm_program_sku_rel表中是否存在数据，不存在则新增一条HQM_FQC_task
		 * 同时根据此条检验通知生成在线新品检验的检验单(执行FQC检验通知中生成报告的逻辑)
		 */
		if ("15".equals(fqcInspectionHAdder.getSourceType())) {
			programSkuRel(request, fqcInspectionHAdder);
		}
		List<FqcInspectionH> rows = new ArrayList<FqcInspectionH>();
		rows.add(fqcInspectionHAdder);
		responseData.setRows(rows);
		return responseData;
	}

	/**
	 * @description fqc创建后 sku新品与编号关系 相关逻辑
	 * @author tianmin.wang
	 * @date 2020年2月18日
	 * @param request
	 * @param fqcInspectionHAdder
	 * @throws Exception
	 */
	private void programSkuRel(HttpServletRequest request, FqcInspectionH fqc) throws Exception {
		ProgramSkuRel skuRelSearch = new ProgramSkuRel();
		skuRelSearch.setItemId(fqc.getItemId());
		skuRelSearch.setPlantId(fqc.getPlantId());
		List<ProgramSkuRel> result = programSkuRelMapper.select(skuRelSearch);
		if (result == null || result.size() == 0) {
			FqcTask fqcTask = new FqcTask();
			fqcTask.setPlantId(fqc.getPlantId());
			fqcTask.setSourceOrder(fqc.getSourceOrder());
			fqcTask.setProdLineId(fqc.getProdLineId());
			fqcTask.setSourceType("20");
			fqcTask.setItemId(fqc.getItemId());
			fqcTask.setItemVersion(fqc.getItemEdition());
			fqcTask.setProductQty(fqc.getProduceQty());
			fqcTask.setErrorMsg(SystemApiMethod.getPromptDescription(request, iPromptService, "error.fqc_create001"));
			iFqcTaskService.insertSelective(RequestHelper.createServiceRequest(request), fqcTask);
			List<FqcTask> dto = new ArrayList<FqcTask>();
			dto.add(fqcTask);
			// FQC检验通知生成报告
			iFqcTaskService.createFqc(dto, request);
		} else {
			if (!"Y".equals(result.get(0).getIsPass())) {
				FqcTask fqcTask = new FqcTask();
				fqcTask.setPlantId(fqc.getPlantId());
				fqcTask.setSourceOrder(fqc.getSourceOrder());
				fqcTask.setProdLineId(fqc.getProdLineId());
				fqcTask.setSourceType("20");
				fqcTask.setItemId(fqc.getItemId());
				fqcTask.setItemVersion(fqc.getItemEdition());
				fqcTask.setProductQty(fqc.getProduceQty());
				iFqcTaskService.insertSelective(RequestHelper.createServiceRequest(request), fqcTask);
				List<FqcTask> dto = new ArrayList<FqcTask>();
				dto.add(fqcTask);
				// FQC检验通知生成报告
				iFqcTaskService.createFqc(dto, request);
			}
		}

	}

	public Date getPlanTime(Float timeLimit) {
		Date currentDate = new Date();
		if (timeLimit == null)
			return null;
		// 计算计划完成时间
		Date returnDate = new Date(currentDate.getTime() + (timeLimit.intValue()) * 60 * 60 * 1000);
		return returnDate;
	}

	public Float getSupplierSitId(Float suplierId) {
		Suppliers dto = new Suppliers();
		dto.setSupplierId(suplierId);
		Suppliers li = suppliersMapper.lovselect(dto).get(0);
		return li.getSupplersSiteId();
	}

	/**
	 * 获取一个检验单号的流水
	 * 
	 * @param inspectionNumStart
	 * @return
	 */
	public String getInspectionNumber(String inspectionNumStart) {
		Integer count = 1;
		FqcInspectionH sr = new FqcInspectionH();
		sr.setInspectionNum(inspectionNumStart);
		List<FqcInspectionH> list = new ArrayList<FqcInspectionH>();
		list = fqcInspectionHMapper.selectMaxNumber(sr);
		if (list != null && list.size() > 0) {
			String inspectionNum = list.get(0).getInspectionNum();
			String number = inspectionNum.substring(inspectionNum.length() - 5);// 流水
			count = Integer.valueOf(number) + 1;
		}
		String str = String.format("%05d", count);
		return inspectionNumStart + str;// 最终检验单号
	}

	@Override
	public List<FqcInspectionH> qmsQuery(IRequest requestContext, FqcInspectionH dto, int page, int pageSize) {
		PageHelper.startPage(page, pageSize);
		return fqcInspectionHMapper.qmsQuery(dto);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.hand.hqm.hqm_fqc_inspection_h.service.IFqcInspectionHService#addSample(
	 * com.hand.hqm.hqm_fqc_inspection_h.dto.FqcInspectionH,
	 * javax.servlet.http.HttpServletRequest)
	 */
	@Override
	public void addSample(FqcInspectionH dto, HttpServletRequest request) {
		IRequest requestContext = RequestHelper.createServiceRequest(request);
		FqcInspectionH fqch = fqcInspectionHMapper.selectByPrimaryKey(dto);
		fqch.setSampleQty(fqch.getSampleQty() + 1);
		fqch.setInspectionJudge(null);
		self().updateByPrimaryKey(requestContext, fqch);
		FqcInspectionL lse = new FqcInspectionL();
		lse.setHeaderId(fqch.getHeaderId());
		List<FqcInspectionL> lres = fqcInspectionLMapper.select(lse);
		lres.stream().forEach(fqcl -> {
			fqcl.setSampleQty(fqcl.getSampleQty() + 1);
			fqcl.setAttributeInspectionRes(null);
			iFqcInspectionLService.updateByPrimaryKey(requestContext, fqcl);
			FqcInspectionD dinsert = new FqcInspectionD();
			dinsert.setLineId(fqcl.getLineId());
			dinsert.setOrderNum(String.valueOf(fqcl.getSampleQty().intValue() - 1));
			iFqcInspectionDService.insertSelective(requestContext, dinsert);
		});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hand.hqm.hqm_fqc_inspection_h.service.IFqcInspectionHService#
	 * getLimitCount(com.hand.hqm.hqm_fqc_inspection_h.dto.FqcInspectionH)
	 */
	@Override
	public List<FqcInspectionH> getLimitCount(FqcInspectionH dto) {
		// TODO Auto-generated method stub
		return fqcInspectionHMapper.getLimitCount(dto);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hand.hqm.hqm_fqc_inspection_h.service.IFqcInspectionHService#
	 * addNewInspection(com.hand.hqm.hqm_qc_task.dto.FqcTask,
	 * com.hand.hqm.hqm_fqc_inspection_h.dto.FqcInspectionH,
	 * javax.servlet.http.HttpServletRequest)
	 */
	@Override
	public void addNewInspection(FqcTask fqctask, FqcInspectionH dto, HttpServletRequest request) {
		// TODO 新增检验单FQC
		IRequest requestCtx;
		if (request == null) {
			ServiceRequest sr = new ServiceRequest();
			sr.setLocale("zn_CN");
			sr.setUserId(-1l);
			requestCtx = sr;
		} else {
			requestCtx = RequestHelper.createServiceRequest(request);
		}
		FqcInspectionH dao = dto;
		IqcInspectionTemplateH templateHeadSearch = new IqcInspectionTemplateH();
		templateHeadSearch.setItemId(dao.getItemId());
		templateHeadSearch.setSourceType(dao.getSourceType());
		templateHeadSearch.setPlantId(dao.getPlantId());
		templateHeadSearch.setEnableFlag("Y");
		// 检验单头模板查找
		List<IqcInspectionTemplateH> templateHeadList = iqcInspectionTemplateHMapper.myselect(templateHeadSearch);
		if (templateHeadList == null || templateHeadList.size() == 0) {
			// 未找到有效检验单模板，请先维护检验单模板
			throw new RuntimeException(
					SystemApiMethod.getPromptDescription(request, iPromptService, "error.hqm_fqc_order_create_05"));
		}
		if (!"4".equals(templateHeadList.get(0).getStatus())) {
			throw new RuntimeException("检验单模板未发布不能生成检验单");
		}
		IqcInspectionTemplateL templateLineSearch = new IqcInspectionTemplateL();
		templateLineSearch.setHeaderId(templateHeadList.get(0).getHeaderId());
		// 检验单行模板查找
		String isUrgency = "N";// 是否加急
		String timeLimit = iCodeService.getCodeValueByMeaning(requestCtx, "HQM_INSPECTION_TIME_LIMIT",
				"HQM_INSPECTION_TIME_LIMIT");
		if (StringUtils.isEmpty(timeLimit))
			throw new RuntimeException("未维护检验时效的编码");
		Float timeLimitFloat = Float.valueOf(0);
		List<IqcInspectionTemplateL> templateLineList = iqcInspectionTemplateLMapper.myselect(templateLineSearch);
		if (templateLineList.stream().filter(p -> {
			return p.getTimeLimit() != null && p.getTimeLimit().compareTo(timeLimitFloat) > 0;
		}).count() > 0)
			isUrgency = "Y";
		String inspectionNum;// 检验单号
		Date date = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		String time = dateFormat.format(date);
		String inspectionNumStart = dao.getPlantCode() + "-" + dao.getInspectionType() + "-" + time.substring(2) + "-";
		// 流水一个检验单号
		inspectionNum = getInspectionNumber(inspectionNumStart);

		// 获取抽样计划类型
		String samplePlan;// 抽样计划类型(抽样方案)
		SwitchScore scoreSearch = new SwitchScore();
		SwitchScore switchScore;
		scoreSearch.setItemId(dao.getItemId());
		scoreSearch.setPlantId(dao.getPlantId());
		List<SwitchScore> switchScoreList = switchScoreMapper.select(scoreSearch);
		if (switchScoreList == null || switchScoreList.size() == 0) {
			// 如果不存在则新增一个抽样计划
//					SwitchScore scoreAdder = new SwitchScore();
//					scoreAdder.setItemId(dao.getItemId());
//					scoreAdder.setPlantId(dao.getPlantId());
//					scoreAdder.setSamplePlanTypeNow("N");
//					scoreAdder.setSamplePlanTypeNext("N");
//					scoreAdder.setSwitchScore(0f);
//					scoreAdder.setNonnconformingLot(0f);
//					scoreAdder.setConsecutiveConformingLot(0f);
//					scoreAdder.setChangeFlag("Y");
//					iSwitchScoreService.self().insertSelective(requestCtx, scoreAdder);
//					samplePlan = scoreAdder.getSamplePlanTypeNow();
			// 该SKU不存在其对应的平台，无法生成抽样数量
			throw new RuntimeException(
					SystemApiMethod.getPromptDescription(request, iPromptService, "error.hqm_fqc_inspection_sample01"));
		} else {
			switchScore = switchScoreList.get(0);
			samplePlan = switchScoreList.get(0).getSamplePlanTypeNow();
		}

		List<FqcInspectionL> inspectionLAdderList = new ArrayList<FqcInspectionL>();// 所有检验项模型

		// 检验项数据构建
		Float sampleSize = 1f;// 头的抽样数量
		if ("3".equals(fqctask.getTaskFrom()) || "4".equals(fqctask.getTaskFrom())) {
			sampleSize = inspectionLGenerateOr(fqctask, templateLineList, inspectionLAdderList, dao, dto, request,
					samplePlan, switchScore);
		} else {
			sampleSize = inspectionLGenerate(templateLineList, inspectionLAdderList, dao, dto, request, samplePlan,
					switchScore);
		}

		// 更新日历表
		dto.setInspectionNum(inspectionNum);
		updateCalendar(dto, requestCtx);

		FqcInspectionH fqcInspectionHAdder = new FqcInspectionH();
		fqcInspectionHAdder.setInspectorId(dao.getInspectorId());
		fqcInspectionHAdder.setInspectionRes("P");
		fqcInspectionHAdder.setInspectionJudge("P");
		fqcInspectionHAdder.setApprovalResult("P");
		fqcInspectionHAdder.setSamplePlan(samplePlan);
		fqcInspectionHAdder.setInspectionNum(inspectionNum);
		fqcInspectionHAdder.setInspectionType(dao.getInspectionType());
		fqcInspectionHAdder.setPlantId(dao.getPlantId());
		fqcInspectionHAdder.setProdLineId(dao.getProdLineId());
		fqcInspectionHAdder.setSourceType(dao.getSourceType());
		fqcInspectionHAdder.setEmergencyFlag(isUrgency);
		fqcInspectionHAdder.setSourceOrder(dao.getSourceOrder());
		fqcInspectionHAdder.setVersionNum(templateHeadList.get(0).getVersionNum());
		fqcInspectionHAdder.setTemplateHeadId(templateHeadList.get(0).getHeaderId());
		fqcInspectionHAdder.setItemId(dao.getItemId());
		fqcInspectionHAdder.setProduceQty(dao.getProduceQty());
		fqcInspectionHAdder.setProductionBatch(dao.getProductionBatch());
		fqcInspectionHAdder.setInspectionStatus("2");
		fqcInspectionHAdder.setSampleQty(sampleSize);
		fqcInspectionHAdder.setProduceDate(dao.getProduceDate());
		fqcInspectionHAdder.setInspectionFrom(dao.getInspectionFrom());
		// fqcInspectionHAdder.setInspectionDate(new Date());
		fqcInspectionHAdder.setPlanTime(getPlanTime(templateHeadList.get(0).getTimeLimit()));
		fqcInspectionHAdder.setRemark(dao.getRemark());
		dao.setSampleQty(sampleSize);
		// fqcInspectionHAdder.setScoreFlag(dao.getSourceType().equals("F") ? "Y" :
		// "N");

		/**
		 * added by wtm 20191218 新增了一个维护字段 itemEdition
		 */
		fqcInspectionHAdder.setItemEdition(dao.getItemEdition());
		// 生成检验单头
		fqcInspectionHMapper.insertSelective(fqcInspectionHAdder);
		for (FqcInspectionL ldto : inspectionLAdderList) {
			// 生成检验单行
			ldto.setHeaderId(fqcInspectionHAdder.getHeaderId());
			fqcInspectionLMapper.insertSelective(ldto);
			iFqcInspectionDService.batchCreateByInspectionL(requestCtx, ldto);
		}
		fqctask.setInspectionNum(fqcInspectionHAdder.getInspectionNum());
	}

	Float inspectionLGenerateOr(FqcTask fqctask, List<IqcInspectionTemplateL> templateLineList,
			List<FqcInspectionL> inspectionLAdderList, FqcInspectionH dao, FqcInspectionH dto,
			HttpServletRequest request, String samplePlan, SwitchScore switchScore) {
		Float sampleSize = 1f;
		FqcInspectionH inshSearch = new FqcInspectionH();
		inshSearch.setInspectionNum(fqctask.getRelOrder());
		List<FqcInspectionH> relOrderSelect = mapper.select(inshSearch);
		if (relOrderSelect != null && relOrderSelect.size() > 0) {
			FqcInspectionL inslSearch = new FqcInspectionL();
			inslSearch.setHeaderId(relOrderSelect.get(0).getHeaderId());
			inslSearch.setAttributeInspectionRes("NG");
			List<FqcInspectionL> selects = fqcInspectionLMapper.select(inslSearch);
			for (FqcInspectionL inspectionL : selects) {
				switch (fqctask.getRecheckSampleWay() == null ? "" : fqctask.getRecheckSampleWay()) {
				case "N":// 按照现有逻辑生成检验项的抽样数sample_size
					break;
				case "S":// 按照物料的抽样方案为S，再生成sample_size
					// 找抽样方式
					SampleManage smSearch = new SampleManage();
					smSearch.setSampleWayId(Float.valueOf(inspectionL.getSampleWayId()));
					SampleManage sampleManageResult = sampleManageMapper.selectByPrimaryKey(smSearch);
					if (sampleManageResult.getSampleProcedureType() == null) {
						throw new RuntimeException(SystemApiMethod.getPromptDescription(request, iPromptService,
								"error.samplemanage_is_null"));
					}
					if (dao.getProduceQty() == 1) {
						// 生产数量为1时
						inspectionL.setSampleQty(1f);// 抽样数量
					} else {
						// 生产数量大于1时
						switch (sampleManageResult.getSampleType()) {
						case "0":
							inspectionL.setSampleQty(Float.valueOf(sampleManageResult.getParameter()));
							if (sampleSize < inspectionL.getSampleQty()) {
								sampleSize = inspectionL.getSampleQty();
							}
							break;
						case "2":
							inspectionL.setSampleQty(dto.getProduceQty());
							if (sampleSize < inspectionL.getSampleQty()) {
								sampleSize = inspectionL.getSampleQty();
							}
							break;
						case "1":
							SampleSizeCodeLetter searchCodeLetter = new SampleSizeCodeLetter();
							searchCodeLetter.setInspectionLevels(sampleManageResult.getInspectionLevels());
							searchCodeLetter.setLotSizeFrom(dao.getProduceQty());
							searchCodeLetter.setLotSizeTo(dao.getProduceQty());
							// 查找对应的样本量字码
							List<SampleSizeCodeLetter> sizeCodeResultList = new ArrayList<SampleSizeCodeLetter>();
							List<SampleScheme> sampleSchemeResultList = new ArrayList<SampleScheme>();
							if (sampleManageResult.getSampleProcedureType() == null) {
								throw new RuntimeException(SystemApiMethod.getPromptDescription(request, iPromptService,
										"error.sampleproceduretype_is_null"));
							}
							if (inspectionL.getSampleProcedureType().equals("0")) {
								// 抽样标准为0时
								sizeCodeResultList = sampleSizeCodeLetterMapper.selectCodeSizeLetter(searchCodeLetter);
								SampleScheme sampleSchemeSearch = new SampleScheme();
								sampleSchemeSearch
										.setSampleSizeCodeLetter(sizeCodeResultList.get(0).getSizeCodeLetter());
								sampleSchemeSearch.setSamplePlanType(samplePlan);
								sampleSchemeSearch
										.setAcceptanceQualityLimit(sampleManageResult.getAcceptanceQualityLimit());
								// 查找确定检验项的抽样数量、AC、RE值
								sampleSchemeResultList = sampleSchemeMapper.select(sampleSchemeSearch);
								if (sampleSchemeResultList == null || sampleSchemeResultList.size() == 0) {
									throw new RuntimeException(SystemApiMethod.getPromptDescription(request,
											iPromptService, "error.sample_scheme_is_null"));
								}
								if (sampleSchemeResultList.get(0).getSampleSize() > dao.getProduceQty()
										|| sampleSchemeResultList.get(0).getSampleSize().intValue() == (-1)) {
									// 若计算出的抽样数量大于生产数量 或者为-1的话 则抽样数量取值为生产数量
									sampleSchemeResultList.get(0).setSampleSize(dao.getProduceQty());
								}
								if (sampleSize < sampleSchemeResultList.get(0).getSampleSize()) {
									sampleSize = sampleSchemeResultList.get(0).getSampleSize();
								}
								inspectionL.setSizeCodeLetter(sizeCodeResultList.get(0).getSizeCodeLetter());// 样本量字码
								inspectionL.setSampleQty(sampleSchemeResultList.get(0).getSampleSize());// 抽样数量
								inspectionL.setAc(String.valueOf(sampleSchemeResultList.get(0).getAcValue()));// AC值
								inspectionL.setRe(String.valueOf(sampleSchemeResultList.get(0).getReValue()));// RE值
							} else if (inspectionL.getSampleProcedureType().equals("1")) {
								// 抽样标准为1时
								SampleScheme sampleSchemeSearch = new SampleScheme();
								sampleSchemeSearch
										.setAcceptanceQualityLimit(sampleManageResult.getAcceptanceQualityLimit());
								sampleSchemeSearch.setAttribute1(String.valueOf(dao.getProduceQty()));
								sampleSchemeSearch.setAttribute2(String.valueOf(dao.getProduceQty()));
								sampleSchemeResultList = sampleSchemeMapper.selectSampleSize(sampleSchemeSearch);
								if (sampleSchemeResultList == null || sampleSchemeResultList.size() == 0) {
									throw new RuntimeException(SystemApiMethod.getPromptDescription(request,
											iPromptService, "error.sample_scheme_is_null"));
								}
								if (sampleSchemeResultList.get(0).getSampleSize() > dao.getProduceQty()
										|| sampleSchemeResultList.get(0).getSampleSize() == (-1)) {
									// 若计算出的抽样数量大于生产数量 或者为-1的话 则抽样数量取值为生产数量
									sampleSchemeResultList.get(0).setSampleSize(dao.getProduceQty());
								}
								if (sampleSize < sampleSchemeResultList.get(0).getSampleSize()) {
									sampleSize = sampleSchemeResultList.get(0).getSampleSize();
								}
								inspectionL.setSampleQty(sampleSchemeResultList.get(0).getSampleSize());// 抽样数量
							} else if (inspectionL.getSampleProcedureType().equals("3")) {
								/**
								 * 抽样标准为3的逻辑 add by wtm 20191220 switchScore
								 */
								// 查询 检验项目
								InspectionAttribute iaSearch = new InspectionAttribute();
								iaSearch.setAttributeId(inspectionL.getAttributeId());
								List<InspectionAttribute> iaResult = inspectionAttributeMapper.select(iaSearch);
								PlatformProgram ppSearch = new PlatformProgram();
								ppSearch.setPlantId(dao.getPlantId());
								ppSearch.setPlatformType(switchScore.getPlatform());
								ppSearch.setProgramType(iaResult.get(0).getAttribute1());
								List<PlatformProgram> ppResult = platformProgramMapper.select(ppSearch);
								if (ppResult == null || ppResult.size() == 0) {
									throw new RuntimeException(SystemApiMethod.getPromptDescription(request,
											iPromptService, "error.platform_program_is_null"));
								}
								String samplePlanType = "";
								switch (inspectionL.getSamplePlan()) {// 获取抽样方案
								case "K":
									switch (ppResult.get(0).getSampleType()) {
									case "6":
									case "5":
										samplePlanType = "K";
										break;
									case "4":
									case "3":
										samplePlanType = "L";
										break;
									case "2":
									case "1":
										samplePlanType = "N";
										break;
									}
									break;
								case "N":
									samplePlanType = "N";
									break;
								case "S":
									switch (ppResult.get(0).getSampleType()) {
									case "6":
									case "4":
									case "2":
										samplePlanType = "S";
										break;
									case "5":
									case "3":
									case "1":
										samplePlanType = "N";
										break;
									}
									break;
								case "L":
									switch (ppResult.get(0).getSampleType()) {
									case "6":
									case "5":
									case "4":
									case "3":
										samplePlanType = "L";
										break;
									case "2":
									case "1":
										samplePlanType = "N";
										break;
									}
									break;
								}
								SampleScheme hssSearch = new SampleScheme();
								hssSearch.setAttribute3(switchScore.getPlatform());// 平台
								hssSearch.setSamplePlanType(samplePlanType);
								List<SampleScheme> hssResult = sampleSchemeMapper.select(hssSearch);
								if (hssResult == null || hssResult.size() == 0) {
									throw new RuntimeException(SystemApiMethod.getPromptDescription(request,
											iPromptService, "error.sample_scheme_is_null"));
								}
								Double sampleQty = Math
										.ceil(Float.valueOf(hssResult.get(0).getAttribute4()) * dao.getProduceQty());
								if (sampleSize < sampleQty) {
									sampleSize = sampleQty.floatValue();
								}
								inspectionL.setSampleQty(sampleQty.floatValue());// 抽样数量
							} else {
								// 抽样标准为2时
								inspectionL.setSampleQty(1f);// 抽样数量
							}
							break;
						}
					}
					break;
				case "A":// 抽样数量sample_size直接取HQM_IQC_TASK表中中的receive_qty
					inspectionL.setSampleQty(fqctask.getSampleQty());
					break;
				}
				if (inspectionL.getSampleQty() >= sampleSize) {
					sampleSize = inspectionL.getSampleQty();
				}
				inspectionL.setLineId(null);
				inspectionLAdderList.add(inspectionL);
			}
		} else {
			throw new RuntimeException("not have rel order data");
		}

		return sampleSize;

	}

	Float inspectionLGenerate(List<IqcInspectionTemplateL> templateLineList, List<FqcInspectionL> inspectionLAdderList,
			FqcInspectionH dao, FqcInspectionH dto, HttpServletRequest request, String samplePlan,
			SwitchScore switchScore) {
		Float sampleSize = 1f;
		for (IqcInspectionTemplateL inspectionTemplateL : templateLineList) {
			FqcInspectionL inspectionL = new FqcInspectionL();// 新的检验项
			// 找抽样方式
			SampleManage smSearch = new SampleManage();
			smSearch.setSampleWayId(Float.valueOf(inspectionTemplateL.getSampleWayId()));
			SampleManage sampleManageResult = sampleManageMapper.selectByPrimaryKey(smSearch);
			if (sampleManageResult.getSampleProcedureType() == null) {
				throw new RuntimeException(
						SystemApiMethod.getPromptDescription(request, iPromptService, "error.samplemanage_is_null"));
			}
			if (dao.getProduceQty() == 1) {
				// 生产数量为1时
				inspectionL.setSampleQty(1f);// 抽样数量
			} else {
				// 生产数量大于1时

				switch (sampleManageResult.getSampleType()) {
				case "0":
					inspectionL.setSampleQty(Float.valueOf(sampleManageResult.getParameter()));
					if (sampleSize < inspectionL.getSampleQty()) {
						sampleSize = inspectionL.getSampleQty();
					}
					break;
				case "2":
					inspectionL.setSampleQty(dto.getProduceQty());
					if (sampleSize < inspectionL.getSampleQty()) {
						sampleSize = inspectionL.getSampleQty();
					}
					break;
				case "1":
					SampleSizeCodeLetter searchCodeLetter = new SampleSizeCodeLetter();
					searchCodeLetter.setInspectionLevels(sampleManageResult.getInspectionLevels());
					searchCodeLetter.setLotSizeFrom(dao.getProduceQty());
					searchCodeLetter.setLotSizeTo(dao.getProduceQty());
					// 查找对应的样本量字码
					List<SampleSizeCodeLetter> sizeCodeResultList = new ArrayList<SampleSizeCodeLetter>();
					List<SampleScheme> sampleSchemeResultList = new ArrayList<SampleScheme>();
					if (sampleManageResult.getSampleProcedureType() == null) {
						throw new RuntimeException(SystemApiMethod.getPromptDescription(request, iPromptService,
								"error.sampleproceduretype_is_null"));
					}
					if (inspectionTemplateL.getSampleProcedureType().equals("0")) {
						// 抽样标准为0时
						sizeCodeResultList = sampleSizeCodeLetterMapper.selectCodeSizeLetter(searchCodeLetter);
						SampleScheme sampleSchemeSearch = new SampleScheme();
						sampleSchemeSearch.setSampleSizeCodeLetter(sizeCodeResultList.get(0).getSizeCodeLetter());
						sampleSchemeSearch.setSamplePlanType(samplePlan);
						sampleSchemeSearch.setAcceptanceQualityLimit(sampleManageResult.getAcceptanceQualityLimit());
						// 查找确定检验项的抽样数量、AC、RE值
						sampleSchemeResultList = sampleSchemeMapper.select(sampleSchemeSearch);
						if (sampleSchemeResultList == null || sampleSchemeResultList.size() == 0) {
							throw new RuntimeException(SystemApiMethod.getPromptDescription(request, iPromptService,
									"error.sample_scheme_is_null"));
						}
						if (sampleSchemeResultList.get(0).getSampleSize() > dao.getProduceQty()
								|| sampleSchemeResultList.get(0).getSampleSize().intValue() == (-1)) {
							// 若计算出的抽样数量大于生产数量 或者为-1的话 则抽样数量取值为生产数量
							sampleSchemeResultList.get(0).setSampleSize(dao.getProduceQty());
						}
						if (sampleSize < sampleSchemeResultList.get(0).getSampleSize()) {
							sampleSize = sampleSchemeResultList.get(0).getSampleSize();
						}
						inspectionL.setSizeCodeLetter(sizeCodeResultList.get(0).getSizeCodeLetter());// 样本量字码
						inspectionL.setSampleQty(sampleSchemeResultList.get(0).getSampleSize());// 抽样数量
						inspectionL.setAc(String.valueOf(sampleSchemeResultList.get(0).getAcValue()));// AC值
						inspectionL.setRe(String.valueOf(sampleSchemeResultList.get(0).getReValue()));// RE值
					} else if (inspectionTemplateL.getSampleProcedureType().equals("1")) {
						// 抽样标准为1时
						SampleScheme sampleSchemeSearch = new SampleScheme();
						sampleSchemeSearch.setAcceptanceQualityLimit(sampleManageResult.getAcceptanceQualityLimit());
						sampleSchemeSearch.setAttribute1(String.valueOf(dao.getProduceQty()));
						sampleSchemeSearch.setAttribute2(String.valueOf(dao.getProduceQty()));
						sampleSchemeResultList = sampleSchemeMapper.selectSampleSize(sampleSchemeSearch);
						if (sampleSchemeResultList == null || sampleSchemeResultList.size() == 0) {
							throw new RuntimeException(SystemApiMethod.getPromptDescription(request, iPromptService,
									"error.sample_scheme_is_null"));
						}
						if (sampleSchemeResultList.get(0).getSampleSize() > dao.getProduceQty()
								|| sampleSchemeResultList.get(0).getSampleSize() == (-1)) {
							// 若计算出的抽样数量大于生产数量 或者为-1的话 则抽样数量取值为生产数量
							sampleSchemeResultList.get(0).setSampleSize(dao.getProduceQty());
						}
						if (sampleSize < sampleSchemeResultList.get(0).getSampleSize()) {
							sampleSize = sampleSchemeResultList.get(0).getSampleSize();
						}
						inspectionL.setSampleQty(sampleSchemeResultList.get(0).getSampleSize());// 抽样数量
					} else if (inspectionTemplateL.getSampleProcedureType().equals("3")) {
						/**
						 * 抽样标准为3的逻辑 add by wtm 20191220 switchScore
						 */
						// 查询 检验项目
						InspectionAttribute iaSearch = new InspectionAttribute();
						iaSearch.setAttributeId(inspectionTemplateL.getAttributeId());
						List<InspectionAttribute> iaResult = inspectionAttributeMapper.select(iaSearch);
						PlatformProgram ppSearch = new PlatformProgram();
						ppSearch.setPlantId(dao.getPlantId());
						ppSearch.setPlatformType(switchScore.getPlatform());
						ppSearch.setProgramType(iaResult.get(0).getAttribute1());
						List<PlatformProgram> ppResult = platformProgramMapper.select(ppSearch);
						if (ppResult == null || ppResult.size() == 0) {
							throw new RuntimeException(SystemApiMethod.getPromptDescription(request, iPromptService,
									"error.platform_program_is_null"));
						}
						String samplePlanType = "";
						switch (StringUtil.isEmpty(inspectionTemplateL.getSamplePlan()) ? "N"
								: inspectionTemplateL.getSamplePlan()) {// 获取抽样方案
						case "K":
							switch (ppResult.get(0).getSampleType()) {
							case "6":
							case "5":
								samplePlanType = "K";
								break;
							case "4":
							case "3":
								samplePlanType = "L";
								break;
							case "2":
							case "1":
								samplePlanType = "N";
								break;
							}
							break;
						case "N":
							samplePlanType = "N";
							break;
						case "S":
							switch (ppResult.get(0).getSampleType()) {
							case "6":
							case "4":
							case "2":
								samplePlanType = "S";
								break;
							case "5":
							case "3":
							case "1":
								samplePlanType = "N";
								break;
							}
							break;
						case "L":
							switch (ppResult.get(0).getSampleType()) {
							case "6":
							case "5":
							case "4":
							case "3":
								samplePlanType = "L";
								break;
							case "2":
							case "1":
								samplePlanType = "N";
								break;
							}
							break;
						}
						SampleScheme hssSearch = new SampleScheme();
						hssSearch.setAttribute3(switchScore.getPlatform());// 平台
						hssSearch.setSamplePlanType(samplePlanType);
						List<SampleScheme> hssResult = sampleSchemeMapper.select(hssSearch);
						if (hssResult == null || hssResult.size() == 0) {
							throw new RuntimeException(SystemApiMethod.getPromptDescription(request, iPromptService,
									"error.sample_scheme_is_null"));
						}
						Double sampleQty = Math
								.ceil(Float.valueOf(hssResult.get(0).getAttribute4()) * dao.getProduceQty());
						if (sampleSize < sampleQty) {
							sampleSize = sampleQty.floatValue();
						}
						inspectionL.setSampleQty(sampleQty.floatValue());// 抽样数量
					} else {
						// 抽样标准为2时
						inspectionL.setSampleQty(1f);// 抽样数量
					}
					break;
				}

			}
			inspectionL.setSamplePlan(inspectionTemplateL.getSamplePlan());
			inspectionL.setTemplateLineId(inspectionTemplateL.getLineId());
			inspectionL.setInspectionMethod(inspectionTemplateL.getInspectionMethod());
			inspectionL.setPrecision(inspectionTemplateL.getPrecision());
			inspectionL.setAttributeId(inspectionTemplateL.getAttributeId());
			inspectionL.setAttributeType(inspectionTemplateL.getAttributeType());
			inspectionL.setOrderCode(inspectionTemplateL.getOrderCode());
			inspectionL.setInspectionAttribute(inspectionTemplateL.getInspectionAttribute());
			inspectionL.setInspectionTool(inspectionTemplateL.getInspectionTool());
			inspectionL.setSampleProcedureType(inspectionTemplateL.getSampleProcedureType());
			inspectionL.setInspectionLevels(inspectionTemplateL.getInspectionLevels());
			inspectionL.setQualityCharacterGrade(inspectionTemplateL.getQualityCharacterGrade());
			inspectionL.setStandardType(inspectionTemplateL.getStandardType());
			inspectionL.setStandradFrom(inspectionTemplateL.getStandradFrom());
			inspectionL.setStandradTo(inspectionTemplateL.getStandradTo());
			inspectionL.setStandradUom(inspectionTemplateL.getStandradUom());
			inspectionL.setTextStandrad(inspectionTemplateL.getTextStandrad());
			// inspectionL.setDataFrom(inspectionTemplateL.getFillInType());
			inspectionL.setAcceptanceQualityLimit(inspectionTemplateL.getAcceptanceQualityLimit());
			inspectionL.setSampleWayId(Float.valueOf(inspectionTemplateL.getSampleWayId()));
			inspectionL.setSampleType(Float.parseFloat(sampleManageResult.getSampleType()));
			if (sampleManageResult.getParameter() != null && !StringUtils.isBlank(sampleManageResult.getParameter())) {
				inspectionL.setParameter(Float.parseFloat(sampleManageResult.getParameter()));
			}
			inspectionL.setFillInType(inspectionTemplateL.getFillInType());
			inspectionLAdderList.add(inspectionL);
		}
		return sampleSize;

	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void samplingFeedback(ResponseData rsd) {
		IfInvokeOutbound iio = new IfInvokeOutbound();
		List<FqcInspectionH> fqcInspectionHs = (List<FqcInspectionH>) rsd.getRows();
		FqcInspectionH fih = fqcInspectionHs.get(0);
		SamplingFeedbackInfo sfi = new SamplingFeedbackInfo();
		// 查询工厂id
		Plant plant = new Plant();
		plant.setPlantId(fih.getPlantId());
		List<Plant> plantList = plantMapper.select(plant);
		if (plantList != null && plantList.size() > 0) {
			sfi.setPlantCode(plantList.get(0).getPlantCode());
		} else {
			// 工厂编码在工厂表中找不到！
			logger.warn("工厂编码在工厂表中找不到！");
			return;
		}
		sfi.setSourceOrder(fih.getSourceOrder());
		// 关联检验通知表查询
		FqcTask fqcTask = new FqcTask();
		fqcTask.setInspectionNum(fih.getInspectionNum());
		List<FqcTask> fqcTasks = ffqcTaskMapper.select(fqcTask);
		if (fqcTasks != null && fqcTasks.size() > 0) {
			FqcTask fqcTaskR = fqcTasks.get(0);

			// 查询物料编码
			Item item = new Item();
			item.setItemId(fqcTaskR.getItemId());
			List<Item> items = itemMapper.select(item);
			if (items != null && items.size() > 0) {
				sfi.setItemCode(items.get(0).getItemCode());
			} else {
				// 物料编码在物料表中找不到！
				logger.warn("物料编码在物料表中找不到！");
				return;
			}

			sfi.setItemVersion(fqcTaskR.getItemVersion());
		} else {
			// 无法关联检验通知表
			logger.warn("无法关联检验通知表!");
			return;
		}

		sfi.setReceiveQty(fih.getSampleQty());
		sfi.setInspectionNum(fih.getInspectionNum());
		// 往回找 FqcTask
		FqcTask ftSearch = new FqcTask();
		ftSearch.setInspectionNum(fih.getInspectionNum());
		List<FqcTask> taskResult = fqcTaskMapper.select(ftSearch);
		if (taskResult != null && taskResult.size() > 0) {
			sfi.setSpreading(taskResult.get(0).getSpreading());
			sfi.setLotNumber(taskResult.get(0).getProductionBatch());
		}
		// 获取物料检验地点
		ItemControlQms ic = new ItemControlQms();
		ic.setItemId(fih.getItemId());
		ic.setPlantId(fih.getPlantId());
		List<ItemControlQms> icResult = itemControlQmsMapper.select(ic);
		if (icResult != null && icResult.size() > 0) {
			sfi.setInspectionPlace(icResult.get(0).getCheckPlace());
		}
		taskExecutor.execute(new Runnable() {
			@Override
			public void run() {
				// 转为JSON String发送
				ObjectMapper omapper = new ObjectMapper();
				String post;
				try {
					ServiceRequest sr = new ServiceRequest();
					sr.setLocale("zh_CN");
					String uri = iCodeService.getCodeValueByMeaning(sr, "HAP.SYSTEM", "WMS_WS_URI");// 获取调用地址
					post = omapper.writeValueAsString(sfi);
					SoapPostUtil.Response re = SoapPostUtil.ticketSrmToWms("getSkuSampleQty", post, iio, uri);
					ifInvokeOutboundMapper.insertSelective(iio);
					logger.info(SoapPostUtil.getStringFromResponse(re));
				} catch (Exception e) {
					iio.setResponseContent(e.getMessage());
					iio.setResponseCode("E");
					ifInvokeOutboundMapper.insertSelective(iio);
					logger.warn(e.getMessage());
				}
			}
		});

	}

	/* (non-Javadoc)
	 * @see com.hand.hqm.hqm_fqc_inspection_h.service.IFqcInspectionHService#reSelect(com.hand.hap.core.IRequest, com.hand.hqm.hqm_fqc_inspection_h.dto.FqcInspectionH, int, int)
	 */
	@Override
	public List<FqcInspectionH> reSelect(IRequest requestContext, FqcInspectionH dto, int page, int pageSize) {
		PageHelper.startPage(page, pageSize);
		return fqcInspectionHMapper.selectByNumber(dto);
	}
}