package com.hand.hqm.hqm_scrapped_detail.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.system.service.IBaseService;
import com.hand.hqm.hqm_sample_scrapped.dto.SampleScrapped;
import com.hand.hqm.hqm_scrapped_detail.dto.ScrappedDetail;

public interface IScrappedDetailService extends IBaseService<ScrappedDetail>, ProxySelf<IScrappedDetailService>{
	 List<ScrappedDetail>  myselect(IRequest requestContext,ScrappedDetail dto,int page, int pageSize); 
	 ResponseData addline(ScrappedDetail dto,IRequest requestCtx, HttpServletRequest request);
	 ResponseData updateReason(ScrappedDetail dto,IRequest requestCtx, HttpServletRequest request);
	 List<ScrappedDetail>  selectforsample(IRequest requestContext,ScrappedDetail dto,int page, int pageSize); 

	 List<ScrappedDetail>  selectforcs(IRequest requestContext,ScrappedDetail dto,int page, int pageSize); 
	 void deleteRow(ScrappedDetail dto);

}