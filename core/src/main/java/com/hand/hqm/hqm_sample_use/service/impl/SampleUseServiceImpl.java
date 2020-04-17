package com.hand.hqm.hqm_sample_use.service.impl;

import com.github.pagehelper.PageHelper;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.system.service.impl.BaseServiceImpl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hand.hqm.hqm_sample_account.dto.SampleAccount;
import com.hand.hqm.hqm_sample_account.mapper.SampleAccountMapper;
import com.hand.hqm.hqm_sample_account.service.ISampleAccountService;
import com.hand.hqm.hqm_sample_scrapped.dto.SampleScrapped;
import com.hand.hqm.hqm_sample_use.dto.SampleUse;
import com.hand.hqm.hqm_sample_use.mapper.SampleUseMapper;
import com.hand.hqm.hqm_sample_use.service.ISampleUseService;
import com.hand.hqm.hqm_use_detail.dto.UseDetail;
import com.hand.hqm.hqm_use_detail.mapper.UseDetailMapper;

import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class SampleUseServiceImpl extends BaseServiceImpl<SampleUse> implements ISampleUseService{
	@Autowired
	SampleUseMapper sampleUseMapper;
	@Autowired
	ISampleUseService  SampleUseService;
	@Autowired
	UseDetailMapper  useDetailMapper;
	@Autowired
	SampleAccountMapper sampleAccountMapper;
	@Autowired
	ISampleAccountService SampleAccountService;
	 /**
     * 查询
     * @param dto 查询内容
     * @param page 页码
     * @param pageSize 页大小
     * @param request 请求
     * @return 结果集
     */
	@Override
	public List<SampleUse> myselect(IRequest requestContext, SampleUse dto, int page, int pageSize) {
	    PageHelper.startPage(page, pageSize);
	    return sampleUseMapper.myselect(dto);
	 }
	/**
     * 查询历史
     * @param dto 查询内容
     * @param page 页码
     * @param pageSize 页大小
     * @param request 请求
     * @return 结果集
     */
	@Override
	public List<SampleUse> select_his(IRequest requestContext, SampleUse dto, int page, int pageSize) {
	    PageHelper.startPage(page, pageSize);
	    return sampleUseMapper.select_his(dto);
	 }
	 /**
     * 提交
     * @description
     * @author tianmin.wang
     * @date 2019年11月22日 
     * @param request
     * @param dto
     * @return
     */
	@Override
	public ResponseData comfirm(IRequest requestContext, List<String> headList) {
		//List<PoHeaders> headList1 = new ArrayList();
		    Float id =Float.valueOf(headList.get(0));
		    String status =headList.get(1);
		    SampleUse test =new SampleUse();
		    test=sampleUseMapper.selectByPrimaryKey(id);
		    
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
		    
		    if(status.equals("6"))
		    {
		    	if(!test.getDocumentStatus().equals("5"))
		    	{
		    		ResponseData data =new ResponseData();
		    		data.setSuccess(false);
		    		data.setMessage("只有已执行的领用单才可以关闭!");
		    		return data;
		    	}
		    }
		    
		    if(!status.equals("2"))
		    {
		    SampleUse head = new SampleUse();
		    head = sampleUseMapper.selectByPrimaryKey(id);
			head.setDocumentStatus(status);
			head.setApprover((float)requestContext.getUserId());
			head.setApprovalDate(new Date());		
			SampleUseService.updateByPrimaryKey(requestContext, head);
		    }
		    else
		    { 
		    SampleUse head = new SampleUse();
		    head = sampleUseMapper.selectByPrimaryKey(id);
			head.setDocumentStatus(status);
			/*head.setApprover((float)requestContext.getUserId());
			head.setApprovalDate(new Date());	*/	
			SampleUseService.updateByPrimaryKey(requestContext, head);
		    	
		    }
		    //更新样品表
			 if(status.equals("5"))
			    {
				 UseDetail  line =new UseDetail();
				 line.setUseId(id);
				 List<UseDetail> lines =useDetailMapper.select(line) ;
				 for(UseDetail sample_l :lines)	
				 {
					 SampleAccount sample_head =new SampleAccount();
					 sample_head.setSampleId(sample_l.getSampleId());
					 sample_head= sampleAccountMapper.selectByPrimaryKey(sample_head);
					 sample_head.setSealingSamplePositionStatus("2");
					 sample_head.setSampleUseDate(new Date());
					 SampleAccountService.updateByPrimaryKey(requestContext, sample_head);			 
				 }				 
			    }
					
		    return new ResponseData();
	}

	/**
     * 新建测试样品
     * @param dto 操作数据集
     * @param result 结果参数
     * @param request 请求
     * @return 操作结果
     */
	@Override
    public ResponseData addNewforcs(SampleUse dto,IRequest requestCtx, HttpServletRequest request) {  
    	//获取当前的 登记人
		SampleUse test =new SampleUse();
		SampleUse res =new SampleUse();
	   test.setDocumentStatus("1");
	   test.setDocumentType("3");
	    //生成单号
        String Num;
		Date date = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		String time = dateFormat.format(date);
		String inspectionNumStart =time.substring(2) ;
		// 流水一个检验单号			
		Num = getInspectionNumber("CL"+inspectionNumStart);
		test.setUseDocumentCode(Num);
		res= SampleUseService.insertSelective(requestCtx, test);	
		List<SampleUse> li =new ArrayList<SampleUse>();
		li.add(res);
		ResponseData r  =new ResponseData();
		r.setRows(li);
		return r;
 } 
	
	 //获取流水
	   public String getInspectionNumber(String inspectionNumStart) {
			Integer count = 1;
			SampleUse sr = new SampleUse();
			sr.setUseDocumentCode(inspectionNumStart) ;
			List<SampleUse> list = new ArrayList<SampleUse>();
			list = sampleUseMapper.selectMaxNumber(sr);
			if (list != null && list.size() > 0) {
				String NoNum = list.get(0).getUseDocumentCode();
				String number = NoNum.substring(NoNum.length() - 3);// 流水
				count =Integer.valueOf(number) + 1 ;
			}
			String str = String.format("%03d", count);
			return inspectionNumStart + str;// 最终检验单号
		}
	/* (non-Javadoc)
	 * @see com.hand.hqm.hqm_sample_use.service.ISampleUseService#forUse(com.hand.hap.core.IRequest, java.util.List)
	 */
	@Override
	public ResponseData forUse(IRequest requestContext, List<SampleAccount> dto) {
		// TODO Auto-generated method stub
		ResponseData responseData  =new ResponseData();
		String keepBy = dto.get(0).getKeepBy();
		String testBy = dto.get(0).getTestBy();
		String userId = dto.get(0).getUserId();
		String useDepartment = dto.get(0).getUseDepartment();
		String type = dto.get(0).getSampleType();
		Date now = new Date();
		dto.forEach(e -> {
			SampleAccount account = sampleAccountMapper.selectByPrimaryKey(e.getSampleId());
			//售后
			if ("2".equals(type)) {
				String status = account.getSealingSamplePositionStatus();
				if ("1".equals(status)) {
					account.setUserId(userId);
					account.setUseDepartment(useDepartment);
					account.setSealingSamplePositionStatus("3");
					account.setSampleUseDate(now);
					sampleAccountMapper.updateByPrimaryKeySelective(account);
				}else {
					responseData.setSuccess(false);
					responseData.setMessage("样品状态已改变");
				}
			}
			//测试
			if ("3".equals(type)) {
				String status = account.getSealingSamplePositionStatus();
				if ("1".equals(status)) {
					account.setKeepBy(keepBy);
					account.setTestBy(testBy);
					account.setSealingSamplePositionStatus("2");
					account.setSampleUseDate(now);
					sampleAccountMapper.updateByPrimaryKeySelective(account);
				}else {
					responseData.setSuccess(false);
					responseData.setMessage("样品状态已改变");
				}
			}
		});
		return responseData;
	}
	
	@Override
	public ResponseData scrap(IRequest requestContext, List<SampleAccount> dto) {
		// TODO Auto-generated method stub
		ResponseData responseData  =new ResponseData();
		String scrapReason = dto.get(0).getScrapReason();
		String type = dto.get(0).getSampleType();
		Date now = new Date();
		dto.forEach(e -> {
			SampleAccount account = sampleAccountMapper.selectByPrimaryKey(e.getSampleId());
			//售后
			if ("2".equals(type)) {
				String status = account.getScrapStatus();
				if (!"1".equals(status)) {
					account.setScrapStatus("1");
					account.setSealingSamplePositionStatus("2");
				}else {
					responseData.setSuccess(false);
					responseData.setMessage("样品状态已改变");
				}
			}
			//测试
			if ("3".equals(type)) {
				String status = account.getSealingSampleStatus();
				if ("1".equals(status)) {
					account.setSealingSampleStatus("2");
					account.setSealingSamplePositionStatus("3");	
				}else {
					responseData.setSuccess(false);
					responseData.setMessage("样品状态已改变");
				}
			}
			account.setScrappedDate(now);
			account.setScrapReason(scrapReason);
			sampleAccountMapper.updateByPrimaryKeySelective(account);
		});
		return responseData;
	}
}