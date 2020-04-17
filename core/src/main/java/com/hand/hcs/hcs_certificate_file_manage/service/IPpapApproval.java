package com.hand.hcs.hcs_certificate_file_manage.service;

import com.hand.wfl.util.ActException;

/**
 * description ppap审批情况处理接口
 *
 * @author KOCE3G3 2020/03/26 11:23 AM
 */
public interface IPpapApproval {

    /**
     * 单据id，证书类型
     * @param id
     * @param approveResult
     * @param certificateType
     */
    void ppapApproveResult(Long id,String certificateType, String approveResult) throws ActException;
}
