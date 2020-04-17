package com.hand.hcs.hcs_produce.service;

import com.hand.wfl.util.ActException;

/**
 * description 制成监控审批处理接口
 *
 * @author KOCE3G3 2020/03/28 1:30 PM
 */
public interface IProduceApproval {

    void produceApproveResult(Long id,String approveResult) throws ActException;

}
