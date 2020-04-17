package com.hand.hqm.file_permission.mapper;

import java.util.List;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.hqm.file_permission.dto.Permission;
import com.hand.hqm.hqm_fmea.dto.Fmea;

public interface PermissionMapper extends Mapper<Permission>{
	 List<Permission> myselect(Permission dto);
	 List<Permission> myselectline(Permission dto);
	 
	 List<Permission> selectlcheckone(Permission dto);
	 List<Permission> selectlchecktwo(Permission dto);
	 List<Permission> selectlcheckthree(Permission dto);
	 
	 List<Permission> checkUserRole(Permission dto);
	 List<Permission> checkcount(Permission dto);
	 List<Permission> checkcountwithNull(Permission dto);
}