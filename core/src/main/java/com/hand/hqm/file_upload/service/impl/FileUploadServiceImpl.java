package com.hand.hqm.file_upload.service.impl;

import com.github.pagehelper.PageHelper;
import com.hand.hap.activiti.dto.ProcessInstanceResponseExt;
import com.hand.hap.activiti.dto.TaskActionRequestExt;
import com.hand.hap.activiti.exception.TaskActionException;
import com.hand.hap.activiti.service.IActivitiService;
import com.hand.hap.code.rule.exception.CodeRuleException;
import com.hand.hap.code.rule.service.ISysCodeRuleProcessService;
import com.hand.hap.core.IRequest;
import com.hand.hap.hr.dto.Employee;
import com.hand.hap.hr.mapper.EmployeeMapper;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import com.hand.hap.util.MailUtil;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.ExecutionListener;
import org.activiti.engine.delegate.TaskListener;
import org.activiti.rest.service.api.engine.variable.RestVariable;
import org.activiti.rest.service.api.runtime.process.ProcessInstanceCreateRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hand.hqm.file_classify.dto.Classify;
import com.hand.hqm.file_classify.mapper.FileClassifyMapper;
import com.hand.hqm.file_classify.service.IFileClassifyService;
import com.hand.hqm.file_email.dto.FileEmail;
import com.hand.hqm.file_email.mapper.FileEmailMapper;
import com.hand.hqm.file_email.service.IFileEmailService;
import com.hand.hqm.file_manager.dto.Manager;
import com.hand.hqm.file_manager.service.IManagerService;
import com.hand.hqm.file_manager_classify.dto.ManagerClassify;
import com.hand.hqm.file_manager_classify.service.IManagerClassifyService;
import com.hand.hqm.file_manager_his.dto.ManagerHis;
import com.hand.hqm.file_manager_his.service.IManagerHisService;
import com.hand.hqm.file_type.dto.FileType;
import com.hand.hqm.file_type.mapper.FileTypeMapper;
import com.hand.hqm.file_upload.dto.FileUpload;
import com.hand.hqm.file_upload.mapper.FileUploadMapper;
import com.hand.hqm.file_upload.service.IFileUploadService;
import com.hand.hqm.hqm_measure_tool_repair.service.IMeasureToolRepairService;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.ContextLoader;

@Service
@Transactional(rollbackFor = Exception.class)
public class FileUploadServiceImpl extends BaseServiceImpl<FileUpload> implements IFileUploadService,TaskListener{

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private IActivitiService activitiService;
	
	@Autowired
	private IManagerService managerService;
	
	@Autowired
	private IManagerHisService managerHisService;
	
	@Autowired
	private IFileClassifyService fileClassifyService;
	
	@Autowired
	private IManagerClassifyService managerClassifyService;
	
	@Autowired 
	private ISysCodeRuleProcessService codeRuleProcessService;
	
	@Autowired
	private FileUploadMapper fileUploadMapper;
	
	@Autowired
	private FileTypeMapper fileTypeMapper;
	
	@Autowired
	private FileClassifyMapper fileClassifyMapper;
	
	@Autowired
	private EmployeeMapper employeeMapper;
	
	@Autowired
	private FileEmailMapper fileEmailMapper;
	
	@Override
	public void notify(DelegateTask delegateTask) {
		if (fileTypeMapper == null) {
			fileTypeMapper = ContextLoader.getCurrentWebApplicationContext()
					.getBean(FileTypeMapper.class);
		}
		if (fileUploadMapper == null) {
			fileUploadMapper = ContextLoader.getCurrentWebApplicationContext()
					.getBean(FileUploadMapper.class);
		}
		if (employeeMapper == null) {
			employeeMapper = ContextLoader.getCurrentWebApplicationContext()
					.getBean(EmployeeMapper.class);
		}
		try {
			//String processInstanceId = delegateTask.getProcessInstanceId();
			String businessKey = delegateTask.getExecution().getProcessInstanceBusinessKey();
			//查询流程关联业务的文件类型信息
			FileUpload fileUpload = new FileUpload();
			//fileUpload.setProcessInstanceId(processInstanceId);
			fileUpload.setBusinessKey(businessKey);
			List<FileUpload> fileUploadList = fileUploadMapper.queryByCondition(fileUpload);
			if (!fileUploadList.isEmpty() && fileUploadList.size() == 1) {
				fileUpload = fileUploadList.get(0);
				if (fileUpload != null && fileUpload.getFileTypeId() != null) {
					FileType fileType = new FileType();
					fileType.setFileTypeId(fileUpload.getFileTypeId());
					fileType = fileTypeMapper.selectByPrimaryKey(fileType);
					Employee employee = new Employee();
					if ("二级审批".equals(delegateTask.getName())) {
						//设置二级审批人
						employee.setEmployeeId(fileType.getApproverTwo().longValue());
						employee = employeeMapper.selectByPrimaryKey(employee);
						delegateTask.setAssignee(employee.getEmployeeCode());
					} else if ("三级审批".equals(delegateTask.getName())) {
						//设置三级审批人
						employee.setEmployeeId(fileType.getApproverThree().longValue());
						employee = employeeMapper.selectByPrimaryKey(employee);
						delegateTask.setAssignee(employee.getEmployeeCode());
					} else if ("一级审批".equals(delegateTask.getName())) {
						//设置一级审批人
						employee.setEmployeeId(fileType.getApproverOne().longValue());
						employee = employeeMapper.selectByPrimaryKey(employee);
						delegateTask.setAssignee(employee.getEmployeeCode());
					}
				}
			}
		} catch (Exception e) {
			logger.error(">>>设置流程审批人失败:" + e.getMessage());
		}
		
	}
	
	@Override
	public List<FileUpload> queryByCondition(IRequest requestContext, FileUpload dto, int page, int pageSize) {
		PageHelper.startPage(page, pageSize);
		return fileUploadMapper.queryByCondition(dto);
	}

	
	@Override
	public List<FileUpload> add(IRequest requestCtx, FileUpload dto) {
		if ("add".equals(dto.get__status())) {
			try {
				dto.setProcessNumber(codeRuleProcessService.getRuleCode("FILE_UPLOAD_NUMBER"));
			} catch (CodeRuleException e) {
				logger.error(">>>获取编码失败:" + e.getMessage());
			}
			self().insertSelective(requestCtx, dto);
		} else if ("update".equals(dto.get__status())) {
			self().updateByPrimaryKeySelective(requestCtx, dto);
		}
		return Arrays.asList(dto);
	}


	
	@Override
	public void createProcessInstance(IRequest requestCtx, FileUpload dto) {
		if ("add".equals(dto.get__status())) {
			try {
				dto.setProcessNumber(codeRuleProcessService.getRuleCode("FILE_UPLOAD_NUMBER"));
			} catch (CodeRuleException e) {
				logger.error(">>>获取编码失败:" + e.getMessage());
			}
		}
		//判断该单据是否自动审批
		FileType fileType = new FileType();
		fileType.setFileTypeId(dto.getFileTypeId());
		fileType = fileTypeMapper.selectByPrimaryKey(fileType);
		if ("Y".equals(fileType.getIsAutoApproval())) {
			//自动审批，不走流程
			dto.setStatus("审批通过");
			if ("add".equals(dto.get__status())) {
				self().insertSelective(requestCtx, dto);
			} else if ("update".equals(dto.get__status())) {
				self().updateByPrimaryKeySelective(requestCtx, dto);
			}
			//操作业务数据
			saveFile(requestCtx, dto);
		} else {
			dto.setStatus("一级审批");
			dto.setApplicationTime(new Date());
			dto.setProcessStatus("AP");
			dto.setBusinessKey(dto.getProcessNumber());
			if ("add".equals(dto.get__status())) {
				self().insertSelective(requestCtx, dto);
			} else if ("update".equals(dto.get__status())) {
				self().updateByPrimaryKeySelective(requestCtx, dto);
			}
			
			//创建流程
			ProcessInstanceCreateRequest instanceCreateRequest = new ProcessInstanceCreateRequest();
			instanceCreateRequest.setBusinessKey(dto.getProcessNumber());
			instanceCreateRequest.setProcessDefinitionKey("FILE_UPLOAD_WFL");
			ProcessInstanceResponseExt responseExt = (ProcessInstanceResponseExt) activitiService.startProcess(requestCtx, instanceCreateRequest);
			
			//将流程id存入业务数据
			dto.setProcessStartTime(responseExt.getStartTime());
			dto.setProcessInstanceId(responseExt.getId());
			self().updateByPrimaryKeySelective(requestCtx, dto);
		}
	}


	
	@Override
	public ResponseData approveProcess(IRequest requestCtx, FileUpload dto, TaskActionRequestExt actionRequest,
			String processInstanceId, Integer flowNum)  throws TaskActionException{
		String taskId = actionRequest.getCurrentTaskId();
		if (flowNum == 10 || flowNum == 20 || flowNum == 30) {
			//审批
			if (dto != null) {
				List<RestVariable> restVariablesList = actionRequest.getVariables();
				FileType fileType = new FileType();
				fileType.setFileTypeId(dto.getFileTypeId());
				fileType = fileTypeMapper.selectByPrimaryKey(fileType);
				String assigneeTwo = "";
				String assigneeThree = "";
				//判断二、三级审批有无审批人
				if(flowNum == 10) {
					if (fileType.getApproverTwo() == null || fileType.getApproverTwo() == 0) {
						assigneeTwo = "N";
						RestVariable assigneeList = new RestVariable();
						assigneeList.setName("assigneeTwo");
						assigneeList.setValue("N");
						restVariablesList.add(assigneeList);
					} else {
						assigneeTwo = "Y";
						RestVariable assigneeList = new RestVariable();
						assigneeList.setName("assigneeTwo");
						assigneeList.setValue("Y");
						restVariablesList.add(assigneeList);
					}
					if (fileType.getApproverThree() == null || fileType.getApproverThree() == 0) {
						assigneeThree = "N";
						RestVariable assigneeList = new RestVariable();
						assigneeList.setName("assigneeThree");
						assigneeList.setValue("N");
						restVariablesList.add(assigneeList);
					} else {
						assigneeThree = "Y";
						RestVariable assigneeList = new RestVariable();
						assigneeList.setName("assigneeThree");
						assigneeList.setValue("Y");
						restVariablesList.add(assigneeList);
					}
				}
				for(int i = 0; i < restVariablesList.size(); i++) {
					if ("approveResult".equals(restVariablesList.get(i).getName()) && 
							"APPROVED".equals(restVariablesList.get(i).getValue())){
						//状态设置
						if (flowNum == 10) {
							if ("Y".equals(assigneeTwo)) {
								dto.setStatus("二级审批");
							} else if ("N".equals(assigneeTwo) && "Y".equals(assigneeThree)) {
								dto.setStatus("三级审批");
							} else {
								dto.setStatus("审批通过");
								saveFile(requestCtx, dto);
							}
						} else if (flowNum == 20) {
							if ("Y".equals(assigneeThree)) {
								dto.setStatus("三级审批");
							} else {
								dto.setStatus("审批通过");
								saveFile(requestCtx, dto);
							}
						} else {
							dto.setStatus("审批通过");
							saveFile(requestCtx, dto);
						}
					} else if ("approveResult".equals(restVariablesList.get(i).getName()) && 
							"REJECTED".equals(restVariablesList.get(i).getValue())) {
						//状态设置为审批驳回
						dto.setStatus("单据修改");
					}
					self().updateByPrimaryKey(requestCtx, dto);
				}
			}
		} else if (flowNum == 40) {
			//修改数据
			if (dto != null) {
				List<RestVariable> restVariablesList = actionRequest.getVariables();
				for(int i = 0; i < restVariablesList.size(); i++) {
					if ("approveResult".equals(restVariablesList.get(i).getName()) && 
							"APPROVED".equals(restVariablesList.get(i).getValue())){
						//状态设置为审批完成
						dto.setStatus("一级审批");
					} else if ("approveResult".equals(restVariablesList.get(i).getName()) && 
							"REJECTED".equals(restVariablesList.get(i).getValue())) {
						//状态设置为审批驳回
						dto.setStatus("审批拒绝");
					}
				}
				self().updateByPrimaryKey(requestCtx, dto);
			}
		}
		activitiService.executeTaskAction(requestCtx, taskId, actionRequest, false);
		return new ResponseData();
	}

	public void saveFile(IRequest requestCtx, FileUpload dto) {
		FileType fileType = new FileType();
		fileType.setFileTypeId(dto.getFileTypeId());
		fileType = fileTypeMapper.selectByPrimaryKey(fileType);
		dto.setFileTypeName(fileType.getFileTypeName());
		//操作业务数据
		if (dto.getFileIdList() != null && dto.getFileIdList() != "") {
			String[] fileIdStr = dto.getFileIdList().split(",");
			for(int m = 0; m < fileIdStr.length; m++) {
				Float fileId = Float.parseFloat(fileIdStr[m]);
				//1.将file_manager里的信息补充完整
				Manager manager = new Manager();
				manager.setFileId(fileId);
				manager.setFileNum(dto.getProcessNumber());
				manager.setFileTitle(dto.getFileName());
				manager.setEnableFlag("Y");
				managerService.self().updateByPrimaryKeySelective(requestCtx, manager);
				manager = managerService.self().selectByPrimaryKey(requestCtx, manager);
				//2.在三级文件（文件夹）下的研发受控文件（文件夹）下，新增一个文件夹名称为该文件类型
				Classify classifyOne = new Classify();
				classifyOne.setClassifyDescriptions("三级文件");
				List<Classify> lassifyOneList = fileClassifyMapper.queryByCondition(classifyOne);
				//存在三级文件（文件夹）
				if (!lassifyOneList.isEmpty()) {
					Classify classifyTwo = new Classify();
					classifyTwo.setParentClassifyId(lassifyOneList.get(0).getClassifyId());
					classifyTwo.setClassifyDescriptions("研发受控文件");
					List<Classify> lassifyTwoList = fileClassifyMapper.queryByCondition(classifyTwo);
					//存在研发受控文件（文件夹）
					if (!lassifyTwoList.isEmpty()) {
						//查找名称为当前文件类型的文件夹
						Classify classifyThree = new Classify();
						classifyThree.setParentClassifyId(lassifyTwoList.get(0).getClassifyId());
						classifyThree.setClassifyDescriptions(dto.getFileTypeName());
						List<Classify> classifyThreeList = fileClassifyMapper.queryByCondition(classifyThree);
						//存在这个文件夹，获取这个文件夹
						if (!classifyThreeList.isEmpty()) {
							classifyThree = classifyThreeList.get(0);
						} else {
							//不存在这个文件夹，新建一个
							classifyThree.setClassifyCode(dto.getFileTypeName());
							classifyThree.setEnableFlag("Y");
							fileClassifyService.self().insertSelective(requestCtx, classifyThree);
						}
						//3.将上传的文件放到上面的文件夹下
						ManagerClassify managerClassify = new ManagerClassify();
						managerClassify.setFileId(fileId);
						managerClassify.setClassifyId(Long.valueOf(classifyThree.getClassifyId()).floatValue());
						managerClassifyService.insertSelective(requestCtx, managerClassify);
						//4.在file_manager_his表里存一条记录
						ManagerHis managerHis = new ManagerHis();
						managerHis.setFileId(fileId);
						managerHis.setFileNum(dto.getProcessNumber());
						managerHis.setFileTitle(dto.getFileName());
						managerHis.setEdition("A");
						managerHis.setEnableFlag("Y");
						managerHis.setUploadDate(new Date());
						managerHis.setFileUrl(manager.getFileUrl());
						managerHisService.insertSelective(requestCtx, managerHis);
						
					}
				}
			}
			//5.发送邮件
			List<FileEmail> fileEmailList = fileEmailMapper.queryByCondition(new FileEmail());
			String to = "";
			if (fileEmailList != null && !fileEmailList.isEmpty()) {
				for(int i = 0; i < fileEmailList.size(); i ++) {
					if (i == fileEmailList.size() - 1) {
						to += fileEmailList.get(i).getEmployeeEmail();
					} else {
						to = to + fileEmailList.get(i).getEmployeeEmail() + ",";
					}
				}
			}
			MailUtil.sendExcelMail(to, "", "ISO文件管理", "文件已上传");
		}
		
	}
	
}