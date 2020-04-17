package com.hand.hqm.hqm_qc_task.service.impl;

import com.github.pagehelper.PageHelper;
import com.hand.hap.core.IRequest;
import com.hand.hap.core.impl.RequestHelper;
import com.hand.hap.core.impl.ServiceRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.system.service.ICodeService;
import com.hand.hap.system.service.IPromptService;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import com.hand.hap.util.SoapPostUtil;
import com.hand.hap.util.SystemApiMethod;
import com.hand.hap.webservice.ws.idto.FqcOutLineInspection;
import com.hand.hap.webservice.ws.idto.RecallTestInfo;
import com.hand.hap.util.SoapPostUtil.Response;
import com.hand.hap.util.SoapPostUtil.ResponseSap;
import com.hand.hcm.hcm_item.dto.Item;
import com.hand.hcm.hcm_item.mapper.ItemMapper;
import com.hand.hcm.hcm_item_b.dto.ItemB;
import com.hand.hcm.hcm_item_b.mapper.ItemBMapper;
import com.hand.hcm.hcm_item_category.dto.ItemCategory;
import com.hand.hcm.hcm_item_category.mapper.ItemCategoryMapper;
import com.hand.hcm.hcm_plant.dto.Plant;
import com.hand.hcm.hcm_plant.mapper.PlantMapper;
import com.hand.hcm.hcm_production_line.dto.ProductionLine;
import com.hand.hcm.hcm_production_line.mapper.ProductionLineMapper;
import com.hand.hcs.hcs_suppliers.dto.Suppliers;
import com.hand.hcs.hcs_suppliers.mapper.SuppliersMapper;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;
import com.hand.hqm.hqm_fqc_inspection_h.dto.FqcInspectionH;
import com.hand.hqm.hqm_fqc_inspection_h.mapper.FqcInspectionHMapper;
import com.hand.hqm.hqm_fqc_inspection_h.service.IFqcInspectionHService;
import com.hand.hqm.hqm_iqc_inspection_h.dto.IqcInspectionH;
import com.hand.hqm.hqm_iqc_inspection_template_h.dto.IqcInspectionTemplateH;
import com.hand.hqm.hqm_iqc_inspection_template_h.mapper.IqcInspectionTemplateHMapper;
import com.hand.hqm.hqm_item_type_tests.dto.ItemTypeTests;
import com.hand.hqm.hqm_item_type_tests.mapper.ItemTypeTestsMapper;
import com.hand.hqm.hqm_qc_task.dto.FqcTask;
import com.hand.hqm.hqm_qc_task.mapper.FqcTaskMapper;
import com.hand.hqm.hqm_qc_task.service.IFqcTaskService;
import com.hand.sys.sys_if_invoke_outbound.dto.IfInvokeOutbound;
import com.hand.sys.sys_if_invoke_outbound.mapper.IfInvokeOutboundMapper;
import com.hand.sys.sys_user.dto.UserSys;
import com.hand.sys.sys_user.mapper.UserSysMapper;
import jodd.util.StringUtil;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class FqcTaskServiceImpl extends BaseServiceImpl<FqcTask> implements IFqcTaskService {
	@Autowired
	FqcTaskMapper mapper;
	@Autowired
	IFqcInspectionHService iFqcInspectionHService;
	@Autowired
	FqcInspectionHMapper fqcInspectionHMapper;
	@Autowired
	SuppliersMapper suppliersMapper;
	@Autowired
	PlantMapper plantMapper;
	@Autowired
	UserSysMapper userSysMapper;
	@Autowired
	IPromptService iPromptService;
	@Autowired
	ICodeService iCodeService;
	@Autowired
	IfInvokeOutboundMapper ifInvokeOutboundMapper;
	@Autowired
	ItemMapper itemMapper;
	@Autowired
	ItemBMapper itemBMapper;
	@Autowired
	ProductionLineMapper productionLineMapper;
	@Autowired
	ItemCategoryMapper itemCategoryMapper;
	@Autowired
	ItemTypeTestsMapper itemTypeTestsMapper;
	@Autowired
	IqcInspectionTemplateHMapper iqcInspectionTemplateHMapper;
	@Resource(name = "taskExecutor")
	private TaskExecutor taskExecutor;
	Logger logger = LoggerFactory.getLogger(FqcTaskServiceImpl.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hand.hqm.hqm_qc_task.service.IFqcTaskService#transferFqcTask()
	 */
	@Override
	public Response transferFqcTask() {
		Response re = new Response();
		FqcTask te = new FqcTask();
//		((IqcTaskServiceImpl) AopContext.currentProxy()).generatorByInterface(te);
		try {
			generatorByInterface(te);
		} catch (Exception e) {
			re.setResult(false);
			re.setMessage(e.getMessage());
		}
		return re;
	}

	/**
	 * @description
	 * @author tianmin.wang
	 * @date 2019年11月19日
	 * @param te
	 * @throws Exception
	 */
	@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
	private void generatorByInterface(FqcTask fqctask) throws Exception {
		FqcInspectionH fqcinspectionh = new FqcInspectionH();
		fqcinspectionh.setItemId(fqctask.getItemId());
		fqcinspectionh.setPlantId(fqctask.getPlantId());
		fqcinspectionh.setPlantCode(getPlantById(fqctask.getPlantId()).getPlantCode());
		fqcinspectionh.setInspectionType("FQC");
		fqcinspectionh.setProduceQty(fqctask.getProductQty());
		fqcinspectionh.setProduceDate(fqctask.getProductDate());
		fqcinspectionh.setSourceOrder(fqctask.getSourceOrder());
		fqcinspectionh.setInspectionFrom("MES");
		fqcinspectionh.setProductionBatch(fqctask.getProductionBatch());
		fqcinspectionh.setSourceType(fqctask.getSourceType());
		fqcinspectionh.setProdLineId(fqctask.getProdLineId());
		ResponseData rd = iFqcInspectionHService.addNewInspection(fqcinspectionh, null, null);
		fqctask.setInspectionNum(((IqcInspectionH) rd.getRows().get(0)).getInspectionNum());
	}

	public String getSourceType(Float itemId, Float plantId) {
		FqcTask its = new FqcTask();
		its.setItemId(itemId);
		its.setPlantId(plantId);
		List<FqcTask> res = mapper.select(its);
		if (res != null && res.size() > 0) {
			if (res.stream().filter(p -> !StringUtil.isEmpty(p.getInspectionNum())).count() > 0) {
				return "1";
			}
			return "4";// 首件检验

		} else {
			return "4";// VTP实验
		}
	}

	public Float getUserIdByName(String name) {
		UserSys us = new UserSys();
		us.setUserName(name);
		return userSysMapper.select(us).get(0).getUserId();
	}

	public Suppliers getSupplierById(Float kid) {
		Suppliers sus = new Suppliers();
		sus.setSupplierId(kid);
		return suppliersMapper.select(sus).get(0);
	}

	public Plant getPlantById(Float kid) {
		Plant sus = new Plant();
		sus.setPlantId(kid);
		return plantMapper.select(sus).get(0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.hand.hqm.hqm_qc_task.service.IFqcTaskService#reSelect(com.hand.hap.core.
	 * IRequest, com.hand.hqm.hqm_qc_task.dto.FqcTask, int, int)
	 */
	@Override
	public List<FqcTask> reSelect(IRequest requestContext, FqcTask dto, int page, int pageSize) {
		PageHelper.startPage(page, pageSize);
		return mapper.reSelect(dto);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.hand.hqm.hqm_qc_task.service.IFqcTaskService#createFqc(java.util.List,
	 * javax.servlet.http.HttpServletRequest)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
	public void createFqc(List<FqcTask> dto, HttpServletRequest request) throws Exception {
		IRequest requestCtx;
		if (request == null) {
			ServiceRequest sr = new ServiceRequest();
			sr.setLocale("zh_CN");
			sr.setUserId(-1l);
			requestCtx = sr;
		} else {
			requestCtx = RequestHelper.createServiceRequest(request);
		}
		// TODO 生成报告
		ResponseData res = new ResponseData();
		List<FqcInspectionH> lis = new ArrayList();
		for (FqcTask fqcTask : dto) {
			FqcTask fqcTaskRes = mapper.selectByPrimaryKey(fqcTask);
			if (StringUtil.isNotEmpty(fqcTaskRes.getInspectionNum())) {
				throw new RuntimeException(SystemApiMethod.getPromptDescription(requestCtx, iPromptService,
						"error.hqm_iqc_order_create_20"));// 该检验通知已生成检验单
			}
			generatorByButton(fqcTaskRes, request);
			fqcTask.setInspectionNum(fqcTaskRes.getInspectionNum());
			FqcInspectionH search = new FqcInspectionH();
			search.setInspectionNum(fqcTaskRes.getInspectionNum());
			lis.addAll(fqcInspectionHMapper.select(search));
		}
		// 发送给 WMS的接口
		res.setRows(lis);
		iFqcInspectionHService.samplingFeedback(res);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
	public void createFqc(FqcTask dto, HttpServletRequest request) throws Exception {
		IRequest requestCtx;
		if (request == null) {
			ServiceRequest sr = new ServiceRequest();
			sr.setLocale("zh_CN");
			sr.setUserId(-1l);
			requestCtx = sr;
		} else {
			requestCtx = RequestHelper.createServiceRequest(request);
		}
		// TODO 生成报告
		FqcTask fqcTaskRes = dto;
		generatorByButtonNoUpdate(fqcTaskRes, request);
		dto.setInspectionNum(fqcTaskRes.getInspectionNum());

		ResponseData res = new ResponseData();
		FqcInspectionH search = new FqcInspectionH();
		search.setInspectionNum(fqcTaskRes.getInspectionNum());
		res.setRows(fqcInspectionHMapper.select(search));
		iFqcInspectionHService.samplingFeedback(res);
	}

	public void generatorByButtonNoUpdate(FqcTask fqctask, HttpServletRequest request) throws Exception {
		FqcInspectionH fqcinspectionh = new FqcInspectionH();
		fqcinspectionh.setItemId(fqctask.getItemId());
		fqcinspectionh.setPlantId(fqctask.getPlantId());
		fqcinspectionh.setPlantCode(getPlantById(fqctask.getPlantId()).getPlantCode());
		fqcinspectionh.setInspectionType("FQC");
		fqcinspectionh.setProduceQty(fqctask.getProductQty());
		fqcinspectionh.setProduceDate(fqctask.getProductDate());
		fqcinspectionh.setSourceOrder(fqctask.getSourceOrder());
		fqcinspectionh.setInspectionFrom("MES");
		fqcinspectionh.setProductionBatch(fqctask.getProductionBatch());
		fqcinspectionh.setSourceType(fqctask.getSourceType());
		fqcinspectionh.setItemEdition(fqctask.getItemVersion());
		fqcinspectionh.setEmergencyFlag(fqctask.getIsUrgency());
		fqcinspectionh.setProdLineId(fqctask.getProdLineId());
		iFqcInspectionHService.addNewInspection(fqctask, fqcinspectionh, request);
		fqctask.setInspectionNum(fqcinspectionh.getInspectionNum());
		// mapper.updateByPrimaryKeySelective(fqctask);
		webServiceMes(fqcinspectionh);
	}

	public void generatorByButton(FqcTask fqctask, HttpServletRequest request) throws Exception {
		FqcInspectionH fqcinspectionh = new FqcInspectionH();
		fqcinspectionh.setItemId(fqctask.getItemId());
		fqcinspectionh.setPlantId(fqctask.getPlantId());
		fqcinspectionh.setPlantCode(getPlantById(fqctask.getPlantId()).getPlantCode());
		fqcinspectionh.setInspectionType("FQC");
		fqcinspectionh.setProduceQty(fqctask.getProductQty());
		fqcinspectionh.setProduceDate(fqctask.getProductDate());
		fqcinspectionh.setSourceOrder(fqctask.getSourceOrder());
		fqcinspectionh.setInspectionFrom("MES");
		fqcinspectionh.setProductionBatch(fqctask.getProductionBatch());
		fqcinspectionh.setSourceType(fqctask.getSourceType());
		fqcinspectionh.setItemEdition(fqctask.getItemVersion());
		fqcinspectionh.setEmergencyFlag(fqctask.getIsUrgency());
		fqcinspectionh.setProdLineId(fqctask.getProdLineId());
		iFqcInspectionHService.addNewInspection(fqctask, fqcinspectionh, request);
		fqctask.setInspectionNum(fqcinspectionh.getInspectionNum());
		mapper.updateByPrimaryKeySelective(fqctask);
		webServiceMes(fqcinspectionh);
	}

	/**
	 * 
	 * @description mes 抽样数量反馈 接口请求
	 * @author tianmin.wang
	 * @date 2020年2月21日
	 * @param fqch
	 */
	public void webServiceMes(FqcInspectionH fqch) {
		taskExecutor.execute(() -> {
			IfInvokeOutbound iio = new IfInvokeOutbound();
			try {
				String param = "{" + "\"orderNo\":" + "\"" + fqch.getSourceOrder() + "\"," + "\"sampleNumber\":" + "\""
						+ fqch.getSampleQty() + "\"" + "}";
				ServiceRequest sr = new ServiceRequest();
				sr.setLocale("zh_CN");
				String uri = iCodeService.getCodeValueByMeaning(sr, "HAP.SYSTEM", "MES_WS_URI");// 获取调用地址
				SoapPostUtil.Response re = SoapPostUtil.ticketSrmToMes(uri, "getOrderSampleQuantityFeedback", param,
						iio);
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

	@Override
	@Transactional(rollbackFor = Exception.class)
	public ResponseSap fqcRecallRetest(RecallTestInfo dto, List<FqcTask> fqcTaskDto) throws Exception {
		ResponseSap response = new ResponseSap();
		FqcTask fqcTask = new FqcTask();
		fqcTask.setCreatedBy(new Long((long) -1));
		fqcTask.setCreationDate(new Date());
		fqcTask.setLastUpdatedBy(new Long((long) -1));
		fqcTask.setLastUpdateDate(new Date());
		// 查询工厂id
		Plant plant = new Plant();
		plant.setPlantCode(dto.getPlantCode());
		List<Plant> plantList = plantMapper.select(plant);
		if (plantList != null && plantList.size() > 0) {
			fqcTask.setPlantId(plantList.get(0).getPlantId());
		} else {
			response.setResult(false);
			response.setError_info("工厂编码在工厂表中找不到！");
			return response;
		}
		fqcTask.setSourceOrder(dto.getSourceCode());
		fqcTask.setSourceType("6");
		// 查询物料id
		Item item = new Item();
		item.setItemCode(dto.getItemCode());
		item.setPlantId(plantList.get(0).getPlantId());
		List<Item> itedtoist = itemMapper.select(item);
		if (itedtoist != null && itedtoist.size() > 0) {
			fqcTask.setItemId(itedtoist.get(0).getItemId());
		} else {
			response.setResult(false);
			response.setError_info("物料id在物料表中找不到！");
			return response;
		}
		fqcTask.setItemVersion(dto.getItemVersion());
		fqcTask.setInspectionType("FQC");
		fqcTask.setProductQty(dto.getReceiveQty());
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");// 日期格式
		Date date = format.parse(dto.getEvenTime());
		fqcTask.setProductDate(date);
		fqcTask.setTaskFrom("WMS");
		fqcTask.setSolveStatus("N");
		// 新增数据
		mapper.insertSelective(fqcTask);
		fqcTaskDto.add(fqcTask);
		response.setResult(true);

		return response;
	}

	@Override
	public ResponseSap fqcOutLineInspection(FqcOutLineInspection foi) throws ParseException, Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		ResponseSap responsesap = new ResponseSap();
		FqcTask search = new FqcTask();
		Float plantId = getPlantIdByCode(foi.plantCode);
		search.setPlantId(plantId);
		search.setSourceOrder(foi.orderNo);
		List<FqcTask> result = mapper.select(search);
		if (result == null || result.size() == 0) {
			if ("VTP".equals(foi.orderType) || "ECR".equals(foi.orderType)) // ECR VTP试验
				fqcOutLineInspectionVtp(search, foi, sdf);
			else if ("TVTP".equals(foi.orderType)) // TVTP 二次VTP试验
				fqcOutLineInspectionTvtp(search, foi, sdf);
			else // 其他
				fqcOutLineInspectionEcr(search, foi, sdf);
		}
		return responsesap;
	}

	public void fqcOutLineInspectionTvtp(FqcTask ft, FqcOutLineInspection foi, SimpleDateFormat sdf) throws Exception {
		IqcInspectionTemplateH templateSearch = new IqcInspectionTemplateH();
		templateSearch.setMainType("FQC");
		templateSearch.setVtpNumebr(foi.vtpNumber);
		templateSearch.setSourceType("16");
		List<IqcInspectionTemplateH> result = iqcInspectionTemplateHMapper.select(templateSearch);
		ft.setItemId(getItemIdByCode(foi.itemCode, ft.getPlantId()));
		ft.setProdLineId(getProdLineIdByCode(foi.line));
		ft.setOriginalOrder(foi.preOrderNo);
		ft.setOrderType(foi.orderType);
		ft.setSourceType("16");// FQC 二次VTP试验
		ft.setItemVersion(foi.itemVersion);
		ft.setInspectionType("FQC");
		ft.setVtpNumber(foi.vtpNumber);
		ft.setProductQty(Float.valueOf(StringUtils.isEmpty(foi.qty) ? "0" : foi.qty));
		ft.setProductDate(sdf.parse(foi.productDate));
		if (result != null && result.size() > 0) {
			mapper.insertSelective(ft);
			try {
				self().createFqc(ft, null);
			} catch (Exception e) {
				logger.warn("FQC创建失败:" + e.getMessage());
			}
			result.forEach(p -> {
				p.setEnableFlag("N");
				iqcInspectionTemplateHMapper.updateByPrimaryKeySelective(p);
			});
		} else {
			ft.setErrorMsg("检验单模板不存在");
			mapper.insertSelective(ft);
		}
	}

	public void fqcOutLineInspectionVtp(FqcTask ft, FqcOutLineInspection foi, SimpleDateFormat sdf) throws Exception {
		// 如果属于Vtp 生成 VTP的通知和 形式实验的通知 共两条
		ft.setItemId(getItemIdByCode(foi.itemCode, ft.getPlantId()));
		ft.setProdLineId(getProdLineIdByCode(foi.line));
		ft.setOriginalOrder(foi.preOrderNo);
		ft.setSourceType("7");// FQC型式检验
		ft.setItemVersion(foi.itemVersion);
		ft.setInspectionType("FQC");
		ft.setOrderType(foi.orderType);
		ft.setVtpNumber(foi.vtpNumber);
		ft.setProductQty(Float.valueOf(StringUtils.isEmpty(foi.qty) ? "0" : foi.qty));
		ft.setProductDate(sdf.parse(foi.productDate));
		mapper.insertSelective(ft);
		try {
			self().createFqc(ft, null);
			mapper.updateByPrimaryKeySelective(ft);
		} catch (Exception e) {
			logger.warn("FQC创建失败:" + e.getMessage());
		}
		ft.setSourceType("15");// FQCVTP检验
		mapper.insertSelective(ft);
		try {
			self().createFqc(ft, null);
			mapper.updateByPrimaryKeySelective(ft);
		} catch (Exception e) {
			logger.warn("FQC创建失败:" + e.getMessage());
		}
	}

	public void fqcOutLineInspectionEcr(FqcTask ft, FqcOutLineInspection foi, SimpleDateFormat sdf) throws Exception {
		// 生成一条来源类型为首次实验的检验通知
		ft.setItemId(getItemIdByCode(foi.itemCode, ft.getPlantId()));
		ft.setProdLineId(getProdLineIdByCode(foi.line));
		ft.setOriginalOrder(foi.preOrderNo);
		ft.setSourceType("6");// FQC首件检
		ft.setItemVersion(foi.itemVersion);
		ft.setInspectionType("FQC");
		ft.setOrderType(foi.orderType);
		ft.setVtpNumber(foi.vtpNumber);
		ft.setProductQty(Float.valueOf(StringUtils.isEmpty(foi.qty) ? "0" : foi.qty));
		ft.setProductDate(sdf.parse(foi.productDate));
		mapper.insertSelective(ft);
		try {
			self().createFqc(ft, null);
			mapper.updateByPrimaryKeySelective(ft);
		} catch (Exception e) {
			logger.warn("FQC创建失败:" + e.getMessage());
		}
		ft.setSourceType("19");//VTP-寿命检验
		mapper.insertSelective(ft);
		try {
			self().createFqc(ft, null);
			mapper.updateByPrimaryKeySelective(ft);
		} catch (Exception e) {
			logger.warn("FQC创建失败:" + e.getMessage());
		}
		// 同时继续判断 是否要生成形式试验的检验通知
		ItemCategory icSearch = new ItemCategory();
		icSearch.setItemId(ft.getItemId());
		icSearch.setPlantId(ft.getPlantId());
		List<ItemCategory> icResult = itemCategoryMapper.select(icSearch);
		if (icResult == null || icResult.size() == 0)
			return;
		ItemTypeTests itSearch = new ItemTypeTests();
		itSearch.setCategoryId(icResult.get(0).getCategoryId());
		List<ItemTypeTests> itResult = itemTypeTestsMapper.select(itSearch);
		if (itResult == null || itResult.stream().filter(p -> "2".equals(p.getTestType())).count() == 0)
			return;
		if ((itResult.get(0).getTotalQty() == null ? 0f : itResult.get(0).getTotalQty()) + ft
				.getProductQty() > (itResult.get(0).getTriggerNum() == null ? 0f : itResult.get(0).getTriggerNum())) {
			// 生成任务 修改 物料类别的total_qty = 0
			itResult.get(0).setTotalQty(0f);
			itemCategoryMapper.updateByPrimaryKeySelective(icSearch);
			ft.setSourceType("7");
			mapper.insertSelective(ft);
			try {
				self().createFqc(ft, null);
				mapper.updateByPrimaryKeySelective(ft);
			} catch (Exception e) {
				logger.warn("FQC创建失败:" + e.getMessage());
			}
		} else {
			itResult.get(0).setTotalQty(
					(itResult.get(0).getTotalQty() == null ? 0f : itResult.get(0).getTotalQty()) + ft.getProductQty());
			itemCategoryMapper.updateByPrimaryKeySelective(icSearch);
		}

	}

	public Float getPlantIdByCode(String plantCode) {
		Plant sear = new Plant();
		sear.setPlantCode(plantCode);
		return plantMapper.select(sear).get(0).getPlantId();

	}

	public Float getItemIdByCode(String itemCode, Float plantId) {
		ItemB search = new ItemB();
		search.setItemCode(itemCode);
		search.setPlantId(plantId);
		return itemBMapper.select(search).get(0).getItemId();
	}

	public Float getProdLineIdByCode(String prodLineCode) {
		ProductionLine search = new ProductionLine();
		search.setProdLineCode(prodLineCode);
		return productionLineMapper.select(search).get(0).getProdLineId();
	}
}