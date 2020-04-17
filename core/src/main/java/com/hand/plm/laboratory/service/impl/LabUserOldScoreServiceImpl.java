package com.hand.plm.laboratory.service.impl;

import com.github.pagehelper.PageHelper;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.system.service.impl.BaseServiceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hand.plm.laboratory.dto.LabUser;
import com.hand.plm.laboratory.dto.LabUserOldScore;
import com.hand.plm.laboratory.mapper.LabUserOldScoreMapper;
import com.hand.plm.laboratory.service.ILabUserOldScoreService;
import com.hand.plm.laboratory.service.ILabUserService;

import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class LabUserOldScoreServiceImpl extends BaseServiceImpl<LabUserOldScore> implements ILabUserOldScoreService{

	@Autowired
	private ILabUserService userService;
	
	@Autowired
	private LabUserOldScoreMapper oldMapper;

	@Override
	public ResponseData addOldData(IRequest requestCtx, List<LabUser> users) {
		ResponseData response = new ResponseData();
		if(users.size()>0) {
			
		
		LabUser user =  userService.selectByPrimaryKey(requestCtx, users.get(0));
		LabUserOldScore oldUser = new LabUserOldScore();
		oldUser.setLabUserId(user.getLabUserId());
		oldUser.setUserId(user.getUserId());
		oldUser.setUserName(user.getUserName());
		oldUser.setMajor(user.getMajor());
		oldUser.setEducation(user.getEducation());
		oldUser.setAbilityScore(user.getAbilityScore());
		oldUser.setJobDesc(user.getJobDesc());
		oldUser.setQuality(user.getQuality());
		oldUser.setPraiseRate(user.getPraiseRate());
		oldUser.setFinishTimeRate(user.getFinishTimeRate());
		
		//保存历史数据
		oldUser = self().insertSelective(requestCtx, oldUser);
		//修改当前数据
		List<LabUser> userList = userService.batchUpdate(requestCtx, users);
		response.setRows(userList);
		
		}
		return response;
	}



	@Override
	public List<LabUserOldScore> query(LabUserOldScore dto ,int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		List<LabUserOldScore> oldScoreList = oldMapper.selOldScore(dto.getLabUserId());
		return oldScoreList;
//		return null;
	}
	
	
}