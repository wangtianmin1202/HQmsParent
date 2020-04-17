package com.hand.hqm.hqm_fqc_barcode.service.impl;

import com.hand.hap.system.service.impl.BaseServiceImpl;
import com.hand.hap.util.SoapPostUtil.ResponseSap;
import com.hand.hap.webservice.ws.idto.FqcBarcodeInfo;
import com.hand.hcm.hcm_item_b.dto.ItemB;
import com.hand.hcm.hcm_item_b.mapper.ItemBMapper;
import com.hand.hcm.hcm_plant.dto.Plant;
import com.hand.hcm.hcm_plant.mapper.PlantMapper;

import java.util.List;
import java.util.Optional;

import org.drools.core.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.hand.hqm.hqm_fqc_barcode.dto.FqcBarcode;
import com.hand.hqm.hqm_fqc_barcode.mapper.FqcBarcodeMapper;
import com.hand.hqm.hqm_fqc_barcode.service.IFqcBarcodeService;
import com.hand.hqm.hqm_fqc_inspection_d.dto.FqcInspectionD;
import com.hand.hqm.hqm_fqc_inspection_d.mapper.FqcInspectionDMapper;
import com.hand.hqm.hqm_fqc_inspection_h.dto.FqcInspectionH;
import com.hand.hqm.hqm_fqc_inspection_h.mapper.FqcInspectionHMapper;
import com.hand.hqm.hqm_fqc_inspection_l.dto.FqcInspectionL;
import com.hand.hqm.hqm_fqc_inspection_l.mapper.FqcInspectionLMapper;

import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class FqcBarcodeServiceImpl extends BaseServiceImpl<FqcBarcode> implements IFqcBarcodeService {

	@Autowired
	FqcBarcodeMapper mapper;
	@Autowired
	FqcInspectionHMapper fqcInspectionHMapper;
	@Autowired
	FqcInspectionLMapper fqcInspectionLMapper;
	@Autowired
	FqcInspectionDMapper fqcInspectionDMapper;
	@Autowired
	PlantMapper plantMapper;
	@Autowired
	ItemBMapper itemBMapper;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.hand.hqm.hqm_fqc_barcode.service.IFqcBarcodeService#transferFqcBarcode(
	 * com.hand.hap.webservice.ws.idto.FqcBarcodeInfo)
	 */
	@Override
	public ResponseSap transferFqcBarcode(FqcBarcodeInfo fbi) {
		ResponseSap responsesap = new ResponseSap();
		FqcBarcode search = new FqcBarcode();
		search.setOrderNum(fbi.orderNo);
		search.setSerialNumber(fbi.serialNo);
		List<FqcBarcode> result = mapper.select(search);
		if (result != null && result.size() > 0)
			return responsesap;
		Float plantId = getPlantIdByCode(fbi.plantCode);
		search.setIsSample(fbi.isSample);
		search.setItemId(getItemIdByCode(fbi.itemCode, plantId));
		mapper.insertSelective(search);
		serialNumberFill(search);
		return responsesap;
	}

	private void serialNumberFill(FqcBarcode dto) {
		FqcInspectionH fqc = new FqcInspectionH();
		fqc.setSourceOrder(dto.getOrderNum());
		// 工单号找所有相关的 FQC检验单
		List<FqcInspectionH> fqcResult = fqcInspectionHMapper.select(fqc);
		fqcResult.forEach(fqch -> {
			FqcInspectionL lSearch = new FqcInspectionL();
			lSearch.setHeaderId(fqch.getHeaderId());
			// FQC检验单的所有检验项
			List<FqcInspectionL> lResult = fqcInspectionLMapper.select(lSearch);
			lResult.forEach(fqcl -> {
				FqcInspectionD dSearch = new FqcInspectionD();
				dSearch.setLineId(fqcl.getLineId());
				// 检验项下的检验数据
				List<FqcInspectionD> dResult = fqcInspectionDMapper.select(dSearch);
				// 筛选SN号为null的排序靠前的一个 检验数据项 并更新SN号
				Optional<FqcInspectionD> res = dResult.stream().filter(p -> {
					return StringUtils.isEmpty(p.getSerialNumber());
				}).min((p1, p2) -> {
					return Integer.compare(Integer.valueOf(p1.getOrderNum()), Integer.valueOf(p2.getOrderNum()));
				});
				if (res.isPresent()) {
					res.get().setSerialNumber(dto.getSerialNumber());
					fqcInspectionDMapper.updateByPrimaryKeySelective(res.get());
				}
			});
		});
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
}