package com.hand.hcs.hcs_out_barcode.mapper;

import java.util.List;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.hcs.hcs_out_barcode.dto.OutBarcode;

public interface OutBarcodeMapper extends Mapper<OutBarcode>{
	/**
	 * 外箱条码查询
	 * @param outBarcode
	 * @return
	 */
	List<OutBarcode> query(OutBarcode outBarcode);
	/**
	 * 查询供应商当天最大流水号
	 * @param outBarcode
	 * @return
	 */
	Integer selectMaxNum(OutBarcode outBarcode);
	/**
	 * 外箱条码打印查询 外箱信息
	 * @param outBarcode
	 * @return
	 */
	List<OutBarcode> printQueryOutCode(OutBarcode outBarcode);
	/**
	 * 绑定查询
	 * @param outBarcode
	 * @return
	 */
	List<OutBarcode> bindQuery(OutBarcode outBarcode);
	/**
	 * 查询对应类型当天最大标签编码 
	 * @param outBarcode
	 * @return
	 */
	Integer selectMaxObarcode(OutBarcode outBarcode);
	/**
	 * 查询对应类型当天最大标签编码
	 * @param outBarcode
	 * @return
	 */
	Integer selectMaxObarcodeNum(OutBarcode outBarcode);
	/**
	 * 容器绑定查询
	 * @param outBarcode
	 * @return
	 */
	List<OutBarcode> outBindQuery(OutBarcode outBarcode);
	/**
	 * 托盘详情
	 * @param outBarcode
	 * @return
	 */
	List<OutBarcode> tpQuery(OutBarcode outBarcode);
	/**
	 * 箱子详情
	 * @param outBarcode
	 * @return
	 */
	List<OutBarcode> xzQuery(OutBarcode outBarcode);
	/**
	 * 标签类型为不为WL
	 * @param outBarcode
	 * @return
	 */
	List<OutBarcode> unWlQuery(OutBarcode outBarcode);
	/**
	 * 标签类型等于WL
	 * @param outBarcode
	 * @return
	 */
	List<OutBarcode> wlQuery(OutBarcode outBarcode);
	/**
	 * 已绑定标签查询
	 * @param outBarcode
	 * @return
	 */
	List<OutBarcode> queryRight(OutBarcode outBarcode);
}