package com.hand.spc.ecr_main.service.impl;

import com.hand.hap.activiti.service.IActivitiService;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.service.impl.BaseServiceImpl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.activiti.rest.service.api.engine.variable.RestVariable;
import org.activiti.rest.service.api.runtime.process.ProcessInstanceCreateRequest;
import org.activiti.rest.service.api.runtime.process.ProcessInstanceResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hand.spc.ecr_main.dto.EcrItemResult;
import com.hand.spc.ecr_main.dto.EcrMain;
import com.hand.spc.ecr_main.dto.EcrProcess;
import com.hand.spc.ecr_main.mapper.EcrItemResultMapper;
import com.hand.spc.ecr_main.mapper.EcrMainMapper;
import com.hand.spc.ecr_main.mapper.EcrProcessMapper;
import com.hand.spc.ecr_main.service.IEcrProcessService;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class EcrProcessServiceImpl extends BaseServiceImpl<EcrProcess> implements IEcrProcessService{
	
	@Autowired
	private IActivitiService activitiService;
	
	@Autowired
	private EcrProcessMapper mapper;
	
	@Autowired
	private EcrMainMapper mainMapper;
	
	@Autowired
	EcrItemResultMapper resultMapper;
	
	private final Logger logger = LoggerFactory.getLogger(EcrProcessServiceImpl.class); 
	
	/**
	 * 物料管控物料处理方案流程名称
	 */
	private static final String WULIAOGUANLI = "wuliaoguanlibiao";
	/**
	 * 物料管控物料处理方案流程名称
	 */
	private static final String MATERIAL = "MatScrapSolutionApprOfECR";
	/**
	 * 物料管控物料处理方案流程变量
	 */
	private static final String MATERIALV = "matScrap";
	/**
	 * ECR改善方案之审批流程名称
	 */
	private static final String SOLUTION = "SolutionApprovalOfECR2";

	/**
	 * ECR改善方案之审批流程变量
	 */
	private static final String SOLUTIONV = "solution";
	
	
	/**
	 * 发布任务工作流流程名称
	 */
	private static final String TASK = "tasksPublishOfECR";
	
	@Override
	public void startMatScrap(IRequest request, EcrMain dto) {
		logger.debug("-------------------1. 物料管控流程启动 start----------------------");
		logger.debug("ecrno: "+dto.getEcrno());
		startProcess(request, dto, MATERIAL, MATERIALV);
		logger.debug("-------------------1. 物料管控流程启动 end----------------------");
	}

	@Override
	public void startSolution(IRequest request, EcrMain dto) {
		logger.debug("-------------------2.ECR改善方案流程启动 start----------------------");
		logger.debug("ecrno: "+dto.getEcrno());
		startProcess(request, dto, SOLUTION, SOLUTIONV);
		logger.debug("--------------------2.ECR改善方案流程启动  end---------------------");
	}

	@Override
	public void updateSolution(IRequest request, String ecrno, String approveResult, String flowno, Double skuCost) {
		logger.debug("-------------------2.ECR改善方案流程--更新流程状态--start ----------------------");
		logger.debug("ecrno:" + ecrno);
		logger.debug("approveResult:" + approveResult);
		logger.debug("flowno:" + flowno);
		String status = "A";
		boolean end = false;
		switch(flowno) {
			case "10":
			case "20":
			case "30":
			case "40":
			case "50":
			case "60":
				if("APPROVED".equals(approveResult)) {
					status = "A";
				}else if("REJECTED".equals(approveResult)) {
					status = "B";
				}
				break;
			case "70":
			case "80":
				if("APPROVED".equals(approveResult)) {
					if(skuCost == null || skuCost == 0) {
						status = "D";
						end = true;
					}else {
						status = "A";
					}
				}else if("REJECTED".equals(approveResult)) {
					status = "B";
				}
				break;
			case "90":
			case "100":
				if("APPROVED".equals(approveResult)) {
					status = "D";
					end = true;
				}else if("REJECTED".equals(approveResult)) {
					status = "B";
				}
				break;
			case "110":
				if("APPROVED".equals(approveResult)) {
					status = "A";
				}else if("REJECTED".equals(approveResult)) {
					status = "C";
					end = true;
				}
				break;
			default:
				break;
		}
		logger.debug("status: " + status);
		
		// 更新流程状态
		EcrProcess dto = new EcrProcess();
		dto.setEcrno(ecrno);
		EcrProcess process = new EcrProcess();
		try {
			process = mapper.selectOne(dto);
			if(process == null) {
				throw new Exception("流程表不存在字段 ecrno");
			}
		} catch (Exception e) {
			logger.debug("ecrno 不唯一或不存在");
		}
		process.setProcessStatus(status);
		if (end) {
			process.setProcessEndDate(new Date());
		}
		if (process.getKid() != null) {
			mapper.updateByPrimaryKey(process);
		} else {
			logger.debug("kid is null");
		}
		logger.debug("-------------------2.ECR改善方案流程--更新流程状态- end ----------------------");
	}

	/**
	 * 启动流程
	 * @param request
	 * @param dto
	 */
	public void startProcess(IRequest request, EcrMain dto, String processKey, String var) {
		ProcessInstanceCreateRequest create = new ProcessInstanceCreateRequest();
		create.setBusinessKey(String.valueOf(dto.getKid()));   // id
		request.setEmployeeCode(dto.getProcessEmployeeCode()); // 申请人
		create.setProcessDefinitionKey(processKey);
		
		List<RestVariable> variables  = new ArrayList<>();
		RestVariable va = new RestVariable();
		va.setName(var + "Email");
		va.setType("string");
		va.setValue("");
		variables.add(va);
		RestVariable va2 = new RestVariable();
		va2.setName(var + "Ecrno");
		va2.setType("string");
		va2.setValue(dto.getEcrno());
		variables.add(va2);
		create.setVariables(variables);
		
		// 启动流程
		ProcessInstanceResponse response = activitiService.startProcess(request, create);
		
		// 设置状态：审批中，流程 ID，流程启动时间
		EcrProcess process = new EcrProcess();
		process.setEcrId(dto.getKid());
		process.setProcessCode(processKey);
		process.setProcessId(Long.valueOf(response.getId()));
		process.setProcessEmployeeCode(dto.getProcessEmployeeCode());
		process.setProcessStatus("A");
		process.setProcessStartDate(new Date());
		process.setEcrno(dto.getEcrno());
		mapper.insertSelective(process);
		logger.debug(process.toString());
	}

	
	@Override
	public void matScrapCommit(IRequest request, EcrItemResult dto, String ecrno) {
		logger.debug("------------1.物料管控物料处理方案 - 更新采用方案  start-------------");
		logger.debug("ecrno:" + ecrno);		
		if("".equals(dto.getEcrno()) || dto.getEcrno() == null){
			logger.debug("ecrno is null");
			return;
		}
		// 1.该组数据 审批结果已采用修改为未采用
		EcrItemResult rs = new EcrItemResult();
		rs.setEcrno(dto.getEcrno());
		rs.setStatust("B");
		resultMapper.updateState(rs);
		// 2. 本条数据审批结果修改为已采用
		dto.setStatust("A");
		resultMapper.updateState(dto);
		// 3. 修改流程状态
		matScrapStatus(request, ecrno, "APPROVED", "90");
		logger.debug("------------1.物料管控物料处理方案 - 更新采用方案  end-------------");
		
	}
	

	@Override
	public void matScrapStatus(IRequest requestCtx, String ecrno, String apply, String flowno) {
		logger.debug("------------1.物料管控物料处理方案-更新流程状态 start-------------");
		logger.debug("ecrno:" + ecrno);
		logger.debug("apply:" + apply);
		logger.debug("flowno:" + flowno);
		
		String status = "A";
		boolean end = false;
		switch(flowno) {
			case "10":
			case "20":
			case "30":
			case "40":
			case "50":
			case "60":
				if("APPROVED".equals(apply)) {
					status = "A";
				}else if("REJECTED".equals(apply)) {
					status = "B";
				}
				break;
			case "70":
			case "80":
				if("APPROVED".equals(apply)) {
					status = "D";
					end = true;
				}else if("REJECTED".equals(apply)) {
					status = "B";
				}
				break;
			case "90":
				if("APPROVED".equals(apply)) {
					status = "A";
				}else if("REJECTED".equals(apply)) {
					status = "C";
					end = true;
				}
				break;
			default:
				break;
		}
		
		logger.debug("status" + status);
		
		// 更新流程状态
		EcrProcess dto = new EcrProcess();
		dto.setEcrno(ecrno);
		EcrProcess process = new EcrProcess();
		try {
			process = mapper.selectOne(dto);
			if(process == null) {
				throw new Exception("流程表不存在字段 ecrno");
			}
		} catch (Exception e) {
			logger.debug("ecrno 不唯一或不存在");
		}
		process.setProcessStatus(status);
		if (end) {
			process.setProcessEndDate(new Date());
		}
		if (process.getKid() != null) {
			mapper.updateByPrimaryKey(process);
		} else {
			logger.debug("kid is null");
		}
		// 更新流程状态(main)
		/*EcrMain dto = new EcrMain();
		dto.setKid(Long.valueOf(kid));
		dto = mainMapper.selectByPrimaryKey(dto);
		dto.setProcessStatus(status);
		if (end) {
			dto.setProcessEndDate(new Date());
		}
		if(dto.getKid() != null) {
			mainMapper.updateByPrimaryKey(dto);
		}*/
		logger.debug("------------1.物料管控物料处理方案-更新流程状态 end-------------");	
	}

	@Override
	public void startTask(IRequest request, String processCode, String processEmployeeCode, Long id,
			String ecrno) {
		// 查询流程表 hpm_ecr_process 查询到数据则不启动流程(参数：processCode,ecrno)
		// FIXME 添加查询条件：非已完成状态的数据  (!D)
		EcrProcess ep = new EcrProcess();
		ep.setProcessCode(processCode);
		ep.setEcrno(ecrno);
		List<EcrProcess> processes = mapper.select(ep);
		if(!processes.isEmpty() && processes.get(0) != null) {
			processes.forEach(pro->{
				if(!"D".equalsIgnoreCase(pro.getProcessStatus())) {
					throw new RuntimeException("ecrno 编码的流程已存在，请不要重复发起流程！");
				}
			});
		}
		
		// 发起流程
		String processKey = TASK;
		ProcessInstanceCreateRequest create = new ProcessInstanceCreateRequest();
		create.setBusinessKey(String.valueOf(id));   // id
		request.setEmployeeCode(processEmployeeCode); // 申请人
		create.setProcessDefinitionKey(processKey);
		
		List<RestVariable> variables  = new ArrayList<>();
		RestVariable va = new RestVariable();
		va.setName("ecrno");
		va.setType("string");
		va.setValue(ecrno);
		variables.add(va);
		RestVariable va2 = new RestVariable();
		va2.setName("processCode");
		va2.setType("string");
		va2.setValue(processCode);
		variables.add(va2);
		RestVariable va3 = new RestVariable();
		va3.setName("dutyby");
		va3.setType("string");
		va3.setValue(processEmployeeCode);
		variables.add(va3);
		create.setVariables(variables);
		
		// 启动流程
		ProcessInstanceResponse response = activitiService.startProcess(request, create);
		
		// 设置状态：审批中，流程 ID，流程启动时间
		EcrProcess process = new EcrProcess();
		process.setEcrId(id);
		process.setProcessCode(processCode);
		process.setProcessId(Long.valueOf(response.getId()));
		process.setProcessEmployeeCode(processEmployeeCode);
		process.setProcessStatus("A");
		process.setProcessStartDate(new Date());
		process.setEcrno(ecrno);
		mapper.insertSelective(process);
		logger.debug(process.toString());
	}
}