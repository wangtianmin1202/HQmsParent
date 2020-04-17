package com.hand.npi.npi_technology.service.impl;

import com.hand.hap.activiti.dto.ProcessInstanceResponseExt;
import com.hand.hap.activiti.service.IActivitiService;
import com.hand.hap.code.rule.exception.CodeRuleException;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import com.hand.hqm.file_type.dto.FileType;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.activiti.rest.service.api.runtime.process.ProcessInstanceCreateRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hand.npi.npi_technology.dto.InvalidCause;
import com.hand.npi.npi_technology.dto.InvalidPattern;
import com.hand.npi.npi_technology.dto.PreventionMeasure;
import com.hand.npi.npi_technology.mapper.InvalidCauseMapper;
import com.hand.npi.npi_technology.service.IInvalidCauseService;
import com.hand.npi.npi_technology.service.IPreventionMeasureService;

import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class InvalidCauseServiceImpl extends BaseServiceImpl<InvalidCause> implements IInvalidCauseService {

	@Autowired
	InvalidCauseMapper invalidCauseMapper;

	@Autowired
	IPreventionMeasureService iPreventionMeasureService;
	@Autowired
	private IActivitiService activitiService;

	@Override
	public ResponseData addNewInvalidCause(InvalidCause dto, IRequest requestCtx, HttpServletRequest request) {
		if ("add".equals(dto.get__status())) {
			Date date = new Date();
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMM");
			String time = dateFormat.format(date);
			String invalidCauseNumber = "FC" + time;
			invalidCauseNumber = getInvalidCauseNumber(invalidCauseNumber);
			dto.setInvalidCauseNumber(invalidCauseNumber);

			insertSelective(requestCtx, dto);
		} else if ("update".equals(dto.get__status())) {
			updateByPrimaryKey(requestCtx, dto);
		}
		return new ResponseData(true);
	}

	@Override
	public String getInvalidCauseNumber(String invalidCauseNumber) {
		InvalidCause invalidCause = new InvalidCause();
		invalidCause.setInvalidCauseNumber(invalidCauseNumber);
		List<InvalidCause> invalidCauseList = invalidCauseMapper.selectMaxNumber(invalidCause);
		if (invalidCauseList.isEmpty()) {
			invalidCauseNumber = invalidCauseNumber + "0001";
		} else {
			int intNumber = Integer.parseInt(invalidCauseList.get(0).getInvalidCauseNumber().substring(8));
			intNumber++;
			String stringNumber = String.valueOf(intNumber);
			for (int i = 0; i < 3; i++) {
				stringNumber = stringNumber.length() < 4 ? "0" + stringNumber : stringNumber;
			}
			invalidCauseNumber = invalidCauseNumber + stringNumber;
		}

		return invalidCauseNumber;
	}

	@Override
	public void deleteInvalidCause(InvalidCause dto) throws Exception {
		List<InvalidCause> invalidCauseList = invalidCauseMapper.selectInvalidCauseChild(dto);
		for (int i = 0; i < invalidCauseList.size(); i++) {
			if (invalidCauseList.get(i).getPreventionMeasureId() != null) {
				PreventionMeasure preventionMeasure = new PreventionMeasure();
				preventionMeasure.setPreventionId(invalidCauseList.get(i).getPreventionMeasureId());
				iPreventionMeasureService.deleteByPrimaryKey(preventionMeasure);
			}
		}
		self().deleteByPrimaryKey(dto);

	}

	@Override
	public List<InvalidCause> queryInvalidCauseList(InvalidCause dto) {
		return invalidCauseMapper.queryInvalidCauseList(dto);
	}

	@Override
	public void createProcessInstance(IRequest requestCtx, InvalidCause dto) {
		//dto.setStatus("一级审批");
		//dto.setApplicationTime(new Date());
		//dto.setProcessStatus("AP");
		//dto.setBusinessKey(dto.getProcessNumber());
		if ("add".equals(dto.get__status())) {
			self().insertSelective(requestCtx, dto);
		} else if ("update".equals(dto.get__status())) {
			self().updateByPrimaryKeySelective(requestCtx, dto);
		}

		// 创建流程
		ProcessInstanceCreateRequest instanceCreateRequest = new ProcessInstanceCreateRequest();
		instanceCreateRequest.setBusinessKey(dto.getInvalidCauseId().toString());
		instanceCreateRequest.setProcessDefinitionKey("zcbtestone");
		ProcessInstanceResponseExt responseExt = (ProcessInstanceResponseExt) activitiService.startProcess(requestCtx,
				instanceCreateRequest);

		// 将流程id存入业务数据
		//dto.setProcessStartTime(responseExt.getStartTime());
		//dto.setProcessInstanceId(responseExt.getId());
		//self().updateByPrimaryKeySelective(requestCtx, dto);
	}

}