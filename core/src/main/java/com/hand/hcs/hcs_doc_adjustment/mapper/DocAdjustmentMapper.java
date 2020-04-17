package com.hand.hcs.hcs_doc_adjustment.mapper;

import java.util.List;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.hcs.hcs_doc_adjustment.dto.DocAdjustment;

public interface DocAdjustmentMapper extends Mapper<DocAdjustment>{
	/**
	 * 查询最大行号
	 * @param docAdjustment
	 * @return
	 */
	Integer queryMaxNumByInvoicId(DocAdjustment docAdjustment);
	/**
	 * 调整行明细查询
	 * @param docAdjustment
	 * @return
	 */
	List<DocAdjustment> query(DocAdjustment docAdjustment);
}