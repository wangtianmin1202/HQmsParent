package com.hand.hqm.hqm_pqc_inspection_l.service.impl;

import com.github.pagehelper.PageHelper;
import com.hand.hap.core.IRequest;
import com.hand.hap.core.impl.RequestHelper;
import com.hand.hap.core.impl.ServiceRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.system.service.IPromptService;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import com.hand.hap.util.SystemApiMethod;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hand.hqm.hqm_fqc_inspection_l.dto.FqcInspectionL;
import com.hand.hqm.hqm_inspection_attribute.dto.InspectionAttribute;
import com.hand.hqm.hqm_inspection_attribute.mapper.InspectionAttributeMapper;
import com.hand.hqm.hqm_pqc_inspection_c.dto.PqcInspectionC;
import com.hand.hqm.hqm_pqc_inspection_c.mapper.PqcInspectionCMapper;
import com.hand.hqm.hqm_pqc_inspection_d.service.IPqcInspectionDService;
import com.hand.hqm.hqm_pqc_inspection_h.dto.PqcInspectionH;
import com.hand.hqm.hqm_pqc_inspection_h.mapper.PqcInspectionHMapper;
import com.hand.hqm.hqm_pqc_inspection_l.dto.PqcInspectionL;
import com.hand.hqm.hqm_pqc_inspection_l.mapper.PqcInspectionLMapper;
import com.hand.hqm.hqm_pqc_inspection_l.service.IPqcInspectionLService;
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
import com.hand.hqm.hqm_standard_op_ins_h.dto.StandardOpInspectionH;
import com.hand.hqm.hqm_standard_op_ins_h.mapper.StandardOpInspectionHMapper;
import com.hand.hqm.hqm_switch_score.dto.SwitchScore;
import com.hand.hqm.hqm_switch_score.mapper.SwitchScoreMapper;

import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class PqcInspectionLServiceImpl extends BaseServiceImpl<PqcInspectionL> implements IPqcInspectionLService {
	@Autowired
	PqcInspectionLMapper pqcInspectionLMapper;
	@Autowired
	PqcInspectionHMapper pqcInspectionHMapper;
	@Autowired
	PqcInspectionCMapper pqcInspectionCMapper;
	@Autowired
	SwitchScoreMapper switchScoreMapper;
	@Autowired
	StandardOpItemHMapper standardOpItemHMapper;
	@Autowired
	StandardOpItemLMapper standardOpItemLMapper;
	@Autowired
	InspectionAttributeMapper inspectionAttributeMapper;
	@Autowired
	SampleSizeCodeLetterMapper sampleSizeCodeLetterMapper;
	@Autowired
	SampleSchemeMapper sampleSchemeMapper;
	@Autowired
	StandardOpInspectionHMapper standardOpInspectionHMapper;
	@Autowired
	IPromptService iPromptService;
	@Autowired
	SampleManageMapper sampleManageMapper;

	@Autowired
	IPqcInspectionDService iPqcInspectionDService;

	@Override
	public List<PqcInspectionL> myselect(IRequest requestContext, PqcInspectionL dto, int page, int pageSize) {
		// TODO Auto-generated method stub
		PageHelper.startPage(page, pageSize);
		return pqcInspectionLMapper.myselect(dto);
	}

	@Override
	public List<PqcInspectionL> selectfornon(IRequest requestContext, PqcInspectionL dto, int page, int pageSize) {
		// TODO Auto-generated method stub
		PageHelper.startPage(page, pageSize);
		return pqcInspectionLMapper.selectfornon(dto);
	}

	@Override
	public ResponseData detailSave(PqcInspectionL dto, HttpServletRequest request) throws Exception {
		// TODO 明细编辑数据处理
		IRequest requestContext;
		if (request == null) {
			ServiceRequest sr = new ServiceRequest();
			sr.setLocale("zh_CN");
			requestContext = sr;
		} else {
			requestContext = RequestHelper.createServiceRequest(request);
		}

		ResponseData responseData = new ResponseData();
		if (dto.getLineId() == null) {
			responseData.setSuccess(false);
			responseData.setMessage("当前行未保存");
			return responseData;
		}
		PqcInspectionH headSearch = new PqcInspectionH();
		headSearch.setHeaderId(dto.getHeaderId());
		PqcInspectionH headModel = pqcInspectionHMapper.selectByPrimaryKey(headSearch);
		// 获取工厂
		Float plantId = headModel.getPlantId();
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
		// 找检验项目构建模型
		StandardOpInspectionH queryOpH = new StandardOpInspectionH();
		queryOpH.setPlantId(headModel.getPlantId());
		queryOpH.setProdLineId(headModel.getProdLineId());
		queryOpH.setStandardOpId(dto.getStandardOpId());
		queryOpH.setWorkstationId(dto.getWorkstationId());
		queryOpH.setSourceType(headModel.getSourceType());
		StandardOpInspectionH resOpH = standardOpInspectionHMapper.selectOne(queryOpH);
		if (resOpH == null) {
			throw new RuntimeException(SystemApiMethod.getPromptDescription(request, iPromptService,
					"error.hqm_pqc_inspection_create_01"));
		}
		StandardOpItemH queryItemH = new StandardOpItemH();
		queryItemH.setHsoHeadId(resOpH.getHeadId());
		queryItemH.setItemId(Float.valueOf(dto.getItemId()));
		List<StandardOpItemH> resItemH = standardOpItemHMapper.select(queryItemH);
		if (resItemH == null || resItemH.size() == 0) {
			throw new RuntimeException(SystemApiMethod.getPromptDescription(request, iPromptService,
					"error.hqm_pqc_inspection_create_02"));
		}
		if (resItemH.get(0).getStatus().equals("1")) {
			throw new RuntimeException(SystemApiMethod.getPromptDescription(request, iPromptService,
					"error.hqm_pqc_inspection_create_06"));// 模板未发布不能进行工序检验
		}
		List<StandardOpItemL> itemLList = new ArrayList<>();
		StandardOpItemL itemLsearCh = new StandardOpItemL();
		itemLsearCh.setHeadId(resItemH.get(0).getHeadId());
		itemLList = standardOpItemLMapper.select(itemLsearCh);
		Float sampleSize = 1f;// productQty
		deleteAllDetail(dto);
		for (StandardOpItemL standardOpItemL : itemLList) {
			PqcInspectionC searchInsc = new PqcInspectionC();
			searchInsc.setLineId(dto.getLineId());
			searchInsc.setAttributeId(standardOpItemL.getAttributeId());
			PqcInspectionC addorInsc = pqcInspectionCMapper.selectOne(searchInsc);

			if (addorInsc == null) {
				// 新增
				addorInsc = new PqcInspectionC();
				addorInsc.setLineId(dto.getLineId());
				addorInsc.setAttributeId(standardOpItemL.getAttributeId());
			}

			if (dto.getProductionQty() == 1) {
				addorInsc.setSampleQty(1f);
			} else {
				// 抽样标准获取

				// 找抽样方式
				SampleManage smSearch = new SampleManage();
				smSearch.setSampleWayId(Float.valueOf(standardOpItemL.getSampleWayId()));
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
					InspectionAttribute serInsAttr = new InspectionAttribute();
					serInsAttr.setAttributeId(standardOpItemL.getAttributeId());
					InspectionAttribute resInsAttr = inspectionAttributeMapper.selectByPrimaryKey(serInsAttr);
					SampleSizeCodeLetter searchCodeLetter = new SampleSizeCodeLetter();
					searchCodeLetter.setInspectionLevels(sampleManageResult.getInspectionLevels());
					searchCodeLetter.setLotSizeFrom(dto.getProductionQty());
					searchCodeLetter.setLotSizeTo(dto.getProductionQty());
					List<SampleSizeCodeLetter> sizeCodeResultList = new ArrayList<SampleSizeCodeLetter>();
					List<SampleScheme> sampleSchemeResultList = new ArrayList<SampleScheme>();
					if (sampleManageResult.getSampleProcedureType() == null) {
						throw new RuntimeException(SystemApiMethod.getPromptDescription(request, iPromptService,
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
			addorInsc.setSampleWayId(standardOpItemL.getSampleWayId());
			addorInsc.setPrecision(standardOpItemL.getPrecision());
			addorInsc.setAttributeId(standardOpItemL.getAttributeId());
			addorInsc.setLineId(dto.getLineId());
			addorInsc.setTextStandrad(standardOpItemL.getTextStandrad());
			addorInsc.setStandradFrom(standardOpItemL.getStandradFrom());
			addorInsc.setStandradTo(standardOpItemL.getStandradTo());
			addorInsc.setStandradUom(standardOpItemL.getStandradUom());
			addorInsc.setAttributeType(standardOpItemL.getAttributeType());
			// 值插入
			if (addorInsc.getConnectId() == null) {
				pqcInspectionCMapper.insertSelective(addorInsc);
			} else {
				pqcInspectionCMapper.updateByPrimaryKeySelective(addorInsc);
			}
			// add by tianmin.wang 20191017 生成固定的C表对应的D表数据

			iPqcInspectionDService.batchCreateByInspectionC(requestContext, addorInsc);
		}
		// 更新L表
		PqcInspectionL updateInsL = new PqcInspectionL();
		updateInsL.setLineId(dto.getLineId());
		updateInsL.setItemId(dto.getItemId());
		updateInsL.setSourceOrder(dto.getSourceOrder());
		updateInsL.setProductionQty(dto.getProductionQty());
		updateInsL.setInspectionStatus("1");
		updateInsL.setInspectionDate(new Date());

		pqcInspectionLMapper.updateByPrimaryKeySelective(updateInsL);
		// 更新H表
		PqcInspectionH updateInsH = new PqcInspectionH();
		updateInsH.setHeaderId(headModel.getHeaderId());
		updateInsH.setInspectionStatus("3");
		pqcInspectionHMapper.updateByPrimaryKeySelective(updateInsH);

		return responseData;
	}

	/**
	 * 删除相明细行
	 * 
	 * @description
	 * @author tianmin.wang
	 * @date 2019年11月22日
	 * @param dto
	 */
	public void deleteAllDetail(PqcInspectionL dto) {
		PqcInspectionC sear = new PqcInspectionC();
		sear.setLineId(dto.getLineId());
		List<PqcInspectionC> inscList = pqcInspectionCMapper.select(sear);
		for (PqcInspectionC Insc : inscList) {
			iPqcInspectionDService.batchDeleteInspectionC(Insc);
		}
	}
}