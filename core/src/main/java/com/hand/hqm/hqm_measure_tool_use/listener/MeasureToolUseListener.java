/**
 * 
 */
package com.hand.hqm.hqm_measure_tool_use.listener;

import java.util.Date;
import java.util.List;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.hand.hqm.hqm_measure_tool.dto.MeasureTool;
import com.hand.hqm.hqm_measure_tool.mapper.MeasureToolMapper;
import com.hand.hqm.hqm_measure_tool_use.dto.MeasureToolUse;
import com.hand.hqm.hqm_measure_tool_use.mapper.MeasureToolUseMapper;
import com.hand.wfl.util.SpringContextHolder;

/** 
 *@version:1.0
 *@Description: 
 *@author: Magicor
 *@date: Dec 19, 2019 6:44:41 PM
*/
@Component
public class MeasureToolUseListener implements TaskListener{

	@Autowired
	private MeasureToolUseMapper measureToolUseMapper;
	
	@Autowired
	private MeasureToolMapper measureToolMapper;
	
	@Override
	public void notify(DelegateTask delegateTask) {
		try {
			if (measureToolUseMapper == null) {
				measureToolUseMapper = SpringContextHolder.getBean(MeasureToolUseMapper.class);
			}
			if (measureToolMapper == null) {
				measureToolMapper = SpringContextHolder.getBean(MeasureToolMapper.class);
			}
			String processInstanceId = delegateTask.getProcessInstanceId();
			MeasureToolUse measureToolUse = new MeasureToolUse();
			measureToolUse.setProcessInstanceId(processInstanceId);
			measureToolUse.setReturnDate(new Date());
			//measureToolUseMapper.insertSelective(measureToolUse);
			List<MeasureToolUse> list = measureToolUseMapper.queryByProcessInstId(measureToolUse);
			if (!list.isEmpty() && list.size() == 1) {
				MeasureTool measureTool = new MeasureTool();
				measureTool.setMeasureToolId(list.get(0).getMeasureToolId());
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

}
