package com.hand.hqm.file_type.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.hqm.file_type.dto.FileType;
import com.hand.wfl.util.DropDownListDto;

public interface FileTypeMapper extends Mapper<FileType>{

	/**
	 * @Description:
	 * @param dto
	 * @return
	 */
	List<FileType> queryByCondition(FileType dto);

	/**
	 * @Description:
	 * @param fileTypeName
	 * @return
	 */
	List<DropDownListDto> queryFileTypeName(@Param("fileTypeName") String fileTypeName);

}