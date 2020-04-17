package com.hand.hqm.file_manager.mapper;

import java.util.List;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.hqm.file_manager.dto.Manager;
import com.hand.hqm.hqm_dfmea_detail.dto.DfmeaDetail;

public interface ManagerMapper extends Mapper<Manager>{
	  /**
		 
		 * @Description 查询
		 * @Date 18:10 2019/8/19
		 * @Param dto 
		 */
	 List<Manager> myselect(Manager dto);

	/**
	 * @Description: 查询出所有有过期时间的文件
	 * @return list
	 */
	List<Manager> selectAllFile();

	/**
	 * @Description:
	 * @param manager
	 * @return
	 */
	List<Manager> queryFileUpload(Manager manager);

	/**
	 * @Description:
	 */
	Float selectMaxFileId();
}