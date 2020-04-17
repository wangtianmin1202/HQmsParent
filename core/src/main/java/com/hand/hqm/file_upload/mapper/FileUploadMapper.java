package com.hand.hqm.file_upload.mapper;

import java.util.List;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.hqm.file_upload.dto.FileUpload;

public interface FileUploadMapper extends Mapper<FileUpload>{

	/**
	 * @Description:
	 * @param dto
	 * @return
	 */
	List<FileUpload> queryByCondition(FileUpload dto);

}