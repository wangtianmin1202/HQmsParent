package com.hand.hqm.hqm_qc_files.mapper;

import java.util.List;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.hqm.hqm_qc_files.dto.QcFiles;

public interface QcFilesMapper extends Mapper<QcFiles>{
	/**
	 * 质量文件管理 查询
	 * @param dto
	 * @return
	 */
	List<QcFiles> query(QcFiles dto);
}