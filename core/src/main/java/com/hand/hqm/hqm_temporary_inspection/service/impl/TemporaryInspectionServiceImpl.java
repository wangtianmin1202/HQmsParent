package com.hand.hqm.hqm_temporary_inspection.service.impl;

import com.github.pagehelper.PageHelper;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.service.IPromptService;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import com.hand.hap.util.SystemApiMethod;
import com.hand.hcs.hcs_suppliers.dto.Suppliers;
import com.hand.hcs.hcs_suppliers.mapper.SuppliersMapper;
import com.hand.hcs.hcs_supply_plan.dto.SupplyPlan;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hand.hqm.hqm_fqc_inspection_d.service.IFqcInspectionDService;
import com.hand.hqm.hqm_fqc_inspection_h.dto.FqcInspectionH;
import com.hand.hqm.hqm_fqc_inspection_h.mapper.FqcInspectionHMapper;
import com.hand.hqm.hqm_fqc_inspection_l.dto.FqcInspectionL;
import com.hand.hqm.hqm_fqc_inspection_l.mapper.FqcInspectionLMapper;
import com.hand.hqm.hqm_fqc_inspection_l.service.IFqcInspectionLService;
import com.hand.hqm.hqm_inspection_attribute.dto.InspectionAttribute;
import com.hand.hqm.hqm_inspection_attribute.mapper.InspectionAttributeMapper;
import com.hand.hqm.hqm_iqc_inspection_d.service.IIqcInspectionDService;
import com.hand.hqm.hqm_iqc_inspection_h.dto.IqcInspectionH;
import com.hand.hqm.hqm_iqc_inspection_h.mapper.IqcInspectionHMapper;
import com.hand.hqm.hqm_iqc_inspection_l.dto.IqcInspectionL;
import com.hand.hqm.hqm_iqc_inspection_l.mapper.IqcInspectionLMapper;
import com.hand.hqm.hqm_iqc_inspection_l.service.IIqcInspectionLService;
import com.hand.hqm.hqm_iqc_inspection_template_h.dto.IqcInspectionTemplateH;
import com.hand.hqm.hqm_iqc_inspection_template_h.mapper.IqcInspectionTemplateHMapper;
import com.hand.hqm.hqm_iqc_inspection_template_l.dto.IqcInspectionTemplateL;
import com.hand.hqm.hqm_iqc_inspection_template_l.mapper.IqcInspectionTemplateLMapper;
import com.hand.hqm.hqm_iqc_inspection_template_l.service.IIqcInspectionTemplateLService;
import com.hand.hqm.hqm_platform_program.dto.PlatformProgram;
import com.hand.hqm.hqm_platform_program.mapper.PlatformProgramMapper;
import com.hand.hqm.hqm_pqc_inspection_c.dto.PqcInspectionC;
import com.hand.hqm.hqm_pqc_inspection_c.mapper.PqcInspectionCMapper;
import com.hand.hqm.hqm_pqc_inspection_d.mapper.PqcInspectionDMapper;
import com.hand.hqm.hqm_pqc_inspection_d.service.IPqcInspectionDService;
import com.hand.hqm.hqm_pqc_inspection_h.dto.PqcInspectionH;
import com.hand.hqm.hqm_pqc_inspection_h.mapper.PqcInspectionHMapper;
import com.hand.hqm.hqm_pqc_inspection_l.dto.PqcInspectionL;
import com.hand.hqm.hqm_pqc_inspection_l.mapper.PqcInspectionLMapper;
import com.hand.hqm.hqm_sample_manage.dto.SampleManage;
import com.hand.hqm.hqm_sample_manage.mapper.SampleManageMapper;
import com.hand.hqm.hqm_sample_scheme.dto.SampleScheme;
import com.hand.hqm.hqm_sample_scheme.mapper.SampleSchemeMapper;
import com.hand.hqm.hqm_sample_size_code_letter.dto.SampleSizeCodeLetter;
import com.hand.hqm.hqm_sample_size_code_letter.mapper.SampleSizeCodeLetterMapper;
import com.hand.hqm.hqm_stan_op_item_h.dto.StandardOpItemH;
import com.hand.hqm.hqm_stan_op_item_h.mapper.StandardOpItemHMapper;
import com.hand.hqm.hqm_stan_op_item_l.dto.StandardOpItemL;
import com.hand.hqm.hqm_stan_op_item_l.mapper.StandardOpItemLMapper;
import com.hand.hqm.hqm_stan_op_item_l.service.IStandardOpItemLService;
import com.hand.hqm.hqm_switch_score.dto.SwitchScore;
import com.hand.hqm.hqm_switch_score.mapper.SwitchScoreMapper;
import com.hand.hqm.hqm_switch_score.service.ISwitchScoreService;
import com.hand.hqm.hqm_temporary_inspection.dto.TemporaryInspection;
import com.hand.hqm.hqm_temporary_inspection.mapper.TemporaryInspectionMapper;
import com.hand.hqm.hqm_temporary_inspection.service.ITemporaryInspectionService;

import jodd.util.StringUtil;

import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class TemporaryInspectionServiceImpl extends BaseServiceImpl<TemporaryInspection>
		implements ITemporaryInspectionService {

	@Autowired
	InspectionAttributeMapper inspectionAttributeMapper;
	@Autowired
	TemporaryInspectionMapper mapper;
	@Autowired
	IPromptService iPromptService;
	@Autowired
	IqcInspectionTemplateHMapper iqcInspectionTemplateHMapper;
	@Autowired
	IqcInspectionTemplateLMapper iqcInspectionTemplateLMapper;
	@Autowired
	IIqcInspectionTemplateLService iIqcInspectionTemplateLService;
	@Autowired
	IqcInspectionHMapper iqcInspectionHMapper;
	@Autowired
	IIqcInspectionLService iIqcInspectionLService;
	@Autowired
	IqcInspectionLMapper iqcInspectionLMapper;
	@Autowired
	SampleManageMapper sampleManageMapper;
	@Autowired
	SampleSizeCodeLetterMapper sampleSizeCodeLetterMapper;
	@Autowired
	SampleSchemeMapper sampleSchemeMapper;
	@Autowired
	SwitchScoreMapper switchScoreMapper;
	@Autowired
	ISwitchScoreService iSwitchScoreService;
	@Autowired
	SuppliersMapper suppliersMapper;
	@Autowired
	FqcInspectionHMapper fqcInspectionHMapper;
	@Autowired
	IFqcInspectionLService iFqcInspectionLService;
	@Autowired
	FqcInspectionLMapper fqcInspectionLMapper;
	@Autowired
	StandardOpItemHMapper standardOpItemHMapper;
	@Autowired
	IStandardOpItemLService iStandardOpItemLService;
	@Autowired
	PqcInspectionHMapper pqcInspectionHMapper;
	@Autowired
	PqcInspectionLMapper pqcInspectionLMapper;
	@Autowired
	PqcInspectionCMapper pqcInspectionCMapper;
	@Autowired
	IPqcInspectionDService iPqcInspectionDService;
	@Autowired
	StandardOpItemLMapper standardOpItemLMapper;
	@Autowired
	PqcInspectionDMapper pqcInspectionDMapper;
	@Autowired
	IIqcInspectionDService iIqcInspectionDService;
	@Autowired
	IFqcInspectionDService iFqcInspectionDService;
	@Autowired
	PlatformProgramMapper platformProgramMapper;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.hand.hqm.hqm_temporary_inspection.service.ITemporaryInspectionService#
	 * addNew(com.hand.hap.core.IRequest,
	 * com.hand.hqm.hqm_temporary_inspection.dto.TemporaryInspection)
	 */
	@Override

	public void addNew(IRequest requestContext, TemporaryInspection dto) {

		int precision = -1;// 精度
		// 计量型 M 需要规格值从和规格值至
		if (dto.getStandardType().equals("M")) {
			if (StringUtils.isEmpty(dto.getStandradFrom()) || StringUtils.isEmpty(dto.getStandradTo())) {
				// 规格类型为计量时 规格值从 和 规格值至为必输项
				throw new RuntimeException(SystemApiMethod.getPromptDescription(requestContext, iPromptService,
						"temporaryinspection_add_error1"));
			}
			if (!SystemApiMethod.judgePercision(dto.getStandradFrom(), dto.getStandradTo())) {
				// 规格值从 和 规格值至 精度不一致
				throw new RuntimeException(SystemApiMethod.getPromptDescription(requestContext, iPromptService,
						"temporaryinspection_add_error2"));
			}
			precision = SystemApiMethod.getPercision(dto.getStandradFrom());
		}
		/*
		 * 判断检验项目是否存在 不存在就新增 取 attributeId 存在就直接取
		 */

		InspectionAttribute ia = new InspectionAttribute();
		ia.setInspectionAttribute(dto.getInspectionAttribute());
		List<InspectionAttribute> list = inspectionAttributeMapper.select(ia);
		if (list != null && list.size() > 0) {
			ia = list.get(0);
		} else {
			ia.setAttributeType(dto.getAttributeType());
			ia.setQualityCharacterGrade(dto.getQualityCharacterGrade());
			ia.setStandardType(dto.getStandardType());
			ia.setInspectionMethod(String.valueOf(dto.getInspectionMethod().intValue()));
			ia.setInspectionTool(dto.getInspectionRequest());
			if (precision != -1) {
				ia.setPrecision(Float.valueOf(precision));
			}
			inspectionAttributeMapper.insertSelective(ia);
		}
		dto.setAttributeId(ia.getAttributeId());
		if (dto.getKid().intValue() == -1) {
			dto.setEnableTime(new Date());
			dto.setStatus("P");
			dto.setEnableType("T");
			dto.setInspectionType(StringUtil.isEmpty(dto.getInspectionType()) ? "IQC" : dto.getInspectionType());
			mapper.insertSelective(dto);
		} else {
			mapper.updateByPrimaryKeySelective(dto);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.hand.hqm.hqm_temporary_inspection.service.ITemporaryInspectionService#
	 * reSelect(com.hand.hap.core.IRequest,
	 * com.hand.hqm.hqm_temporary_inspection.dto.TemporaryInspection, int, int)
	 */
	@Override
	public List<TemporaryInspection> reSelect(IRequest requestContext, TemporaryInspection dto, int page,
			int pageSize) {
		PageHelper.startPage(page, pageSize);
		return mapper.reSelect(dto);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.hand.hqm.hqm_temporary_inspection.service.ITemporaryInspectionService#
	 * batchIssue(com.hand.hap.core.IRequest, java.util.List)
	 */
	@Override
	public void batchIssue(IRequest requestCtx, List<TemporaryInspection> dto) {// 发布
		// TODO Auto-generated method stub
		for (TemporaryInspection ti : dto) {
			if (!ti.getStatus().equals("P")) {
				// 只能对未发布的进行发布
				throw new RuntimeException(SystemApiMethod.getPromptDescription(requestCtx, iPromptService,
						"temporaryinspection_issue_error1"));
			}

			switch (ti.getInspectionType()) {
			case "IQC":
				iqcTypeIssue(requestCtx, ti);
				break;
			case "FQC":
				fqcTypeIssue(requestCtx, ti);
				break;
			case "PQC":
				pqcTypeIssue(requestCtx, ti);
				break;
			}
			TemporaryInspection up = new TemporaryInspection();
			up.setKid(ti.getKid());
			up.setStatus("R");
			self().updateByPrimaryKeySelective(requestCtx, up);
		}
	}

	/**
	 * @description 发布PQC类型的临时检验项
	 * @author tianmin.wang
	 * @date 2019年12月12日
	 * @param requestCtx
	 * @param ti
	 */
	private void pqcTypeIssue(IRequest requestCtx, TemporaryInspection ti) {
		List<StandardOpItemH> hList = standardOpItemHMapper.selectByCategoryId(ti);
		if (hList != null && hList.size() > 0) {
			for (StandardOpItemH itemH : hList) {

				/**
				 * 是否存在性
				 */
				StandardOpItemL lSearch = new StandardOpItemL();
				lSearch.setHeadId(itemH.getHeadId());
				lSearch.setAttributeId(ti.getAttributeId());
				List<StandardOpItemL> res = standardOpItemLMapper.select(lSearch);
				if (res != null && res.size() > 0) {
					continue;
				}
				StandardOpItemL linsert = new StandardOpItemL();
				linsert.setHeadId(itemH.getHeadId());
				linsert.setAttribute5(String.valueOf(ti.getKid().intValue()));
				linsert.setAttributeId(ti.getAttributeId());
				linsert.setSampleWayId(ti.getSampleWayId());
				linsert.setStandardType(ti.getStandardType());
				linsert.setIsSync(ti.getIsSync());
				linsert.setStandradUom(ti.getStandradUom());
				linsert.setTextStandrad(ti.getTextStandrad());
				linsert.setInspectionMethod(String.valueOf(ti.getInspectionMethod().intValue()));
				linsert.setEnableTime(new Date());
				linsert.setDisableTime(ti.getDisableTime());
				linsert.setFillInType(ti.getInspectionRequest());
				if (ti.getStandardType().equals("M")) {
					linsert.setStandradFrom(ti.getStandradFrom());
					linsert.setStandradTo(ti.getStandradTo());
					linsert.setPrecision(Float.valueOf(SystemApiMethod.getPercision(ti.getStandradFrom())));
				}
				iStandardOpItemLService.insertSelective(requestCtx, linsert);
			}
		}

		List<PqcInspectionH> ihList = pqcInspectionHMapper.selectByCategoryId(ti);
		for (PqcInspectionH pqcInspectionH : ihList) {
			PqcInspectionL lSearch = new PqcInspectionL();
			lSearch.setHeaderId(pqcInspectionH.getHeaderId());
			lSearch.setStandardOpId(ti.getStandardOpId());
			lSearch.setWorkstationId(ti.getWorkstationId());
			List<PqcInspectionL> ilList = pqcInspectionLMapper.select(lSearch);
			for (PqcInspectionL pqcInspectionL : ilList) {
				PqcInspectionC cSearch = new PqcInspectionC();
				cSearch.setLineId(pqcInspectionL.getLineId());
				List<PqcInspectionC> cList = pqcInspectionCMapper.select(cSearch);
				if (cList != null && cList.size() > 0) {
					// 计算最大抽样数
					PqcInspectionC maxc = cList.stream().max(Comparator.comparing(PqcInspectionC::getSampleQty)).get();
					addNewPqcInspectionC(requestCtx, ti, maxc, pqcInspectionL);
				}
			}
		}
	}

	/**
	 * 
	 * @description 新增pqc检验单C
	 * @author tianmin.wang
	 * @date 2019年12月12日
	 * @param requestCtx
	 * @param maxc
	 * @param pqcInspectionL
	 */
	private void addNewPqcInspectionC(IRequest requestCtx, TemporaryInspection ti, PqcInspectionC maxc,
			PqcInspectionL dto) {

		// 获取工厂
		Float plantId = ti.getPlantId();
		// 获取抽样计划类型(抽样方案)
		String samplePlan;// 抽样计划类型(抽样方案)
		SwitchScore scoreSearch = new SwitchScore();
		scoreSearch.setItemId(Float.valueOf(dto.getItemId()));
		scoreSearch.setPlantId(plantId);
		List<SwitchScore> switchScoreList = switchScoreMapper.select(scoreSearch);
		if (switchScoreList == null || switchScoreList.size() == 0) {
			// 如果不存在则新增一个抽样计划
			SwitchScore scoreAdder = new SwitchScore();
			scoreAdder.setItemId(scoreSearch.getItemId());
			scoreAdder.setPlantId(scoreSearch.getPlantId());
			scoreAdder.setSamplePlanTypeNow("N");
			scoreAdder.setSamplePlanTypeNext("N");
			scoreAdder.setSwitchScore(0f);
			scoreAdder.setNonnconformingLot(0f);
			scoreAdder.setConsecutiveConformingLot(0f);
			scoreAdder.setChangeFlag("Y");
			switchScoreMapper.insertSelective(scoreAdder);
			samplePlan = scoreAdder.getSamplePlanTypeNow();
		} else {
			samplePlan = switchScoreList.get(0).getSamplePlanTypeNow();
		}

		Float sampleSize = 1f;
		PqcInspectionC searchInsc = new PqcInspectionC();
		searchInsc.setLineId(dto.getLineId());
		searchInsc.setAttributeId(ti.getAttributeId());
		PqcInspectionC addorInsc = pqcInspectionCMapper.selectOne(searchInsc);

		if (addorInsc == null) {
			// 新增
			addorInsc = new PqcInspectionC();
			addorInsc.setLineId(dto.getLineId());
			addorInsc.setAttributeId(ti.getAttributeId());
		}

		if (dto.getProductionQty() == 1) {
			addorInsc.setSampleQty(1f);
		} else {
			// 抽样标准获取

			// 找抽样方式
			SampleManage smSearch = new SampleManage();
			smSearch.setSampleWayId(Float.valueOf(ti.getSampleWayId()));
			SampleManage sampleManageResult = sampleManageMapper.selectByPrimaryKey(smSearch);
			addorInsc.setSampleType(Float.valueOf(sampleManageResult.getSampleType()));
			switch (sampleManageResult.getSampleType()) {
			case "0":
				addorInsc.setSampleQty(Float.valueOf(sampleManageResult.getParameter()));
				if (sampleSize < addorInsc.getSampleQty()) {
					sampleSize = addorInsc.getSampleQty();
				}
				break;
			case "2":
				addorInsc.setSampleQty(dto.getProductionQty());
				if (sampleSize < addorInsc.getSampleQty()) {
					sampleSize = addorInsc.getSampleQty();
				}
				break;
			case "1":
				// InspectionAttribute serInsAttr = new InspectionAttribute();
				// serInsAttr.setAttributeId(ti.getAttributeId());
				// InspectionAttribute resInsAttr =
				// inspectionAttributeMapper.selectByPrimaryKey(serInsAttr);
				SampleSizeCodeLetter searchCodeLetter = new SampleSizeCodeLetter();
				searchCodeLetter.setInspectionLevels(sampleManageResult.getInspectionLevels());
				searchCodeLetter.setLotSizeFrom(dto.getProductionQty());
				searchCodeLetter.setLotSizeTo(dto.getProductionQty());
				List<SampleSizeCodeLetter> sizeCodeResultList = new ArrayList<SampleSizeCodeLetter>();
				List<SampleScheme> sampleSchemeResultList = new ArrayList<SampleScheme>();
				if (sampleManageResult.getSampleProcedureType() == null) {
					throw new RuntimeException(SystemApiMethod.getPromptDescription(requestCtx, iPromptService,
							"error.sampleproceduretype_is_null"));
				}
				if ("0".equals(sampleManageResult.getSampleProcedureType())) {
					// 抽样标准为0时
					sizeCodeResultList = sampleSizeCodeLetterMapper.selectCodeSizeLetter(searchCodeLetter);
					SampleScheme sampleSchemeSearch = new SampleScheme();
					sampleSchemeSearch.setSampleSizeCodeLetter(sizeCodeResultList.get(0).getSizeCodeLetter());
					sampleSchemeSearch.setSamplePlanType(samplePlan);
					sampleSchemeSearch.setAcceptanceQualityLimit(sampleManageResult.getAcceptanceQualityLimit());
					// 查找确定检验项的抽样数量、AC、RE值
					sampleSchemeResultList = sampleSchemeMapper.select(sampleSchemeSearch);
					if (dto.getProductionQty() < sampleSchemeResultList.get(0).getSampleSize()
							|| sampleSchemeResultList.get(0).getSampleSize() == (-1f)) {
						addorInsc.setSampleQty(dto.getProductionQty());
					} else {
						addorInsc.setSampleQty(sampleSchemeResultList.get(0).getSampleSize());
					}
					addorInsc.setSizeCodeLetter(sizeCodeResultList.get(0).getSizeCodeLetter());// 样本量字码
					addorInsc.setAc(String.valueOf(sampleSchemeResultList.get(0).getAcValue()));// AC值
					addorInsc.setRe(String.valueOf(sampleSchemeResultList.get(0).getReValue()));// RE值
				} else if (sampleManageResult.getSampleProcedureType().equals("1")) {
					// 抽样标准为1时
					SampleScheme sampleSchemeSearch = new SampleScheme();
					sampleSchemeSearch.setAcceptanceQualityLimit(sampleManageResult.getAcceptanceQualityLimit());
					sampleSchemeSearch.setAttribute1(String.valueOf(dto.getProductionQty()));
					sampleSchemeSearch.setAttribute2(String.valueOf(dto.getProductionQty()));
					sampleSchemeResultList = sampleSchemeMapper.selectSampleSize(sampleSchemeSearch);
					if (sampleSchemeResultList.get(0).getSampleSize() > dto.getProductionQty()
							|| sampleSchemeResultList.get(0).getSampleSize() == (-1f)) {
						// 若计算出的抽样数量大于生产数量 则抽样数量取值为生产数量
						addorInsc.setSampleQty(dto.getProductionQty());
					} else {
						addorInsc.setSampleQty(sampleSchemeResultList.get(0).getSampleSize());// 抽样数量
					}
				} else {
					// 抽样标准为2时
					addorInsc.setSampleQty(1f);
				}
				break;
			}

		}
		addorInsc.setAttribute5(String.valueOf(ti.getKid().intValue()));
		addorInsc.setSampleQty(maxc.getSampleQty());
		addorInsc.setSampleWayId(ti.getSampleWayId());
		if (ti.getStandradFrom() != null) {
			addorInsc.setPrecision(Float.valueOf(SystemApiMethod.getPercision(ti.getStandradFrom())));
		}
		addorInsc.setAttributeId(ti.getAttributeId());
		addorInsc.setLineId(dto.getLineId());
		addorInsc.setTextStandrad(ti.getTextStandrad());
		addorInsc.setStandradFrom(ti.getStandradFrom());
		addorInsc.setStandradTo(ti.getStandradTo());
		addorInsc.setStandradUom(ti.getStandradUom());
		addorInsc.setAttributeType(ti.getAttributeType());
		// 值插入
		if (addorInsc.getConnectId() == null) {
			pqcInspectionCMapper.insertSelective(addorInsc);
		} else {
			// pqcInspectionCMapper.updateByPrimaryKeySelective(addorInsc);
		}
		// add by tianmin.wang 20191017 生成固定的C表对应的D表数据

		iPqcInspectionDService.batchCreateByInspectionC(requestCtx, addorInsc);
	}

	/**
	 * @description 发布FQC类型的临时检验项
	 * @author tianmin.wang
	 * @date 2019年12月12日
	 * @param requestCtx
	 * @param ti
	 */
	private void fqcTypeIssue(IRequest requestCtx, TemporaryInspection ti) {
		List<IqcInspectionTemplateH> iithList = iqcInspectionTemplateHMapper.selectByCategoryId(ti);
		if (iithList != null && iithList.size() > 0) {
			// 在所有检验单模板行上新增一行
			for (IqcInspectionTemplateH templateH : iithList) {

				/**
				 * attributeId不能重复 同一个模板头下不能重复 FQC和IQC都用的 IQC模板
				 */
				IqcInspectionTemplateL templateSearch = new IqcInspectionTemplateL();
				templateSearch.setHeaderId(templateH.getHeaderId());
				templateSearch.setAttributeId(ti.getAttributeId());
				List<IqcInspectionTemplateL> res = iqcInspectionTemplateLMapper.select(templateSearch);
				if (res != null && res.size() > 0)
					continue;
				//

				IqcInspectionTemplateL templateIns = new IqcInspectionTemplateL();
				templateIns.setHeaderId(templateH.getHeaderId());
				templateIns.setAttributeId(ti.getAttributeId());
				templateIns.setAttributeType(ti.getAttributeType());
				templateIns.setInspectionAttribute(ti.getInspectionAttribute());
				templateIns.setInspectionTool(ti.getInspectionRequest());
				templateIns.setInspectionMethod(String.valueOf(ti.getInspectionMethod().intValue()));
				templateIns.setSampleWayId(ti.getSampleWayId());
				templateIns.setEnableType(ti.getEnableType());
				templateIns.setStandardType(ti.getStandardType());
				templateIns.setStandradFrom(ti.getStandradFrom());
				templateIns.setStandradTo(ti.getStandradTo());
				templateIns.setQualityCharacterGrade(ti.getQualityCharacterGrade());
				templateIns.setEnableTime(new Date());
				templateIns.setIsSync(ti.getIsSync());
				templateIns.setDisableTime(ti.getDisableTime());
				templateIns.setAttribute5(String.valueOf(ti.getKid().intValue()));
				iIqcInspectionTemplateLService.insertSelective(requestCtx, templateIns);
			}
		}

		List<FqcInspectionH> iihList = fqcInspectionHMapper.selectByCategoryId(ti);

		if (iihList != null && iihList.size() > 0) {
			for (FqcInspectionH inspectionH : iihList) {
				addNewFqcInspectionL(ti, inspectionH, requestCtx);
			}
		}
	}

	/**
	 * @description 新增fqc检验单行
	 * @author tianmin.wang
	 * @date 2019年12月12日
	 * @param ti
	 * @param inspectionH
	 * @param requestCtx
	 */
	private void addNewFqcInspectionL(TemporaryInspection ti, FqcInspectionH dto, IRequest requestCtx) {

		/**
		 * attributeId不能重复 同一个模板头下不能重复 FQC和IQC都用的 IQC模板
		 */
		FqcInspectionL templateSearch = new FqcInspectionL();
		templateSearch.setHeaderId(dto.getHeaderId());
		templateSearch.setAttributeId(ti.getAttributeId());
		List<FqcInspectionL> res = fqcInspectionLMapper.select(templateSearch);
		if (res != null && res.size() > 0)
			return;
		//

		FqcInspectionL inspectionL = new FqcInspectionL();// 新的检验项
		// 找抽样方式
		FqcInspectionH dao = dto;

		// 获取抽样计划类型
		String samplePlan;// 抽样计划类型(抽样方案)
		SwitchScore scoreSearch = new SwitchScore();
		SwitchScore switchScore;
		scoreSearch.setItemId(dao.getItemId());
		scoreSearch.setPlantId(dao.getPlantId());
		List<SwitchScore> switchScoreList = switchScoreMapper.select(scoreSearch);
		if (switchScoreList == null || switchScoreList.size() == 0) {
			throw new RuntimeException(SystemApiMethod.getPromptDescription(requestCtx, iPromptService,
					"error.hqm_fqc_inspection_sample01"));
		} else {
			switchScore = switchScoreList.get(0);
			samplePlan = switchScoreList.get(0).getSamplePlanTypeNow();
		}

		Float sampleSize = 1f;// 头的抽样数量
		SampleManage smSearch = new SampleManage();
		smSearch.setSampleWayId(ti.getSampleWayId());
		SampleManage sampleManageResult = sampleManageMapper.selectByPrimaryKey(smSearch);
		if (dao.getProduceQty() == 1) {
			inspectionL.setSampleQty(1f);// 抽样数量
		} else {
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
				// 抽样标准确定抽样数量
				if (sampleManageResult.getSampleProcedureType().equals("0")) {
					// 抽样标准为0时
					sizeCodeResultList = sampleSizeCodeLetterMapper.selectCodeSizeLetter(searchCodeLetter);
					SampleScheme sampleSchemeSearch = new SampleScheme();
					sampleSchemeSearch.setSampleSizeCodeLetter(sizeCodeResultList.get(0).getSizeCodeLetter());
					sampleSchemeSearch.setSamplePlanType(samplePlan);
					sampleSchemeSearch.setAcceptanceQualityLimit(sampleManageResult.getAcceptanceQualityLimit());
					// 查找确定检验项的抽样数量、AC、RE值
					sampleSchemeResultList = sampleSchemeMapper.select(sampleSchemeSearch);
					if (sampleSchemeResultList.get(0).getSampleSize() > dao.getProduceQty()
							|| sampleSchemeResultList.get(0).getSampleSize() == (-1f)) {
						// 若计算出的抽样数量大于接收数量 则抽样数量取值为接收数量
						sampleSchemeResultList.get(0).setSampleSize(dao.getProduceQty());
					}
					if (sampleSize < sampleSchemeResultList.get(0).getSampleSize()) {
						sampleSize = sampleSchemeResultList.get(0).getSampleSize();
					}
					inspectionL.setSizeCodeLetter(sizeCodeResultList.get(0).getSizeCodeLetter());// 样本量字码
					inspectionL.setSampleQty(sampleSchemeResultList.get(0).getSampleSize());// 抽样数量
					inspectionL.setAc(String.valueOf(sampleSchemeResultList.get(0).getAcValue()));// AC值
					inspectionL.setRe(String.valueOf(sampleSchemeResultList.get(0).getReValue()));// RE值
				} else if (sampleManageResult.getSampleProcedureType().equals("1")) {
					// 抽样标准为1时
					SampleScheme sampleSchemeSearch = new SampleScheme();
					sampleSchemeSearch.setAcceptanceQualityLimit(sampleManageResult.getAcceptanceQualityLimit());
					sampleSchemeSearch.setAttribute1(String.valueOf(dao.getProduceQty()));
					sampleSchemeSearch.setAttribute2(String.valueOf(dao.getProduceQty()));
					sampleSchemeResultList = sampleSchemeMapper.selectSampleSize(sampleSchemeSearch);
					if (sampleSchemeResultList.get(0).getSampleSize() > dao.getProduceQty()
							|| sampleSchemeResultList.get(0).getSampleSize().intValue() == (-1)) {
						// 若计算出的抽样数量大于接收数量或者为-1 则抽样数量取值为生产数量
						sampleSchemeResultList.get(0).setSampleSize(dao.getProduceQty());
					}
					if (sampleSize < sampleSchemeResultList.get(0).getSampleSize()) {
						sampleSize = sampleSchemeResultList.get(0).getSampleSize();
					}
					inspectionL.setSampleQty(sampleSchemeResultList.get(0).getSampleSize());// 抽样数量
				} else if (sampleManageResult.getSampleProcedureType().equals("3")) {

					/**
					 * 抽样标准为3的逻辑 add by wtm 20191220 switchScore
					 */
					// 查询 检验项目
					InspectionAttribute iaSearch = new InspectionAttribute();
					iaSearch.setAttributeId(ti.getAttributeId());
					List<InspectionAttribute> iaResult = inspectionAttributeMapper.select(iaSearch);
					PlatformProgram ppSearch = new PlatformProgram();
					ppSearch.setPlantId(dao.getPlantId());
					ppSearch.setPlatformType(switchScore.getPlatform());
					ppSearch.setProgramType(iaResult.get(0).getAttribute1());
					List<PlatformProgram> ppResult = platformProgramMapper.select(ppSearch);
					if (ppResult == null || ppResult.size() == 0) {
						throw new RuntimeException(SystemApiMethod.getPromptDescription(requestCtx, iPromptService,
								"error.platform_program_is_null"));
					}
					String samplePlanType = "";
					switch (dao.getSamplePlan()) {// 获取抽样方案
					case "K":
						switch (ppResult.get(0).getSampleType()) {
						case "6":
							samplePlanType = "K";
							break;
						case "5":
							samplePlanType = "K";
							break;
						case "4":
							samplePlanType = "L";
							break;
						case "3":
							samplePlanType = "L";
							break;
						case "2":
							samplePlanType = "N";
							break;
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
							samplePlanType = "S";
							break;
						case "5":
							samplePlanType = "N";
							break;
						case "4":
							samplePlanType = "S";
							break;
						case "3":
							samplePlanType = "N";
							break;
						case "2":
							samplePlanType = "S";
							break;
						case "1":
							samplePlanType = "N";
							break;
						}
						break;
					case "L":
						switch (ppResult.get(0).getSampleType()) {
						case "6":
							samplePlanType = "L";
							break;
						case "5":
							samplePlanType = "L";
							break;
						case "4":
							samplePlanType = "L";
							break;
						case "3":
							samplePlanType = "L";
							break;
						case "2":
							samplePlanType = "N";
							break;
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
						throw new RuntimeException(SystemApiMethod.getPromptDescription(requestCtx, iPromptService,
								"error.sample_scheme_is_null"));
					}
					Double sampleQty = Math.ceil(Float.valueOf(hssResult.get(0).getAttribute4()) * dao.getProduceQty());
					if (sampleSize < sampleQty) {
						sampleSize = sampleQty.floatValue();
					}
					inspectionL.setSampleQty(sampleQty.floatValue());// 抽样数量
				} else {
					// 抽样标准为2时
					inspectionL.setSampleQty(1f);
				}
				break;
			}

		}
		inspectionL.setAttribute5(String.valueOf(ti.getKid().intValue()));
		inspectionL.setHeaderId(dao.getHeaderId());

		if (inspectionL.getSampleQty().intValue() > dao.getSampleQty().intValue()) {
			inspectionL.setSampleQty(dao.getSampleQty());
		}
		// inspectionL.setTemplateLineId(inspectionTemplateL.getLineId());
		inspectionL.setInspectionMethod(String.valueOf(ti.getInspectionMethod()));
		if (ti.getStandradFrom() != null) {
			inspectionL.setPrecision(Float.valueOf(String.valueOf(SystemApiMethod.getPercision(ti.getStandradFrom()))));
		}
		inspectionL.setInspectionMethod(String.valueOf(ti.getInspectionMethod().intValue()));
		inspectionL.setAttributeId(ti.getAttributeId());
		inspectionL.setAttributeType(ti.getAttributeType());
		// inspectionL.setOrderCode(inspectionTemplateL.getOrderCode());
		inspectionL.setInspectionAttribute(ti.getInspectionAttribute());
		inspectionL.setInspectionTool(ti.getInspectionRequest());
		inspectionL.setSampleProcedureType(sampleManageResult.getSampleProcedureType());
		inspectionL.setInspectionLevels(sampleManageResult.getInspectionLevels());
		inspectionL.setQualityCharacterGrade(ti.getQualityCharacterGrade());
		inspectionL.setStandardType(ti.getStandardType());
		inspectionL.setStandradFrom(ti.getStandradFrom());
		inspectionL.setStandradTo(ti.getStandradTo());
		inspectionL.setStandradUom(ti.getStandradUom());
		inspectionL.setTextStandrad(ti.getTextStandrad());
		// inspectionL.setDataFrom(inspectionTemplateL.getFillInType());
		inspectionL.setAcceptanceQualityLimit(sampleManageResult.getAcceptanceQualityLimit());
		// inspectionL.setFillInType(sampleManageResult.getFillInType());
		inspectionL.setSampleWayId(Float.valueOf(ti.getSampleWayId()));
		inspectionL.setSampleType(Float.parseFloat(sampleManageResult.getSampleType()));
		if (sampleManageResult.getParameter() != null && !StringUtils.isBlank(sampleManageResult.getParameter())) {
			inspectionL.setParameter(Float.parseFloat(sampleManageResult.getParameter()));
		}
		iFqcInspectionLService.insertSelective(requestCtx, inspectionL);
		iFqcInspectionDService.batchCreateByInspectionL(requestCtx, inspectionL);
	}

	/**
	 * 
	 * @description 发布IQC类型的临时检验项
	 * @author tianmin.wang
	 * @date 2019年12月12日
	 * @param requestCtx
	 * @param ti
	 */
	public void iqcTypeIssue(IRequest requestCtx, TemporaryInspection ti) {
		List<IqcInspectionTemplateH> iithList = iqcInspectionTemplateHMapper.selectByCategoryId(ti);
		if (iithList != null && iithList.size() > 0) {
			// 在所有检验单模板行上新增一行
			for (IqcInspectionTemplateH templateH : iithList) {
				/**
				 * attributeId不能重复 同一个模板头下不能重复
				 */
				IqcInspectionTemplateL templateSearch = new IqcInspectionTemplateL();
				templateSearch.setHeaderId(templateH.getHeaderId());
				templateSearch.setAttributeId(ti.getAttributeId());
				List<IqcInspectionTemplateL> res = iqcInspectionTemplateLMapper.select(templateSearch);
				if (res != null && res.size() > 0)
					continue;
				//
				IqcInspectionTemplateL templateIns = new IqcInspectionTemplateL();
				templateIns.setHeaderId(templateH.getHeaderId());
				templateIns.setAttributeId(ti.getAttributeId());
				templateIns.setAttributeType(ti.getAttributeType());
				templateIns.setInspectionAttribute(ti.getInspectionAttribute());
				templateIns.setInspectionTool(ti.getInspectionRequest());
				templateIns.setInspectionMethod(String.valueOf(ti.getInspectionMethod().intValue()));
				templateIns.setSampleWayId(ti.getSampleWayId());
				templateIns.setEnableType(ti.getEnableType());
				templateIns.setStandardType(ti.getStandardType());
				templateIns.setStandradFrom(ti.getStandradFrom());
				templateIns.setStandradTo(ti.getStandradTo());
				templateIns.setQualityCharacterGrade(ti.getQualityCharacterGrade());
				templateIns.setEnableTime(new Date());
				templateIns.setIsSync(ti.getIsSync());
				templateIns.setDisableTime(ti.getDisableTime());
				templateIns.setAttribute5(String.valueOf(ti.getKid().intValue()));
				iIqcInspectionTemplateLService.insertSelective(requestCtx, templateIns);
			}
		}

		List<IqcInspectionH> iihList = iqcInspectionHMapper.selectByCategoryId(ti);

		if (iihList != null && iihList.size() > 0) {
			for (IqcInspectionH inspectionH : iihList) {
				addNewIqcInspectionL(ti, inspectionH, requestCtx);
			}
		}

	}

	/**
	 * @description 新增iqc检验单行
	 * @author tianmin.wang
	 * @param requestCtx
	 * @param dao
	 * @param ti
	 * @date 2019年12月10日
	 */
	private void addNewIqcInspectionL(TemporaryInspection ti, IqcInspectionH dto, IRequest requestCtx) {

		/**
		 * attributeId不能重复 同一个模板头下不能重复
		 */
		IqcInspectionL templateSearch = new IqcInspectionL();
		templateSearch.setHeaderId(dto.getHeaderId());
		templateSearch.setAttributeId(ti.getAttributeId());
		List<IqcInspectionL> res = iqcInspectionLMapper.select(templateSearch);
		if (res != null && res.size() > 0)
			return;
		//

		IqcInspectionL inspectionL = new IqcInspectionL();// 新的检验项
		// 找抽样方式
		IqcInspectionH dao = dto;

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

		Float sampleSize = 1f;// 头的抽样数量
		SampleManage smSearch = new SampleManage();
		smSearch.setSampleWayId(ti.getSampleWayId());
		SampleManage sampleManageResult = sampleManageMapper.selectByPrimaryKey(smSearch);
		if (dao.getReceiveQty() == 1) {
			inspectionL.setSampleSize(1f);// 抽样数量
		} else {
			switch (sampleManageResult.getSampleType()) {
			case "0":
				inspectionL.setSampleSize(Float.valueOf(sampleManageResult.getParameter()));
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
				// 查找对应的样本量字码
				List<SampleSizeCodeLetter> sizeCodeResultList = new ArrayList<SampleSizeCodeLetter>();
				List<SampleScheme> sampleSchemeResultList = new ArrayList<SampleScheme>();
				// 抽样标准确定抽样数量
				if (sampleManageResult.getSampleProcedureType().equals("0")) {
					// 抽样标准为0时
					sizeCodeResultList = sampleSizeCodeLetterMapper.selectCodeSizeLetter(searchCodeLetter);
					SampleScheme sampleSchemeSearch = new SampleScheme();
					sampleSchemeSearch.setSampleSizeCodeLetter(sizeCodeResultList.get(0).getSizeCodeLetter());
					sampleSchemeSearch.setSamplePlanType(samplePlan);
					sampleSchemeSearch.setAcceptanceQualityLimit(sampleManageResult.getAcceptanceQualityLimit());
					// 查找确定检验项的抽样数量、AC、RE值
					sampleSchemeResultList = sampleSchemeMapper.select(sampleSchemeSearch);
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
				} else if (sampleManageResult.getSampleProcedureType().equals("1")) {
					// 抽样标准为1时
					SampleScheme sampleSchemeSearch = new SampleScheme();
					sampleSchemeSearch.setAcceptanceQualityLimit(sampleManageResult.getAcceptanceQualityLimit());
					sampleSchemeSearch.setAttribute1(String.valueOf(dao.getReceiveQty()));
					sampleSchemeSearch.setAttribute2(String.valueOf(dao.getReceiveQty()));
					sampleSchemeResultList = sampleSchemeMapper.selectSampleSize(sampleSchemeSearch);
					if (sampleSchemeResultList.get(0).getSampleSize() > dao.getReceiveQty()
							|| sampleSchemeResultList.get(0).getSampleSize().intValue() == (-1)) {
						// 若计算出的抽样数量大于接收数量或者为-1 则抽样数量取值为生产数量
						sampleSchemeResultList.get(0).setSampleSize(dao.getReceiveQty());
					}
					if (sampleSize < sampleSchemeResultList.get(0).getSampleSize()) {
						sampleSize = sampleSchemeResultList.get(0).getSampleSize();
					}
					inspectionL.setSampleSize(sampleSchemeResultList.get(0).getSampleSize());// 抽样数量
				} else {
					// 抽样标准为2时
					inspectionL.setSampleSize(1f);
				}
				break;

			}

		}
		inspectionL.setAttribute5(String.valueOf(ti.getKid().intValue()));
		inspectionL.setHeaderId(dao.getHeaderId());

		if (inspectionL.getSampleSize().intValue() > dao.getSampleSize().intValue()) {
			inspectionL.setSampleSize(dao.getSampleSize());
		}

		// inspectionL.setTemplateLineId(inspectionTemplateL.getLineId());
		inspectionL.setInspectionMethod(String.valueOf(ti.getInspectionMethod()));
		if (ti.getStandradFrom() != null) {
			inspectionL.setPrecision(Float.valueOf(String.valueOf(SystemApiMethod.getPercision(ti.getStandradFrom()))));
		}
		inspectionL.setAttributeId(ti.getAttributeId());
		inspectionL.setInspectionMethod(String.valueOf(ti.getInspectionMethod().intValue()));
		inspectionL.setAttributeType(ti.getAttributeType());
		// inspectionL.setOrderCode(inspectionTemplateL.getOrderCode());
		inspectionL.setInspectionAttribute(ti.getInspectionAttribute());
		inspectionL.setInspectionTool(ti.getInspectionRequest());
		inspectionL.setSampleProcedureType(sampleManageResult.getSampleProcedureType());
		inspectionL.setInspectionLevels(sampleManageResult.getInspectionLevels());
		inspectionL.setQualityCharacterGrade(ti.getQualityCharacterGrade());
		inspectionL.setStandardType(ti.getStandardType());
		inspectionL.setStandradFrom(ti.getStandradFrom());
		inspectionL.setStandradTo(ti.getStandradTo());
		inspectionL.setStandradUom(ti.getStandradUom());
		inspectionL.setTextStandrad(ti.getTextStandrad());
		// inspectionL.setDataFrom(inspectionTemplateL.getFillInType());
		inspectionL.setAcceptanceQualityLimit(sampleManageResult.getAcceptanceQualityLimit());
		// inspectionL.setFillInType(sampleManageResult.getFillInType());
		inspectionL.setSampleWayId(Float.valueOf(ti.getSampleWayId()));
		inspectionL.setSampleType(Float.parseFloat(sampleManageResult.getSampleType()));
		if (sampleManageResult.getParameter() != null && !StringUtils.isBlank(sampleManageResult.getParameter())) {
			inspectionL.setParameter(Float.parseFloat(sampleManageResult.getParameter()));
		}
		iIqcInspectionLService.insertSelective(requestCtx, inspectionL);
		iIqcInspectionDService.batchCreateByInspectionL(requestCtx, inspectionL);
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.hand.hqm.hqm_temporary_inspection.service.ITemporaryInspectionService#
	 * batchDisable(com.hand.hap.core.IRequest, java.util.List)
	 */
	@Override
	public void batchDisable(IRequest requestCtx, List<TemporaryInspection> dto) {// 失效
		// TODO Auto-generated method stub
		for (TemporaryInspection ti : dto) {
			if (!ti.getStatus().equals("R")) {
				// 只能对已发布状态的进行失效操作
				throw new RuntimeException(SystemApiMethod.getPromptDescription(requestCtx, iPromptService,
						"temporaryinspection_disable_error1"));
			}
			switch (ti.getInspectionType()) {
			case "IQC":
				iqcDisable(requestCtx, ti);
				break;
			case "FQC":
				fqcDisable(requestCtx, ti);
				break;
			case "PQC":
				pqcDisable(requestCtx, ti);
				break;
			}
			TemporaryInspection up = new TemporaryInspection();
			up.setKid(ti.getKid());
			up.setStatus("C");
			self().updateByPrimaryKeySelective(requestCtx, up);
		}
	}

	/**
	 * @description
	 * @author tianmin.wang
	 * @date 2019年12月12日
	 * @param requestCtx
	 * @param ti
	 */
	private void pqcDisable(IRequest requestCtx, TemporaryInspection ti) {
		/**
		 * 将PQC检验单模板行表HQM_STANDARD_OP_ITEM_L中attribute5为选中行的kid的检验单模板行数据进行删除
		 */
		StandardOpItemL itemLSearch = new StandardOpItemL();
		itemLSearch.setAttribute5(String.valueOf(ti.getKid().intValue()));

		List<StandardOpItemL> lResult = standardOpItemLMapper.select(itemLSearch);
		lResult.forEach(p -> {
			standardOpItemLMapper.delete(p);
		});

		/**
		 * 将PQC检验单C表HQM_PQC_INSPECTION_C的数据中attribute5字段为选中行的kid
		 * 且检验单头表HQM_PQC_INSPECTION_H的检验单状态不为 已完成 inspectionStatus.equals("5")
		 * 的检验单该行数据进行删除
		 */
		PqcInspectionC iCSearch = new PqcInspectionC();
		iCSearch.setAttribute5(String.valueOf(ti.getKid().intValue()));
		List<PqcInspectionC> cResult = pqcInspectionCMapper.select(iCSearch);
		cResult.forEach(p -> {
			PqcInspectionH head = pqcInspectionHMapper
					.selectByPrimaryKey(pqcInspectionLMapper.selectByPrimaryKey(p.getLineId()).getHeaderId());
			if (!head.getInspectionStatus().equals("5")) {
				pqcInspectionCMapper.delete(p);
				pqcInspectionDMapper.deleteByConnectId(p);
			}
		});

	}

	/**
	 * @description
	 * @author tianmin.wang
	 * @date 2019年12月12日
	 * @param requestCtx
	 * @param ti
	 */
	private void fqcDisable(IRequest requestCtx, TemporaryInspection ti) {
		IqcInspectionTemplateL sertl = new IqcInspectionTemplateL();
		sertl.setAttribute5(String.valueOf(ti.getKid().intValue()));
		FqcInspectionL serl = new FqcInspectionL();
		serl.setAttribute5(String.valueOf(ti.getKid().intValue()));
		List<IqcInspectionTemplateL> tlList = iqcInspectionTemplateLMapper.select(sertl);
		List<FqcInspectionL> lList = fqcInspectionLMapper.select(serl);
		// 删除检验单模板行
		tlList.stream().forEach(p -> {
			iqcInspectionTemplateLMapper.deleteByPrimaryKey(p);

		});
		// 删除检验单行
		lList.stream().forEach(p -> {
			// 头的状态
			FqcInspectionH head = fqcInspectionHMapper.selectByPrimaryKey(p.getHeaderId());
			if (head != null && !head.getInspectionStatus().equals("5")) {
				fqcInspectionLMapper.deleteByPrimaryKey(p);
			}
		});

	}

	/**
	 * @description
	 * @author tianmin.wang
	 * @date 2019年12月12日
	 * @param requestCtx
	 * @param ti
	 */
	private void iqcDisable(IRequest requestCtx, TemporaryInspection ti) {
		IqcInspectionTemplateL sertl = new IqcInspectionTemplateL();
		sertl.setAttribute5(String.valueOf(ti.getKid().intValue()));
		IqcInspectionL serl = new IqcInspectionL();
		serl.setAttribute5(String.valueOf(ti.getKid().intValue()));
		List<IqcInspectionTemplateL> tlList = iqcInspectionTemplateLMapper.select(sertl);
		List<IqcInspectionL> lList = iqcInspectionLMapper.select(serl);
		// 删除检验单模板行
		tlList.stream().forEach(p -> {
			iqcInspectionTemplateLMapper.deleteByPrimaryKey(p);

		});
		// 删除检验单行
		lList.stream().forEach(p -> {
			// 头的状态
			IqcInspectionH head = iqcInspectionHMapper.selectByPrimaryKey(p.getHeaderId());
			if (head != null && !head.getInspectionStatus().equals("5")) {
				iqcInspectionLMapper.deleteByPrimaryKey(p);
			}
		});
	}

}