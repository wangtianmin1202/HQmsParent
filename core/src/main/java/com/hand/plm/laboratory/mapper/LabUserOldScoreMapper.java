package com.hand.plm.laboratory.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.plm.laboratory.dto.LabUserOldScore;

public interface LabUserOldScoreMapper extends Mapper<LabUserOldScore>{
	
//	 List<LabUserOldScore> query(@Param("labUserId")Float labUserId);
	
	 List<LabUserOldScore> selOldScore(@Param("labUserId") Float labUserId);

}