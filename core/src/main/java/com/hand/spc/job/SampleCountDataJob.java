package com.hand.spc.job;

import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.hand.hap.job.AbstractJob;
import com.hand.spc.job.service.ISampleCountCalculationSService;
/**
 * 样本数据JOB(计数型)
 */
public class SampleCountDataJob extends AbstractJob {

	private Logger logger = LoggerFactory.getLogger(SampleCountDataJob.class);

	@Autowired
	private ISampleCountCalculationSService sampleCountCalculationService;

	@Override
	public void safeExecute(JobExecutionContext context) throws Exception {
		try {
			// String strParam= context.getMergedJobDataMap().getString("param1");
			Long tenantId = -1L;
			Long siteId = -1L;
			sampleCountCalculationService.sampleCountCalculation(tenantId, siteId);
		} catch (Exception e) {
			if (logger.isErrorEnabled()) {
				logger.error(e.getMessage(), e);
			}
		}

		logger.info("计数型 job   1111111111111111111111111111111111111111111111");

	}

	@Override
	public boolean isRefireImmediatelyWhenException() {
		return true;
	}
}
