package com.hand.srm.srm_user_purchase_group.mapper;

import java.util.List;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.srm.srm_user_purchase_group.dto.UserPurchaseGroup;

public interface UserPurchaseGroupMapper extends Mapper<UserPurchaseGroup>{
	/**
	 * 用户采购组关系查询
	 * @param userPurchaseGroup
	 * @return
	 */
	List<UserPurchaseGroup> query(UserPurchaseGroup userPurchaseGroup);
	/**
	 * 用户LOV
	 * @param userPurchaseGroup
	 * @return
	 */
	List<UserPurchaseGroup> userPurchaseLov(UserPurchaseGroup userPurchaseGroup);
}