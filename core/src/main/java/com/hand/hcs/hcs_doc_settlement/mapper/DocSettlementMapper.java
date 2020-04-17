package com.hand.hcs.hcs_doc_settlement.mapper;

import java.util.List;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.hcs.hcs_doc_settlement.dto.DocSettlement;

public interface DocSettlementMapper extends Mapper<DocSettlement>{
	
	/**
	 * 对账单创建  查询
	 * @param dto
	 * @return
	 */
	List<DocSettlement> query(DocSettlement dto);
	/**
	 * 对账单明细
	 * @param dto
	 * @return
	 */
	List<DocSettlement> queryDetail(DocSettlement dto);
	/**
	 * 清空对账单id
	 * @param dto
	 */
	void updateDocStatementId(DocSettlement dto);
	/**
	 * 清空网上发票id
	 * @param dto
	 */
	void updateWebInvoiceId(DocSettlement dto);
	/**
	 * 获取最大流水号
	 * @return
	 */
	Integer queryMaxNum(DocSettlement dto);
	/**
	 * 发票明细预览：获取结算单据类型D、R
	 * @param dto 结算单据
	 * @return 获取数据集合
	 */
	List<DocSettlement> printQueryDR(DocSettlement dto);
	/**
	 * 发票明细预览：获取结算单据类型Q
	 * @param dto
	 * @return
	 */
	List<DocSettlement> printQueryQ(DocSettlement dto);
	/**
	 * 发票明细预览：获取调整行
	 * @param dto
	 * @return
	 */
	List<DocSettlement> printQueryAdjustment(DocSettlement dto);
}