package com.hand.hcs.hcs_supplier_onhand_quantities.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.hand.hap.account.dto.User;
import com.hand.hap.account.service.IUserService;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import com.hand.hcs.hcs_supplier_bom.dto.SupplierBom;
import com.hand.hcs.hcs_supplier_bom.service.ISupplierBomService;
import com.hand.hcs.hcs_supplier_bom_components.dto.SupplierBomComponents;
import com.hand.hcs.hcs_supplier_bom_components.service.ISupplierBomComponentsService;
import com.hand.hcs.hcs_supplier_item_version.dto.SupplierItemVersion;
import com.hand.hcs.hcs_supplier_item_version.service.ISupplierItemVersionService;
import com.hand.hcs.hcs_supplier_onhand_quantities.dto.SupplierOnhandQuantities;
import com.hand.hcs.hcs_supplier_onhand_quantities.mapper.SupplierOnhandQuantitiesMapper;
import com.hand.hcs.hcs_supplier_onhand_quantities.service.ISupplierOnhandQuantitiesService;
import com.hand.hcs.hcs_suppliers.dto.Suppliers;
import com.hand.hcs.hcs_suppliers.service.ISuppliersService;

@Service
@Transactional(rollbackFor = Exception.class)
public class SupplierOnhandQuantitiesServiceImpl extends BaseServiceImpl<SupplierOnhandQuantities> implements ISupplierOnhandQuantitiesService{
	@Autowired
	private SupplierOnhandQuantitiesMapper mapper;
	@Autowired
	private IUserService userService;
	@Autowired
	private ISuppliersService suppliersService;
	@Autowired
	private ISupplierItemVersionService supplierItemVersionService;
	@Autowired
	private ISupplierBomComponentsService supplierBomComponentsService;
	@Autowired
	private ISupplierBomService supplierBomService;
	
	@Override
	public List<SupplierOnhandQuantities> query(IRequest requestContext,SupplierOnhandQuantities dto,  int page,
			int pageSize) {
		PageHelper.startPage(page, pageSize);
		return mapper.query(dto);
	}

	@Override
	public ResponseData querySupplier(IRequest requestContext) {
		ResponseData responseData = new ResponseData();
		User user = new User();
		user.setUserId(requestContext.getUserId());
		user = userService.selectByPrimaryKey(requestContext, user);
		Suppliers suppliers = new Suppliers();
		if("S".equals(user.getUserType())) {
			suppliers.setUserType(user.getUserType());
			suppliers.setSupplierId((float)user.getSupplierId());
			suppliers = suppliersService.selectByPrimaryKey(requestContext, suppliers);
		}
		List<Suppliers> suppliersList = new ArrayList();
		suppliersList.add(suppliers);
		responseData.setRows(suppliersList);
		return responseData;
	}

	@Override
	public List<SupplierOnhandQuantities> queryTree(IRequest requestContext, SupplierOnhandQuantities dto, int page,
			int pageSize) {
		//PageHelper.startPage(page, pageSize);
		return mapper.queryTree(dto);
	}

	@Override
	public List<SupplierOnhandQuantities> updateData(IRequest requestContext, List<SupplierOnhandQuantities> dto) {
		for(SupplierOnhandQuantities supplierOnhandQuantities : dto) {
			if(supplierOnhandQuantities.getComponentId() != null) {
				//更新关键件信息
				SupplierBomComponents components = new SupplierBomComponents();
				components.setComponentId(supplierOnhandQuantities.getComponentId());
				if(supplierOnhandQuantities.getFormat() != null) {					
					components.setFormat(supplierOnhandQuantities.getFormat());
				}
				if(supplierOnhandQuantities.getBomUsage() != null) {
					components.setBomUsage(supplierOnhandQuantities.getBomUsage());
				}
				if(supplierOnhandQuantities.getProductionCycle() != null) {
					components.setProductionCycle(supplierOnhandQuantities.getProductionCycle());			
				}
				if(supplierOnhandQuantities.getPurchaseCycle() != null) {
					components.setPurchaseCycle(supplierOnhandQuantities.getPurchaseCycle());
				}
				if(StringUtils.isNotBlank(supplierOnhandQuantities.getRemarks())) {
					components.setRemarks(supplierOnhandQuantities.getRemarks());
				}
				supplierBomComponentsService.updateByPrimaryKeySelective(requestContext, components);
			}else {
				//更新采购件信息
				SupplierOnhandQuantities quantities = new SupplierOnhandQuantities();
				quantities.setOnhandId(supplierOnhandQuantities.getOnhandId());
				if(supplierOnhandQuantities.getFormat() != null) {					
					quantities.setFormat(supplierOnhandQuantities.getFormat());
				}
				if(supplierOnhandQuantities.getBomUsage() != null) {
					quantities.setBomUsage(supplierOnhandQuantities.getBomUsage());
				}
				if(supplierOnhandQuantities.getProductionCycle() != null) {
					quantities.setProductionCycle(supplierOnhandQuantities.getProductionCycle());			
				}
				if(supplierOnhandQuantities.getPurchaseCycle() != null) {
					quantities.setPurchaseCycle(supplierOnhandQuantities.getPurchaseCycle());
				}
				if(StringUtils.isNotBlank(supplierOnhandQuantities.getRemarks())) {
					quantities.setRemarks(supplierOnhandQuantities.getRemarks());
				}
				self().updateByPrimaryKeySelective(requestContext, quantities);
			}
			//更新供应商物料版本明细
			if(supplierOnhandQuantities.getDataCount() == 1) {
				updateSupplierItemVersion(requestContext, supplierOnhandQuantities);
			}
		}
		return dto;
	}
	/**
	 *  更新供应商物料版本明细
	 * @param requestContext
	 * @param supplierOnhandQuantities
	 */
	private void updateSupplierItemVersion(IRequest requestContext, SupplierOnhandQuantities supplierOnhandQuantities) {
		SupplierItemVersion supplierItemVersion = new SupplierItemVersion();
		supplierItemVersion.setItemId(supplierOnhandQuantities.getItemId());
		supplierItemVersion.setPlantId(supplierOnhandQuantities.getPlantId());
		supplierItemVersion.setSupplierId(supplierOnhandQuantities.getSupplierId());
		supplierItemVersion.setBomId(supplierOnhandQuantities.getBomId());
		supplierItemVersion.setEnableFlag("Y");
		
		List<SupplierItemVersion> supplierItemVersionList = supplierItemVersionService.select(requestContext, supplierItemVersion, 0, 0);
		if(supplierItemVersionList != null && supplierItemVersionList.size() == 1) {
			if((supplierOnhandQuantities.getWipQty() != null && supplierItemVersionList.get(0).getWipQty() != supplierOnhandQuantities.getWipQty())
					|| (supplierOnhandQuantities.getStockQty() != null && supplierItemVersionList.get(0).getStockQty() != supplierOnhandQuantities.getStockQty())
					|| (supplierOnhandQuantities.getOutOrderQty() != null && supplierItemVersionList.get(0).getOutOrderQty() != supplierOnhandQuantities.getOutOrderQty())) {
				
				SupplierItemVersion version = new SupplierItemVersion();
				version.setKid(supplierItemVersionList.get(0).getKid());
				version.setStockQty(supplierOnhandQuantities.getStockQty());
				version.setWipQty(supplierOnhandQuantities.getWipQty());
				version.setOutOrderQty(supplierOnhandQuantities.getOutOrderQty());
				
				supplierItemVersionService.updateByPrimaryKeySelective(requestContext, version);
			}
		}
	}

	@Override
	public SupplierOnhandQuantities addData(IRequest requestContext, SupplierOnhandQuantities dto) throws ParseException  {
		//采购件物料版本
		SupplierItemVersion supplierItemVersion = new SupplierItemVersion();
		supplierItemVersion.setItemVersion(dto.getItemVersion());
		if(dto.getStockQty() != null) {
			supplierItemVersion.setStockQty(dto.getStockQty());
		}
		if(dto.getWipQty() != null) {
			supplierItemVersion.setWipQty(dto.getWipQty());
		}
		if(dto.getOutOrderQty() != null) {
			supplierItemVersion.setOutOrderQty(dto.getOutOrderQty());
		}
		supplierItemVersion.setItemId(dto.getItemId());
		supplierItemVersion.setSupplierId(dto.getSupplierId());
		supplierItemVersion.setPlantId(dto.getPlantId());
		supplierItemVersion.setMainVersion("Y");
		
		//新增采购件
		dto = self().insertSelective(requestContext, dto);
		//新增供应商BOM信息
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		SupplierBom supplierBom = new SupplierBom();
		supplierBom.setPlantId(dto.getPlantId());
		supplierBom.setItemId(dto.getItemId());
		supplierBom.setSupplierId(dto.getSupplierId());
		supplierBom.setBomVersion("V1");
		supplierBom.setStartDate(sdf.parse(sdf.format(new Date())));
		supplierBom = supplierBomService.insertSelective(requestContext, supplierBom);
		//新增采购件物料版本
		supplierItemVersion.setBomId(supplierBom.getBomId());
		supplierItemVersionService.insertSelective(requestContext, supplierItemVersion);
		return dto;
	}

	@Override
	public SupplierOnhandQuantities deleteData(IRequest requestContext, SupplierOnhandQuantities dto) throws ParseException {
		float bomId = dto.getBomId();
		dto.setEnableFlag("N");
		//失效采购信息
		self().updateByPrimaryKeySelective(requestContext, dto);
		
		//获取前一天
		Date date=new Date();
		//Calendar calendar =new GregorianCalendar();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(calendar.DATE, -1);
		date = calendar.getTime();
		SimpleDateFormat format= new SimpleDateFormat("yyyy-MM-dd");
		String dateString = format.format(date);
		//更新供应商bom信息
		SupplierBom supplierBom = new SupplierBom();
		supplierBom.setBomId(bomId);
		supplierBom.setEndDate(format.parse(dateString));
		supplierBomService.updateByPrimaryKeySelective(requestContext, supplierBom);
		//失效Bom组件行
		SupplierBomComponents supplierBomComponents = new SupplierBomComponents();
		supplierBomComponents.setBomId(bomId);
		supplierBomComponents.setEnableFlag("Y");
		List<SupplierBomComponents> supplierBomComponentsList = supplierBomComponentsService.select(requestContext, supplierBomComponents, 0, 0);
		if(supplierBomComponentsList != null && supplierBomComponentsList.size() > 0) {
			for(SupplierBomComponents components : supplierBomComponentsList) {
				components.setEnableFlag("N");
				supplierBomComponentsService.updateByPrimaryKeySelective(requestContext, components);
			}
		}
		//失效供应商物料版本明细
		SupplierItemVersion supplierItemVersion = new SupplierItemVersion();
		supplierItemVersion.setBomId(bomId);
		supplierItemVersion.setEnableFlag("Y");
		List<SupplierItemVersion> supplierItemVersionList = supplierItemVersionService.select(requestContext, supplierItemVersion, 0, 0);
		if(supplierItemVersionList != null && supplierItemVersionList.size() > 0) {
			for(SupplierItemVersion version : supplierItemVersionList) {
				version.setEnableFlag("N");
				supplierItemVersionService.updateByPrimaryKeySelective(requestContext, version);
			}
		}
		return dto;
	}
}