package com.hand.hcs.hcs_certificate_file_manage.service.impl;

import com.github.pagehelper.PageHelper;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import com.hand.hcs.hcs_certificate_file_manage.mapper.CertificateHistoryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.hand.hcs.hcs_certificate_file_manage.dto.CertificateHistory;
import com.hand.hcs.hcs_certificate_file_manage.service.ICertificateHistoryService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class CertificateHistoryServiceImpl extends BaseServiceImpl<CertificateHistory> implements ICertificateHistoryService{

    @Autowired
    private CertificateHistoryMapper historyMapper;

    @Override
    public List<CertificateHistory> listQuery(IRequest request, CertificateHistory dto,int page,int pageSize) {
        PageHelper.startPage(page,pageSize);
        return historyMapper.hisQuery(dto);
    }
}