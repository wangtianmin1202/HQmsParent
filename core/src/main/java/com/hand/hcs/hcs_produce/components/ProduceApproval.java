package com.hand.hcs.hcs_produce.components;

import com.hand.hap.activiti.custom.IActivitiBean;
import com.hand.hap.activiti.event.TaskRecallListener;
import com.hand.hap.activiti.event.dto.TaskRecallInfo;
import com.hand.hap.core.IRequest;
import com.hand.hcs.hcs_produce.dto.ItemStationProduce;
import com.hand.hcs.hcs_produce.service.IProduceApproval;
import com.hand.wfl.util.ActException;
import com.hand.wfl.util.ActivitiConstants;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import java.util.Objects;

/**
 * description 制程监控审批处理接口
 *
 * @author KOCE3G3 2020/03/28 1:31 PM
 */
@Component
public class ProduceApproval implements IActivitiBean, JavaDelegate, TaskRecallListener {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public String processDefinitionKey() {
        return ActivitiConstants.SUPPLIER_WORKFLOW_PRODUCE;
    }

    /**
     * 撤审监听
     * @param request
     * @param taskRecallInfo
     */
    @Override
    public void doRecall(IRequest request, TaskRecallInfo taskRecallInfo) {
        logger.debug("trigger listener");
        try {
            String produceId = (String) taskRecallInfo.getVariables().get(ItemStationProduce.FIELD_PRODUCE_ID);
            String produceNumber = (String) taskRecallInfo.getVariables().get(ItemStationProduce.FIELD_PRODUCE_NUMBER);
            WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
            String beanName = (String) taskRecallInfo.getVariables().get("beanName");
            IProduceApproval service = wac.getBean(beanName, IProduceApproval.class);

            service.produceApproveResult(Long.parseLong(produceId),"N");
        } catch (ActException e) {
            logger.error("error:{}",e.getMessage());
        }
    }

    @Override
    public void execute(DelegateExecution execution) {
        try{
            WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
            Long produceId = Long.parseLong(execution.getVariable(ItemStationProduce.FIELD_PRODUCE_ID, String.class));
            String result = execution.getVariable("approveResult", String.class);
            String beanName = execution.getVariable("beanName", String.class);
            IProduceApproval service = wac.getBean(beanName, IProduceApproval.class);
            if(Objects.isNull(produceId)){
                throw new RuntimeException("produceId is not found");
            }
            service.produceApproveResult(produceId,result);
        }catch (ActException e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            throw new RuntimeException(e.getLocalizedMessage());
        }
    }

    @Override
    public String getBeanName() {
        return "ProduceApproval";
    }
}
