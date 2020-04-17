package com.hand.plm.plm_prod_design_standard_detail.service;

import java.util.List;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;
import com.hand.plm.plm_prod_design_standard_detail.dto.ProdDesignStandardDetail;
import com.hand.plm.plm_prod_design_standard_detail.view.ProdDesignStandardDetailVO;
import com.hand.plm.plm_prod_design_standard_draft.dto.ProdDesignStandardDraft;

public interface IProdDesignStandardDetailService
		extends IBaseService<ProdDesignStandardDetail>, ProxySelf<IProdDesignStandardDetailService> {

	/**
	 * 查询明细
	 * 
	 * @param iRequest
	 * @param vo
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	List<ProdDesignStandardDetailVO> queryAll(IRequest iRequest, ProdDesignStandardDetailVO vo, int pageNum,
			int pageSize);

	/**
	 * 作废
	 * 
	 * @param iRequest
	 * @param detailIdList 规则明细ID集合
	 */
	List<ProdDesignStandardDraft> invalid(IRequest iRequest, List<String> detailIdList);

	/**
	 * 生效
	 * 
	 * @param iRequest
	 * @param detailIdList 规则明细ID集合
	 * @return
	 */
	List<ProdDesignStandardDraft> effective(IRequest iRequest, List<String> detailIdList);
}