package com.hand.hcs.hcs_po_lines.mapper;

import java.util.List;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.hcs.hcs_po_lines.dto.PoLines;

public interface PoLinesMapper extends Mapper<PoLines>{
	
	/**
	 * 查询明细信息
	 * @param poLines
	 * @return
	 */
	List<PoLines> query(PoLines poLines);
	
	List<PoLines> selectCanShipQty(PoLines poLines);
	
	List<PoLines> selectCanShipData(PoLines poLines);
}