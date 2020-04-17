package com.hand.plm.laboratory.service;

import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.system.service.IBaseService;
import com.hand.plm.laboratory.dto.LabUser;


public interface ILabUserService extends IBaseService<LabUser>, ProxySelf<ILabUserService>{

	
	List<LabUser> getUserInfoList(int pageNum, int pageSize);
	
	LabUser createUser(IRequest requestContext,LabUser dto);
	
	ResponseData deleteUser(IRequest requestContext,List<LabUser> dto);
	
	List<LabUser> getExcelUserData(IRequest requestContext);
	
	void excelExport(IRequest requestContext,HttpServletResponse response,HttpServletRequest request) throws Exception;
	
}