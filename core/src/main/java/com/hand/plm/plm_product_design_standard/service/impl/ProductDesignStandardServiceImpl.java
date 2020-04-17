package com.hand.plm.plm_product_design_standard.service.impl;

import com.hand.hap.core.IRequest;
import com.hand.hap.system.service.impl.BaseServiceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hand.plm.plm_prod_design_standard_detail.dto.ProdDesignStandardDetail;
import com.hand.plm.plm_prod_design_standard_detail.service.IProdDesignStandardDetailService;
import com.hand.plm.plm_product_basic_data.dto.ProductBasicData;
import com.hand.plm.plm_product_basic_data.service.IProductBasicDataService;
import com.hand.plm.plm_product_design_standard.dto.ProductDesignStandard;
import com.hand.plm.plm_product_design_standard.mapper.ProductDesignStandardMapper;
import com.hand.plm.plm_product_design_standard.service.IProductDesignStandardService;
import com.hand.plm.product_func_attr_basic.dto.ProductFuncAttrBasic;
import com.hand.plm.product_func_attr_basic.dto.ProductFuncAttrDetail;
import com.hand.plm.product_func_attr_basic.view.TreeVO;

import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class ProductDesignStandardServiceImpl extends BaseServiceImpl<ProductDesignStandard>
		implements IProductDesignStandardService {

	@Autowired
	private ProductDesignStandardMapper mapper;
	@Autowired
	private IProdDesignStandardDetailService iProdDesignStandardDetailService;
	@Autowired
	private IProductBasicDataService iProductBasicDataService;

	@Override
	public List<TreeVO> selectTreeDatas(IRequest iRequest) {
		// TODO Auto-generated method stub
		return mapper.selectTreeDatas();
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public void renameTree(IRequest iRequest, TreeVO treeVO) {
		// TODO Auto-generated method stub
		if (treeVO == null) {
			return;
		}

		// 如果是1级树，则校验产品名字是否存在存在于产品表中plm_product_basic_data
		if ("1".equals(treeVO.getLevel())) {
			ProductBasicData product = new ProductBasicData();
			product.setProductName(treeVO.getName());
			List<ProductBasicData> productList = iProductBasicDataService.select(iRequest, product, 1, 1000);
			if (productList == null || productList.size() == 0) {
				throw new RuntimeException("产品：" + treeVO.getName() + "，在产品功能属性中不存在，请确认！");
			}
		}

		// 如果是新增树，则执行新增数据逻辑
		if ("add".equals(treeVO.getNodeType())) {
			self().addTree(iRequest, treeVO);
			return;
		}

		// 如果是顶层树不允许重命名
		if ("0".equals(treeVO.getLevel())) {
			throw new RuntimeException("顶层目录不允许重命名！");
		} else if ("1".equals(treeVO.getLevel())) {
			// 检验当前树节点下是否还有子节点
			List<ProductDesignStandard> list = mapper.getAllTreeByPid(treeVO.getId());
			if (list != null && list.size() > 1) {
				throw new RuntimeException(treeVO.getName() + "目录下存在模块属性，不允许重命名！");
			} else if (list != null && list.size() == 0) {
				// 当前节点下没有子节点，那校验当前节点是否有规则明细
				ProdDesignStandardDetail detail = new ProdDesignStandardDetail();
				detail.setDesignStandardId(treeVO.getId());
				List<ProdDesignStandardDetail> detailList = iProdDesignStandardDetailService.select(iRequest, detail, 1,
						1000);
				if (detailList != null && detailList.size() > 0) {
					throw new RuntimeException(treeVO.getName() + "目录下存在规则明细，不允许重命名！");
				}
			}
		}

		// 重命名树
		ProductDesignStandard record = new ProductDesignStandard();
		record.setKid(treeVO.getId());
		record = self().selectByPrimaryKey(iRequest, record);
		record.setName(treeVO.getName());
		record.setDescription(treeVO.getName());

		self().updateByPrimaryKey(iRequest, record);
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public void removeTree(IRequest iRequest, TreeVO treeVO) {
		// TODO Auto-generated method stub
		if (treeVO == null) {
			return;
		}

		// 如果是顶层树不允许重命名
		if ("0".equals(treeVO.getLevel())) {
			throw new RuntimeException("顶层目录不允许删除！");
		} else if ("1".equals(treeVO.getLevel())) {
			// 检验当前树节点下是否还有子节点
			List<ProductDesignStandard> list = mapper.getAllTreeByPid(treeVO.getId());
			if (list != null && list.size() > 1) {
				throw new RuntimeException(treeVO.getName() + "目录下存在模块属性，不允许删除！");
			} else if (list != null && list.size() == 0) {
				// 当前节点下没有子节点，那校验当前节点是否有规则明细
				ProdDesignStandardDetail detail = new ProdDesignStandardDetail();
				detail.setDesignStandardId(treeVO.getId());
				List<ProdDesignStandardDetail> detailList = iProdDesignStandardDetailService.select(iRequest, detail, 1,
						1000);
				if (detailList != null && detailList.size() > 0) {
					throw new RuntimeException(treeVO.getName() + "目录下存在规则明细，不允许删除！");
				}
			}
		}

		ProductDesignStandard record = new ProductDesignStandard();
		record.setKid(treeVO.getId());
		self().deleteByPrimaryKey(record);
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public TreeVO addTree(IRequest iRequest, TreeVO treeVO) {
		// TODO Auto-generated method stub
		if (treeVO == null) {
			throw new RuntimeException("新增树节点信息获取失败！");
		}

		// 往树层级目录表中新增
		ProductDesignStandard record = new ProductDesignStandard();
		record.setName(treeVO.getName());
		record.setDescription(treeVO.getName());
		record.setParentId(treeVO.getpId());
		record.setLevelNum(treeVO.getLevel());
		record = self().insert(iRequest, record);

		// 构建树结构
		TreeVO tree = new TreeVO();
		tree.setId(record.getKid());
		tree.setpId(record.getParentId());
		tree.setName(record.getName());
		tree.setLevel(record.getLevelNum());
		tree.setNodeType(record.getLevelNum());
		tree.setOpen(true);

		return tree;
	}

	/**
	 * 零件下拉框取值 ,只取第三层级的树
	 */
	@Override
	public List<ProductDesignStandard> queryrelatedParts(IRequest iRequest) {
		// TODO Auto-generated method stub
		ProductDesignStandard productDesignStandard = new ProductDesignStandard();
		productDesignStandard.setLevelNum("3");
		List<ProductDesignStandard> list = mapper.select(productDesignStandard);
		ProductDesignStandard na = new ProductDesignStandard();
		na.setKid("N/A");
		na.setName("N/A");
		na.setDescription("N/A");
		list.add(na);
		return list;
	}
}