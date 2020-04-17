package com.hand.hap.system.mapper;

import java.util.List;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.hap.system.dto.UserLogin;

/**
 *create by:jialong.zuo@hand-china.com on 2016/10/11.
 */
public interface UserLoginMapper extends Mapper<UserLogin>{
	/**
	 *  登录日志查询
	 * @param userLogin
	 * @return
	 */
	List<UserLogin> query(UserLogin userLogin);
	/**
	 * 员工LOV
	 * @param userLogin
	 * @return
	 */
	List<UserLogin> employeeLov(UserLogin userLogin);
}
