package com.hand.sys.sys_user.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.StringUtil;
import com.hand.hap.cache.impl.UserCache;
import com.hand.hap.core.IRequest;
import com.hand.hap.core.impl.ServiceRequest;
import com.hand.hap.system.dto.SysConfig;
import com.hand.hap.system.dto.UserLogin;
import com.hand.hap.system.dto.UserLoginFail;
import com.hand.hap.system.mapper.SysConfigMapper;
import com.hand.hap.system.mapper.UserLoginFailMapper;
import com.hand.hap.system.mapper.UserLoginMapper;
import com.hand.hap.system.service.ISysConfigService;
import com.hand.hap.system.service.impl.BaseServiceImpl;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.hand.sys.sys_user.dto.UserSys;
import com.hand.sys.sys_user.mapper.UserSysMapper;
import com.hand.sys.sys_user.service.IUserSysService;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class UserSysServiceImpl extends BaseServiceImpl<UserSys> implements IUserSysService {

	@Autowired
	private UserSysMapper mapper;
	@Autowired
	private UserLoginMapper userLoginMapper;
	@Autowired
	private UserCache userCache;
	@Autowired
	private ISysConfigService configService;
	@Autowired
	private SysConfigMapper sysConfigMapper;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.hand.sys.sys_user.service.IUserSysService#reSelect(com.hand.hap.core.
	 * IRequest, com.hand.sys.sys_user.dto.UserSys, int, int)
	 */
	@Override
	public List<UserSys> reSelect(IRequest requestContext, UserSys dto, int page, int pageSize) {
		// TODO Auto-generated method stub
		PageHelper.startPage(page, pageSize);
		return mapper.reSelect(dto);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.hand.sys.sys_user.service.IUserSysService#srmUserTypeSelect(com.hand.hap.
	 * core.IRequest, com.hand.sys.sys_user.dto.UserSys, int, int)
	 */
	@Override
	public List<UserSys> srmUserTypeSelect(IRequest requestContext, UserSys dto, int page, int pageSize) {
		// TODO Auto-generated method stub
		PageHelper.startPage(page, pageSize);
		return mapper.srmUserTypeSelect(dto);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.hand.sys.sys_user.service.IUserSysService#loginFail(java.lang.String)
	 */
	@Override
	public void loginFail(String userName) {
		if (userName.equals("admin"))
			return;
		UserSys sear = new UserSys();
		sear.setUserName(userName);
		List<UserSys> result = mapper.select(sear);
		if (result != null && result.size() > 0) {
			List<SysConfig> configs = sysConfigMapper.selectAll();
			String loginFailuerUpperLimitConfig = configs.stream().filter(p -> {
				return "LOGIN_FAILUER_UPPER_LIMIT".equals(p.getConfigCode());
			}).collect(Collectors.toList()).get(0).getConfigValue();
			Integer loginFailuerUpperLimit = Integer.valueOf(loginFailuerUpperLimitConfig);
			List<UserSys> res = mapper.getLoginData(result.get(0).getUserId());
			if (res != null && res.size() > 0 && res.stream().filter(p -> p.getSuccessFlag().equals("T")).count() == 0
					&& res.size() >= (loginFailuerUpperLimit - 1) && !result.get(0).getStatus().equals("LOCK")) {
				// 执行锁定
				UserSys usupdate = new UserSys();
				usupdate.setUserId(result.get(0).getUserId());
				usupdate.setStatus("LOCK");
				mapper.updateByPrimaryKeySelective(usupdate);
				userCache.remove(userName);
			}
			// 记录此次失败记录
			UserLogin ul = new UserLogin();
			ul.setUserId(result.get(0).getUserId().longValue());
			ul.setLoginTime(new Date());
			ul.setSuccessFlag("F");
			userLoginMapper.insertSelective(ul);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.hand.sys.sys_user.service.IUserSysService#loginSuccess(java.lang.String)
	 */
	@Override
	public void loginSuccess(String username) {
		UserSys sear = new UserSys();
		sear.setUserName(username);
		List<UserSys> result = mapper.select(sear);
		if (result != null && result.size() > 0) {
			if (result.get(0).getUserName().equals("admin")) {// 对admin不做处理
				return;
			}
			if (StringUtil.isNotEmpty(result.get(0).getAttribute1()) && !result.get(0).getAttribute1().equals("0")) {
				UserSys update = new UserSys();
				update.setUserId(result.get(0).getUserId());
				update.setAttribute1("0");
				update.setLastUpdateDate(new Date());
				update.setLastUpdatedBy(-1l);
				mapper.updateByPrimaryKeySelective(update);
			}
		}
	}

	/**
	 * ureport use data source test 报表查询
	 */
	@Override
	public List<UserSys> loadData(String dsName, String datasetName, Map<String, Object> parameters) {
		UserSys ser = new UserSys();
		ser.setUserId(10011f);
		return mapper.select(ser);
	}
}