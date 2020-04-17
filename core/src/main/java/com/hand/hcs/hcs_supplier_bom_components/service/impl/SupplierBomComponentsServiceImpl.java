package com.hand.hcs.hcs_supplier_bom_components.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hand.hap.core.IRequest;
import com.hand.hap.system.service.IPromptService;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import com.hand.hap.util.SystemApiMethod;
import com.hand.hcs.hcs_supplier_bom.dto.SupplierBom;
import com.hand.hcs.hcs_supplier_bom.service.ISupplierBomService;
import com.hand.hcs.hcs_supplier_bom_components.dto.SupplierBomComponents;
import com.hand.hcs.hcs_supplier_bom_components.mapper.SupplierBomComponentsMapper;
import com.hand.hcs.hcs_supplier_bom_components.service.ISupplierBomComponentsService;
import com.hand.hcs.hcs_supplier_item_version.dto.SupplierItemVersion;
import com.hand.hcs.hcs_supplier_item_version.service.ISupplierItemVersionService;

@Service
@Transactional(rollbackFor = Exception.class)
public class SupplierBomComponentsServiceImpl extends BaseServiceImpl<SupplierBomComponents> implements ISupplierBomComponentsService{
	
	@Autowired
	private SupplierBomComponentsMapper mapper;
	@Autowired
	private ISupplierBomService supplierBomService;
	@Autowired
	private ISupplierItemVersionService supplierItemVersionService;
	@Autowired
	private IPromptService iPromptService;
	
	@Override
	public SupplierBomComponents checkData(IRequest requestCotext, SupplierBomComponents dto) throws RuntimeException {
		// TODO Auto-generated method stub
		SupplierBomComponents components = new SupplierBomComponents();
		SupplierBom supplierBom = new SupplierBom();
		supplierBom.setItemId(dto.getItemId());
		supplierBom.setSupplierId(dto.getSupplierId());
		supplierBom.setPlantId(dto.getPlantId());
		List<SupplierBom> bomList = supplierBomService.queryInNow(supplierBom);
		if(bomList !=null && bomList.size() > 0) {
			components.setBomId(bomList.get(0).getBomId());
			for(SupplierBom bom : bomList) {
				SupplierBomComponents supplierBomComponents = new SupplierBomComponents();
				supplierBomComponents.setBomId(bom.getBomId());
				supplierBomComponents.setItemCode(dto.getItemCode());
				supplierBomComponents.setEnableFlag("Y");
				
				List<SupplierBomComponents> supplierBomComponentsList = mapper.select(supplierBomComponents);
				if(supplierBomComponentsList != null && supplierBomComponentsList.size() > 0) {
					throw new RuntimeException(SystemApiMethod.getPromptDescription(requestCotext, iPromptService, "error.srm_purchase_1096"));
				}
			}
		}
		return components;
		
	}

	@Override
	public SupplierBomComponents addData(IRequest requestCotext, SupplierBomComponents dto) {
		//关键件物料版本
		SupplierItemVersion supplierItemVersion = new SupplierItemVersion();
		supplierItemVersion.setItemVersion(dto.getItemVersion());
		supplierItemVersion.setStockQty(dto.getStockQty());
		supplierItemVersion.setWipQty(dto.getWipQty());
		supplierItemVersion.setOutOrderQty(dto.getOutOrderQty());
		supplierItemVersion.setSupplierId(dto.getSupplierId());
		supplierItemVersion.setPlantId(dto.getPlantId());
		supplierItemVersion.setMainVersion("Y");
		supplierItemVersion.setBomId(dto.getBomId());
		
		//新增关键件库存信息
		int itemId = mapper.queryMaxNum();
		dto.setItemId((float)itemId);
		self().insertSelective(requestCotext, dto);
		
		//新增关键件物料版本
		supplierItemVersion.setItemId(dto.getItemId());
		supplierItemVersionService.insertSelective(requestCotext, supplierItemVersion);
		return dto;
	}

	@Override
	public SupplierBomComponents deleteData(IRequest requestCotext, SupplierBomComponents dto) {
		//失效供应商物料版本明细信息
		SupplierItemVersion supplierItemVersion = new SupplierItemVersion();
		supplierItemVersion.setItemId(dto.getItemId());
		supplierItemVersion.setBomId(dto.getBomId());
		supplierItemVersion.setPlantId(dto.getPlantId());
		supplierItemVersion.setSupplierId(dto.getSupplierId());
		supplierItemVersion.setEnableFlag("Y");
		List<SupplierItemVersion> supplierItemVersionList = supplierItemVersionService.select(requestCotext, supplierItemVersion, 0, 0);
		if(supplierItemVersionList != null && supplierItemVersionList.size() > 0) {
			for(SupplierItemVersion version : supplierItemVersionList) {
				version.setEnableFlag("N");
				supplierItemVersionService.updateByPrimaryKeySelective(requestCotext, version);
			}
		}
		//失效关键件信息
		dto.setEnableFlag("N");
		self().updateByPrimaryKeySelective(requestCotext, dto);
		return dto;
	}

}