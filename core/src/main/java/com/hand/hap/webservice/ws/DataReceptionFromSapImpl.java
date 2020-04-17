package com.hand.hap.webservice.ws;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.jws.WebService;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;

import com.hand.hap.util.SoapPostUtil;
import com.hand.hap.util.SystemApiMethod;
import com.hand.hap.webservice.ws.idto.ItemStock;
import com.hand.hap.webservice.ws.idto.Material;
import com.hand.hap.webservice.ws.idto.PurchaseOrderLine;
import com.hand.hap.webservice.ws.idto.PurchaseOrderTop;
import com.hand.hap.webservice.ws.idto.RecallTestInfo;
import com.hand.hap.webservice.ws.idto.SupplierInfo;
import com.hand.hap.webservice.ws.idto.SupplierMaterials;
import com.hand.hap.webservice.ws.idto.TaskInComeInspect;
import com.hand.hap.webservice.ws.wsi.DataReceptionFromSap;
import com.hand.hcm.hcm_item.mapper.ItemMapper;
import com.hand.hcm.hcm_item_b.service.IItemBService;
import com.hand.hcs.hcs_asl_control.service.IAslControlService;
import com.hand.hcs.hcs_po_lines.service.IPoLinesService;
import com.hand.hcs.hcs_suppliers.service.ISuppliersService;
import com.hand.hqm.hqm_qc_task.dto.FqcTask;
import com.hand.hqm.hqm_qc_task.dto.IqcTask;
import com.hand.hqm.hqm_qc_task.service.IFqcTaskService;
import com.hand.hqm.hqm_qc_task.service.IIqcTaskService;
import com.hand.hqm.hqm_qc_task.service.IOqcTaskService;
import com.hand.itf.itf_global_history.dto.GlobalHistory;
import com.hand.itf.itf_global_history.service.IGlobalHistoryService;
import com.hand.itf.itf_material.mapper.MaterialMapper;
import com.hand.itf.itf_purchase_order_header.mapper.PurchaseOrderHeaderMapper;
import com.hand.itf.itf_purchase_order_line.mapper.PurchaseOrderLineMapper;
import com.hand.itf.itf_supplier_info.mapper.SupplierInfoMapper;
import com.hand.itf.itf_supplier_materials.mapper.SupplierMaterialsMapper;
import jodd.util.StringUtil;
import org.codehaus.jackson.type.TypeReference;

/**
 * @author tainmin.wang
 * @version date：2019年11月6日 上午11:28:31
 * 
 */
@WebService(endpointInterface = "com.hand.hap.webservice.ws.wsi.DataReceptionFromSap", serviceName = "DataReceptionFromSap")
public class DataReceptionFromSapImpl extends DataReceptionFromWmsImpl implements DataReceptionFromSap {
	private ObjectMapper mapper = new ObjectMapper();

	@Autowired
	ItemMapper itemMapper;
	@Autowired
	SupplierInfoMapper supplierInfoMapper;
	@Autowired
	ISuppliersService iSuppliersService;
	@Autowired
	SupplierMaterialsMapper supplierMaterialsMapper;
	@Autowired
	IAslControlService iAslControlService;
	// com.hand.itf.itf_supplier_info.mapper.SupplierInfoMapper
	@Autowired
	MaterialMapper materialMapper;
	@Autowired
	IItemBService iItemBService;
	@Autowired
	PurchaseOrderHeaderMapper purchaseOrderHeaderMapper;
	@Autowired
	PurchaseOrderLineMapper purchaseOrderLineMapper;
	@Autowired
	IPoLinesService iPoLinesService;
	@Autowired
	IGlobalHistoryService iGlobalHistoryService;
	@Autowired
	IIqcTaskService iIqcTaskService;
	@Autowired
	IOqcTaskService iOqcTaskService;
	@Autowired
	IFqcTaskService iFqcTaskService;

	@Override
	public String SRMGlobalFunc(String FuncName, String JsonString) {
		try {
			GlobalHistory gh = new GlobalHistory();
			String uuid = UUID.randomUUID().toString();
			gh.setBatchId(uuid);
			gh.setFuncName(FuncName);
			gh.setJsonString(JsonString);
			gh.setOperationTime(new Date());
			/**
			 * FuncName 去调用不同方法
			 */
			if (StringUtil.isNotEmpty(FuncName)) {
				if (FuncName.toLowerCase().contains("aps"))
					FuncName = "procedureAps";// 如果为aps的接口 去调用aps的存储过程
				try {
					String back = (String) this.getClass()
							.getMethod(FuncName, GlobalHistory.class, String.class, String.class)
							.invoke(this, gh, JsonString, uuid);
					gh.setMessageStore(back);
				} catch (Exception e) {
					SoapPostUtil.ResponseSap response = new SoapPostUtil.ResponseSap(false,
							"functionName can not match");
					gh.setInStatus(response.getResult().toString());
					gh.setMessageStore(SoapPostUtil.getStringFromResponseSap(response));
				}
				return iGlobalHistoryService.itfInsert(gh);
			} else {
				SoapPostUtil.ResponseSap response = new SoapPostUtil.ResponseSap(false, "functionName can not be null");
				gh.setInStatus(response.getResult().toString());
				gh.setMessageStore(SoapPostUtil.getStringFromResponseSap(response));
				return iGlobalHistoryService.itfInsert(gh);
			}
		} catch (Exception e) {
			return "{\"result\":1,\"error_code\":null,\"error_info\":\"1:fail-未知的错误:" + e.getMessage() + "\"}";
		}
	}

	public String transferMaterial(GlobalHistory gh, String listen, String guid) {// 物料信息接口
		SoapPostUtil.ResponseSap response = new SoapPostUtil.ResponseSap();
		// String guid = itemMapper.getGuid();
		try {
			// MaterialTop mt = mapper.readValue(listen, MaterialTop.class);
			// List<Material> ml = mt.Material_Master;
			List<Material> ml = mapper.readValue(listen, new TypeReference<List<Material>>() {
			});
			List<com.hand.itf.itf_material.dto.Material> mli = new ArrayList<com.hand.itf.itf_material.dto.Material>();
			for (Material mi : ml) {
				com.hand.itf.itf_material.dto.Material minsert = new com.hand.itf.itf_material.dto.Material();
				minsert.setBatchNumber(guid);
				minsert.setProcessStatus("N");
				minsert.setMatnr(SystemApiMethod.removeZero(mi.MATNR));
				minsert.setMaktx(mi.MAKTX);
				minsert.setWerks(mi.WERKS);
				minsert.setMeins(mi.MEINS);
				minsert.setMtart(mi.MTART);
				minsert.setBeskz(mi.BESKZ);
				minsert.setStprs(mi.STPRS);
				minsert.setPeinh(mi.PEINH);
				minsert.setMeinh(mi.MEINH);
				minsert.setBstrf(mi.BSTRF);
				minsert.setPlifz(mi.PLIFZ);
				materialMapper.insertSelective(minsert);
				mli.add(minsert);
			}
			response = iItemBService.transferMaterial(mli);

		} catch (Exception e) {
			response.setResult(false);
			response.setMessage(e.getMessage());
		}
		gh.setInStatus(response.getResult().toString());
		return SoapPostUtil.getStringFromResponseSap(response);
	}

	public String transferSupplier(GlobalHistory gh, String listen, String guid) {// 供应商接口
		SoapPostUtil.ResponseSap response = new SoapPostUtil.ResponseSap();
		// String guid = itemMapper.getGuid();
		try {

//			SupplierInfoTop st = mapper.readValue(listen, SupplierInfoTop.class);
//			List<SupplierInfo> sil = st.Vendor;

			List<SupplierInfo> sil = mapper.readValue(listen, new TypeReference<List<SupplierInfo>>() {
			});
			List<com.hand.itf.itf_supplier_info.dto.SupplierInfo> sili = new ArrayList<com.hand.itf.itf_supplier_info.dto.SupplierInfo>();
			for (SupplierInfo sii : sil) {
				com.hand.itf.itf_supplier_info.dto.SupplierInfo siinsert = new com.hand.itf.itf_supplier_info.dto.SupplierInfo();
				siinsert.setBatchNumber(guid);
				siinsert.setLifnr(SystemApiMethod.removeZero(sii.LIFNR));
				siinsert.setName1(StringUtil.isEmpty(sii.NAME1) ? "SAP NULL" : sii.NAME1);
				siinsert.setZterm(sii.ZTERM);
				siinsert.setProcessStatus("N");
				supplierInfoMapper.insertSelective(siinsert);
				sili.add(siinsert);
			}
			response = iSuppliersService.transferSupplier(sili);

		} catch (Exception e) {
			response.setResult(false);
			response.setMessage(e.getMessage());
		}
		gh.setInStatus(response.getResult().toString());
		return SoapPostUtil.getStringFromResponseSap(response);
	}

	public String transferSupplierMaterials(GlobalHistory gh, String listen, String guid) {// 供应商物料关系信息接口
		SoapPostUtil.ResponseSap response = new SoapPostUtil.ResponseSap();
		// String guid = itemMapper.getGuid();
		try {
//			SupplierMaterialsTop st = mapper.readValue(listen, SupplierMaterialsTop.class);
//			List<SupplierMaterials> smlist = st.Sourcelist;
			List<SupplierMaterials> smlist = mapper.readValue(listen, new TypeReference<List<SupplierMaterials>>() {
			});
			List<com.hand.itf.itf_supplier_materials.dto.SupplierMaterials> smilist = new ArrayList<com.hand.itf.itf_supplier_materials.dto.SupplierMaterials>();
			for (SupplierMaterials sm : smlist) {
				com.hand.itf.itf_supplier_materials.dto.SupplierMaterials smiinsert = new com.hand.itf.itf_supplier_materials.dto.SupplierMaterials();
				smiinsert.setBatchNumber(guid);
				smiinsert.setProcessStatus("N");
				smiinsert.setWerks(sm.WERKS);
				smiinsert.setMatnr(SystemApiMethod.removeZero(sm.MATNR));
				smiinsert.setLifnr(SystemApiMethod.removeZero(sm.LIFNR));
				smiinsert.setVdatu(sm.VDATU);
				smiinsert.setBdatu(sm.BDATU);
				smiinsert.setEkgrp(sm.EKGRP);
				smiinsert.setUntto(sm.UNTTO);
				smiinsert.setUebto(sm.UEBTO);
				smiinsert.setAplfz(sm.APLFZ);
				supplierMaterialsMapper.insertSelective(smiinsert);
				smilist.add(smiinsert);
			}
			response = iAslControlService.transferSupplierMaterials(smilist);

		} catch (Exception e) {
			response.setResult(false);
			response.setMessage(e.getMessage());
		}
		gh.setInStatus(response.getResult().toString());
		return SoapPostUtil.getStringFromResponseSap(response);
	}

	public String transferPurchaseOrder(GlobalHistory gh, String listen, String guid) {// 物料信息接口
		SoapPostUtil.ResponseSap response = new SoapPostUtil.ResponseSap();
		// String guid = itemMapper.getGuid();
		try {
			PurchaseOrderTop mt = mapper.readValue(listen, PurchaseOrderTop.class);
			com.hand.itf.itf_purchase_order_header.dto.PurchaseOrderHeader ipo = new com.hand.itf.itf_purchase_order_header.dto.PurchaseOrderHeader();
			ipo.setBatchNumber(guid);
			ipo.setProcessStatus("N");
			ipo.setBelnr(mt.ASN.BELNR);
			ipo.setBsart(mt.ASN.BSART);
			ipo.setDatum(mt.ASN.DATUM);
			ipo.setPartn(SystemApiMethod.removeZero(mt.SHP.PARTN));
			purchaseOrderHeaderMapper.insertSelective(ipo);
			List<com.hand.itf.itf_purchase_order_line.dto.PurchaseOrderLine> lineList = new ArrayList<com.hand.itf.itf_purchase_order_line.dto.PurchaseOrderLine>();
			for (PurchaseOrderLine pol : mt.DET) {
				com.hand.itf.itf_purchase_order_line.dto.PurchaseOrderLine ipoli = new com.hand.itf.itf_purchase_order_line.dto.PurchaseOrderLine();
				ipoli.setBatchNumber(guid);
				ipoli.setProcessStatus("N");
				ipoli.setPurchaseOrderHeaderId(ipo.getPurchaseOrderHeaderId());
				ipoli.setBelnr(pol.BELNR);
				ipoli.setWerks(pol.WERKS);
				ipoli.setPosex(SystemApiMethod.removeZero(pol.POSEX));
				ipoli.setIdtnr(SystemApiMethod.removeZero(pol.IDTNR));
				ipoli.setAnetw(pol.ANETW);
				ipoli.setBmng2(pol.BMNG2);
				ipoli.setPmene(pol.PMENE);
				ipoli.setCurcy(pol.CURCY);
				ipoli.setVprei(pol.VPREI);
				ipoli.setMwskz(pol.MWSKZ);
				ipoli.setPeinh(pol.PEINH);
				ipoli.setEdatu(pol.EDATU);
				ipoli.setAction(StringUtil.isEmpty(pol.ACTION) ? "001" : pol.ACTION);
				purchaseOrderLineMapper.insertSelective(ipoli);
				lineList.add(ipoli);
			}
			try {
				response = iPoLinesService.transferPurchaseOrder(ipo, lineList);
			} catch (Exception e) {
				response.setResult(false);
				response.setError_info(e.getMessage());
			}
			purchaseOrderHeaderMapper.updateByPrimaryKeySelective(ipo);
			lineList.forEach(p -> {
				purchaseOrderLineMapper.updateByPrimaryKeySelective(p);
			});
		} catch (Exception e) {
			response.setResult(false);
			response.setMessage(e.getMessage());
		}
		gh.setInStatus(response.getResult().toString());
		return SoapPostUtil.getStringFromResponseSap(response);
	}

	public String inspectWmsIncome(GlobalHistory gh, String listen, String guid) {// WMS来料报验接口
		List<IqcTask> iqcTaskDto = new ArrayList<IqcTask>();

		SoapPostUtil.ResponseSap response = new SoapPostUtil.ResponseSap();
		try {
			TaskInComeInspect ml = mapper.readValue(listen, new TypeReference<TaskInComeInspect>() {
			});

			response = iIqcTaskService.inspectWmsIncome(ml, iqcTaskDto);
		} catch (Exception e) {
			response.setResult(false);
			response.setMessage(e.getMessage());
		}
		gh.setInStatus(response.getResult().toString());
		try {
			iIqcTaskService.createIqcInterface(iqcTaskDto, null);
		} catch (Exception e) {
		}

		return SoapPostUtil.getStringFromResponseSap(response);
	}

	public String itemStockRetest(GlobalHistory gh, String listen, String guid) {// WMS-OQC库存超预警接口
		SoapPostUtil.ResponseSap response = new SoapPostUtil.ResponseSap();
		try {
			ItemStock is = mapper.readValue(listen, new TypeReference<ItemStock>() {
			});

			response = iOqcTaskService.ItemStockRetest(is);

		} catch (Exception e) {
			response.setResult(false);
			response.setMessage(e.getMessage());
		}
		gh.setInStatus(response.getResult().toString());
		return SoapPostUtil.getStringFromResponseSap(response);
	}

	public String fqcRecallRetest(GlobalHistory gh, String listen, String guid) {// WMS-返修品/成品召回FQC报验申请接口
		List<FqcTask> fqcTaskDto = new ArrayList<FqcTask>();

		SoapPostUtil.ResponseSap response = new SoapPostUtil.ResponseSap();
		try {
			RecallTestInfo rti = mapper.readValue(listen, new TypeReference<RecallTestInfo>() {
			});

			response = iFqcTaskService.fqcRecallRetest(rti, fqcTaskDto);
		} catch (Exception e) {
			response.setResult(false);
			response.setMessage(e.getMessage());
		}
		gh.setInStatus(response.getResult().toString());
		try {
			iFqcTaskService.createFqc(fqcTaskDto, null);
		} catch (Exception e) {
		}

		return SoapPostUtil.getStringFromResponseSap(response);
	}

}
