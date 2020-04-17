package com.hand.plm.laboratory.mapper;

import java.util.List;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.plm.laboratory.dto.LabUser;

public interface LabUserMapper extends Mapper<LabUser>{
	
	List<LabUser> getUserInfo();
	
	List<LabUser> getExcelUserDatas();

}