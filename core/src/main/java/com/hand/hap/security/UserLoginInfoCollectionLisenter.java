package com.hand.hap.security;

import com.hand.hap.message.IMessageConsumer;
import com.hand.hap.message.QueueMonitor;
import com.hand.hap.system.dto.UserLogin;
import com.hand.hap.system.mapper.UserLoginFailMapper;
import com.hand.hap.system.mapper.UserLoginMapper;
import com.hand.sys.sys_user.service.IUserSysService;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author jialong.zuo@hand-china.com
 * @date 2017/12/15.
 */
@QueueMonitor(queue = "hap:queue:loginInfo")
public class UserLoginInfoCollectionLisenter implements IMessageConsumer<UserLogin> {

	@Autowired
	UserLoginMapper userLoginMapper;
	@Override
	public void onMessage(UserLogin userLogin, String pattern) {
		userLoginMapper.insertSelective(userLogin);
	}
}
