package com.hand.hap.system.service.impl;

import com.hand.hap.system.dto.UserLogin;
import com.hand.hap.system.mapper.UserLoginMapper;
import com.hand.hap.system.service.IUserLoginService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.hand.hap.core.IRequest;

import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class UserLoginServiceImpl extends BaseServiceImpl<UserLogin> implements IUserLoginService{
	
	@Autowired
	private UserLoginMapper mapper;
	
	@Override
	public List<UserLogin> query(IRequest requestContext, UserLogin userLogin, int page, int pageSize) {
		PageHelper.startPage(page, pageSize);
		return mapper.query(userLogin);
	}

}