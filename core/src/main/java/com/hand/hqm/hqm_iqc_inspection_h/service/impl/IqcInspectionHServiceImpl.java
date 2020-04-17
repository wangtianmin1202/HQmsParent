package com.hand.hqm.hqm_iqc_inspection_h.service.impl;

import com.github.pagehelper.PageHelper;
import com.hand.hap.activiti.dto.ProcessInstanceResponseExt;
import com.hand.hap.activiti.service.IActivitiService;
import com.hand.hap.core.IRequest;
import com.hand.hap.core.impl.RequestHelper;
import com.hand.hap.core.impl.ServiceRequest;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.system.service.ICodeService;
import com.hand.hap.system.service.IPromptService;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import com.hand.hap.util.SoapPostUtil;
import com.hand.hap.util.SystemApiMethod;
import com.hand.hcm.hcm_item.dto.Item;
import com.hand.hcm.hcm_item.mapper.ItemMapper;
import com.hand.hcm.hcm_plant.dto.Plant;
import com.hand.hcm.hcm_plant.mapper.PlantMapper;
import com.hand.hcs.hcs_supplier_site.dto.SupplierSite;
import com.hand.hcs.hcs_supplier_site.mapper.SupplierSiteMapper;
import com.hand.hcs.hcs_suppliers.dto.Suppliers;
import com.hand.hcs.hcs_suppliers.mapper.SuppliersMapper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;

import com.hand.hqm.hqm_asl_iqc_control.dto.AslIqcControl;
import com.hand.hqm.hqm_asl_iqc_control.mapper.AslIqcControlMapper;
import com.hand.hqm.hqm_fqc_inspection_l.dto.FqcInspectionL;
import com.hand.hqm.hqm_fqc_inspection_l.mapper.FqcInspectionLMapper;
import com.hand.hqm.hqm_fqc_sample_switch.dto.FqcSampleSwitch;
import com.hand.hqm.hqm_iqc_inspection_d.dto.IqcInspectionD;
import com.hand.hqm.hqm_iqc_inspection_d.mapper.IqcInspectionDMapper;
import com.hand.hqm.hqm_iqc_inspection_d.service.IIqcInspectionDService;
import com.hand.hqm.hqm_iqc_inspection_h.dto.IqcInspectionH;
import com.hand.hqm.hqm_iqc_inspection_h.dto.SamplingNumInfo;
import com.hand.hqm.hqm_iqc_inspection_h.dto.TestConclusionInfo;
import com.hand.hqm.hqm_iqc_inspection_h.mapper.IqcInspectionHMapper;
import com.hand.hqm.hqm_iqc_inspection_h.service.IIqcInspectionHService;
import com.hand.hqm.hqm_iqc_inspection_l.dto.IqcInspectionL;
import com.hand.hqm.hqm_iqc_inspection_l.mapper.IqcInspectionLMapper;
import com.hand.hqm.hqm_iqc_inspection_template_h.dto.IqcInspectionTemplateH;
import com.hand.hqm.hqm_iqc_inspection_template_h.mapper.IqcInspectionTemplateHMapper;
import com.hand.hqm.hqm_iqc_inspection_template_l.dto.IqcInspectionTemplateL;
import com.hand.hqm.hqm_iqc_inspection_template_l.mapper.IqcInspectionTemplateLMapper;
import com.hand.hqm.hqm_iqc_inspection_template_l.service.IIqcInspectionTemplateLService;
import com.hand.hqm.hqm_nonconforming_order.dto.NonconformingOrder;
import com.hand.hqm.hqm_nonconforming_order.mapper.NonconformingOrderMapper;
import com.hand.hqm.hqm_nonconforming_order.service.impl.NonconformingOrderServiceImpl;
import com.hand.hqm.hqm_qc_task.dto.IqcTask;
import com.hand.hqm.hqm_qc_task.mapper.IqcTaskMapper;
import com.hand.hqm.hqm_sample_manage.dto.SampleManage;
import com.hand.hqm.hqm_sample_manage.mapper.SampleManageMapper;
import com.hand.hqm.hqm_sample_scheme.dto.SampleScheme;
import com.hand.hqm.hqm_sample_scheme.mapper.SampleSchemeMapper;
import com.hand.hqm.hqm_sample_size_code_letter.dto.SampleSizeCodeLetter;
import com.hand.hqm.hqm_sample_size_code_letter.mapper.SampleSizeCodeLetterMapper;
import com.hand.hqm.hqm_sample_switch_rule.dto.SampleSwitchRule;
import com.hand.hqm.hqm_sample_switch_rule.mapper.SampleSwitchRuleMapper;
import com.hand.hqm.hqm_supp_item_exemption.dto.SuppItemExemption;
import com.hand.hqm.hqm_supp_item_exemption.mapper.SuppItemExemptionMapper;
import com.hand.hqm.hqm_switch_rule.dto.SwitchRule;
import com.hand.hqm.hqm_switch_rule.mapper.SwitchRuleMapper;
import com.hand.hqm.hqm_switch_score.dto.SwitchScore;
import com.hand.hqm.hqm_switch_score.mapper.SwitchScoreMapper;
import com.hand.hqm.hqm_switch_score.service.ISwitchScoreService;
import com.hand.sys.sys_if_invoke_outbound.dto.IfInvokeOutbound;
import com.hand.sys.sys_if_invoke_outbound.mapper.IfInvokeOutboundMapper;

import jodd.util.StringUtil;

import org.springframework.transaction.annotation.Transactional;
import org.activiti.rest.service.api.engine.variable.RestVariable;
import org.activiti.rest.service.api.runtime.process.ProcessInstanceCreateRequest;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@Transactional(rollbackFor = Exception.class)
public class IqcInspectionHServiceImpl extends BaseServiceImpl<IqcInspectionH> implements IIqcInspectionHService {

	@Autowired
	IqcInspectionHMapper iqcInspectionHMapper;
	@Autowired
	IqcTaskMapper iqcTaskMapper;
	@Autowired
	IqcInspectionDMapper iqcInspectionDMapper;
	@Autowired
	IqcInspectionLMapper iqcInspectionLMapper;
	@Autowired
	IPromptService iPromptService;
	@Autowired
	AslIqcControlMapper aslIqcControlMapper;
	@Autowired
	IqcInspectionTemplateHMapper iqcInspectionTemplateHMapper;
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
	SampleSwitchRuleMapper sampleSwitchRuleMapper;
	@Autowired
	SwitchRuleMapper switchRuleMapper;
	@Autowired
	SampleManageMapper sampleManageMapper;
	@Autowired
	IIqcInspectionTemplateLService iIqcInspectionTemplateLService;
	@Autowired
	IIqcInspectionDService iIqcInspectionDService;
	@Autowired
	PlantMapper plantMapper;
	@Autowired
	SupplierSiteMapper supplierSiteMapper;
	@Autowired
	ItemMapper itemMapper;
	@Autowired
	SuppItemExemptionMapper suppItemExemptionMapper;
	@Autowired
	NonconformingOrderMapper nonconformingOrderMapper;
	@Resource(name = "taskExecutor")
	private TaskExecutor taskExecutor;
	@Autowired
	ICodeService iCodeService;
	@Autowired
	IfInvokeOutboundMapper ifInvokeOutboundMapper;
	@Autowired
	private IActivitiService activitiService;

	private Logger logger = LoggerFactory.getLogger(IqcInspectionHServiceImpl.class);

	@Override
	public List<IqcInspectionH> selectByNumber(IRequest requestContext, IqcInspectionH dto, int page, int pageSize) {
		// TODO 查询检验单号
		PageHelper.startPage(page, pageSize);
		return iqcInspectionHMapper.selectByNumber(dto);
	}

	@Override
	public List<IqcInspectionH> selectForOther(IqcInspectionH dto, List<IqcInspectionL> lineList, int page,
			int pageSize) {
		PageHelper.startPage(page, pageSize);
		return iqcInspectionHMapper.selectForOther(dto);
	}

	/**
	 * 
	 * ruifu.jiang 20190813
	 * 
	 * @throws Exception 不合格检验单关联
	 */
	@Override
	public List<IqcInspectionH> selectFornon(IRequest requestContext, IqcInspectionH dto) {
		// TODO 查询检验单号
		return iqcInspectionHMapper.selectFornon(dto);
	}

	/**
	 * 
	 * tianmin.wang 20190725
	 * 
	 * @throws Exception 暂挂检验单
	 */
	@Override
	public ResponseData pauseInspection(IqcInspectionH dto, List<IqcInspectionL> lineList, HttpServletRequest request)
			throws Exception {

		// TODO 检验单暂挂
		ResponseData responseData = new ResponseData();
		IqcInspectionH updaterIqcInspectionH = dto;
		updaterIqcInspectionH.setInspectionStatus("3");
		// 更新检验单的状态为 检测中3
		iqcInspectionHMapper.updateByPrimaryKeySelective(updaterIqcInspectionH);

		IqcTask updateIqcTask = new IqcTask();
		IqcTask searchIqcTask = new IqcTask();
		searchIqcTask.setInspectionNum(updaterIqcInspectionH.getInspectionNum());
		List<IqcTask> iqcTaskList = new ArrayList<IqcTask>();
		iqcTaskList = iqcTaskMapper.select(searchIqcTask);
		if (iqcTaskList != null && iqcTaskList.size() > 0) {
			updateIqcTask = iqcTaskList.get(0);
			updateIqcTask.setSolveStatus("3");
			// 更新检验任务的状态为 检测中3
			iqcTaskMapper.updateByPrimaryKey(updateIqcTask);
		}

		for (IqcInspectionL iqcInspectionL : lineList) {
			// 保存每项检验项状态并同时保存每项检验项的值 至 HQM_IQC_INSPECTION_D表内
//			String[] hisArray = new String[] {};
//			if (iqcInspectionL.getInspectionHistory() != null) {
//				hisArray = iqcInspectionL.getInspectionHistory().split(",");
//			}
//			for (int i = 0; i < hisArray.length; i++) {
//				saveInspectionD(iqcInspectionL.getLineId(), String.valueOf(i), hisArray[i], iqcInspectionL);
//			}
			iqcInspectionLMapper.updateByPrimaryKeySelective(iqcInspectionL);
		}
		return responseData;
	}

	/**
	 * 
	 * @description 保存D表数据
	 * @author tianmin.wang
	 * @date 2019年11月22日
	 * @param lineId
	 * @param orderNum
	 * @param data
	 * @param iqcInspectionL
	 */
	public void saveInspectionD(Float lineId, String orderNum, String data, IqcInspectionL iqcInspectionL) {
		IqcInspectionD search = new IqcInspectionD();
		search.setLineId(lineId);
		search.setOrderNum(orderNum);
		List<IqcInspectionD> li = new ArrayList<IqcInspectionD>();
		String judgement = new String();
		if ("M".equals(iqcInspectionL.getStandardType())) {
			// M型需要依照规格值从和规格值至计算judgement
			if (Float.valueOf(data).floatValue() < Float.valueOf(iqcInspectionL.getStandradFrom())
					|| Float.valueOf(data).floatValue() > Float.valueOf(iqcInspectionL.getStandradTo())) {
				judgement = "NG";
			} else {
				judgement = "OK";
			}

		} else {
			judgement = data;
		}
		li = iqcInspectionDMapper.select(search);
		if (li.size() == 0) {
			IqcInspectionD insert = new IqcInspectionD();
			insert.setLineId(lineId);
			insert.setOrderNum(orderNum);
			insert.setData(data);
			insert.setJudgement(judgement);
			iqcInspectionDMapper.insertSelective(insert);
		} else {
			IqcInspectionD updater = li.get(0);
			updater.setData(data);
			updater.setJudgement(judgement);
			iqcInspectionDMapper.updateByPrimaryKeySelective(updater);
		}
	}

	/**
	 * tianmin.wang 20190725 提交检验单
	 */
	@Override
	public ResponseData commitInspection(IqcInspectionH dto, List<IqcInspectionL> lineList, HttpServletRequest request,
			IRequest requestCtx) throws Exception {
		// TODO 检验单提交
		ResponseData responseData = new ResponseData();
		IqcInspectionH updaterIqcInspectionH = dto;
		String inspectionStatus = "4";
		if ("OK".equals(dto.getInspectionJudge())) {
			inspectionStatus = "5";

			updaterIqcInspectionH.setInspectionStatus("4");
			updaterIqcInspectionH.setInspectionDate(new Date());
			updaterIqcInspectionH.setDetailStatus("C");
			// 更新检验单的状态为 待审核4
			iqcInspectionHMapper.updateByPrimaryKeySelective(updaterIqcInspectionH);
		} else if ("NG".equals(dto.getInspectionJudge())) {
			inspectionStatus = "4";
		} else {

		}
		/*
		 * updaterIqcInspectionH.setInspectionStatus("4");
		 * updaterIqcInspectionH.setInspectionDate(new Date());
		 * updaterIqcInspectionH.setDetailStatus("C"); // 更新检验单的状态为 待审核4
		 * iqcInspectionHMapper.updateByPrimaryKeySelective(updaterIqcInspectionH);
		 */

		IqcTask updateIqcTask = new IqcTask();
		IqcTask searchIqcTask = new IqcTask();
		searchIqcTask.setInspectionNum(updaterIqcInspectionH.getInspectionNum());
		List<IqcTask> iqcTaskList = new ArrayList<IqcTask>();

		iqcTaskList = iqcTaskMapper.select(searchIqcTask);
		if (iqcTaskList != null && iqcTaskList.size() > 0) {
			updateIqcTask = iqcTaskList.get(0);
			updateIqcTask.setSolveStatus(inspectionStatus);
			// 更新检验任务的状态为 待审核4
			iqcTaskMapper.updateByPrimaryKey(updateIqcTask);
		}
		IqcInspectionL max = new IqcInspectionL();
		max.setSampleSize(0f);
		Float passQuantity = 0f;
		Float ngQuantity = 0f;
		for (IqcInspectionL iqcInspectionL : lineList) {
			IqcInspectionD sear = new IqcInspectionD();
			sear.setLineId(iqcInspectionL.getLineId());
			List<IqcInspectionD> result = iqcInspectionDMapper.select(sear);
			if (iqcInspectionL.getSampleSize() > max.getSampleSize()) {
				passQuantity = 0f;
				ngQuantity = 0f;
				max = iqcInspectionL;
				for (IqcInspectionD ddto : result) {
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
				for (IqcInspectionD ddto : result) {
					if (StringUtils.isEmpty(ddto.getData())) {
						throw new Exception(SystemApiMethod.getPromptDescription(request, iPromptService,
								"error.hqm_iqc_order_submit_02"));
					}
				}
			}
			iqcInspectionLMapper.updateByPrimaryKeySelective(iqcInspectionL);
		}
		IqcInspectionH update = new IqcInspectionH();
		update.setHeaderId(dto.getHeaderId());
		update.setPassQuantity(passQuantity);
		update.setNgQuantity(ngQuantity);
		update.setInspectionDate(new Date());
		self().updateByPrimaryKeySelective(RequestHelper.createServiceRequest(request), update);
		// 如果为检验OK状态就再执行一次审核
		// wtm:0920注释掉
		if ("5".equals(inspectionStatus)) {
			updaterIqcInspectionH.setApprovalResult("OK");
			updaterIqcInspectionH.setChangedValue(dto.getChangedValue());
			this.auditInspection(updaterIqcInspectionH, lineList, request);
		} else if ("4".equals(inspectionStatus)) {// 不合格时发起流程
			updaterIqcInspectionH.setInspectionStatus("5");
			updaterIqcInspectionH.setInspectionDate(new Date());
			updaterIqcInspectionH.setDetailStatus("C");
			updaterIqcInspectionH.setApprovalResult("NG");
			updaterIqcInspectionH.setChangedValue(dto.getChangedValue());
			this.auditInspection(updaterIqcInspectionH, lineList, request);

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
			if (dto.getSupplierId() != null) {
				nonconformingOrder.setSupplierId(dto.getSupplierId());
			}

			nonconformingOrder.setNoStatus("0");

			if (dto.getProductionBatch() != null) {
				nonconformingOrder.setProductionBatch(dto.getProductionBatch());
			}

			if (dto.getReceiveDate() != null) {
				nonconformingOrder.setReceiveDate(dto.getReceiveDate());
			}

			nonconformingOrder.setTotalityQty(dto.getReceiveQty());
			nonconformingOrder.setSampleSize(dto.getSampleSize());

			if (dto.getInspectionDate() != null) {
				nonconformingOrder.setInspectionDate(dto.getInspectionDate());
			}

			if (dto.getInspectorId() != null) {
				nonconformingOrder.setApprovalId(Float.parseFloat(dto.getInspectorId()));
			}
			nonconformingOrder.setNoSourceType("1");
			nonconformingOrderMapper.insertSelective(nonconformingOrder);

			// 启动流程
			ProcessInstanceCreateRequest instanceCreateRequest = new ProcessInstanceCreateRequest();
			instanceCreateRequest.setBusinessKey(String.valueOf(nonconformingOrder.getNoId()));
			ProcessInstanceResponseExt responseExt = new ProcessInstanceResponseExt();

			if ("IQC".equals(dto.getInspectionType())) {
				instanceCreateRequest.setProcessDefinitionKey("iqcUnqualityProcess");
				responseExt = (ProcessInstanceResponseExt) activitiService.startProcess(requestCtx,
						instanceCreateRequest);
			} else if ("FQC".equals(dto.getInspectionType())) {
				instanceCreateRequest.setProcessDefinitionKey("fqcUnqualityProcess");
				responseExt = (ProcessInstanceResponseExt) activitiService.startProcess(requestCtx,
						instanceCreateRequest);
			}

			nonconformingOrder.setProcessInstanceId(responseExt.getId());
			nonconformingOrderMapper.updateByPrimaryKeySelective(nonconformingOrder);
		}
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

	@Override
	public ResponseData auditInspection(IqcInspectionH dto, List<IqcInspectionL> lineList, HttpServletRequest request)
			throws Exception {
		// TODO 检验单审核
		ResponseData responseData = new ResponseData();
		IqcInspectionH updaterIqcInspectionH = dto;

		updaterIqcInspectionH.setInspectionStatus("5");
		if ("Y".equals(updaterIqcInspectionH.getScoreFlag())) {
			// 转移得分修改
			SwitchScore searchSwitchScore = new SwitchScore();
			searchSwitchScore.setItemId(dto.getItemId());
			searchSwitchScore.setPlantId(dto.getPlantId());
			searchSwitchScore.setSupplierId(dto.getSupplierId());
			List<SwitchScore> resultSwitchScoreList = new ArrayList<>();
			resultSwitchScoreList = switchScoreMapper.select(searchSwitchScore);
			if (resultSwitchScoreList == null || resultSwitchScoreList.size() == 0) {
				throw new Exception("SwitchScore未找到记录(转移得分修改)");
			}
			SwitchScore updaterSwitchScore = new SwitchScore();
			updaterSwitchScore.setScoreId(resultSwitchScoreList.get(0).getScoreId());
			updaterSwitchScore.setSwitchScore(resultSwitchScoreList.get(0).getSwitchScore());
			SwitchRule searchSwitchRule = new SwitchRule();
			searchSwitchRule.setSamplePlanType(dto.getSamplePlan());
			searchSwitchRule.setInspectionJudge(dto.getInspectionJudge());
			List<SwitchRule> searchSwitchRuleList = new ArrayList<>();
			searchSwitchRuleList = switchRuleMapper.select(searchSwitchRule);
			if (searchSwitchRuleList == null || searchSwitchRuleList.size() == 0) {
				throw new Exception("SwitchRule未找到记录(转移得分修改)");
			}
			if ("S".equals(dto.getSamplePlan())) {
				// 加严
				if ("OK".equals(searchSwitchRuleList.get(0).getInspectionJudge())) {
					Float endValue = updaterSwitchScore.getSwitchScore()
							+ searchSwitchRuleList.get(0).getSwitchScoreRule();
					if ((endValue - searchSwitchRuleList.get(0).getSwitchScoreLimit()) > 0.0F) {
						endValue = searchSwitchRuleList.get(0).getSwitchScoreLimit();
					}
					updaterSwitchScore.setSwitchScore(endValue);
				} else {
					updaterSwitchScore.setSwitchScore(searchSwitchRuleList.get(0).getSwitchScoreRule());
				}
			} else if ("P".equals(dto.getSamplePlan())) {
				// 暂停
				updaterSwitchScore.setSwitchScore(searchSwitchRuleList.get(0).getSwitchScoreRule());
			} else {
				// 其他
				if ("NG".equals(searchSwitchRuleList.get(0).getInspectionJudge())) {
					Float endValue = updaterSwitchScore.getSwitchScore()
							- searchSwitchRuleList.get(0).getSwitchScoreRule();
					if ((searchSwitchRuleList.get(0).getSwitchScoreLimit() - endValue) > 0.0F) {
						endValue = searchSwitchRuleList.get(0).getSwitchScoreLimit();
					}
					updaterSwitchScore.setSwitchScore(endValue);
				} else {
					Float endValue = updaterSwitchScore.getSwitchScore()
							+ searchSwitchRuleList.get(0).getSwitchScoreRule();
					if ((endValue - searchSwitchRuleList.get(0).getSwitchScoreLimit()) > 0.0F) {
						endValue = searchSwitchRuleList.get(0).getSwitchScoreLimit();
					}
					updaterSwitchScore.setSwitchScore(endValue);
				}
			}
			// updaterSwitchScore.setSwitchScore(searchSwitchRuleList.get(0).getSwitchScoreRule());
			switchScoreMapper.updateByPrimaryKeySelective(updaterSwitchScore);
		}

//		if (!StringUtils.isEmpty(dto.getChangedValue()) && !"-1".equals(dto.getChangedValue())) {
//			// 抽样方案逻辑
//			SwitchScore searchSwitchScore = new SwitchScore();
//			searchSwitchScore.setItemId(dto.getItemId());
//			searchSwitchScore.setPlantId(dto.getPlantId());
//			searchSwitchScore.setSupplierId(dto.getSupplierId());
//			List<SwitchScore> resultSwitchScoreList = new ArrayList<>();
//			resultSwitchScoreList = switchScoreMapper.select(searchSwitchScore);
//			if (resultSwitchScoreList == null || resultSwitchScoreList.size() == 0) {
//				throw new Exception("SwitchScore未找到记录(抽样方案逻辑)");
//			}
//			SwitchScore updaterSwitchScore = new SwitchScore();
//			updaterSwitchScore.setScoreId(resultSwitchScoreList.get(0).getScoreId());
//			updaterSwitchScore.setSamplePlanTypeNow(dto.getChangedValue());
//			updaterSwitchScore.setSamplePlanTypeNext(dto.getSamplePlan());
//			switchScoreMapper.updateByPrimaryKeySelective(updaterSwitchScore);
//		}
		// 更新检验单的状态为 待审核4
		iqcInspectionHMapper.updateByPrimaryKeySelective(updaterIqcInspectionH);

		IqcTask updateIqcTask = new IqcTask();
		IqcTask searchIqcTask = new IqcTask();
		searchIqcTask.setInspectionNum(updaterIqcInspectionH.getInspectionNum());
		List<IqcTask> iqcTaskList = new ArrayList<IqcTask>();

		iqcTaskList = iqcTaskMapper.select(searchIqcTask);
		if (iqcTaskList.size() > 0) {
			updateIqcTask = iqcTaskList.get(0);
			updateIqcTask.setSolveStatus("C");
			updateIqcTask.setInspectionRes(updaterIqcInspectionH.getInspectionJudge());
			// 更新检验任务的状态为 已完成5
			iqcTaskMapper.updateByPrimaryKey(updateIqcTask);
		}
		if ("0".equals(dto.getSourceType()))// IQC.首次检 抽样方案变更 必须在 检验单状态变更后执行
			responseData.setMessage(changeSamplePlan(request, dto));

		// 审核完成后反馈WMS
		conclusionToWMS(dto);

		return responseData;
	}

	/**
	 * IQC反馈检验结论
	 * 
	 * @author kai.li
	 * @param dto
	 */
	@Transactional(rollbackFor = Exception.class)
	@Override
	public void conclusionToWMS(IqcInspectionH dto) {
		IfInvokeOutbound iio = new IfInvokeOutbound();
		TestConclusionInfo tci = new TestConclusionInfo();

		// 关联工厂表查询
		Plant plant = new Plant();
		plant.setPlantId(dto.getPlantId());
		List<Plant> plantList = plantMapper.select(plant);
		if (plantList != null && plantList.size() > 0) {
			tci.setPlantCode(plantList.get(0).getPlantCode());
		} else {
			// 工厂编码在工厂表中找不到！
			logger.warn("工厂编码在工厂表中找不到！");
			return;
		}

		tci.setInspectionNum(dto.getInspectionNum());
		tci.setQty(dto.getReceiveQty());

		// 查看是否存在不合格处理单
		NonconformingOrder nfoSearch = new NonconformingOrder();
		nfoSearch.setInspectionCode(dto.getInspectionNum());
		nfoSearch.setInspectionType("IQC");
		List<NonconformingOrder> nfoResult = nonconformingOrderMapper.select(nfoSearch);
		if (nfoResult != null && nfoResult.size() > 0) {
			tci.setDealMethod(nfoResult.get(0).getDealMethod());
		}
		if (!StringUtils.isEmpty(tci.getDealMethod())) {
			tci.setItemStatus("1/3".equals(tci.getDealMethod()) ? "OK" : dto.getInspectionJudge());
		} else {
			tci.setItemStatus(dto.getInspectionJudge());
		}

		// 关联检验通知表查询
		IqcTask iqcTask = new IqcTask();
		iqcTask.setInspectionNum(dto.getInspectionNum());
		List<IqcTask> iqcTaskList = iqcTaskMapper.select(iqcTask);
		if (iqcTaskList != null && iqcTaskList.size() > 0) {
			IqcTask iqcTaskR = iqcTaskList.get(0);

			// tci.setLineNum(iqcTaskR.getLineNum());
			// 关联物料表查询
			Item item = new Item();
			item.setItemId(iqcTaskR.getItemId());
			List<Item> itemList = itemMapper.select(item);
			if (itemList != null && itemList.size() > 0) {
				tci.setItemCode(itemList.get(0).getItemCode());
			} else {
				// 物料编码在物料表中找不到！
				logger.warn("物料编码在物料表中找不到！");
				return;
			}

			tci.setItemVersion(iqcTaskR.getItemVersion());
			tci.setSpreading(iqcTaskR.getSpreading());
			tci.setLotNumber(iqcTaskR.getProductionBatch());
			// tci.setPoNumber(Float.toString(iqcTaskR.getPoNumber()));
		} else {
			// 无法关联检验通知表
			logger.warn("无法关联检验通知表！");
			return;
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
					post = omapper.writeValueAsString(tci);
					SoapPostUtil.Response re = SoapPostUtil.ticketSrmToWms("getIqcInspectionResult", post, iio, uri);
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
	 * @description 变更抽样方案
	 * @author tianmin.wang
	 * @date 2020年2月20日
	 * @param request
	 * @param dto
	 */
	private String changeSamplePlan(HttpServletRequest request, IqcInspectionH dto) {
		IRequest ist = RequestHelper.createServiceRequest(request);
		IqcInspectionL searchL = new IqcInspectionL();
		searchL.setHeaderId(dto.getHeaderId());
		List<IqcInspectionL> lineList = iqcInspectionLMapper.select(searchL);
		String message = "successs";
		for (IqcInspectionL model : lineList) {
			String blo = "";
			if ("NG".equals(model.getAttributeInspectionRes()))
				blo = changeSamlePlanNg(ist, dto, model);
			else
				blo = changeSamlePlanOk(ist, dto, model);
			if (!"successs".equals(blo))
				message = blo;
		}
		return message;
	}

	/**
	 * @description
	 * @author tianmin.wang
	 * @date 2020年2月20日
	 * @param ist
	 * @param dto
	 * @param model
	 * @return
	 */
	private String changeSamlePlanOk(IRequest ist, IqcInspectionH dto, IqcInspectionL model) {
		String message = "success";
		IqcInspectionTemplateL temSearch = new IqcInspectionTemplateL();
		temSearch.setLineId(model.getTemplateLineId());
		List<IqcInspectionTemplateL> temResult = iqcInspectionTemplateLMapper.reSelect(temSearch);
		if (temResult == null || temResult.size() == 0)
			return null;
		if (("0".equals(temResult.get(0).getSampleType()) || "1".equals(temResult.get(0).getSampleType()))
				&& (!"1".equals(temResult.get(0).getSampleProcedureType())
						&& !"3".equals(temResult.get(0).getSampleProcedureType()))) {// 抽样方式为0 1且其抽样标准不为1 3时
			switch (StringUtil.isEmpty(model.getSamplePlan()) ? "N" : model.getSamplePlan()) {
			case "S":// 加严S变为正常N
				if (!getNMValue("S", model))
					return message;
				List<IqcInspectionL> lLists = iqcInspectionLMapper.timeItemSelectN(model);
				if (lLists.stream().filter(p -> "OK".equals(p.getAttributeInspectionRes())).count() >= model
						.getValueM()) {
					if (lLists.stream()
							.filter(p -> !"S".equals(StringUtils.isEmpty(p.getSamplePlan()) ? "N" : p.getSamplePlan()))
							.count() > 0)
						return message;
					IqcInspectionTemplateL upda = new IqcInspectionTemplateL();
					upda.setLineId(model.getTemplateLineId());
					upda.setSamplePlan("N");
					iIqcInspectionTemplateLService.updateByPrimaryKeySelective(ist, upda);
				}
				break;
			case "N":// 正常N变为放宽L
				if (!getNMValue("N", model))
					return message;
				List<IqcInspectionL> lListn = iqcInspectionLMapper.timeItemSelectN(model);
				if (lListn.stream().filter(p -> "OK".equals(p.getAttributeInspectionRes())).count() >= model
						.getValueM()) {

					if (lListn.stream()
							.filter(p -> !"N".equals(StringUtils.isEmpty(p.getSamplePlan()) ? "N" : p.getSamplePlan()))
							.count() > 0)
						return message;// ??
					IqcInspectionTemplateL upda = new IqcInspectionTemplateL();
					upda.setLineId(model.getTemplateLineId());
					upda.setSamplePlan("L");
					iIqcInspectionTemplateLService.updateByPrimaryKeySelective(ist, upda);
				}
				break;
			case "L":// 放宽L变更为免检K
				if (!getNMValue("L", model))
					return message;
				List<IqcInspectionL> lListl = iqcInspectionLMapper.timeItemSelectN(model);
				if (lListl.stream().filter(p -> "OK".equals(p.getAttributeInspectionRes())).count() >= model
						.getValueM()) {
					if (lListl.stream()
							.filter(p -> !"L".equals(StringUtils.isEmpty(p.getSamplePlan()) ? "N" : p.getSamplePlan()))
							.count() > 0)
						return message;
					IqcInspectionTemplateL upda = new IqcInspectionTemplateL();
					upda.setLineId(model.getTemplateLineId());
					upda.setSamplePlan("K");
					iIqcInspectionTemplateLService.updateByPrimaryKeySelective(ist, upda);
					message = "检验单的模板行的抽样方案抽样方案由\"正常检验\"变更为\"免检\"";
				}
				break;
			}
		}
		return message;
	}

	/**
	 * @description
	 * @author tianmin.wang
	 * @date 2020年2月20日
	 * @param ist
	 * @param dto
	 * @param model
	 * @return
	 */
	private String changeSamlePlanNg(IRequest ist, IqcInspectionH dto, IqcInspectionL model) {
		String message = "success";
		IqcInspectionTemplateL temSearch = new IqcInspectionTemplateL();
		temSearch.setLineId(model.getTemplateLineId());
		List<IqcInspectionTemplateL> temResult = iqcInspectionTemplateLMapper.reSelect(temSearch);
		if (temResult == null || temResult.size() == 0)
			return null;
		if (("0".equals(temResult.get(0).getSampleType()) || "1".equals(temResult.get(0).getSampleType()))
				&& (!"1".equals(temResult.get(0).getSampleProcedureType())
						&& !"3".equals(temResult.get(0).getSampleProcedureType()))) {// 抽样方式为0 1且其抽样标准不为1 3时
			switch (StringUtil.isEmpty(model.getSamplePlan()) ? "N" : model.getSamplePlan()) {
			case "S":
				break;
			case "N":// 正常N变更为加严S
				if (!getNMValue("N", model))
					return message;
				List<IqcInspectionL> lListn = iqcInspectionLMapper.timeItemSelectN(model);
				if (lListn.stream().filter(p -> "NG".equals(p.getAttributeInspectionRes())).count() >= model
						.getValueM()) {
					if (lListn.stream()
							.filter(p -> !"N".equals(StringUtils.isEmpty(p.getSamplePlan()) ? "N" : p.getSamplePlan()))
							.count() > 0)
						return message;
					IqcInspectionTemplateL upda = new IqcInspectionTemplateL();
					upda.setLineId(model.getTemplateLineId());
					upda.setSamplePlan("S");
					iIqcInspectionTemplateLService.updateByPrimaryKeySelective(ist, upda);
				}
				break;
			case "L"://
				if (!getNMValue("L", model))
					return message;
				List<IqcInspectionL> lListl = iqcInspectionLMapper.timeItemSelectN(model);

				if (lListl.stream().filter(p -> "NG".equals(p.getAttributeInspectionRes())).count() >= model
						.getValueM()) {
					if (lListl.stream()
							.filter(p -> !"L".equals(StringUtils.isEmpty(p.getSamplePlan()) ? "N" : p.getSamplePlan()))
							.count() > 0)
						return message;
					IqcInspectionTemplateL upda = new IqcInspectionTemplateL();
					upda.setLineId(model.getTemplateLineId());
					upda.setSamplePlan("N");
					iIqcInspectionTemplateLService.updateByPrimaryKeySelective(ist, upda);
				}
				break;
			}
		}
		return message;
	}

	private boolean getNMValue(String sourceSampleType, IqcInspectionL model) {
		SampleSwitchRule search = new SampleSwitchRule();
		search.setSourceSampleType(sourceSampleType);
		search.setInspectionJudge(model.getAttributeInspectionRes());
		List<SampleSwitchRule> result = sampleSwitchRuleMapper.select(search);
		if (result == null || result.size() == 0)
			return false;
		model.setValueM(Integer.valueOf(result.get(0).getSwitchRuleValueM().intValue()));
		model.setValueN(Integer.valueOf(result.get(0).getSwitchRuleValueN().intValue()));
		return true;
	}

	@Override
	public List<IqcInspectionH> getLimitCount(IqcInspectionH dto) {
		ResponseData responseData = new ResponseData();

		List<IqcInspectionH> list = iqcInspectionHMapper.getLimitCount(dto);
		return list;
	}

	@Override
	public ResponseData addNewInspection(IqcInspectionH dto, IRequest requestCtx, HttpServletRequest request) {
		// TODO 新增检验单
		IqcInspectionH dao = dto;
		ResponseData responseData = new ResponseData();
//		AslIqcControl aslSearch = new AslIqcControl();
//		aslSearch.setItemId(dao.getItemId());
//		aslSearch.setPlantId(dao.getPlantId());
//		aslSearch.setSupplierId(dao.getSupplierId());
//		aslSearch.setIsInspection("Y");
//		aslSearch.setEnableFlag("Y");
//		// 物料的iqc设置获取
//		List<AslIqcControl> aslList = aslIqcControlMapper.myselect(aslSearch);
//		if (aslList == null || aslList.size() == 0) {
//			// 未设置ASL，无法生成检验单
//			responseData.setSuccess(false);
//			responseData.setMessage(
//					SystemApiMethod.getPromptDescription(request, iPromptService, "error.hqm_iqc_order_create_04"));
//			return responseData;
//		}

		IqcInspectionTemplateH templateHeadSearch = new IqcInspectionTemplateH();
		templateHeadSearch.setItemId(dao.getItemId());
		templateHeadSearch.setPlantId(dao.getPlantId());
		templateHeadSearch.setSourceType(dao.getSourceType());
		templateHeadSearch.setEnableFlag("Y");
		// 检验单头模板查找
		List<IqcInspectionTemplateH> templateHeadList = iqcInspectionTemplateHMapper.myselect(templateHeadSearch);
		if (templateHeadList == null || templateHeadList.size() == 0) {
			// 未找到有效检验单模板，请先维护检验单模板
			responseData.setSuccess(false);
			responseData.setMessage(
					SystemApiMethod.getPromptDescription(request, iPromptService, "error.hqm_iqc_order_create_05"));
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
		scoreSearch.setItemId(dao.getItemId());
		scoreSearch.setPlantId(dao.getPlantId());
		scoreSearch.setSupplierId(dao.getSupplierId());
		List<SwitchScore> switchScoreList = switchScoreMapper.select(scoreSearch);
		if (switchScoreList == null || switchScoreList.size() == 0) {
			// 如果不存在则新增一个抽样计划
			SwitchScore scoreAdder = new SwitchScore();
			scoreAdder.setItemId(dao.getItemId());
			scoreAdder.setPlantId(dao.getPlantId());
			scoreAdder.setSupplierId(dao.getSupplierId());
			scoreAdder.setSamplePlanTypeNow("N");
			scoreAdder.setSamplePlanTypeNext("N");
			scoreAdder.setSwitchScore(0f);
			scoreAdder.setNonnconformingLot(0f);
			scoreAdder.setConsecutiveConformingLot(0f);
			scoreAdder.setChangeFlag("Y");
			scoreAdder.setSupplierSitId(getSupplierSitId(dao.getSupplierId()));
			iSwitchScoreService.self().insertSelective(requestCtx, scoreAdder);
			samplePlan = scoreAdder.getSamplePlanTypeNow();
		} else {
			samplePlan = switchScoreList.get(0).getSamplePlanTypeNow();
		}

		List<IqcInspectionL> inspectionLAdderList = new ArrayList<IqcInspectionL>();// 所有检验项模型

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
			IqcInspectionL inspectionL = new IqcInspectionL();// 新的检验项
			// 找抽样方式
			SampleManage smSearch = new SampleManage();
			smSearch.setSampleWayId(Float.valueOf(inspectionTemplateL.getSampleWayId()));
			SampleManage sampleManageResult = sampleManageMapper.selectByPrimaryKey(smSearch);
			if (dao.getReceiveQty() == 1) {
				inspectionL.setSampleSize(1f);// 抽样数量
			} else {
				switch (sampleManageResult.getSampleType()) {
				case "0":// 抽样方式为 固定数量
					// wtm-20200220 添加sample_plan字段影响
					switch (StringUtil.isEmpty(inspectionTemplateL.getSamplePlan()) ? "N"
							: inspectionTemplateL.getSamplePlan()) {
					case "N":// 正常检验 抽样数取参数值
						inspectionL.setSampleSize(Float.valueOf(sampleManageResult.getParameter()));
						break;
					case "S":// 加严检验 抽样数取加严参数值
						inspectionL.setSampleSize(Float.valueOf(sampleManageResult.getParameterS()));
						break;
					case "L":// 放宽检验 抽样数取放宽参数值
						inspectionL.setSampleSize(Float.valueOf(sampleManageResult.getParameterL()));
						break;
					}
					if (sampleSize < inspectionL.getSampleSize()) {
						sampleSize = inspectionL.getSampleSize();
					}
					break;
				case "3":// 抽样方式为 固定比例
					switch (StringUtil.isEmpty(inspectionTemplateL.getSamplePlan()) ? "N"
							: inspectionTemplateL.getSamplePlan()) {
					case "N":// 正常检验 抽样数取参数值*接收数
						inspectionL
								.setSampleSize(Float.valueOf(sampleManageResult.getParameter()) * dao.getReceiveQty());
						break;
					case "S":// 加严检验 抽样数取加严参数值*接收数
						inspectionL
								.setSampleSize(Float.valueOf(sampleManageResult.getParameterS()) * dao.getReceiveQty());
						break;
					case "L":// 放宽检验 抽样数取放宽参数值*接收数
						inspectionL
								.setSampleSize(Float.valueOf(sampleManageResult.getParameterL()) * dao.getReceiveQty());
						break;
					}
					if (sampleSize < inspectionL.getSampleSize()) {
						sampleSize = inspectionL.getSampleSize();
					}
					break;
				case "2":// 抽样方式为 100%全检
					inspectionL.setSampleSize(dto.getReceiveQty());
					if (sampleSize < inspectionL.getSampleSize()) {
						sampleSize = inspectionL.getSampleSize();
					}
					break;
				case "1":// 抽样方式为 使用抽样方案
					SampleSizeCodeLetter searchCodeLetter = new SampleSizeCodeLetter();
					searchCodeLetter.setInspectionLevels(inspectionTemplateL.getInspectionLevels());
					searchCodeLetter.setLotSizeFrom(dao.getReceiveQty());
					searchCodeLetter.setLotSizeTo(dao.getReceiveQty());
					searchCodeLetter.setSampleProcedureType(sampleManageResult.getSampleProcedureType());// 抽样标准
																											// wtm-20200220
					List<SampleSizeCodeLetter> sizeCodeResultList = new ArrayList<SampleSizeCodeLetter>();
					List<SampleScheme> sampleSchemeResultList = new ArrayList<SampleScheme>();
					switch (sampleManageResult.getSampleProcedureType()) {// 抽样标准确定抽样数量
					case "0":// 抽样标准为0时 GB2828
						sizeCodeResultList = sampleSizeCodeLetterMapper.selectCodeSizeLetter(searchCodeLetter);// 样本量字码
						if (sizeCodeResultList == null || sizeCodeResultList.size() == 0)
							throw new RuntimeException(SystemApiMethod.getPromptDescription(request, iPromptService,
									"error.hqm_iqc_order_create_07"));
						SampleScheme sampleSchemeSearch0 = new SampleScheme();
						sampleSchemeSearch0.setSampleSizeCodeLetter(sizeCodeResultList.get(0).getSizeCodeLetter());
						sampleSchemeSearch0
								.setSamplePlanType(StringUtil.isEmpty(inspectionTemplateL.getSamplePlan()) ? "N"
										: inspectionTemplateL.getSamplePlan());
						sampleSchemeSearch0.setAcceptanceQualityLimit(sampleManageResult.getAcceptanceQualityLimit());
						sampleSchemeResultList = sampleSchemeMapper.select(sampleSchemeSearch0);// 查找确定检验项的抽样数量、AC、RE值
						if (sampleSchemeResultList.get(0).getSampleSize() > dao.getReceiveQty()
								|| sampleSchemeResultList.get(0).getSampleSize() == (-1f)) {
							// 若计算出的抽样数量大于接收数量 则抽样数量取值为接收数量
							sampleSchemeResultList.get(0).setSampleSize(dao.getReceiveQty());
						}
						if (sampleSize < sampleSchemeResultList.get(0).getSampleSize()) {
							sampleSize = sampleSchemeResultList.get(0).getSampleSize();
						}
						inspectionL.setSizeCodeLetter(sizeCodeResultList.get(0).getSizeCodeLetter());// 样本量字码
						inspectionL.setSampleSize(sampleSchemeResultList.get(0).getSampleSize());// 抽样数量
						inspectionL.setAc(String.valueOf(sampleSchemeResultList.get(0).getAcValue()));// AC值
						inspectionL.setRe(String.valueOf(sampleSchemeResultList.get(0).getReValue()));// RE值
						break;
					case "1":// 抽样标准为1时 C=0
						SampleScheme sampleSchemeSearch1 = new SampleScheme();
						sampleSchemeSearch1.setSampleProcedureType(sampleManageResult.getSampleProcedureType());
						sampleSchemeSearch1.setAcceptanceQualityLimit(sampleManageResult.getAcceptanceQualityLimit());
						sampleSchemeSearch1.setSamplePlanType(samplePlan);
						sampleSchemeSearch1.setAttribute1(String.valueOf(dao.getReceiveQty()));
						sampleSchemeSearch1.setAttribute2(String.valueOf(dao.getReceiveQty()));
						sampleSchemeResultList = sampleSchemeMapper.selectSampleSize(sampleSchemeSearch1);
						if (sampleSchemeResultList == null || sampleSchemeResultList.size() == 0)
							throw new RuntimeException("C=0 hqm_sample_scheme not have data");
						if (sampleSchemeResultList.get(0).getSampleSize() > dao.getReceiveQty()
								|| sampleSchemeResultList.get(0).getSampleSize().intValue() == (-1)) {
							// 若计算出的抽样数量大于接收数量或者为-1 则抽样数量取值为生产数量
							sampleSchemeResultList.get(0).setSampleSize(dao.getReceiveQty());
						}
						if (sampleSize < sampleSchemeResultList.get(0).getSampleSize()) {
							sampleSize = sampleSchemeResultList.get(0).getSampleSize();
						}
						inspectionL.setSampleSize(sampleSchemeResultList.get(0).getSampleSize());// 抽样数量
						break;
					case "2":// 抽样标准为2时 常规检验
						inspectionL.setSampleSize(1f);
						break;
					default:
						throw new RuntimeException("抽样标准: 不满足 常规检验/C=0/GB2828");
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
			inspectionL.setDataFrom(inspectionTemplateL.getFillInType());
			inspectionL.setAcceptanceQualityLimit(inspectionTemplateL.getAcceptanceQualityLimit());
			inspectionL.setFillInType(inspectionTemplateL.getFillInType());
			inspectionL.setSampleWayId(Float.valueOf(inspectionTemplateL.getSampleWayId()));
			inspectionL.setSampleType(Float.parseFloat(sampleManageResult.getSampleType()));
			if (sampleManageResult.getParameter() != null && !StringUtils.isBlank(sampleManageResult.getParameter())) {
				inspectionL.setParameter(Float.parseFloat(sampleManageResult.getParameter()));
			}
			inspectionLAdderList.add(inspectionL);
		}
		IqcInspectionH iqcInspectionHAdder = new IqcInspectionH();
		iqcInspectionHAdder.setInspectionRes("P");
		iqcInspectionHAdder.setInspectionJudge("P");
		iqcInspectionHAdder.setApprovalResult("P");
		iqcInspectionHAdder.setSamplePlan(samplePlan);
		iqcInspectionHAdder.setInspectionNum(inspectionNum);
		iqcInspectionHAdder.setInspectionType(dao.getInspectionType());
		iqcInspectionHAdder.setPlantId(dao.getPlantId());
		iqcInspectionHAdder.setSupplierId(dao.getSupplierId());
		iqcInspectionHAdder.setSourceType(dao.getSourceType());
		iqcInspectionHAdder.setIsUrgency(isUrgency);
		iqcInspectionHAdder.setSourceOrder(dao.getSourceOrder());
		iqcInspectionHAdder.setVersionNum(templateHeadList.get(0).getVersionNum());
		iqcInspectionHAdder.setTemplateHeadId(templateHeadList.get(0).getHeaderId());
		iqcInspectionHAdder.setItemId(dao.getItemId());
		iqcInspectionHAdder.setReceiveQty(dao.getReceiveQty());
		iqcInspectionHAdder.setProductionBatch(dao.getProductionBatch());
		iqcInspectionHAdder.setInspectionStatus("2");
		iqcInspectionHAdder.setSampleSize(sampleSize);
		iqcInspectionHAdder.setReceiveDate(dao.getReceiveDate());
		iqcInspectionHAdder.setTaskFrom(dao.getTaskFrom());
		// iqcInspectionHAdder.setInspectionDate(new Date());
		iqcInspectionHAdder.setPlanTime(getPlanTime(templateHeadList.get(0).getTimeLimit()));
		iqcInspectionHAdder.setScoreFlag(dao.getSourceType().equals("0") ? "Y" : "N");
		// 接收人
		iqcInspectionHAdder.setRecipientId(dao.getRecipientId());
		// 检验人
		if (dao.getInspectorId() != null) {
			iqcInspectionHAdder.setInspectorId(dao.getInspectorId());
		}

		/**
		 * added by wtm 20191218 物料版本ItemEdition字段新增
		 */
		iqcInspectionHAdder.setItemEdition(dao.getItemEdition());

		// 生成检验单头
		iqcInspectionHMapper.insertSelective(iqcInspectionHAdder);
		for (IqcInspectionL ldto : inspectionLAdderList) {
			// 生成检验单行
			ldto.setHeaderId(iqcInspectionHAdder.getHeaderId());
			iqcInspectionLMapper.insertSelective(ldto);
			iIqcInspectionDService.batchCreateByInspectionL(requestCtx, ldto);
		}
		List<IqcInspectionH> rows = new ArrayList<IqcInspectionH>();
		rows.add(iqcInspectionHAdder);
		responseData.setRows(rows);
		return responseData;
	}

	/**
	 * 
	 * @description 计划完成时间
	 * @author tianmin.wang
	 * @date 2019年11月22日
	 * @param timeLimit
	 * @return
	 */
	public Date getPlanTime(Float timeLimit) {
		Date currentDate = new Date();
		if (timeLimit == null)
			return null;
		// 计算计划完成时间
		Date returnDate = new Date(currentDate.getTime() + (timeLimit.intValue()) * 60 * 60 * 1000);
		return returnDate;
	}

	/**
	 * 
	 * @description 供应商地点ID
	 * @author tianmin.wang
	 * @date 2019年11月22日
	 * @param suplierId
	 * @return
	 */
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
		IqcInspectionH sr = new IqcInspectionH();
		sr.setInspectionNum(inspectionNumStart);
		List<IqcInspectionH> list = new ArrayList<IqcInspectionH>();
		list = iqcInspectionHMapper.selectMaxNumber(sr);
		if (list != null && list.size() > 0) {
			String inspectionNum = list.get(0).getInspectionNum();
			String number = inspectionNum.substring(inspectionNum.length() - 5);// 流水
			count = Integer.valueOf(number) + 1;
		}
		String str = String.format("%05d", count);
		return inspectionNumStart + str;// 最终检验单号
	}

	@Override
	public List<IqcInspectionH> qmsQuery(IRequest requestContext, IqcInspectionH dto, int page, int pageSize) {
		PageHelper.startPage(page, pageSize);
		return iqcInspectionHMapper.qmsQuery(dto);
	}

	@Override
	public List<IqcInspectionH> qmsQueryAll(IRequest requestContext, IqcInspectionH dto, int page, int pageSize) {
		// TODO Auto-generated method stub
		PageHelper.startPage(page, pageSize);
		List<IqcInspectionH> iqcInspectionHList = new ArrayList();
		if ("IQC".equals(dto.getInspectionType())) {
			iqcInspectionHList = iqcInspectionHMapper.qmsQuery(dto);
		} else if ("PQC".equals(dto.getInspectionType())) {
			iqcInspectionHList = iqcInspectionHMapper.qmsPqcQuery(dto);
		} else if ("FQC".equals(dto.getInspectionType()) || "OQC".equals(dto.getInspectionType())) {
			iqcInspectionHList = iqcInspectionHMapper.qmsFqcQuery(dto);
		}
		return iqcInspectionHList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hand.hqm.hqm_iqc_inspection_h.service.IIqcInspectionHService#
	 * addNewInspection(com.hand.hqm.hqm_iqc_inspection_h.dto.IqcInspectionH,
	 * com.hand.hqm.hqm_qc_task.dto.IqcTask, javax.servlet.http.HttpServletRequest)
	 */
	@Override
	public void addNewInspection(IqcTask iqctask, IqcInspectionH dto, HttpServletRequest request) {
		IRequest requestCtx;
		if (request == null) {
			ServiceRequest sr = new ServiceRequest();
			sr.setLocale("zh_CN");
			sr.setUserId(-1l);
			requestCtx = sr;
		} else {
			requestCtx = RequestHelper.createServiceRequest(request);
		}

		IqcInspectionH dao = dto;
//		AslIqcControl aslSearch = new AslIqcControl();
//		aslSearch.setItemId(dao.getItemId());
//		aslSearch.setPlantId(dao.getPlantId());
//		aslSearch.setSupplierId(dao.getSupplierId());
//		aslSearch.setIsInspection("Y");
//		aslSearch.setEnableFlag("Y");
//		// 物料的iqc设置获取
//		List<AslIqcControl> aslList = aslIqcControlMapper.myselect(aslSearch);
//		if (aslList == null || aslList.size() == 0) {
//			// 未设置ASL，无法生成检验单
//			throw new RuntimeException(
//					SystemApiMethod.getPromptDescription(request, iPromptService, "error.hqm_iqc_order_create_04"));
//		}

		IqcInspectionTemplateH templateHeadSearch = new IqcInspectionTemplateH();
		templateHeadSearch.setItemId(dao.getItemId());
		templateHeadSearch.setPlantId(dao.getPlantId());
		templateHeadSearch.setSourceType(dao.getSourceType());
		templateHeadSearch.setEnableFlag("Y");
		// 检验单头模板查找
		List<IqcInspectionTemplateH> templateHeadList = iqcInspectionTemplateHMapper.myselect(templateHeadSearch);
		if (templateHeadList == null || templateHeadList.size() == 0) {
			// 未找到有效检验单模板，请先维护检验单模板
			throw new RuntimeException(
					SystemApiMethod.getPromptDescription(request, iPromptService, "error.hqm_iqc_order_create_05"));
		}
		if (!"4".equals(templateHeadList.get(0).getStatus())) {// 检验单模板已发布之后的状态为 4
			throw new RuntimeException(
					SystemApiMethod.getPromptDescription(request, iPromptService, "error.hqm_iqc_order_create_1001"));
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
		scoreSearch.setItemId(dao.getItemId());
		scoreSearch.setPlantId(dao.getPlantId());
		scoreSearch.setSupplierId(dao.getSupplierId());
		List<SwitchScore> switchScoreList = switchScoreMapper.select(scoreSearch);
		if (switchScoreList == null || switchScoreList.size() == 0) {
			// 如果不存在则新增一个抽样计划
			SwitchScore scoreAdder = new SwitchScore();
			scoreAdder.setItemId(dao.getItemId());
			scoreAdder.setPlantId(dao.getPlantId());
			scoreAdder.setSupplierId(dao.getSupplierId());
			scoreAdder.setSamplePlanTypeNow("N");
			scoreAdder.setSamplePlanTypeNext("N");
			scoreAdder.setSwitchScore(0f);
			scoreAdder.setNonnconformingLot(0f);
			scoreAdder.setConsecutiveConformingLot(0f);
			scoreAdder.setChangeFlag("Y");
			scoreAdder.setSupplierSitId(getSupplierSitId(dao.getSupplierId()));
			iSwitchScoreService.self().insertSelective(requestCtx, scoreAdder);
			samplePlan = scoreAdder.getSamplePlanTypeNow();
		} else {
			samplePlan = switchScoreList.get(0).getSamplePlanTypeNow();
		}

		List<IqcInspectionL> inspectionLAdderList = new ArrayList<IqcInspectionL>();// 所有检验项模型

		// 检验项数据构建
		Float sampleSize = 1f;// 头的抽样数量

		/**
		 * 
		 * 若表中的task_from为3 4 生成检验单行表的数据时
		 * 通过hqm_iqc_task关联检验单rel_order和hqm_iqc_inspection_h的inspection_num关联,
		 * 在通过hqm_iqc_inspection_h表的头ID字段header_id跟hqm_iqc_inspection_l的header_id进行关联;
		 * 找到原有检验单的所有检验项目,取其中attribute_inspection_res为 NG 的检验项目作为当前生成的检验单的检验项
		 */
		if ("3".equals(iqctask.getTaskFrom()) || "4".equals(iqctask.getTaskFrom())) {
			sampleSize = inspectionLGenerateOr(iqctask, templateLineList, inspectionLAdderList, dao, dto, samplePlan);// 检验项数据构建
		} else {
			sampleSize = inspectionLGenerate(templateLineList, inspectionLAdderList, dao, dto, samplePlan);// 检验项数据构建
		}

		IqcInspectionH iqcInspectionHAdder = new IqcInspectionH();
		iqcInspectionHAdder.setInspectionRes("P");
		iqcInspectionHAdder.setInspectionJudge("P");
		iqcInspectionHAdder.setApprovalResult("P");
		iqcInspectionHAdder.setSamplePlan(samplePlan);
		iqcInspectionHAdder.setInspectionNum(inspectionNum);
		iqcInspectionHAdder.setInspectionType(dao.getInspectionType());
		iqcInspectionHAdder.setPlantId(dao.getPlantId());
		iqcInspectionHAdder.setSupplierId(dao.getSupplierId());
		iqcInspectionHAdder.setSourceType(dao.getSourceType());
		iqcInspectionHAdder.setIsUrgency(isUrgency);
		iqcInspectionHAdder.setSourceOrder(dao.getSourceOrder());
		iqcInspectionHAdder.setVersionNum(templateHeadList.get(0).getVersionNum());
		iqcInspectionHAdder.setTemplateHeadId(templateHeadList.get(0).getHeaderId());
		iqcInspectionHAdder.setItemId(dao.getItemId());
		iqcInspectionHAdder.setReceiveQty(dao.getReceiveQty());
		iqcInspectionHAdder.setProductionBatch(dao.getProductionBatch());
		iqcInspectionHAdder.setInspectionStatus("2");
		iqcInspectionHAdder.setSampleSize(sampleSize);
		iqcInspectionHAdder.setReceiveDate(dao.getReceiveDate());
		iqcInspectionHAdder.setTaskFrom(dao.getTaskFrom());
		// iqcInspectionHAdder.setInspectionDate(new Date());
		iqcInspectionHAdder.setPlanTime(getPlanTime(templateHeadList.get(0).getTimeLimit()));
		iqcInspectionHAdder.setScoreFlag(dao.getSourceType().equals("0") ? "Y" : "N");
		// 接收人
		iqcInspectionHAdder.setRecipientId(dao.getRecipientId());
		// 检验人
		if (dao.getInspectorId() != null) {
			iqcInspectionHAdder.setInspectorId(dao.getInspectorId());
		}

		/**
		 * added by wtm 20191218 物料版本ItemEdition字段新增
		 */
		iqcInspectionHAdder.setItemEdition(dao.getItemEdition());
		// 生成检验单头
		iqcInspectionHMapper.insertSelective(iqcInspectionHAdder);
		for (IqcInspectionL ldto : inspectionLAdderList) {
			// 生成检验单行
			ldto.setHeaderId(iqcInspectionHAdder.getHeaderId());
			iqcInspectionLMapper.insertSelective(ldto);
			iIqcInspectionDService.batchCreateByInspectionL(requestCtx, ldto);
		}
		dto.setInspectionNum(iqcInspectionHAdder.getInspectionNum());
	}

	/**
	 * @description 通过当前检验单 的 NG项生成数据
	 * @author tianmin.wang
	 * @date 2020年1月7日
	 * @param templateLineList
	 * @param inspectionLAdderList
	 * @param dao
	 * @param dto
	 * @param samplePlan
	 * @return
	 */
	private Float inspectionLGenerateOr(IqcTask iqctask, List<IqcInspectionTemplateL> templateLineList,
			List<IqcInspectionL> inspectionLAdderList, IqcInspectionH dao, IqcInspectionH dto, String samplePlan) {
		Float sampleSize = 1f;// 头的抽样数量
		IqcInspectionH inshSearch = new IqcInspectionH();
		inshSearch.setInspectionNum(iqctask.getRelOrder());
		List<IqcInspectionH> relOrderSelect = mapper.select(inshSearch);
		if (relOrderSelect != null && relOrderSelect.size() > 0) {
			IqcInspectionL inslSearch = new IqcInspectionL();
			inslSearch.setHeaderId(relOrderSelect.get(0).getHeaderId());
			inslSearch.setAttributeInspectionRes("NG");
			List<IqcInspectionL> selects = iqcInspectionLMapper.select(inslSearch);
			for (IqcInspectionL inspectionL : selects) {
				switch (iqctask.getRecheckSampleWay() == null ? "" : iqctask.getRecheckSampleWay()) {
				case "N":// 按照现有逻辑生成检验项的抽样数sample_size
					break;
				case "S":// 按照物料的抽样方案为S，再生成sample_size
					// 找抽样方式
					SampleManage smSearch = new SampleManage();
					smSearch.setSampleWayId(Float.valueOf(inspectionL.getSampleWayId()));
					SampleManage sampleManageResult = sampleManageMapper.selectByPrimaryKey(smSearch);
					if (dao.getReceiveQty() == 1) {
						inspectionL.setSampleSize(1f);// 抽样数量
					} else {
						switch (sampleManageResult.getSampleType()) {
						case "0":
							// wtm-20200220 添加sample_plan字段影响
							switch (StringUtil.isEmpty(inspectionL.getSamplePlan()) ? "N"
									: inspectionL.getSamplePlan()) {
							case "N":// 正常检验 抽样数取参数值
								inspectionL.setSampleSize(Float.valueOf(sampleManageResult.getParameter()));
								break;
							case "S":// 加严检验 抽样数取加严参数值
								inspectionL.setSampleSize(Float.valueOf(sampleManageResult.getParameterS()));
								break;
							case "L":// 放宽检验 抽样数取放宽参数值
								inspectionL.setSampleSize(Float.valueOf(sampleManageResult.getParameterL()));
								break;
							}
							// inspectionL.setSampleSize(Float.valueOf(sampleManageResult.getParameter()));
							if (sampleSize < inspectionL.getSampleSize()) {
								sampleSize = inspectionL.getSampleSize();
							}
							break;
						case "3":// 抽样方式为 固定比例
							switch (StringUtil.isEmpty(inspectionL.getSamplePlan()) ? "N"
									: inspectionL.getSamplePlan()) {
							case "N":// 正常检验 抽样数取参数值*接收数
								inspectionL.setSampleSize(
										Float.valueOf(sampleManageResult.getParameter()) * dao.getReceiveQty());
								break;
							case "S":// 加严检验 抽样数取加严参数值*接收数
								inspectionL.setSampleSize(
										Float.valueOf(sampleManageResult.getParameterS()) * dao.getReceiveQty());
								break;
							case "L":// 放宽检验 抽样数取放宽参数值*接收数
								inspectionL.setSampleSize(
										Float.valueOf(sampleManageResult.getParameterL()) * dao.getReceiveQty());
								break;
							}
							if (sampleSize < inspectionL.getSampleSize()) {
								sampleSize = inspectionL.getSampleSize();
							}
							break;
						case "2":
							inspectionL.setSampleSize(dto.getReceiveQty());
							if (sampleSize < inspectionL.getSampleSize()) {
								sampleSize = inspectionL.getSampleSize();
							}
							break;
						case "1":
							SampleSizeCodeLetter searchCodeLetter = new SampleSizeCodeLetter();
							searchCodeLetter.setInspectionLevels(sampleManageResult.getInspectionLevels());
							searchCodeLetter.setLotSizeFrom(dao.getReceiveQty());
							searchCodeLetter.setLotSizeTo(dao.getReceiveQty());
							searchCodeLetter.setSampleProcedureType(sampleManageResult.getSampleProcedureType());// 抽样标准wtm-20200220
							// 查找对应的样本量字码
							List<SampleSizeCodeLetter> sizeCodeResultList = new ArrayList<SampleSizeCodeLetter>();
							List<SampleScheme> sampleSchemeResultList = new ArrayList<SampleScheme>();
							// 抽样标准确定抽样数量
							switch (sampleManageResult.getSampleProcedureType()) {
							case "0":
								// 抽样标准为0时
								sizeCodeResultList = sampleSizeCodeLetterMapper.selectCodeSizeLetter(searchCodeLetter);
								if (sizeCodeResultList == null || sizeCodeResultList.size() == 0)
									throw new RuntimeException("error.hqm_iqc_order_create_07");
								SampleScheme sampleSchemeSearch0 = new SampleScheme();
								sampleSchemeSearch0
										.setSampleSizeCodeLetter(sizeCodeResultList.get(0).getSizeCodeLetter());
								sampleSchemeSearch0
										.setSamplePlanType(StringUtil.isEmpty(inspectionL.getSamplePlan()) ? "N"
												: inspectionL.getSamplePlan());
								sampleSchemeSearch0
										.setAcceptanceQualityLimit(sampleManageResult.getAcceptanceQualityLimit());
								// 查找确定检验项的抽样数量、AC、RE值
								sampleSchemeResultList = sampleSchemeMapper.select(sampleSchemeSearch0);
								if (sampleSchemeResultList.get(0).getSampleSize() > dao.getReceiveQty()
										|| sampleSchemeResultList.get(0).getSampleSize() == (-1f)) {
									// 若计算出的抽样数量大于接收数量 则抽样数量取值为接收数量
									sampleSchemeResultList.get(0).setSampleSize(dao.getReceiveQty());
								}
								if (sampleSize < sampleSchemeResultList.get(0).getSampleSize()) {
									sampleSize = sampleSchemeResultList.get(0).getSampleSize();
								}
								inspectionL.setSizeCodeLetter(sizeCodeResultList.get(0).getSizeCodeLetter());// 样本量字码
								inspectionL.setSampleSize(sampleSchemeResultList.get(0).getSampleSize());// 抽样数量
								inspectionL.setAc(String.valueOf(sampleSchemeResultList.get(0).getAcValue()));// AC值
								inspectionL.setRe(String.valueOf(sampleSchemeResultList.get(0).getReValue()));// RE值
								break;
							case "1":
								// 抽样标准为1时
								SampleScheme sampleSchemeSearch1 = new SampleScheme();
								sampleSchemeSearch1.setSampleProcedureType(sampleManageResult.getSampleProcedureType());
								sampleSchemeSearch1
										.setAcceptanceQualityLimit(sampleManageResult.getAcceptanceQualityLimit());
								sampleSchemeSearch1.setSamplePlanType(samplePlan);
								sampleSchemeSearch1.setAttribute1(String.valueOf(dao.getReceiveQty()));
								sampleSchemeSearch1.setAttribute2(String.valueOf(dao.getReceiveQty()));
								sampleSchemeResultList = sampleSchemeMapper.selectSampleSize(sampleSchemeSearch1);
								if (sampleSchemeResultList == null || sampleSchemeResultList.size() == 0)
									throw new RuntimeException("C=0 hqm_sample_scheme not have data");
								if (sampleSchemeResultList.get(0).getSampleSize() > dao.getReceiveQty()
										|| sampleSchemeResultList.get(0).getSampleSize().intValue() == (-1)) {
									// 若计算出的抽样数量大于接收数量或者为-1 则抽样数量取值为生产数量
									sampleSchemeResultList.get(0).setSampleSize(dao.getReceiveQty());
								}
								if (sampleSize < sampleSchemeResultList.get(0).getSampleSize()) {
									sampleSize = sampleSchemeResultList.get(0).getSampleSize();
								}
								inspectionL.setSampleSize(sampleSchemeResultList.get(0).getSampleSize());// 抽样数量
								break;
							case "2":
								// 抽样标准为2时
								inspectionL.setSampleSize(1f);
								break;
							}

							break;
						}
					}
					break;
				case "A":// 抽样数量sample_size直接取HQM_IQC_TASK表中中的receive_qty
					inspectionL.setSampleSize(iqctask.getSampleQty());
					break;
				}
				if (inspectionL.getSampleSize() >= sampleSize) {
					sampleSize = inspectionL.getSampleSize();
				}
				inspectionL.setLineId(null);
				inspectionLAdderList.add(inspectionL);
			}
		} else {
			throw new RuntimeException("not have rel order data");
		}

		return sampleSize;
	}

	Float inspectionLGenerate(List<IqcInspectionTemplateL> templateLineList, List<IqcInspectionL> inspectionLAdderList,
			IqcInspectionH dao, IqcInspectionH dto, String samplePlan) {
		Float sampleSize = 1f;// 头的抽样数量
		for (IqcInspectionTemplateL inspectionTemplateL : templateLineList) {
			IqcInspectionL inspectionL = new IqcInspectionL();// 新的检验项
			// 找抽样方式
			SampleManage smSearch = new SampleManage();
			smSearch.setSampleWayId(Float.valueOf(inspectionTemplateL.getSampleWayId()));
			SampleManage sampleManageResult = sampleManageMapper.selectByPrimaryKey(smSearch);
			if (dao.getReceiveQty() == 1) {
				inspectionL.setSampleSize(1f);// 抽样数量
			} else {
				switch (sampleManageResult.getSampleType()) {
				case "0":
					// wtm-20200220 添加sample_plan字段影响
					switch (StringUtil.isEmpty(inspectionTemplateL.getSamplePlan()) ? "N"
							: inspectionTemplateL.getSamplePlan()) {
					case "N":// 正常检验 抽样数取参数值
						if (sampleManageResult.getParameter() == null)
							throw new RuntimeException(SystemApiMethod.getPromptDescription("error.hqm_sample_plan01"));
						inspectionL.setSampleSize(Float.valueOf(sampleManageResult.getParameter()));
						break;
					case "S":// 加严检验 抽样数取加严参数值
						if (sampleManageResult.getParameterS() == null)
							throw new RuntimeException(SystemApiMethod.getPromptDescription("error.hqm_sample_plan01"));
						inspectionL.setSampleSize(Float.valueOf(sampleManageResult.getParameterS()));
						break;
					case "L":// 放宽检验 抽样数取放宽参数值
						if (sampleManageResult.getParameterL() == null)
							throw new RuntimeException(SystemApiMethod.getPromptDescription("error.hqm_sample_plan01"));
						inspectionL.setSampleSize(Float.valueOf(sampleManageResult.getParameterL()));
						break;
					}
					if (sampleSize < inspectionL.getSampleSize()) {
						sampleSize = inspectionL.getSampleSize();
					}
					break;
				case "3":
					switch (StringUtil.isEmpty(inspectionTemplateL.getSamplePlan()) ? "N"
							: inspectionTemplateL.getSamplePlan()) {
					case "N":// 正常检验 抽样数取参数值*接收数
						if (sampleManageResult.getParameter() == null)
							throw new RuntimeException(SystemApiMethod.getPromptDescription("error.hqm_sample_plan01"));
						inspectionL
								.setSampleSize(Float.valueOf(sampleManageResult.getParameter()) * dao.getReceiveQty());
						break;
					case "S":// 加严检验 抽样数取加严参数值*接收数
						if (sampleManageResult.getParameterS() == null)
							throw new RuntimeException(SystemApiMethod.getPromptDescription("error.hqm_sample_plan01"));
						inspectionL
								.setSampleSize(Float.valueOf(sampleManageResult.getParameterS()) * dao.getReceiveQty());
						break;
					case "L":// 放宽检验 抽样数取放宽参数值*接收数
						if (sampleManageResult.getParameterL() == null)
							throw new RuntimeException(SystemApiMethod.getPromptDescription("error.hqm_sample_plan01"));
						inspectionL
								.setSampleSize(Float.valueOf(sampleManageResult.getParameterL()) * dao.getReceiveQty());
						break;
					}
					break;
				case "2":
					inspectionL.setSampleSize(dto.getReceiveQty());
					if (sampleSize < inspectionL.getSampleSize()) {
						sampleSize = inspectionL.getSampleSize();
					}
					break;
				case "1":
					SampleSizeCodeLetter searchCodeLetter = new SampleSizeCodeLetter();
					searchCodeLetter.setInspectionLevels(inspectionTemplateL.getInspectionLevels());
					searchCodeLetter.setLotSizeFrom(dao.getReceiveQty());
					searchCodeLetter.setLotSizeTo(dao.getReceiveQty());
					searchCodeLetter.setSampleProcedureType(sampleManageResult.getSampleProcedureType());// 抽样标准
					// wtm-20200220
					// 查找对应的样本量字码
					List<SampleSizeCodeLetter> sizeCodeResultList = new ArrayList<SampleSizeCodeLetter>();
					List<SampleScheme> sampleSchemeResultList = new ArrayList<SampleScheme>();
					// 抽样标准确定抽样数量
					switch (sampleManageResult.getSampleProcedureType()) {
					case "0":// 抽样标准为0时

						sizeCodeResultList = sampleSizeCodeLetterMapper.selectCodeSizeLetter(searchCodeLetter);
						if (sizeCodeResultList == null || sizeCodeResultList.size() == 0)
							throw new RuntimeException("error.hqm_iqc_order_create_07");
						SampleScheme sampleSchemeSearch0 = new SampleScheme();
						sampleSchemeSearch0.setSampleSizeCodeLetter(sizeCodeResultList.get(0).getSizeCodeLetter());
						sampleSchemeSearch0.setSamplePlanType(samplePlan);
						sampleSchemeSearch0
								.setSamplePlanType(StringUtil.isEmpty(inspectionTemplateL.getSamplePlan()) ? "N"
										: inspectionTemplateL.getSamplePlan());
						sampleSchemeSearch0.setAcceptanceQualityLimit(sampleManageResult.getAcceptanceQualityLimit());
						// 查找确定检验项的抽样数量、AC、RE值
						sampleSchemeResultList = sampleSchemeMapper.select(sampleSchemeSearch0);
						if (sampleSchemeResultList.get(0).getSampleSize() > dao.getReceiveQty()
								|| sampleSchemeResultList.get(0).getSampleSize() == (-1f)) {
							// 若计算出的抽样数量大于接收数量 则抽样数量取值为接收数量
							sampleSchemeResultList.get(0).setSampleSize(dao.getReceiveQty());
						}
						if (sampleSize < sampleSchemeResultList.get(0).getSampleSize()) {
							sampleSize = sampleSchemeResultList.get(0).getSampleSize();
						}
						inspectionL.setSizeCodeLetter(sizeCodeResultList.get(0).getSizeCodeLetter());// 样本量字码
						inspectionL.setSampleSize(sampleSchemeResultList.get(0).getSampleSize());// 抽样数量
						inspectionL.setAc(String.valueOf(sampleSchemeResultList.get(0).getAcValue()));// AC值
						inspectionL.setRe(String.valueOf(sampleSchemeResultList.get(0).getReValue()));// RE值
						break;
					case "1":// 抽样标准为1时

						SampleScheme sampleSchemeSearch1 = new SampleScheme();
						sampleSchemeSearch1.setSampleProcedureType(sampleManageResult.getSampleProcedureType());
						sampleSchemeSearch1.setAcceptanceQualityLimit(sampleManageResult.getAcceptanceQualityLimit());
						sampleSchemeSearch1.setSamplePlanType(samplePlan);
						sampleSchemeSearch1.setAttribute1(String.valueOf(dao.getReceiveQty()));
						sampleSchemeSearch1.setAttribute2(String.valueOf(dao.getReceiveQty()));
						sampleSchemeResultList = sampleSchemeMapper.selectSampleSize(sampleSchemeSearch1);
						if (sampleSchemeResultList == null || sampleSchemeResultList.size() == 0)
							throw new RuntimeException("C=0 hqm_sample_scheme not have data");
						if (sampleSchemeResultList.get(0).getSampleSize() > dao.getReceiveQty()
								|| sampleSchemeResultList.get(0).getSampleSize().intValue() == (-1)) {
							// 若计算出的抽样数量大于接收数量或者为-1 则抽样数量取值为生产数量
							sampleSchemeResultList.get(0).setSampleSize(dao.getReceiveQty());
						}
						if (sampleSize < sampleSchemeResultList.get(0).getSampleSize()) {
							sampleSize = sampleSchemeResultList.get(0).getSampleSize();
						}
						inspectionL.setSampleSize(sampleSchemeResultList.get(0).getSampleSize());// 抽样数量
						break;
					case "2": // 抽样标准为2时

						inspectionL.setSampleSize(1f);
						break;
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
			inspectionL.setDataFrom(inspectionTemplateL.getFillInType());
			inspectionL.setAcceptanceQualityLimit(inspectionTemplateL.getAcceptanceQualityLimit());
			inspectionL.setFillInType(inspectionTemplateL.getFillInType());
			inspectionL.setSampleWayId(Float.valueOf(inspectionTemplateL.getSampleWayId()));
			inspectionL.setSampleType(Float.parseFloat(sampleManageResult.getSampleType()));
			if (sampleManageResult.getParameter() != null && !StringUtils.isBlank(sampleManageResult.getParameter())) {
				inspectionL.setParameter(Float.parseFloat(sampleManageResult.getParameter()));
			}
			inspectionLAdderList.add(inspectionL);
		} // 检验项数据构建
		return sampleSize;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void samplingToWms(ResponseData rsd) {
		IfInvokeOutbound iio = new IfInvokeOutbound();
		List<IqcInspectionH> inspectionHs = (List<IqcInspectionH>) rsd.getRows();
		IqcInspectionH inspectionH = inspectionHs.get(0);
		SamplingNumInfo samplingNumInfo = new SamplingNumInfo();

		// 查询工厂id
		Plant plant = new Plant();
		plant.setPlantId(inspectionH.getPlantId());
		List<Plant> plantList = plantMapper.select(plant);
		if (plantList != null && plantList.size() > 0) {
			samplingNumInfo.setPlantCode(plantList.get(0).getPlantCode());
		} else {
			// 工厂编码在工厂表中找不到！
			logger.warn("工厂编码在工厂表中找不到！");
			return;
		}

		// 关联检验通知表查询
		IqcTask iqcTask = new IqcTask();
		iqcTask.setInspectionNum(inspectionH.getInspectionNum());
		List<IqcTask> iqcTasks = iqcTaskMapper.select(iqcTask);
		if (iqcTasks != null && iqcTasks.size() > 0) {
			IqcTask iqcTaskR = iqcTasks.get(0);
			samplingNumInfo.setSourceOrder(iqcTaskR.getSourceOrder());
			samplingNumInfo.setLineNum(iqcTaskR.getLineNum());
			samplingNumInfo.setItemCode(iqcTaskR.getItemCode());
			samplingNumInfo.setItemVersion(iqcTaskR.getItemVersion());
			samplingNumInfo.setSpreading(iqcTaskR.getSpreading());
			samplingNumInfo.setLotNumber(iqcTaskR.getProductionBatch());
			samplingNumInfo.setIsUrgency(iqcTaskR.getIsUrgency());
			// samplingNumInfo.setSupplierCode(iqcTaskR.getSupplierCode());
			// samplingNumInfo.setPackQty(iqcTaskR.getPackQty());
			// samplingNumInfo.setPacketInfo(iqcTaskR.getPacketInfo());
			// samplingNumInfo.setPoNumber(Float.toString(iqcTaskR.getPoNumber()));
			samplingNumInfo.setInspectionPlace(iqcTaskR.getInspectionPlace());
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			samplingNumInfo.setEvenTime(sdf.format(iqcTaskR.getReceiveDate()));
			// samplingNumInfo.setWarehouseType(iqcTaskR.getWareHouseType());

			samplingNumInfo.setInspectionNum(iqcTaskR.getInspectionNum());
		} else {
			// 无法关联检验通知表
			logger.warn("无法关联检验通知表！");
			return;
		}
		samplingNumInfo.setReceiveQty(inspectionH.getSampleSize());
		// 确定 免检标识的值 exemptFlag
		SuppItemExemption sieSearch = new SuppItemExemption();
		sieSearch.setItemId(inspectionH.getItemId());
		List<SuppItemExemption> resu = suppItemExemptionMapper.select(sieSearch);
		if (resu != null && resu.size() > 0) {
			if (resu.get(0).getExemptionTimeFrom() != null && resu.get(0).getExemptionTimeTo() != null
					&& resu.get(0).getExemptionTimeFrom().before(new Date())
					&& resu.get(0).getExemptionTimeTo().after(new Date())) {
				samplingNumInfo.setExemptFlag("1");
			} else {
				samplingNumInfo.setExemptFlag("0");
			}
		} else {
			samplingNumInfo.setExemptFlag("0");
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
					post = omapper.writeValueAsString(samplingNumInfo);
					SoapPostUtil.Response re = SoapPostUtil.ticketSrmToWms("getSampleQty", post, iio, uri);
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

}