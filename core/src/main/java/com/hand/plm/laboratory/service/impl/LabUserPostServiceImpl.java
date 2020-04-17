package com.hand.plm.laboratory.service.impl;

import com.hand.hap.core.IRequest;
import com.hand.hap.system.service.impl.BaseServiceImpl;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import com.hand.plm.laboratory.dto.LabUser;
import com.hand.plm.laboratory.dto.LabUserPost;
import com.hand.plm.laboratory.service.ILabUserPostService;

import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class LabUserPostServiceImpl extends BaseServiceImpl<LabUserPost> implements ILabUserPostService{

	@Override
	public void createUserPost(LabUser dto,IRequest requestContext,Float labUserId) {
		LabUserPost userPost1 = new LabUserPost();
		if(labUserId.equals(-1f)) {
			// 插入主岗位数据
	       
	        userPost1.setLabUserId(dto.getLabUserId());
	        userPost1.setPostCode(dto.getPost1());
	        userPost1.setPostNum(1L);

	        self().insertSelective(requestContext, userPost1);
		}else {
			// 修改主岗位数据
			userPost1.setLabUserId(dto.getLabUserId());
			userPost1.setPostNum(1L);
			List<LabUserPost> postList = self().select(requestContext, userPost1, 0, 100);
			if(!postList.isEmpty()) {
				userPost1 = postList.get(0);
			}
	        userPost1.setPostCode(dto.getPost1());
	        
	        self().updateByPrimaryKey(requestContext, userPost1);
		}
		
	}

}