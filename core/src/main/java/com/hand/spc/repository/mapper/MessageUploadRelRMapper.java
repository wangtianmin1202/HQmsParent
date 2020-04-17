package com.hand.spc.repository.mapper;

import java.util.List;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.spc.repository.dto.MessageUploadRelR;

/**
 * Mapper
 *
 * @author peng.hu04@hand-china.com 2019-07-07 11:32:28
 */
public interface MessageUploadRelRMapper extends Mapper<MessageUploadRelR> {
    /**
     * 批量保存消息命令
     *
     * @param messageUploadRelList
     * @return
     */
    public int batchInsertRows(List<MessageUploadRelR> messageUploadRelList);
}
