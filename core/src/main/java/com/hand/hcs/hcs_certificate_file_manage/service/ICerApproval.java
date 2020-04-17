package com.hand.hcs.hcs_certificate_file_manage.service;

import com.hand.wfl.util.ActException;

/**
 * description
 *
 * @author KOCE3G3 2020/03/27 5:43 PM
 */
public interface ICerApproval {

    /**
     * 证书审批处理接口
     * @param id
     * @param certificateType
     * @param approveResult
     * @throws ActException
     */
    void cerApproveResult(Long id,String certificateType, String approveResult) throws ActException;

}
