package com.hand.hqm.hqm_sample_scrapped.service.impl;

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

import com.hand.hqm.hqm_sample.dto.Sample;
import com.hand.hqm.hqm_sample_account.dto.SampleAccount;
import com.hand.hqm.hqm_sample_account.mapper.SampleAccountMapper;
import com.hand.hqm.hqm_sample_account.service.ISampleAccountService;
import com.hand.hqm.hqm_sample_scrapped.dto.SampleScrapped;
import com.hand.hqm.hqm_sample_scrapped.mapper.SampleScrappedMapper;
import com.hand.hqm.hqm_sample_scrapped.service.ISampleScrappedService;
import com.hand.hqm.hqm_scrapped_detail.dto.ScrappedDetail;
import com.hand.hqm.hqm_scrapped_detail.mapper.ScrappedDetailMapper;

import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class SampleScrappedServiceImpl extends BaseServiceImpl<SampleScrapped> implements ISampleScrappedService{
	 @Autowired
	 SampleAccountMapper  sampleAccountMapper;	 
	 @Autowired
	 ISampleAccountService   SampleAccountService;
	 @Autowired
	 SampleScrappedMapper  sampleScrappedMapper;
	 @Autowired
	 ISampleScrappedService  SampleScrappedService;
	 @Autowired
	 ScrappedDetailMapper  scrappedDetailMapper;
	 /**
	     * 样品新建
	     * @param dto 操作数据集
	     * @param result 结果参数
	     * @param request 请求
	     * @return 操作结果
	     */
	 @Override
	    public ResponseData addNew(SampleScrapped dto,IRequest requestCtx, HttpServletRequest request) {  
	    	//获取当前的 登记人
		   SampleScrapped test =new SampleScrapped();
		   SampleScrapped res =new SampleScrapped();
		   test.setDocumentStatus("1");
		   test.setDocumentType("1");
		    //生成单号
   	        String Num;
			Date date = new Date();
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
			String time = dateFormat.format(date);
			String inspectionNumStart =time.substring(2) ;
			// 流水一个检验单号			
			Num = getInspectionNumber(inspectionNumStart);
			test.setApplicantDocumentCode(Num);
			res= SampleScrappedService.insertSelective(requestCtx, test);	
			List<SampleScrapped> li =new ArrayList<SampleScrapped>();
			li.add(res);
			ResponseData r  =new ResponseData();
			r.setRows(li);
			return r;
	 } 
	 
	 /**
	     * 测试样品页面查询
	     * @param dto 查询内容
	     * @param page 页码
	     * @param pageSize 页大小
	     * @param request 请求
	     * @return 结果集
	     */
	 @Override
	    public ResponseData addNewforcs(SampleScrapped dto,IRequest requestCtx, HttpServletRequest request) {  
	    	//获取当前的 登记人
		   SampleScrapped test =new SampleScrapped();
		   SampleScrapped res =new SampleScrapped();
		   test.setDocumentStatus("1");
		   test.setDocumentType("3");
		    //生成单号
	        String Num;
			Date date = new Date();
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
			String time = dateFormat.format(date);
			String inspectionNumStart =time.substring(2) ;
			// 流水一个检验单号			
			Num = getInspectionNumber(inspectionNumStart);
			test.setApplicantDocumentCode(Num);
			res= SampleScrappedService.insertSelective(requestCtx, test);	
			List<SampleScrapped> li =new ArrayList<SampleScrapped>();
			li.add(res);
			ResponseData r  =new ResponseData();
			r.setRows(li);
			return r;
	 } 
	 //获取流水
	   public String getInspectionNumber(String inspectionNumStart) {
			Integer count = 1;
			SampleScrapped sr = new SampleScrapped();
			sr.setApplicantDocumentCode(inspectionNumStart) ;
			List<SampleScrapped> list = new ArrayList<SampleScrapped>();
			list = sampleScrappedMapper.selectMaxNumber(sr);
			if (list != null && list.size() > 0) {
				String NoNum = list.get(0).getApplicantDocumentCode();
				String number = NoNum.substring(NoNum.length() - 5);// 流水
				count =Integer.valueOf(number) + 1 ;
			}
			String str = String.format("%05d", count);
			return inspectionNumStart + str;// 最终检验单号
		}

	   /**
	     * 提交
	     * @param dto 操作数据集
	     * @param result 结果参数
	     * @param request 请求
	     * @return 操作结果
	     */
@Override
public ResponseData comfirm(IRequest requestContext, List<String> headList) {
	//List<PoHeaders> headList1 = new ArrayList();
	    Float id =Float.valueOf(headList.get(0));
	    String status =headList.get(1);
	    SampleScrapped test =new SampleScrapped();
	    test=sampleScrappedMapper.selectByPrimaryKey(id);
	    
	    if(status.equals("3"))
	    {
	    	if(!test.getDocumentStatus().equals("2"))
	    	{
	    		ResponseData data =new ResponseData();
	    		data.setSuccess(false);
	    		data.setMessage("只有状态为待审批的报废单才可以审批 !");
	    		return data;
	    	}
	    }
	    if(status.equals("4"))
	    {
	    	if(!test.getDocumentStatus().equals("2"))
	    	{
	    		ResponseData data =new ResponseData();
	    		data.setSuccess(false);
	    		data.setMessage("只有状态为待审批的报废单才可以驳回 !");
	    		return data;
	    	}
	    }
	    if(status.equals("5"))
	    {
	    	if(!test.getDocumentStatus().equals("3"))
	    	{
	    		ResponseData data =new ResponseData();
	    		data.setSuccess(false);
	    		data.setMessage("只有已通过的报废单才可以关闭!");
	    		return data;
	    	}
	    }
	    
	    SampleScrapped head = new SampleScrapped();
	    head = sampleScrappedMapper.selectByPrimaryKey(id);
		head.setDocumentStatus(status);
		head.setApprover((float)requestContext.getUserId());
		head.setApprovalDate(new Date());		
		SampleScrappedService.updateByPrimaryKey(requestContext, head);
	   
	    //更新样品表
		 if(status.equals("5"))
		    {
			 
			 ScrappedDetail line =new ScrappedDetail();
			 line.setScrappedId(id);
			// SampleAccount main =new SampleAccount();
			 //main.setScrappedDocument(head.getApplicantDocumentCode());
			 List<ScrappedDetail> lines =scrappedDetailMapper.select(line);
			 for(ScrappedDetail sample_1:lines)
			 {
				 SampleAccount main =new SampleAccount();
				 main.setSampleId(sample_1.getSampleId());
				 main= sampleAccountMapper.selectByPrimaryKey(main);
				 main.setSealingSampleStatus("2");
				 main.setScrappedFlag("Y");
				 main.setScrappedDate(new Date());
				 SampleAccountService.updateByPrimaryKey(requestContext, main);
			 }		 
		    }
				
	    return new ResponseData();
}
/**
 * 查询
 * @param dto 查询内容
 * @param page 页码
 * @param pageSize 页大小
 * @param request 请求
 * @return 结果集
 */
@Override
public List<SampleScrapped> myselect(IRequest requestContext, SampleScrapped dto, int page, int pageSize) {
    PageHelper.startPage(page, pageSize);
    return sampleScrappedMapper.myselect(dto);
 }
/**
 * 测试样品页面查询
 * @param dto 查询内容
 * @param page 页码
 * @param pageSize 页大小
 * @param request 请求
 * @return 结果集
 */
@Override
public List<SampleScrapped> myselectforcs(IRequest requestContext, SampleScrapped dto, int page, int pageSize) {
    PageHelper.startPage(page, pageSize);
    return sampleScrappedMapper.myselectforcs(dto);
 }
}
