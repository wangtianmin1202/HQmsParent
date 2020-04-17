package com.hand.plm.plm_prod_design_standard_detail.service.impl;

import com.github.pagehelper.PageHelper;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.service.impl.BaseServiceImpl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hand.plm.plm_prod_design_standard_approve.dto.ProdDesignStandardApprove;
import com.hand.plm.plm_prod_design_standard_approve.service.IProdDesignStandardApproveService;
import com.hand.plm.plm_prod_design_standard_detail.dto.ProdDesignStandardDetail;
import com.hand.plm.plm_prod_design_standard_detail.mapper.ProdDesignStandardDetailMapper;
import com.hand.plm.plm_prod_design_standard_detail.service.IProdDesignStandardDetailService;
import com.hand.plm.plm_prod_design_standard_detail.view.ProdDesignStandardDetailVO;
import com.hand.plm.plm_prod_design_standard_draft.dto.ProdDesignStandardDraft;
import com.hand.plm.plm_prod_design_standard_draft.service.IProdDesignStandardDraftService;
import com.hand.plm.plm_prod_design_standard_draft.view.ProdDesignStandardDraftVO;
import com.hand.plm.utils.Util;

import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class ProdDesignStandardDetailServiceImpl extends BaseServiceImpl<ProdDesignStandardDetail>
		implements IProdDesignStandardDetailService {

	@Autowired
	private ProdDesignStandardDetailMapper mapper;
	@Autowired
	private IProdDesignStandardDraftService iProdDesignStandardDraftService;
	@Autowired
	private IProdDesignStandardApproveService iProdDesignStandardApproveService;

	@Override
	public List<ProdDesignStandardDetailVO> queryAll(IRequest iRequest, ProdDesignStandardDetailVO vo, int pageNum,
			int pageSize) {
		// TODO Auto-generated method stub
		PageHelper.startPage(pageNum, pageSize);
		List<ProdDesignStandardDetailVO> list = mapper.queryAll(vo);
		List<String> tempList = new ArrayList<>();
		for (ProdDesignStandardDetailVO detailVO : list) {
			tempList = Arrays.asList(detailVO.getName().split(";"));
			if (tempList != null && tempList.size() > 0) {
				detailVO.setStructureModule(tempList.get(2));
				detailVO.setPartName(tempList.get(3));
			}

			Util.nullConverNullString(detailVO);
		}
		return list;
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public List<ProdDesignStandardDraft> invalid(IRequest iRequest, List<String> detailIdList) {
		// TODO Auto-generated method stub
		if (detailIdList == null || detailIdList.size() == 0) {
			throw new RuntimeException("请选择要废止的数据！");
		}

		List<ProdDesignStandardDraft> result = new ArrayList<>();
		for (String detailId : detailIdList) {
			// 获取明细数据
			ProdDesignStandardDetail detail = new ProdDesignStandardDetail();
			detail.setDetailId(detailId);
			detail = mapper.selectByPrimaryKey(detail);
			if (detail == null) {
				throw new RuntimeException("明细数据不存在，请刷新数据之后再操作！");
			}
			if ("INVALID".equals(detail.getStatus())) {
				throw new RuntimeException("规则明细内容：" + detail.getDesignStandardDescription() + "，状态已经废止，请检查数据！");
			}

			// 检验如果当前明细已经进入了废止的审批流中，报错
			Long count = mapper.getInvalidDetailCount(detailId);
			if (count > 0) {
				throw new RuntimeException(
						"规则明细内容：" + detail.getDesignStandardDescription() + "，已经进入废止审批流，请在【产品结构与设计规范审批】功能中查看！");
			}

			ProdDesignStandardApprove approve = new ProdDesignStandardApprove();
			BeanUtils.copyProperties(detail, approve);
			approve.setOperationType("invalid");
			approve.setStatus("PENDING_APPROVE");
			
			// 复制属性到草稿表对象
			ProdDesignStandardDraft draft = new ProdDesignStandardDraft();
			draft.setDetailId(detailId);
			// 首先判断草稿表中是否已经存在明细的记录，如果有，则更新，没有，则新增草稿记录
			List<ProdDesignStandardDraft> list = iProdDesignStandardDraftService.select(iRequest, draft, 1, 1000);
			if (list != null && list.size() > 0) {
				draft = list.get(0);
				draft.setOperationType("invalid");
				draft.setStatus("DRAFT");
				iProdDesignStandardDraftService.updateByPrimaryKey(iRequest, draft);
				result.add(draft);
			} else {
				BeanUtils.copyProperties(detail, draft);
				// 赋值操作类型为废止
				draft.setOperationType("invalid");
				draft.setStatus("DRAFT");
				draft = iProdDesignStandardDraftService.insertSelective(iRequest, draft);
				result.add(draft);
			}

		}
		return result;
	}

	@Override
	public List<ProdDesignStandardDraft> effective(IRequest iRequest, List<String> detailIdList) {
		// TODO Auto-generated method stub
		if (detailIdList == null || detailIdList.size() == 0) {
			throw new RuntimeException("请选择要生效的数据！");
		}

		List<ProdDesignStandardDraft> result = new ArrayList<>();
		for (String detailId : detailIdList) {
			// 获取明细数据
			ProdDesignStandardDetail detail = new ProdDesignStandardDetail();
			detail.setDetailId(detailId);
			detail = mapper.selectByPrimaryKey(detail);
			if (detail == null) {
				throw new RuntimeException("明细数据不存在，请刷新数据之后再操作！");
			}
			if (!"INVALID".equals(detail.getStatus())) {
				throw new RuntimeException("规则明细内容：" + detail.getDesignStandardDescription() + "，状态不为废止，请检查数据！");
			}

			// 复制属性到草稿表对象
			ProdDesignStandardDraft draft = new ProdDesignStandardDraft();
			draft.setDetailId(detailId);
			// 首先判断草稿表中是否已经存在明细的记录，如果有，则更新，没有，则新增草稿记录
			List<ProdDesignStandardDraft> list = iProdDesignStandardDraftService.select(iRequest, draft, 1, 1000);
			if (list != null && list.size() > 0) {
				draft = list.get(0);
				draft.setOperationType("effective");
				draft.setStatus("DRAFT");
				iProdDesignStandardDraftService.updateByPrimaryKey(iRequest, draft);
				result.add(draft);
			} else {
				BeanUtils.copyProperties(detail, draft);
				// 赋值操作类型为废止
				draft.setOperationType("effective");
				draft.setStatus("DRAFT");
				draft = iProdDesignStandardDraftService.insertSelective(iRequest, draft);
				result.add(draft);
			}
		}
		return result;
	}

}