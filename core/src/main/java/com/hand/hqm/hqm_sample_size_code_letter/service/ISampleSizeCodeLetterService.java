package com.hand.hqm.hqm_sample_size_code_letter.service;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;
import com.hand.hqm.hqm_sample_size_code_letter.dto.SampleSizeCodeLetter;

import java.util.List;

public interface ISampleSizeCodeLetterService extends IBaseService<SampleSizeCodeLetter>, ProxySelf<ISampleSizeCodeLetterService>{

    List<SampleSizeCodeLetter> myselect(IRequest requestContext, SampleSizeCodeLetter dto, int page, int pageSize);

    List<SampleSizeCodeLetter> LevelUpdate(IRequest requestCtx, List<SampleSizeCodeLetter> dto);

	List<SampleSizeCodeLetter> selectSandardCode();

	List<SampleSizeCodeLetter> selectCodeLevel(SampleSizeCodeLetter dto);
}