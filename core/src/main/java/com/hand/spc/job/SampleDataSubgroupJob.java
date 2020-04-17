package com.hand.spc.job;

import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.hand.hap.job.AbstractJob;
import com.hand.spc.job.service.ISampleCalculationSService;

/**
 * 样本数据分组JOB(计量型)
 */
public class SampleDataSubgroupJob extends AbstractJob {
	private Logger logger = LoggerFactory.getLogger(SampleDataSubgroupJob.class);

	@Autowired
	private ISampleCalculationSService sampleCalculationService;

	@Override
	public void safeExecute(JobExecutionContext context) throws Exception {
		try {
			// String strParam= context.getMergedJobDataMap().getString("param1");
			Long tenantId = -1L;
			Long siteId = -1L;
			sampleCalculationService.sampleDataSubgroupCalculation(tenantId, siteId);
		} catch (Exception e) {
			if (logger.isErrorEnabled()) {
				logger.error(e.getMessage(), e);
			}
		}

		logger.info("计量型 job   2222222222222222222222222222222222222222222");
	}

	@Override
	public boolean isRefireImmediatelyWhenException() {
		return true;
	}

}
