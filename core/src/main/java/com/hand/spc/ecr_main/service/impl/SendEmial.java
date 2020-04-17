package com.hand.spc.ecr_main.service.impl;

import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hand.spc.utils.MailUtil;
import net.logstash.logback.encoder.org.apache.commons.lang.StringUtils;

import com.hand.hap.activiti.custom.IActivitiBean;
import com.hand.hap.hr.dto.Employee;
import com.hand.hap.hr.mapper.EmployeeMapper;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.activiti.engine.delegate.DelegateExecution;

@Service
public class SendEmial implements IActivitiBean {
	
	@Autowired
	private EmployeeMapper empMapper;
	
	private final Logger logger = LoggerFactory.getLogger(SendEmial.class); 
	
	private static final String MATESCRAPEM = "matScrapEmail";
	private static final String MATESCRAPEC = "matScrapEcrno";
	private static final String SOLUTIONEM = "solutionEmail";
	private static final String SOLUTIONEC = "solutionEcrno";
	
	/**
	 * 流程-全员邮件
	 * @param execution
	 */
	public void sendAllEmail(DelegateExecution execution, String email) {
		String bk = execution.getProcessInstanceBusinessKey();
		String flowName = execution.getCurrentActivityId();
		logger.debug("bk:" + bk);
		logger.debug("type:" + email);
		logger.debug("flowName:" + flowName);
		// 发送通过邮件
		String emails = (String) execution.getVariable(email);
		String[] emailarr = StringUtils.split(emails, ";");
	    Set<String> sets = new HashSet<>();
		for(int i=0; i < emailarr.length; i++) {
			sets.add(emailarr[i]);
		}
		sets.forEach(set ->{
			logger.debug("email:" + set);
			MailUtil.sendMail(set, "申请流程", "流程申请已通过。" + flowName);
		});
	}
	
	/**
	 * 流程-给上级和员工发送邮件
	 * 注：例 监听节点为  solution12 ，员工应为审批节点为  solution10 的审批人
	 * 即监听节点和审批节点前缀保持一致
	 * @param execution
	 */
	public void sendDirEmail(DelegateExecution execution) {
		logger.debug("-----------------1. 给上级和员工发送邮件 start---------------------------");
		// 获得节点ID
		String flowName = execution.getCurrentActivityId();
		logger.debug("flowName：" + flowName);
		// 节点对应的审批节点
		String activityId = flowName.substring(0, flowName.length()-1) + "0";
		logger.debug("activityId：" + activityId);
	    // 获取员工编号
		String empCode = (String) execution.getVariable(activityId);
		logger.debug("empCode：" + empCode);
		// 通过员工编码查询员工.
		Employee emp = empMapper.queryByCode(empCode);
		if(emp.getEmail() == null || "".equals(emp.getEmail())) {
			logger.debug("员工邮件为空");
			logger.debug("-----------------1. 给上级和员工发送邮件 end---------------------------");
			return;
		}
		// 给员工发送超时邮件
		MailUtil.sendMail(emp.getEmail(), "申请流程", "您有一个流程审批已超时员工。" + flowName);
		logger.debug("emp-email: " + emp.getEmail());
		// 通过员工编码查找上级
		List<Employee> dirs = empMapper.getDirector(empCode);
		// 给上级发送超时邮件
		dirs.forEach(dir ->{
			if(dir.getEmail() != null && !"".equals(dir.getEmail())) {
				logger.debug("dir-email: " + dir.getEmail());
				// 记录邮箱
				execution.setVariable(MATESCRAPEM, execution.getVariable(MATESCRAPEM)+";"+(dir.getEmail()));
				MailUtil.sendMail(dir.getEmail(), "申请流程", "流程审批已超时上级。" + flowName);
			}
		});
		logger.debug("-----------------1. 给上级和员工发送邮件 end---------------------------");
	}
	
	/**
	 * 1. 物料管控物料处理方案流程-员工邮件
	 * @param execution
	 * @param tasks
	 * @param empCode
	 */
	public void matScrap(DelegateExecution execution, List<TaskEntity> tasks, String empCode) {
		logger.debug("----------1.物料管控处理方案-发送邮件-start-----------");
		// 将节点和员工编码存入流程变量.
		execution.setVariable(execution.getCurrentActivityId(), empCode);
		logger.debug("节点: " + execution.getCurrentActivityId());
		logger.debug("empCode: " + empCode);
		
		String bk = execution.getProcessInstanceBusinessKey();
		execution.getVariable(MATESCRAPEM);
		String flowName = execution.getCurrentActivityId();
		
		// 通过员工编码查询员工.
		Employee emp = empMapper.queryByCode(empCode);
		if(emp == null || emp.getEmail() == null || "".equals(emp.getEmail())) {
			return;
		}
		logger.debug("bk: " + bk);
		logger.debug("flowName: " + flowName);
		logger.debug("email: " + emp.getEmail());
		switch(flowName) {
			case "erc10":
			case "erc30":
			case "erc50":			
			case "erc70":
				// 记录邮箱
				execution.setVariable(MATESCRAPEM, execution.getVariable(MATESCRAPEM)+";"+(emp.getEmail()));
				// 发送审批邮件
				MailUtil.sendMail(emp.getEmail(), "申请流程", "您有一个流程需要审批。" + flowName);
				break;
			case "erc20":
			case "erc40":
			case "erc60":
			case "erc80":
				// 通过员工编码查找上级
				List<Employee> dirs = empMapper.getDirector(empCode);
				// 给上级发送超时邮件
				dirs.forEach(employee ->{
					if(employee.getEmail() != null && !"".equals(employee.getEmail())) {
						logger.debug("dir-email: " + employee.getEmail());
						// 记录邮箱
						execution.setVariable(MATESCRAPEM, execution.getVariable(MATESCRAPEM)+";"+(employee.getEmail()));
						MailUtil.sendMail(employee.getEmail(), "申请流程", "流程审批已超时上级。" + flowName);
					}
				});
				// 给员工发送超时邮件
				MailUtil.sendMail(emp.getEmail(), "申请流程", "您有一个流程审批已超时员工。" + flowName);
				break;
			case "erc90":
				// 发送拒绝邮件
				MailUtil.sendMail(emp.getEmail(), "申请流程", "您有一个流程申请被拒绝。" + flowName);
				break;
			default:
				break;
		}
		logger.debug("---------1.物料管控处理方案-发送邮件-end------------");
	}
	
	/**
	 * 2. 物料管控物料处理方案流程-监听（全员邮件）
	 * @param exe
	 */
	public void matScrap(DelegateExecution exe) {
		logger.debug("----------2.ECR改善方案-全员邮件-end-----------");
		sendAllEmail(exe, MATESCRAPEM);
		logger.debug("----------2.ECR改善方案-全员邮件-end-----------");
	}
	
	
	
	/**
	 * 2. ECR改善方案之审批流程-员工邮件
	 * @param execution
	 * @param tasks
	 * @param empCode
	 */
	public void solution(DelegateExecution exe, List<TaskEntity> tasks, String empCode) {
		logger.debug("----------2.ECR改善方案-发送邮件-start-----------");
		// 将节点和员工编码存入流程变量.
		exe.setVariable(exe.getCurrentActivityId(), empCode);
		logger.debug("节点: " + exe.getCurrentActivityId());
		logger.debug("empCode: " + empCode);
		
		String bk = exe.getProcessInstanceBusinessKey();
		String flowName = exe.getCurrentActivityId();
		logger.debug("bk: " + bk);
		logger.debug("email 流程变量: "+ exe.getVariable(SOLUTIONEM));
		logger.debug("flowName:" + flowName);
		// 通过员工编码查询员工.
		Employee emp = empMapper.queryByCode(empCode);
		if(emp.getEmail() == null || "".equals(emp.getEmail())) {
			logger.debug("人员 无 email 信息");
			logger.debug("----------2.ECR改善方案-发送邮件-end-----------");
			return;
		}
		logger.debug("员工 email: " + emp.getEmail());
		switch(flowName) {
			case "solution10":
			case "solution30":
			case "solution50":			
			case "solution70":
			case "solution90":
				// 记录邮箱
				exe.setVariable(SOLUTIONEM, exe.getVariable(SOLUTIONEM)+";"+(emp.getEmail()));
				// 发送审批邮件
				MailUtil.sendMail(emp.getEmail(), "申请流程", "您有一个流程需要审批。" + flowName);
				break;
			case "solution20":
			case "solution40":
			case "solution60":
			case "solution80":
			case "solution100":
				// 通过员工编码查找上级
				List<Employee> dirs = empMapper.getDirector(empCode);
				// 给上级发送超时邮件
				dirs.forEach(dir ->{
					if(dir.getEmail() != null && !"".equals(dir.getEmail())) {
						logger.debug("上级 email: " + dir.getEmail());
						// 记录邮箱
						exe.setVariable(SOLUTIONEM, exe.getVariable(SOLUTIONEM)+";"+(dir.getEmail()));
						MailUtil.sendMail(dir.getEmail(), "申请流程", "流程审批已超时上级。" + flowName);
					}
				});
				// 给员工发送超时邮件
				MailUtil.sendMail(emp.getEmail(), "申请流程", "您有一个流程审批已超时员工。" + flowName);
				break;
			case "solution110":
				// 发送拒绝邮件
				MailUtil.sendMail(emp.getEmail(), "申请流程", "您有一个流程申请被拒绝。" + flowName);
				break;
			default:
				break;
		}
		logger.debug("----------2.ECR改善方案-发送邮件-end-----------");
		
	}
	
	/**
	 * 2. ECR改善方案之审批流程-监听（全员邮件）
	 * @param exe
	 */
	public void solution(DelegateExecution exe) {
		logger.debug("----------2.ECR改善方案-全员邮件-start-----------");
		sendAllEmail(exe,SOLUTIONEM);
		logger.debug("----------2.ECR改善方案-全员邮件-end-----------");
	}
	
	@Override
	public String getBeanName() {
		return "SendEmial";
	}
	
}
