package com.hand.hqm.hqm_sample_scrapped_after.service.impl;

import com.github.pagehelper.PageHelper;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.system.service.IPromptService;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import com.hand.hap.util.SystemApiMethod;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hand.hqm.hqm_iqc_inspection_h.dto.IqcInspectionH;
import com.hand.hqm.hqm_sample_account_after.dto.SampleAccountAfter;
import com.hand.hqm.hqm_sample_account_after.service.ISampleAccountAfterService;
import com.hand.hqm.hqm_sample_scrapped.dto.SampleScrapped;
import com.hand.hqm.hqm_sample_scrapped_after.dto.SampleScrappedAfter;
import com.hand.hqm.hqm_sample_scrapped_after.mapper.SampleScrappedAfterMapper;
import com.hand.hqm.hqm_sample_scrapped_after.service.ISampleScrappedAfterService;
import com.hand.hqm.hqm_scrapped_detail_after.dto.ScrappedDetailAfter;
import com.hand.hqm.hqm_scrapped_detail_after.mapper.ScrappedDetailAfterMapper;

import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class SampleScrappedAfterServiceImpl extends BaseServiceImpl<SampleScrappedAfter>
		implements ISampleScrappedAfterService {

	@Autowired
	SampleScrappedAfterMapper mapper;
	
	@Autowired
	IPromptService iPromptService;
	
	@Autowired
	ScrappedDetailAfterMapper scrappedDetailAfterMapper;
	@Autowired
	ISampleAccountAfterService iSampleAccountAfterService;
	
	@Override
	public ResponseData addOne( SampleScrappedAfter dto,IRequest requestCtx, HttpServletRequest request) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		String time = dateFormat.format(new Date());
		String applicantDocumentCode = "SB" + time;
		// 流水一个报废单号
		applicantDocumentCode = getApplicantDocumentCode(applicantDocumentCode);
		dto.setApplicantDocumentCode(applicantDocumentCode);
		dto.setDocumentStatus("1");
		dto.setDocumentType("2");
		List<SampleScrappedAfter> returnList = new ArrayList<SampleScrappedAfter>();
		SampleScrappedAfter res =new SampleScrappedAfter();
		res =self().insertSelective(requestCtx, dto);
		returnList.add(res);
		ResponseData r  =new ResponseData();
		r.setRows(returnList);
		return r;
	}

	public String getApplicantDocumentCode(String inspectionNumStart) {
		Integer count = 1;
		SampleScrappedAfter sr = new SampleScrappedAfter();
		sr.setApplicantDocumentCode(inspectionNumStart);
		List<SampleScrappedAfter> list = new ArrayList<SampleScrappedAfter>();
		list = mapper.selectMaxNumber(sr);
		if (list != null && list.size() > 0) {
			String inspectionNum = list.get(0).getApplicantDocumentCode();
			String number = inspectionNum.substring(inspectionNum.length() - 3);// 流水
			count = Integer.valueOf(number) + 1;
		}
		String str = String.format("%03d", count);
		return inspectionNumStart + str;// 最终检验单号
	}

	@Override
	public List<SampleScrappedAfter> commit(IRequest requestContext, SampleScrappedAfter dto) {
		// TODO Auto-generated method stub
		List<SampleScrappedAfter> re = new ArrayList<>();
		dto.setDocumentStatus("2");
		self().updateByPrimaryKeySelective(requestContext, dto);
		re.add(dto);
		return re;
	}

	@Override
	public List<SampleScrappedAfter> approval(IRequest requestContext, SampleScrappedAfter dto) throws Exception {
		// TODO Auto-generated method stub
		SampleScrappedAfter res = self().selectByPrimaryKey(requestContext, dto);
		if(!"2".equals(res.getDocumentStatus())) {
			throw new Exception(SystemApiMethod.getPromptDescription(requestContext, iPromptService, "只有状态为待审批的报废单才可以审批"));
		}
		List<SampleScrappedAfter> re = new ArrayList<>();
		dto.setDocumentStatus("3");
		dto.setApprover(Float.valueOf(requestContext.getUserId().toString()));
		dto.setApprovalDate(new Date());
		self().updateByPrimaryKeySelective(requestContext, dto);
		re.add(dto);
		return re;
	}
	
	@Override
	public List<SampleScrappedAfter> turnDown(IRequest requestContext, SampleScrappedAfter dto) throws Exception {
		// TODO Auto-generated method stub
		SampleScrappedAfter res = self().selectByPrimaryKey(requestContext, dto);
		if(!"2".equals(res.getDocumentStatus())) {
			throw new Exception(SystemApiMethod.getPromptDescription(requestContext, iPromptService, "只有状态为待审批的报废单才可以驳回"));
		}
		List<SampleScrappedAfter> re = new ArrayList<>();
		dto.setDocumentStatus("4");
		dto.setApprover(Float.valueOf(requestContext.getUserId().toString()));
		dto.setApprovalDate(new Date());
		self().updateByPrimaryKeySelective(requestContext, dto);
		re.add(dto);
		return re;
	}
	
	@Override
	public List<SampleScrappedAfter> close(IRequest requestContext, SampleScrappedAfter dto) throws Exception {
		// TODO Auto-generated method stub
		SampleScrappedAfter res = self().selectByPrimaryKey(requestContext, dto);
		if(!"3".equals(res.getDocumentStatus())) {
			throw new Exception(SystemApiMethod.getPromptDescription(requestContext, iPromptService, "只有已通过的报废单才可以关闭"));
		}
		List<SampleScrappedAfter> re = new ArrayList<>();
		dto.setDocumentStatus("5");
		self().updateByPrimaryKeySelective(requestContext, dto);
		re.add(dto);
		sampleAccountScrap(requestContext,dto);
		return re;
	}
	
	public void sampleAccountScrap(IRequest requestContext,SampleScrappedAfter sampleScrappedAfter) {
		ScrappedDetailAfter sear = new ScrappedDetailAfter();
		sear.setScrappedId(sampleScrappedAfter.getScrappedId());
		List<ScrappedDetailAfter> detailList = scrappedDetailAfterMapper.select(sear);
		for(ScrappedDetailAfter fo : detailList) {
			SampleAccountAfter updator = new SampleAccountAfter();
			updator.setSampleId(fo.getSampleId());
			updator.setScrappedFlag("Y");
			updator.setSealingSampleStatus("2");
			updator.setScrappedDate(new Date());
			iSampleAccountAfterService.updateByPrimaryKeySelective(requestContext, updator);
		}
	}

	@Override
	public List<SampleScrappedAfter> reSelect(IRequest requestContext, SampleScrappedAfter dto, int page,
			int pageSize) {
		// TODO Auto-generated method stub
		PageHelper.startPage(page, pageSize);
		return mapper.reSelect(dto);
	}
}