package com.hand.hcs.hcs_barcode.mapper;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hcm.hcm_item.dto.Item;
import com.hand.hcs.hcs_barcode.dto.SmallBarcode;

public interface SmallBarcodeMapper extends Mapper<SmallBarcode>{
	/**
	 * 查询
	 * @param deliveryTicketH
	 * @return
	 */
	List<SmallBarcode> query(SmallBarcode smallBarcode);
	
	ResponseData changesmall(String enableFlag, HttpServletRequest request);

	/**
	 * 关联小箱条码
	 * @param smallBarcode
	 * @return
	 */
	List<SmallBarcode> querySmallBarcode(SmallBarcode smallBarcode);

	
	String selectitems(Item item);
	/**
	 * 打印外箱条码查询
	 * @param smallBarcode
	 * @return
	 */
	List<SmallBarcode> printQuery(SmallBarcode smallBarcode);
	/**
	 * 获取最大流水号
	 * @param smallBarcode
	 * @return
	 */
	Integer selectMaxNum(SmallBarcode smallBarcode);
}