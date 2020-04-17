package com.hand.spc.ecr_main.service.impl;

import com.github.pagehelper.PageHelper;
import com.hand.hap.activiti.service.IActivitiService;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.CodeValue;
import com.hand.hap.system.service.ICodeService;
import com.hand.hap.system.service.impl.BaseServiceImpl;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hand.spc.ecr_main.dto.EcrDesignReview;
import com.hand.spc.ecr_main.dto.EcrMain;
import com.hand.spc.ecr_main.dto.EcrProcess;
import com.hand.spc.ecr_main.dto.EcrProjectTracking;
import com.hand.spc.ecr_main.dto.EcrQtp;
import com.hand.spc.ecr_main.dto.EcrRfq;
import com.hand.spc.ecr_main.dto.EcrSolutionSku;
import com.hand.spc.ecr_main.dto.EcrSolutionSkuRelation;
import com.hand.spc.ecr_main.dto.EcrVtp;
import com.hand.spc.ecr_main.mapper.EcrDesignReviewMapper;
import com.hand.spc.ecr_main.mapper.EcrMainMapper;
import com.hand.spc.ecr_main.mapper.EcrProcessMapper;
import com.hand.spc.ecr_main.mapper.EcrProjectTrackingMapper;
import com.hand.spc.ecr_main.mapper.EcrQtpMapper;
import com.hand.spc.ecr_main.mapper.EcrRfqMapper;
import com.hand.spc.ecr_main.mapper.EcrSolutionSkuMapper;
import com.hand.spc.ecr_main.mapper.EcrSolutionSkuRelationMapper;
import com.hand.spc.ecr_main.mapper.EcrVtpMapper;
import com.hand.spc.ecr_main.service.IEcrProcessService;
import com.hand.spc.ecr_main.service.IEcrProjectTrackingService;
import com.hand.spc.ecr_main.service.IEcrRfqService;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class EcrRfqServiceImpl extends BaseServiceImpl<EcrRfq> implements IEcrRfqService{

	@Autowired
	EcrRfqMapper rfqMapper;
	
	@Autowired
	EcrProjectTrackingMapper eptMapper;
	
	@Autowired
	EcrProcessMapper processMapper;
	
	@Autowired
	EcrQtpMapper qtpMapper;
	
	@Autowired
	EcrVtpMapper vtpMapper;
	
	@Autowired
	private ICodeService codeService; 
		
	@Autowired
	private IActivitiService activitiService; 
	
	@Autowired
	private IEcrProjectTrackingService eptService; 
	
	@Autowired
	private IEcrProcessService processService; 
	
	@Autowired
	private EcrMainMapper mainMapper;
	

	@Autowired
	private EcrSolutionSkuRelationMapper essrMapper;

	@Autowired
	private EcrSolutionSkuMapper esrMapper;
	
	@Autowired
	private EcrDesignReviewMapper designMapper;
	
	Logger logger = LoggerFactory.getLogger(EcrRfqServiceImpl.class);
	
	
	
	@Override
	public List<EcrRfq> select(IRequest request, EcrRfq dto, int pageNum, int pageSize) {
		logger.debug("------------------RFQ 查询 start --------------------");
		PageHelper.startPage(pageNum, pageSize);
		List<EcrRfq> list = new ArrayList<>();
		logger.debug("ecrno:" + dto.getEcrno() + ", itemId:" + dto.getItemId() + ", rfqId:" + dto.getRfqId());
		list = rfqMapper.selectData(dto);
		logger.debug("------------------RFQ 查询 end --------------------");
		return list;
	}



	@Override
	public List<EcrRfq> initData(IRequest request, String ecrno, List<String> itemIds) {
		logger.debug("------------------RFQ 任务写到任务列表 start --------------------");
		// FIXME　查询要求完成时间 dueDate,参数：ecrno,taskName="RFQ"
		final Date askFinishedDate = eptService.selectDueDate(ecrno, "RFQ");
		
		// 查询改善方案表
		List<EcrRfq> list = rfqMapper.selectSolution(ecrno, itemIds);
		list.forEach(dto ->{
			// 查询 skuCode
		//	List<String> itemCodes = rfqMapper.selectItemCode(dto.getItemId());
			EcrSolutionSkuRelation  essr=new EcrSolutionSkuRelation();
			essr.setItemEcrId(Long.valueOf(dto.getItemId()));
			
			List<EcrSolutionSkuRelation>  essrs=  essrMapper.select(essr);
			if(!essrs.isEmpty()) {
				EcrSolutionSku ess=new EcrSolutionSku();
				ess.setItemSkuKid(essrs.get(0).getItemSkuId());
				List<EcrSolutionSku> essList =esrMapper.select(ess);
				// 查询产品品类
				String category = "";
				//if(itemCodes != null) {
					List<String> categorys = rfqMapper.selectCatogry(essList.get(0).getSkuCode());
					if(!categorys.isEmpty()) {
						category = categorys.get(0);
					}	
			//	}
				// 查询新品老品
				String type = "";
				List<String> types = rfqMapper.selectMainType(ecrno);
				if(!types.isEmpty()) {
					type = types.get(0);
				}
				// 查询责任人 HPM_RFQ_CATEGORY
				String mainDuty = "";
				String codeName = "HPM_RFQ_CATEGORY";
				List<CodeValue> codes = codeService.getCodeValuesByCode(request, codeName);
				for(CodeValue cv : codes){
					if(cv.getValue().equals(category) && cv.getDescription().equals(type)) {
						mainDuty = cv.getMeaning();
						dto.setDutyby(mainDuty);
						break;
					}
				}
				dto.setAskFinishedDate(askFinishedDate);
				// 保存到表
				rfqMapper.updateRfqInvalide(ecrno, dto.getItemId());
				rfqMapper.insertSelective(dto);
			}
		});
		logger.debug("------------------RFQ 任务写到任务列表 end --------------------");
		return list;
	}

	@Override
	public List<EcrRfq> selectTask(IRequest requestContext, EcrRfq dto, int page, int pageSize) {
		logger.debug("------------------流程 查询 start --------------------");
		logger.debug("ecrno:" + dto.getEcrno() + ", Type:" + dto.getTaskType() + ", id:" + dto.getId());
    	List<EcrRfq> list = new ArrayList<>();
    	if(dto.getTaskType() == null) {
    		return list;
    	}
    	// 查询
    	switch(dto.getTaskType()) {
    		case "QTP":
    			PageHelper.startPage(page, pageSize);
    			dto.setId(null);
    			list = rfqMapper.selectTaskQtp(dto);
    			logger.debug("------------------流程：QTP 查询 end --------------------");
    			break;
    		case "VTP":
    			PageHelper.startPage(page, pageSize);
    			dto.setId(null);
    			list = rfqMapper.selectTaskVtp(dto);
    			logger.debug("------------------流程：VTP 查询 end --------------------");
    			break;
    		case "PCI":
    			PageHelper.startPage(page, pageSize);
    			dto.setId(null);
    			list = rfqMapper.selectTaskPci(dto);
    			logger.debug("------------------流程：PCI 查询 end --------------------");
    			break;
    		case "RFQ":
    			PageHelper.startPage(page, pageSize);
    			dto.setId(null);
    			list = rfqMapper.selectTaskRfq(dto);
    			logger.debug("------------------流程：RFQ 查询 end --------------------");
    			break;
    		case "designReview":
    			PageHelper.startPage(page, pageSize);
    			EcrDesignReview edr = new EcrDesignReview();
    			edr.setEcrno(dto.getEcrno());
    			List<EcrDesignReview> edrList = designMapper.select(edr);
    			for(EcrDesignReview e:edrList){
    				EcrRfq rfq = new EcrRfq();
    				try {
						BeanUtils.copyProperties(e, rfq);
						rfq.setId(e.getKid());
						rfq.setTaskStatus(e.getStatus());
						rfq.setTaskType("设计评审");
						list.add(rfq);
					} catch (Exception e1) {
						e1.printStackTrace();
					} 
    			}
    			logger.debug("------------------流程：设计评审  查询 end --------------------");
    			break;
    		default:
    			PageHelper.startPage(page, pageSize);
    			list = rfqMapper.selectTaskFile(dto);
    			logger.debug("------------------流程：技术文件 查询 end --------------------");
    			break;
    	}
		return list;
	}

	@Override
	public List<EcrRfq> saveTask(IRequest request, List<EcrRfq> list, String taskType) {
		switch(taskType) {
			case "QTP":
				list.forEach(dto ->{
					// 针对每行记录，如果选择不需要，直接更新业务表单，将任务状态标记为完成，时间为提交时间
					// 任务状态快码值：ECR_TASK_STATUS
					if(!"Y".equals(list.get(0).getIsNeed())) {
						dto.setTaskStatus("FINISHED");
						dto.setPlanFinishedDate(new Date());
						dto.setActFinishedDate(new Date());
					}
					// 保存业务数据
					rfqMapper.updateTaskQtp(dto);
				});
				break;
			case "VTP":
				list.forEach(dto ->{
					// 针对每行记录，如果选择不需要，直接更新业务表单，将任务状态标记为完成，时间为提交时间
					// 任务状态快码值：ECR_TASK_STATUS
					if(!"Y".equals(list.get(0).getIsNeed())) {
						dto.setTaskStatus("FINISHED");
						dto.setPlanFinishedDate(new Date());
						dto.setActFinishedDate(new Date());
					}
					// 保存业务数据
					rfqMapper.updateTaskVtp(dto);
				});
				break;
			case "PCI":
				list.forEach(dto ->{
					// 针对每行记录，如果选择不需要，直接更新业务表单，将任务状态标记为完成，时间为提交时间
					// 任务状态快码值：ECR_TASK_STATUS
					if(!"Y".equals(list.get(0).getIsNeed())) {
						dto.setTaskStatus("FINISHED");
						dto.setPlanFinishedDate(new Date());
						dto.setActFinishedDate(new Date());
					}
					// 保存业务数据
					rfqMapper.updateTaskPci(dto);
				});
				break;
			case "RFQ":
				list.forEach(dto ->{
					// 针对每行记录，如果选择不需要，直接更新业务表单，将任务状态标记为完成，时间为提交时间
					// 任务状态快码值：ECR_TASK_STATUS
					if(!"Y".equals(list.get(0).getIsNeed())) {
						dto.setTaskStatus("FINISHED");
						dto.setPlanFinishedDate(new Date());
						dto.setActFinishedDate(new Date());
					}
					// 保存业务数据
					rfqMapper.updateTaskRfq(dto);
				});
				break;
			case "designReview":
				list.forEach(dto ->{
					// 针对每行记录，如果选择不需要，直接更新业务表单，将任务状态标记为完成，时间为提交时间
					// 任务状态快码值：ECR_TASK_STATUS
					if(!"Y".equals(list.get(0).getIsNeed())) {
						dto.setTaskStatus("FINISHED");
						dto.setPlanFinishedDate(new Date());
						dto.setActFinishedDate(new Date());
					}					
					EcrDesignReview edr = new EcrDesignReview();
					try {
						BeanUtils.copyProperties(dto, edr);
					} catch (Exception e) {
						e.printStackTrace();
					}
					edr.setKid(dto.getId());
					edr.setStatus(dto.getTaskStatus());
					// 保存业务数据
					designMapper.updateByPrimaryKey(edr);
				});
				break;
			default:
				list.forEach(dto ->{
					// 针对每行记录，如果选择不需要，直接更新业务表单，将任务状态标记为完成，时间为提交时间
					// 任务状态快码值：ECR_TASK_STATUS
					if(!"Y".equals(list.get(0).getEditFlag())) {
						dto.setTaskStatus("FINISHED");
						dto.setPlanFinishedDate(new Date());
						dto.setActFinishedDate(new Date());
					}
					// 到业务表中抓取当前版本信息
					String newVersion = rfqMapper.selectFileVersion(dto.getItemId());
					logger.debug("=====newVersion:" + newVersion + ",itemId:" + dto.getItemId());
					dto.setNewVersion(newVersion);
					// 保存业务数据
					rfqMapper.updateTaskFile(dto);
				});
				break;
		}
		return list;
	}

	@Transactional(rollbackFor = Exception.class,propagation=Propagation.REQUIRES_NEW) 
	@Override
	public void taskFinish(IRequest request, String taskType, String ecrno, String skuId, String newVersion, String taskStatus, Date actFinishDate) {
		// TaskType 就是 processCode
		logger.debug("==================查询任务完成状态，完成时将新版本和时间更新到任务表中,再结束流程 start====================");
		logger.debug("taskType:" + taskType + ",ecrno:" + ecrno);
		Long id = 0L;  // 技术文件：head 表的id;其他：ecrMain 的 kid
		Long processInstanceId = 0L;
		// 根据参数查询出流程id 和 流程表主键  
		// FIXME 查询条件：不是已完成状态的数据  (!D)
		EcrProcess ep = new EcrProcess();
		ep.setProcessCode(taskType);
		ep.setEcrno(ecrno);
		List<EcrProcess> list = processMapper.select(ep);
		EcrProcess thisPro = new EcrProcess();
		if(!list.isEmpty() && list.get(0) !=null) {
			for(EcrProcess pro: list){
				if(!"D".equalsIgnoreCase(pro.getProcessStatus())) {
					id = pro.getEcrId();
					processInstanceId = pro.getProcessId();
					thisPro = pro;
					break;
				}
			}
		} else {
			logger.debug("未查询到流程数据");
			logger.debug("=================end================");
			return;
		}
		// 查询参数
		EcrRfq dto = new EcrRfq();
		dto.setEcrno(ecrno);
		dto.setItemId(skuId);
		// 查询 ecrId
		EcrMain main = new EcrMain();
		main.setEcrno(ecrno);
		List<EcrMain> mainList = mainMapper.select(main);
		
		switch(taskType) {
			case "QTP":
				List<EcrRfq> qtps = rfqMapper.selectTaskQtp(dto);
				qtps.forEach(qtp->{
					// 更新状态，实际完成时间（现在）
					qtp.setActFinishedDate(actFinishDate);
					qtp.setTaskStatus(taskStatus);
					// 更新到QTP
					rfqMapper.updateTaskQtp(qtp);
				});
				// 触发 VTP 初始化任务
				if(eptService.vtp(request, ecrno)) {
					//  触发 VTP 工作流
					if(mainList.isEmpty()) {
						logger.debug("========== 未找到 ecrId");
					} else {
						Long ecrId = mainList.get(0).getKid();
						processService.startTask(request, "VTP", request.getEmployeeCode(), ecrId, ecrno);
					}
				} 
				break;
			case "VTP":
				List<EcrRfq> vtps = rfqMapper.selectTaskVtp(dto);
				vtps.forEach(vtp->{
					// 更新状态，实际完成时间（现在）
					vtp.setActFinishedDate(actFinishDate);
					vtp.setTaskStatus(taskStatus);
					// 更新到VTP
					rfqMapper.updateTaskVtp(vtp);					
				});
				// 触发 PCI 初始化任务
				if(eptService.pci(request, ecrno)) {
					// 触发 PCI 工作流
					if(mainList.isEmpty()) {
						logger.debug("========== 未找到 ecrId");
					} else {
						Long ecrId = mainList.get(0).getKid();
						processService.startTask(request, "PCI", request.getEmployeeCode(), ecrId, ecrno);
					}
				}
				break;
			case "PCI":
				List<EcrRfq> pcis = rfqMapper.selectTaskPci(dto);
				pcis.forEach(pci->{
					// 更新状态，实际完成时间（现在）
					pci.setActFinishedDate(actFinishDate);
					pci.setTaskStatus(taskStatus);
					// 更新到PCI
					rfqMapper.updateTaskPci(pci);					
				});
				// 修改 ECR 批准状态生效为已完成
				if(mainList.isEmpty()) {
					logger.debug("========== 未找到 ecrId，未修改 ECR 批准生效状态");
				} else {
					EcrMain em = mainList.get(0);
					em.setStatus("已完成");
					mainMapper.updateByPrimaryKey(em);
					logger.debug("========== ecrId： "+ em.getKid()+ "，已修改 ECR 批准生效状态");
				}
				break;
			case "RFQ":
				List<EcrRfq> rfqs = rfqMapper.selectTaskRfq(dto);
				rfqs.forEach(rfq->{
					// 更新状态，实际完成时间（现在）
					rfq.setActFinishedDate(actFinishDate);
					rfq.setTaskStatus(taskStatus);
					// 更新到RFQ
					rfqMapper.updateTaskRfq(rfq);
				});
				break;
			case "designReview":
				EcrDesignReview edr = new EcrDesignReview();
				List<EcrDesignReview> edrList = designMapper.select(edr);
				edrList.forEach(e->{
					// 更新状态，实际完成时间（现在）
					e.setActFinishedDate(actFinishDate);
					e.setStatus(taskStatus);
					// TODO 评审编码取值逻辑
					// 更新到 设计评审
					designMapper.updateByPrimaryKey(e);
				});
				break;
			default:
				// 更新到技术文件表头表
				dto.setId(Long.valueOf(id));
				dto.setActFinishedDate(actFinishDate);
				dto.setTaskStatus(taskStatus);
				rfqMapper.updateTaskFileHead(dto);
				List<EcrRfq> files = rfqMapper.selectTaskFile(dto);
				files.forEach(file->{
					// 字段新版本和状态，实际完成时间（现在）
					file.setActFinishedDate(actFinishDate);
					file.setNewVersion(newVersion);
					logger.debug("==========newVersion:" + newVersion + ",itemId:" + file.getItemId());
					file.setTaskStatus(taskStatus);
					// 更新到技术文件表行表
					rfqMapper.updateTaskFile(file);
				});
				// 触发 QTP 初始化任务
				if(eptService.qtp(request, ecrno)) {
					// 触发 QTP 工作流
					if(mainList.isEmpty()) {
						logger.debug("========== 未找到 ecrId");
					} else {
						Long ecrId = mainList.get(0).getKid();
						processService.startTask(request, "QTP", request.getEmployeeCode(), ecrId, ecrno);
					}
				}
				break; 
		}
		// 3. 结束流程
		activitiService.deleteProcessInstance(String.valueOf(processInstanceId));
		logger.debug("结束流程");
		// 4.修改流程状态为已完成
		thisPro.setProcessStatus("D");
		thisPro.setProcessEndDate(new Date());
		processMapper.updateByPrimaryKey(thisPro);
		logger.debug("=================end================");
	}



	@Override
	public void returnNewVersion(IRequest request, String taskType, String ecrno, String skuId) {
		Long id = 0L;  // 技术文件：head 表的id;其他表：ecrMain 的 kid
		// 根据参数查询出流程id 和 流程表主键
		// FIXME 查询条件：不是已完成状态的数据  (!D)
		EcrProcess ep = new EcrProcess();
		ep.setProcessCode(taskType);
		ep.setEcrno(ecrno);
		EcrProcess thisPro = new EcrProcess();
		List<EcrProcess> proList = processMapper.select(ep);
		if(!proList.isEmpty() && proList.get(0) !=null) {
			for(EcrProcess pro: proList){
				if(!"D".equalsIgnoreCase(pro.getProcessStatus())) {
					id = pro.getEcrId();
					thisPro = pro;
					break;
				}
			}
		} else {
			logger.debug("未查询到流程数据");
			logger.debug("=================end================");
			return;
		}
		logger.debug("ecrno:" + ecrno + ",bk:" + id + ",taskType:" + taskType);
		// 查询参数
		EcrRfq dto = new EcrRfq();
		dto.setEcrno(ecrno);
		dto.setItemId(skuId);
		List<EcrRfq> list = new ArrayList<>();
		
		// 查询 ecrId
		EcrMain main = new EcrMain();
		main.setEcrno(ecrno);
		List<EcrMain> mainList = mainMapper.select(main);
		
		switch(taskType) {
		case "QTP":
			list = rfqMapper.selectTaskQtp(dto);
			list.forEach(qtp->{
				//更新状态和实际完成时间
				qtp.setTaskStatus("FINISHED");
				qtp.setActFinishedDate(new Date());
				// 根据 id 进行保存
				rfqMapper.updateTaskQtp(qtp);
			});
			// 触发 VTP 初始化任务
			if(eptService.vtp(request, ecrno)) {
				// 触发 VTP 工作流
				if(mainList.isEmpty()) {
					logger.debug("========== 未找到 ecrId");
				} else {
					Long ecrId = mainList.get(0).getKid();
					processService.startTask(request, "VTP", request.getEmployeeCode(), ecrId, ecrno);
				}
			}
			break;
		case "VTP":
			list = rfqMapper.selectTaskVtp(dto);
			list.forEach(vtp->{
				//更新状态和实际完成时间
				vtp.setTaskStatus("FINISHED");
				vtp.setActFinishedDate(new Date());
				// 根据 id 进行保存
				rfqMapper.updateTaskVtp(vtp);
			});
			// 触发 PCI 初始化任务
			if(eptService.pci(request, ecrno)) {
				// 触发 PCI 工作流
				if(mainList.isEmpty()) {
					logger.debug("========== 未找到 ecrId");
				} else {
					Long ecrId = mainList.get(0).getKid();
					processService.startTask(request, "PCI", request.getEmployeeCode(), ecrId, ecrno);
				}
			}
			break;
		case "PCI":
			list = rfqMapper.selectTaskPci(dto);
			list.forEach(pci->{
				//更新状态和实际完成时间
				pci.setTaskStatus("FINISHED");
				pci.setActFinishedDate(new Date());
				// 根据 id 进行保存
				rfqMapper.updateTaskPci(pci);
			});
			// 修改 ECR 批准状态生效为已完成
			if(mainList.isEmpty()) {
				logger.debug("========== 未找到 ecrId，未修改 ECR 批准生效状态");
			} else {
				EcrMain em = mainList.get(0);
				em.setStatus("已完成");
				mainMapper.updateByPrimaryKey(em);
				logger.debug("========== ecrId： "+ em.getKid()+ "，已修改 ECR 批准生效状态");
			}
			break;
		case "RFQ":
			list = rfqMapper.selectTaskRfq(dto);
			list.forEach(rfq->{
				//更新状态和实际完成时间
				rfq.setTaskStatus("FINISHED");
				rfq.setActFinishedDate(new Date());
				// 根据 id 进行保存
				rfqMapper.updateTaskRfq(rfq);
			});
			break;
		case "designReview":
			EcrDesignReview edr = new EcrDesignReview();
			List<EcrDesignReview> edrList = designMapper.select(edr);
			edrList.forEach(e->{
				// 更新状态，实际完成时间（现在）
				e.setActFinishedDate(new Date());
				e.setStatus("FINISHED");
				// TODO 评审编码取值逻辑
				// 更新到 设计评审
				designMapper.updateByPrimaryKey(e);
			});
			break;
		default:
			// 更新到技术文件表头表
			dto.setId(Long.valueOf(id));
			dto.setActFinishedDate(new Date());
			dto.setTaskStatus("FINISHED");
			rfqMapper.updateTaskFileHead(dto);
			list = rfqMapper.selectTaskFile(dto);
			list.forEach(file->{
				// 查询当前版本
				String newVersion = rfqMapper.selectFileVersion(file.getItemId());
				logger.debug("==========newVersion:" + newVersion + ",itemId:" + file.getItemId());
				file.setNewVersion(newVersion);
				file.setTaskStatus("FINISHED");
				file.setActFinishedDate(new Date());			
				// 根据 id 进行保存
				rfqMapper.updateTaskFile(file);
			});
			// 触发 QTP 初始化任务
			if(eptService.qtp(request, ecrno)) {
				// 触发 QTP 工作流
				if(mainList.isEmpty()) {
					logger.debug("========== 未找到 ecrId");
				} else {
					Long ecrId = mainList.get(0).getKid();
					processService.startTask(request, "QTP", request.getEmployeeCode(), ecrId, ecrno);
				}
			}
			break;
		}
		// 修改流程状态为已完成
		thisPro.setProcessStatus("D");
		thisPro.setProcessEndDate(new Date());
		processMapper.updateByPrimaryKey(thisPro);
	}
	
	/**
	 * 初始化  RFQ 数据 + 发起 RFQ 工作流
	 * @param request
	 * @param ecrno
	 * @param itemIds
	 */
	@Override
	public void startProcessRfq(IRequest request, String ecrno, List<String> itemIds) {
		List<EcrRfq> list = initData(request, ecrno, itemIds);
		Set<String> set = new HashSet<>();		
		list.forEach(rfq ->{	
			if(rfq.getDutyby() != null) {
				set.add(rfq.getDutyby());
			}
		});
		if(set.isEmpty()) {
			throw new RuntimeException("未找到 RFQ 流程的负责人");
		}
		for(String dutyBy: set) {
			EcrMain main = new EcrMain();
			main.setEcrno(ecrno);
			List<EcrMain> mainList = mainMapper.select(main);
			if(mainList.isEmpty() && mainList.get(0) == null ) {
				logger.debug("========== 未找到 ecrId");
			} else {
				Long ecrId = mainList.get(0).getKid();
				processService.startTask(request, "RFQ", dutyBy, ecrId, ecrno);
				logger.debug("========== 发起工作流 RFQ, 负责人：" + dutyBy + ", ecrno:" + ecrno);
			}
		}
	}



	@Override
	public void updateIsNeed(IRequest requestCtx, String ecrno, String taskType, String ecrId, String dutyby) {
		logger.debug("传入参数 taskType:" + taskType + ",ecrno:" + ecrno + "-------------");
		if("".equals(taskType) || taskType == null) {
			logger.debug("------------- 未传入参数 taskType, 无法更新 VTP，QTP 是否要做字段-------------");
			return;
		}
		if("".equals(ecrno) || ecrno == null) {
			logger.debug("------------- 未传入参数 ecrno, 无法更新 VTP，QTP 是否要做字段-------------");
			return;
		}
		// 查询参数
		EcrMain main = new EcrMain();
		main.setEcrno(ecrno);
		List<EcrMain> mainList = mainMapper.select(main);
		if(mainList.isEmpty() || mainList.get(0) == null) {
			logger.debug("------------- 表 hpm_ecr_main 不存在 ecrno 的数据，  无法更新 VTP，QTP 是否要做字段-------------");
			return;
		}
		EcrMain ecrmain = mainList.get(0);
		
		// 是否需要字段
		String isNeed = "N";
		switch(taskType) {
			case "QTP": 
				
				// 查询 是否要做字段 存在 Y 的数据为需要
				EcrQtp qtp = new EcrQtp();
				qtp.setEcrno(ecrno);
				List<EcrQtp> qtplist = qtpMapper.select(qtp);
				for(EcrQtp dto : qtplist) {
					if("Y".equals(dto.getIsNeed())) {
						isNeed = "Y";
					}
				}
				
				// 更新 main 表的 QTP 是否要做字段
				ecrmain.setQtpFlag(isNeed);
				mainMapper.updateByPrimaryKeySelective(ecrmain);
				logger.debug("------------- 更新 QTP 是否需要："+ isNeed +"-------------");
				break;
			case "VTP":
				
				// 查询 是否要做字段 存在 Y 的数据为需要
				EcrVtp vtp = new EcrVtp();
				vtp.setEcrno(ecrno);
				List<EcrVtp> vtplist = vtpMapper.select(vtp);
				for(EcrVtp dto : vtplist) {
					if("Y".equals(dto.getIsNeed())) {
						isNeed = "Y";
					}
				}
				
				// 更新 main 表的 VTP 是否要做字段
				ecrmain.setVtpFlag(isNeed);
				mainMapper.updateByPrimaryKeySelective(ecrmain);
				logger.debug("------------- 更新 VTP 是否需要："+ isNeed +"-------------");
				break;
			default:break;
		}
	
	}
}