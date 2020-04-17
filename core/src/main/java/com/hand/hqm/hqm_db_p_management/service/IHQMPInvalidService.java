package com.hand.hqm.hqm_db_p_management.service;

import java.io.InputStream;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.system.service.IBaseService;
import com.hand.hqm.hqm_db_p_management.dto.HQMPInvalid;

public interface IHQMPInvalidService extends IBaseService<HQMPInvalid>, ProxySelf<IHQMPInvalidService> {
	public List<HQMPInvalid> query();
	
	public List<HQMPInvalid> save(IRequest requestCtx,List<HQMPInvalid> list);
	
	public int delete(List<HQMPInvalid> list);
	
	public ResponseData inputDataFromExcel(HttpServletRequest request, IRequest requestContext,InputStream inputStream) throws Exception;

}