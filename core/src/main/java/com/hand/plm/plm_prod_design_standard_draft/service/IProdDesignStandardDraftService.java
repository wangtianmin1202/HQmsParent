package com.hand.plm.plm_prod_design_standard_draft.service;

import java.util.List;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;
import com.hand.plm.plm_prod_design_standard_draft.dto.ProdDesignStandardDraft;
import com.hand.plm.plm_prod_design_standard_draft.view.ProdDesignStandardDraftVO;

public interface IProdDesignStandardDraftService
		extends IBaseService<ProdDesignStandardDraft>, ProxySelf<IProdDesignStandardDraftService> {

	/**
	 * 校验规则明细是否已经在草稿表中
	 * 
	 * @param irequest
	 * @param specLineId 产品属性明细ID
	 * @return 如果存在，则返回草稿表KID,不存返回空
	 */
	String checkDetailDataIsExist(IRequest irequest, String detailId);

	List<ProdDesignStandardDraftVO> queryNew(IRequest irequest, ProdDesignStandardDraftVO vo, int pageNum,
			int pageSize);

	List<ProdDesignStandardDraftVO> updateQuery(IRequest irequest, ProdDesignStandardDraftVO vo);
}