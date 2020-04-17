package com.hand.hcs.hcs_asl_control.mapper;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.hcs.hcs_asl_control.dto.AslControl;

public interface AslControlMapper extends Mapper<AslControl>{

	/**
	 * @description
	 * @author tianmin.wang
	 * @date 2019年11月29日 
	 * @param aslcinsert
	 */
	void insertInterface(AslControl aslcinsert);

}