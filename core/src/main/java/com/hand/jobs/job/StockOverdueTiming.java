package com.hand.jobs.job;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.codehaus.jackson.map.ObjectMapper;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;

import com.hand.hap.core.impl.ServiceRequest;
import com.hand.hap.job.AbstractJob;
import com.hand.hap.system.service.ICodeService;
import com.hand.hap.task.info.ExecutionInfo;
import com.hand.hap.task.service.ITask;
import com.hand.hap.util.SoapPostUtil;
import com.hand.hcm.hcm_item.dto.Item;
import com.hand.hcm.hcm_item.mapper.ItemMapper;
import com.hand.hcm.hcm_plant.dto.Plant;
import com.hand.hcm.hcm_plant.mapper.PlantMapper;
import com.hand.hqm.hqm_item_control.dto.ItemControlQms;
import com.hand.hqm.hqm_item_control.dto.StockOverdueInfo;
import com.hand.hqm.hqm_item_control.mapper.ItemControlQmsMapper;
import com.hand.sys.sys_if_invoke_outbound.dto.IfInvokeOutbound;
import com.hand.sys.sys_if_invoke_outbound.mapper.IfInvokeOutboundMapper;

/**
 * @author kai.li
 * @version date：2020年2月26日12:14:26 OQC库存超期基础数据-定时任务 每天早上8点
 */
public class StockOverdueTiming extends AbstractJob implements ITask  {
	
	@Autowired
	ItemControlQmsMapper mapper;
	@Autowired
	PlantMapper plantMapper;
	@Autowired
	ItemMapper itemMapper;
	@Resource(name = "taskExecutor")
	private TaskExecutor taskExecutor;
	@Autowired
	ICodeService iCodeService;
	@Autowired
	IfInvokeOutboundMapper ifInvokeOutboundMapper;
	
	Logger logger = LoggerFactory.getLogger(StockOverdueTiming.class);


	@Override
	public void execute(ExecutionInfo executionInfo) throws Exception {
		self();
	}

	@Override
	public void safeExecute(JobExecutionContext context) throws Exception {
		self();
	}

	public void self() {
		try {
			IfInvokeOutbound iio = new IfInvokeOutbound();
			List<StockOverdueInfo> stockOverdueInfos = new ArrayList<StockOverdueInfo>();
			//检索HQM_ITEM_CONTROL数据
			List<ItemControlQms> itemControlQmss = mapper.selectAll();
			for(ItemControlQms icq:itemControlQmss) {
				StockOverdueInfo soi = new StockOverdueInfo();
				//关联工厂表查询
				Plant plant = new Plant();
				plant.setPlantId(icq.getPlantId());
				List<Plant> plantList = plantMapper.select(plant);
				if (plantList != null && plantList.size() > 0) {
					soi.setPlantCode(plantList.get(0).getPlantCode());
				} else {
					//工厂编码在工厂表中找不到！
					logger.warn("工厂编码在工厂表中找不到！");
					return;
				}
				
				//关联物料表查询
				Item item = new Item();
				item.setItemId(icq.getItemId());
				List<Item> itemList = itemMapper.select(item);
				if (itemList != null && itemList.size() > 0) {
					soi.setItemCode(itemList.get(0).getItemCode());
				} else {
					//物料编码在物料表中找不到！
					logger.warn("物料编码在物料表中找不到！");
					return;
				}
				
				soi.setWarningDate(icq.getRecheckCycle());
				stockOverdueInfos.add(soi);
			}
			
			taskExecutor.execute(new Runnable() {
				@Override
				public void run() {
					// 转为JSON String发送
					ObjectMapper omapper = new ObjectMapper();
					String post;
					try {
						ServiceRequest sr = new ServiceRequest();
						sr.setLocale("zh_CN");
						String uri = iCodeService.getCodeValueByMeaning(sr, "HAP.SYSTEM", "WMS_WS_URI");// 获取调用地址
						post = omapper.writeValueAsString(stockOverdueInfos);
						SoapPostUtil.Response re = SoapPostUtil.ticketSrmToWms("getOqcBaiscDate",post, iio, uri);
						ifInvokeOutboundMapper.insertSelective(iio);
						logger.info(SoapPostUtil.getStringFromResponse(re));
					} catch (Exception e) {
						iio.setResponseContent(e.getMessage());
						iio.setResponseCode("E");
						ifInvokeOutboundMapper.insertSelective(iio);
						logger.warn(e.getMessage());
					}
				}
			});
		} catch (Exception e) {
			logger.warn("发送第三方发生错误:" + e.getMessage());
		}
	}
}
