package com.hand.spc.job.controllers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.system.dto.ResponseData;
import com.hand.spc.job.service.ISampleCalculationSService;
import com.hand.spc.job.service.ISampleCountCalculationSService;
import com.hand.spc.job.service.impl.InterfaceService;
import com.hand.spc.job.service.impl.MessageThread;
import com.hand.spc.pspc_classify.dto.Classify;
import com.hand.spc.repository.dto.BaseCalcResultVO;
import com.hand.spc.repository.dto.EntityR;
import com.hand.spc.repository.dto.MessageR;

@Controller
public class JobTestController extends BaseController {
	
	@Autowired
	private ISampleCountCalculationSService sampleCountCalculationService;
	
	@Autowired
	private ISampleCalculationSService sampleCalculationService;
	
	@Autowired
    private InterfaceService interfaceService;// 发送接口
	
	@RequestMapping(value = "/pspc/jobtest1/query",method = RequestMethod.GET)//计数型
	@ResponseBody
	public ResponseData query1(Classify dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
			@RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) throws Exception {
		sampleCountCalculationService.sampleCountCalculation(-1L, -1L);
		
		//RedisSingle redisHelper = RedisSingle.getInstance();
		
		//redisHelper.strSet("kkk", "12345");
		
		//redisHelper测试
		/*String key= "key";
		System.out.println();
		hapRedisHelper.strSet(key, "sss");
		System.out.println(hapRedisHelper.strGet(key));
		
		
		List<CountStatistic> countStatisticList = new ArrayList<CountStatistic>();
		CountStatistic countStatistic = new CountStatistic();
		countStatistic.setTenantId(11L);
		countStatisticList.add(countStatistic);
		
		String countStatisticRediskey = "kkk";
		redisHelper.strSet(countStatisticRediskey, redisHelper.toJson(countStatisticList));
		String countStatisticRedis = redisHelper.strGet(countStatisticRediskey);
		countStatisticList = redisHelper.fromJsonList(countStatisticRedis, CountStatistic.class);
		System.out.println(countStatisticList);*/
		return new ResponseData();
	}
	
	@RequestMapping(value = "/pspc/jobtest2/query",method = RequestMethod.GET)//计量型
	@ResponseBody
	public ResponseData query2(Classify dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
			@RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) throws Exception {
		sampleCalculationService.sampleDataSubgroupCalculation(-1L, -1L);
		return new ResponseData();
	}
	
	@RequestMapping(value = "/pspc/jobtest/sendmail",method = RequestMethod.GET)//邮件发送测试
	@ResponseBody
	public ResponseData sendmail(Classify dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
			@RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) throws Exception {
			ExecutorService exec = new ThreadPoolExecutor(100, 1000, 60L, TimeUnit.SECONDS, new LinkedBlockingQueue<>());
			EntityR entity = new EntityR();
			List<MessageR> messageList = new ArrayList<MessageR>();
			BaseCalcResultVO baseCalcResultVO = new BaseCalcResultVO();
			
			for(int i=0;i<=1;i++) {
				MessageR message = new MessageR();
				//message.setMessageId("'-1,-1,41501,10015,M,KOIL-CPK-TEMP,1'");
				message.setMessageId("-1,-1,41501,10015,M,KOIL-CPK-TEMP,1");
				message.setSiteId(-1L);
				message.setTenantId(-1L);
				message.setGroupCode("KOIL-NP-6-1-79");
				messageList.add(message);
			}
			
			baseCalcResultVO.setMessageList(messageList);
			baseCalcResultVO.setMessageCreationDate(new Date());
			
			
            MessageThread messageThread =  new MessageThread("dsffhf", entity, baseCalcResultVO, interfaceService);
            exec.submit(messageThread);
		return new ResponseData();
	}

}