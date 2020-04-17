package com.hand.dimension.hqm_dimension_order.service.impl;

import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.system.service.IPromptService;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import com.hand.hap.util.SystemApiMethod;
import com.hand.hqm.hqm_fqc_inspection_h.dto.FqcInspectionH;
import com.hand.hqm.hqm_fqc_inspection_h.mapper.FqcInspectionHMapper;
import com.hand.hqm.hqm_iqc_inspection_h.dto.IqcInspectionH;
import com.hand.hqm.hqm_iqc_inspection_h.mapper.IqcInspectionHMapper;
import com.hand.hqm.hqm_pqc_inspection_h.dto.PqcInspectionH;
import com.hand.hqm.hqm_pqc_inspection_h.mapper.PqcInspectionHMapper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.hand.custom.annotation.LogCustom;
import com.hand.dimension.hqm_dimension_order.dto.DimensionOrder;
import com.hand.dimension.hqm_dimension_order.mapper.DimensionOrderMapper;
import com.hand.dimension.hqm_dimension_order.service.IDimensionOrderService;
import com.hand.dimension.hqm_dimension_problem_description.dto.DimensionProblemDescription;
import com.hand.dimension.hqm_dimension_problem_description.service.IDimensionProblemDescriptionService;
import com.hand.dimension.hqm_dimension_root_cause.dto.DimensionRootCause;
import com.hand.dimension.hqm_dimension_root_cause.service.IDimensionRootCauseService;
import com.hand.dimension.hqm_dimension_step.dto.DimensionStep;
import com.hand.dimension.hqm_dimension_step.mapper.DimensionStepMapper;
import com.hand.dimension.hqm_dimension_step.service.IDimensionStepService;

import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class DimensionOrderServiceImpl extends BaseServiceImpl<DimensionOrder> implements IDimensionOrderService {
	@Autowired
	DimensionOrderMapper dimensionOrderMapper;
	@Autowired
	IDimensionStepService iDimensionStepService;
	@Autowired
	DimensionStepMapper dimensionStepMapper;
	@Autowired
	IDimensionRootCauseService iDimensionRootCauseService;
	@Autowired
	IPromptService iPromptService;
	@Autowired
	FqcInspectionHMapper fqcInspectionHMapper;
	@Autowired
	IDimensionProblemDescriptionService iDimensionProblemDescriptionService;
	@Autowired
	PqcInspectionHMapper pqcInspectionHMapper;
	@Autowired
	IqcInspectionHMapper iqcInspectionHMapper;
	
	
	@Override
	public ResponseData iSponsor(HttpServletRequest request, IRequest requestCtx, IqcInspectionH dto) throws Exception {
		// TODO 来自IQC的发起
		ResponseData responseData = new ResponseData();
		dto = iqcInspectionHMapper.selectByPrimaryKey(dto);
		//8D主表结构数据构建及新建
		DimensionOrder creator = new DimensionOrder();
		creator.setPlantId(dto.getPlantId());
		creator.setSourceType(dto.getInspectionType());
		creator.setSourceOrder(dto.getInspectionNum());
		DimensionOrder dimensionResult = this.addNew(requestCtx, creator);
		//8D的D2表结构构建及创建
		DimensionProblemDescription problemDescriptionCreator = new DimensionProblemDescription();
		problemDescriptionCreator.setItemIdComponent(dto.getItemId());
		problemDescriptionCreator.setSupplierId(dto.getSupplierId());
		problemDescriptionCreator.setOccurTime(dto.getInspectionDate());
		problemDescriptionCreator.setOrderId(dimensionResult.getOrderId());
		iDimensionProblemDescriptionService.saveDetail(requestCtx, problemDescriptionCreator);
		
		List<DimensionOrder> resList = new ArrayList<>();
		resList.add(dimensionResult);
		responseData.setRows(resList);
		return responseData;
	}
	
	@Override
	public ResponseData pSponsor(HttpServletRequest request, IRequest requestCtx, PqcInspectionH dto) throws Exception {
		// TODO 来自PQC的发起
		ResponseData responseData = new ResponseData();
		dto = pqcInspectionHMapper.selectByPrimaryKey(dto);
		//8D主表结构数据构建及新建
		DimensionOrder creator = new DimensionOrder();
		creator.setPlantId(dto.getPlantId());
		creator.setSourceType(dto.getInspectionType());
		creator.setSourceOrder(dto.getInspectionNum());
		DimensionOrder dimensionResult = this.addNew(requestCtx, creator);
		//8D的D2表结构构建及创建
		DimensionProblemDescription problemDescriptionCreator = new DimensionProblemDescription();
		//problemDescriptionCreator.setItemId(dto.getItemId());
		problemDescriptionCreator.setProdLineId(dto.getProdLineId());
		problemDescriptionCreator.setOccurTime(dto.getInspectionDate());
		problemDescriptionCreator.setOrderId(dimensionResult.getOrderId());
		iDimensionProblemDescriptionService.saveDetail(requestCtx, problemDescriptionCreator);
		
		List<DimensionOrder> resList = new ArrayList<>();
		resList.add(dimensionResult);
		responseData.setRows(resList);
		return responseData;
	}
	
	@Override
	public ResponseData foSponsor(HttpServletRequest request, IRequest requestCtx, FqcInspectionH dto) throws Exception {
		// TODO 发起8D 来自FOQC
		ResponseData responseData = new ResponseData();
		dto = fqcInspectionHMapper.selectByPrimaryKey(dto);
		//8D主表结构数据构建及新建
		DimensionOrder creator = new DimensionOrder();
		creator.setPlantId(dto.getPlantId());
		creator.setSourceType(dto.getInspectionType());
		creator.setSourceOrder(dto.getInspectionNum());
		DimensionOrder dimensionResult = this.addNew(requestCtx, creator);
		//8D的D2表结构构建及创建
		DimensionProblemDescription problemDescriptionCreator = new DimensionProblemDescription();
		problemDescriptionCreator.setItemId(dto.getItemId());
		problemDescriptionCreator.setProdLineId(dto.getProdLineId());
		problemDescriptionCreator.setOccurTime(dto.getInspectionDate());
		problemDescriptionCreator.setOrderId(dimensionResult.getOrderId());
		iDimensionProblemDescriptionService.saveDetail(requestCtx, problemDescriptionCreator);
		
		List<DimensionOrder> resList = new ArrayList<>();
		resList.add(dimensionResult);
		responseData.setRows(resList);
		return responseData;
	}
	@Override
	public List<DimensionOrder> reselect(IRequest requestContext, DimensionOrder dto, int pageNum, int pageSize) {
		// TODO Auto-generated method stub
		PageHelper.startPage(pageNum, pageSize);
		return dimensionOrderMapper.reselect(dto);
	}

	@Override
	public DimensionOrder addNew(IRequest requestContext, DimensionOrder dto) {
		// TODO Auto-generated method stub
		// 单号
		String orderCode = getOrderCode();

		dto.setOrderProcess("0");
		dto.setOrderStatus("0");
		dto.setOrderCode(orderCode);
		self().insertSelective(requestContext, dto);
		// 步骤表数据生成
		this.createStep(requestContext, dto);
		// 产生一个鱼骨图初始骨架信息
		this.createDimensionRootCause(requestContext, dto);
		return dto;
	}

	/**
	 * 
	 * @description 创建一个DimensionRootCause 数据
	 * @author tianmin.wang
	 * @date 2019年11月21日 
	 * @param requestContext
	 * @param dto
	 */
	public void createDimensionRootCause(IRequest requestContext, DimensionOrder dto) {
		DimensionRootCause dimen = new DimensionRootCause();
		dimen.setOrderId(dto.getOrderId());
		String rootCause = "Question\n" + "-人/机\n"  + "-料\n" + "-法\n" + "-环" ;
		dimen.setRootCause(rootCause);
		iDimensionRootCauseService.insertSelective(requestContext, dimen);
	}

	/**
	 * 
	 * @description 步骤数据创建
	 * @author tianmin.wang
	 * @date 2019年11月21日 
	 * @param requestContext
	 * @param dto
	 */
	public void createStep(IRequest requestContext, DimensionOrder dto) {
		for (float i = 0; i < 9; i++) {
			if (i == 0) {
				DimensionStep adder = new DimensionStep();
				adder.setOrderId(dto.getOrderId());
				adder.setStatus("2");
				adder.setStep(i);
				iDimensionStepService.insertSelective(requestContext, adder);
				continue;
			}
			DimensionStep adder = new DimensionStep();
			adder.setOrderId(dto.getOrderId());
			adder.setStatus("0");
			adder.setStep(i);
			iDimensionStepService.insertSelective(requestContext, adder);
		}
	}

	/**
	 * 流水一个8D单号
	 * 
	 * @return
	 */
	public String getOrderCode() {
		Integer count = 1;
		String orderCode = "";
		Date date = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		String time = dateFormat.format(date);
		String orderCodePre = "8D" + "-" + time.substring(2) + "-";
		DimensionOrder search = new DimensionOrder();
		search.setOrderCode(orderCodePre);
		List<DimensionOrder> resultList = dimensionOrderMapper.selectMaxNumber(search);
		if (resultList != null && resultList.size() != 0) {
			count = Integer.valueOf(
					resultList.get(0).getOrderCode().substring(resultList.get(0).getOrderCode().length() - 3)) + 1;
		}
		String orderCodeEnd = String.format("%03d", count);
		return orderCodePre + orderCodeEnd;
	}

	@Override
	public boolean changeStepStatus(IRequest request, Float orderId, int currentStep) throws Exception {
		// TODO 改变步骤状态
		DimensionStep dsSearch1 = new DimensionStep();
		dsSearch1.setOrderId(orderId);
		dsSearch1.setStep(Float.valueOf(String.valueOf(currentStep)));
		DimensionStep dsResult1 = dimensionStepMapper.selectOne(dsSearch1);
		// 修改第当前步的状态
		if ("0".equals(dsResult1.getStatus())) {
			DimensionStep updaterStep1 = new DimensionStep();
			updaterStep1.setKid(dsResult1.getKid());
			updaterStep1.setStatus("1");
			iDimensionStepService.updateByPrimaryKeySelective(request, updaterStep1);
		}
		return true;
	}

	@Override
	public boolean changeStep(IRequest request, Float orderId, int currentStep, int nextStep) throws Exception {
		// TODO 改变步骤进程
		DimensionStep dsSearch1 = new DimensionStep();
		dsSearch1.setOrderId(orderId);
		dsSearch1.setStep(Float.valueOf(String.valueOf(currentStep)));
		DimensionStep dsResult1 = dimensionStepMapper.selectOne(dsSearch1);
		// 修改第当前步的状态
		DimensionStep updaterStep1 = new DimensionStep();
		updaterStep1.setKid(dsResult1.getKid());
		updaterStep1.setStatus("2");
		iDimensionStepService.updateByPrimaryKeySelective(request, updaterStep1);
		// 下一步
		if (currentStep == 8) {
			// 第8步为最后一步所以不修改下一步状态
			DimensionOrder updater = new DimensionOrder();
			updater.setOrderId(orderId);
			updater.setOrderProcess(String.valueOf(currentStep));
			updater.setOrderStatus("2");
			self().updateByPrimaryKeySelective(request, updater);
			return true;
		}
		/**
		 * 20190909 不修改下一步状态
		 */
//		DimensionStep dsSearch2 = new DimensionStep();
//		dsSearch2.setOrderId(orderId);
//		dsSearch2.setStep(Float.valueOf(String.valueOf(nextStep)));
//		DimensionStep dsResult2 = dimensionStepMapper.selectOne(dsSearch2);
//		//修改下一步的状态
//		DimensionStep updaterStep2 = new DimensionStep();
//		updaterStep2.setKid(dsResult2.getKid());
//		updaterStep2.setStatus("0");
//		iDimensionStepService.updateByPrimaryKeySelective(request, updaterStep2);
		// 改变8D单进程
		// 更新8D头的信息
		DimensionOrder updater = new DimensionOrder();
		updater.setOrderId(orderId);
		updater.setOrderProcess(String.valueOf(currentStep));
		self().updateByPrimaryKeySelective(request, updater);
		return true;
	}

	@Override
	public int batchClose(HttpServletRequest request, IRequest requestCtx, List<DimensionOrder> dto) throws Exception {
		// TODO Auto-generated method stub
		for (DimensionOrder forModel : dto) {
			DimensionOrder res = self().selectByPrimaryKey(null, forModel);
			if (!"2".equals(res.getOrderStatus())) {
				throw new Exception(
						SystemApiMethod.getPromptDescription(request, iPromptService, "error.hqm_8d_order_03"));// 只有已完成的8D单才可以关闭
			}
			forModel.setOrderStatus("3");
			self().updateByPrimaryKeySelective(requestCtx, forModel);
		}
		return 0;
	}

	@Override
	public int commit(HttpServletRequest request, IRequest requestCtx, DimensionOrder dto) throws Exception {
		// TODO 提交
		DimensionStep search = new DimensionStep();
		search.setOrderId(dto.getOrderId());
		List<DimensionStep> stepList = dimensionStepMapper.select(search);
		for (DimensionStep forIn : stepList) {
			if (!"2".equals(forIn.getStatus())) {
				throw new Exception(
						SystemApiMethod.getPromptDescription(request, iPromptService, "error.hqm_8d_order_02"));
			}
		}
		// 开始更新8D状态
		dto.setOrderStatus("2");
		self().updateByPrimaryKeySelective(requestCtx, dto);
		return 0;
	}

	@Override
	public int saveOne(HttpServletRequest request, IRequest requestCtx, DimensionOrder dto) {
		// TODO Auto-generated method stub
		self().updateByPrimaryKeySelective(requestCtx, dto);
		return 0;
	}
	

}