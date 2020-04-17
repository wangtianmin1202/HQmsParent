package com.hand.hqm.hqm_qc_task.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageHelper;
import com.hand.hap.core.IRequest;
import com.hand.hap.core.impl.RequestHelper;
import com.hand.hap.core.impl.ServiceRequest;
import com.hand.hap.system.service.ICodeService;
import com.hand.hap.system.service.IPromptService;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import com.hand.hap.util.SoapPostUtil;
import com.hand.hap.util.SystemApiMethod;
import com.hand.hap.webservice.ws.idto.ItemStock;
import com.hand.hap.util.SoapPostUtil.ResponseSap;
import com.hand.hcm.hcm_item.dto.Item;
import com.hand.hcm.hcm_item.mapper.ItemMapper;
import com.hand.hcm.hcm_item_b.dto.ItemB;
import com.hand.hcm.hcm_item_b.mapper.ItemBMapper;
import com.hand.hcm.hcm_plant.dto.Plant;
import com.hand.hcm.hcm_plant.mapper.PlantMapper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;

import com.hand.hqm.hqm_fqc_inspection_h.dto.FqcInspectionH;
import com.hand.hqm.hqm_fqc_inspection_h.service.IFqcInspectionHService;
import com.hand.hqm.hqm_qc_task.dto.FqcTask;
import com.hand.hqm.hqm_qc_task.dto.OqcTask;
import com.hand.hqm.hqm_qc_task.idto.OqcSampleQty;
import com.hand.hqm.hqm_qc_task.mapper.OqcTaskMapper;
import com.hand.hqm.hqm_qc_task.service.IOqcTaskService;
import com.hand.sys.sys_if_invoke_outbound.dto.IfInvokeOutbound;
import com.hand.sys.sys_if_invoke_outbound.mapper.IfInvokeOutboundMapper;

import jodd.util.StringUtil;

import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class OqcTaskServiceImpl extends BaseServiceImpl<OqcTask> implements IOqcTaskService {

	@Autowired
	OqcTaskMapper mapper;
	@Autowired
	IPromptService iPromptService;
	@Autowired
	IFqcInspectionHService iFqcInspectionHService;
	@Autowired
	PlantMapper plantMapper;
	@Autowired
	ItemMapper itemMapper;
	@Autowired
	ICodeService iCodeService;
	@Autowired
	IfInvokeOutboundMapper ifInvokeOutboundMapper;
	@Autowired
	ItemBMapper itemBMapper;
	@Resource(name = "taskExecutor")
	private TaskExecutor taskExecutor;
	Logger logger = LoggerFactory.getLogger(FqcTaskServiceImpl.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.hand.hqm.hqm_qc_task.service.IOqcTaskService#reSelect(com.hand.hap.core.
	 * IRequest, com.hand.hqm.hqm_qc_task.dto.OqcTask, int, int)
	 */
	@Override
	public List<OqcTask> reSelect(IRequest requestContext, OqcTask dto, int page, int pageSize) {
		// TODO Auto-generated method stub
		PageHelper.startPage(page, pageSize);
		return mapper.reSelect(dto);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.hand.hqm.hqm_qc_task.service.IOqcTaskService#createOqc(javax.servlet.http
	 * .HttpServletRequest, java.util.List)
	 */
	@Override
	public void createOqc(HttpServletRequest request, List<OqcTask> dto) throws Exception {
		for (OqcTask oqcTask : dto) {
			OqcTask oqcTaskRes = mapper.selectByPrimaryKey(oqcTask);
			if (StringUtil.isNotEmpty(oqcTaskRes.getInspectionNum())) {
				throw new RuntimeException(
						SystemApiMethod.getPromptDescription(request, iPromptService, "error.hqm_iqc_order_create_20"));// 该检验通知已生成检验单
			}
			generatorByButton(oqcTaskRes, request);
			oqcTask.setInspectionNum(oqcTaskRes.getInspectionNum());
		}
	}

	/**
	 * @description
	 * @author tianmin.wang
	 * @date 2020年1月8日
	 * @param oqcTaskRes
	 * @param request
	 * @throws Exception
	 */
	private void generatorByButton(OqcTask oqcTaskRes, HttpServletRequest request) throws Exception {
		IRequest ir = RequestHelper.createServiceRequest(request);
		FqcInspectionH dto = new FqcInspectionH();
		dto.setPlantId(oqcTaskRes.getPlantId());
		dto.setInspectionFrom("WMS");
		dto.setItemId(oqcTaskRes.getItemId());
		dto.setProductionBatch(oqcTaskRes.getProductionBatch());
		dto.setSourceType("9");
		dto.setPlantCode(getPlantCodeById(dto.getPlantId()));
		dto.setItemEdition(oqcTaskRes.getItemVersion());
		dto.setProduceQty(oqcTaskRes.getReceiveQty());
		dto.setEmergencyFlag(oqcTaskRes.getIsUrgency());
		dto.setInspectionType("OQC");
		dto.setItemEdition(oqcTaskRes.getItemVersion());
		iFqcInspectionHService.addNewInspection(dto, ir, request);
		oqcTaskRes.setInspectionNum(dto.getInspectionNum());
		oqcTaskRes.setSolveStatus("Y");
		self().updateByPrimaryKeySelective(ir, oqcTaskRes);
		sendDataToMes(oqcTaskRes, dto);
	}

	private void sendDataToMes(OqcTask oqcTask, FqcInspectionH fqcInspectionH) {
		OqcSampleQty os = new OqcSampleQty();
		os.plantCode = "CNKE";
		os.inspectionNum = fqcInspectionH.getInspectionNum();
		os.itemCode = getItemCodeById(oqcTask.getItemId(), oqcTask.getPlantId());
		os.itemVersion = oqcTask.getItemVersion();
		os.spreading = oqcTask.getSpreading();
		os.lotNumber = oqcTask.getProductionBatch();
		os.sampleQty = String.valueOf(fqcInspectionH.getSampleQty().intValue());
		taskExecutor.execute(() -> {
			ObjectMapper om = new ObjectMapper();
			IfInvokeOutbound iio = new IfInvokeOutbound();
			try {
				String param = om.writeValueAsString(os);
				ServiceRequest sr = new ServiceRequest();
				sr.setLocale("zh_CN");
				String uri = iCodeService.getCodeValueByMeaning(sr, "HAP.SYSTEM", "WMS_WS_URI");// 获取调用地址
				SoapPostUtil.Response re = SoapPostUtil.ticketSrmToWms("getOqcSampleQty", param, iio, uri);
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

	private String getPlantCodeById(Float plantId) {
		Plant pl = new Plant();
		pl.setPlantId(plantId);
		return plantMapper.select(pl).get(0).getPlantCode();
	}

	private String getItemCodeById(Float itemId, Float plantId) {
		ItemB ib = new ItemB();
		ib.setItemId(itemId);
		ib.setPlantId(plantId);
		return itemBMapper.select(ib).get(0).getItemCode();
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public ResponseSap ItemStockRetest(ItemStock dto) throws Exception {
		ResponseSap response = new ResponseSap();
		OqcTask oqcTask = new OqcTask();

		// 查询工厂id
		Plant plant = new Plant();
		plant.setPlantCode(dto.getPlantCode());
		List<Plant> plantList = plantMapper.select(plant);
		if (plantList != null && plantList.size() > 0) {
			oqcTask.setPlantId(plantList.get(0).getPlantId());
		} else {
			response.setResult(false);
			response.setError_info("工厂编码在工厂表中找不到！");
			return response;
		}

		// 查询物料id
		Item item = new Item();
		item.setItemCode(dto.getItemCode());
		item.setPlantId(plantList.get(0).getPlantId());
		List<Item> itedtoist = itemMapper.select(item);
		if (itedtoist != null && itedtoist.size() > 0) {
			oqcTask.setItemId(itedtoist.get(0).getItemId());
		} else {
			response.setResult(false);
			response.setError_info("物料id在物料表中找不到！");
			return response;
		}

		oqcTask.setItemVersion(dto.getItemVersion());
		oqcTask.setSpreading(dto.getSpreading());
		oqcTask.setProductionBatch(dto.getLotNumber());
		oqcTask.setReceiveQty(dto.getQty());
		oqcTask.setWarehouseCode(dto.getWarehouseCode());

		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");// 日期格式
		Date date = format.parse(dto.getWarningDate());
		oqcTask.setWarningDate(date);

		oqcTask.setSolveStatus("N");

		// 新增数据
		mapper.insertSelective(oqcTask);

		response.setResult(true);

		return response;
	}

}