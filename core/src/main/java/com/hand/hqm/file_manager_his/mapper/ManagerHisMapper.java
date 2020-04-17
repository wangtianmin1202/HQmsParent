package com.hand.hqm.file_manager_his.mapper;

import java.util.List;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.hqm.file_manager.dto.Manager;
import com.hand.hqm.file_manager_his.dto.ManagerHis;

public interface ManagerHisMapper extends Mapper<ManagerHis>{
	 List<ManagerHis> myselect(ManagerHis dto);
}