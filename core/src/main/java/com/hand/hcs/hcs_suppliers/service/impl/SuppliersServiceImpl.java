package com.hand.hcs.hcs_suppliers.service.impl;

import com.github.pagehelper.PageHelper;
import com.hand.hap.account.dto.User;
import com.hand.hap.account.service.IUserService;
import com.hand.hap.core.IRequest;
import com.hand.hap.core.impl.ServiceRequest;
import com.hand.hap.system.service.ICodeService;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import com.hand.hap.util.SoapPostUtil.Response;
import com.hand.hap.util.SoapPostUtil.ResponseSap;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.hand.hcs.hcs_suppliers.dto.Suppliers;
import com.hand.hcs.hcs_suppliers.mapper.SuppliersMapper;
import com.hand.hcs.hcs_suppliers.service.ISuppliersService;
import com.hand.itf.itf_supplier_info.dto.SupplierInfo;
import com.hand.itf.itf_supplier_info.mapper.SupplierInfoMapper;

import jodd.util.StringUtil;

import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class SuppliersServiceImpl extends BaseServiceImpl<Suppliers> implements ISuppliersService {

	@Autowired
	private SuppliersMapper mapper;
	@Autowired
	private IUserService userService;
	@Autowired
	private ICodeService iCodeService;

	@Override
	public List<Suppliers> query(IRequest requestContext, Suppliers dto, int page, int pageSize) {
		PageHelper.startPage(page, pageSize);
		return mapper.query(dto);
	}

	@Override
	public List<Suppliers> save(IRequest requestContext, List<Suppliers> dto) {

		for (Suppliers supplier : dto) {
			Suppliers supplierData = new Suppliers();
			supplierData.setUserId(supplier.getUserId());
			supplierData.setUserType("S");
			supplierData.setSupplierId(supplier.getSupplierId());
			/*
			 * user.setStartActiveDate(new Date()); user.setEndActiveDate(new Date());
			 */

			mapper.updateUserInfo(supplierData);
		}
		return dto;
	}

	@Override
	public void deleteInfo(IRequest requestContext, List<Suppliers> dto) {
		for (Suppliers supplier : dto) {
			mapper.updateSupplier(supplier);
		}
	}

	@Autowired
	SupplierInfoMapper supplierInfoMapper;

	@Override
	public ResponseSap transferSupplier(List<SupplierInfo> sili) {
		IRequest request = new ServiceRequest();
		request.setLocale("zh_CN");
		Date now = new Date();
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMdd");
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMddHHmmss");
		ResponseSap response = new ResponseSap();
		// 先全部更新一次
		List<Suppliers> slist = mapper.selectAll();

		for (SupplierInfo supplierInfo : sili) {
			supplierInfo.setProcessStatus("Y");
			supplierInfo.setProcessTime(new Date());
			try {
				List<Suppliers> haveList = slist.stream()
						.filter((p) -> p.getSupplierCode().equals(supplierInfo.getLifnr()))
						.collect(Collectors.toList());
				String paymentTerms = iCodeService.getCodeMeaningByValue(request, "SRM_PAYMENT_TERMS",
						supplierInfo.getZterm()); // 付款周期
				
				/**
				 * 从要批量修改的list中移出当前的已存在的
				 */
//				slist.removeIf(p -> {
//					return p.getSupplierCode().equals(supplierInfo.getLifnr());
//				});
				/**
				 * 如果因为传输的付款周期本身为null 则不会因为在快码中找不到而throw异常, 而如果传输的不为 null 则如果在快码中找不到 报错
				 */

				if (StringUtil.isEmpty(paymentTerms) && !StringUtil.isEmpty(supplierInfo.getZterm())) {
					throw new RuntimeException("[" + supplierInfo.getZterm() + "]在快速编码SRM_PAYMENT_TERMS中未找到");
				}
				if (haveList != null && haveList.size() > 0) {// 正式表已经存在此供应商
					haveList.get(0).setPaymentTerms(
							StringUtil.isEmpty(supplierInfo.getZterm()) ? null : Float.valueOf(paymentTerms));
					if (haveList.get(0).getStartDate() != null && !haveList.get(0).getStartDate().after(now)) {
						haveList.get(0).setSupplierName(supplierInfo.getName1());
						mapper.updateHaveYes(haveList.get(0));
					} else if (haveList != null && haveList.get(0).getStartDate().after(now)) {
						haveList.get(0).setStartDate(sdf2.parse(sdf1.format(now) + "000000"));
						haveList.get(0).setSupplierName(supplierInfo.getName1());
						mapper.updateHaveNo(haveList.get(0));
					}
				} else {// 正式表不存在此供应商
					Suppliers suins = new Suppliers();
					suins.setSupplierCode(supplierInfo.getLifnr());
					suins.setSupplierName(supplierInfo.getName1());
					suins.setPaymentTerms(
							StringUtil.isEmpty(supplierInfo.getZterm()) ? null : Float.valueOf(paymentTerms));
					suins.setStartDate(sdf2.parse(sdf1.format(now) + "000000"));
					mapper.insertSelective(suins);
				}
			} catch (Exception e) {
				supplierInfo.setProcessStatus("E");
				supplierInfo.setMessage(e.getMessage());
			}
			supplierInfoMapper.updateByPrimaryKeySelective(supplierInfo);
		}
//		for (Suppliers suppliers : slist) {
//			try {
//				suppliers.setEndDate(sdf2.parse(sdf1.format(now) + "000000"));
//			} catch (ParseException e) {
//				response.setMessage(e.getMessage());
//			}
//			suppliers.setLastUpdateDate(now);
//			mapper.updateByPrimaryKeySelective(suppliers);
//		}
		return response;
	}

}