/**
 * 
 */
package com.hand.npi.npi_technology.serviceTask;

import java.util.Date;
import java.util.List;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.ContextLoader;

import com.hand.hqm.hqm_measure_tool.dto.MeasureTool;
import com.hand.hqm.hqm_measure_tool.mapper.MeasureToolMapper;
import com.hand.hqm.hqm_measure_tool_scrap.dto.MeasureToolScrap;
import com.hand.hqm.hqm_measure_tool_scrap.mapper.MeasureToolScrapMapper;
import com.hand.hqm.hqm_measure_tool_scrap.service.IMeasureToolScrapService;

/** 
 *@version:1.0
 *@Description: 
 *@author: Magicor
 *@date: Jan 10, 2020 1:43:19 PM
*/
@Component
public class NpiPmfeaServiceTask implements JavaDelegate{

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private MeasureToolScrapMapper measureToolScrapMapper;
	
	@Autowired
	private MeasureToolMapper measureToolMapper;
	
	@Override
	public void execute(DelegateExecution execution) {
		if (measureToolScrapMapper == null) {
			measureToolScrapMapper = ContextLoader.getCurrentWebApplicationContext()
					.getBean(MeasureToolScrapMapper.class);
		}
		if (measureToolMapper == null) {
			measureToolMapper = ContextLoader.getCurrentWebApplicationContext()
					.getBean(MeasureToolMapper.class);
		}
		try {
			//根据流程id获取业务数据
			String processInstanceId = execution.getProcessInstanceId();
			MeasureToolScrap scrap = new MeasureToolScrap();
			scrap.setProcessInstanceId(processInstanceId);
			List<MeasureToolScrap> list = measureToolScrapMapper.queryByCondition(scrap);
			if (list != null && list.size() == 1) {
				scrap = list.get(0);
				//根据当前节点的uuid来判断走到了哪一步
				String currentActivityId = execution.getCurrentActivityId();
				if ("sid-VOrsEB6G-SFa3-4amK-83G1-yYmmFsSfxc7S".equals(currentActivityId)) {
					//直属经理审批通过
					scrap.setStatus("质量部经理审批");
				} else if ("sid-J65Eii2G-R0Lt-4Whb-8c2j-65n72WtyMmn2".equals(currentActivityId)
						|| "sid-DtYSVIAs-w3NT-4bCe-8Dea-q6xm8UTnMYGT".equals(currentActivityId)) {
					//直属经理审批拒绝 || 质量部经理审批拒绝
					scrap.setStatus("单据修改");
				} else if ("sid-OcFeOOXi-8Wf2-4lM5-8N1N-VAFeSYUXGrqg".equals(currentActivityId)) {
					//质量部经理审批通过
					scrap.setStatus("审批通过");
					scrap.setProcessEndTime(new Date());
					MeasureTool measureTool = new MeasureTool();
					measureTool.setMeasureToolId(scrap.getMeasureToolId());
					//更新此量具在台账表中的状态为“报废”
					measureTool.setMeasureToolStatus("3");
					measureToolMapper.updateByPrimaryKeySelective(measureTool);
				} else if ("sid-enK4nAb2-qtFy-4XvA-82yu-PtG20JM7Tks8".equals(currentActivityId)) {
					//单据修改审批通过
					scrap.setStatus("直属经理审批");
				} else if ("sid-y6l0wCsI-6JkE-4428-8BFA-VB7dcHFkjfof".equals(currentActivityId)) {
					//单据修改审批拒绝
					scrap.setStatus("审批拒绝");
					scrap.setProcessEndTime(new Date());
				} else {
					throw new Exception();
				}
				measureToolScrapMapper.updateByPrimaryKeySelective(scrap);
			} else {
				throw new Exception();
			}
		} catch (Exception e) {
			logger.error(">>>更新业务数据失败:" + e.getMessage());
		}
	}

}
