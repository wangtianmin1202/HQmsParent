package com.hand.hcs.hcs_certificate_file_manage.service;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;
import com.hand.hcs.hcs_certificate_file_manage.dto.CertificateHistory;

import java.util.List;

public interface ICertificateHistoryService extends IBaseService<CertificateHistory>, ProxySelf<ICertificateHistoryService>{

    List<CertificateHistory> listQuery(IRequest request,CertificateHistory dto,int page,int pageSize);
}