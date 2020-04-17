package com.hand.npi.npi_technology.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Maps;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import com.hand.npi.npi_technology.dto.SparePartMenuItem;
import com.hand.npi.npi_technology.dto.TechnologySparePart;
import com.hand.npi.npi_technology.mapper.TechnologySparePartDetailsMapper;
import com.hand.npi.npi_technology.mapper.TechnologySparePartMapper;
import com.hand.npi.npi_technology.service.ITechnologySparePartService;

@Service
@Transactional(rollbackFor = Exception.class)
public class TechnologySparePartServiceImpl extends BaseServiceImpl<TechnologySparePart> implements ITechnologySparePartService{

	@Autowired
	private TechnologySparePartMapper technologySparePartMapper;
	
	@Autowired
	private TechnologySparePartDetailsMapper technologySparePartDetailsMapper;
	
	@Override
	public List<SparePartMenuItem> queryTreeData(IRequest requestContext, TechnologySparePart dto) {
		//查询根节点
		List<TechnologySparePart> list = technologySparePartMapper.queryRootData(dto);
		//查询下层数据,作为叶子添加进去
		List<SparePartMenuItem> menuItemList = castToMenuItem(list);
		return menuItemList;
	}
	
	private List<SparePartMenuItem> castToMenuItem(List<TechnologySparePart> dto) {
		// 根
		List<SparePartMenuItem> menuItemList = new ArrayList<>();
		dto.stream().forEach(sparePart -> {
			if (sparePart.getParentId() == null) {
				menuItemList.add(createMenuItem(sparePart));
			}
		});
		
		// 添加叶子
		menuItemList.stream().forEach(menuItem -> {
			additem(menuItem);
		});
		return menuItemList;
	}
	
	private SparePartMenuItem createMenuItem(TechnologySparePart sparePart) {
		SparePartMenuItem menuItem = new SparePartMenuItem();
		menuItem.setFunctionCode(sparePart.getSparePartCode());
		menuItem.setText(sparePart.getSparePartCode());
		menuItem.setType(sparePart.getSparePartCode());
		menuItem.setId(sparePart.getSparePartId().longValue());
		menuItem.setSparePartId(sparePart.getSparePartId());
		menuItem.setSparePartCode(sparePart.getSparePartCode());
		menuItem.setSparePartName(sparePart.getSparePartName());
		menuItem.setSparePartLevel(sparePart.getSparePartLevel());
		menuItem.setParentId(sparePart.getParentId());
		return menuItem;
	}
	
	private void additem(SparePartMenuItem menuItem) {
		List<SparePartMenuItem> children = new ArrayList<>();
		// 查询子
		TechnologySparePart sparePart = new TechnologySparePart();
		sparePart.setParentId(menuItem.getSparePartId());
		List<TechnologySparePart> sparePartList = technologySparePartMapper.queryLeafData(sparePart);
		// 添加子
		sparePartList.stream().forEach(item -> {
			children.add(createMenuItem(item));
		});
		// 设定子菜单
		if (children.size() > 0) {
			menuItem.setChildren(children);
			// 递归，有子 继续添加
			children.stream().forEach(childrenItem -> {
				additem(childrenItem);
			});
		}
		menuItem.setChildren(children);
	}

	
	@Override
	public List<TechnologySparePart> add(IRequest requestCtx, TechnologySparePart dto) {
		//新增和修改时组件分类编码不可以重复
		TechnologySparePart te=new TechnologySparePart();
		te.setSparePartCode(dto.getSparePartCode());
		if ("add".equals(dto.get__status())) {
			List<TechnologySparePart> select = technologySparePartMapper.select(te);
			if(select.size()>0) {
				return Collections.EMPTY_LIST;
			}
			self().insertSelective(requestCtx, dto);
		} else if ("update".equals(dto.get__status())) {
			TechnologySparePart selectDto = technologySparePartMapper.selectByPrimaryKey(dto);
			if(!dto.getSparePartCode().equals(selectDto.getSparePartCode())) {
				List<TechnologySparePart> select = technologySparePartMapper.select(te);
				if(select.size()>0) {
					return Collections.EMPTY_LIST;
				}
			}
			self().updateByPrimaryKeySelective(requestCtx, dto);
		}
		return Arrays.asList(dto);
	}

	@Override
	public Map<String, Object> deleteSparePartById(IRequest requestCtx, TechnologySparePart dto) {
		//根据主键删除组件  当前组件有子组件的时不允许删除
		//删除时3级的查询行表 1、2级查询自己的下级
		String sparePartLevel = dto.getSparePartLevel();
		Long hasChild=0L;
		Map<String, Object> resultMap=Maps.newHashMap();
		resultMap.put("isSuccess", false);
		resultMap.put("message", "未知错误,请联系管理员");
		//查询是否包含子集
		if (sparePartLevel.equals("3")) {
			hasChild = technologySparePartDetailsMapper.hasChild(dto.getSparePartId().toString());
		}else {
			hasChild = technologySparePartMapper.hasChild(dto.getSparePartId().toString());
		}
		if (hasChild>0) {
			resultMap.put("isSuccess", false);
			resultMap.put("message", "该组件含有子代组件,不可删除");
		}else {
			technologySparePartMapper.deleteByPrimaryKey(dto);
			resultMap.put("isSuccess", true);
		}
		return resultMap;
	}

}