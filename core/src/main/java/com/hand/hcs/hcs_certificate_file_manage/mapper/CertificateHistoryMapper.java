package com.hand.hcs.hcs_certificate_file_manage.mapper;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.hcs.hcs_certificate_file_manage.dto.CertificateHistory;

import java.util.List;

public interface CertificateHistoryMapper extends Mapper<CertificateHistory>{


    List<CertificateHistory> hisQuery(CertificateHistory dto);
}