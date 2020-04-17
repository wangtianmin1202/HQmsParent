package com.hand.hqm.hqm_db_management.service;

import java.io.InputStream;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.system.service.IBaseService;
import com.hand.hqm.hqm_db_management.dto.HQMInvalid;

public interface IHQMInvalidService extends IBaseService<HQMInvalid>, ProxySelf<IHQMInvalidService> {
	public List<HQMInvalid> query();
	
	public List<HQMInvalid> save(IRequest requestCtx,List<HQMInvalid> list);
	
	public int delete(List<HQMInvalid> list);
	
	public ResponseData inputDataFromExcel(HttpServletRequest request, IRequest requestContext,InputStream inputStream) throws Exception;

}