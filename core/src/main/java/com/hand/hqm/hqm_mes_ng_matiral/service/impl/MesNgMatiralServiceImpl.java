package com.hand.hqm.hqm_mes_ng_matiral.service.impl;

import com.hand.hap.system.service.impl.BaseServiceImpl;
import com.hand.hap.util.SoapPostUtil.ResponseSap;
import com.hand.hap.webservice.ws.idto.MaterialNgInfo;
import com.hand.hcm.hcm_item_b.dto.ItemB;
import com.hand.hcm.hcm_item_b.mapper.ItemBMapper;
import com.hand.hcm.hcm_plant.dto.Plant;
import com.hand.hcm.hcm_plant.mapper.PlantMapper;
import com.hand.hcm.hcm_production_line.dto.ProductionLine;
import com.hand.hcm.hcm_production_line.mapper.ProductionLineMapper;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.hand.hqm.hqm_mes_ng_matiral.dto.MesNgMatiral;
import com.hand.hqm.hqm_mes_ng_matiral.mapper.MesNgMatiralMapper;
import com.hand.hqm.hqm_mes_ng_matiral.service.IMesNgMatiralService;
import com.hand.hqm.hqm_mes_ng_recorde_line.mapper.MesNgRecordeLineMapper;
import com.hand.hqm.hqm_sop_pqc_control_l.dto.SopPqcControlL;
import com.hand.hqm.hqm_sop_pqc_control_l.mapper.SopPqcControlLMapper;
import org.springframework.transaction.annotation.Transactional;
@Service
@Transactional(rollbackFor = Exception.class)
public class MesNgMatiralServiceImpl extends BaseServiceImpl<MesNgMatiral> implements IMesNgMatiralService {
	@Autowired
	MesNgMatiralMapper mapper;
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
	 * @see
	 * com.hand.hqm.hqm_mes_ng_matiral.service.IMesNgMatiralService#materialNgInfo(
	 * com.hand.hap.webservice.ws.idto.MaterialNgInfo)
	 */
	@Override
	public ResponseSap materialNgInfo(MaterialNgInfo mni) {
		ResponseSap responsesap = new ResponseSap();
		MesNgMatiral search = new MesNgMatiral();
		Float plantId = getPlantIdByCode(mni.plantCode);
		search.setOrderNum(mni.orderNo);
		search.setItemId(getItemIdByCode(mni.itemCode, plantId));
		search.setLotNumber(mni.lotNo);
		List<MesNgMatiral> result = mapper.select(search);
		if (result != null && result.size() > 0)
			return responsesap;
		search.setProdLineId(getProdLineIdByCode(mni.line));
		search.setWorkstationId(getWorkStationIdByCode(mni.workStation));
		search.setNgCode(mni.ngCode);
		search.setQty(Float.valueOf(mni.qty == null ? "0.0" : mni.qty));
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