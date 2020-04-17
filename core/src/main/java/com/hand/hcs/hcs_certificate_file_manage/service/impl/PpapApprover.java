package com.hand.hcs.hcs_certificate_file_manage.service.impl;

import com.hand.hap.activiti.custom.IActivitiBean;
import com.hand.hcm.hcm_category_settings.mapper.ItemCategorySettingsMapper;
import com.hand.hcs.hcs_certificate_file_manage.dto.Certificate;
import com.hand.hcs.hcs_certificate_file_manage.mapper.CertificateMapper;
import org.activiti.engine.delegate.DelegateExecution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * description 获取指定人审批
 *
 * @author KOCDZX0 2020/03/25 1:14 PM
 */
@Service
public class PpapApprover implements IActivitiBean {

    @Autowired
    private CertificateMapper certificateMapper;

    @Autowired
    private ItemCategorySettingsMapper settingsMapper;

    private final Logger logger = LoggerFactory.getLogger(PpapApprover.class);

    /**
     * 获取研发工程师
     * @param execution
     * @return
     */
    public List<String> getRDengineer(DelegateExecution execution){

        String processInstanceBusinessKey = execution.getProcessInstanceBusinessKey();
        String businessKey = processInstanceBusinessKey.split(":")[0];
        Long certificateId = Long.valueOf(businessKey);
        Certificate certificate = certificateMapper.selectByPrimaryKey(certificateId);
        List<String> dengineer = certificateMapper.getRDengineer(certificate.getItemId());
        return dengineer;
    }

    /**
     * 获取有效SQE人员
     * @param execution
     * @return
     */
    public List<String> getSQE(DelegateExecution execution){
        String processInstanceBusinessKey = execution.getProcessInstanceBusinessKey();
        String businessKey = processInstanceBusinessKey.split(":")[0];
        Long certificateId = Long.valueOf(businessKey);
        Certificate certificate = certificateMapper.selectByPrimaryKey(certificateId);
        List<String> sqe = certificateMapper.getSQE(certificate.getSupplierId());
        return sqe;
    }


    @Override
    public String getBeanName() {
        return "PpapApprover";

    }
}
