package com.hand.dimension.hqm_dimension_upload_files.mapper;

import com.hand.hap.mybatis.common.Mapper;

import java.util.List;

import com.hand.dimension.hqm_dimension_upload_files.dto.DimensionUploadFiles;

public interface DimensionUploadFilesMapper extends Mapper<DimensionUploadFiles>{
	List<DimensionUploadFiles> reSelect(DimensionUploadFiles dto);
}