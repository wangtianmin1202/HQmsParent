package com.hand.hqm.hqm_sample_size_code_letter.mapper;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.hqm.hqm_sample_size_code_letter.dto.SampleSizeCodeLetter;

import java.util.List;

public interface SampleSizeCodeLetterMapper extends Mapper<SampleSizeCodeLetter>{

    List<SampleSizeCodeLetter> myselect(SampleSizeCodeLetter dto);

	List<SampleSizeCodeLetter> selectSandardCode();

	List<SampleSizeCodeLetter> selectCodeLevel(SampleSizeCodeLetter dto);
	
	List<SampleSizeCodeLetter> selectCodeSizeLetter(SampleSizeCodeLetter dto);
}