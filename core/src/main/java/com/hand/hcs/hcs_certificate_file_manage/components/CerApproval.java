package com.hand.hcs.hcs_certificate_file_manage.components;

import com.hand.hap.activiti.custom.IActivitiBean;
import com.hand.hap.activiti.event.dto.TaskRecallInfo;
import com.hand.hap.core.IRequest;
import com.hand.hcs.hcs_certificate_file_manage.dto.Certificate;
import com.hand.hcs.hcs_certificate_file_manage.mapper.CertificateMapper;
import com.hand.hcs.hcs_certificate_file_manage.service.ICerApproval;
import com.hand.wfl.util.ActException;
import com.hand.wfl.util.ActivitiConstants;
import com.hand.wfl.util.TaskRecallListener;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;
import java.util.Objects;

/**
 * description 证书审批处理接口
 *
 * @author KOCE3G3 2020/03/27 5:44 PM
 */
@Component
public class CerApproval implements JavaDelegate, IActivitiBean, TaskRecallListener {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private CertificateMapper certificateMapper;

    @Override
    public String processDefinitionKey() {
        return ActivitiConstants.CERFICATE_WORKFLOW_CER;
    }

    @Override
    public void doRecall(IRequest request, TaskRecallInfo taskRecallInfo) {
        logger.debug("trigger listener");
        try {
            String certificateId = (String) taskRecallInfo.getVariables().get(Certificate.FIELD_CERTIFICATE_ID);
            String certificateType = (String) taskRecallInfo.getVariables().get(Certificate.FIELD_CERTIFICATE_TYPE);
            WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
            String beanName = (String) taskRecallInfo.getVariables().get("beanName");
            ICerApproval service = wac.getBean(beanName, ICerApproval.class);

            service.cerApproveResult(Long.parseLong(certificateId), certificateType,"P");
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
            ICerApproval service = wac.getBean(beanName, ICerApproval.class);
            if(Objects.isNull(certificateId)){
                throw new RuntimeException("CertificateId is not found");
            }
            service.cerApproveResult(certificateId,certificateType, result);
        }catch (ActException e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            throw new RuntimeException(e.getLocalizedMessage());
        }
    }

    /**
     * 是否为第一次提交审批
     * @param delegateExecution
     */
    public void isFirstSumit(DelegateExecution delegateExecution){
        Long certificateId = Long.parseLong(delegateExecution.getVariable(Certificate.FIELD_CERTIFICATE_ID, String.class));
        Certificate certificate = new Certificate();
        certificate.setApprovalStatus("A");
        certificate.setCertificateId(certificateId);
        List<Certificate> select = certificateMapper.select(certificate);
        if(CollectionUtils.isEmpty(select)){
            delegateExecution.setVariable("isFirstSubmit","Y");
        }else {
            delegateExecution.setVariable("isFirstSubmit","N");
        }
    }

    @Override
    public String getBeanName() {
        return "CerApproval";
    }
}
