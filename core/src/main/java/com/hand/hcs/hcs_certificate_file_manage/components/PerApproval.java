package com.hand.hcs.hcs_certificate_file_manage.components;

import com.hand.hap.activiti.custom.IActivitiBean;
import com.hand.hap.activiti.event.TaskRecallListener;
import com.hand.hap.activiti.event.dto.TaskRecallInfo;
import com.hand.hap.core.IRequest;
import com.hand.hcs.hcs_certificate_file_manage.dto.Certificate;
import com.hand.hcs.hcs_certificate_file_manage.service.IPerApproval;
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
 * description 人员认证审批情况处理接口
 *
 * @author KOCE3G3 2020/03/27 2:35 PM
 */
@Component
public class PerApproval implements JavaDelegate, IActivitiBean, TaskRecallListener {

    private Logger logger = LoggerFactory.getLogger(this.getClass());


    @Override
    public String processDefinitionKey() {
        return ActivitiConstants.CERFICATE_WORKFLOW_PER;
    }

    /**
     * 撤审
     * @param iRequest
     * @param taskRecallInfo
     */
    @Override
    public void doRecall(IRequest iRequest, TaskRecallInfo taskRecallInfo) {
        logger.debug("trigger listener");
        try {
            String certificateId = (String) taskRecallInfo.getVariables().get(Certificate.FIELD_CERTIFICATE_ID);
            String certificateType = (String) taskRecallInfo.getVariables().get(Certificate.FIELD_CERTIFICATE_TYPE);
            WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
            String beanName = (String) taskRecallInfo.getVariables().get("beanName");
            IPerApproval service = wac.getBean(beanName, IPerApproval.class);

            service.perApproveResult(Long.parseLong(certificateId), certificateType,"R");
        } catch (ActException e) {
            logger.error("error:{}",e.getMessage());
        }
    }

    @Override
    public void execute(DelegateExecution execution) {
        try{
            WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
            Long certificateId = Long.parseLong(execution.getVariable(Certificate.FIELD_CERTIFICATE_ID, String.class));
            String certificateType = execution.getVariable(Certificate.FIELD_CERTIFICATE_TYPE,String.class);
            String result = execution.getVariable("approveResult", String.class);
            String beanName = execution.getVariable("beanName", String.class);
            IPerApproval service = wac.getBean(beanName, IPerApproval.class);
            if(Objects.isNull(certificateId)){
                throw new RuntimeException("CertificateId is not found");
            }
            service.perApproveResult(certificateId,certificateType, result);
        }catch (ActException e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            throw new RuntimeException(e.getLocalizedMessage());
        }

    }

    @Override
    public String getBeanName() {
        return "PerApproval";
    }
}
