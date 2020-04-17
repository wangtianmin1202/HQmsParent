package com.hand.hcs.hcs_po_headers.mapper;

import java.util.List;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.hcs.hcs_po_headers.dto.PoHeaders;

public interface PoHeadersMapper extends Mapper<PoHeaders>{
	/**
	 * userType为S是查询 业务实体、库存组织、供应商、供应商地点
	 * @param poHeaders
	 * @return
	 */
	List<PoHeaders> queryUtilS(PoHeaders poHeaders);
	/**
	 * userType不为S是查询 业务实体、库存组织、供应商、供应商地点
	 * @param poHeaders
	 * @return
	 */
	List<PoHeaders> queryUtilNoS(PoHeaders poHeaders);
	/**
	 *  采购订单头查询
	 * @param poHeaders
	 * @return
	 */
	List<PoHeaders> query(PoHeaders poHeaders);
	
	Float selectPlant(String par1,String par2);
	/**
	 * 行的有效数量
	 * @param string
	 * @return
	 */
	Float getNoStatusCount(Float headid);
}