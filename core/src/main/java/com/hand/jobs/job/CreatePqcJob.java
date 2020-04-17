package com.hand.jobs.job;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hand.hap.job.AbstractJob;
import com.hand.hap.mail.dto.Message;
import com.hand.hap.mail.mapper.MessageMapper;
import com.hand.hap.mail.service.IEmailService;
import com.hand.hap.task.info.ExecutionInfo;
import com.hand.hap.task.service.ITask;

import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.hand.hap.core.IRequest;
import com.hand.hqm.hqm_pqc_inspection_h.dto.PqcInspectionH;
import com.hand.hqm.hqm_pqc_inspection_h.service.IPqcInspectionHService;
import com.hand.hqm.hqm_pqc_inspection_l.dto.PqcInspectionL;
import com.hand.hqm.hqm_qua_ins_time_l.dto.QuaInsTimeL;
import com.hand.hqm.hqm_qua_ins_time_l.mapper.QuaInsTimeLMapper;
import com.hand.hqm.hqm_qua_ins_time_l.service.IQuaInsTimeLService;
import org.apache.commons.collections.CollectionUtils;

/**
 * 定时生成巡检单定时job
 * 
 * @author tainmin.wang
 * @version date：2019年8月7日 下午1:55:16
 * 
 */
@Transactional(rollbackFor = Exception.class)
public class CreatePqcJob extends AbstractJob implements ITask{

	@Autowired
	QuaInsTimeLMapper quaInsTimeLMapper;
	@Autowired
	IQuaInsTimeLService iQuaInsTimeLService;
	@Autowired
	IPqcInspectionHService iPqcInspectionHService;
	@Autowired
    private IEmailService mailService;
    @Autowired
    private MessageMapper messageMapper;
	IRequest requestContext;
	
	@Override
	public void execute(ExecutionInfo executionInfo) throws Exception {
		//self();
	}
	@Override
	public void safeExecute(JobExecutionContext context) throws Exception {
		//self();
	}

	private void self() throws Exception { // TODO Auto-generated method stub
		Date t = new Date();
		SimpleDateFormat tn = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String timeNow = tn.format(t).substring(11, 16);

		QuaInsTimeL lineData = new QuaInsTimeL();
		lineData.setInspectionTime(timeNow);
		// 获取所有符合时间的Time 行数据
		List<QuaInsTimeL> DataList = iQuaInsTimeLService.groupbyselect(lineData);

		if (DataList == null || DataList.size() == 0) {
			return;
		} else {
			for (QuaInsTimeL line : DataList) {
				if (!isShiftNow(line.getProdLineId(), line.getShiftCode(), line.getPlantId()))
					return;
				PqcInspectionH pqcInspectionH = new PqcInspectionH();
				// 获取目标头表的业务字段调接口
				pqcInspectionH.setPlantId(line.getPlantId());
				pqcInspectionH.setProdLineId(line.getProdLineId());
				// 获取当前 时间头表的 ID 来获取同一时间的行
				QuaInsTimeL timeLSearch = new QuaInsTimeL();
				timeLSearch.setInspectionTime(timeNow);
				timeLSearch.setTimeId(line.getTimeId());
				List<QuaInsTimeL> timeLine = quaInsTimeLMapper.myselect(timeLSearch);
				List<PqcInspectionL> pqcLineList = new ArrayList<PqcInspectionL>();
				for (QuaInsTimeL lineDto : timeLine) {
					PqcInspectionL pqcInspectionL = new PqcInspectionL();
					pqcInspectionL.setStandardOpId(lineDto.getStandardOpId());
					pqcInspectionL.setWorkstationId(lineDto.getWorkstationId());
					pqcLineList.add(pqcInspectionL);
				}
				iPqcInspectionHService.addNewInspectionJob(pqcInspectionH, pqcLineList);
			}

		}

	}

	private boolean isShiftNow(Float prodLineId, String shiftCode, Float plantId) {
		// 判断是否符合当前班次范围
		QuaInsTimeL search = new QuaInsTimeL();
		search.setProdLineId(prodLineId);
		search.setShiftCode(shiftCode);
		search.setPlantId(plantId);
		List<QuaInsTimeL> resultList = quaInsTimeLMapper.shiftNowQuery(search);
		if (resultList == null || resultList.size() == 0) {
			return false;
		}
		return true;
	}
	
	private void mailDemoTest() {
		try {
            Map<String, Object> param = new HashMap<>();
            List<Message> userEmailToSend = messageMapper.selectEmailSendByJob();
            if(CollectionUtils.isNotEmpty(userEmailToSend)){
                mailService.sendEmailMessage(userEmailToSend,param);
                setExecutionSummary((String) param.get(""));
            }
        } catch (Exception e) {
            
        }
	}
}
