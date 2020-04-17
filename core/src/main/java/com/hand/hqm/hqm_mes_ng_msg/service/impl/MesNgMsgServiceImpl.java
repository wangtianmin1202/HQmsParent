package com.hand.hqm.hqm_mes_ng_msg.service.impl;

import com.hand.hap.system.service.impl.BaseServiceImpl;
import com.hand.hap.util.SoapPostUtil.ResponseSap;
import com.hand.hap.webservice.ws.idto.ProductionNgInfo;
import com.hand.hcm.hcm_item_b.dto.ItemB;
import com.hand.hcm.hcm_item_b.mapper.ItemBMapper;
import com.hand.hcm.hcm_plant.dto.Plant;
import com.hand.hcm.hcm_plant.mapper.PlantMapper;
import com.hand.hcm.hcm_production_line.dto.ProductionLine;
import com.hand.hcm.hcm_production_line.mapper.ProductionLineMapper;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.hand.hqm.hqm_mes_ng_msg.dto.MesNgMsg;
import com.hand.hqm.hqm_mes_ng_msg.mapper.MesNgMsgMapper;
import com.hand.hqm.hqm_mes_ng_msg.service.IMesNgMsgService;
import com.hand.hqm.hqm_sop_pqc_control_l.dto.SopPqcControlL;
import com.hand.hqm.hqm_sop_pqc_control_l.mapper.SopPqcControlLMapper;

import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class MesNgMsgServiceImpl extends BaseServiceImpl<MesNgMsg> implements IMesNgMsgService {
	@Autowired
	MesNgMsgMapper mapper;
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
	 * @see
	 * com.hand.hqm.hqm_mes_ng_msg.service.IMesNgMsgService#productionNgInfo(com.
	 * hand.hap.webservice.ws.idto.ProductionNgInfo)
	 */
	@Override
	public ResponseSap productionNgInfo(ProductionNgInfo pni) {
		ResponseSap responsesap = new ResponseSap();
		MesNgMsg search = new MesNgMsg();
		Float plantId = getPlantIdByCode(pni.plantCode);
		search.setOrderNum(pni.orderNo);
		search.setItemId(getItemIdByCode(pni.itemCode, plantId));
		search.setSerialNumer(pni.serialNo);
		List<MesNgMsg> result = mapper.select(search);
		if (result != null && result.size() > 0)
			return responsesap;
		search.setProdLineId(getProdLineIdByCode(pni.line));
		search.setWorkstationId(getWorkStationIdByCode(pni.workStation));
		search.setNgCode(pni.ngCode);
		search.setQty(Float.valueOf(pni.qty == null ? "0.0" : pni.qty));
		mapper.insertSelective(search);
		return responsesap;
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