package com.hand.plm.plm_prod_design_standard_draft.service.impl;

import com.github.pagehelper.PageHelper;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.service.impl.BaseServiceImpl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hand.plm.plm_prod_design_standard_detail.mapper.ProdDesignStandardDetailMapper;
import com.hand.plm.plm_prod_design_standard_detail.service.IProdDesignStandardDetailService;
import com.hand.plm.plm_prod_design_standard_detail.view.ProdDesignStandardDetailVO;
import com.hand.plm.plm_prod_design_standard_draft.dto.ProdDesignStandardDraft;
import com.hand.plm.plm_prod_design_standard_draft.mapper.ProdDesignStandardDraftMapper;
import com.hand.plm.plm_prod_design_standard_draft.service.IProdDesignStandardDraftService;
import com.hand.plm.plm_prod_design_standard_draft.view.ProdDesignStandardDraftVO;

import org.springframework.transaction.annotation.Transactional;

@Service
public class ProdDesignStandardDraftServiceImpl extends BaseServiceImpl<ProdDesignStandardDraft>
		implements IProdDesignStandardDraftService {

	@Autowired
	private ProdDesignStandardDraftMapper mapper;
	@Autowired
	private IProdDesignStandardDetailService iProdDesignStandardDetailService;

	@Override
	public List<ProdDesignStandardDraftVO> queryNew(IRequest irequest, ProdDesignStandardDraftVO vo, int pageNum,
			int pageSize) {
		// TODO Auto-generated method stub
		PageHelper.startPage(pageNum, pageSize);
		vo.setCreatedBy(irequest.getUserId());
		return mapper.queryAll(vo);
	}

	@Override
	public String checkDetailDataIsExist(IRequest irequest, String detailId) {
		// TODO Auto-generated method stub
		ProdDesignStandardDraft draft = new ProdDesignStandardDraft();
		draft.setDetailId(detailId);

		List<ProdDesignStandardDraft> list = self().select(irequest, draft, 1, 1000);
		if (list != null && list.size() > 0) {
			return list.get(0).getKid();
		} else {
			return "";
		}
	}

	@Override
	public List<ProdDesignStandardDraftVO> updateQuery(IRequest irequest, ProdDesignStandardDraftVO vo) {
		// TODO Auto-generated method stub
		if (vo.getDetailIdList() == null || vo.getDetailIdList().size() == 0) {
			throw new RuntimeException("请选择要更新的规则明细记录！");
		}
		/**
		 * 勾选数据修改的时候，有的明细数据之前已经修改过，存在了草稿表中，则直接去草稿表中的数据；
		 * 有的数据则是没有修改改过的，在草稿表中没有，则需要取明细表里的数据 因此这里分四步取数据， 1.先获取草稿表中的数据 2.获取明细表中的数据
		 * 3，循环草稿表中的数据，将明细ID一致的数据从明细数据的集合中剔除（草稿表中没有数据， 则不会进入循环剔除明细数据） 4.所有集合取并集
		 */

		// 先通过明细ID获取草稿表中已经报错的数据
		List<ProdDesignStandardDraftVO> resultList = mapper.queryAll(vo);
		// 获取草稿表中明细ID集合
		List<String> details = resultList.stream().map(ProdDesignStandardDraftVO::getDetailId)
				.collect(Collectors.toList());

		// 再获取明细记录
		ProdDesignStandardDetailVO detailVO = new ProdDesignStandardDetailVO();
		detailVO.setDetailIdList(vo.getDetailIdList());
		List<ProdDesignStandardDetailVO> detailList = iProdDesignStandardDetailService.queryAll(irequest, detailVO, 1,
				10000);
		for (ProdDesignStandardDetailVO detail : detailList) {
			if (!details.contains(detail.getDetailId())) {
				ProdDesignStandardDraftVO draft = new ProdDesignStandardDraftVO();
				BeanUtils.copyProperties(detail, draft);
				resultList.add(draft);
			}
		}

		return resultList;
	}

}