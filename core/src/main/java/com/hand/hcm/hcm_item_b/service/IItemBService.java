package com.hand.hcm.hcm_item_b.service;

import java.util.List;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;
import com.hand.hap.util.SoapPostUtil.Response;
import com.hand.hap.util.SoapPostUtil.ResponseSap;
import com.hand.hcm.hcm_item_b.dto.ItemB;
import com.hand.itf.itf_material.dto.Material;

public interface IItemBService extends IBaseService<ItemB>, ProxySelf<IItemBService>{

	List<?> teselect(IRequest requestContext, ItemB dto, int page, int pageSize);

	ResponseSap transferMaterial(List<Material> mli);

}