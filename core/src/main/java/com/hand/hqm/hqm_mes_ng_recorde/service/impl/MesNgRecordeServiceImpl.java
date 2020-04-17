package com.hand.hqm.hqm_mes_ng_recorde.service.impl;

import com.hand.hap.system.service.impl.BaseServiceImpl;
import com.hand.hap.util.SoapPostUtil.ResponseSap;
import com.hand.hap.webservice.ws.idto.SerialDisqualified;
import com.hand.hap.webservice.ws.idto.SerialDisqualifiedLine;
import com.hand.hcm.hcm_item_b.dto.ItemB;
import com.hand.hcm.hcm_item_b.mapper.ItemBMapper;
import com.hand.hcm.hcm_plant.dto.Plant;
import com.hand.hcm.hcm_plant.mapper.PlantMapper;
import com.hand.hcm.hcm_production_line.dto.ProductionLine;
import com.hand.hcm.hcm_production_line.mapper.ProductionLineMapper;
import java.text.SimpleDateFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.hand.hqm.hqm_mes_ng_recorde.dto.MesNgRecorde;
import com.hand.hqm.hqm_mes_ng_recorde.mapper.MesNgRecordeMapper;
import com.hand.hqm.hqm_mes_ng_recorde.service.IMesNgRecordeService;
import com.hand.hqm.hqm_mes_ng_recorde_line.dto.MesNgRecordeLine;
import com.hand.hqm.hqm_mes_ng_recorde_line.mapper.MesNgRecordeLineMapper;
import com.hand.hqm.hqm_sop_pqc_control_l.dto.SopPqcControlL;
import com.hand.hqm.hqm_sop_pqc_control_l.mapper.SopPqcControlLMapper;
import org.springframework.transaction.annotation.Transactional;
@Service
@Transactional(rollbackFor = Exception.class)
public class MesNgRecordeServiceImpl extends BaseServiceImpl<MesNgRecorde> implements IMesNgRecordeService {
	@Autowired
	MesNgRecordeMapper mapper;
	@Autowired
	MesNgRecordeLineMapper mesNgRecordeLineMapper;
	@Autowired
	PlantMapper plantMapper;
	@Autowired
	ItemBMapper itemBMapper;
	@Autowired
	ProductionLineMapper productionLineMapper;
	@Autowired
	SopPqcControlLMapper sopPqcControlLMapper;
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hand.hqm.hqm_mes_ng_recorde.service.IMesNgRecordeService#
	 * serialDisqualified(com.hand.hap.webservice.ws.idto.SerialDisqualified)
	 */
	@Override
	public ResponseSap serialDisqualified(SerialDisqualified sdf) throws Exception {
		SimpleDateFormat sdaf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		ResponseSap response = new ResponseSap();
		MesNgRecorde mnr = new MesNgRecorde();
		Float plantId = getPlantIdByCode(sdf.plantCode);
		mnr.setPlantId(plantId);
		mnr.setTaskType(sdf.taskType);
		mnr.setTaskNumber(sdf.taskNo);
		mnr.setProdLineId(getProdLineIdByCode(sdf.line));
		mnr.setWorkstationsId(getWorkStationIdByCode(sdf.workStation));
		mnr.setEventTime(sdaf.parse(sdf.eventTime));
		mapper.insertSelective(mnr);
		for (SerialDisqualifiedLine sdfl : sdf.recordLines) {
			MesNgRecordeLine linsert = new MesNgRecordeLine();
			linsert.setOrderNum(sdfl.orderNo);
			linsert.setItemId(getItemIdByCode(sdfl.itemCode,plantId));
			linsert.setSerialNumber(sdfl.serialNo);
			linsert.setNgCode(sdfl.ngCode);
			linsert.setTaskId(mnr.getTaskId());
			mesNgRecordeLineMapper.insertSelective(linsert);
		}
		return response;
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
	
	public Float getWorkStationIdByCode(String code) {
		SopPqcControlL search = new SopPqcControlL();
		search.setWorkstationCode(code);
		return sopPqcControlLMapper.select(search).get(0).getWorkstationId();
	}
}