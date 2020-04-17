package com.hand.hqm.hqm_measuretool_file.mapper;

import java.util.List;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.hqm.hqm_measuretool_file.dto.MeasuretoolFile;

public interface MeasuretoolFileMapper extends Mapper<MeasuretoolFile>{
	/**
	 * 文件 查询
	 * @param dto
	 * @return
	 */
	List<MeasuretoolFile> query(MeasuretoolFile dto);
}