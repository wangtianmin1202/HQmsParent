package com.hand.hcs.hcs_doc_statement.mapper;

import java.util.List;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.hcs.hcs_doc_statement.dto.DocStatement;

public interface DocStatementMapper extends Mapper<DocStatement>{
	/**
	 * 查询今年最大流水号
	 * @param dto
	 * @return
	 */
	Integer queryMaxNum();
	/**
	 * 对账单确认 查询
	 * @param dto
	 * @return
	 */
	List<DocStatement> query(DocStatement dto);
	/**
	 * 新建对账单查询
	 * @param dto 对账单信息
	 * @return 对账单集合
	 */
	List<DocStatement> queryAddInto(DocStatement dto);
}