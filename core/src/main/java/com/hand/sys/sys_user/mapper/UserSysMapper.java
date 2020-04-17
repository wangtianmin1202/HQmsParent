package com.hand.sys.sys_user.mapper;

import java.util.List;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.sys.sys_user.dto.UserSys;

public interface UserSysMapper extends Mapper<UserSys> {

	/**
	 * 
	 * @description
	 * @author tianmin.wang
	 * @date 2019年12月26日
	 * @param dto
	 * @return
	 */
	List<UserSys> selectSupplierName(UserSys dto);

	/**
	 * 
	 * @description
	 * @author tianmin.wang
	 * @date 2019年12月26日
	 * @param name
	 * @return
	 */
	Float getUserIdByEmployeeName(String name);

	/**
	 */
	String getEmployeeNameByUserId(Float userId);

	/**
	 * @description
	 * @author tianmin.wang
	 * @date 2019年12月26日
	 * @param dto
	 * @return
	 */
	List<UserSys> reSelect(UserSys dto);

	/**
	 * 
	 * @description
	 * @author tianmin.wang
	 * @date 2019年12月27日
	 * @param dto
	 * @return
	 */
	List<UserSys> srmUserTypeSelect(UserSys dto);

	/**
	 * @description
	 * @author tianmin.wang
	 * @date 2019年12月27日
	 * @param valueOf
	 * @return
	 */
	String getEmailByUserId(Float valueOf);
	
	/**
	 * 
	 * @description 获取某用户最后一次登陆成功到当前的时间
	 * @author tianmin.wang
	 * @date 2019年12月30日 
	 * @param userId
	 * @return
	 */
	Float lastLoginSuccessDaysByUserId(Long userId);
	
	/**
	 * 
	 * @description 成功登陆次数
	 * @author tianmin.wang
	 * @date 2020年1月3日 
	 * @param userId
	 * @return
	 */
	Integer getLoginSuccessCount(Float userId);
	
	
	/**
	 * 
	 * @description 失败登陆
	 * @author tianmin.wang
	 * @date 2020年1月3日 
	 * @param userId
	 * @return
	 */
	Integer getLoginFailCount(Float userId);
	
	
	/**
	 * 
	 * @description
	 * @author tianmin.wang
	 * @date 2020年1月3日 
	 * @param dto
	 * @return
	 */
	List<UserSys> getLoginData(Float userId);
	
	
	String getEmployeeCodeBySupplierId(Float supplierId);
}