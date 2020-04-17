package com.hand.spc.job;

import java.util.Random;

import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.hand.hap.job.AbstractJob;
import com.hand.spc.pspc_classify.dto.Classify;
import com.hand.spc.pspc_classify.service.IClassifyService;

public class JobTest extends AbstractJob {

	private Logger logger = LoggerFactory.getLogger(JobTest.class);

	@Autowired
	private IClassifyService service;

	@Override
	public void safeExecute(JobExecutionContext context) throws Exception {
		try {
			Classify classify1 = new Classify();
			Random r = new Random();
			int ran1 = r.nextInt(100000);
			classify1.setTenantId((long) ran1);
			classify1.setSiteId((long) ran1);
			classify1.setClassifyCode("test");
			service.insertSelective(null, classify1);
		} catch (Exception e) {
			if (logger.isErrorEnabled()) {
				logger.error(e.getMessage(), e);
			}
		}

		logger.info("ssssssssssssssssssssssssssssssssssssssssssssssssss");

	}

	@Override
	public boolean isRefireImmediatelyWhenException() {
		return true;
	}
}
