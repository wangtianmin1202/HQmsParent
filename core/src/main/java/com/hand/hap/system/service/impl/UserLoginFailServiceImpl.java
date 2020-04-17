package com.hand.hap.system.service.impl;

import com.hand.hap.system.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;
import com.hand.hap.system.dto.UserLoginFail;
import com.hand.hap.system.service.IUserLoginFailService;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class UserLoginFailServiceImpl extends BaseServiceImpl<UserLoginFail> implements IUserLoginFailService{

}