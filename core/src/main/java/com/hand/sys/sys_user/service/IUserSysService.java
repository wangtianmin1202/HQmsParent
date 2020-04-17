package com.hand.sys.sys_user.service;

import java.util.List;
import java.util.Map;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;
import com.hand.sys.sys_user.dto.UserSys;

public interface IUserSysService extends IBaseService<UserSys>, ProxySelf<IUserSysService>{

	/**
	 * @description
	 * @author tianmin.wang
	 * @date 2019年12月26日 
	 * @param requestContext
	 * @param dto
	 * @param page
	 * @param pageSize
	 * @return
	 */
	List<UserSys> reSelect(IRequest requestContext, UserSys dto, int page, int pageSize);
	
	/**
	 * 
	 * @description
	 * @author tianmin.wang
	 * @date 2019年12月27日 
	 * @param requestContext
	 * @param dto
	 * @param page
	 * @param pageSize
	 * @return
	 */
	List<UserSys> srmUserTypeSelect(IRequest requestContext, UserSys dto, int page, int pageSize);
	
	/**
	 * 
	 * @description 登陆失败记录
	 * @author tianmin.wang
	 * @date 2019年12月30日 
	 * @param userName
	 */
	void loginFail(String userName);

	/**
	 * @description 登陆成功清空计数记录
	 * @author tianmin.wang
	 * @date 2019年12月30日 
	 * @param userName
	 */
	void loginSuccess(String userName);

	/**
	 * @description
	 * @author tianmin.wang
	 * @date 2020年1月9日 
	 * @param dsName
	 * @param datasetName
	 * @param parameters
	 * @return
	 */
	List<UserSys> loadData(String dsName, String datasetName, Map<String, Object> parameters);
}