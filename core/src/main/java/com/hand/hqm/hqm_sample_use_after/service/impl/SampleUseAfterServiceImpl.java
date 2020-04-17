package com.hand.hqm.hqm_sample_use_after.service.impl;

import com.github.pagehelper.PageHelper;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.service.IPromptService;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import com.hand.hap.util.SystemApiMethod;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hand.hqm.hqm_sample_account_after.dto.SampleAccountAfter;
import com.hand.hqm.hqm_sample_account_after.service.ISampleAccountAfterService;
import com.hand.hqm.hqm_sample_scrapped_after.dto.SampleScrappedAfter;
import com.hand.hqm.hqm_sample_use_after.dto.SampleUseAfter;
import com.hand.hqm.hqm_sample_use_after.mapper.SampleUseAfterMapper;
import com.hand.hqm.hqm_sample_use_after.service.ISampleUseAfterService;
import com.hand.hqm.hqm_scrapped_detail_after.dto.ScrappedDetailAfter;
import com.hand.hqm.hqm_use_detail_after.dto.UseDetailAfter;
import com.hand.hqm.hqm_use_detail_after.mapper.UseDetailAfterMapper;

import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class SampleUseAfterServiceImpl extends BaseServiceImpl<SampleUseAfter> implements ISampleUseAfterService{
	
	@Autowired
	SampleUseAfterMapper mapper;

	@Autowired
	IPromptService iPromptService;
	
	@Autowired
	UseDetailAfterMapper useDetailAfterMapper;
	
	@Autowired
	ISampleAccountAfterService iSampleAccountAfterService;
	
	@Override
	public List<SampleUseAfter> addOne(IRequest requestContext, SampleUseAfter dto, int page, int pageSize) {
		// TODO Auto-generated method stub
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		String time = dateFormat.format(new Date());
		String applicantDocumentCode = "SL" + time;
		// 流水一个报废单号
		applicantDocumentCode = getUseDocumentCode(applicantDocumentCode);
		dto.setUseDocumentCode(applicantDocumentCode);
		dto.setDocumentStatus("1");
		dto.setDocumentType("2");
		List<SampleUseAfter> returnList = new ArrayList<SampleUseAfter>();
		returnList.add(self().insertSelective(requestContext, dto));
		return returnList;
	}

	/**
	 * 
	 * @description
	 * @author tianmin.wang
	 * @date 2019年11月22日 
	 * @param inspectionNumStart
	 * @return
	 */
	public String getUseDocumentCode(String inspectionNumStart) {
		Integer count = 1;
		SampleUseAfter sr = new SampleUseAfter();
		sr.setUseDocumentCode(inspectionNumStart);
		List<SampleUseAfter> list = new ArrayList<SampleUseAfter>();
		list = mapper.selectMaxNumber(sr);
		if (list != null && list.size() > 0) {
			String inspectionNum = list.get(0).getUseDocumentCode();
			String number = inspectionNum.substring(inspectionNum.length() - 3);// 流水
			count = Integer.valueOf(number) + 1;
		}
		String str = String.format("%03d", count);
		return inspectionNumStart + str;// 最终检验单号
	}

	@Override
	public List<SampleUseAfter> reSelect(IRequest requestContext, SampleUseAfter dto, int page, int pageSize) {
		// TODO Auto-generated method stub
		PageHelper.startPage(page, pageSize);
		return mapper.reSelect(dto);
	}

	
	@Override
	public List<SampleUseAfter> commit(IRequest requestContext, SampleUseAfter dto) {
		List<SampleUseAfter> re = new ArrayList<>();
		dto.setDocumentStatus("2");
		self().updateByPrimaryKeySelective(requestContext, dto);
		re.add(dto);
		return re;
	}

	@Override
	public List<SampleUseAfter> approval(IRequest requestContext, SampleUseAfter dto) throws Exception {
		// TODO Auto-generated method stub
		SampleUseAfter res = self().selectByPrimaryKey(requestContext, dto);
		if(!"2".equals(res.getDocumentStatus())) {
			throw new Exception(SystemApiMethod.getPromptDescription(requestContext, iPromptService, "只有状态为待审批的领用单才可以审批"));
		}
		List<SampleUseAfter> re = new ArrayList<>();
		dto.setDocumentStatus("3");
		dto.setApprover(Float.valueOf(requestContext.getUserId().toString()));
		dto.setApprovalDate(new Date());
		self().updateByPrimaryKeySelective(requestContext, dto);
		re.add(dto);
		return re;
	}

	@Override
	public List<SampleUseAfter> turnDown(IRequest requestContext, SampleUseAfter dto) throws Exception {
		SampleUseAfter res = self().selectByPrimaryKey(requestContext, dto);
		if(!"2".equals(res.getDocumentStatus())) {
			throw new Exception(SystemApiMethod.getPromptDescription(requestContext, iPromptService, "只有状态为待审批的领用单才可以驳回"));
		}
		List<SampleUseAfter> re = new ArrayList<>();
		dto.setDocumentStatus("4");
		dto.setApprover(Float.valueOf(requestContext.getUserId().toString()));
		dto.setApprovalDate(new Date());
		self().updateByPrimaryKeySelective(requestContext, dto);
		re.add(dto);
		return re;
	}

	@Override
	public List<SampleUseAfter> execute(IRequest requestContext, SampleUseAfter dto) throws Exception {
		SampleUseAfter res = self().selectByPrimaryKey(requestContext, dto);
		if(!"3".equals(res.getDocumentStatus())) {
			throw new Exception(SystemApiMethod.getPromptDescription(requestContext, iPromptService, "只有状态为已通过的领用单才可以执行"));
		}
		List<SampleUseAfter> re = new ArrayList<>();
		dto.setDocumentStatus("5");
		dto.setApprover(Float.valueOf(requestContext.getUserId().toString()));
		dto.setApprovalDate(new Date());
		self().updateByPrimaryKeySelective(requestContext, dto);
		re.add(dto);
		sampleAccountUse(requestContext,dto);
		return re;
	}
	
	
	/**
	 * 
	 * @description
	 * @author tianmin.wang
	 * @date 2019年11月22日 
	 * @param requestContext
	 * @param sampleScrappedAfter
	 */
	public void sampleAccountUse(IRequest requestContext,SampleUseAfter sampleScrappedAfter) {
		UseDetailAfter sear = new UseDetailAfter();
		sear.setUseId(sampleScrappedAfter.getUseId());
		List<UseDetailAfter> detailList = useDetailAfterMapper.select(sear);
		for(UseDetailAfter fo : detailList) {
			SampleAccountAfter updator = new SampleAccountAfter();
			updator.setSampleId(fo.getSampleId());
			updator.setSealingSamplePositionStatus("3");
			iSampleAccountAfterService.updateByPrimaryKeySelective(requestContext, updator);
		}
	}
	
	/**
	 * 关闭
	 * @description
	 * @author tianmin.wang
	 * @date 2019年11月22日 
	 * @param requestContext
	 * @param sampleScrappedAfter
	 */
	public void sampleAccountUseClose(IRequest requestContext,SampleUseAfter sampleScrappedAfter) {
		UseDetailAfter sear = new UseDetailAfter();
		sear.setUseId(sampleScrappedAfter.getUseId());
		List<UseDetailAfter> detailList = useDetailAfterMapper.select(sear);
		for(UseDetailAfter fo : detailList) {
			SampleAccountAfter updator = new SampleAccountAfter();
			updator.setSampleId(fo.getSampleId());
			if(sampleScrappedAfter.getSampleIds().contains(String.valueOf(fo.getSampleId().intValue()))) {
				updator.setSealingSamplePositionStatus("1");
			}else {
				updator.setSealingSamplePositionStatus("4");
			}
			iSampleAccountAfterService.updateByPrimaryKeySelective(requestContext, updator);
		}
	}
	@Override
	public List<SampleUseAfter> close(IRequest requestContext, SampleUseAfter dto) throws Exception {
		SampleUseAfter res = self().selectByPrimaryKey(requestContext, dto);
		if(!"5".equals(res.getDocumentStatus())) {
			throw new Exception(SystemApiMethod.getPromptDescription(requestContext, iPromptService, "只有状态为已执行的领用单才可以关闭"));
		}
		List<SampleUseAfter> re = new ArrayList<>();
		dto.setDocumentStatus("6");
		dto.setApprover(Float.valueOf(requestContext.getUserId().toString()));
		dto.setApprovalDate(new Date());
		self().updateByPrimaryKeySelective(requestContext, dto);
		re.add(dto);
		sampleAccountUseClose(requestContext,dto);
		
		
		
		return re;
	}
}