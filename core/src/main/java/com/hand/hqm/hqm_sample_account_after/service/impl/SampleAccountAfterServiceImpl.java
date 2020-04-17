package com.hand.hqm.hqm_sample_account_after.service.impl;

import com.github.pagehelper.PageHelper;
import com.hand.hap.account.dto.User;
import com.hand.hap.account.mapper.UserMapper;
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

import com.hand.hqm.hqm_sample_account.dto.SampleAccount;
import com.hand.hqm.hqm_sample_account.mapper.SampleAccountMapper;
import com.hand.hqm.hqm_sample_account_after.dto.SampleAccountAfter;
import com.hand.hqm.hqm_sample_account_after.mapper.SampleAccountAfterMapper;
import com.hand.hqm.hqm_sample_account_after.service.ISampleAccountAfterService;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class SampleAccountAfterServiceImpl extends BaseServiceImpl<SampleAccountAfter> implements ISampleAccountAfterService{
	 @Autowired
	 SampleAccountMapper sampleAccountMapper;
	
	@Autowired
	SampleAccountAfterMapper mapper;
	
	@Autowired
	ISampleAccountAfterService service;
	@Override
	public List<SampleAccountAfter> reSelect(IRequest requestContext, SampleAccountAfter dto, int page, int pageSize) {
		// TODO Auto-generated method stub
		PageHelper.startPage(page, pageSize);
		return mapper.reSelect(dto);
	}
	@Override
	public List<SampleAccountAfter> selectBf(IRequest requestContext, SampleAccountAfter dto, int page, int pageSize) {
		// TODO Auto-generated method stub
		PageHelper.startPage(page, pageSize);
		return mapper.selectBf(dto);
	}
	@Override
	public List<SampleAccountAfter> selectLy(IRequest requestContext, SampleAccountAfter dto, int page, int pageSize) {
		// TODO Auto-generated method stub
		PageHelper.startPage(page, pageSize);
		return mapper.selectLy(dto);
	}
	@Override
	public List<SampleAccountAfter> createSave(IRequest requestContext, SampleAccountAfter dto) {
		// TODO Auto-generated method stub
		
		//add by jrf 10.28 
		//样品编号 流水号
		List<SampleAccountAfter> res = new ArrayList<SampleAccountAfter>();
		
		Date date = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		String time = dateFormat.format(date);
		String inspectionNumStart =time.substring(2) ;
		// 流水一个检验单号			
		String Num =getInspectionNumber("S"+inspectionNumStart);	
		dto.setSampleCode(Num);
		//赋值在库状态及分析状态
		String returnStatus = dto.getReturnStatus();
		if ("1".equals(returnStatus)) {
			dto.setSealingSamplePositionStatus("1");
			dto.setAnalysisStatus("1");
		} else {
			dto.setSealingSamplePositionStatus("4");
		}
		service.insertSelective(requestContext, dto);
		res.add(dto);
		return res;
	}
	
	@Override
    public ResponseData saveFx(SampleAccountAfter dto,IRequest requestCtx, HttpServletRequest request) {  
    	//获取当前的 登记人
		SampleAccountAfter sampleAccountAfter = new SampleAccountAfter();
		sampleAccountAfter=mapper.selectByPrimaryKey(dto);
		sampleAccountAfter.setAnalysisResult(dto.getAnalysisResult());
		sampleAccountAfter.setBadComponent(dto.getBadComponent());
		sampleAccountAfter.setAnalyst(dto.getAnalyst());
		//赋值
		String method = dto.getProcessingMethod();
		sampleAccountAfter.setProcessingMethod(method);
		if ("0".equals(method)) {
			sampleAccountAfter.setAnalysisStatus("2");
		} else {
			sampleAccountAfter.setAnalysisStatus("3");
			if ("2".equals(method)) {
				sampleAccountAfter.setScrapStatus("2");
			}
		}
		
		self().updateByPrimaryKey(requestCtx, sampleAccountAfter);		
	    return new ResponseData();
     }  

	@Override
	public ResponseData updateData(IRequest requestContext, SampleAccountAfter dto) {
		// TODO Auto-generated method stub
		self().updateByPrimaryKey(requestContext, dto);	
		return new ResponseData();
	}
	
	 public String getInspectionNumber(String  samplecode) {
			Integer count = 1;
			SampleAccount sr = new SampleAccount();
			sr.setSampleCode(samplecode) ;
			List<SampleAccount> list = new ArrayList<SampleAccount>();
			list = sampleAccountMapper.selectMaxCodeByDay(sr);
			if (list != null && list.size() > 0) {
				String NoNum = list.get(0).getSampleCode();
				String number = NoNum.substring(7,9);// 流水
				count =Integer.valueOf(number) + 1 ;
			}
			String str = String.format("%02d", count);
			return samplecode + str;// 最终检验单号
		}
	 
	 @Autowired
	 private UserMapper userMapper;
	/* (non-Javadoc)
	 * @see com.hand.hqm.hqm_sample_account_after.service.ISampleAccountAfterService#checkUserId(com.hand.hap.core.IRequest, java.lang.Float)
	 */
	@Override
	public ResponseData checkUserId(IRequest requestContext, Float userId) {
		// TODO Auto-generated method stub
		ResponseData responseData = new ResponseData();
		User user = userMapper.selectByPrimaryKey(userId);
		if (!"wy".equals(user.getUserName()) && !"daisy".equals(user.getUserName())) {
			responseData.setSuccess(false);
			responseData.setMessage("您暂无编辑权限");
		}
		return responseData;
	}
}