package com.hand.hqm.hqm_sample_account.service.impl;

import com.github.pagehelper.PageHelper;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.system.service.impl.BaseServiceImpl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hand.hqm.hqm_pfmea_level.dto.PfmeaLevel;
import com.hand.hqm.hqm_sam_use_order_h.dto.SamUseOrderH;
import com.hand.hqm.hqm_sam_use_order_h.mapper.SamUseOrderHMapper;
import com.hand.hqm.hqm_sam_use_order_h.service.ISamUseOrderHService;
import com.hand.hqm.hqm_sample.dto.Sample;
import com.hand.hqm.hqm_sample_account.dto.SampleAccount;
import com.hand.hqm.hqm_sample_account.mapper.SampleAccountMapper;
import com.hand.hqm.hqm_sample_account.service.ISampleAccountService;
import com.hand.hqm.hqm_sample_scrapped.dto.SampleScrapped;

import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class SampleAccountServiceImpl extends BaseServiceImpl<SampleAccount> implements ISampleAccountService{
	 @Autowired
	 SampleAccountMapper sampleAccountMapper;
	 @Autowired
	 ISampleAccountService SampleAccountService;
	 /**
	     * 页面查询
	     * @param dto 查询内容
	     * @param page 页码
	     * @param pageSize 页大小
	     * @param request 请求
	     * @return 结果集
	     */
    @Override
    public List<SampleAccount> myselect(IRequest requestContext, SampleAccount dto, int page, int pageSize) {
        PageHelper.startPage(page, pageSize);
        return sampleAccountMapper.myselect(dto);
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
    public List<SampleAccount> myselectforcs(IRequest requestContext, SampleAccount dto, int page, int pageSize) {
        PageHelper.startPage(page, pageSize);
        return sampleAccountMapper.myselectforcs(dto);
	 }
    /**
     * 新建
     * @param dto 查询内容
     * @param request 请求
     * @return 结果集
     */
    @Override
    public ResponseData addNew(SampleAccount dto,IRequest requestCtx, HttpServletRequest request) {  
    	//获取当前的 登记人
     
    	dto.setRegistrationBy((float)requestCtx.getUserId());
    	SampleAccount test =new SampleAccount();
    	SampleAccount test1 =new SampleAccount();
    	test.setSupplierId(dto.getSupplierId());
    	test.setSealingSampleVerson(dto.getSealingSampleVerson()); 
    	test.setSampleModel(dto.getSampleModel());
    	List<SampleAccount> l = sampleAccountMapper.select(test);

    	if(l.size()==1)
    	{
    		ResponseData r =new ResponseData();
    		r.setMessage("违反唯一性(待确认)");
    		r.setSuccess(false);
    		return r;
    	}
    	test1.setItemId(dto.getItemId());
    	test1.setSupplierId(dto.getSupplierId());
    	List<SampleAccount> l2 = sampleAccountMapper.select(test1);
    	if(l2.size()>0)
    	{
    		for(SampleAccount n1 :l2)
    		{
    			n1.setEnableFlag("N");
    			n1.setInvalidDate(new Date());
    			n1.setSealingSampleStatus("3");
    			sampleAccountMapper.updateByPrimaryKey(n1);
    		}
    	 
    	 
    	}
    	
    	//dto.setSealingSampleVerson(fir);
    	dto.setManagementTime(new Date());
    	SampleAccountService.insertSelective(requestCtx, dto);
	    return new ResponseData();
     }  
    /**
	    * 删除
	    * @param dto 查询内容
	    * @return 结果集
	    */
    @Override
	public void deleteRow(SampleAccount dto) {

		// 查询后代所有需要删除的附着对象
	/*	List<PfmeaLevel> delList = new ArrayList<>();
		delList.add(dto);
		queryDownAttachment(delList, Collections.singletonList(dto));
		// 删除
		self().batchDelete(delList);*/
		self().deleteByPrimaryKey(dto);
	}
    /**
	    * 测试样品新建
	    * @param dto 查询内容
	    * @param request 请求
	    * @return 结果集
	    */
    @Override
    public ResponseData addNewforcs(SampleAccount dto,IRequest requestCtx, HttpServletRequest request) {  
    	//获取当前的 登记人
    	dto.setRegistrationBy((float)requestCtx.getUserId());
    	SampleAccount test =new SampleAccount();
    	test.setSampleModel(dto.getSampleModel());
    	test.setSampleType("3");
    	List<SampleAccount> l = sampleAccountMapper.select(test);
    	if(l.size()>0)
    		//取当先型号下对应的编号最大的后缀 然后新增
    	{   //不存在有后缀的编号，直接按照数量新增，加后缀 
    		if(l.size()==1)
    		{
    			String code = l.get(0).getSampleCode();
    			String codeNew =code+"-"+"1";
    			l.get(0).setSampleCode(codeNew);
    			SampleAccountService.updateByPrimaryKey(requestCtx, l.get(0));
    			Date date = new Date();
    			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
    			String time = dateFormat.format(date);
    			String inspectionNumStart =time.substring(2) ;
    			Float num =dto.getSampleNum();   		  
    			for(int n=2;n <= num+1 ;n++)
    			{
    				SampleAccount sampleCs =new SampleAccount();	
    				sampleCs.setSampleCode(code+"-"+n);	
    				sampleCs.setEnableFlag(dto.getEnableFlag());
    				sampleCs.setSampleType(dto.getSampleType());
    				sampleCs.setScrappedFlag(dto.getScrappedFlag());
    				sampleCs.setSampleAttribute(dto.getSampleAttribute());
    				sampleCs.setSealingSampleStatus(dto.getSealingSampleStatus());
    				sampleCs.setProduceFlag(dto.getProduceFlag());
    				sampleCs.setPlantId(dto.getPlantId());
    				sampleCs.setSampleModel(dto.getSampleModel());
    				sampleCs.setTestBy(dto.getTestBy());
    				sampleCs.setTestCode(dto.getTestCode());
    				sampleCs.setSampleGiveDate(dto.getSampleGiveDate());
    				sampleCs.setAvailableStatus(dto.getAvailableStatus());
    				sampleCs.setStorageLocation(dto.getStorageLocation());
    				sampleCs.setKeepBy(dto.getKeepBy());
    				sampleCs.setRegistrationBy((float)requestCtx.getUserId());
    				sampleCs.setSealingSamplePositionStatus(dto.getSealingSamplePositionStatus());
    				sampleCs.setDescription("CS");
    				sampleCs.setMachineComponent(dto.getMachineComponent());
    				SampleAccountService.insertSelective(requestCtx, sampleCs);
    			}
    		}
    		//存在有后缀的编号 先获取最大后缀的数量 然后接着新增
    		else
    		{    	
    			List<SampleAccount> sl= sampleAccountMapper.selectMaxNumber(dto);
    			String num_max= sl.get(0).getSampleCode();
    			String number = num_max.substring(12);
				int  count =Integer.valueOf(number) + 1 ;
				Float num =dto.getSampleNum();   
				String code = l.get(0).getSampleCode();
				for(int n=count;n < num + count;n++)
    			{
    				SampleAccount sampleCs =new SampleAccount();	
    				sampleCs.setSampleCode(code.substring(0,11)+"-"+n);	
    				sampleCs.setEnableFlag(dto.getEnableFlag());
    				sampleCs.setSampleType(dto.getSampleType());
    				sampleCs.setScrappedFlag(dto.getScrappedFlag());
    				sampleCs.setSampleAttribute(dto.getSampleAttribute());
    				sampleCs.setSealingSampleStatus(dto.getSealingSampleStatus());
    				sampleCs.setProduceFlag(dto.getProduceFlag());
    				sampleCs.setPlantId(dto.getPlantId());
    				sampleCs.setTestCode(dto.getTestCode());
    				sampleCs.setSampleGiveDate(dto.getSampleGiveDate());
    				sampleCs.setSampleModel(dto.getSampleModel());
    				sampleCs.setTestBy(dto.getTestBy());
    				sampleCs.setAvailableStatus(dto.getAvailableStatus());
    				sampleCs.setStorageLocation(dto.getStorageLocation());
    				sampleCs.setKeepBy(dto.getKeepBy());
    				sampleCs.setRegistrationBy((float)requestCtx.getUserId());
    				sampleCs.setSealingSamplePositionStatus(dto.getSealingSamplePositionStatus());
    				sampleCs.setDescription("CS");
    				sampleCs.setMachineComponent(dto.getMachineComponent());

    				SampleAccountService.insertSelective(requestCtx, sampleCs);
    			}
    			
    		}  	
    	}
    	else {
    		//先获取最大单号
    	    //生成单号
   	        Float sample_num=dto.getSampleNum();
   	        String Num;

			Date date = new Date();
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
			String time = dateFormat.format(date);
			String inspectionNumStart =time.substring(2) ;
			// 流水一个检验单号			
			Num = getInspectionNumber("TY"+inspectionNumStart);
    		//只新增	
    		if(dto.getSampleNum()==1)
    		{
    			dto.setSampleCode(Num);
    			SampleAccountService.insertSelective(requestCtx, dto);
    		}
    		else
    		{
    			for(int n=1;n<=sample_num;n++)
    			{
    				dto.setSampleCode(Num+"-"+n);
    				SampleAccountService.insertSelective(requestCtx, dto);
    			}
    			
    		}
    	}
	    return new ResponseData();
     }  
  
    //获取流水
	   public String getInspectionNumber(String  samplecode) {
			Integer count = 1;
			SampleAccount sr = new SampleAccount();
			sr.setSampleCode(samplecode) ;
			List<SampleAccount> list = new ArrayList<SampleAccount>();
			list = sampleAccountMapper.selectMaxCodeByDay(sr);
			if (list != null && list.size() > 0) {
				String NoNum = list.get(0).getSampleCode();
				String number = NoNum.substring(8,11);// 流水
				count =Integer.valueOf(number) + 1 ;
			}
			String str = String.format("%03d", count);
			return samplecode + str;// 最终检验单号
		}
	   /**
	    * 购买样品新建
	    * @param dto 查询内容
	    * @param request 请求
	    * @return 结果集
	    */
    @Override
    public ResponseData addnewforgm(SampleAccount dto,IRequest requestCtx, HttpServletRequest request) {  
    	//获取当前的 登记人
    	dto.setRegistrationBy((float)requestCtx.getUserId());
    	SampleAccount test =new SampleAccount();
    	test.setSampleModel(dto.getSampleModel());
    	test.setProduceFlag("N");
    	test.setSampleType("3");
    	List<SampleAccount> l = sampleAccountMapper.select(test);
    	if(l.size()>0)
    		//取当先型号下对应的编号最大的后缀 然后新增
    	{   //不存在有后缀的编号，直接按照数量新增，加后缀 
    		if(l.size()==1)
    		{
    			String code = l.get(0).getSampleCode();
    			String codeNew =code+"-"+"1";
    			l.get(0).setSampleCode(codeNew);
    			SampleAccountService.updateByPrimaryKey(requestCtx, l.get(0));
    			Date date = new Date();
    			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
    			String time = dateFormat.format(date);
    			String inspectionNumStart =time.substring(2) ;
    			Float num =dto.getSampleNum();   		  
    			for(int n=2;n <= num+1 ;n++)
    			{
    				SampleAccount sampleCs =new SampleAccount();	
    				sampleCs.setSampleCode(code+"-"+n);	
    				sampleCs.setEnableFlag(dto.getEnableFlag());
    				sampleCs.setSampleType(dto.getSampleType());
    				sampleCs.setScrappedFlag(dto.getScrappedFlag());
    				sampleCs.setSampleAttribute(dto.getSampleAttribute());
    				sampleCs.setSealingSampleStatus(dto.getSealingSampleStatus());
    				sampleCs.setProduceFlag(dto.getProduceFlag());
    				sampleCs.setPlantId(dto.getPlantId());
    				sampleCs.setSampleModel(dto.getSampleModel());
    				sampleCs.setTestBy(dto.getTestBy());
    				sampleCs.setTestCode(dto.getTestCode());
    				sampleCs.setSampleGiveDate(dto.getSampleGiveDate());
    				sampleCs.setAvailableStatus(dto.getAvailableStatus());
    				sampleCs.setStorageLocation(dto.getStorageLocation());
    				sampleCs.setKeepBy(dto.getKeepBy());
    				sampleCs.setRegistrationBy((float)requestCtx.getUserId());
    				sampleCs.setSealingSamplePositionStatus(dto.getSealingSamplePositionStatus());
    				sampleCs.setDescription("CS");
    				sampleCs.setMachineComponent(dto.getMachineComponent());
    				SampleAccountService.insertSelective(requestCtx, sampleCs);
    			}
    		}
    		//存在有后缀的编号 先获取最大后缀的数量 然后接着新增
    		else
    		{    	
    			List<SampleAccount> sl= sampleAccountMapper.selectMaxNumber(dto);
    			String num_max= sl.get(0).getSampleCode();
    			String number = num_max.substring(12);
				int  count =Integer.valueOf(number) + 1 ;
				Float num =dto.getSampleNum();   
				String code = l.get(0).getSampleCode();
				for(int n=count;n < num + count;n++)
    			{
    				SampleAccount sampleCs =new SampleAccount();	
    				sampleCs.setEnableFlag(dto.getEnableFlag());
    				sampleCs.setSampleType(dto.getSampleType());
    				sampleCs.setScrappedFlag(dto.getScrappedFlag());
    				sampleCs.setSealingSampleStatus(dto.getSealingSampleStatus());
    				sampleCs.setProduceFlag(dto.getProduceFlag());
    				sampleCs.setSampleAttribute(dto.getSampleAttribute());
    				sampleCs.setPlantId(dto.getPlantId());
    				sampleCs.setSampleModel(dto.getSampleModel());
    				sampleCs.setKeepBy(dto.getKeepBy());
    				sampleCs.setCompetingBrand(dto.getCompetingBrand());
    				sampleCs.setTestBy(dto.getTestBy());
    				sampleCs.setAvailableStatus(dto.getAvailableStatus());
    				sampleCs.setStorageLocation(dto.getStorageLocation());		
    				sampleCs.setRegistrationBy((float)requestCtx.getUserId());
    				sampleCs.setSealingSamplePositionStatus(dto.getSealingSamplePositionStatus());
    				sampleCs.setDescription("GM");
    				sampleCs.setMachineComponent(dto.getMachineComponent());
    				
    				SampleAccountService.insertSelective(requestCtx, sampleCs);
    				
    				
    			}
    			
    		}  	
    	}
    	else {
    		//先获取最大单号
    	    //生成单号
   	        Float sample_num=dto.getSampleNum();
   	        String Num;

			Date date = new Date();
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
			String time = dateFormat.format(date);
			String inspectionNumStart =time.substring(2) ;
			// 流水一个检验单号			
			Num = getInspectionNumber("TY"+inspectionNumStart);
    		//只新增	
    		if(dto.getSampleNum()==1)
    		{
    			dto.setSampleCode(Num);
    			SampleAccountService.insertSelective(requestCtx, dto);
    		}
    		else
    		{
    			for(int n=1;n<=sample_num;n++)
    			{
    				dto.setSampleCode(Num+"-"+n);
    				SampleAccountService.insertSelective(requestCtx, dto);
    			}
    			
    		}
    	}
    	/*if(l.size()==1)
    	{
    		//l.get(0).setSampleCode(code.substring(0,11)+"-"+n);	
    		l.get(0).setEnableFlag(dto.getEnableFlag());
    		l.get(0).setSampleType(dto.getSampleType());
    		l.get(0).setScrappedFlag(dto.getScrappedFlag());
    		l.get(0).setSealingSampleStatus(dto.getSealingSampleStatus());
    		l.get(0).setProduceFlag(dto.getProduceFlag());
    		l.get(0).setSampleAttribute(dto.getSampleAttribute());
    		l.get(0).setPlantId(dto.getPlantId());
    		l.get(0).setSampleModel(dto.getSampleModel());
    		l.get(0).setKeepBy(dto.getKeepBy());
    		l.get(0).setCompetingBrand(dto.getCompetingBrand());
    		l.get(0).setTestBy(dto.getTestBy());
    		l.get(0).setAvailableStatus(dto.getAvailableStatus());
    		l.get(0).setStorageLocation(dto.getStorageLocation());		
    		l.get(0).setRegistrationBy((float)requestCtx.getUserId());
    		l.get(0).setSealingSamplePositionStatus(dto.getSealingSamplePositionStatus());
    		l.get(0).setDescription("GM");
    		l.get(0).setMachineComponent(dto.getMachineComponent());
    		SampleAccountService.updateByPrimaryKey(requestCtx, l.get(0));
    	}
    	String Num;
    	Date date = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		String time = dateFormat.format(date);
		String inspectionNumStart =time.substring(2) ;
		// 流水一个检验单号			
		Num = getInspectionNumber("TY"+inspectionNumStart);
    	dto.setSampleCode(Num);
    	SampleAccountService.insertSelective(requestCtx, dto);*/
	    return new ResponseData();
     }  
}