package com.hand.hqm.hqm_scrapped_detail.service.impl;

import com.github.pagehelper.PageHelper;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.system.service.impl.BaseServiceImpl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hand.hqm.hqm_sample_scrapped.dto.SampleScrapped;
import com.hand.hqm.hqm_scrapped_detail.dto.ScrappedDetail;
import com.hand.hqm.hqm_scrapped_detail.mapper.ScrappedDetailMapper;
import com.hand.hqm.hqm_scrapped_detail.service.IScrappedDetailService;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class ScrappedDetailServiceImpl extends BaseServiceImpl<ScrappedDetail> implements IScrappedDetailService{
	 @Autowired
	 ScrappedDetailMapper   scrappedDetailMapper;
	 @Autowired
	 IScrappedDetailService   ScrappedDetailService; 
	
	@Override
	    public List<ScrappedDetail> myselect(IRequest requestContext, ScrappedDetail dto, int page, int pageSize) {
	        PageHelper.startPage(page, pageSize);
	        return scrappedDetailMapper.myselect(dto);
		 }
	@Override
    public List<ScrappedDetail> selectforcs(IRequest requestContext, ScrappedDetail dto, int page, int pageSize) {
        PageHelper.startPage(page, pageSize);
        return scrappedDetailMapper.selectforcs(dto);
	 }
	@Override
    public List<ScrappedDetail> selectforsample(IRequest requestContext, ScrappedDetail dto, int page, int pageSize) {
        PageHelper.startPage(page, pageSize);
        return scrappedDetailMapper.selectforsample(dto);
	 }
	
	 public ResponseData addline(ScrappedDetail dto,IRequest requestCtx, HttpServletRequest request) {  
	    	//获取当前的 登记人
		 ResponseData r  =new ResponseData();
		 ScrappedDetail res =new ScrappedDetail();
		 int n = scrappedDetailMapper.selectCount(dto);
		 if(n>0)
		 {
			 r.setSuccess(false);
			 r.setMessage("该样品已存在");
			 return r ;
		 }
		 ScrappedDetailService.insertSelective(requestCtx, dto);		 
		 return r;
	 } 

	    @Override
		public void deleteRow(ScrappedDetail dto) {

			// 查询后代所有需要删除的附着对象
		/*	List<PfmeaLevel> delList = new ArrayList<>();
			delList.add(dto);
			queryDownAttachment(delList, Collections.singletonList(dto));
			// 删除
			self().batchDelete(delList);*/
			self().deleteByPrimaryKey(dto);
		}
	 
	 public ResponseData updateReason(ScrappedDetail dto,IRequest requestCtx, HttpServletRequest request) {  
	    	//获取当前的 登记人
		 ResponseData r  =new ResponseData();
		 ScrappedDetail res =new ScrappedDetail();
		 res =scrappedDetailMapper.selectByPrimaryKey(dto);
		 res.setReason(dto.getReason());
		 ScrappedDetailService.updateByPrimaryKey(requestCtx, res) ;		 
		 return r;
	 } 
}