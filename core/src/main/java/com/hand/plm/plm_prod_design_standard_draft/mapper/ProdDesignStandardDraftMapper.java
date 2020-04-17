package com.hand.plm.plm_prod_design_standard_draft.mapper;

import java.util.List;

import com.hand.hap.mybatis.common.Mapper;
import com.hand.plm.plm_prod_design_standard_draft.dto.ProdDesignStandardDraft;
import com.hand.plm.plm_prod_design_standard_draft.view.ProdDesignStandardDraftVO;

public interface ProdDesignStandardDraftMapper extends Mapper<ProdDesignStandardDraft> {
	List<ProdDesignStandardDraftVO> queryAll(ProdDesignStandardDraftVO vo);
}