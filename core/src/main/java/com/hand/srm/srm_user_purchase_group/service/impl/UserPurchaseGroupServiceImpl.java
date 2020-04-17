package com.hand.srm.srm_user_purchase_group.service.impl;

import com.github.pagehelper.PageHelper;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.system.service.IPromptService;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import com.hand.hap.util.SystemApiMethod;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.hand.srm.srm_user_purchase_group.dto.UserPurchaseGroup;
import com.hand.srm.srm_user_purchase_group.mapper.UserPurchaseGroupMapper;
import com.hand.srm.srm_user_purchase_group.service.IUserPurchaseGroupService;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class UserPurchaseGroupServiceImpl extends BaseServiceImpl<UserPurchaseGroup> implements IUserPurchaseGroupService{
	
	@Autowired
	private UserPurchaseGroupMapper mapper;
	@Autowired
	private IPromptService iPromptService;
	
	@Override
	public List<UserPurchaseGroup> query(IRequest requestContext, UserPurchaseGroup userPurchaseGroup, int page,
			int pageSize) {
		PageHelper.startPage(page, pageSize);
		return mapper.query(userPurchaseGroup);
	}

	@Override
	public ResponseData batchSubmit(IRequest requestContext, List<UserPurchaseGroup> dto) {
		ResponseData responseData = new ResponseData();
		String errorMsg = "";
		boolean errorFlag = false;
		for(UserPurchaseGroup userPurchaseGroup : dto) {
			UserPurchaseGroup group = new UserPurchaseGroup();
			group.setPurchaseGroupCode(userPurchaseGroup.getPurchaseGroupCode());
			if("add".equals(userPurchaseGroup.get__status())) {
				List<UserPurchaseGroup> list = mapper.select(group);
				if(list != null && list.size() == 1) {
					errorMsg = SystemApiMethod.getPromptDescription(requestContext, iPromptService, "error.srm_purchase_1001");
					errorFlag = true;
					break;
				}else if(list.size() > 1) {
					responseData.setMessage("采购组织出现重复，请联系技术人员检查");
					responseData.setSuccess(false);
					return responseData;
				}
			}else if("update".equals(userPurchaseGroup.get__status())) {
				List<UserPurchaseGroup> list = mapper.select(group);
				if(list != null && list.size() == 1 && list.get(0).getPurchaseGroupId() == userPurchaseGroup.getPurchaseGroupId()) {
					errorMsg = SystemApiMethod.getPromptDescription(requestContext, iPromptService, "error.srm_purchase_1001");
					errorFlag = true;
					break;
				}else if(list.size() > 1) {
					responseData.setMessage("采购组织出现重复，请联系技术人员检查");
					responseData.setSuccess(false);
					return responseData;
				}
			}
		}
		if(errorFlag) {
			responseData.setMessage(errorMsg);
			responseData.setSuccess(false);
			return responseData;
		}
		return new ResponseData(self().batchUpdate(requestContext, dto));
	}

}