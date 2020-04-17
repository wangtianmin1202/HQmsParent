package com.hand.plm.plm_product_func_attr_draft.service.impl;

import com.hand.hap.core.IRequest;
import com.hand.hap.system.service.impl.BaseServiceImpl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hand.plm.plm_product_func_attr_approve.dto.ProductFuncAttrApprove;
import com.hand.plm.plm_product_func_attr_approve.service.IProductFuncAttrApproveService;
import com.hand.plm.plm_product_func_attr_draft.dto.ProductFuncAttrDraft;
import com.hand.plm.plm_product_func_attr_draft.mapper.ProductFuncAttrDraftMapper;
import com.hand.plm.plm_product_func_attr_draft.service.IProductFuncAttrDraftService;
import com.hand.plm.product_func_attr_basic.dto.ProductFuncAttrDetail;
import com.hand.plm.product_func_attr_basic.service.IProductFuncAttrDetailService;

import org.springframework.transaction.annotation.Transactional;
import com.hand.plm.utils.Util;
import com.mchange.v2.beans.BeansUtils;

@Service
@Transactional(rollbackFor = Exception.class)
public class ProductFuncAttrDraftServiceImpl extends BaseServiceImpl<ProductFuncAttrDraft>
		implements IProductFuncAttrDraftService {
	@Autowired
	private ProductFuncAttrDraftMapper mapper;
	@Autowired
	private IProductFuncAttrApproveService iProductFuncAttrApproveService;
	@Autowired
	private IProductFuncAttrDetailService iProductFuncAttrDetailService;

	@Override
	public boolean checkTreeLevelIsExist(IRequest irequest, String product, String productFunc) {
		// TODO Auto-generated method stub
		Long count = mapper.checkTreeLevel(product, productFunc);

		return count == 0 ? false : true;
	}

	@Override
	public String checkDetailDataIsExist(IRequest irequest, String detailId) {
		// TODO Auto-generated method stub
		ProductFuncAttrDraft draft = new ProductFuncAttrDraft();
		draft.setDetailId(detailId);

		List<ProductFuncAttrDraft> list = mapper.select(draft);
		if (list != null && list.size() > 0) {
			return list.get(0).getKid();
		} else {
			return "";
		}
	}

	/**
	 * 提交审批
	 */
	@Transactional(rollbackFor = Exception.class)
	@Override
	public List<ProductFuncAttrApprove> submitApprove(IRequest irequest, List<String> kidList) {
		// TODO Auto-generated method stub
		if (kidList == null || kidList.size() == 0) {
			throw new RuntimeException("请选择草稿的数据提交！");
		}

		List<ProductFuncAttrApprove> approveList = new ArrayList<>();
		for (String kid : kidList) {
			ProductFuncAttrDraft draft = new ProductFuncAttrDraft();
			draft.setKid(kid);
			// 获取草稿表数据
			draft = self().selectByPrimaryKey(irequest, draft);
			if (draft == null) {
				throw new RuntimeException("草稿表数据不存在！");
			}

			// 校验草稿表状态
			if (!"DRAFT".equals(draft.getStatus())) {
				throw new RuntimeException(draft.getProductFuncAttr() + "在草稿表状态不是新建，不允许提交审批！");
			}

			// 如果是新增，则校验属性内容是否已经在当前属性下处女座
			if ("add".equals(draft.getOperationFlag())) {
				Long count = mapper.insertDetailCheck(draft.getProductFuncId(), draft.getProductFuncAttr());
				if (count > 0) {
					throw new RuntimeException(
							draft.getProductFunc() + "属性下，属性内容：" + draft.getProductFuncAttr() + "，已存在，请确认！");
				}
			} else {
				// 更新属性内容，校验更新前后内容是否一致，如果 一致则无需更新
				ProductFuncAttrDetail detail = new ProductFuncAttrDetail();
				detail.setDetailId(draft.getDetailId());
				detail = iProductFuncAttrDetailService.selectByPrimaryKey(irequest, detail);
				if (detail == null) {
					throw new RuntimeException(draft.getProductFuncAttr() + "获取要更新的属性内容行失败！");
				}

				if (draft.getProductFuncAttr().equals(detail.getFunctionContent())) {
					throw new RuntimeException("此次提交的变更属性内容：" + draft.getProductFuncAttr() + ",与系统中属性内容无差异，请确认，请确认！");
				}
			}

			// 新增审批数据
			ProductFuncAttrApprove approve = new ProductFuncAttrApprove();
			BeanUtils.copyProperties(draft, approve);
			approve.setKid(null);
			approve.setStatus("PENDING_APPROVE");
			// 设置第一级审批人
			approve.setCurrentApproveBy("admin");

			// 版本号逻辑，新增为-，更新要在当前的版本号上叠加，A,B,...Z,AA,BB,..ZZ,最大ZZ
			if ("add".equals(draft.getOperationFlag())) {
				// 新增的第一版数据，版本号为-
				approve.setVersion("-");
			} else {
				// 获取变更明细当前版本号
				String version = mapper.getDetailVersion(draft.getDetailId());
				// 当前版本是两位，AA,BB这种
				if (version.length() > 1) {
					// 如果当前版本号已经是最大，ZZ,则不变更版本号
					if ("ZZ".equals(version)) {
						approve.setVersion(version);
					} else {
						// 截取版本号第一个字符
						version = version.substring(0, 1);
						// 然后将指定的26进制表示转换为自然数
						int num = Util.fromNumberSystem26(version);
						// 计算下一个版本号
						String s = Util.toNumberSystem26(num + 1);
						// 拼接版本号为AA,BB形式
						approve.setVersion(s.concat(s));
					}
				} else {
					// 当前版本还是A,B,C,D，并未达到2位AA，BB

					// 将指定的26进制表示转换为自然数
					int num = Util.fromNumberSystem26(version);
					// 计算下一个版本号
					String s = Util.toNumberSystem26(num + 1);
					approve.setVersion(s);
				}
			}

			String changeNum = mapper.getChangeNum();
			// 变更号逻辑待定
			approve.setChangeNum(changeNum);
			// 新增审批表数据
			approve = iProductFuncAttrApproveService.insertSelective(irequest, approve);
			// 删除草稿表数据
			self().deleteByPrimaryKey(draft);
			approveList.add(approve);
		}
		return approveList;
	}

}