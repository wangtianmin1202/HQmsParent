package com.hand.hcs.hcs_certificate_file_manage.service;

import com.hand.wfl.util.ActException;

/**
 * description 证书审批情况处理接口
 *
 * @author KOCE3G3 2020/03/27 2:34 PM
 */
public interface IPerApproval {

    /**
     * 人员认证审批结果处理
     * @param id
     * @param certificateType 证书类型
     * @param approveResult 审批结果
     * @throws ActException
     */
    void perApproveResult(Long id,String certificateType, String approveResult) throws ActException;

}
