package com.hand.itf.itf_transaction_records.mapper;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.itf.itf_transaction_records.dto.TransactionRecords;

import java.util.List;

public interface TransactionRecordsMapper extends Mapper<TransactionRecords>{

    /**
     * 查询接口履历数据
     * @param t
     * @return List集合
     */
    public List<TransactionRecords> queryTransactionRecords(TransactionRecords t);

}