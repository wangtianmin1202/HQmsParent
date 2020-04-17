package com.hand.hqm.file_manager_his.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.system.service.IBaseService;
import com.hand.hqm.file_manager.dto.Manager;
import com.hand.hqm.file_manager_his.dto.ManagerHis;

public interface IManagerHisService extends IBaseService<ManagerHis>, ProxySelf<IManagerHisService>{
	List<ManagerHis>  myselect(IRequest requestContext,ManagerHis dto,int page, int pageSize); 
	 ResponseData fileUpload(IRequest requestCtx, HttpServletRequest request);
}