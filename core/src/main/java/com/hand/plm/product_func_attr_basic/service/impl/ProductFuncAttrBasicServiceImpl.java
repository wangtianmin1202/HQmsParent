package com.hand.plm.product_func_attr_basic.service.impl;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hand.hap.core.IRequest;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import com.hand.plm.plm_product_basic_data.dto.ProductBasicData;
import com.hand.plm.plm_product_basic_data.service.IProductBasicDataService;
import com.hand.plm.product_func_attr_basic.dto.ProductFuncAttrBasic;
import com.hand.plm.product_func_attr_basic.dto.ProductFuncAttrDetail;
import com.hand.plm.product_func_attr_basic.mapper.ProductFuncAttrBasicMapper;
import com.hand.plm.product_func_attr_basic.mapper.ProductFuncAttrDetailMapper;
import com.hand.plm.product_func_attr_basic.service.IProductFuncAttrBasicService;
import com.hand.plm.product_func_attr_basic.view.TreeVO;

@Service
public class ProductFuncAttrBasicServiceImpl extends BaseServiceImpl<ProductFuncAttrBasic>
		implements IProductFuncAttrBasicService {
	@Autowired
	private ProductFuncAttrBasicMapper mapper;
	@Autowired
	private ProductFuncAttrDetailMapper productFuncAttrDetailMapper;
	@Autowired
	private IProductBasicDataService iProductBasicDataService;

	@Override
	public List<TreeVO> selectTreeDatas(IRequest iRequest) {
		// TODO Auto-generated method stub
		return mapper.selectTreeDatas();
	}

	@Override
	public List<TreeVO> selectTreeDatasByParms(IRequest iRequest, String param) {
		// TODO Auto-generated method stub
		return mapper.selectTreeDatasByParms(param);
	}

	@Override
	public List<ProductFuncAttrBasic> queryLevelNum(IRequest iRequest, ProductFuncAttrBasic dto) {
		System.out.println(dto.getLevelNum());
		// TODO Auto-generated method stub
		return mapper.select(dto);
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public void renameTree(IRequest iRequest, TreeVO treeVO) {
		// TODO Auto-generated method stub
		if (treeVO == null) {
			return;
		}
		if ("0".equals(treeVO.getLevel())) {
			throw new RuntimeException("顶层目录不允许重命名！");
		} else if ("1".equals(treeVO.getLevel())) {
			ProductFuncAttrBasic vo = new ProductFuncAttrBasic();
			vo.setParentId(treeVO.getId());
			List<ProductFuncAttrBasic> list = mapper.select(vo);
			if (list != null && list.size() > 0) {
				throw new RuntimeException(treeVO.getName() + "目录下存在属性，不允许重命名！");
			}
		} else {
			ProductFuncAttrDetail detail = new ProductFuncAttrDetail();
			detail.setKid(treeVO.getId());
			List<ProductFuncAttrDetail> list = productFuncAttrDetailMapper.select(detail);
			if (list != null && list.size() > 0) {
				throw new RuntimeException(treeVO.getName() + "目录下存在属性内容，不允许重命名！");
			}
		}
		ProductFuncAttrBasic record = new ProductFuncAttrBasic();
		record.setKid(treeVO.getId());
		record = self().selectByPrimaryKey(iRequest, record);
		record.setName(treeVO.getName());
		record.setDescription(treeVO.getName());

		self().updateByPrimaryKey(iRequest, record);

		// 更新产品品类基础数据表信息
		if ("1".equals(treeVO.getLevel())) {
			ProductBasicData product = new ProductBasicData();
			product.setProductId(record.getProductId());
			product = iProductBasicDataService.selectByPrimaryKey(iRequest, product);

			product.setProductName(treeVO.getName());
			product.setProductDescription(treeVO.getName());
			iProductBasicDataService.updateByPrimaryKey(iRequest, product);
		}
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public void removeTree(IRequest iRequest, TreeVO treeVO) {
		// TODO Auto-generated method stub
		if (treeVO == null) {
			return;
		}
		if ("0".equals(treeVO.getNodeType())) {
			throw new RuntimeException("顶层目录不允许重删除！");
		} else if ("1".equals(treeVO.getNodeType())) {
			ProductFuncAttrBasic vo = new ProductFuncAttrBasic();
			vo.setParentId(treeVO.getId());
			List<ProductFuncAttrBasic> list = mapper.select(vo);
			if (list != null && list.size() > 0) {
				throw new RuntimeException(treeVO.getName() + "目录下存在属性，不允许删除！");
			}
		} else {
			ProductFuncAttrDetail detail = new ProductFuncAttrDetail();
			detail.setKid(treeVO.getId());
			List<ProductFuncAttrDetail> list = productFuncAttrDetailMapper.select(detail);
			if (list != null && list.size() > 0) {
				throw new RuntimeException(treeVO.getName() + "目录下存在属性内容，不允许删除！");
			}
		}

		ProductFuncAttrBasic record = new ProductFuncAttrBasic();
		record.setKid(treeVO.getId());
		self().deleteByPrimaryKey(record);

		// 删除产品品类基础数据表信息
		if ("1".equals(treeVO.getLevel())) {
			ProductBasicData product = new ProductBasicData();
			product.setProductId(record.getProductId());
			product = iProductBasicDataService.selectByPrimaryKey(iRequest, product);

			iProductBasicDataService.deleteByPrimaryKey(product);
		}
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public TreeVO addTree(IRequest iRequest, TreeVO treeVO) {
		// TODO Auto-generated method stub
		if (treeVO == null) {
			throw new RuntimeException("新增树节点信息获取失败！");
		}

		// 如果是一级树，则是产品，因此新增产品基础表数据
		String productId = "";
		if ("1".equals(treeVO.getLevel())) {
			ProductBasicData product = new ProductBasicData();
			product.setProductName(treeVO.getName());
			product.setProductDescription(treeVO.getName());
			product = iProductBasicDataService.insertSelective(iRequest, product);
			productId = product.getProductId();
		}

		// 往树层级目录表中新增
		ProductFuncAttrBasic record = new ProductFuncAttrBasic();
		record.setName(treeVO.getName());
		record.setDescription(treeVO.getName());
		record.setParentId(treeVO.getpId());
		record.setLevelNum(treeVO.getLevel());
		record.setProductId(productId);
		record = self().insert(iRequest, record);

		// 构建树结构
		TreeVO tree = new TreeVO();
		tree.setId(record.getKid());
		tree.setpId(record.getParentId());
		tree.setName(record.getName());
		tree.setLevel(record.getLevelNum());
		tree.setNodeType(record.getLevelNum());
		tree.setOpen(true);

		if ("1".equals(treeVO.getLevel())) {
			tree.setId(productId);
		}

		return tree;
	}

}
