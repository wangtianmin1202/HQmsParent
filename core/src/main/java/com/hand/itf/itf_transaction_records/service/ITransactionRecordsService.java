package com.hand.itf.itf_transaction_records.service;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;
import com.hand.itf.itf_transaction_records.dto.TransactionRecords;

import java.util.List;
import java.util.Map;

public interface ITransactionRecordsService extends IBaseService<TransactionRecords>, ProxySelf<ITransactionRecordsService>{

    /**
     * 查询接口履历数据
     * @param t
     * @return List集合
     */
    public List<TransactionRecords> queryTransactionRecords(IRequest request, TransactionRecords t, int pageNum, int pageSize);

    /**
     * 重传
     * @param request
     * @param id
     */
    public void transactionResend(IRequest request,float id,int page,int pageSize);

    /**z
     * 明细
     * @param request
     * @param id
     */
    public String transactionDetail(IRequest request, float id);

}