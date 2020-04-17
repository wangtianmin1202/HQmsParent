package com.hand.hqm.hqm_scrapped_file_after.mapper;

import java.util.List;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.hqm.hqm_scrapped_file_after.dto.ScrappedFileAfter;

public interface ScrappedFileAfterMapper extends Mapper<ScrappedFileAfter>{

	
	/**
	 * 基础查询
	 * @description
	 * @author tianmin.wang
	 * @date 2019年11月22日 
	 * @param dto
	 * @return
	 */
	List<ScrappedFileAfter> reSelect(ScrappedFileAfter dto);

}