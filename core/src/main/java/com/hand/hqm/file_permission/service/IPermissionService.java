package com.hand.hqm.file_permission.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.system.service.IBaseService;
import com.hand.hqm.file_permission.dto.Permission;
import com.hand.hqm.hqm_qua_ins_time_l.dto.QuaInsTimeL;

public interface IPermissionService extends IBaseService<Permission>, ProxySelf<IPermissionService>{

	
	List<Permission>  myselect(IRequest requestContext,Permission dto,int page, int pageSize); 
	List<Permission>  myselectline(IRequest requestContext,Permission dto,int page, int pageSize); 
    ResponseData check(IRequest requestCtx, Permission dto);  
   // ResponseData addNewInspection(Permission dto,IRequest requestCtx, HttpServletRequest request);
    ResponseData addNewInspection(IRequest requestContext, List<Permission> dto);
}