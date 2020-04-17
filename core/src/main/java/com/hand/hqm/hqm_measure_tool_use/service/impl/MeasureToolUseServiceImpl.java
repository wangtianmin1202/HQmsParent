package com.hand.hqm.hqm_measure_tool_use.service.impl;

import com.github.pagehelper.PageHelper;
import com.hand.hap.activiti.dto.ProcessInstanceResponseExt;
import com.hand.hap.activiti.dto.TaskActionRequestExt;
import com.hand.hap.activiti.exception.TaskActionException;
import com.hand.hap.activiti.service.IActivitiService;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import com.hand.hap.task.service.ITaskExecutionService;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.activiti.engine.DynamicBpmnService;
import org.activiti.engine.FormService;
import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.ManagementService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.ExecutionListener;
import org.activiti.engine.delegate.TaskListener;
import org.activiti.rest.service.api.engine.variable.RestVariable;
import org.activiti.rest.service.api.runtime.process.ProcessInstanceCreateRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.context.ContextLoader;

import com.hand.hqm.hqm_measure_tool.dto.MeasureTool;
import com.hand.hqm.hqm_measure_tool.mapper.MeasureToolMapper;
import com.hand.hqm.hqm_measure_tool.service.IMeasureToolService;
import com.hand.hqm.hqm_measure_tool_use.dto.MeasureToolUse;
import com.hand.hqm.hqm_measure_tool_use.mapper.MeasureToolUseMapper;
import com.hand.hqm.hqm_measure_tool_use.service.IMeasureToolUseService;
import com.mchange.v2.async.StrandedTaskReporting;

import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class MeasureToolUseServiceImpl extends BaseServiceImpl<MeasureToolUse> implements IMeasureToolUseService, 
		ExecutionListener, TaskListener{
	

	@Autowired
	private IActivitiService activitiService;
	
	@Autowired
	private IMeasureToolService measureToolService;
	
	@Autowired
	private MeasureToolUseMapper measureToolUseMapper;
	
	@Autowired
	private MeasureToolMapper measureToolMapper;
	
	
	@Override
	public void notify(DelegateTask delegateTask) {
		if (measureToolUseMapper == null) {
			measureToolUseMapper = ContextLoader.getCurrentWebApplicationContext()
					.getBean(MeasureToolUseMapper.class);
		}
		if (measureToolMapper == null) {
			measureToolMapper = ContextLoader.getCurrentWebApplicationContext()
					.getBean(MeasureToolMapper.class);
		}
		try {
			/*ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
			TaskService taskService = processEngine.getTaskService();
			taskService.getVariable(delegateTask., variableName);*/
			String processInstanceId = delegateTask.getProcessInstanceId();
			MeasureToolUse measureToolUse = new MeasureToolUse();
			measureToolUse.setProcessInstanceId(processInstanceId);
			List<MeasureToolUse> list = measureToolUseMapper.queryByProcessInstId(measureToolUse);
			if (!list.isEmpty() && list.size() == 1) {
				measureToolUse = list.get(0);
				measureToolUse.setProcessStatus("已完成");
				measureToolUse.setStatus("已完成");
				measureToolUse.setProcessEndTime(new Date());
				measureToolUseMapper.updateByPrimaryKeySelective(measureToolUse);
				MeasureTool measureTool = new MeasureTool();
				measureTool.setMeasureToolId(measureToolUse.getMeasureToolId());
				//measureTool = measureToolmapper.selectByPrimaryKey(measureTool);
				//更新量具状态为“领用”,如果领用单已选择“外借供应商”，在库状态更新为“外借”，反之为“出库”
				measureTool.setMeasureToolStatus("5");
				if(list.get(0).getBorrowedSupplier() != null) {
					measureTool.setMeasureToolPositionStatus("2");
				} else {
					measureTool.setMeasureToolPositionStatus("O");
				}
				measureToolMapper.updateByPrimaryKeySelective(measureTool);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	
	@Override
	public void notify(DelegateExecution execution) {
		
		
	}
	
	@Override
	public List<MeasureToolUse> queryByCondition(IRequest requestContext, MeasureToolUse dto, int page, int pageSize) {
		PageHelper.startPage(page, pageSize);
		return measureToolUseMapper.queryByCondition(dto);
	}

	
	@Override
	public List<MeasureToolUse> add(IRequest requestCtx, MeasureToolUse dto) {
		if ("add".equals(dto.get__status())) {
			self().insertSelective(requestCtx, dto);
		} else if ("update".equals(dto.get__status())) {
			self().updateByPrimaryKeySelective(requestCtx, dto);
		}
		return Arrays.asList(dto);
	}

	
	@Override
	public List<MeasureToolUse> confirmReturn(IRequest requestContext, MeasureToolUse dto) {
		//设置归还日期，将量具状态改为“正常”，在库状态改为“在库”
		dto.setReturnDate(new Date());
		self().updateByPrimaryKeySelective(requestContext, dto);
		dto = self().selectByPrimaryKey(requestContext, dto);
		MeasureTool measureTool = new MeasureTool();
		measureTool.setMeasureToolId(dto.getMeasureToolId());
		measureTool.setMeasureToolStatus("1");
		measureTool.setMeasureToolPositionStatus("I");
		measureToolService.self().updateByPrimaryKeySelective(requestContext, measureTool);
		return Arrays.asList(dto);
	}


	@Override
	public void createProcessInstance(IRequest requestCtx, MeasureToolUse dto) {
		//创建流程
		ProcessInstanceCreateRequest instanceCreateRequest = new ProcessInstanceCreateRequest();
		instanceCreateRequest.setBusinessKey(dto.getUseNumber());
		instanceCreateRequest.setProcessDefinitionKey("HQM_MEASURE_TOOL_USE_WFL");
		ProcessInstanceResponseExt responseExt = (ProcessInstanceResponseExt) activitiService.startProcess(requestCtx, instanceCreateRequest);

		dto.setStatus("直属经理审批");
		dto.setApplicationDate(new Date());
		dto.setProcessStatus("AP");
		dto.setBusinessKey(dto.getUseNumber());
		dto.setProcessStartTime(responseExt.getStartTime());
		dto.setProcessInstanceId(responseExt.getId());
		if ("add".equals(dto.get__status())) {
			self().insertSelective(requestCtx, dto);
		} else if ("update".equals(dto.get__status())) {
			self().updateByPrimaryKeySelective(requestCtx, dto);
		}
		
	}


	@Override
	public ResponseData approveProcess(IRequest requestCtx, MeasureToolUse dto,
			TaskActionRequestExt actionRequest, String processInstanceId, Integer flowNum) throws TaskActionException{
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
							//更新量具状态为“领用”,如果领用单已选择“外借供应商”，在库状态更新为“外借”，反之为“出库”
							measureTool.setMeasureToolStatus("5");
							if(dto.getBorrowedSupplier() != null) {
								measureTool.setMeasureToolPositionStatus("2");
							} else {
								measureTool.setMeasureToolPositionStatus("O");
							}
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
			//二级审批
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


	
	

}