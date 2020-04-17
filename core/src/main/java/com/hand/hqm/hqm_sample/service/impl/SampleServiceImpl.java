package com.hand.hqm.hqm_sample.service.impl;

import com.github.pagehelper.PageHelper;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.system.service.IPromptService;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import com.hand.hap.util.SystemApiMethod;
import com.hand.hcs.hcs_po_headers.dto.PoHeaders;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hand.hqm.hqm_sample.mapper.SampleMapper;
import com.hand.hqm.hqm_sample.dto.Sample;
import com.hand.hqm.hqm_sample.service.ISampleService;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class SampleServiceImpl extends BaseServiceImpl<Sample> implements ISampleService {
	@Autowired
	SampleMapper sampleMapper;
	@Autowired
	ISampleService SampleService;
	@Autowired
	IPromptService iPromptService;

	@Override
	public ResponseData addNew(Sample dto, IRequest requestCtx, HttpServletRequest request) {
		Sample head = new Sample();
		Sample receive = new Sample();
		head.setSampleNumber(dto.getSampleNumber());
		receive = sampleMapper.selectOne(head);
		if (receive == null) {
			sampleMapper.insertSelective(dto);
		} else {
			ResponseData responseData = new ResponseData();
			responseData.setSuccess(false);
			responseData.setMessage("该样品编号已存在，请重新输入");
			return responseData;
		}
		return new ResponseData();
	}

	@Override
	public List<Sample> myselect(IRequest requestContext, Sample dto, int page, int pageSize) {
		PageHelper.startPage(page, pageSize);
		return sampleMapper.myselect(dto);
	}

	@Override
	public List<Sample> selectforuse(IRequest requestContext, Sample dto, int page, int pageSize) {
		PageHelper.startPage(page, pageSize);
		return sampleMapper.selectforuse(dto);
	}

	@Override
	public List<Sample> confirm(IRequest requestContext, List<Float> headList) {
		// List<PoHeaders> headList1 = new ArrayList();
		for (Float id : headList) {
			Sample head = new Sample();
			head.setSampleId(id);
			head = sampleMapper.selectByPrimaryKey(head);
			head.setSamplePositionStatus("I");
			head.setSampleUser(null);
			head.setSampleUseDate(null);
			head.setSampleUseDepartment("");
			head.setAssociatedDocument("");
			// headList1.add(head);
			self().updateByPrimaryKey(requestContext, head);
		}
		return new ArrayList();
	}

	@Override
	public List<Sample> deal(IRequest requestContext, List<String> headList) {
		// List<PoHeaders> headList1 = new ArrayList();
		Float id = Float.valueOf(headList.get(0));
		String method = headList.get(1);

		Sample head = new Sample();
		head.setSampleId(id);
		head = sampleMapper.selectByPrimaryKey(id);
		String status = head.getSampleDisposalMethod();

		/*
		 * if(status=="2") { if(method=="1") { head.setSampleStatus("1");
		 * head.setEnableFlag("Y"); }
		 * 
		 * } else {
		 * 
		 * }
		 */
		if (method.equals("2")) {
			head.setEnableFlag("N");
			head.setSampleStatus("3");
		}
		/*
		 * else { head.setEnableFlag("Y"); head.setSampleStatus("1"); }
		 */
		head.setSampleDisposalMethod(method);
		head.setSampleDisposalDate(new Date());
		sampleMapper.updateByPrimaryKey(head);

		return new ArrayList();
	}

	@Override
	public List<Sample> useDeal(IRequest requestContext, List<Sample> headList) {
		// List<PoHeaders> headList1 = new ArrayList();

		for (int i = 0; i < headList.size(); i++) {

			if (headList.get(i).getSampleNumber() == null) {
			} else {
				String str = headList.get(i).getSampleNumber();
				String[] strs = str.split(",");
				String SampleNumber = null;
				for (int j = 0; j < headList.get(i).getNum(); j++) {
					SampleNumber = strs[j].toString();
					Sample head = new Sample();
					head.setSampleNumber(SampleNumber);
					head = sampleMapper.selectOne(head);
					head.setAssociatedDocument(headList.get(i).getAssociatedDocument());
					head.setSampleUser(headList.get(i).getSampleUser());
					head.setSampleUseDepartment(headList.get(i).getSampleUseDepartment());
					head.setSampleUseDate(new Date());
					head.setSamplePositionStatus("O");
					sampleMapper.updateByPrimaryKeySelective(head);
				}
			}
		}

		return new ArrayList();
	}
}