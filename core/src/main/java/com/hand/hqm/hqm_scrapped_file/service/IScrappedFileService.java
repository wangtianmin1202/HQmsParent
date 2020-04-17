package com.hand.hqm.hqm_scrapped_file.service;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.system.service.IBaseService;
import com.hand.hqm.hqm_scrapped_file.dto.ScrappedFile;

public interface IScrappedFileService extends IBaseService<ScrappedFile>, ProxySelf<IScrappedFileService>{
	ResponseData fileUpload(IRequest requestCtx, HttpServletRequest request) throws IllegalStateException, IOException;
	List<ScrappedFile>  myselect(IRequest requestContext,ScrappedFile dto,int page, int pageSize); 

}