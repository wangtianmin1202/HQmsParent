package com.hand.hap.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;

import com.hand.hap.core.IRequest;
import com.hand.hqm.hqm_pqc_inspection_h.dto.PqcInspectionH;
import com.hand.hqm.hqm_pqc_inspection_h.service.IPqcInspectionHService;
import com.hand.hqm.hqm_pqc_inspection_l.dto.PqcInspectionL;
import com.hand.hqm.hqm_qua_ins_time_h.dto.QuaInsTimeH;
import com.hand.hqm.hqm_qua_ins_time_l.dto.QuaInsTimeL;
import com.hand.hqm.hqm_qua_ins_time_l.mapper.QuaInsTimeLMapper;
import com.hand.hqm.hqm_qua_ins_time_l.service.IQuaInsTimeLService;

/**
 * @author tainmin.wang
 * @version date：2019年8月7日 下午1:55:16
 * 
 */
public class JobTest extends com.hand.hap.job.AbstractJob {

	@Autowired
	QuaInsTimeLMapper quaInsTimeLMapper;
	@Autowired
	private QuaInsTimeLMapper mapper;
	@Autowired
	IQuaInsTimeLService QuaInsTimeLService;
	@Autowired
	IPqcInspectionHService PqcInspectionHService;

	IRequest requestContext;

	@Override
	public void safeExecute(JobExecutionContext context) throws Exception {
		// TODO Auto-generated method stub
		Date t = new Date();
		SimpleDateFormat tn = new SimpleDateFormat("HH:mm");
		String timeNow = tn.format(t);

		QuaInsTimeL lineData = new QuaInsTimeL();
		lineData.setInspectionTime(timeNow);
		// 获取所有符合时间的Time 行数据
		List<QuaInsTimeL> DataList = QuaInsTimeLService.groupbyselect(lineData);

		if (DataList.size() == 0) {
			return;
		} else {
			for (QuaInsTimeL line : DataList) {
				PqcInspectionH HeadData = new PqcInspectionH();
				// 获取 到 目标头表的 业务字段 直接 穿接口
				HeadData.setPlantId(line.getPlantId());
				HeadData.setProdLineId(line.getProdLineId());

				// 获取当前 时间头表的 ID 来获取同一时间的行
				lineData.setTimeId(line.getTimeId());
				List<QuaInsTimeL> timeLine = QuaInsTimeLService.myselect(null, lineData, 0, 0);
				List<PqcInspectionL> PqcLineList = new ArrayList<PqcInspectionL>();
				for (QuaInsTimeL lineT : DataList) {
					PqcInspectionL pqcInspectionL = new PqcInspectionL();
					pqcInspectionL.setStandardOpId(lineT.getStandardOpId());
					pqcInspectionL.setWorkstationId(lineT.getWorkstationId());

					PqcLineList.add(pqcInspectionL);
				}
				PqcInspectionHService.addNewInspectionJob(HeadData, PqcLineList);

			}

		}

	}

}
