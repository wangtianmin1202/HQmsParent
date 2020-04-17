package com.hand.hcs.hcs_asl_control.service.impl;

import com.hand.hap.system.service.impl.BaseServiceImpl;
import com.hand.hap.util.SoapPostUtil.Response;
import com.hand.hap.util.SoapPostUtil.ResponseSap;
import com.hand.hcm.hcm_item_b.dto.ItemB;
import com.hand.hcm.hcm_item_b.mapper.ItemBMapper;
import com.hand.hcm.hcm_plant.dto.Plant;
import com.hand.hcm.hcm_plant.mapper.PlantMapper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hand.hcs.hcs_asl.dto.Asl;
import com.hand.hcs.hcs_asl.mapper.AslMapper;
import com.hand.hcs.hcs_asl_control.dto.AslControl;
import com.hand.hcs.hcs_asl_control.mapper.AslControlMapper;
import com.hand.hcs.hcs_asl_control.service.IAslControlService;
import com.hand.hcs.hcs_suppliers.dto.Suppliers;
import com.hand.hcs.hcs_suppliers.mapper.SuppliersMapper;
import com.hand.itf.itf_supplier_materials.dto.SupplierMaterials;
import com.hand.itf.itf_supplier_materials.mapper.SupplierMaterialsMapper;

import jodd.util.StringUtil;

import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class AslControlServiceImpl extends BaseServiceImpl<AslControl> implements IAslControlService {

	@Autowired
	AslMapper aslMapper;
	@Autowired
	AslControlMapper aslControlMapper;
	@Autowired
	SupplierMaterialsMapper supplierMaterialsMapper;
	@Autowired
	PlantMapper plantMapper;
	@Autowired
	ItemBMapper itemBMapper;
	@Autowired
	SuppliersMapper suppliersMapper;

	@Override
	public ResponseSap transferSupplierMaterials(List<SupplierMaterials> smilist) {
		ResponseSap response = new ResponseSap();
		Date now = new Date();
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMdd");
		try {
			//List<Asl> aslall = aslMapper.selectAll();
			for (SupplierMaterials sm : smilist) {
				sm.setProcessStatus("Y");
				sm.setProcessTime(now);
				try {
					Asl aslsea = new Asl();
					aslsea.setSupplierCode(sm.getLifnr());
					aslsea.setItemCode(sm.getMatnr());
					aslsea.setPlantCode(sm.getWerks());
					List<Asl> aslres = aslMapper.interfaceSelect(aslsea);
					if (aslres != null && aslres.size() > 0) {// 存在
						//aslall.removeIf(p -> p.getAslId().intValue() == aslres.get(0).getAslId());
						Asl aslu = new Asl();
						aslu.setAslId(aslres.get(0).getAslId());
//					aslu.setPurchaseGroup(sm.getEkgrp());
						aslu.setEnableFlag("Y");
						aslu.setLastUpdateDate(now);
						AslControl aslcu = new AslControl();
						aslcu.setAslId(aslres.get(0).getAslId());
						aslcu.setPurchaseGroup(sm.getEkgrp());
						aslcu.setLastUpdateDate(now);
						/**
						 * added by wtm 20191223 添加三个接收字段 允差上限 允差下限 采购提前期
						 */
						aslcu.setUnderdeliveryToleranceLimit(StringUtil.isEmpty(sm.getUntto()) ? null
								: Double.valueOf(Float.valueOf(sm.getUntto()) * 0.01).floatValue());// 允差下限
						aslcu.setOverdeliveryToleranceLimit(StringUtil.isEmpty(sm.getUebto()) ? null
								: Double.valueOf(Float.valueOf(sm.getUebto()) * 0.01).floatValue());// 上限
						aslcu.setPurchaseLeadTime(
								StringUtil.isEmpty(sm.getAplfz()) ? null : Float.valueOf(sm.getAplfz()));
						aslControlMapper.updateByPrimaryKeySelective(aslcu);
						aslMapper.updateByPrimaryKeySelective(aslu);
					} else {// 不存在
						Asl aslinsert = new Asl();
//						aslinsert
						aslinsert.setPlantId(getPlantId(sm.getWerks()));
						aslinsert.setItemId(getItemId(sm.getMatnr(), aslinsert.getPlantId()));
						aslinsert.setSupplierId(getSuppliersId(sm.getLifnr()));
						aslinsert.setEnableFlag("Y");
						aslMapper.insertSelective(aslinsert);
						AslControl aslcinsert = new AslControl();
						aslcinsert.setAslId(aslinsert.getAslId());
						//aslcinsert.setLeadTime(0f);
						aslcinsert.setPurchaseGroup(sm.getEkgrp());

						/**
						 * added by wtm 20191223 添加三个接收字段 允差上限 允差下限 采购提前期
						 */
						aslcinsert.setUnderdeliveryToleranceLimit(StringUtil.isEmpty(sm.getUntto()) ? null
								: Double.valueOf(Float.valueOf(sm.getUntto()) * 0.01).floatValue());// 允差下限
						aslcinsert.setOverdeliveryToleranceLimit(StringUtil.isEmpty(sm.getUebto()) ? null
								: Double.valueOf(Float.valueOf(sm.getUebto()) * 0.01).floatValue());// 上限
						aslcinsert.setPurchaseLeadTime(
								StringUtil.isEmpty(sm.getAplfz()) ? null : Float.valueOf(sm.getAplfz()));

						aslControlMapper.insertSelective(aslcinsert);
					}
				} catch (Exception e) {
					sm.setMessage(e.getMessage());
					sm.setProcessStatus("E");
				}
				supplierMaterialsMapper.updateByPrimaryKeySelective(sm);
			}
//			for (Asl ass : aslall) {
//				ass.setEnableFlag("N");
//				ass.setLastUpdateDate(now);
//				aslMapper.updateByPrimaryKeySelective(ass);
//			}
		} catch (Exception e) {
			response.setResult(false);
			response.setMessage(e.getMessage());
		}
		return response;
	}

	public Float getPlantId(String plantCode) {
		Plant se = new Plant();
		se.setPlantCode(plantCode);
		return plantMapper.select(se).get(0).getPlantId();
	}

	public Float getItemId(String itemCode, Float plantId) {
		ItemB se = new ItemB();
		se.setPlantId(plantId);
		se.setItemCode(itemCode);
		List<ItemB> re = itemBMapper.select(se);
		if (re == null || re.size() == 0) {
			throw new RuntimeException("工厂下无此物料");
		}
		return re.get(0).getItemId();
	}

	public Float getSuppliersId(String supplierCode) {
		Suppliers se = new Suppliers();
		se.setSupplierCode(supplierCode);
		List<Suppliers> re = suppliersMapper.select(se);
		if (re == null || re.size() == 0) {
			throw new RuntimeException("供应商编码不存在");
		}
		return re.get(0).getSupplierId();
	}
}