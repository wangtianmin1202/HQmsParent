package com.hand.hqm.hqm_use_detail_after.mapper;

import java.util.List;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.hqm.hqm_use_detail_after.dto.UseDetailAfter;

public interface UseDetailAfterMapper extends Mapper<UseDetailAfter>{

	List<UseDetailAfter> reSelect(UseDetailAfter dto);

}