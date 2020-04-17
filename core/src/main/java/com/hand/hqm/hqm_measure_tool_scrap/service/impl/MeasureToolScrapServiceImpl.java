package com.hand.hqm.hqm_measure_tool_scrap.service.impl;

import com.github.pagehelper.PageHelper;
import com.hand.hap.activiti.dto.ProcessInstanceResponseExt;
import com.hand.hap.activiti.dto.TaskActionRequestExt;
import com.hand.hap.activiti.exception.TaskActionException;
import com.hand.hap.activiti.service.IActivitiService;
import com.hand.hap.code.rule.exception.CodeRuleException;
import com.hand.hap.code.rule.service.ISysCodeRuleProcessService;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.system.service.impl.BaseServiceImpl;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.ExecutionListener;
import org.activiti.engine.delegate.TaskListener;
import org.activiti.engine.impl.persistence.entity.VariableInstance;
import org.activiti.rest.service.api.engine.variable.RestVariable;
import org.activiti.rest.service.api.runtime.process.ProcessInstanceCreateRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hand.hqm.hqm_measure_tool.dto.MeasureTool;
import com.hand.hqm.hqm_measure_tool.mapper.MeasureToolMapper;
import com.hand.hqm.hqm_measure_tool_scrap.dto.MeasureToolScrap;
import com.hand.hqm.hqm_measure_tool_scrap.mapper.MeasureToolScrapMapper;
import com.hand.hqm.hqm_measure_tool_scrap.service.IMeasureToolScrapService;
import com.hand.wfl.util.WflTask;
import com.hand.wfl.util.WflVariable;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.ContextLoader;

@Service
@Transactional(rollbackFor = Exception.class)
public class MeasureToolScrapServiceImpl extends BaseServiceImpl<MeasureToolScrap> implements IMeasureToolScrapService
		, TaskListener, ExecutionListener{
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private IActivitiService activitiService;
	
	@Autowired 
	private ISysCodeRuleProcessService codeRuleProcessService;
	
	@Autowired
	private MeasureToolScrapMapper measureToolScrapMapper;
	
	@Autowired
	private MeasureToolMapper measureToolMapper;
	
	@Override
	public void notify(DelegateTask delegateTask) {
		if (measureToolScrapMapper == null) {
			measureToolScrapMapper = ContextLoader.getCurrentWebApplicationContext()
					.getBean(MeasureToolScrapMapper.class);
		}
		if (measureToolMapper == null) {
			measureToolMapper = ContextLoader.getCurrentWebApplicationContext()
					.getBean(MeasureToolMapper.class);
		}
		try {
			String processInstanceId = delegateTask.getProcessInstanceId();
			MeasureToolScrap measureToolScrap = new MeasureToolScrap();
			measureToolScrap.setProcessInstanceId(processInstanceId);
			List<MeasureToolScrap> list = measureToolScrapMapper.queryByCondition(measureToolScrap);
			if (!list.isEmpty() && list.size() == 1) {
				measureToolScrap = list.get(0);
				measureToolScrap.setProcessStatus("已完成");
				measureToolScrap.setStatus("已完成");
				measureToolScrap.setProcessEndTime(new Date());
				measureToolScrapMapper.updateByPrimaryKeySelective(measureToolScrap);
				MeasureTool measureTool = new MeasureTool();
				measureTool.setMeasureToolId(measureToolScrap.getMeasureToolId());
				//更新此量具在台账表中的状态为“报废”
				measureTool.setMeasureToolStatus("3");
				measureToolMapper.updateByPrimaryKeySelective(measureTool);
			}
		} catch (Exception e) {
			logger.error(">>>更新业务数据失败:" + e.getMessage());
		}
		
	}
	
	@Override
	public void notify(DelegateExecution execution) {
		if (measureToolScrapMapper == null) {
			measureToolScrapMapper = ContextLoader.getCurrentWebApplicationContext()
					.getBean(MeasureToolScrapMapper.class);
		}
		try {
			String currentActivityId = execution.getCurrentActivityId();
			String eventName = execution.getEventName();
			String processInstanceId = execution.getProcessInstanceId();
			List<WflVariable> variableList = measureToolScrapMapper.queryVariable(processInstanceId);
			//一级审批，直属经理审批
			if ("sid-vDloP9ZC-CvNE-4g9D-80sP-G2s9LAsZDDZi".equals(currentActivityId)) {
				
			}
			VariableInstance approveResult = execution.getVariableInstance("approveResult");
			Object aaa = execution.getVariable("approveResult");
			Map<String, VariableInstance> map = execution.getVariableInstances();
			
			Object bbb = execution.getParent().getVariable("approveResult");
			Map<String, VariableInstance> map2 = execution.getParent().getVariableInstances();
			VariableInstance parentApproveResult = execution.getParent().getVariableInstance("approveResult");
		} catch (Exception e) {
			logger.error(">>>更新业务数据失败:" + e.getMessage());
		}
		
	}
	
	@Override
	public List<MeasureToolScrap> queryByCondition(IRequest requestContext, MeasureToolScrap dto, int page,
			int pageSize) {
		PageHelper.startPage(page, pageSize);
		return measureToolScrapMapper.queryByCondition(dto);
	}

	
	@Override
	public List<MeasureToolScrap> add(IRequest requestContext, MeasureToolScrap dto) {
		if ("add".equals(dto.get__status())) {
			try {
				dto.setScrapNumber(codeRuleProcessService.getRuleCode("MEASURE_TOOL_SCRAP_NUMBER"));
			} catch (CodeRuleException e) {
				logger.error(">>>获取编码失败:" + e.getMessage());
			}
			self().insertSelective(requestContext, dto);
		} else if ("update".equals(dto.get__status())) {
			self().updateByPrimaryKeySelective(requestContext, dto);
		}
		return Arrays.asList(dto);
	}


	@Override
	public void createProcessInstance(IRequest requestCtx, MeasureToolScrap dto) {
		//创建流程
		ProcessInstanceCreateRequest instanceCreateRequest = new ProcessInstanceCreateRequest();
		instanceCreateRequest.setBusinessKey(dto.getScrapNumber());
		instanceCreateRequest.setProcessDefinitionKey("HQM_MEASURE_TOOL_SCRAP_WFL");
		ProcessInstanceResponseExt responseExt = (ProcessInstanceResponseExt) activitiService.startProcess(requestCtx, instanceCreateRequest);

		dto.setStatus("直属经理审批");
		dto.setApplicationDate(new Date());
		dto.setProcessStatus("AP");
		dto.setBusinessKey(dto.getScrapNumber());
		dto.setProcessStartTime(responseExt.getStartTime());
		dto.setProcessInstanceId(responseExt.getId());
		if ("add".equals(dto.get__status())) {
			try {
				dto.setScrapNumber(codeRuleProcessService.getRuleCode("MEASURE_TOOL_SCRAP_NUMBER"));
			} catch (CodeRuleException e) {
				logger.error(">>>获取编码失败:" + e.getMessage());
			}
			self().insertSelective(requestCtx, dto);
		} else if ("update".equals(dto.get__status())) {
			self().updateByPrimaryKeySelective(requestCtx, dto);
		}
		
	}

	
	@Override
	public ResponseData approveProcess(IRequest requestCtx, MeasureToolScrap dto,
			TaskActionRequestExt actionRequest, String processInstanceId, Integer flowNum) throws TaskActionException {
		String taskId = actionRequest.getCurrentTaskId();
		if (flowNum == 10 || flowNum == 20) {
			//审批
			if (dto != null) {
				List<RestVariable> restVariablesList = actionRequest.getVariables();
				for(int i = 0; i < restVariablesList.size(); i++) {
					if ("approveResult".equals(restVariablesList.get(i).getName()) && 
							"APPROVED".equals(restVariablesList.get(i).getValue())){
						//状态设置
						if (flowNum == 10) {
							dto.setStatus("质量部经理审批");
						} else if (flowNum == 20) {
							dto.setStatus("审批通过");
							dto.setProcessEndTime(new Date());
							MeasureTool measureTool = new MeasureTool();
							measureTool.setMeasureToolId(dto.getMeasureToolId());
							//更新此量具在台账表中的状态为“报废”
							measureTool.setMeasureToolStatus("3");
							measureToolMapper.updateByPrimaryKeySelective(measureTool);
						}
					} else if ("approveResult".equals(restVariablesList.get(i).getName()) && 
							"REJECTED".equals(restVariablesList.get(i).getValue())) {
						//状态设置
						dto.setStatus("单据修改");
					}
				}
				self().updateByPrimaryKey(requestCtx, dto);
			}
		} else if (flowNum == 30) {
			//单据修改
			if (dto != null) {
				List<RestVariable> restVariablesList = actionRequest.getVariables();
				for(int i = 0; i < restVariablesList.size(); i++) {
					if ("approveResult".equals(restVariablesList.get(i).getName()) && 
							"APPROVED".equals(restVariablesList.get(i).getValue())){
						//状态设置为审批完成
						dto.setStatus("直属经理审批");
					} else if ("approveResult".equals(restVariablesList.get(i).getName()) && 
							"REJECTED".equals(restVariablesList.get(i).getValue())) {
						//状态设置为审批拒绝
						dto.setStatus("审批拒绝");
						dto.setProcessEndTime(new Date());
					}
				}
				self().updateByPrimaryKey(requestCtx, dto);
			}
		}
		activitiService.executeTaskAction(requestCtx, taskId, actionRequest, false);
		return new ResponseData();
	}

	
	@Override
	public List<WflTask> queryTask(IRequest requestContext, String processInstanceId) {
		return measureToolScrapMapper.queryTask(processInstanceId);
	}

	public List<WflVariable> queryVariable(String processInstanceId) {
		return measureToolScrapMapper.queryVariable(processInstanceId);
	}
	

}