package com.hand.hqm.hqm_scrapped_file.mapper;

import java.util.List;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.hqm.hqm_scrapped_file.dto.ScrappedFile;

public interface ScrappedFileMapper extends Mapper<ScrappedFile>{
	List<ScrappedFile> myselect(ScrappedFile dto);

}