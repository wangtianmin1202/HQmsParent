package com.hand.hcs.hcs_asl.mapper;

import java.util.List;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.hcs.hcs_asl.dto.Asl;

public interface AslMapper extends Mapper<Asl>{
	
	/**
	 * 合格物料查询
	 * @param asl
	 * @return
	 */
	List<Asl> query(Asl asl);
	
	List<Asl> selectDatas(Asl asl);
	
	List<Asl> IQCControlSelectDatas(Asl asl);
	
	
	/**
	 * sap接口传输用到的查询
	 * @param asl
	 * @return
	 */
	List<Asl> interfaceSelect(Asl asl);
}