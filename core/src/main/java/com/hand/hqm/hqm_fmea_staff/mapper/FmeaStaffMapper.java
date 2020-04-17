package com.hand.hqm.hqm_fmea_staff.mapper;

import java.util.List;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.hqm.hqm_fmea.dto.Fmea;
import com.hand.hqm.hqm_fmea_staff.dto.FmeaStaff;

public interface FmeaStaffMapper extends Mapper<FmeaStaff>{
	 List<FmeaStaff> myselect(FmeaStaff dto);
}