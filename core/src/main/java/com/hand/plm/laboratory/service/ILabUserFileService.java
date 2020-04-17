package com.hand.plm.laboratory.service;

import javax.servlet.http.HttpServletRequest;
import com.hand.hap.system.dto.ResponseData;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;
import com.hand.plm.laboratory.dto.LabUser;
import com.hand.plm.laboratory.dto.LabUserFile;

public interface ILabUserFileService extends IBaseService<LabUserFile>, ProxySelf<ILabUserFileService>{
	
	ResponseData fileUpload(IRequest requestCtx, HttpServletRequest request,Float labUserId);
	
	ResponseData deleteFile(String filePath);
	

}