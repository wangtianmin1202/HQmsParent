package com.hand.hqm.hqm_sample.mapper;

import java.util.List;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.hqm.hqm_sample.dto.Sample;

public interface SampleMapper extends Mapper<Sample>{
	
 /**
  * 
  * @description
  * @author tianmin.wang
  * @date 2019年11月22日 
  * @param dto
  * @return
  */
 List<Sample> myselect(Sample dto);
 
 
 List<Sample> selectforuse(Sample dto);
}