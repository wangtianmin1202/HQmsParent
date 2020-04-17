package com.hand.jobs.job;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.hand.hap.job.AbstractJob;
import com.hand.hap.mail.dto.Message;
import com.hand.hap.mail.mapper.MessageMapper;
import com.hand.hap.mail.service.IEmailService;
import com.hand.hap.system.dto.SysConfig;
import com.hand.hap.system.service.ISysConfigService;
import com.hand.hap.task.info.ExecutionInfo;
import com.hand.hap.task.service.ITask;

import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.hand.hap.account.dto.User;
import com.hand.hap.account.service.IUserService;
import com.hand.hap.cache.impl.UserCache;
import com.hand.hap.core.IRequest;
import com.hand.hap.core.impl.ServiceRequest;
import com.hand.hqm.hqm_pqc_inspection_h.dto.PqcInspectionH;
import com.hand.hqm.hqm_pqc_inspection_h.service.IPqcInspectionHService;
import com.hand.hqm.hqm_pqc_inspection_l.dto.PqcInspectionL;
import com.hand.hqm.hqm_qua_ins_time_l.dto.QuaInsTimeL;
import com.hand.hqm.hqm_qua_ins_time_l.mapper.QuaInsTimeLMapper;
import com.hand.hqm.hqm_qua_ins_time_l.service.IQuaInsTimeLService;
import com.hand.sys.sys_user.dto.UserSys;
import com.hand.sys.sys_user.mapper.UserSysMapper;

import org.apache.commons.collections.CollectionUtils;

/**
 * 定时锁定 最后登陆时间到现在超过 N 天 的账号/用户
 * 
 * @author tainmin.wang
 * @version date：2019年12月30日 12点18
 * 
 */
@Transactional(rollbackFor = Exception.class)
public class LoginOverDayLock extends AbstractJob implements ITask {

	@Autowired
	private UserCache userCache;
	@Autowired
	private ISysConfigService configService;
	@Autowired
	private IUserService userService;
	@Autowired
	private UserSysMapper userSysMapper;

	@Override
	public void execute(ExecutionInfo executionInfo) throws Exception {
		self();
	}

	@Override
	public void safeExecute(JobExecutionContext context) throws Exception {
		self();
	}

	private void self() throws Exception {
		ServiceRequest sr = new ServiceRequest();
		sr.setLocale("zh_CN");
		List<SysConfig> sysConfigs = configService.selectAll(sr);
		Float lastLoginSuccessDays = 30f;// 配置项 登陆成功间隔天数
		List<SysConfig> filterResult = sysConfigs.stream()
				.filter(p -> p.getConfigCode().equals("LAST_LOGIN_SUCCESS_DAYS")).collect(Collectors.toList());
		if (filterResult != null && filterResult.size() > 0) {
			lastLoginSuccessDays = Float.valueOf(filterResult.get(0).getConfigValue());
		}
		List<User> users = userService.selectAll(sr);
		for (User user : users) {// 对满足条件的 进行失效
			if (user.getUserName().equals("admin"))
				continue;
			Float days = userSysMapper.lastLoginSuccessDaysByUserId(user.getUserId());
			if (days != null && days >= lastLoginSuccessDays) {
//				user.setStatus("LOCK");
//				userService.updateByPrimaryKeySelective(null, user);
				UserSys upor = new UserSys();
				upor.setUserId(Float.valueOf(user.getUserId()));
				upor.setStatus("LOCK");
				userSysMapper.updateByPrimaryKeySelective(upor);
				userCache.remove(user.getUserName());
			}
		}
	}
}
