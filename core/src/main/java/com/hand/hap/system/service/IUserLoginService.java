package com.hand.hap.system.service;

import java.util.List;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.dto.UserLogin;

public interface IUserLoginService extends IBaseService<UserLogin>, ProxySelf<IUserLoginService>{
	
	/**
	 * 登录日志查询
	 * @param requestContext
	 * @param userLogin
	 * @param page
	 * @param pageSize
	 * @return
	 */
	List<UserLogin> query(IRequest requestContext, UserLogin userLogin, int page, int pageSize);
}