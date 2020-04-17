/**
 * 
 */
package com.hand.hcs.hcs_certificate_file_manage.service.impl;

import org.activiti.engine.delegate.DelegateExecution;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.hand.hap.activiti.custom.IActivitiBean;
import com.hand.sys.sys_user.mapper.UserSysMapper;

/**
 * @author tainmin.wang
 * @version date：2020年3月24日 下午3:16:39
 * 
 */
@Service
public class GetSupplierUser implements IActivitiBean {

	@Autowired
	UserSysMapper userSysMapper;
	
	public String getEnginee(DelegateExecution execution) {
		Float supplierId = Float.valueOf((String) execution.getVariable("supplierId"));
		String returnEmployeeCode = userSysMapper.getEmployeeCodeBySupplierId(supplierId);
		if(StringUtils.isEmpty(returnEmployeeCode))
			return "wtm";
		else
			return returnEmployeeCode;
	}

	@Override
	public String getBeanName() {
		return "getSupplierUser";
	}
}
