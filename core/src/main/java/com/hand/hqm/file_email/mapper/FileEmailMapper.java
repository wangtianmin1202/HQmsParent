package com.hand.hqm.file_email.mapper;

import java.util.List;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.hqm.file_email.dto.FileEmail;

public interface FileEmailMapper extends Mapper<FileEmail>{

	/**
	 * @Description:
	 * @param dto
	 * @return
	 */
	List<FileEmail> queryByCondition(FileEmail dto);

}