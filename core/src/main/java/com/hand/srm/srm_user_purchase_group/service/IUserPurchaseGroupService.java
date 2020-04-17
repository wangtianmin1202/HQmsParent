package com.hand.srm.srm_user_purchase_group.service;

import java.util.List;

import org.springframework.web.bind.annotation.RequestBody;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.system.service.IBaseService;
import com.hand.srm.srm_user_purchase_group.dto.UserPurchaseGroup;

public interface IUserPurchaseGroupService extends IBaseService<UserPurchaseGroup>, ProxySelf<IUserPurchaseGroupService>{
	
	/**
	 * 用户采购组关系查询
	 * @param requestContext
	 * @param userPurchaseGroup
	 * @param page
	 * @param pageSize
	 * @return
	 */
	List<UserPurchaseGroup> query(IRequest requestContext, UserPurchaseGroup userPurchaseGroup, int page, int pageSize);
	/**
	 * 保存
	 * @param request
	 * @param dto
	 * @return
	 */
	ResponseData batchSubmit(IRequest requestContext,@RequestBody List<UserPurchaseGroup> dto);
}