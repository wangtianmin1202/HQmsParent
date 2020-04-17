package com.hand.hqm.hqm_scrapped_detail.mapper;

import java.util.List;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.hqm.hqm_scrapped_detail.dto.ScrappedDetail;

public interface ScrappedDetailMapper extends Mapper<ScrappedDetail>{
	List<ScrappedDetail> myselect(ScrappedDetail dto);
	List<ScrappedDetail> selectforcs(ScrappedDetail dto);
	List<ScrappedDetail> selectforsample(ScrappedDetail dto);

}