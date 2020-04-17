package com.hand.npi.npi_technology.service.impl;

import com.github.pagehelper.PageHelper;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.service.impl.BaseServiceImpl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hand.npi.npi_technology.dto.TechnologySparePart;
import com.hand.npi.npi_technology.dto.TechnologySparePartDetails;
import com.hand.npi.npi_technology.mapper.TechnologySparePartDetailsMapper;
import com.hand.npi.npi_technology.mapper.TechnologySparePartMapper;
import com.hand.npi.npi_technology.service.ITechnologySparePartDetailsService;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class TechnologySparePartDetailsServiceImpl extends BaseServiceImpl<TechnologySparePartDetails> implements ITechnologySparePartDetailsService{

	@Autowired
	private TechnologySparePartDetailsMapper technologySparePartDetailsMapper;
	
	@Autowired
	private TechnologySparePartMapper technologySparePartMapper;
	
	@Override
	public List<TechnologySparePartDetails> queryByCondition(IRequest requestContext, TechnologySparePartDetails dto,
			int page, int pageSize) {
		List<TechnologySparePart> queryList = new ArrayList<>();
		List<Float> floatList = new ArrayList<>();
		if (dto.getSparePartId() != null && dto.getSparePartLevel() != null) {
			//说明是点击了左侧菜单进行查询
			if ("1".equals(dto.getSparePartLevel())) {
				//一级层级
				TechnologySparePart sparePartOne = new TechnologySparePart();
				sparePartOne.setSparePartId(dto.getSparePartId());
				//查询当前一级层级下的所有三级层级
				queryList = technologySparePartMapper.queryThreeLevelByOneLevel(sparePartOne);
			} else if ("2".equals(dto.getSparePartLevel())) {
				//二级层级
				TechnologySparePart sparePartTwo = new TechnologySparePart();
				sparePartTwo.setParentId(dto.getSparePartId());
				//查询当前二级层级下的所有三级层级
				queryList = technologySparePartMapper.queryLeafData(sparePartTwo);
			} else {
				//三级层级
				TechnologySparePart sparePartThree = new TechnologySparePart();
				sparePartThree.setSparePartId(dto.getSparePartId());
				queryList.add(sparePartThree);
			}
		} else {
			if (dto.getSparePartDetailsId() != null && dto.getSparePartDetailsId() != 0) {
				//存在主键，说明是单据的修改操作
				return technologySparePartDetailsMapper.queryByCondition(floatList, dto);
			} else {
				//说明是进入页面进行的查询，不带条件，查询所有数据
				return self().select(requestContext, dto, page, pageSize);
			}
		}
		if (!queryList.isEmpty()) {
			//点击的菜单下存在三级层级
			for(TechnologySparePart sparePart : queryList) {
				floatList.add(sparePart.getSparePartId());
			}
			PageHelper.startPage(page, pageSize);
			return technologySparePartDetailsMapper.queryByCondition(floatList, dto);
		} else {
			//没有三级层级，说明当前菜单下不存在数据
			return new ArrayList<>();
		}
	}

	
	@Override
	public List<TechnologySparePartDetails> add(IRequest requestCtx, TechnologySparePartDetails dto) {
		if ("add".equals(dto.get__status())) {
			//新增时 零件属性编码的生成规则 LS+六位序列号
	        //获取序列
			String codeSeq = technologySparePartDetailsMapper.getCodeSeq();
	        dto.setDetailsCode("LS"+codeSeq);
			self().insertSelective(requestCtx, dto);
		} else if ("update".equals(dto.get__status())) {
			//编辑
			self().updateByPrimaryKeySelective(requestCtx, dto);
		}
		return Arrays.asList(dto);
	}

}