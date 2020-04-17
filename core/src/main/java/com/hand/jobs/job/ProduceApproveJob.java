package com.hand.jobs.job;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.impl.RequestHelper;
import com.hand.hap.job.AbstractJob;
import com.hand.hcs.hcs_produce.dto.ItemStationProduce;
import com.hand.hcs.hcs_produce.mapper.ItemStationProduceMapper;
import com.hand.hcs.hcs_produce.service.IItemStationProduceService;
import org.apache.commons.collections4.CollectionUtils;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * description 制程监控定时审批任务
 *
 * @author KOCE3G3 2020/03/28 2:21 PM
 */
@Transactional(rollbackFor = Exception.class)
public class ProduceApproveJob extends AbstractJob {

    private Logger logger = LoggerFactory.getLogger(ProduceApproveJob.class);

    @Autowired
    private ItemStationProduceMapper produceMapper;

    @Autowired
    private IItemStationProduceService service;

    @Override
    public void safeExecute(JobExecutionContext context) throws Exception {
        logger.info("================"+"制程监控定时审批开始"+"==================");
        //查询存在ng项的制程
        List<ItemStationProduce> listExistNg = produceMapper.listExistNg();
        //查询不存在ng项的制程
        List<ItemStationProduce> listNotExistNg = produceMapper.listNotExistNg();
        try{
            if(CollectionUtils.isNotEmpty(listExistNg)){
                //存在ng项的提交审批
                IRequest request = RequestHelper.newEmptyRequest();
                for (ItemStationProduce produce : listExistNg) {
                    service.approve(request,produce);
                }
            }

            if(CollectionUtils.isNotEmpty(listNotExistNg)){
                //不存在ng项的系统自动审批
                for (ItemStationProduce produce : listNotExistNg) {
                    produce.setStatus("S");
                    produceMapper.updateByPrimaryKey(produce);
                }
            }
        }catch (Exception e){
                logger.error("制程监控审批出错：{}",e.getMessage());
        }
        logger.info("================"+"制程监控定时审批完成"+"==================");

    }
}
